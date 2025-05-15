package ru.mslotvi.exchange.moex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.mslotvi.config.MoexConfig;
import ru.mslotvi.exchange.*;
import ru.mslotvi.http.HttpRequestService;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
@Component
@Log4j2
public class MoexExchange implements Exchange {
    private final String name = "MOEX";
    private final MoexConfig moexConfig;
    private final Map<String, MoexSecuritie> securities = new HashMap<>();

    public MoexExchange(MoexConfig moexConfig) {
        this.moexConfig = moexConfig;
    }

    public PortfolioCalculator createPortfolioCalculator(List<ExchangeSecuritie> allSecurities, LocalDate start, LocalDate end) {

        allSecurities.forEach(s -> s.loadMarketHistory(start, end));

        return new PortfolioCalculator(allSecurities);
    }

    public PortfolioCalculator createPortfolioCalculator(Set<String> ids, LocalDate start, LocalDate end) {
        return createPortfolioCalculator(ids.stream().map(i -> (ExchangeSecuritie) securities.get(i)).filter(Objects::nonNull).toList(), start, end);
    }

    @Override
    public List<Portfolio> generatePortfolios(Set<String> ids, LocalDate start, LocalDate end, int amount) {
        var calculator = createPortfolioCalculator(ids, start, end);
        calculator.generatePortfolios(amount);
        return calculator.getPortfolios();
    }


    @SneakyThrows
    public void loadSecurities() {

        securities.clear();

        String response = HttpRequestService.sendGetRequest(URI.create(moexConfig.getSecuritiesEntryPoint()+".json"));
        Type securityListType = new TypeToken<List<MoexSecuritie>>(){}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(securityListType, new SecurityDeserializer(moexConfig))
                .create();

        List<MoexSecuritie> moexResponse = gson.fromJson(response, securityListType);

        moexResponse.forEach(r -> securities.put(r.secId(), r));
    }

    @Override
    public CompletableFuture<Void> loadData() {
        return CompletableFuture.runAsync(() -> {

            loadSecurities();

            log.info("Load {} securities", securities.values().size());
        });
    }


    @Override
    public List<ExchangeBoard> getBoards() {
        return List.of(MoexBoard.values());
    }

    @Override
    public Map<String, ExchangeSecuritie> getSecurities() {
        return securities.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
