package uk.co.leemorris.microservicedemo.web.remote;

import rx.Observable;
import uk.co.leemorris.microservicedemo.common.ProviderType;
import uk.co.leemorris.microservicedemo.common.ServiceName;

/**
 * Created by Lee on 05/03/2016.
 */
public interface RemoteProvider {

    Observable<String> get(ServiceName service, String url);

    ProviderType getProviderType();

}
