package ru.mslotvi.exchange;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeSecuritie {
    String shortName();
    String secId();
    List<ExchangeDateSnapshot> lastLoadMarketHistory();
    List<ExchangeDateSnapshot> loadMarketHistory(LocalDate from, LocalDate to);
    double calculateDeviation();
    double calculateExpectedReturn();
}
