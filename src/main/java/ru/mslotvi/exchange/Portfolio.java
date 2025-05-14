package ru.mslotvi.exchange;

import java.util.Map;

public class Portfolio {
    private final Map<ExchangeSecuritie, Double> weights;
    private final double expectedReturn;
    private final double risk;

    // Конструктор
    public Portfolio(Map<ExchangeSecuritie, Double> weights, double expectedReturn, double risk) {
        this.weights = weights;
        this.expectedReturn = expectedReturn;
        this.risk = risk;
    }

    // Получить веса активов
    public Map<ExchangeSecuritie, Double> getWeights() {
        return weights;
    }

    // Получить ожидаемую доходность
    public double getExpectedReturn() {
        return expectedReturn;
    }

    // Получить риск (стандартное отклонение)
    public double getRisk() {
        return risk;
    }

    // Вывод данных о портфеле
    public void printPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<ExchangeSecuritie, Double> entry : weights.entrySet()) {
            System.out.println(entry.getKey().shortName() + ": " + entry.getValue());
        }
        System.out.println("Expected Return: " + expectedReturn);
        System.out.println("Risk (Standard Deviation): " + risk);
    }
}