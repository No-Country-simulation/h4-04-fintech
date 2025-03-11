package com.practice.Portfolio.repository;

import com.practice.Instrument.model.InstrumentModel;
import com.practice.Portfolio.model.PortfolioModel;
import com.practice.User.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioModel, Long> {
    List<PortfolioModel> findByUserId(Long userId);
    Optional<PortfolioModel> findByUserAndInstrument(UserModel user, InstrumentModel instrument);

}
