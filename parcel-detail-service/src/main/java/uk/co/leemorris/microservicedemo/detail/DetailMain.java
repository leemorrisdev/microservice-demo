package uk.co.leemorris.microservicedemo.detail;

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

public class DetailMain {

    private static final Logger LOG = LoggerFactory.getLogger(DetailMain.class);

    private ParcelDetailService parcelDetailService;
    private DemoConfiguration demoConfiguration;
    private JsonTransformer jsonTransformer;

    private ConsulService consulService;

    public DetailMain() {
        parcelDetailService = new ParcelDetailService();
        jsonTransformer = new JsonTransformer();
        demoConfiguration = new DemoConfiguration();
        consulService = new ConsulService(ServiceName.PARCEL_DETAIL);
    }

    public void initialise() {

        Service thisService = demoConfiguration.getConfig().getServices().get(ServiceName.PARCEL_DETAIL);

        port(thisService.getPort());

        get("/parcels/", (req, res) -> parcelDetailService.getSummaries(), jsonTransformer);
        get("/parcels/:parcelId", (req, res) -> parcelDetailService.getDetails(Long.valueOf(req.params("parcelId"))),
                jsonTransformer);

        before((req, res) -> LOG.info(req.pathInfo()));
    }

    public static void main(String[] args) {

        new DetailMain().initialise();

    }
}
