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
package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.filter.CustomFilter;
import com.sun.hotspot.igv.filter.Filter;
import com.sun.hotspot.igv.filter.FilterChain;
import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * @author Thomas Wuerthinger
 */
public final class FilterTopComponent extends TopComponent implements ExplorerManager.Provider {

    public static final String FOLDER_ID = "Filters";
    public static final String AFTER_ID = "after";
    public static final String ENABLED_ID = "enabled";
    public static final String PREFERRED_ID = "FilterTopComponent";
    public static final String JAVASCRIPT_HELPER_ID = "JavaScriptHelper";
    private static final FilterChain defaultFilterChain = new FilterChain();
    private static FilterTopComponent instance;
    private final CheckListView view;
    private final ExplorerManager manager;
    private final ScriptEngine engine;
    private final FilterChain allFiltersOrdered = new FilterChain();
    private final ChangedEvent<FilterTopComponent> filterSettingsChangedEvent = new ChangedEvent<>(this);


    private FilterTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(FilterTopComponent.class, "CTL_FilterTopComponent"));
        setToolTipText(NbBundle.getMessage(FilterTopComponent.class, "HINT_FilterTopComponent"));

        ScriptEngineManager sem = new ScriptEngineManager();
        engine = sem.getEngineByName("ECMAScript");
        try {
            engine.eval(getJsHelperText());
        } catch (ScriptException ex) {
            Exceptions.printStackTrace(ex);
        }
        engine.getContext().getBindings(ScriptContext.ENGINE_SCOPE).put("IO", System.out);

        initFilters();

        manager = new ExplorerManager();
        manager.setRootContext(new AbstractNode(new FilterChildren()));
        associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
        view = new CheckListView();
        this.add(view, BorderLayout.CENTER);

        filterSettingsChangedEvent.fire();
    }

    private static String getJsHelperText() {
        InputStream is = null;
        StringBuilder sb = new StringBuilder("if (typeof importPackage === 'undefined') { try { load('nashorn:mozilla_compat.js'); } catch (e) {} }"
                + "importPackage(Packages.com.sun.hotspot.igv.filter);"
                + "importPackage(Packages.com.sun.hotspot.igv.graph);"
                + "importPackage(Packages.com.sun.hotspot.igv.data);"
                + "importPackage(Packages.com.sun.hotspot.igv.util);"
                + "importPackage(java.awt);");
        try {
            FileObject fo = FileUtil.getConfigRoot().getFileObject(JAVASCRIPT_HELPER_ID);
            is = fo.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = r.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }

        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return sb.toString();
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance()}.
     */
    public static synchronized FilterTopComponent getDefault() {
        if (instance == null) {
            instance = new FilterTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the FilterTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FilterTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Filter component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FilterTopComponent) {
            return (FilterTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public ChangedEvent<FilterTopComponent> getFilterSettingsChangedEvent() {
        return filterSettingsChangedEvent;
    }

    public FilterChain getAllFiltersOrdered() {
        return allFiltersOrdered;
    }

    public FilterChain getCurrentChain() {
        return defaultFilterChain;
    }

    public void addFilter(CustomFilter customFilter) {
        allFiltersOrdered.addFilter(customFilter);
        FileObject fileObject = getFileObject(customFilter);
        FilterChangedListener listener = new FilterChangedListener(fileObject, customFilter);
        listener.changed(customFilter);
        customFilter.getChangedEvent().addListener(listener);
    }

    private FileObject getFileObject(CustomFilter customFilter) {
        FileObject fileObject = FileUtil.getConfigRoot().getFileObject(FOLDER_ID + "/" + customFilter.getName() + ".js");
        if (fileObject == null) {
            try {
                fileObject = FileUtil.getConfigRoot().getFileObject(FOLDER_ID).createData(customFilter.getName() + ".js");
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return fileObject;
    }

    public void initFilters() {
        FileObject folder = FileUtil.getConfigRoot().getFileObject(FOLDER_ID);
        FileObject[] children = folder.getChildren();

        List<CustomFilter> customFilters = new ArrayList<>();
        HashMap<CustomFilter, String> afterMap = new HashMap<>();
        Set<CustomFilter> enabledSet = new HashSet<>();
        HashMap<String, CustomFilter> map = new HashMap<>();

        for (final FileObject fo : children) {
            InputStream is = null;

            String code = "";
            FileLock lock = null;
            try {
                lock = fo.lock();
                is = fo.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String s;
                StringBuilder sb = new StringBuilder();
                while ((s = r.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
                code = sb.toString();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    assert is != null;
                    is.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                lock.releaseLock();
            }

            String displayName = fo.getName();

            final CustomFilter cf = new CustomFilter(displayName, code, engine);
            map.put(displayName, cf);

            String after = (String) fo.getAttribute(AFTER_ID);
            afterMap.put(cf, after);

            Boolean enabled = (Boolean) fo.getAttribute(ENABLED_ID);
            if (enabled != null && enabled) {
                enabledSet.add(cf);
            }

            customFilters.add(cf);
        }

        for (int j = 0; j < customFilters.size(); j++) {
            for (int i = 0; i < customFilters.size(); i++) {
                List<CustomFilter> copiedList = new ArrayList<>(customFilters);
                for (CustomFilter cf : copiedList) {

                    String after = afterMap.get(cf);

                    if (map.containsKey(after)) {
                        CustomFilter afterCf = map.get(after);
                        int index = customFilters.indexOf(afterCf);
                        int currentIndex = customFilters.indexOf(cf);

                        if (currentIndex < index) {
                            customFilters.remove(currentIndex);
                            customFilters.add(index, cf);
                        }
                    }
                }
            }
        }

        for (CustomFilter cf : customFilters) {
            addFilter(cf);
            if (enabledSet.contains(cf)) {
                defaultFilterChain.addFilter(cf);
            }
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public boolean requestFocus(boolean temporary) {
        view.requestFocus();
        return super.requestFocus(temporary);
    }

    @Override
    protected boolean requestFocusInWindow(boolean temporary) {
        view.requestFocus();
        return super.requestFocusInWindow(temporary);
    }

    @Override
    public void requestActive() {
        super.requestActive();
        view.requestFocus();
    }

    private static class FilterChangedListener implements ChangedListener<Filter> {

        private final CustomFilter filter;
        private FileObject fileObject;

        public FilterChangedListener(FileObject fo, CustomFilter cf) {
            fileObject = fo;
            filter = cf;
        }

        @Override
        public void changed(Filter source) {
            try {
                if (!fileObject.getName().equals(filter.getName())) {
                    FileLock lock = fileObject.lock();
                    fileObject.move(lock, fileObject.getParent(), filter.getName(), "js");
                    lock.releaseLock();
                    fileObject = fileObject.getParent().getFileObject(filter.getName() + ".js");
                }

                FileLock lock = fileObject.lock();
                OutputStream os = fileObject.getOutputStream(lock);
                try (Writer w = new OutputStreamWriter(os)) {
                    String s = filter.getCode();
                    w.write(s);
                }
                lock.releaseLock();

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private class FilterChildren extends Children.Keys<Filter> implements ChangedListener<CheckNode> {

        private final HashMap<Filter, Node> nodeHash = new HashMap<>();

        public FilterChildren() {
            allFiltersOrdered.getChangedEvent().addListener(source -> addNotify());
            setBefore(false);
        }

        @Override
        protected Node[] createNodes(Filter filter) {
            if (nodeHash.containsKey(filter)) {
                return new Node[]{nodeHash.get(filter)};
            }

            FilterNode node = new FilterNode(filter);
            node.getSelectionChangedEvent().addListener(this);
            nodeHash.put(filter, node);
            return new Node[]{node};
        }

        @Override
        protected void addNotify() {
            setKeys(allFiltersOrdered.getFilters());
            updateSelection();
        }

        private void updateSelection() {
            Node[] nodes = getExplorerManager().getSelectedNodes();
            int[] arr = new int[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                int index = allFiltersOrdered.getFilters().indexOf(((FilterNode) nodes[i]).getFilter());
                arr[i] = index;
            }
            view.showSelection(arr);
        }

        @Override
        public void changed(CheckNode source) {
            FilterNode node = (FilterNode) source;
            Filter f = node.getFilter();
            FilterChain chain = getCurrentChain();
            if (node.isSelected()) {
                if (!chain.containsFilter(f)) {
                    chain.addFilter(f);
                }
            } else {
                if (chain.containsFilter(f)) {
                    chain.removeFilter(f);
                }
            }
            view.revalidate();
            view.repaint();
        }
    }
}
