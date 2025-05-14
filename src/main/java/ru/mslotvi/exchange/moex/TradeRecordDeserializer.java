package ru.mslotvi.exchange.moex;

import com.google.gson.*;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TradeRecordDeserializer implements JsonDeserializer<List<MoexResponse.TradeRecord>> {

    @Override
    public List<MoexResponse.TradeRecord> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        List<MoexResponse.TradeRecord> tradeRecords = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (JsonElement element : jsonArray) {
            JsonArray row = element.getAsJsonArray();
            MoexResponse.TradeRecord tradeRecord = new MoexResponse.TradeRecord();

            try {

                tradeRecord.boardId(row.get(0).getAsString());
                tradeRecord.tradeDate(dateFormat.parse(row.get(1).getAsString()));
                tradeRecord.shortName(row.get(2).getAsString());
                tradeRecord.secId(row.get(3).getAsString());
                tradeRecord.numTrades(row.get(4).getAsDouble());
                tradeRecord.value(row.get(5).getAsDouble());
                tradeRecord.open(row.get(6).isJsonNull() ? null : row.get(6).getAsDouble());
                tradeRecord.low(row.get(7).isJsonNull() ? null : row.get(7).getAsDouble());
                tradeRecord.high(row.get(8).isJsonNull() ? null : row.get(8).getAsDouble());
                tradeRecord.legalClosePrice(row.get(9).isJsonNull() ? null : row.get(9).getAsDouble());
                tradeRecord.waPrice(row.get(10).isJsonNull() ? null : row.get(10).getAsDouble());
                tradeRecord.close(row.get(11).isJsonNull() ? null : row.get(11).getAsDouble());
                tradeRecord.volume(row.get(12).getAsDouble());
                tradeRecord.marketPrice2(row.get(13).isJsonNull() ? null : row.get(13).getAsDouble());
                tradeRecord.marketPrice3(row.get(14).isJsonNull() ? null : row.get(14).getAsDouble());
                tradeRecord.admittedQuote(row.get(15).isJsonNull() ? null : row.get(15).getAsDouble());
                tradeRecord.mp2ValTrd(row.get(16).isJsonNull() ? null : row.get(16).getAsDouble());
                tradeRecord.marketPrice3TradesValue(row.get(17).isJsonNull() ? null : row.get(17).getAsDouble());
                tradeRecord.admittedValue(row.get(18).isJsonNull() ? null : row.get(18).getAsDouble());
                tradeRecord.waval(row.get(19).isJsonNull() ? null : row.get(19).getAsDouble());
                tradeRecord.tradingSession(row.get(20).getAsInt());
                tradeRecord.currencyId(row.get(21).getAsString());
                tradeRecord.trendClSpr(row.get(22).isJsonNull() ? null : row.get(22).getAsDouble());
                tradeRecord.tradeSessionDate(row.get(23).isJsonNull() ? null : dateFormat.parse(row.get(23).getAsString()));

            } catch (Exception e) {
                log.warn("Failed to deserialize TradeRecord", e);
            }

            tradeRecords.add(tradeRecord);
        }

        return tradeRecords;
    }
}