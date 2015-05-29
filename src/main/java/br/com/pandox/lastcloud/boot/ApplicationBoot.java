package br.com.pandox.lastcloud.boot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("br.com.pandox.lastcloud")
@EnableScheduling
public class ApplicationBoot {

    private static final Logger LOGGER = LogManager.getLogger();

    public ApplicationBoot() throws Exception {
        LOGGER.debug("Initializing Application");
    }

}
