package uk.co.leemorris.microservicedemo.web;

import uk.co.leemorris.microservicedemo.common.ProviderType;

/**
 * @author lmorris
 */
public class NewProvider {

    private ProviderType provider;

    public ProviderType getProvider() {
        return provider;
    }

    public void setProvider(ProviderType provider) {
        this.provider = provider;
    }
}
