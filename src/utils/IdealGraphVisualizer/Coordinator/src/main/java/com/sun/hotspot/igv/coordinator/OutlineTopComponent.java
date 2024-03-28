/*
 * Copyright (c) 2008, 2024, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.hotspot.igv.coordinator;

import com.sun.hotspot.igv.connection.Server;
import com.sun.hotspot.igv.coordinator.actions.*;
import com.sun.hotspot.igv.data.*;
import com.sun.hotspot.igv.data.serialization.ParseMonitor;
import com.sun.hotspot.igv.data.serialization.Parser;
import com.sun.hotspot.igv.data.serialization.Printer;
import com.sun.hotspot.igv.data.serialization.Printer.GraphContext;
import com.sun.hotspot.igv.data.serialization.Printer.SerialData;
import com.sun.hotspot.igv.data.services.GraphViewer;
import com.sun.hotspot.igv.data.services.GroupCallback;
import com.sun.hotspot.igv.data.services.InputGraphProvider;
import com.sun.hotspot.igv.settings.Settings;
import com.sun.hotspot.igv.util.LookupHistory;
import com.sun.hotspot.igv.view.EditorTopComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.ErrorManager;
import org.openide.awt.Toolbar;
import org.openide.awt.ToolbarPool;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import org.openide.util.*;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Thomas Wuerthinger
 */
public final class OutlineTopComponent extends TopComponent implements ExplorerManager.Provider, ChangedListener<InputGraphProvider> {

    public static OutlineTopComponent instance;
    public static final String PREFERRED_ID = "OutlineTopComponent";
    private ExplorerManager manager;
    private static final GraphDocument document = new GraphDocument();
    private FolderNode root;
    private SaveAction saveAction;
    private SaveAsAction saveAsAction;
    private RemoveAllAction removeAllAction;
    private GraphNode[] selectedGraphs = new GraphNode[0];
    private final Set<FolderNode> selectedFolders = new HashSet<>();
    private static final int WORK_UNITS = 10000;
    private static final RequestProcessor RP = new RequestProcessor("OutlineTopComponent", 1);
    private Path documentPath = null;


    private OutlineTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(OutlineTopComponent.class, "CTL_OutlineTopComponent"));
        setToolTipText(NbBundle.getMessage(OutlineTopComponent.class, "HINT_OutlineTopComponent"));
        initListView();
        initToolbar();
        initReceivers();
    }

    private void initListView() {
        setDocumentPath(null);
        FolderNode.clearGraphNodeMap();
        document.clear();
        root = new FolderNode(document);
        manager = new ExplorerManager();
        manager.setRootContext(root);
        ((BeanTreeView) this.treeView).setRootVisible(false);
        associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
    }

    private void initToolbar() {
        Toolbar toolbar = new Toolbar();
        toolbar.setBorder((Border) UIManager.get("Nb.Editor.Toolbar.border")); //NOI18N
        toolbar.setMinimumSize(new Dimension(0,0)); // MacOS BUG with ToolbarWithOverflow

        this.add(toolbar, BorderLayout.NORTH);

        toolbar.add(OpenAction.get(OpenAction.class));
        toolbar.addSeparator();

        saveAction = SaveAction.get(SaveAction.class);
        saveAction.setEnabled(false);
        toolbar.add(saveAction);
        saveAsAction = SaveAsAction.get(SaveAsAction.class);
        saveAsAction.setEnabled(false);
        toolbar.add(saveAsAction);

        toolbar.addSeparator();
        toolbar.add(ImportAction.get(ImportAction.class));
        toolbar.add(ExportAction.get(ExportAction.class).createContextAwareInstance(this.getLookup()));

        toolbar.addSeparator();

        toolbar.add(RemoveAction.get(RemoveAction.class).createContextAwareInstance(this.getLookup()));
        removeAllAction = RemoveAllAction.get(RemoveAllAction.class);
        removeAllAction.setEnabled(false);
        toolbar.add(removeAllAction);

        for (Toolbar tb : ToolbarPool.getDefault().getToolbars()) {
            tb.setVisible(false);
        }

        document.getChangedEvent().addListener(g -> documentChanged());
    }

    private void documentChanged() {
        boolean enableButton = !document.getElements().isEmpty();
        saveAction.setEnabled(enableButton);
        saveAsAction.setEnabled(enableButton);
        removeAllAction.setEnabled(enableButton);
    }

    private void initReceivers() {
        final GroupCallback callback = g -> {
            synchronized(OutlineTopComponent.this) {
                g.setParent(getDocument());
                getDocument().addElement(g);
            }
        };

        new Server(callback);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public static GraphDocument getDocument() {
        return document;
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance()}.
     */
    public static synchronized OutlineTopComponent getDefault() {
        if (instance == null) {
            instance = new OutlineTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the OutlineTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized OutlineTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Outline component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof OutlineTopComponent) {
            return (OutlineTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        LookupHistory.addListener(InputGraphProvider.class, this);
        this.requestActive();
    }

    @Override
    public void componentClosed() {
        LookupHistory.removeListener(InputGraphProvider.class, this);
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public void requestActive() {
        super.requestActive();
        treeView.requestFocus();
    }

    @Override
    public boolean requestFocus(boolean temporary) {
        treeView.requestFocus();
        return super.requestFocus(temporary);
    }

    @Override
    protected boolean requestFocusInWindow(boolean temporary) {
        treeView.requestFocus();
        return super.requestFocusInWindow(temporary);
    }

    @Override
    public void changed(InputGraphProvider lastProvider) {
        for (GraphNode graphNode : selectedGraphs) {
            graphNode.setSelected(false);
        }
        for (FolderNode folderNode : selectedFolders) {
            folderNode.setSelected(false);
        }
        selectedGraphs = new GraphNode[0];
        selectedFolders.clear();
        if (lastProvider != null) {
            // Try to fetch and select the latest active graph.
            InputGraph graph = lastProvider.getGraph();
            if (graph != null) {
                if (graph.isDiffGraph()) {
                    InputGraph firstGraph = graph.getFirstGraph();
                    GraphNode firstNode = FolderNode.getGraphNode(firstGraph);
                    InputGraph secondGraph = graph.getSecondGraph();
                    GraphNode secondNode = FolderNode.getGraphNode(secondGraph);
                    if (firstNode != null && secondNode != null) {
                        selectedGraphs = new GraphNode[]{firstNode, secondNode};
                    }
                } else {
                    GraphNode graphNode = FolderNode.getGraphNode(graph);
                    if (graphNode != null) {
                        selectedGraphs = new GraphNode[]{graphNode};
                    }
                }
            }
        }
        try {
            for (GraphNode graphNode : selectedGraphs) {
                Node parentNode = graphNode.getParentNode();
                if (parentNode instanceof FolderNode) {
                    FolderNode folderNode = (FolderNode) graphNode.getParentNode();
                    folderNode.setSelected(true);
                    selectedFolders.add(folderNode);
                }
                graphNode.setSelected(true);
            }
            manager.setSelectedNodes(selectedGraphs);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    @Override
    public boolean canClose() {
        SwingUtilities.invokeLater(() -> {
            this.clearWorkspace();
            this.open(); // Reopen the OutlineTopComponent
            this.requestActive();
        });
        return true;
    }

    @Override
    public void setDisplayName(String fileName) {
        super.setDisplayName(fileName);
        setHtmlDisplayName(Objects.requireNonNullElse(fileName, "<html><i>untitled</i></html>"));
    }

    private void setDocumentPath(String path) {
        if (path != null) {
            documentPath = Paths.get(path);
            setDisplayName(path);
            setToolTipText("File: " + path);
        } else {
            documentPath = null;
            setDisplayName("untitled");
            setToolTipText("No file" );
        }

    }

    private String getDocumentPath() {
        return documentPath.toAbsolutePath().toString();
    }

    public void clearWorkspace() {
        setDocumentPath(null);
        document.clear();
        FolderNode.clearGraphNodeMap();
        root = new FolderNode(document);
        manager.setRootContext(root);
    }

    public void openFile() {
        JFileChooser fc = new JFileChooser(Settings.get().get(Settings.DIRECTORY, Settings.DIRECTORY_DEFAULT));
        fc.setFileFilter(xmlFileFilter);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            clearWorkspace();
            String path = fc.getSelectedFile().getAbsolutePath();
            Settings.get().put(Settings.DIRECTORY, path);
            setDocumentPath(path);
            try {
                loadGraphDocument(path, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void save() {
        String filePath = getDocumentPath();
        boolean exists = Files.exists(Paths.get(filePath));
        if (exists) {
            try {
                saveGraphDocument(getDocument(), filePath, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            saveAs();
        }
    }

    public void saveAs() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(xmlFileFilter);
        fc.setCurrentDirectory(new File(Settings.get().get(Settings.DIRECTORY, Settings.DIRECTORY_DEFAULT)));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            Settings.get().put(Settings.DIRECTORY, path);
            setDocumentPath(path);
            try {
                saveGraphDocument(getDocument(), path, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static final FileFilter xmlFileFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
        }

        @Override
        public String getDescription() {
            return "Graph files (*.xml)";
        }
    };


    public static void exportToXML(GraphDocument doc) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(xmlFileFilter);
        fc.setCurrentDirectory(new File(Settings.get().get(Settings.DIRECTORY, Settings.DIRECTORY_DEFAULT)));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            try {
                saveGraphDocument(doc, path, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void importFromXML() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(xmlFileFilter);
        fc.setCurrentDirectory(new File(Settings.get().get(Settings.DIRECTORY, Settings.DIRECTORY_DEFAULT)));
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            for (final File file : fc.getSelectedFiles()) {
                String path = fc.getSelectedFile().getAbsolutePath();
                Settings.get().put(Settings.DIRECTORY, path);
                try {
                    loadGraphDocument(path, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void loadContexts(Set<GraphContext> contexts) {
        RP.post(() -> {
            final GraphViewer viewer = Lookup.getDefault().lookup(GraphViewer.class);
            assert viewer != null;
            for (GraphContext context : contexts) {

                final int difference = context.posDiff().get();
                final Set<Integer> hiddenNodes = context.hiddenNodes();
                final InputGraph firstGraph = context.inputGraph();

                SwingUtilities.invokeLater(() -> {
                    InputGraph openedGraph;
                    if (difference > 0) {
                        Group group = firstGraph.getGroup();
                        int firstGraphIdx = group.getGraphs().indexOf(firstGraph);
                        final InputGraph secondGraph = group.getGraphs().get(firstGraphIdx + difference);
                        openedGraph = viewer.viewDifference(firstGraph, secondGraph);
                    } else {
                        openedGraph = viewer.view(firstGraph, true);
                    }
                    if (openedGraph != null) {
                        EditorTopComponent etc = EditorTopComponent.findEditorForGraph(openedGraph);
                        if (etc != null) {
                            etc.getModel().setHiddenNodes(hiddenNodes);
                        }
                    }
                });
            }
        });
    }


    private void loadGraphDocument(String path,  boolean loadContext) throws IOException {
        RP.post(() -> {
            if (path == null || Files.notExists(Path.of(path))) {
                return;
            }
            File file = new File(path);
            final FileChannel channel;
            final long start;
            try {
                channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
                start = channel.size();
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
                return;
            }

            final ProgressHandle handle = ProgressHandleFactory.createHandle("Opening file " + file.getName());
            handle.start(WORK_UNITS);

            ParseMonitor monitor = new ParseMonitor() {
                @Override
                public void updateProgress() {
                    try {
                        int prog = (int) (WORK_UNITS * (double) channel.position() / (double) start);
                        handle.progress(prog);
                    } catch (IOException ignored) {}
                }
                @Override
                public void setState(String state) {
                    updateProgress();
                    handle.progress(state);
                }
            };
            try {
                if (file.getName().endsWith(".xml")) {
                    final Parser parser = new Parser(channel, monitor, null);
                    parser.setInvokeLater(false);
                    final SerialData<GraphDocument> parsedData = parser.parse();
                    final GraphDocument parsedDoc = parsedData.data();
                    if (loadContext) {
                        final Set<GraphContext> parsedContexts = parsedData.contexts();
                        loadContexts(parsedContexts);
                    }
                    getDocument().addGraphDocument(parsedDoc);
                    SwingUtilities.invokeLater(this::requestActive);
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            handle.finish();
        });
    }

    private static void saveGraphDocument(GraphDocument doc, String path, boolean saveContext) throws IOException {
        if (path == null || Files.notExists(Path.of(path))) {
            return;
        }

        Set<GraphContext> saveContexts = new HashSet<>();
        if (saveContext) {
            WindowManager manager = WindowManager.getDefault();
            for (Mode mode : manager.getModes()) {
                List<TopComponent> compList = new ArrayList<>(Arrays.asList(manager.getOpenedTopComponents(mode)));
                for (TopComponent comp : compList) {
                    if (comp instanceof EditorTopComponent etc) {
                        GraphContext graphContext = getGraphContext(etc);
                        saveContexts.add(graphContext);
                    }
                }
            }
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(path))) {
            Printer printer = new Printer();
            printer.exportGraphDocument(writer, new SerialData<>(doc, saveContexts));
        }
    }

    private static GraphContext getGraphContext(EditorTopComponent etc) {
        InputGraph openedGraph = etc.getModel().getFirstGraph();
        int posDiff = etc.getModel().getSecondPosition() - etc.getModel().getFirstPosition();
        Set<Integer> hiddenNodes = new HashSet<>(etc.getModel().getHiddenNodes());
        return new GraphContext(openedGraph, new AtomicInteger(posDiff), hiddenNodes);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treeView = new BeanTreeView();

        setLayout(new BorderLayout());
        add(treeView, BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JScrollPane treeView;
    // End of variables declaration//GEN-END:variables
}
