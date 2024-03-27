package edu.java.configuration;

import edu.java.service.Updater;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@Log4j2
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final Updater linkUpdater;

    @Scheduled(fixedDelayString = "#{@interval.toMillis()}")
    public void update() {
        linkUpdater.update();
    }
}
