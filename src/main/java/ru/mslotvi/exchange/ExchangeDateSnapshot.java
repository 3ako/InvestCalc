package ru.mslotvi.exchange;

import java.util.List;

public interface ExchangeDateSnapshot {
    List<? extends ExchangeTradeRecord> tradeRecords();
}
