package com.sun.hotspot.igv.filterwindow;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckRenderer extends JCheckBox implements ListCellRenderer<Object> {

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
                            CheckNode node = ((CheckNodeListModel) list.getModel()).getCheckNodeAt(index);
                            node.setSelected(!node.isSelected());
                            list.repaint();
                            e.consume();
                        }
                    }
                });

        this.setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height - 5));
        startBackground = this.getBackground();
    }

    @Override
    public Component getListCellRendererComponent(final JList<? extends Object> list, Object value, final int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        CheckNode node = ((CheckNodeListModel) list.getModel()).getCheckNodeAt(index);
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
