package ru.mslotvi.exchange;

import java.util.Date;

public interface ExchangeTradeRecord {
    String boardId();
    Date tradeDate();
    String shortName();
    String secId();
    Double numTrades();
    Double value();
    Double volume();
    Double close();
}
