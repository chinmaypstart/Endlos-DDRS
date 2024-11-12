package com.endlosiot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootApplication(scanBasePackages = {"com.endlosiot.*"}, exclude = {DataSourceAutoConfiguration.class})
@EntityScan("com.endlosiot.*")
@EnableCaching
public class EndlosiotApiApplication {

    public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    public static void main(String[] args) {
        SpringApplication.run(EndlosiotApiApplication.class, args);
        System.out.println("Run Successful");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        String activeProfile = System.getProperty(SPRING_PROFILES_ACTIVE);
        if (activeProfile == null) {
            return getPropertySourcesPlaceholderConfigurer("application.properties");
        } else {
            return getPropertySourcesPlaceholderConfigurer("application-" + activeProfile + ".properties");
        }
    }

    private static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer(String path) {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                new PropertySourcesPlaceholderConfigurer();
        Resource[] resources =
                new ClassPathResource[]{
                        new ClassPathResource("application.properties"),
                        new ClassPathResource(path)
                };
        propertySourcesPlaceholderConfigurer.setLocations(resources);
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertySourcesPlaceholderConfigurer;
    }

}
