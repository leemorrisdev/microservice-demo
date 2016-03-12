package uk.co.leemorris.microservicedemo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import rx.Observable;
import uk.co.leemorris.microservicedemo.common.ParcelDetails;
import uk.co.leemorris.microservicedemo.common.ParcelSummary;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.TrackingInfo;
import uk.co.leemorris.microservicedemo.common.consul.ConsulService;
import uk.co.leemorris.microservicedemo.web.remote.ConsulRemoteProvider;
import uk.co.leemorris.microservicedemo.web.remote.RemoteProvider;
import uk.co.leemorris.microservicedemo.web.remote.SimpleRemoteProvider;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lee on 05/03/2016.
 */
public class ParcelService {

    private RemoteProvider remoteProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ParcelService(RemoteProvider remoteProvider) {
        this.remoteProvider = remoteProvider;
    }

    public void setRemoteProvider(RemoteProvider remoteProvider) {
        this.remoteProvider = remoteProvider;
    }

    public RemoteProvider getRemoteProvider() {
        return remoteProvider;
    }

    public Observable<ParcelSummary> listParcels() {

        return remoteProvider.get(ServiceName.PARCEL_DETAIL, "/parcels/")
                .flatMap(json -> {
                    try {
                        List<ParcelSummary> summaries = objectMapper.readValue(json,
                                objectMapper.getTypeFactory().constructCollectionType(List.class, ParcelSummary.class));

                        return Observable.from(summaries);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                }).flatMap(this::enrichStatus);
    }

    public Observable<ParcelDetails> detail(Long parcelId) {

        return remoteProvider.get(ServiceName.PARCEL_DETAIL, "/parcels/" + parcelId)
                .flatMap(json -> {
                    try {
                        ParcelDetails details = objectMapper.readValue(json, ParcelDetails.class);

                        return Observable.just(details);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                }).flatMap(this::enrichStatus);

    }

    private Observable<ParcelDetails> enrichStatus(ParcelDetails details) {

        return remoteProvider.get(ServiceName.TRACKER, "/track/" + details.getId())
                .flatMap(json -> {
                    try {
                        TrackingInfo info = objectMapper.readValue(json, TrackingInfo.class);

                        details.setTracking(info);

                        return Observable.just(details);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }

    private Observable<ParcelSummary> enrichStatus(ParcelSummary summary) {

        return remoteProvider.get(ServiceName.TRACKER, "/track/summary/" + summary.getId())
                .flatMap(json -> {
                    try {
                        TrackingInfo info = objectMapper.readValue(json, TrackingInfo.class);

                        summary.setStatus(info.getEntries().get(info.getEntries().size() - 1));

                        return Observable.just(summary);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }
}
