package com.practice.Portfolio.model;

import com.practice.Instrument.model.InstrumentModel;
import com.practice.Transactions.model.TransactionModel;
import com.practice.User.model.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "portfolio")
public class PortfolioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @Column(name = "purchase_price")
    private Double purchasePrice;
    @Column(name = "purchase_date")
    private LocalDate purchaseDate = LocalDate.now();

    @OneToMany(mappedBy = "portfolio", targetEntity = TransactionModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransactionModel> transactionModels = new ArrayList<>();

    @ManyToOne(targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(targetEntity = InstrumentModel.class)
    @JoinColumn(name = "instrument_id")
    private InstrumentModel instrument;

}
