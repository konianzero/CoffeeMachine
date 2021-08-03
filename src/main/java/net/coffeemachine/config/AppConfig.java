package net.coffeemachine.config;

import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class AppConfig {

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource(@Value("${spring.datasource.driver-class-name}") String driver,
                                              @Value("${spring.datasource.url}") String url,
                                              @Value("${spring.datasource.username}") String username,
                                              @Value("${spring.datasource.password}") String password,
                                              @Value("${spring.sql.init.data-locations}") String schema) {
        log.info("Initializing logging database");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // schema init
        // schema.substring(10) -> get only schema name, without 'classpath:'
        Resource initSchema = new ClassPathResource(schema.substring(10));
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);

        return dataSource;
    }

    @Bean
    public Map<CoffeeType, Coffee> coffeeFactory(List<Coffee> coffeeList) {
        log.info("Create coffee factory");
        return coffeeList.stream().collect(Collectors.toMap(Coffee::getType, Function.identity()));
    }

    @Bean
    public ExecutorService getThreadPool() {
        log.info("Create coffee machine service");
        return Executors.newSingleThreadExecutor();
    }
}
