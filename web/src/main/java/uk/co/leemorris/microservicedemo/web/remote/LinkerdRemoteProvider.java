package uk.co.leemorris.microservicedemo.web.remote;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import rx.Observable;
import uk.co.leemorris.microservicedemo.common.ProviderType;
import uk.co.leemorris.microservicedemo.common.ServiceName;

/**
 * Forwards all traffic to local Linkerd instance, using the Host header to specify where the message
 * should be routed.
 * @author lmorris
 */
public class LinkerdRemoteProvider implements RemoteProvider {

    private final String url = "http://localhost:4140/";

    @Override
    public Observable<String> get(ServiceName service, String url) {
        return Observable.from(Unirest.get(this.url + url).header("Host", service.toString()).asStringAsync()).map(HttpResponse::getBody);
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.LINKERD;
    }
}
