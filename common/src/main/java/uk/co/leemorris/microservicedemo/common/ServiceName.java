package uk.co.leemorris.microservicedemo.common;

/**
 * Created by Lee on 05/03/2016.
 */
public enum ServiceName {

    WEB("Web"), PARCEL_DETAIL("ParcelDetail"), TRACKER("Tracker");

    private String name;

    ServiceName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return name;
    }

}
