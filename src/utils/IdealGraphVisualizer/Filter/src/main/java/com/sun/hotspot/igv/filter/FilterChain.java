/*
 * Copyright (c) 2008, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */
package com.sun.hotspot.igv.filter;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.ChangedEventProvider;
import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.graph.Diagram;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Thomas Wuerthinger
 */
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
