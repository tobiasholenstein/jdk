package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.ChangedEventProvider;
import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.graph.Diagram;

public interface Filter extends Properties.Provider, ChangedEventProvider<Filter> {

    String getName();

    void apply(Diagram d);

    @Override
    ChangedEvent<Filter> getChangedEvent();
}
