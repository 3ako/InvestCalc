package ru.mslotvi.exchange;

import java.util.List;

/**
 * Интерфейс, представляющий снимок данных о сделках на бирже за определенную дату.
 * <p>Этот интерфейс представляет собой абстракцию для хранения и извлечения данных о сделках на бирже за конкретный день.</p>
 *
 * <p>Метод {@link #tradeRecords()} позволяет извлечь список записей сделок {@link ExchangeTradeRecord} для данной даты.</p>
 *
 * @see ExchangeTradeRecord
 */
public interface ExchangeDateSnapshot {

    /**
     * Получает список записей о сделках на бирже для данной даты.
     * <p>Этот метод возвращает список объектов, реализующих интерфейс {@link ExchangeTradeRecord},
     * которые содержат информацию о сделках, совершенных на бирже в течение определенного дня.</p>
     *
     * @return Список записей сделок {@link ExchangeTradeRecord} для данной даты.
     */
    List<? extends ExchangeTradeRecord> tradeRecords();
}
