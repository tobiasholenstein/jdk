package com.sun.hotspot.igv.filterspanel.actions;

import com.sun.hotspot.igv.filterspanel.FilterTopComponent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;


public class FilterAction extends AbstractAction {

    public FilterAction() {
        super(NbBundle.getMessage(FilterAction.class, "CTL_FilterAction"));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = FilterTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
