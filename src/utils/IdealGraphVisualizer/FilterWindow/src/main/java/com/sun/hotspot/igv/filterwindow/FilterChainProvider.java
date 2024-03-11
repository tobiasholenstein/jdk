package com.sun.hotspot.igv.filterwindow;

public interface FilterChainProvider {

    FilterChain getFilterChain();

    FilterChain getAllFiltersOrdered();
}
