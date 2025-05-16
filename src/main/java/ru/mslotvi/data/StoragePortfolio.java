package ru.mslotvi.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.mslotvi.exchange.DefaultPortfolio;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@Accessors(chain = true, fluent = true)
public class StoragePortfolio implements DefaultPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "portfolio_securities", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyColumn(name = "security_name")
    @Column(name = "amount")
    private Map<String, Double> securities = new HashMap<>();
    private double expectedReturn;
    private double risk;
    private Instant createDate;

    @Override
    public Map<String, Double> weights() {
        return securities;
    }

    @Override
    public double expectedReturn() {
        return expectedReturn;
    }

    @Override
    public double risk() {
        return risk;
    }
}
