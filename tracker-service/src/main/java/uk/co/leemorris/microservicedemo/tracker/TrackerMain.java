package uk.co.leemorris.microservicedemo.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.leemorris.microservicedemo.common.JsonTransformer;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.config.DemoConfiguration;
import uk.co.leemorris.microservicedemo.common.config.Service;
import uk.co.leemorris.microservicedemo.common.consul.ConsulService;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by Lee on 05/03/2016.
 */
public class TrackerMain {

    private static final Logger LOG = LoggerFactory.getLogger(TrackerMain.class);

    private ParcelTracker parcelTracker;
    private DemoConfiguration demoConfiguration;
    private JsonTransformer jsonTransformer = new JsonTransformer();

    private ConsulService consulService;

    public TrackerMain() {
        this.parcelTracker = new ParcelTracker();
        demoConfiguration = new DemoConfiguration();
        consulService = new ConsulService(ServiceName.TRACKER);
    }

    public void initialise() {

        Service thisService = demoConfiguration.getConfig().getServices().get(ServiceName.TRACKER);

        port(thisService.getPort());

        get("/track/:parcelId", (req, res) ->
                parcelTracker.getInfoForParcel(Long.valueOf(req.params("parcelId"))), jsonTransformer);

        get("/track/summary/:parcelId", (req, res) ->
                parcelTracker.getLatestForParcel(Long.valueOf(req.params("parcelId"))), jsonTransformer);

        before((req, res) -> LOG.info(req.pathInfo()));
    }

    public static void main(String[] args) {

        new TrackerMain().initialise();

    }
}
