/*
 * TextSetting.java
 *
 * Created on 23.6.2009, 19:36:58
 */

package kandrm.JLatVis.gui.settings.visual;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;

/**
 *
 * @author Michal Kandr
 */
public class TextSetting extends javax.swing.JPanel {
    private boolean isSearch;
    private Color color;

    public TextSetting() {
        this(false);
    }
    public TextSetting(boolean isSearch) {
        this.isSearch = isSearch;
        initComponents();
    }

    public String getFontName(){
        return fontDisable.isSelected() ? null : fontComboBox.getSelectedItem().toString();
    }
    public void setFontName(String font){
        SettingUtils.showDisableIfNull(fontDisable, font, isSearch);
        if(font != null){
            fontComboBox.setSelectedItem(font);
        }
    }

    public Integer getFontSize(){
        return sizeDisable.isSelected() ? null : Integer.valueOf(sizeSpinner.getValue().toString());
    }
    public void setFontSize(Integer size){
        SettingUtils.showDisableIfNull(sizeDisable, size, isSearch);
        if(size != null) {
            sizeSpinner.setValue(size);
        }
    }

    public Color getFontColor() {
        return colorDisable.isSelected() ? null : color;
    }
    public void setFontColor(Color color) {
        SettingUtils.showDisableIfNull(colorDisable, color, isSearch);
        if(color != null){
            this.color = color;
            colorButton.setBackground(color);
        }
    }

    
    public Boolean isFontBold(){
        return boldDisable.isSelected() ? null : boldCheckBox.isSelected();
    }
    public void setFontBold(Boolean bold){
        SettingUtils.showDisableIfNull(boldDisable, bold, isSearch);
        if(bold != null){
            boldCheckBox.setSelected(bold);
        }
    }

    public Boolean isFontItalic(){
        return italicDisable.isSelected() ? null : italicCheckBox.isSelected();
    }
    public void setFontItalic(Boolean italic){
        SettingUtils.showDisableIfNull(italicDisable, italic, isSearch);
        if(italic != null){
            italicCheckBox.setSelected(italic);
        }
    }

    public Boolean isFontUnderline(){
        return underlineDisable.isSelected() ? null : underlineCheckBox.isSelected();
    }
    public void setFontUnderline(Boolean underline){
        SettingUtils.showDisableIfNull(underlineDisable, underline, isSearch);
        if(underline != null){
            underlineCheckBox.setSelected(underline);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        fontLabel = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        colorLabel = new javax.swing.JLabel();
        underlineCheckBox = new javax.swing.JCheckBox();
        italicCheckBox = new javax.swing.JCheckBox();
        boldCheckBox = new javax.swing.JCheckBox();
        sizeSpinner = new javax.swing.JSpinner();
        fontComboBox = new javax.swing.JComboBox();
        pxLabel = new javax.swing.JLabel();
        colorButton = new javax.swing.JButton();
        fontDisable = new javax.swing.JCheckBox();
        sizeDisable = new javax.swing.JCheckBox();
        colorDisable = new javax.swing.JCheckBox();
        boldDisable = new javax.swing.JCheckBox();
        italicDisable = new javax.swing.JCheckBox();
        underlineDisable = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        fontLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fontLabel.setText("Font");
        fontLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(fontLabel, gridBagConstraints);

        sizeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sizeLabel.setText("Size");
        sizeLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        add(sizeLabel, gridBagConstraints);

        colorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel.setText("Color");
        colorLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        add(colorLabel, gridBagConstraints);

        underlineCheckBox.setText("Underline");
        underlineCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        underlineCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        underlineCheckBox.setPreferredSize(new java.awt.Dimension(76, 23));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, underlineDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), underlineCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(underlineCheckBox, gridBagConstraints);

        italicCheckBox.setText("Italic");
        italicCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        italicCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        italicCheckBox.setPreferredSize(new java.awt.Dimension(76, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, italicDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), italicCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(italicCheckBox, gridBagConstraints);

        boldCheckBox.setText("Bold");
        boldCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        boldCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        boldCheckBox.setPreferredSize(new java.awt.Dimension(76, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, boldDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), boldCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(boldCheckBox, gridBagConstraints);

        sizeSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        sizeSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sizeDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), sizeSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(sizeSpinner, gridBagConstraints);

        fontComboBox.setModel(new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        fontComboBox.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, fontDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), fontComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(fontComboBox, gridBagConstraints);

        pxLabel.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        add(pxLabel, gridBagConstraints);

        colorButton.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, colorDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), colorButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(colorButton, gridBagConstraints);

        fontDisable.setText(SettingUtils.getDisableText(isSearch));
        fontDisable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontDisableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        add(fontDisable, gridBagConstraints);

        sizeDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(sizeDisable, gridBagConstraints);

        colorDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(colorDisable, gridBagConstraints);

        boldDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(boldDisable, gridBagConstraints);

        italicDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        add(italicDisable, gridBagConstraints);

        underlineDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        add(underlineDisable, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        Color selectedColor = JColorChooser.showDialog(this, "Choose color", color);
        if(selectedColor != null){
            setFontColor(selectedColor);
        }
}//GEN-LAST:event_colorButtonActionPerformed

    private void fontDisableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontDisableActionPerformed
        if( ! fontDisable.isSelected()){
            fontComboBox.setSelectedIndex(0);
        }
    }//GEN-LAST:event_fontDisableActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox boldCheckBox;
    private javax.swing.JCheckBox boldDisable;
    private javax.swing.JButton colorButton;
    private javax.swing.JCheckBox colorDisable;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JComboBox fontComboBox;
    private javax.swing.JCheckBox fontDisable;
    private javax.swing.JLabel fontLabel;
    private javax.swing.JCheckBox italicCheckBox;
    private javax.swing.JCheckBox italicDisable;
    private javax.swing.JLabel pxLabel;
    private javax.swing.JCheckBox sizeDisable;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSpinner sizeSpinner;
    private javax.swing.JCheckBox underlineCheckBox;
    private javax.swing.JCheckBox underlineDisable;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}
