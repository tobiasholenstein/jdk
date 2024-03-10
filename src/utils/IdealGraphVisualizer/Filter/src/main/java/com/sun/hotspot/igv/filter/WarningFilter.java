package com.sun.hotspot.igv.filter;

import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.graph.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarningFilter extends AbstractFilter {

    private final List<WarningRule> rules;
    private final String name;
    private final String warning;

    public WarningFilter(String name, String warning) {
        this.name = name;
        this.warning = warning;
        rules = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(Diagram diagram) {
        Properties.PropertySelector<Figure> selector = new Properties.PropertySelector<>(diagram.getFigures());
        for (WarningRule rule : rules) {
            if (rule.getSelector() != null) {
                Set<Figure> figures = rule.getSelector().selected(diagram);
                for (Figure f : figures) {
                    f.setWarning(warning);
                }
            }
        }
    }

    public void addRule(WarningRule r) {
        rules.add(r);
    }

    public static class WarningRule {

        private final Selector selector;

        public WarningRule(Selector selector) {
            this.selector = selector;
        }

        public Selector getSelector() {
            return selector;
        }
    }
}
