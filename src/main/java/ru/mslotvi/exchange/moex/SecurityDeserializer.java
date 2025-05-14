package ru.mslotvi.exchange.moex;

import com.google.gson.*;
import ru.mslotvi.config.MoexConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SecurityDeserializer implements JsonDeserializer<List<MoexSecuritie>> {

    private final MoexConfig moexConfig;

    public SecurityDeserializer(MoexConfig moexConfig) {
        this.moexConfig = moexConfig;
    }

    @Override
    public List<MoexSecuritie> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray array = jsonElement.getAsJsonObject().get("securities").getAsJsonObject().get("data").getAsJsonArray();

        final List<MoexSecuritie> result = new ArrayList<>();

        for (JsonElement element : array) {
            JsonArray row = element.getAsJsonArray();

            MoexSecuritie securitie = new MoexSecuritie(moexConfig);


            securitie.secId(row.get(0).getAsString());
            securitie.boardId(MoexBoard.valueOf(row.get(1).getAsString()));
            securitie.shortName(row.get(2).getAsString());
            securitie.prevPrice(row.get(3).isJsonNull() ? null : row.get(3).getAsDouble());
            securitie.lotSize(row.get(4).getAsInt());
            securitie.faceValue(row.get(5).getAsDouble());
            securitie.status(row.get(6).getAsString());
            securitie.boardName(row.get(7).getAsString());
            securitie.decimals(row.get(8).getAsInt());
            securitie.secName(row.get(9).isJsonNull() ? null : row.get(9).getAsString());
            securitie.remarks(row.get(10).isJsonNull() ? null : row.get(10).getAsString());
            securitie.marketCode(row.get(11).getAsString());
            securitie.instrId(row.get(12).isJsonNull() ? null : row.get(12).getAsString());
            securitie.sectorId(row.get(13).isJsonNull() ? null : row.get(13).getAsString());
            securitie.minStep(row.get(14).isJsonNull() ? null : row.get(14).getAsDouble());
            securitie.prevPrice(row.get(15).isJsonNull() ? null : row.get(15).getAsDouble());
            securitie.faceUnit(row.get(16).isJsonNull() ? null : row.get(16).getAsString());
            securitie.prevDate(row.get(17).isJsonNull() ? null : row.get(17).getAsString());
            securitie.issueSize(row.get(18).isJsonNull() ? null : row.get(18).getAsLong());
            securitie.isin(row.get(19).getAsString());
            securitie.latName(row.get(20).getAsString());
            securitie.regNumber(row.get(21).isJsonNull() ? null : row.get(21).getAsString());
            securitie.prevLegalClosePrice(row.get(22).isJsonNull() ? null : row.get(22).getAsDouble());
            securitie.currencyId(row.get(23).getAsString());
            securitie.secType(row.get(24).getAsString());
            securitie.listLevel(row.get(25).getAsInt());
            securitie.settleDate(row.get(26).getAsString());

            result.add(securitie);
        }

        return result;
    }
}
