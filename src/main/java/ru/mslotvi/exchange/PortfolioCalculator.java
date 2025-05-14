package ru.mslotvi.exchange;

import lombok.Getter;
import ru.mslotvi.util.MathUtil;

import java.util.*;

@Getter
public class PortfolioCalculator {

    private final List<ExchangeSecuritie> companies;
    private final List<Portfolio> portfolios = new ArrayList<>();

    public PortfolioCalculator(List<ExchangeSecuritie> companies) {
        this.companies = companies;
    }
    /**
     * Метод для вычисления эффективной линии на основе существующих портфелей.
     * Эффективная линия представляет собой список портфелей, которые являются
     * оптимальными для разных уровней риска.
     *
     * Для каждого уровня риска выбирается портфель с максимальной доходностью.
     * Риск округляется до указанного количества знаков после запятой.
     *
     * @param precision Количество знаков после запятой, до которых следует округлять риск.
     * @return Список портфелей, которые составляют эффективную линию.
     */
    public List<Portfolio> calculateEfficientFrontier(int precision) {
        Map<Double, Portfolio> efficientPortfolios = new HashMap<>();

        // Проходим по всем портфелям
        for (Portfolio portfolio : portfolios) {
            double portfolioRisk = portfolio.getRisk();
            double portfolioReturn = portfolio.getExpectedReturn();

            // Округляем риск до заданного числа знаков после запятой
            double roundedRisk = roundToPrecision(portfolioRisk, precision);

            // Если для этого уровня риска еще нет портфеля, или текущий портфель дает большую доходность
            // для данного уровня риска, заменяем портфель
            if (!efficientPortfolios.containsKey(roundedRisk) ||
                    efficientPortfolios.get(roundedRisk).getExpectedReturn() < portfolioReturn) {
                efficientPortfolios.put(roundedRisk, portfolio);
            }
        }

        // Возвращаем список портфелей, которые составляют эффективную линию
        return new ArrayList<>(efficientPortfolios.values());
    }

    /**
     * Метод для округления числа до заданного количества знаков после запятой.
     *
     * @param value Число, которое нужно округлить.
     * @param precision Количество знаков после запятой, до которых нужно округлить.
     * @return Округленное значение.
     */
    private double roundToPrecision(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }

    public List<Portfolio> getPortfolios() {
        return Collections.unmodifiableList(portfolios);
    }

    /**
     * Генерирует заданное количество случайных портфелей и добавляет их в список {@link List<Portfolio>}.
     *
     * <p>Метод вызывает {@link #generateRandomPortfolio()} для генерации случайного портфеля и добавляет его
     * в список портфелей {@link  List<Portfolio>}. Количество портфелей, которое нужно создать, передается через
     * параметр <code>amount</code>.</p>
     *
     * <p>Этот метод полезен для создания множества случайных портфелей, которые могут быть использованы для анализа
     * различных комбинаций активов и их рисков/доходностей.</p>
     *
     * <p>Пример использования:</p>
     * <pre>
     *  {@code
     * generatePortfolios(100);  // Генерирует 100 случайных портфелей
     * System.out.println("Количество сгенерированных портфелей: " + portfolios.size());
     * }
     * </pre>
     *
     * @param amount Количество портфелей, которые нужно сгенерировать.
     */
    public void generatePortfolios(int amount) {
        for (int i = 0; i < amount; i++) {
            Portfolio portfolio = generateRandomPortfolio();
            portfolios.add(portfolio);
        }
    }

    public void clearPortfolios() {
        portfolios.clear();
    }


    /**
     * Генерирует случайный портфель с нормализованными весами для активов,
     * рассчитывает его ожидаемую доходность и риск (стандартное отклонение).
     *
     * <p>Метод выполняет следующие шаги:</p>
     * <ol>
     *     <li>Вычисляет ожидаемую доходность для каждого актива (компании).</li>
     *     <li>Создает ковариационную матрицу для всех активов (отражает взаимозависимость доходности между активами).</li>
     *     <li>Инициализирует случайные веса для активов.</li>
     *     <li>Нормализует веса так, чтобы их сумма была равна 1 (для формирования корректного портфеля).</li>
     *     <li>Вычисляет ожидаемую доходность портфеля как взвешенную сумму доходностей активов.</li>
     *     <li>Вычисляет риск портфеля, используя ковариационную матрицу и веса активов.</li>
     *     <li>Создает объект {@link Portfolio}, который содержит веса активов, доходность и риск портфеля.</li>
     * </ol>
     *
     * <p>Пример использования:</p>
     * <pre>
     * {@code
     * Portfolio randomPortfolio = generateRandomPortfolio();
     * System.out.println("Ожидаемая доходность: " + randomPortfolio.getExpectedReturn());
     * System.out.println("Риск портфеля: " + randomPortfolio.getRisk());
     * }
     * </pre>
     *
     * @return объект {@link Portfolio}, который содержит:
     *         <ul>
     *             <li>Список весов активов в портфеле.</li>
     *             <li>Ожидаемую доходность портфеля.</li>
     *             <li>Риск (стандартное отклонение) портфеля.</li>
     *         </ul>
     */
    private Portfolio generateRandomPortfolio() {
        double[] expectedReturnsArray = new double[companies.size()];
        for (int i = 0; i < companies.size(); i++) {
            expectedReturnsArray[i] = companies.get(i).calculateExpectedReturn();
        }

        double[][] covarianceMatrix = new double[companies.size()][companies.size()];
        for (int i = 0; i < companies.size(); i++) {
            for (int j = 0; j < companies.size(); j++) {
                covarianceMatrix[i][j] = MathUtil.computeCovariance(companies.get(i), companies.get(j)).covariance();
            }
        }

        double[] weights = new double[companies.size()];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }

        double sumWeights = Arrays.stream(weights).sum();
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sumWeights;
        }

        double portfolioReturn = 0.0;
        for (int i = 0; i < weights.length; i++) {
            portfolioReturn += weights[i] * expectedReturnsArray[i];
        }

        double portfolioRisk = 0.0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights.length; j++) {
                portfolioRisk += weights[i] * weights[j] * covarianceMatrix[i][j];
            }
        }
        portfolioRisk = Math.sqrt(portfolioRisk);

        Map<ExchangeSecuritie, Double> weightMap = new HashMap<>();
        for (int i = 0; i < companies.size(); i++) {
            weightMap.put(companies.get(i), weights[i]);
        }

        return new Portfolio(weightMap, portfolioReturn, portfolioRisk);
    }
}
