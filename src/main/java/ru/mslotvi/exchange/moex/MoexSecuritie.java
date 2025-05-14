package ru.mslotvi.exchange.moex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import ru.mslotvi.config.MoexConfig;
import ru.mslotvi.exchange.ExchangeDateSnapshot;
import ru.mslotvi.exchange.ExchangeSecuritie;
import ru.mslotvi.http.HttpRequestService;
import ru.mslotvi.util.MathUtil;
import ru.mslotvi.util.QueryUtil;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@Accessors(fluent = true)
public class MoexSecuritie implements ExchangeSecuritie {

    private final MoexConfig moexConfig;

    public MoexSecuritie(MoexConfig moexConfig) {
        this.moexConfig = moexConfig;
    }

    @SerializedName("SECID")
    private String secId;

    @SerializedName("BOARDID")
    private MoexBoard boardId;

    @SerializedName("SHORTNAME")
    private String shortName;

    @SerializedName("PREVPRICE")
    private Double prevPrice;

    @SerializedName("LOTSIZE")
    private Integer lotSize;

    @SerializedName("FACEVALUE")
    private Double faceValue;

    @SerializedName("STATUS")
    private String status;

    @SerializedName("BOARDNAME")
    private String boardName;

    @SerializedName("DECIMALS")
    private Integer decimals;

    @SerializedName("SECNAME")
    private String secName;

    @SerializedName("REMARKS")
    private String remarks;

    @SerializedName("MARKETCODE")
    private String marketCode;

    @SerializedName("INSTRID")
    private String instrId;

    @SerializedName("SECTORID")
    private String sectorId;

    @SerializedName("MINSTEP")
    private Double minStep;

    @SerializedName("PREVWAPRICE")
    private Double prevWapPrice;

    @SerializedName("FACEUNIT")
    private String faceUnit;

    @SerializedName("PREVDATE")
    private String prevDate;

    @SerializedName("ISSUESIZE")
    private Long issueSize;

    @SerializedName("ISIN")
    private String isin;

    @SerializedName("LATNAME")
    private String latName;

    @SerializedName("REGNUMBER")
    private String regNumber;

    @SerializedName("PREVLEGALCLOSEPRICE")
    private Double prevLegalClosePrice;

    @SerializedName("CURRENCYID")
    private String currencyId;

    @SerializedName("SECTYPE")
    private String secType;

    @SerializedName("LISTLEVEL")
    private Integer listLevel;

    @SerializedName("SETTLEDATE")
    private String settleDate;

    private final List<ExchangeDateSnapshot> marketHistory = new ArrayList<>();

    @Override
    public double calculateDeviation() {
        return MathUtil.calculateStandardDeviation(marketHistory);
    }

    @Override
    public double calculateExpectedReturn() {
        return MathUtil.calculateExpectedReturn(this);
    }

    @Override
    public List<ExchangeDateSnapshot> lastLoadMarketHistory() {
        return marketHistory;
    }

    @Override
    @SneakyThrows
    public List<ExchangeDateSnapshot> loadMarketHistory(LocalDate from, LocalDate to) {
        marketHistory.clear();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = from.format(dateFormat);
        String toDate = to.format(dateFormat);

        int start = 0;
        boolean hasMoreData = true;

        while (hasMoreData) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("from", fromDate);
            queryParams.put("till", toDate);
            queryParams.put("start", String.valueOf(start));

            String url = moexConfig.getEntryPoint() +"/"+secId+ ".json?" + QueryUtil.buildQueryString(queryParams);

            String response = HttpRequestService.sendGetRequest(URI.create(url));

            Type tradeRecordListType = new TypeToken<List<MoexResponse.TradeRecord>>(){}.getType();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(tradeRecordListType, new TradeRecordDeserializer())
                    .create();

            MoexResponse moexResponse = gson.fromJson(response, MoexResponse.class);

            List<MoexResponse.TradeRecord> tradeRecords = moexResponse.history().data();
            if (tradeRecords.isEmpty()) {
                hasMoreData = false;
            } else {
                marketHistory.add(moexResponse);
                start += tradeRecords.size();
            }
        }

        return marketHistory;
    }
}
