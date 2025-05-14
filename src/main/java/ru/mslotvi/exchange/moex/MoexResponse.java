package ru.mslotvi.exchange.moex;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.mslotvi.exchange.ExchangeDateSnapshot;
import ru.mslotvi.exchange.ExchangeTradeRecord;

import java.util.Date;
import java.util.List;

@Data
@Accessors(fluent = true)
public class MoexResponse implements ExchangeDateSnapshot {

    private History history;
    private HistoryCursor historyCursor;

    @Override
    public List<? extends ExchangeTradeRecord> tradeRecords() {
        return history.data;
    }

    @Data
    @Accessors(fluent = true)
    public static class History {
        private Metadata metadata;
        private List<String> columns;
        private List<TradeRecord> data;
    }

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
    @Data
    @Accessors(fluent = true)
    static class FieldType<T> {
        private T value;
        private String type;
        private int bytes;
        private int maxSize;
    }

    @Data
    @Accessors(fluent = true)
    public static class HistoryCursor {
        private CursorMetadata metadata;
        private List<String> columns;
        private List<List<Object>> data;
    }
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
