package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
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
