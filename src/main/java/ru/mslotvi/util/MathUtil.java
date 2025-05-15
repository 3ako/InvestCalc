package ru.mslotvi.util;

import lombok.experimental.UtilityClass;
import ru.mslotvi.exchange.DefaultPortfolio;
import ru.mslotvi.exchange.ExchangeDateSnapshot;
import ru.mslotvi.exchange.ExchangeSecuritie;
import ru.mslotvi.exchange.ExchangeTradeRecord;

import java.util.*;

@UtilityClass
public class MathUtil {

    /**
     * Средняя доходность
     * @param closes цены закрытия торгов
     * @return средняя доходность
     */
    public double averageYield(double[] closes) {
        double sum = 0;
        for (double close : closes) {
            sum += close;
        }
        return sum / closes.length;
    }

    /**
     * Метод для округления числа до заданного количества знаков после запятой.
     *
     * @param value Число, которое нужно округлить.
     * @param precision Количество знаков после запятой, до которых нужно округлить.
     * @return Округленное значение.
     */
    public double roundToPrecision(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
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
    public <T extends DefaultPortfolio> List<T> calculateEfficientFrontier(List<T> portfolios, int precision) {
        Map<Double, T> efficientPortfolios = new HashMap<>();

        for (T portfolio : portfolios) {
            double portfolioRisk = portfolio.risk();
            double portfolioReturn = portfolio.expectedReturn();

            double roundedRisk = roundToPrecision(portfolioRisk, precision);

            if (!efficientPortfolios.containsKey(roundedRisk) ||
                    efficientPortfolios.get(roundedRisk).expectedReturn() < portfolioReturn) {
                efficientPortfolios.put(roundedRisk, portfolio);
            }
        }

        return new ArrayList<>(efficientPortfolios.values());
    }

    /**
     * Рассчитывает стандартное отклонение для списка объектов {@link ExchangeDateSnapshot}, содержащих записи о торговых сделках.
     * Стандартное отклонение измеряет, насколько значения отклоняются от среднего.
     *
     * <p>Метод сначала извлекает цены закрытия из всех торговых записей всех {@link ExchangeDateSnapshot} в списке.
     * Затем рассчитывается среднее значение (средняя доходность), после чего вычисляется сумма квадратов отклонений
     * от среднего для каждой цены закрытия. Итоговое стандартное отклонение вычисляется по формуле для выборки.
     *
     * @param snapshots Список объектов {@link ExchangeDateSnapshot}, содержащих данные о сделках с ценами закрытия.
     * @return Стандартное отклонение цен закрытия на основе переданных данных.
     * @throws IllegalArgumentException Если список {@code snapshots} пуст.
     */
    public double calculateStandardDeviation(List<ExchangeDateSnapshot> snapshots) {
        int n = snapshots.stream().mapToInt(s -> s.tradeRecords().size()).sum();
        double[] closes = new double[n];
        int i = 0;
        for (ExchangeDateSnapshot snapshot : snapshots) {
            for (ExchangeTradeRecord tradeRecord: snapshot.tradeRecords()) {
                var close = tradeRecord.close();
                if (close != null) {
                    closes[i] += close;
                }
                i++;
            }
        }
        double mean = averageYield(closes);
        double sumOfSquaredDifferences = 0;
        for (double close : closes) {
            double difference = close - mean;
            sumOfSquaredDifferences += difference * difference;
        }

        return Math.sqrt(sumOfSquaredDifferences / (n - 1));
    }
    public record CovariantCompanyModel(ExchangeSecuritie company1, ExchangeSecuritie company2, double covariance) {
    }


    public double calculateExpectedReturn(ExchangeSecuritie company) {
        List<ExchangeDateSnapshot> snapshots = company.lastLoadMarketHistory();
        double sumReturns = 0;
        int validRecordsCount = 0;  // Считаем количество валидных записей

        for (ExchangeDateSnapshot snapshot : snapshots) {
            for (ExchangeTradeRecord tradeRecord : snapshot.tradeRecords()) {
                Double closePrice = tradeRecord.close();  // Получаем цену закрытия
                if (closePrice != null) {  // Проверяем, не равна ли цена закрытия null
                    sumReturns += closePrice;
                    validRecordsCount++;
                }
            }
        }

        if (validRecordsCount > 0) {
            return sumReturns / validRecordsCount;
        } else {
            return 0;  // Если нет валидных данных, возвращаем 0
        }
    }



    /**
     * Вычисляет ковариантность между двумя компаниями на основе их исторических данных,
     * учитывая только общие дни торговли.
     *
     * @param company1 Первая компания.
     * @param company2 Вторая компания.
     * @return Модель с ковариантностью между двумя компаниями.
     */
    public CovariantCompanyModel computeCovariance(ExchangeSecuritie company1, ExchangeSecuritie company2) {
        List<ExchangeDateSnapshot> snapshots1 = company1.lastLoadMarketHistory();
        List<ExchangeDateSnapshot> snapshots2 = company2.lastLoadMarketHistory();

        Map<Date, Double> company1ClosingPrices = extractClosingPricesByDate(snapshots1);
        Map<Date, Double> company2ClosingPrices = extractClosingPricesByDate(snapshots2);

        Set<Date> commonTradeDays = new HashSet<>(company1ClosingPrices.keySet());
        commonTradeDays.retainAll(company2ClosingPrices.keySet());  // Оставляем только общие дни

        if (commonTradeDays.isEmpty()) {
            throw new IllegalArgumentException("Нет общих дней торговли между компаниями.");
        }

        List<Double> closes1 = new ArrayList<>();
        List<Double> closes2 = new ArrayList<>();

        for (Date tradeDay : commonTradeDays) {
            closes1.add(company1ClosingPrices.get(tradeDay));
            closes2.add(company2ClosingPrices.get(tradeDay));
        }

        double mean1 = calculateMean(closes1);
        double mean2 = calculateMean(closes2);

        double covariance = 0.0;
        for (int i = 0; i < closes1.size(); i++) {
            double deviation1 = closes1.get(i) - mean1;
            double deviation2 = closes2.get(i) - mean2;
            covariance += deviation1 * deviation2;
        }

        covariance /= (closes1.size() - 1); // Для выборки, делим на (n-1)
        return new CovariantCompanyModel(company1, company2, covariance);
    }

    /**
     * Извлекает все цены закрытия по дням из списка исторических данных для компании.
     *
     * @param snapshots Список исторических данных.
     * @return Карта с датами и соответствующими ценами закрытия.
     */
    private static Map<Date, Double> extractClosingPricesByDate(List<ExchangeDateSnapshot> snapshots) {
        Map<Date, Double> closingPricesByDate = new HashMap<>();
        for (ExchangeDateSnapshot snapshot : snapshots) {
            for (ExchangeTradeRecord tradeRecord : snapshot.tradeRecords()) {
                closingPricesByDate.put(tradeRecord.tradeDate(), tradeRecord.close());
            }
        }
        return closingPricesByDate;
    }




    /**
     * Рассчитывает среднее значение из списка чисел.
     *
     * @param values Список чисел.
     * @return Среднее значение.
     */
    private static double calculateMean(List<Double> values) {
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

}