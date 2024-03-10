package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.filter.Filter;
import org.openide.nodes.Children;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


public class FilterNode extends CheckNode implements ChangedListener<FilterTopComponent> {

    private final Filter filter;

    public FilterNode(Filter filter) {
        this(filter, new InstanceContent());
    }

    private FilterNode(Filter filter, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(filter);

        this.filter = filter;

        setDisplayName(filter.getName());

        FilterTopComponent.findInstance().getFilterSettingsChangedEvent().addListener(this);
        changed(FilterTopComponent.findInstance());
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public void changed(FilterTopComponent source) {
        setSelected(source.getFilterChain().containsFilter(filter));
    }
}
