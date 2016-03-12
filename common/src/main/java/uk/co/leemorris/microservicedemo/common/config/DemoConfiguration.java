package uk.co.leemorris.microservicedemo.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

/**
 * Created by Lee on 05/03/2016.
 */
public class DemoConfiguration {

    private Configuration configuration;

    public DemoConfiguration() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            configuration = mapper.readValue(getClass().getResourceAsStream("/services.yaml"), Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return configuration;
    }

}
