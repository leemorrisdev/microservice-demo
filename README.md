# Microservice demo

This project exists to test various microservice tools.  It consists of three microservices, each running Spark,
which communicate to deliver a web page to a user.  In this case, the page is a list of parcels
in various stages of being delivered.  The example is rather contrived but gives me an excuse to separate out the
process of displaying parcel data into several concerns.

## Services

### Parcel detail service

Returns either a summary list of parcels in the system, or can give more detail for a single parcel.  Responses from
this service do not include anything regarding what state the parcel is in.

### Tracker service

Returns tracking information for the provided parcel id.  This can either be a summary (i.e. the current state) or a full
history of states with timestamps for each.

### Web service

Provides a UI for the user, and is also responsible for calling the parcel detail and tracker services to build a response.

## How responses are built

The web service uses RxJava internally to handle collation of data from different services to build a single response.

If a user requests a list of parcels, the service makes a single request to the parcel detail service for a list of available
parcels.  Once the response is received, a request is made to the tracker service for each parcel.  These requests are made
asynchronously and are merged into the original data as they are received.  Once all of this data has been returned, the
response is sent.

## How the services are co-ordinated

- Direct : Services communicate directly with each other using the predefined port numbers in config.  Only works for localhost.
- Consul : Uses a list of healthy nodes retrieved from consul and round-robin load balances requests.  Works across multiple hosts and multiple
instances of the service.
- Linkerd : Simply forwards requests to Linkerd's local instance and lets Linkerd worry about how the messages should be routed.

## Starting other services

### Consul

    ./consul agent -dev -data-dir ./data -bind=127.0.0.1 -ui

#### Linkerd

    java -jar linkerd-0.2.0-exec config/linkerd.yaml

Add the following files into $LINKERD_HOME/disco

##### PARCEL_DETAIL

    127.0.0.1 9054

##### TRACKER

    127.0.0.1 9055