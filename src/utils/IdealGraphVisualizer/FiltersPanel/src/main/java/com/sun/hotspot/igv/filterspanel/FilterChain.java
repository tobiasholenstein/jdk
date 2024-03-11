package com.sun.hotspot.igv.filterspanel;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.ChangedEventProvider;
import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.graph.Diagram;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FilterChain implements ChangedEventProvider<FilterChain> {

    private final List<Filter> filters;
    private final transient ChangedEvent<FilterChain> changedEvent;

    private final ChangedListener<Filter> changedListener = new ChangedListener<>() {
        @Override
        public void changed(Filter source) {
            changedEvent.fire();
        }
    };

    public FilterChain() {
        filters = new ArrayList<>();
        changedEvent = new ChangedEvent<>(this);
    }

    @Override
    public ChangedEvent<FilterChain> getChangedEvent() {
        return changedEvent;
    }

    public void applyInOrder(Diagram diagram, FilterChain filterOrder) {
        for (Filter filter : filterOrder.getFilters()) {
            if (filters.contains(filter)) {
                filter.apply(diagram);
            }
        }
    }

    public void addFilter(Filter filter) {
        assert filter != null;
        filters.add(filter);
        filter.getChangedEvent().addListener(changedListener);
        changedEvent.fire();
    }

    public boolean containsFilter(Filter filter) {
        return filters.contains(filter);
    }

    public void removeFilter(Filter filter) {
        assert filters.contains(filter);
        filters.remove(filter);
        filter.getChangedEvent().removeListener(changedListener);
        changedEvent.fire();
    }

    public List<Filter> getFilters() {
        return Collections.unmodifiableList(filters);
    }
}
