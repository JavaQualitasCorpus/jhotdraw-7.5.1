/*
 * @(#)PaletteSwatchesChooser.java
 * 
 * Copyright (c) 2010 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 * 
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.gui.plaf.palette.colorchooser;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ListModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import org.jhotdraw.draw.action.ButtonFactory;
import org.jhotdraw.draw.action.ColorIcon;
import org.jhotdraw.gui.plaf.palette.PaletteListUI;
import org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel;
import org.jhotdraw.gui.plaf.palette.PalettePanelUI;

/**
 * PaletteSwatchesChooser.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class PaletteSwatchesChooser extends AbstractColorChooserPanel {

    private int updateRecursion = 0;

    /** Creates new form PaletteSwatchesChooser */
    public PaletteSwatchesChooser() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jList = new javax.swing.JList();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 6, 6, 6));
        setLayout(new java.awt.GridBagLayout());

        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        add(jList, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateChooser() {
        if (updateRecursion++ == 0) {
            ListModel m = jList.getModel();
            Color mc = getColorFromModel();
            if (mc != null) {
                int ma = mc.getAlpha();
                int mr = mc.getRed();
                int mg = mc.getGreen();
                int mb = mc.getBlue();
                int closestSquaredDistance = Integer.MAX_VALUE;
                int closestIndex = -1;
                for (int i = 0, n = m.getSize(); i < n; i++) {
                    ColorIcon item = (ColorIcon) m.getElementAt(i);
                    Color ic = item.getColor();
                    int squaredDistance;
                    if (ic == null||ic.getAlpha()!=ma) {
                        squaredDistance = Integer.MAX_VALUE;
                    } else {
                        squaredDistance = (mr - ic.getRed()) * (mr - ic.getRed())
                                + (mg - ic.getGreen()) * (mg - ic.getGreen())
                                + (mb - ic.getBlue()) * (mb - ic.getBlue());
                    }
                    if (squaredDistance <= closestSquaredDistance) {
                        closestSquaredDistance = squaredDistance;
                        closestIndex = i;
                        if (squaredDistance == 0) {
                            break;
                        }
                    }
                }
                if (closestIndex == -1) {
                    jList.clearSelection();
                } else {
                    jList.setSelectedIndex(closestIndex);
                }
            }
        }
            updateRecursion--;
    }

    @Override
    public String getDisplayName() {
        return PaletteLookAndFeel.getInstance().getString("ColorChooser.colorSwatches");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return PaletteLookAndFeel.getInstance().getIcon("ColorChooser.colorSwatchesIcon");
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return getSmallDisplayIcon();
    }

    @Override
    protected void buildChooser() {
        initComponents();
        setUI(PalettePanelUI.createUI(this));
        jList.setUI((ListUI) PaletteListUI.createUI(jList));
        jList.setListData(ButtonFactory.HSB_COLORS_AS_RGB.toArray());
        jList.setVisibleRowCount(ButtonFactory.HSB_COLORS_AS_RGB.size() / ButtonFactory.HSB_COLORS_AS_RGB_COLUMN_COUNT);
        jList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (updateRecursion++ == 0) {
                    ColorIcon item = (ColorIcon) jList.getSelectedValue();
                    setColorToModel(item == null ? null : item.getColor());
                }
                updateRecursion--;
            }
        });
    }

    public void setColorToModel(Color color) {
        getColorSelectionModel().setSelectedColor(color);
    }
}
