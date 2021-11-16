package de.schosins.resourceresolverperformance.database;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = { DatabaseConfig.class })
@EntityScan(basePackageClasses = { DatabaseConfig.class })
public class DatabaseConfig {
}
