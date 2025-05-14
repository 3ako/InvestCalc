package ru.mslotvi.exchange;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Exchange {

    CompletableFuture<Void> loadData();
    List<ExchangeBoard> getBoards();
    Map<String, ExchangeSecuritie> getSecurities();

    PortfolioCalculator getEfficientFrontier(Set<String> ids, LocalDate start, LocalDate end);
}
