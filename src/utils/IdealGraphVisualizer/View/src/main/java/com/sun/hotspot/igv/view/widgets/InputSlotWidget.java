package com.sun.hotspot.igv.view.widgets;

import com.sun.hotspot.igv.graph.InputSlot;
import com.sun.hotspot.igv.view.DiagramScene;

public class InputSlotWidget extends SlotWidget {

    public InputSlotWidget(InputSlot slot, DiagramScene scene, FigureWidget fw) {
        super(slot, scene, fw);
    }

    @Override
    protected int yOffset() {
        return 0;
    }
}
