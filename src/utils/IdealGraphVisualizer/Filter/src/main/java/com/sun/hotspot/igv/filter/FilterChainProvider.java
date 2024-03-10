package com.sun.hotspot.igv.filter;

public interface FilterChainProvider {

    FilterChain getFilterChain();

    FilterChain getAllFiltersOrdered();
}
