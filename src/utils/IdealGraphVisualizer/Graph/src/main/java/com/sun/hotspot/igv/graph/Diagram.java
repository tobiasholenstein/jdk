package com.sun.hotspot.igv.graph;

import com.sun.hotspot.igv.data.InputEdge;
import com.sun.hotspot.igv.data.InputGraph;
import com.sun.hotspot.igv.data.InputNode;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Diagram {

    public static final Font FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font SLOT_FONT = new Font("Arial", Font.PLAIN, 10);
    private final Set<Figure> figures;
    private final Set<Figure> hiddenFigures;
    private final Set<Figure> selectedFigures;
    private boolean showBoundaryFigures;


    public Diagram(InputGraph graph) {
        showBoundaryFigures = true;

        figures = new HashSet<>();
        hiddenFigures = new HashSet<>();
        selectedFigures = new HashSet<>();

        int curId = 0;
        Collection<InputNode> nodes = graph.getNodes();
        Hashtable<Integer, Figure> figureHash = new Hashtable<>();
        for (InputNode n : nodes) {
            Figure f = new Figure(this, curId, n);
            curId++;
            f.getProperties().add(n.getProperties());
            figureHash.put(n.getId(), f);
            figures.add(f);
        }

        for (InputEdge e : graph.getEdges()) {
            int from = e.getFrom();
            int to = e.getTo();
            Figure fromFigure = figureHash.get(from);
            Figure toFigure = figureHash.get(to);

            if (fromFigure == null || toFigure == null) continue;

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

    public boolean getShowBoundaryFigures() {
        return showBoundaryFigures;
    }

    public void setShowBoundaryFigures(boolean show) {
        showBoundaryFigures = show;
    }

    public List<Integer> getHiddenNodesByID() {
        return hiddenFigures.stream()
                .map(hiddenFigure -> hiddenFigure.getInputNode().getId())
                .toList();
    }

    public void extractFigure(Figure figure) {
        hiddenFigures.clear();
        hiddenFigures.addAll(figures);
        hiddenFigures.remove(figure);
    }

    public void selectFigureExclusively(Figure figure) {
        selectedFigures.clear();
        selectedFigures.add(figure);
    }

    public void hideFigure(Figure figure) {
        hiddenFigures.add(figure);
    }

    public void showFigure(Figure figure) {
        hiddenFigures.remove(figure);
    }

    public boolean allFiguresVisible() {
        return hiddenFigures.isEmpty();
    }

    public void showAllFigures() {
        hiddenFigures.clear();
    }

    public boolean isFigureSelected(Figure figure) {
        return selectedFigures.contains(figure);
    }

    public void extractSelectedFigures() {
        hiddenFigures.clear();
        hiddenFigures.addAll(figures);
        hiddenFigures.removeAll(selectedFigures);
    }

    public void hideSelectedFigures() {
        hiddenFigures.addAll(selectedFigures);
    }

    public void updateFigureVisibility() {
        // Initially set all figures to not boundary and update visibility based on hiddenNodesByID
        figures.forEach(figure -> {
            figure.setBoundary(false);
            figure.setVisible(!hiddenFigures.contains(figure));
        });
        selectedFigures.removeAll(hiddenFigures);

        if (showBoundaryFigures) {
            // Marks non-visible figures with visible neighbors as boundary figures and makes them visible
            figures.stream()
                    .filter(figure -> !figure.isVisible())
                    .filter(figure -> Stream.concat(figure.getPredecessors().stream(), figure.getSuccessors().stream())
                            .anyMatch(Figure::isVisible))
                    .peek(figure -> figure.setBoundary(true))
                    .toList() // needed!
                    .forEach(figure -> figure.setVisible(true));
        }
    }

    public String getNodeText() {
        return "[idx] [name]";
    }

    public String getShortNodeText() {
        return "[idx] [name]";
    }

    public Set<Figure> getFigures() {
        return Collections.unmodifiableSet(figures);
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
        for (Figure figure : figuresToRemove) {
            removeFigure(figure);
        }
    }

    public void removeFigure(Figure figure) {
        if (figures.contains(figure)) {
            freeFigureSlots(figure);
            figures.remove(figure);
        }
    }

    private void freeFigureSlots(Figure Figure) {
        List<InputSlot> inputSlots = new ArrayList<>(Figure.getInputSlots());
        for (InputSlot inputSlot : inputSlots) {
            Figure.removeInputSlot(inputSlot);
        }

        List<OutputSlot> outputSlots = new ArrayList<>(Figure.getOutputSlots());
        for (OutputSlot outputSlot : outputSlots) {
            Figure.removeOutputSlot(outputSlot);
        }
    }

    public Set<Figure> getVisibleFigures() {
        return figures.stream().filter(Figure::isVisible).collect(Collectors.toSet());
    }

    public Set<Connection> getVisibleConnections() {
        return figures.stream()
                .flatMap(figure -> figure.getInputSlots().stream()) // all input slots of all figures
                .flatMap(inputSlot -> inputSlot.getConnections().stream()) // all connections of all input slots
                .filter(Connection::isVisible) // only include visible connections
                .collect(Collectors.toSet());
    }
}
