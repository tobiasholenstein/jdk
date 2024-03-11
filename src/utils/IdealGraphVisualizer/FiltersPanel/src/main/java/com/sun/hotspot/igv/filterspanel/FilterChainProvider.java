package com.sun.hotspot.igv.filterspanel;

public interface FilterChainProvider {

    FilterChain getFilterChain();

    FilterChain getAllFiltersOrdered();
}
