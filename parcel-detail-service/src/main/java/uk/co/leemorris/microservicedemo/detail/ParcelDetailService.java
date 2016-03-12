package uk.co.leemorris.microservicedemo.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.co.leemorris.microservicedemo.common.ParcelDetails;
import uk.co.leemorris.microservicedemo.common.ParcelSummary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 05/03/2016.
 */
public class ParcelDetailService {

    private Map<Long, ParcelDetails> parcelDetails = Maps.newHashMap();
    private List<ParcelSummary> parcelSummaries = Lists.newArrayList();

    public ParcelDetailService() {
        try {
            readInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ParcelSummary> getSummaries() {
        return parcelSummaries;
    }

    public ParcelDetails getDetails(Long parcelId) {
        return parcelDetails.get(parcelId);
    }

    private void readInfo() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        List<ParcelDetails> parcels = objectMapper.readValue(getClass().getResourceAsStream("/parcels.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ParcelDetails.class));

        for(ParcelDetails parcel : parcels) {
            parcelDetails.put(parcel.getId(), parcel);
            parcelSummaries.add(new ParcelSummary(parcel));
        }
    }
}
