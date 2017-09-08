package kandrm.JLatVis.gui.settings.visual;

import kandrm.JLatVis.guiConnect.settings.visual.TagModel;
import kandrm.JLatVis.guiConnect.settings.visual.TagLineToModel;
import java.awt.Frame;
import kandrm.JLatVis.gui.GuiUtils;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings.LineDests;
import kandrm.JLatVis.lattice.visual.settings.patterns.TagSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class TagSettings extends javax.swing.JDialog {
    private TagModel settingsModel;
    private boolean isSearch;

    private TagLineToModel lineToModel = new TagLineToModel();

    private History history;
    
    public TagSettings(Frame parent){
        this(parent, false);
    }
    public TagSettings(Frame parent, boolean isSearch) {
        this(parent, new TagModel(), isSearch);
    }
    public TagSettings(Frame parent, TagModel settings, boolean isSearch) {
        super(parent, true);
        this.isSearch = isSearch;
        initComponents();
        settingsModel = settings;
        setDefaultValues();
        if(isSearch){
            setTitle("Tag visual search");
        }
    }

    public TagModel getModel(){
        return settingsModel;
    }
    public void setModel(TagModel settings) {
        settings.setHistory(history);
        settingsModel = settings;
    }

    public void setHistory(History history){
        this.history = history;
        settingsModel.setHistory(history);
    }
    
    @Override
    public void setVisible(boolean visible){
        if(visible){
            if( ! isSearch && settingsModel.size() < 1){
                GuiUtils.showSetNoSel(getParent(), "tags");
                return;
            }
            setDefaultValues();
        }
        super.setVisible(visible);
    }

    private void setDistance(Float distance){
        SettingUtils.showDisableIfNull(distanceDisable, distance, isSearch);
        if(distance != null){
            distanceSpinner.setValue(distance);
        }
    }
    private Float getDistance(){
        return distanceDisable.isSelected() ? null : Float.parseFloat(distanceSpinner.getValue().toString());
    }

    private void setAngle(Float angle){
        SettingUtils.showDisableIfNull(angleDisable, angle, isSearch);
        if(angle != null){
            angleSpinner.setValue(angle);
        }
    }
    private Float getAngle(){
        return angleDisable.isSelected() ? null : Float.parseFloat(angleSpinner.getValue().toString());
    }

    private void setLineTo(LineDests lineTo){
        SettingUtils.showDisableIfNull(lineToDisable, lineTo, isSearch);
        if(lineTo != null){
            lineToComboBox.setSelectedItem(lineToModel.getNameByLineTo(lineTo));
        }
    }
    private LineDests getLineTo(){
        return lineToDisable.isSelected() ? null : lineToModel.getLineTo(lineToComboBox.getSelectedIndex());
    }

    private void setLineStartDist(Integer dist){
        SettingUtils.showDisableIfNull(lineStartDistDisable, dist, isSearch);
        if(dist != null){
            lineStartDistSpinner.setValue(dist);
        }
    }
    private Integer getLineStartDist(){
        return lineStartDistDisable.isSelected() ? null : Integer.parseInt(lineStartDistSpinner.getValue().toString());
    }

    private void setTagVisible(Boolean visible){
        SettingUtils.showDisableIfNull(visibleDisable, visible, isSearch);
        if(visible != null){
            visibleCheckBox.setSelected(visible);
        }
    }
    private Boolean getTagVisible(){
        return visibleDisable.isSelected() ? null : visibleCheckBox.isSelected();
    }


    private void setDefaultValues(){
        TagSettingPattern settingPattern = settingsModel.getSettingPattern();

        setDistance(settingPattern.getDistanceFromNode());
        setAngle(settingPattern.getAngleFromHoriz());
        setLineTo(settingPattern.getLineTo());
        setLineStartDist(settingPattern.getLineStartDist());
        setTagVisible(settingPattern.isVisible());

        shapeSetting.setShapeWidth(settingPattern.getWidth());
        shapeSetting.setShapeHeigh(settingPattern.getHeight());
        shapeSetting.setShapeAngle(settingPattern.getAngle());
        shapeSetting.setBackgroundColor(settingPattern.getBackgroundColor());

        lineSetting.setLineWidth(settingPattern.getConnectLineWidth());
        lineSetting.setLineDashing(settingPattern.getConnectLineDashing());
        lineSetting.setLineColor(settingPattern.getConnectLineColor());

        borderSetting.setLineWidth(settingPattern.getBorderWidth());
        borderSetting.setLineDashing(settingPattern.getBorderDashing());
        borderSetting.setLineColor(settingPattern.getBorderColor());

        textSetting.setFontName(settingPattern.getFontName());
        textSetting.setFontSize(settingPattern.getFontSize());
        textSetting.setFontColor(settingPattern.getTextColor());
        textSetting.setFontBold(settingPattern.isFontBold());
        textSetting.setFontItalic(settingPattern.isFontItalic());
        textSetting.setFontUnderline(settingPattern.isFontUnderline());
    }

    
    private void saveSettings(){
        TagSettingPattern newPattern = new TagSettingPattern(
            shapeSetting.getShapeWidth(),
            shapeSetting.getShapeHeigh(),
            shapeSetting.getShapeAngle(),
            shapeSetting.getBackgroundColor(),

            borderSetting.getLineColor(),
            borderSetting.getLineWidth(),
            borderSetting.getLineDashing(),

            textSetting.getFontColor(),
            textSetting.getFontName(),
            textSetting.getFontSize(),
            textSetting.isFontBold(),
            textSetting.isFontItalic(),
            textSetting.isFontUnderline(),
            null,

            lineSetting.getLineColor(),
            lineSetting.getLineWidth(),
            lineSetting.getLineDashing(),

            getTagVisible(),

            getDistance(),
            getAngle(),
            getLineTo(),
            getLineStartDist()
        );

        settingsModel.setSettingPattern(newPattern);

        settingsModel.apply();
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

        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        distanceLabel = new javax.swing.JLabel();
        distanceSpinner = new javax.swing.JSpinner();
        pxLabel1 = new javax.swing.JLabel();
        angleLabel = new javax.swing.JLabel();
        angleSpinner = new javax.swing.JSpinner();
        degreeLabel = new javax.swing.JLabel();
        lineToLabel = new javax.swing.JLabel();
        lineToComboBox = new javax.swing.JComboBox();
        distanceDisable = new javax.swing.JCheckBox();
        angleDisable = new javax.swing.JCheckBox();
        lineToDisable = new javax.swing.JCheckBox();
        lineStartDistLabel = new javax.swing.JLabel();
        lineStartDistDisable = new javax.swing.JCheckBox();
        lineStartDistSpinner = new javax.swing.JSpinner();
        pxLabel2 = new javax.swing.JLabel();
        visibleCheckBox = new javax.swing.JCheckBox();
        visibleDisable = new javax.swing.JCheckBox();
        shapePanel = new javax.swing.JPanel();
        shapeSetting = new kandrm.JLatVis.gui.settings.visual.ShapeSetting(isSearch, true);
        borderPanel = new javax.swing.JPanel();
        borderSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        linePanel = new javax.swing.JPanel();
        lineSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        textPanel = new javax.swing.JPanel();
        textSetting = new kandrm.JLatVis.gui.settings.visual.TextSetting(isSearch);

        jLabel19.setText("jLabel19");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tag visual settings");
        setResizable(false);

        saveButton.setText(isSearch ? "Search" : "Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        generalPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        distanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        distanceLabel.setText("Distance");
        distanceLabel.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel1.add(distanceLabel, gridBagConstraints);

        distanceSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        distanceSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, distanceDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), distanceSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel1.add(distanceSpinner, gridBagConstraints);

        pxLabel1.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(pxLabel1, gridBagConstraints);

        angleLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        angleLabel.setText("Angle");
        angleLabel.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(angleLabel, gridBagConstraints);

        angleSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        angleSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, angleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), angleSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(angleSpinner, gridBagConstraints);

        degreeLabel.setText("deg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(degreeLabel, gridBagConstraints);

        lineToLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lineToLabel.setText("Line to");
        lineToLabel.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(lineToLabel, gridBagConstraints);

        lineToComboBox.setModel(lineToModel);
        lineToComboBox.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, lineToDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), lineToComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(lineToComboBox, gridBagConstraints);

        distanceDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(distanceDisable, gridBagConstraints);

        angleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(angleDisable, gridBagConstraints);

        lineToDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(lineToDisable, gridBagConstraints);

        lineStartDistLabel.setText("Line from node");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(lineStartDistLabel, gridBagConstraints);

        lineStartDistDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(lineStartDistDisable, gridBagConstraints);

        lineStartDistSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        lineStartDistSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, lineStartDistDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), lineStartDistSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(lineStartDistSpinner, gridBagConstraints);

        pxLabel2.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(pxLabel2, gridBagConstraints);

        visibleCheckBox.setText("Visible");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, visibleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), visibleCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(visibleCheckBox, gridBagConstraints);

        visibleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(visibleDisable, gridBagConstraints);

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", generalPanel);

        shapePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        shapePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        shapePanel.add(shapeSetting);

        jTabbedPane1.addTab("Shape", shapePanel);

        borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        borderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        borderPanel.add(borderSetting);

        jTabbedPane1.addTab("Border", borderPanel);

        linePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 20));
        linePanel.add(lineSetting);

        jTabbedPane1.addTab("Line to node", linePanel);

        textPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        textPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        textPanel.add(textSetting);

        jTabbedPane1.addTab("Text", textPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("General");

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveSettings();
        dispose();
}//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox angleDisable;
    private javax.swing.JLabel angleLabel;
    private javax.swing.JSpinner angleSpinner;
    private javax.swing.JPanel borderPanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting borderSetting;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel degreeLabel;
    private javax.swing.JCheckBox distanceDisable;
    private javax.swing.JLabel distanceLabel;
    private javax.swing.JSpinner distanceSpinner;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel linePanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting lineSetting;
    private javax.swing.JCheckBox lineStartDistDisable;
    private javax.swing.JLabel lineStartDistLabel;
    private javax.swing.JSpinner lineStartDistSpinner;
    private javax.swing.JComboBox lineToComboBox;
    private javax.swing.JCheckBox lineToDisable;
    private javax.swing.JLabel lineToLabel;
    private javax.swing.JLabel pxLabel1;
    private javax.swing.JLabel pxLabel2;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel shapePanel;
    private kandrm.JLatVis.gui.settings.visual.ShapeSetting shapeSetting;
    private javax.swing.JPanel textPanel;
    private kandrm.JLatVis.gui.settings.visual.TextSetting textSetting;
    private javax.swing.JCheckBox visibleCheckBox;
    private javax.swing.JCheckBox visibleDisable;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}
