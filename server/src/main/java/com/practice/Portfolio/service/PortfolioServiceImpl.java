package com.practice.Portfolio.service;

import com.practice.Instrument.model.InstrumentModel;
import com.practice.Instrument.repository.InstrumentRepository;
import com.practice.Instrument.service.InstrumentService;
import com.practice.Portfolio.dtoRequest.InvestmentRequestDto;
import com.practice.Portfolio.dtoRequest.PortfolioRequestDto;
import com.practice.Portfolio.dtoResponse.PortfolioListResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioPageResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioValueResponseDto;
import com.practice.Portfolio.model.PortfolioModel;
import com.practice.Portfolio.repository.PortfolioRepository;
import com.practice.Transactions.Enum.EnumTransactionType;
import com.practice.Transactions.model.TransactionModel;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import com.practice.exceptions.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final InstrumentService instrumentService;
    private final InstrumentRepository instrumentRepository;

    private static final String PORTFOLIO_NOT_FOUND = "El portafolio con ID %d no fue encontrado";
    private static final String USER_NOT_FOUND = "El usuario con ID %d no fue encontrado";

    @Override
    public PortfolioPageResponseDto findAllPortfolio(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PortfolioModel> portfolioPage = portfolioRepository.findAll(pageable);

        List<PortfolioResponseDto> portfolioDtos = portfolioPage.getContent()
                .stream()
                .map(portfolio -> modelMapper.map(portfolio, PortfolioResponseDto.class))
                .collect(Collectors.toList());

        return new PortfolioPageResponseDto(
                portfolioDtos,
                portfolioPage.getTotalPages(),
                portfolioPage.getTotalElements()
        );
    }

    @Override
    public PortfolioResponseDto findPortfolioById(Long id) {
        PortfolioModel portfolioModel = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(String.format(PORTFOLIO_NOT_FOUND, id)));
        return modelMapper.map(portfolioModel, PortfolioResponseDto.class);
    }

    @Override
    public PortfolioResponseDto savePortfolio(@Valid PortfolioRequestDto portfolioRequest) {

       UserModel user = userRepository.findById(portfolioRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, portfolioRequest.getUserId())));


        PortfolioModel portfolio = modelMapper.map(portfolioRequest, PortfolioModel.class);
        portfolio.setUser(user);

        PortfolioModel savedPortfolio = portfolioRepository.save(portfolio);

        return modelMapper.map(savedPortfolio, PortfolioResponseDto.class);
    }

    @Override
    public PortfolioResponseDto updatePortfolio(Long id, @Valid PortfolioRequestDto portfolioRequest) {
        PortfolioModel portfolioModel = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(String.format(PORTFOLIO_NOT_FOUND, id)));

        modelMapper.map(portfolioRequest, portfolioModel);

        if (portfolioRequest.getUserId() != null) {
            UserModel user = userRepository.findById(portfolioRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, portfolioRequest.getUserId())));
            portfolioModel.setUser(user);
        }

        PortfolioModel savedPortfolio = portfolioRepository.save(portfolioModel);

        return modelMapper.map(savedPortfolio, PortfolioResponseDto.class);
    }

    @Override
    public void deletePortfolio(Long id) {
        PortfolioModel portfolioModel = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(String.format(PORTFOLIO_NOT_FOUND, id)));
        portfolioRepository.delete(portfolioModel);
    }

    @Override
    public PortfolioValueResponseDto calculateTotalValue(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));

        List<PortfolioModel> portfolios = portfolioRepository.findByUserId(userId);
        double totalValue = portfolios.stream()
                .mapToDouble(portfolio -> {
                    double currentPrice = instrumentService.getCurrentPrice(portfolio.getInstrument().getId());
                    return portfolio.getQuantity() * currentPrice;
                })
                .sum();

        return new PortfolioValueResponseDto(userId, totalValue);
    }

    @Override
    public PortfolioListResponseDto findPortfoliosByUserId(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));

        List<PortfolioModel> portfolios = portfolioRepository.findByUserId(userId);
        List<PortfolioResponseDto> portfolioDtos = portfolios.stream()
                .map(portfolio -> modelMapper.map(portfolio, PortfolioResponseDto.class))
                .collect(Collectors.toList());

        return new PortfolioListResponseDto(portfolioDtos);
    }

    @Override
    public PortfolioResponseDto addOrUpdateInvestment(Long userId, @Valid InvestmentRequestDto investmentRequest) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));

        InstrumentModel instrument = instrumentRepository.findById(investmentRequest.getInstrumentId())
                .orElseThrow(() -> new InstrumentNotFoundException(
                        String.format("El instrumento con ID %d no fue encontrado", investmentRequest.getInstrumentId())));

        PortfolioModel portfolio = portfolioRepository
                .findByUserAndInstrument(user, instrument)
                .orElse(new PortfolioModel());

        if (portfolio.getId() == null) {
            portfolio.setUser(user);
            portfolio.setInstrument(instrument);
            portfolio.setQuantity(0);
            portfolio.setPurchasePrice(0.0);
            portfolio.setPurchaseDate(LocalDate.now());
            portfolio.setTransactionModels(new ArrayList<>());
        }

        // Actualizar cantidad y precio promedio según el tipo de transacción
        int quantityChange = investmentRequest.getTransactionType() == EnumTransactionType.COMPRA ?
                investmentRequest.getQuantity() : -investmentRequest.getQuantity();

        // Validar que haya suficientes instrumentos para vender
        if (investmentRequest.getTransactionType() == EnumTransactionType.VENTA &&
                portfolio.getQuantity() < investmentRequest.getQuantity()) {
            throw new InsufficientInstrumentsException(
                    String.format("No hay suficientes instrumentos para vender. Disponible: %d, Solicitado: %d",
                            portfolio.getQuantity(), investmentRequest.getQuantity()));
        }

        int newQuantity = portfolio.getQuantity() + quantityChange;

        // Solo actualizar precio promedio en compras
        double newPurchasePrice = investmentRequest.getTransactionType() == EnumTransactionType.COMPRA ?
                calculateNewAveragePrice(
                        portfolio.getQuantity(),
                        portfolio.getPurchasePrice(),
                        investmentRequest.getQuantity(),
                        investmentRequest.getUnitPrice()
                ) : portfolio.getPurchasePrice();

        // Crear nueva transacción
        TransactionModel transaction = new TransactionModel();
        transaction.setPortfolio(portfolio);
        transaction.setQuantity(investmentRequest.getQuantity());
        transaction.setUnitPrice(investmentRequest.getUnitPrice());
        transaction.setCommission(investmentRequest.getCommission());
        transaction.setEnumTransactionType(investmentRequest.getTransactionType());
        transaction.setOperationDate(LocalDate.now());

        portfolio.setQuantity(newQuantity);
        portfolio.setPurchasePrice(newPurchasePrice);
        portfolio.getTransactionModels().add(transaction);

        PortfolioModel savedPortfolio = portfolioRepository.save(portfolio);
        return modelMapper.map(savedPortfolio, PortfolioResponseDto.class);
    }


    @Override
    public void deleteInvestment(Long userId, Long instrumentId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));

        InstrumentModel instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException(
                        String.format("El instrumento con ID %d no fue encontrado", instrumentId)));

        PortfolioModel portfolio = portfolioRepository
                .findByUserAndInstrument(user, instrument)
                .orElseThrow(() -> new PortfolioNotFoundException(
                        String.format("La inversión para el usuario %d e instrumento %d no fue encontrada",
                                userId, instrumentId)));

        // Verificar si hay transacciones pendientes antes de eliminar
        if (!portfolio.getTransactionModels().isEmpty()) {
            throw new PortfolioHasTransactionsException(
                    String.format("No se puede eliminar el portafolio ID %d porque tiene transacciones asociadas",
                            portfolio.getId()));
        }

        portfolioRepository.delete(portfolio);
    }

    private double calculateNewAveragePrice(int oldQuantity, double oldPrice, int addedQuantity, double newPrice) {
        // Si es la primera compra, retornar el nuevo precio directamente
        if (oldQuantity == 0) {
            return newPrice;
        }

        // Calcular el valor total de la posición anterior
        double oldPosition = oldQuantity * oldPrice;

        // Calcular el valor de la nueva compra
        double newPosition = addedQuantity * newPrice;

        // Calcular la cantidad total de instrumentos
        int totalQuantity = oldQuantity + addedQuantity;

        // Calcular el nuevo precio promedio: (valor total) / (cantidad total)
        return (oldPosition + newPosition) / totalQuantity;
    }
}