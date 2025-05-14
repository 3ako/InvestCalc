package ru.mslotvi.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Config {

    private final MoexConfig moex;

}
