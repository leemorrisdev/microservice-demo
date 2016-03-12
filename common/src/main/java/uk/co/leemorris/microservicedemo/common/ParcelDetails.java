package uk.co.leemorris.microservicedemo.common;

/**
 * Created by Lee on 05/03/2016.
 */
public class ParcelDetails {

    private Long id;
    private Address sender;
    private Address recipient;
    private TrackingInfo tracking;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getSender() {
        return sender;
    }

    public void setSender(Address sender) {
        this.sender = sender;
    }

    public Address getRecipient() {
        return recipient;
    }

    public void setRecipient(Address recipient) {
        this.recipient = recipient;
    }

    public TrackingInfo getTracking() {
        return tracking;
    }

    public void setTracking(TrackingInfo tracking) {
        this.tracking = tracking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
