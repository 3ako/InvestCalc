package ru.mslotvi;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.mslotvi.config.Config;
import ru.mslotvi.exchange.Exchange;

import java.time.LocalDate;
import java.util.Set;

import static java.time.LocalDate.now;


@Log4j2
@Component
public class InvestCalc {

    private final Config config;

    @SneakyThrows
    public InvestCalc(Config config, Exchange exchange) {
        this.config = config;


        exchange.loadData().thenAccept(Void -> {
            log.info("Success load data");

            LocalDate currentDate = now();

            // Получаем дату год назад
            LocalDate oneYearAgo = currentDate.minusMonths(1);


            System.out.println("Вычисляю...");
            var frontier = exchange.getEfficientFrontier(Set.of("KROTP", "SOFL", "RNFT", "RTSB"), oneYearAgo, currentDate);
//

            frontier.generatePortfolios(20);

            frontier.getPortfolios().forEach(p -> {
                p.printPortfolio();
                System.out.println("   ");
            });

            System.out.println("Эффективная Линия");


            frontier.calculateEfficientFrontier(0).forEach(p -> {
                p.printPortfolio();
                System.out.println("   ");
            });

        }).join();



    }
}