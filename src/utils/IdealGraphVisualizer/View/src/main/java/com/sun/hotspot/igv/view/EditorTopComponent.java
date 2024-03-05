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

    public EditorTopComponent(InputGraph graph) {
        initComponents();

        DiagramViewModel diagramViewModel = new DiagramViewModel(graph);
        LookupHistory.init(InputGraphProvider.class);
        scene = new DiagramScene(diagramViewModel);
        graphContent = new InstanceContent();
        InstanceContent content = new InstanceContent();
        content.add(diagramViewModel);
        associateLookup(new ProxyLookup(scene.getLookup(), new AbstractLookup(graphContent), new AbstractLookup(content)));

        setDisplayName(diagramViewModel.getGraph().getDisplayName());

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
        add(toolBar, BorderLayout.NORTH);
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


    public void addSelectedNodes(Collection<InputNode> nodes, boolean showIfHidden) {
        scene.addSelectedNodes(nodes, showIfHidden);
    }

    public void centerSelectedNodes() {
        scene.centerSelectedFigures();
    }

    public void clearSelectedNodes() {
        scene.clearSelectedNodes();
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

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
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
