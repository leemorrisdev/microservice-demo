package uk.co.leemorris.microservicedemo.common;

import java.util.List;

/**
 * Created by Lee on 05/03/2016.
 */
public class TrackingInfo {

    private Long parcelId;
    private List<TrackingEntry> entries;

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long parcelId) {
        this.parcelId = parcelId;
    }

    public List<TrackingEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TrackingEntry> entries) {
        this.entries = entries;
    }
}
