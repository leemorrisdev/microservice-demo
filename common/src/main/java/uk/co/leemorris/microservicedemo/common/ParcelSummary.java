package uk.co.leemorris.microservicedemo.common;

/**
 * Created by Lee on 05/03/2016.
 */
public class ParcelSummary {

    private Long id;
    private String description;
    private String address;
    private TrackingEntry status;

    public ParcelSummary() {

    }

    public ParcelSummary(ParcelDetails parcelDetails) {
        id = parcelDetails.getId();
        description = parcelDetails.getDescription();
        address = parcelDetails.getRecipient().getAddress1() + ", " +
                parcelDetails.getRecipient().getAddress2() + ", " +
                parcelDetails.getRecipient().getCity() +
                ", " + parcelDetails.getRecipient().getPostCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TrackingEntry getStatus() {
        return status;
    }

    public void setStatus(TrackingEntry status) {
        this.status = status;
    }
}
