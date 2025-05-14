package ru.mslotvi.exchange.moex;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.mslotvi.exchange.ExchangeBoard;

@Getter @Accessors(fluent = true)
public enum MoexBoard implements ExchangeBoard {
    SMAL("SMAL", "Торги акциями малого и среднего капитала"),
    TQBR("TQBR", "Основная торговая сессия (обычно для ликвидных акций)"),
    TQFD("TQFD", "Фондовый рынок"),
    TQFE("TQFE", "Фондовый рынок - Т+ и биржевые операции"),
    TQIF("TQIF", "Иностранные инвестиции"),
    TQIR("TQIR", "Торги по рынку с внутренним расчетом (обычно для акций с меньшей ликвидностью)"),
    TQPI("TQPI", "Торги акциями на первом уровне"),
    TQTD("TQTD", "Торги по дневному рынку (обычно для опционов)"),
    TQTE("TQTE", "Торги облигациями на основном рынке"),
    TQTF("TQTF", "Торги по срочному рынку (обычно для фьючерсов)"),
    TQTY("TQTY", "Торги облигациями на рынке Т+1"),
    SPEQ("SPEQ", "Торги акциями с равным весом (Invesco S&P 500 Equal Weight UCITS ETF Acc)");


    private final String code;
    private final String description;

    MoexBoard(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
