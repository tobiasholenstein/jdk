package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.GraphDocument;
import com.sun.hotspot.igv.data.Group;
import com.sun.hotspot.igv.data.InputGraph;
import com.sun.hotspot.igv.data.InputNode;
import com.sun.hotspot.igv.data.services.InputGraphProvider;
import com.sun.hotspot.igv.util.LookupHistory;
import com.sun.hotspot.igv.util.RangeSlider;
import com.sun.hotspot.igv.view.actions.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import org.openide.actions.RedoAction;
import org.openide.actions.UndoAction;
import org.openide.awt.Toolbar;
import org.openide.awt.ToolbarPool;
import org.openide.awt.UndoRedo;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


public final class EditorTopComponent extends TopComponent {

    private final DiagramViewer scene;
    private final InstanceContent graphContent;

    public EditorTopComponent(DiagramViewModel diagramViewModel) {
        initComponents();

        LookupHistory.init(InputGraphProvider.class);
        setFocusable(true);

        setName(NbBundle.getMessage(EditorTopComponent.class, "CTL_EditorTopComponent"));
        setToolTipText(NbBundle.getMessage(EditorTopComponent.class, "HINT_EditorTopComponent"));

        Action[] actions = new Action[]{
                PrevDiagramAction.get(PrevDiagramAction.class),
                NextDiagramAction.get(NextDiagramAction.class),
                null,
                ExtractAction.get(ExtractAction.class),
                HideAction.get(HideAction.class),
                ShowAllAction.get(ShowAllAction.class),
                null,
                ZoomOutAction.get(ZoomOutAction.class),
                ZoomInAction.get(ZoomInAction.class),
        };

        Action[] actionsWithSelection = new Action[]{
                ExtractAction.get(ExtractAction.class),
                HideAction.get(HideAction.class),
        };


        scene = new DiagramScene(actions, actionsWithSelection, diagramViewModel);
        graphContent = new InstanceContent();
        InstanceContent content = new InstanceContent();
        content.add(diagramViewModel);
        associateLookup(new ProxyLookup(scene.getLookup(), new AbstractLookup(graphContent), new AbstractLookup(content)));

        Group group = diagramViewModel.getGroup();
        group.getChangedEvent().addListener(g -> closeOnRemovedOrEmptyGroup());
        if (group.getParent() instanceof GraphDocument doc) {
            doc.getChangedEvent().addListener(d -> closeOnRemovedOrEmptyGroup());
        }

        diagramViewModel.addTitleCallback(changedGraph -> {
            setDisplayName(changedGraph.getDisplayName());
            setToolTipText(diagramViewModel.getGroup().getDisplayName());
        });

        diagramViewModel.getGraphChangedEvent().addListener(this::graphChanged);


        getModel().setShowSea(true);

        add(scene.getComponent(), BorderLayout.CENTER);
        Toolbar toolBar = new Toolbar();
        toolBar.add(PrevDiagramAction.get(PrevDiagramAction.class));
        toolBar.add(NextDiagramAction.get(NextDiagramAction.class));
        toolBar.add(ExtractAction.get(ExtractAction.class));
        toolBar.add(HideAction.get(HideAction.class));
        toolBar.add(ShowAllAction.get(ShowAllAction.class));
        toolBar.add(new JToggleButton(new PredSuccAction(diagramViewModel.getShowNodeHull())));
        toolBar.add(UndoAction.get(UndoAction.class));
        toolBar.add(RedoAction.get(RedoAction.class));
        JToggleButton cutEdgesButton = new JToggleButton(CutEdgesAction.get(CutEdgesAction.class));
        cutEdgesButton.setHideActionText(true);
        toolBar.add(cutEdgesButton);
        JToggleButton globalSelectionButton = new JToggleButton(GlobalSelectionAction.get(GlobalSelectionAction.class));
        globalSelectionButton.setHideActionText(true);
        toolBar.add(globalSelectionButton);
        toolBar.add(new JToggleButton(new SelectionModeAction()));
        toolBar.add(new ZoomLevelAction(scene));

        // Needed for toolBar to use maximal available width
        //JPanel toolbarPanel = new JPanel(new GridLayout(1, 0));
        //toolbarPanel.add(toolBar);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        //topPanel.add(toolbarPanel);

        add(toolBar, BorderLayout.NORTH);

        graphChanged(diagramViewModel);
    }

    private void graphChanged(DiagramViewModel model) {
        setDisplayName(model.getGraph().getDisplayName());
        setToolTipText(model.getGroup().getDisplayName());
        graphContent.set(Collections.singletonList(new EditorInputGraphProvider(this)), null);
    }

    public DiagramViewModel getModel() {
        return scene.getModel();
    }

    public void setSelectionMode(boolean enable) {
        if (enable) {
            scene.setInteractionMode(DiagramViewer.InteractionMode.SELECTION);
        } else {
            scene.setInteractionMode(DiagramViewer.InteractionMode.PANNING);
        }
    }

    public void zoomOut() {
        scene.zoomOut(null, DiagramScene.ZOOM_INCREMENT);
    }

    public void zoomIn() {
        scene.zoomIn(null, DiagramScene.ZOOM_INCREMENT);
    }

    public void setZoomLevel(int percentage) {
        scene.setZoomPercentage(percentage);
    }

    public static boolean isOpen(EditorTopComponent editor) {
        return WindowManager.getDefault().isOpenedEditorTopComponent(editor);
    }

    public static EditorTopComponent getActive() {
        TopComponent topComponent = getRegistry().getActivated();
        if (topComponent instanceof EditorTopComponent) {
            return (EditorTopComponent) topComponent;
        }
        return null;
    }

    public static EditorTopComponent findEditorForGraph(InputGraph graph) {
        WindowManager manager = WindowManager.getDefault();
        for (Mode m : manager.getModes()) {
            List<TopComponent> l = new ArrayList<>();
            l.add(m.getSelectedTopComponent());
            l.addAll(Arrays.asList(manager.getOpenedTopComponents(m)));
            for (TopComponent t : l) {
                if (t instanceof EditorTopComponent) {
                    EditorTopComponent etc = (EditorTopComponent) t;
                    if (etc.getModel().getGroup().getGraphs().contains(graph)) {
                        return etc;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    private void closeOnRemovedOrEmptyGroup() {
        Group group = getModel().getGroup();
        if (!group.getParent().getElements().contains(group) ||
            group.getGraphs().isEmpty()) {
            close();
        }
    }

    public void addSelectedNodes(Collection<InputNode> nodes, boolean showIfHidden) {
        scene.addSelectedNodes(nodes, showIfHidden);
    }

    public void centerSelectedNodes() {
        scene.centerSelectedFigures();
    }

    public void clearSelectedNodes() {
        scene.clearSelectedNodes();
    }

    public Rectangle getSceneBounds() {
        return scene.getBounds();
    }

    public void paintScene(Graphics2D generator) {
        scene.paint(generator);
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        getModel().close();
        LookupHistory.terminate(InputGraphProvider.class);
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();
        scene.componentHidden();
    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
        scene.componentShowing();
    }

    @Override
    public void requestActive() {
        super.requestActive();
        scene.getComponent().requestFocus();
    }

    @Override
    public UndoRedo getUndoRedo() {
        return scene.getUndoRedo();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jCheckBox1 = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, "jCheckBox1");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    // End of variables declaration//GEN-END:variables
}
