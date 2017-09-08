package kandrm.JLatVis.gui.settings.visual;

import kandrm.JLatVis.guiConnect.settings.visual.NodeTypesModel;
import kandrm.JLatVis.guiConnect.settings.visual.NodeModel;
import java.awt.Frame;
import kandrm.JLatVis.gui.GuiUtils;
import kandrm.JLatVis.guiConnect.settings.visual.NodeNameModel;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeSettings extends javax.swing.JDialog {
    private NodeShapeTypes nodeShapeTypes = NodeShapeTypes.getInstance();

    private NodeModel settingsModel;
    private NodeNameModel nodeNameModel;
    private boolean isSearch;

    private NodeTypesModel nodeTypesModel = new NodeTypesModel(nodeShapeTypes);

    private NodeSpecSettings specSettings;
    private NodeNameSettings nodeNameSettings;

    private History history;

    public NodeSettings(Frame parent){
        this(parent, false);
    }
    public NodeSettings(Frame parent, boolean isSearch) {
        this(parent, new NodeModel(), isSearch);
    }
    public NodeSettings(Frame parent, NodeModel settings, boolean isSearch) {
        super(parent, true);
        this.isSearch = isSearch;
        initComponents();
        settingsModel = settings;
        nodeNameModel = settingsModel.getNodeNameModel();
        nodeNameSettings = new NodeNameSettings(parent, isSearch);
        setDefaultValues();
        if(isSearch){
            setTitle("Node visual search");
        }
    }

    public NodeModel getModel(){
        return settingsModel;
    }

    public void setModel(NodeModel settings) {
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
                GuiUtils.showSetNoSel(getParent(), "nodes");
                return;
            }
            setDefaultValues();
        }
        super.setVisible(visible);
    }

    private void setType(NodeShapeTypes.Type type){
        SettingUtils.showDisableIfNull(shapeDisable, type, isSearch);
        if(type != null){
            typeComboBox.setSelectedIndex(nodeTypesModel.getTypeIndex(type));
        }
    }

    private NodeShapeTypes.Type getType(){
        return shapeDisable.isSelected() ? null : nodeTypesModel.getType(typeComboBox.getSelectedIndex());
    }

    private void setNodeVisible(Boolean visible){
        SettingUtils.showDisableIfNull(visibleDisable, visible, isSearch);
        if(visible != null){
            visibleCheckBox.setSelected(visible);
        }
    }
    private Boolean getNodeVisible(){
        return visibleDisable.isSelected() ? null : visibleCheckBox.isSelected();
    }

    private void setNameVisible(Boolean visible){
        SettingUtils.showDisableIfNull(nameVisibleDisable, visible, isSearch);
        if(visible != null){
            nameVisibleCheckbox.setSelected(visible);
        }
    }
    private Boolean getNameVisible(){
        return nameVisibleDisable.isSelected() ? null : nameVisibleCheckbox.isSelected();
    }
       

    private void setDefaultValues(){
        NodeSettingPattern settingPattern = settingsModel.getSettingPattern();

        setType(settingPattern.getNodeType());
        setNodeVisible(settingPattern.isVisible());
        setNameVisible(settingPattern.isNameVisible());

        shapeSetting.setShapeWidth(settingPattern.getWidth());
        shapeSetting.setShapeHeigh(settingPattern.getHeight());
        shapeSetting.setShapeAngle(settingPattern.getAngle());
        shapeSetting.setBackgroundColor(settingPattern.getBackgroundColor());

        borderSetting.setLineWidth(settingPattern.getBorderWidth());
        borderSetting.setLineDashing(settingPattern.getBorderDashing());
        borderSetting.setLineColor(settingPattern.getBorderColor());

        setSpecSettings(settingPattern.getNodeType());

        nodeNameModel = settingsModel.getNodeNameModel();
        nodeNameSettings.setModel(nodeNameModel);
    }

    private void setSpecSettings(NodeShapeTypes.Type t){
        if(t != null){
            specSettings = nodeShapeTypes.getSettingGuiInstance(
                t,
                this,
                settingsModel,
                isSearch
            );
        }
        if(specSettings != null){
            shapeSpecialButton.setEnabled(true);
        } else {
            shapeSpecialButton.setEnabled(false);
        }
    }

    
    private void saveSettings(){
        NodeSettingPattern newPattern = new NodeSettingPattern(
            getType(),

            shapeSetting.getShapeWidth(),
            shapeSetting.getShapeHeigh(),
            shapeSetting.getShapeAngle(),
            shapeSetting.getBackgroundColor(),

            borderSetting.getLineColor(),
            borderSetting.getLineWidth(),
            borderSetting.getLineDashing(),

            null,
            null,
            null,
            null,
            null,
            null,

            getNodeVisible(),
            getNameVisible(),

            (specSettings != null) ? specSettings.getSettings() : null,
            nodeNameModel.getSettingPattern()
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        shapeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        shapeSpecialButton = new javax.swing.JButton();
        shapeDisable = new javax.swing.JCheckBox();
        visibleCheckBox = new javax.swing.JCheckBox();
        visibleDisable = new javax.swing.JCheckBox();
        nameVisibleCheckbox = new javax.swing.JCheckBox();
        nameVisibleDisable = new javax.swing.JCheckBox();
        nameSettingButton = new javax.swing.JButton();
        shapePanel = new javax.swing.JPanel();
        shapeSetting = new kandrm.JLatVis.gui.settings.visual.ShapeSetting(isSearch);
        borderPanel = new javax.swing.JPanel();
        borderSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        jPanel5 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Node visual settings");
        setResizable(false);

        generalPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        shapeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        shapeLabel.setText("Shape");
        shapeLabel.setPreferredSize(new java.awt.Dimension(40, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel1.add(shapeLabel, gridBagConstraints);

        typeComboBox.setModel(nodeTypesModel);
        typeComboBox.setPreferredSize(new java.awt.Dimension(120, 20));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, shapeDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), typeComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(typeComboBox, new java.awt.GridBagConstraints());

        shapeSpecialButton.setText("Shape settings");
        shapeSpecialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shapeSpecialButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(shapeSpecialButton, gridBagConstraints);

        shapeDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(shapeDisable, gridBagConstraints);

        visibleCheckBox.setText("Visible");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, visibleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), visibleCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(visibleCheckBox, gridBagConstraints);

        visibleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(visibleDisable, gridBagConstraints);

        nameVisibleCheckbox.setText("Show name");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, nameVisibleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), nameVisibleCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(nameVisibleCheckbox, gridBagConstraints);

        nameVisibleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(nameVisibleDisable, gridBagConstraints);

        nameSettingButton.setText("Node name settings");
        nameSettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameSettingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(nameSettingButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameSettingButton)
                .addContainerGap(34, Short.MAX_VALUE))
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
                .addContainerGap(68, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cancelButton)
                .addComponent(saveButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );

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

    private void shapeSpecialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shapeSpecialButtonActionPerformed
        if(specSettings != null){
            specSettings.setVisible(true);
        } else {
            shapeSpecialButton.setEnabled(false);
        }
    }//GEN-LAST:event_shapeSpecialButtonActionPerformed

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        NodeShapeTypes.Type type = nodeTypesModel.getType(typeComboBox.getSelectedIndex());
        if( specSettings!=null && type==specSettings.getType()){
            return;
        }
        setSpecSettings(type);
    }//GEN-LAST:event_typeComboBoxActionPerformed

    private void nameSettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameSettingButtonActionPerformed
        nodeNameSettings.setVisible(true);
    }//GEN-LAST:event_nameSettingButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borderPanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting borderSetting;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton nameSettingButton;
    private javax.swing.JCheckBox nameVisibleCheckbox;
    private javax.swing.JCheckBox nameVisibleDisable;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox shapeDisable;
    private javax.swing.JLabel shapeLabel;
    private javax.swing.JPanel shapePanel;
    private kandrm.JLatVis.gui.settings.visual.ShapeSetting shapeSetting;
    private javax.swing.JButton shapeSpecialButton;
    private javax.swing.JComboBox typeComboBox;
    private javax.swing.JCheckBox visibleCheckBox;
    private javax.swing.JCheckBox visibleDisable;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
