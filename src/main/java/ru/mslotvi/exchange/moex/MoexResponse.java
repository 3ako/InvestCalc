package ru.mslotvi.exchange.moex;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.mslotvi.exchange.ExchangeDateSnapshot;
import ru.mslotvi.exchange.ExchangeTradeRecord;

import java.util.Date;
import java.util.List;
/**
 * Представляет ответ от MOEX с данными о сделках на бирже.
 * <p>Этот класс реализует интерфейс {@link ExchangeDateSnapshot} и содержит данные, полученные от MOEX,
 * такие как истории сделок, метаданные, курсы и другие показатели.</p>
 *
 * <p>Включает информацию о сделках в виде списка {@link TradeRecord}, а также метаданные для работы с историей сделок.</p>
 *
 * @see ExchangeDateSnapshot
 * @see TradeRecord
 */
@Data
@Accessors(fluent = true)
public class MoexResponse implements ExchangeDateSnapshot {

    private History history;
    private HistoryCursor historyCursor;

    /**
     * Возвращает список записей о сделках для данного ответа.
     *
     * @return Список {@link TradeRecord}, представляющих сделки для данного ответа.
     */
    @Override
    public List<? extends ExchangeTradeRecord> tradeRecords() {
        return history.data;
    }

    /**
     * Вложенный класс, представляющий историю сделок.
     * <p>Этот класс содержит метаданные истории сделок, информацию о колонках данных
     * и саму информацию о сделках в виде списка {@link TradeRecord}.</p>
     */
    @Data
    @Accessors(fluent = true)
    public static class History {
        private Metadata metadata;
        private List<String> columns;
        private List<TradeRecord> data;
    }

    /**
     * Вложенный класс, представляющий метаданные для каждой сделки.
     * <p>Метаданные включают в себя различные поля, такие как идентификатор доски, дата сделки, типы сделок,
     * цены, объемы и другие показатели для анализа сделок на бирже.</p>
     */
    @Data
    @Accessors(fluent = true)
    public class Metadata {

        @SerializedName("BOARDID")
        private FieldType<String> boardId;

        @SerializedName("TRADEDATE")
        private FieldType<String> tradeDate;

        @SerializedName("SHORTNAME")
        private FieldType<String> shortName;

        @SerializedName("SECID")
        private FieldType<String> secId;

        @SerializedName("NUMTRADES")
        private FieldType<Double> numTrades;

        @SerializedName("VALUE")
        private FieldType<Double> value;

        @SerializedName("OPEN")
        private FieldType<Double> open;

        @SerializedName("LOW")
        private FieldType<Double> low;

        @SerializedName("HIGH")
        private FieldType<Double> high;

        @SerializedName("LEGALCLOSEPRICE")
        private FieldType<Double> legalClosePrice;

        @SerializedName("WAPRICE")
        private FieldType<Double> waPrice;

        @SerializedName("CLOSE")
        private FieldType<Double> close;

        @SerializedName("VOLUME")
        private FieldType<Double> volume;

        @SerializedName("MARKETPRICE2")
        private FieldType<Double> marketPrice2;

        @SerializedName("MARKETPRICE3")
        private FieldType<Double> marketPrice3;

        @SerializedName("ADMITTEDQUOTE")
        private FieldType<Double> admittedQuote;

        @SerializedName("MP2VALTRD")
        private FieldType<Double> mp2ValTrd;

        @SerializedName("MARKETPRICE3TRADESVALUE")
        private FieldType<Double> marketPrice3TradesValue;

        @SerializedName("ADMITTEDVALUE")
        private FieldType<Double> admittedValue;

        @SerializedName("WAVAL")
        private FieldType<Double> waval;

        @SerializedName("TRADINGSESSION")
        private FieldType<Integer> tradingSession;

        @SerializedName("CURRENCYID")
        private FieldType<String> currencyId;

        @SerializedName("TRENDCLSPR")
        private FieldType<Double> trendClSpr;

        @SerializedName("TRADE_SESSION_DATE")
        private FieldType<String> tradeSessionDate;
    }

    /**
     * Вложенный класс, представляющий тип данных для каждого поля в метаданных.
     * <p>Тип данных включает значение поля, его тип, размер и максимальный размер.</p>
     */
    @Data
    @Accessors(fluent = true)
    static class FieldType<T> {
        private T value;
        private String type;
        private int bytes;
        private int maxSize;
    }

    /**
     * Вложенный класс, представляющий информацию о курсоре истории (например, данные о текущей странице).
     * <p>Этот класс содержит метаданные о курсоре и данные, которые используются для постраничного отображения информации.</p>
     */
    @Data
    @Accessors(fluent = true)
    public static class HistoryCursor {
        private CursorMetadata metadata;
        private List<String> columns;
        private List<List<Object>> data;
    }

    /**
     * Вложенный класс, представляющий метаданные для курсора истории.
     * <p>Метаданные включают индекс текущей страницы, общее количество страниц и размер страницы.</p>
     */
    @Data
    @Accessors(fluent = true)
    public static class CursorMetadata {

        @SerializedName("INDEX")
        private FieldType<Integer> index;

        @SerializedName("TOTAL")
        private FieldType<Integer> total;

        @SerializedName("PAGESIZE")
        private FieldType<Integer> pageSize;
    }

    /**
     * Вложенный класс, представляющий одну запись о сделке на бирже.
     * <p>Каждая запись содержит информацию о сделке, такую как цена, объем, количество сделок, дата сделки и другие данные.</p>
     */
    @Data
    @Accessors(fluent = true)
    public static class TradeRecord implements ExchangeTradeRecord {

        private String boardId;
        private Date tradeDate;
        private String shortName;
        private String secId;
        private Double numTrades;
        private Double value;
        private Double open;
        private Double low;
        private Double high;
        private Double legalClosePrice;
        private Double waPrice;
        private Double close;
        private Double volume;
        private Double marketPrice2;
        private Double marketPrice3;
        private Double admittedQuote;
        private Double mp2ValTrd;
        private Double marketPrice3TradesValue;
        private Double admittedValue;
        private Double waval;
        private Integer tradingSession;
        private String currencyId;
        private Double trendClSpr;
        private Date tradeSessionDate;
    }
}

