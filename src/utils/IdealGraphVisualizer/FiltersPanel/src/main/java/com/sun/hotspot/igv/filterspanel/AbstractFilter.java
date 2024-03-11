package com.sun.hotspot.igv.filterspanel;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.graph.Figure;

public abstract class AbstractFilter implements Filter {

    private final ChangedEvent<Filter> changedEvent;
    private final Properties properties;

    public AbstractFilter() {
        changedEvent = new ChangedEvent<>(this);
        properties = new Properties();
    }

    protected static String getFirstMatchingProperty(Figure figure, String[] propertyNames) {
        for (String propertyName : propertyNames) {
            String s = figure.getProperties().resolveString(propertyName);
            if (!s.isEmpty()) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public ChangedEvent<Filter> getChangedEvent() {
        return changedEvent;
    }
}
