package com.sun.hotspot.igv.filterwindow;

import com.sun.hotspot.igv.graph.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RemoveInputsFilter extends AbstractFilter {

    private final List<RemoveInputsRule> rules;
    private final String name;

    public RemoveInputsFilter(String name) {
        this.name = name;
        rules = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(Diagram diagram) {
        for (RemoveInputsRule r : rules) {
            Set<Figure> list = r.getNodeSelector().selected(diagram);
            Set<Figure> slotList = r.getSlotSelector().selected(diagram);
            for (Figure f : list) {
                for (InputSlot is : f.getInputSlots()) {
                    List<Connection> conns = is.getConnections();
                    if (conns.size() == 1) {
                        Figure i = conns.get(0).getOutputSlot().getFigure();
                        if (slotList.contains(i)) {
                            is.removeAllConnections();
                        }
                    }
                }
            }
        }
    }

    public void addRule(RemoveInputsRule rule) {
        rules.add(rule);
    }

    public static class RemoveInputsRule {

        private final Selector nodeSelector;
        private final Selector slotSelector;

        public RemoveInputsRule(Selector nodeSelector, Selector slotSelector) {
            this.nodeSelector = nodeSelector;
            this.slotSelector = slotSelector;
        }

        public Selector getNodeSelector() {
            return nodeSelector;
        }

        public Selector getSlotSelector() {
            return slotSelector;
        }
    }
}
