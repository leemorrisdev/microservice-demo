package uk.co.leemorris.microservicedemo.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.leemorris.microservicedemo.common.JsonTransformer;
import uk.co.leemorris.microservicedemo.common.ProviderType;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.config.DemoConfiguration;
import uk.co.leemorris.microservicedemo.common.config.Service;
import uk.co.leemorris.microservicedemo.common.consul.ConsulService;
import uk.co.leemorris.microservicedemo.web.remote.ConsulRemoteProvider;
import uk.co.leemorris.microservicedemo.web.remote.LinkerdRemoteProvider;
import uk.co.leemorris.microservicedemo.web.remote.RemoteProvider;
import uk.co.leemorris.microservicedemo.web.remote.SimpleRemoteProvider;

import java.util.Map;

import static spark.Spark.*;

/**
 * Created by Lee on 05/03/2016.
 */
public class WebMain {

    private static final Logger LOG = LoggerFactory.getLogger(WebMain.class);

    private JsonTransformer jsonTransformer = new JsonTransformer();
    private ParcelService parcelService;
    private DemoConfiguration demoConfiguration;

    private ConsulService consulService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<ProviderType, RemoteProvider> providers = Maps.newHashMap();

    public WebMain() {
        this.demoConfiguration = new DemoConfiguration();

        consulService = new ConsulService(ServiceName.WEB);

        providers.put(ProviderType.SIMPLE, new SimpleRemoteProvider());
        providers.put(ProviderType.CONSUL, new ConsulRemoteProvider(consulService));
        providers.put(ProviderType.LINKERD, new LinkerdRemoteProvider());

        parcelService = new ParcelService(providers.get(ProviderType.SIMPLE));
    }

    public void initialise() {

        staticFileLocation("/public");
        Service thisService = demoConfiguration.getConfig().getServices().get(ServiceName.WEB);
        port(thisService.getPort());

        get("/api/parcels/", (req, res) -> {

            return parcelService.listParcels().toBlocking().toIterable();

        }, jsonTransformer);

        get("/api/parcels/:parcelId", (req, res) -> {

            return parcelService.detail(Long.valueOf(req.params("parcelId"))).toBlocking().single();

        }, jsonTransformer);

        post("/api/provider", (req, res) -> {

            NewProvider provider = objectMapper.readValue(req.body(), NewProvider.class);
            parcelService.setRemoteProvider(providers.get(provider.getProvider()));

            return provider;
        }, jsonTransformer);

        get("/api/settings", (req, res) -> {

            NewProvider provider = new NewProvider();
            provider.setProvider(parcelService.getRemoteProvider().getProviderType());

            return provider;
        }, jsonTransformer);

        before((req, res) -> {
            res.type("application/json");
            LOG.info(req.url());
        });

        exception(Exception.class, (e, req, res) -> {

            ErrorResponse response = new ErrorResponse();
            response.setCode(500);
            response.setText(e.getMessage());

            try {
                res.body(objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {

        new WebMain().initialise();

    }
}
