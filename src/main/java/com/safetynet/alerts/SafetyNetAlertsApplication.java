package com.safetynet.alerts;

import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.util.DataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class SafetyNetAlertsApplication {

    private static final Logger logger = LogManager.getLogger(SafetyNetAlertsApplication.class);

    @Value("${info.data}")
    private String dataFileJSON;


    public static void main(final String[] args) {
        SpringApplication.run(SafetyNetAlertsApplication.class, args);
    }

    /**
     * Read json data
     *
     * @return entitiesInfosStorage
     * @throws IOException exception
     */
    @Bean
    public AllInformations jsonFileLoader() throws IOException {
        logger.debug("Data JSON loaded");
        return DataLoader.readJsonFile(dataFileJSON);
    }

}
