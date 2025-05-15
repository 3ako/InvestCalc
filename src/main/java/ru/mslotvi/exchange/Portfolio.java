package ru.mslotvi.exchange;

import ru.mslotvi.data.StoragePortfolio;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class Portfolio implements DefaultPortfolio {
    private final Map<ExchangeSecuritie, Double> weights;
    private final double expectedReturn;
    private final double risk;

    public Portfolio(Map<ExchangeSecuritie, Double> weights, double expectedReturn, double risk) {
        this.weights = weights;
        this.expectedReturn = expectedReturn;
        this.risk = risk;
    }

    public Map<ExchangeSecuritie, Double> getWeights() {
        return weights;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public double getRisk() {
        return risk;
    }

    public StoragePortfolio toStoragePortfolio() {
        return new StoragePortfolio().securities(weights.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().secId(),
                Map.Entry::getValue
        )))
                .risk(risk)
                .createDate(Instant.now())
                .expectedReturn(expectedReturn);
    }

    public void printPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<ExchangeSecuritie, Double> entry : weights.entrySet()) {
            System.out.println(entry.getKey().shortName() + ": " + entry.getValue());
        }
        System.out.println("Expected Return: " + expectedReturn);
        System.out.println("Risk (Standard Deviation): " + risk);
    }

    @Override
    public Map<String, Double> weights() {
        return weights.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().secId(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public double expectedReturn() {
        return expectedReturn;
    }

    @Override
    public double risk() {
        return risk;
    }
}