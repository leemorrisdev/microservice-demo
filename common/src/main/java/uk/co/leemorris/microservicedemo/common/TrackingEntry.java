package uk.co.leemorris.microservicedemo.common;

import java.util.Date;

/**
 * Created by Lee on 05/03/2016.
 */
public class TrackingEntry {

    private Date timestamp;
    private TrackingStatus status;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public TrackingStatus getStatus() {
        return status;
    }

    public void setStatus(TrackingStatus status) {
        this.status = status;
    }
}
