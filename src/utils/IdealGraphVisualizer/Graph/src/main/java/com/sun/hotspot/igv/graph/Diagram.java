package com.sun.hotspot.igv.graph;

import com.sun.hotspot.igv.data.*;
import java.awt.Font;
import java.util.*;


public class Diagram {

    private List<Figure> figures;
    private final String nodeText;
    private final String shortNodeText;
    public static final Font FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font SLOT_FONT = new Font("Arial", Font.PLAIN, 10);
    public static final Font BOLD_FONT = FONT.deriveFont(Font.BOLD);


    public Diagram(InputGraph graph) {
        assert graph != null;
        this.nodeText = "[idx] [name]";
        this.shortNodeText = "[idx] [name]";
        this.figures = new ArrayList<>();
        int curId = 0;

        Collection<InputNode> nodes = graph.getNodes();
        Hashtable<Integer, Figure> figureHash = new Hashtable<>();
        for (InputNode n : nodes) {
            Figure f = new Figure(this, curId, n);
            curId++;
            f.getProperties().add(n.getProperties());
            figureHash.put(n.getId(), f);
            this.figures.add(f);
        }

        for (InputEdge e : graph.getEdges()) {
            int from = e.getFrom();
            int to = e.getTo();
            Figure fromFigure = figureHash.get(from);
            Figure toFigure = figureHash.get(to);

            if(fromFigure == null || toFigure == null) continue;

            int fromIndex = e.getFromIndex();
            while (fromFigure.getOutputSlots().size() <= fromIndex) {
                fromFigure.createOutputSlot();
            }
            OutputSlot outputSlot = fromFigure.getOutputSlots().get(fromIndex);

            int toIndex = e.getToIndex();
            while (toFigure.getInputSlots().size() <= toIndex) {
                toFigure.createInputSlot();
            }
            InputSlot inputSlot = toFigure.getInputSlots().get(toIndex);

            Connection c = createConnection(inputSlot, outputSlot, e.getLabel());

            if (e.getState() == InputEdge.State.NEW) {
                c.setStyle(Connection.ConnectionStyle.BOLD);
            } else if (e.getState() == InputEdge.State.DELETED) {
                c.setStyle(Connection.ConnectionStyle.DASHED);
            }
        }

        for (Figure f : figures) {
            int i = 0;
            for (InputSlot inputSlot : f.getInputSlots()) {
                inputSlot.setOriginalIndex(i);
                i++;
            }
        }
    }


    public String getNodeText() {
        return nodeText;
    }

    public String getShortNodeText() {
        return shortNodeText;
    }

    public List<Figure> getFigures() {
        return Collections.unmodifiableList(figures);
    }

    public Connection createConnection(InputSlot inputSlot, OutputSlot outputSlot, String label) {
        assert inputSlot.getFigure().getDiagram() == this;
        assert outputSlot.getFigure().getDiagram() == this;
        Connection connection = new Connection(inputSlot, outputSlot, label);

        // Connect the slots
        inputSlot.connections.add(connection);
        outputSlot.connections.add(connection);

        // Connect the figures
        Figure sourceFigure = outputSlot.getFigure();
        Figure destFigure = inputSlot.getFigure();
        sourceFigure.addSuccessor(destFigure);
        destFigure.addPredecessor(sourceFigure);
        return connection;
    }

    public void removeAllFigures(Set<Figure> figuresToRemove) {
        for (Figure f : figuresToRemove) {
            freeFigure(f);
        }

        ArrayList<Figure> newFigures = new ArrayList<>();
        for (Figure f : this.figures) {
            if (!figuresToRemove.contains(f)) {
                newFigures.add(f);
            }
        }
        figures = newFigures;
    }

    private void freeFigure(Figure succ) {
        List<InputSlot> inputSlots = new ArrayList<>(succ.getInputSlots());
        for (InputSlot s : inputSlots) {
            succ.removeInputSlot(s);
        }

        List<OutputSlot> outputSlots = new ArrayList<>(succ.getOutputSlots());
        for (OutputSlot s : outputSlots) {
            succ.removeOutputSlot(s);
        }
    }

    public void removeFigure(Figure succ) {
        assert this.figures.contains(succ);
        freeFigure(succ);
        this.figures.remove(succ);
    }

    public Set<Connection> getConnections() {
        Set<Connection> connections = new HashSet<>();
        for (Figure f : figures) {
            for (InputSlot s : f.getInputSlots()) {
                connections.addAll(s.getConnections());
            }
        }
        return connections;
    }
}
