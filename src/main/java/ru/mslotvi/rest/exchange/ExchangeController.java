package ru.mslotvi.rest.exchange;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mslotvi.data.StoragePortfolio;
import ru.mslotvi.exchange.*;
import ru.mslotvi.util.MathUtil;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Set<String>> getUserData() {
        return ResponseEntity.ok(exchangeService.exchanges().keySet());
    }

    @GetMapping("/{exchangeId}/getBoards")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Collection<ExchangeBoard>> getBoards(@PathVariable String exchangeId) {
        Map<String, Exchange> exchanges = exchangeService.exchanges();
        if (exchanges.containsKey(exchangeId)) {
            return ResponseEntity.ok(exchanges.get(exchangeId).getBoards());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{exchangeId}/loadSecurities")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Collection<ExchangeSecuritieDto>> loadSecurities(@PathVariable String exchangeId) {
        Map<String, Exchange> exchanges = exchangeService.exchanges();
        if (exchanges.containsKey(exchangeId)) {
            var exchange = exchanges.get(exchangeId);
            exchange.loadData().join();
            return ResponseEntity.ok(exchange.getSecurities().values().stream().map(e -> ExchangeSecuritieDto.builder()
                    .secId(e.secId())
                    .shortName(e.shortName())
                    .build()).toList());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{exchangeId}/lastLoadSecurities")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Collection<ExchangeSecuritieDto>> lastLoadSecurities(@PathVariable String exchangeId) {
        Map<String, Exchange> exchanges = exchangeService.exchanges();
        if (exchanges.containsKey(exchangeId)) {
            var exchange = exchanges.get(exchangeId);
            return ResponseEntity.ok(exchange.getSecurities().values().stream().map(e -> ExchangeSecuritieDto.builder()
                    .secId(e.secId())
                    .shortName(e.shortName())
                    .build()).toList());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/calculateEffectiveFrontier")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Collection<PortfolioDto>> calculateEffectiveFrontier(
            @RequestParam Instant start,
            @Nullable @RequestParam Instant end,
            int precision) {

        Instant endDate = end != null ? end : Instant.now();

        var portfolios = exchangeService.getStoragePortfolios(start, endDate);

        var result = MathUtil.calculateEfficientFrontier(portfolios, precision).stream().map(PortfolioDto::from).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/calculateEffectiveFrontierCalc")
    @PreAuthorize("hasRole('MEMBER')")
    @SneakyThrows
    public ResponseEntity<byte[]> calculateEffectiveFrontierPdf(
            @RequestParam Instant start,
            @Nullable @RequestParam Instant end,
            int precision) {

        Instant endDate = end != null ? end : Instant.now();

        var portfolios = exchangeService.getStoragePortfolios(start, endDate);

        List<StoragePortfolio> result = MathUtil.calculateEfficientFrontier(portfolios, precision);
        Workbook workbook = EfficientFrontierWorkbook.createEfficientFrontierWorkbook(result);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("EfficientFrontier.xlsx")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/{exchangeId}/generatePortfolio")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Collection<PortfolioDto>> generatePortfolio(
            @PathVariable String exchangeId,
            @RequestParam Set<String> ids,
            @RequestParam LocalDate start,
            @Nullable @RequestParam LocalDate end,
            @RequestParam int amount) {

        if (end == null) {
            end = LocalDate.now();
        }

        Map<String, Exchange> exchanges = exchangeService.exchanges();
        if (exchanges.containsKey(exchangeId)) {
            var result = exchangeService.generatePortfolios(exchangeId, start, end, ids, amount);
            return ResponseEntity.ok(result.stream().map(PortfolioDto::from).toList());
        }
        return ResponseEntity.notFound().build();
    }
}