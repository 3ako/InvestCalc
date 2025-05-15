package ru.mslotvi.rest.exchange;

import lombok.Builder;

@Builder
public record ExchangeSecuritieDto(
        String secId,
        String shortName
) {
}
