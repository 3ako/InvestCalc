package ru.mslotvi.exchange;

import java.util.Map;

public class Portfolio {
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

    public void printPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<ExchangeSecuritie, Double> entry : weights.entrySet()) {
            System.out.println(entry.getKey().shortName() + ": " + entry.getValue());
        }
        System.out.println("Expected Return: " + expectedReturn);
        System.out.println("Risk (Standard Deviation): " + risk);
    }
}