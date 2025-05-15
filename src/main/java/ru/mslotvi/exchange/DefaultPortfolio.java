package ru.mslotvi.exchange;

import java.util.Map;

public interface DefaultPortfolio {
   Map<String, Double> weights();
   double expectedReturn();
   double risk();
}
