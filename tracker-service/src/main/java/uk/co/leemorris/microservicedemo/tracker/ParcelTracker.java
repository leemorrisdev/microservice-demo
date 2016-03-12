package uk.co.leemorris.microservicedemo.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.co.leemorris.microservicedemo.common.TrackingEntry;
import uk.co.leemorris.microservicedemo.common.TrackingInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 05/03/2016.
 */
public class ParcelTracker {

    private Map<Long, TrackingInfo> trackingInformation = Maps.newHashMap();

    public ParcelTracker() {
        try {
            readInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TrackingInfo getInfoForParcel(Long parcelId) {
        return trackingInformation.get(parcelId);
    }

    public TrackingInfo getLatestForParcel(Long parcelId) {
        if(trackingInformation.containsKey(parcelId)) {

            List<TrackingEntry> entries = trackingInformation.get(parcelId).getEntries();

            TrackingInfo info = new TrackingInfo();
            info.setEntries(Lists.newArrayList(entries.get(entries.size() - 1)));

            return info;
        }

        return null;
    }

    private void readInfo() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        List<TrackingInfo> trackingInfos = objectMapper.readValue(getClass().getResourceAsStream("/tracking-info.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TrackingInfo.class));

        trackingInformation.putAll(Maps.uniqueIndex(trackingInfos, TrackingInfo::getParcelId));
    }
}
