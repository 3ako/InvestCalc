package ru.mslotvi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "moex")
@Configuration
public class MoexConfig {
    private String entryPoint;
    private String securitiesEntryPoint;

}
