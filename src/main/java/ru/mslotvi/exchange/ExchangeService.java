package ru.mslotvi.exchange;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.mslotvi.data.PortfolioRepository;
import ru.mslotvi.data.StoragePortfolio;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
@Getter @Accessors(fluent = true)
public class ExchangeService {
    private final Map<String, Exchange> exchanges = new HashMap<>();

    private final PortfolioRepository portfolioRepository;

    public ExchangeService(List<Exchange> exchanges, PortfolioRepository portfolioRepository) {
        exchanges.forEach(this::registerExchange);
        this.portfolioRepository = portfolioRepository;
    }

    public List<StoragePortfolio> getStoragePortfolios(Instant from, Instant to) {
        return portfolioRepository.findByCreateDateBetween(from, to);
    }

    public List<StoragePortfolio> generatePortfolios(String exchangeId, LocalDate start, LocalDate end, Set<String> ids, int amount) {
        var exchange = exchanges.get(exchangeId);
        if (exchange == null) {
            throw new IllegalArgumentException("No exchange with id " + exchangeId + " found");
        }

        var calculator = exchange.createPortfolioCalculator(ids, start, end);
        calculator.generatePortfolios(amount);
        var result = calculator.getPortfolios().stream().map(Portfolio::toStoragePortfolio).toList();
        portfolioRepository.saveAll(result);
        return result;
    }

    public void registerExchange(Exchange exchange) {
        if (exchanges.containsKey(exchange.name())) {
            throw new IllegalArgumentException("Exchange already exists: " + exchange.name());
        }

        this.exchanges.put(exchange.name(), exchange);
        log.info("Success register exchange: {}", exchange.name());
    }

}
