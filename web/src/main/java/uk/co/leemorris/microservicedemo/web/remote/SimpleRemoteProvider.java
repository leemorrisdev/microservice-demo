package uk.co.leemorris.microservicedemo.web.remote;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import rx.Observable;
import uk.co.leemorris.microservicedemo.common.ProviderType;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.config.DemoConfiguration;
import uk.co.leemorris.microservicedemo.common.config.Service;

/**
 *
 * Direct connection to service running on localhost.
 *
 */
public class SimpleRemoteProvider implements RemoteProvider {

    private DemoConfiguration demoConfiguration;

    private String urlPrefix;

    public SimpleRemoteProvider() {
        urlPrefix = "http://localhost";
        demoConfiguration = new DemoConfiguration();
    }

    public Observable<String> get(ServiceName service, String url) {

        String fullUrl = urlPrefix + ":" + getServicePort(service) + url;

        return Observable.from(Unirest.get(fullUrl).asStringAsync()).map(HttpResponse::getBody);

    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.SIMPLE;
    }

    private int getServicePort(ServiceName serviceName) {

        Service service = demoConfiguration.getConfig().getServices().get(serviceName);

        return service.getPort();
    }
}
