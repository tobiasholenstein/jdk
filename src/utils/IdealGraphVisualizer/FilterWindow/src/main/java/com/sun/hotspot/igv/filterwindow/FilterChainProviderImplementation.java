package com.sun.hotspot.igv.filterwindow;

import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = FilterChainProvider.class)
public class FilterChainProviderImplementation implements FilterChainProvider {

    @Override
    public FilterChain getFilterChain() {
        return FilterTopComponent.findInstance().getFilterChain();
    }

    @Override
    public FilterChain getAllFiltersOrdered() {
        return FilterTopComponent.findInstance().getAllFiltersOrdered();
    }

}
