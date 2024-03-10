package com.sun.hotspot.igv.filter;

import com.sun.hotspot.igv.graph.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CombineFilter extends AbstractFilter {

    private final List<CombineRule> rules;
    private final String name;

    public CombineFilter(String name) {
        this.name = name;
        rules = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(Diagram diagram) {
        for (CombineRule r : rules) {
            Set<Figure> first = r.getFirstSelector().selected(diagram);
            Set<Figure> second = r.getSecondSelector().selected(diagram);
            for (Figure f : first) {

                List<Figure> successors = new ArrayList<>(f.getSuccessors());
                if (r.isReversed()) {
                    if (successors.size() == 1) {
                        Figure succ = successors.get(0);
                        InputSlot slot = null;

                        for (InputSlot s : succ.getInputSlots()) {
                            for (Connection c : s.getConnections()) {
                                if (c.getOutputSlot().getFigure() == f) {
                                    slot = s;
                                    break;
                                }
                            }
                        }

                        assert slot != null;
                        slot.setSourceNode(f.getInputNode());
                        if (r.getPropertyNames() != null && r.getPropertyNames().length > 0) {
                            String s = r.getFirstMatchingProperty(f);
                            if (s != null && !s.isEmpty()) {
                                slot.setShortName(s);
                                slot.setText(s);
                                slot.setColor(f.getColor());
                            }
                        } else {
                            slot.setText(f.getProperties().get("dump_spec"));
                            slot.setColor(f.getColor());
                            if (f.getProperties().get("short_name") != null) {
                                slot.setShortName(f.getProperties().get("short_name"));
                            } else {
                                String s = f.getProperties().get("dump_spec");
                                if (s != null && s.length() <= 5) {
                                    slot.setShortName(s);
                                }
                            }
                        }

                        for (InputSlot s : f.getInputSlots()) {
                            for (Connection c : s.getConnections()) {
                                Connection newConn = diagram.createConnection(slot, c.getOutputSlot(), c.getLabel());
                                newConn.setColor(c.getColor());
                                newConn.setStyle(c.getStyle());
                            }
                        }
                    }
                } else {

                    for (Figure succ : successors) {
                        if (succ.getPredecessors().size() == 1) {
                            if (second.contains(succ) && succ.getOutputSlots().size() <= 1) {

                                OutputSlot oldSlot = null;
                                for (OutputSlot s : f.getOutputSlots()) {
                                    for (Connection c : s.getConnections()) {
                                        if (c.getInputSlot().getFigure() == succ) {
                                            oldSlot = s;
                                            break;
                                        }
                                    }
                                }

                                assert oldSlot != null;

                                OutputSlot nextSlot = null;
                                if (succ.getOutputSlots().size() == 1) {
                                    nextSlot = succ.getOutputSlots().get(0);
                                }

                                int pos = 0;
                                if (succ.getProperties().get("con") != null) {
                                    pos = Integer.parseInt(succ.getProperties().get("con"));
                                }
                                OutputSlot slot = f.createOutputSlot(pos);
                                slot.setSourceNode(succ.getInputNode());
                                if (r.getPropertyNames() != null && r.getPropertyNames().length > 0) {
                                    String s = r.getFirstMatchingProperty(succ);
                                    if (s != null && !s.isEmpty()) {
                                        slot.setShortName(s);
                                        slot.setText(s);
                                        slot.setColor(succ.getColor());
                                    }
                                } else {
                                    slot.setText(succ.getProperties().get("dump_spec"));
                                    slot.setColor(succ.getColor());
                                    if (succ.getProperties().get("short_name") != null) {
                                        slot.setShortName(succ.getProperties().get("short_name"));
                                    } else {
                                        String s = succ.getProperties().get("dump_spec");
                                        if (s != null && s.length() <= 2) {
                                            slot.setShortName(s);
                                        } else {
                                            String tmpName = succ.getProperties().get("name");
                                            if (tmpName != null && !tmpName.isEmpty()) {
                                                slot.setShortName(tmpName.substring(0, 1));
                                            }
                                        }
                                    }
                                }
                                if (nextSlot != null) {
                                    for (Connection c : nextSlot.getConnections()) {
                                        Connection newConn = diagram.createConnection(c.getInputSlot(), slot, c.getLabel());
                                        newConn.setColor(c.getColor());
                                        newConn.setStyle(c.getStyle());
                                    }
                                }

                                diagram.removeFigure(succ);

                                if (oldSlot.getConnections().isEmpty()) {
                                    f.removeSlot(oldSlot);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addRule(CombineRule combineRule) {
        rules.add(combineRule);
    }

    public static class CombineRule {

        private final Selector first;
        private final Selector second;
        private final boolean reversed;
        private final String[] propertyNames;

        public CombineRule(Selector first, Selector second, boolean reversed, String[] propertyNames) {
            this.first = first;
            this.second = second;
            this.reversed = reversed;
            this.propertyNames = propertyNames;
        }

        public boolean isReversed() {
            return reversed;
        }

        public Selector getFirstSelector() {
            return first;
        }

        public Selector getSecondSelector() {
            return second;
        }

        public String[] getPropertyNames() {
            return propertyNames;
        }

        public String getFirstMatchingProperty(Figure figure) {
            return AbstractFilter.getFirstMatchingProperty(figure, propertyNames);
        }
    }
}
