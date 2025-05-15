package ru.mslotvi.rest.exchange;

import ru.mslotvi.data.StoragePortfolio;

import java.util.Map;

public record PortfolioDto(
       Map<String, Double> securities,
       double expectedReturn,
       double risk
) {

    public static PortfolioDto from(StoragePortfolio portfolio) {
        return new PortfolioDto(portfolio.securities(), portfolio.expectedReturn(), portfolio.risk());
    }

}
