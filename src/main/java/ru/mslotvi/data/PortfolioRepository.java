package ru.mslotvi.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<StoragePortfolio, Long> {

    List<StoragePortfolio> findByCreateDateBetween(Instant startDate, Instant endDate);

}
