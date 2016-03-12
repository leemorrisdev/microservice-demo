package uk.co.leemorris.microservicedemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.ResponseTransformer;

/**
 * Created by Lee on 05/03/2016.
 */
public class JsonTransformer implements ResponseTransformer {

    private ObjectMapper objectMapper;

    public JsonTransformer() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }
}
