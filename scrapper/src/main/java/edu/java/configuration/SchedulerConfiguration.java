package edu.java.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@Log4j2
public class SchedulerConfiguration {
    @Scheduled(fixedDelayString = "#{@interval.toMillis()}")
    public void update() {
        log.info("Updating");
    }
}
