package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.data.ChangedEvent;
import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.filter.CustomFilter;
import com.sun.hotspot.igv.filter.Filter;
import com.sun.hotspot.igv.filter.FilterChain;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.explorer.view.NodeListModel;
import org.openide.explorer.view.Visualizer;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

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

    public FilterChain getFilterChain() {
        return defaultFilterChain;
    }

    public record FilterRecord(String fileName, String filterName, boolean selectedByDefault) {}


    public void initFilters() {
        System.out.println("initFilters");
        List<FilterRecord> filters = new ArrayList<>();
        String listPath = "com/sun/hotspot/igv/filterwindow/filterList.txt";

        try (InputStream listStream = FilterTopComponent.class.getClassLoader().getResourceAsStream(listPath);
             BufferedReader listReader = new BufferedReader(new InputStreamReader(listStream))) {

            String line;
            while ((line = listReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    String fileName = parts[0];
                    String filterName = parts[1];
                    boolean selectedByDefault = Boolean.parseBoolean(parts[2]);
                    filters.add(new FilterRecord(fileName, filterName, selectedByDefault));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<CustomFilter> customFilters = new ArrayList<>();
        Set<CustomFilter> enabledSet = new HashSet<>();

        String basePath = "com/sun/hotspot/igv/filterwindow/filters/";

        for (FilterRecord filter : filters) {
            String fileName = filter.fileName;
            String filterName = filter.filterName;
            boolean selectedByDefault = filter.selectedByDefault;

            try (InputStream inputStream = FilterTopComponent.class.getClassLoader().getResourceAsStream(basePath + fileName)) {
                if (inputStream == null) {
                    System.err.println("Could not find file: " + fileName);
                    continue;
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String s;
                    StringBuilder sb = new StringBuilder();
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                        sb.append("\n");
                    }
                    String code = sb.toString();

                    final CustomFilter cf = new CustomFilter(filterName, code, engine);
                    if (selectedByDefault) {
                        enabledSet.add(cf);
                    }
                    customFilters.add(cf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



//////


        for (CustomFilter cf : customFilters) {
            allFiltersOrdered.addFilter(cf);
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

    private class FilterChildren extends Children.Keys<Filter> implements ChangedListener<FilterNode> {

        public FilterChildren() {
            allFiltersOrdered.getChangedEvent().addListener(source -> addNotify());
            setBefore(false);
        }

        @Override
        protected Node[] createNodes(Filter filter) {
            FilterNode node = new FilterNode(filter);
            node.getSelectionChangedEvent().addListener(this);
            return new Node[]{node};
        }

        @Override
        protected void addNotify() {
            setKeys(allFiltersOrdered.getFilters());
            Node[] nodes = getExplorerManager().getSelectedNodes();
            int[] arr = new int[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                int index = allFiltersOrdered.getFilters().indexOf(((FilterNode) nodes[i]).getFilter());
                arr[i] = index;
            }
            view.showSelection(arr);
        }

        @Override
        public void changed(FilterNode filterNode) {
            Filter filter = filterNode.getFilter();
            FilterChain chain = getFilterChain();
            if (filterNode.isSelected()) {
                if (!chain.containsFilter(filter)) {
                    chain.addFilter(filter);
                }
            } else {
                if (chain.containsFilter(filter)) {
                    chain.removeFilter(filter);
                }
            }
            view.revalidate();
            view.repaint();
        }
    }

    private static class CheckListView extends ListView {

        public CheckListView() {}

        @Override
        public void showSelection(int[] indices) {
            super.showSelection(indices);
        }

        @Override
        protected NodeListModel createModel() {
            return new NodeListModel();
        }

        @Override
        protected JList<Object> createList() {
            JList<Object> tmpList = super.createList();
            tmpList.setCellRenderer(new CheckRenderer(tmpList));
            return tmpList;
        }

        private static class CheckRenderer extends JCheckBox implements ListCellRenderer<Object> {

            private final Color startBackground;

            public CheckRenderer(final JList<Object> list) {
                list.addMouseListener(
                        new MouseAdapter() {

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                int index = list.locationToIndex(e.getPoint());
                                Point p2 = list.indexToLocation(index);
                                Rectangle r = new Rectangle(p2.x, p2.y, getPreferredSize().height, getPreferredSize().height);
                                if (r.contains(e.getPoint())) {
                                    FilterNode node = getCheckNodeAt(index, (NodeListModel) list.getModel());
                                    node.setSelected(!node.isSelected());
                                    list.repaint();
                                    e.consume();
                                }
                            }
                        });

                this.setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height - 5));
                startBackground = this.getBackground();
            }

            public FilterNode getCheckNodeAt(int index, NodeListModel model) {
                Object item = model.getElementAt(index);
                if (item != null) {
                    return (FilterNode) Visualizer.findNode(item);
                }
                return null;
            }

            @Override
            public Component getListCellRendererComponent(final JList<? extends Object> list, Object value, final int index, boolean isSelected, boolean cellHasFocus) {
                setText(value.toString());
                FilterNode node = getCheckNodeAt(index, (NodeListModel) list.getModel());
                setSelected(node.isSelected());
                setEnabled(list.isEnabled());

                if (isSelected && list.hasFocus()) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else if (isSelected) {
                    assert !list.hasFocus();
                    setBackground(startBackground);
                    setForeground(list.getForeground());

                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                return this;
            }
        }
    }

    private static class FilterNode extends AbstractNode implements ChangedListener<FilterTopComponent> {

        private final Filter filter;

        public boolean selected;
        public boolean enabled;
        private final ChangedEvent<FilterNode> selectionChangedEvent;

        public ChangedEvent<FilterNode> getSelectionChangedEvent() {
            return selectionChangedEvent;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean b) {
            if (b != selected) {
                selected = b;
                selectionChangedEvent.fire();
            }
        }

        public FilterNode(Filter filter) {
            this(filter, new InstanceContent());
        }

        private FilterNode(Filter filter, InstanceContent content) {
            super(Children.LEAF, new AbstractLookup(content));
            selectionChangedEvent = new ChangedEvent<>(this);
            selected = false;
            enabled = true;


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
}
