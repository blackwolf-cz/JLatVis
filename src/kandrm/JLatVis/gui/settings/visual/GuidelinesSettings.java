package kandrm.JLatVis.gui.settings.visual;

import java.awt.Frame;
import kandrm.JLatVis.gui.GuiUtils;
import kandrm.JLatVis.guiConnect.settings.visual.GuidelineModel;
import kandrm.JLatVis.lattice.visual.settings.patterns.GuidelineSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class GuidelinesSettings extends javax.swing.JDialog {
    private static final int SHOW_AS_LINES = 0;
    private static final int SHOW_AS_GRID = 1;
    private static final int SHOW_AS_INTERSEC = 2;

    private GuidelineModel settingsModel;
    private boolean isSearch;

    public GuidelinesSettings(Frame parent){
        this(parent, false);
    }
    public GuidelinesSettings(Frame parent, boolean isSearch) {
        this(parent, new GuidelineModel(), isSearch);
    }
    public GuidelinesSettings(Frame parent, GuidelineModel settings, boolean isSearch) {
        super(parent, true);
        this.isSearch = isSearch;
        initComponents();
        settingsModel = settings;
        setDefaultValues();
    }

    public GuidelineModel getModel(){
        return settingsModel;
    }
    public void setModel(GuidelineModel settings) {
        settingsModel = settings;
    }

    @Override
    public void setVisible(boolean visible){
        if(visible){
            if( ! isSearch && settingsModel.size() < 1){
                GuiUtils.showSetNoSel(getParent(), "guidelines");
                return;
            }
            setDefaultValues();
        }
        super.setVisible(visible);
    }

    private Boolean getMainGridVisible(){
        return visibleCheckBox.isSelected();
    }
    private void setMainGridVisible(Boolean visible){
        if(visible != null){
            visibleCheckBox.setSelected(visible);
        }
    }

    private Boolean getEdgeVisible(){
        return edgeVisibleCheckBox.isSelected();
    }
    private void setEdgeVisible(Boolean visible){
        if(visible != null){
            edgeVisibleCheckBox.setSelected(visible);
        }
    }

    private Boolean getParallelVisible(){
        return parallelVisibleCheckBox.isSelected();
    }
    private void setParallelVisible(Boolean visible){
        if(visible != null){
            parallelVisibleCheckBox.setSelected(visible);
        }
    }

    private void setSpacingHoriz(Integer spacingHoriz){
        if(spacingHoriz != null){
            spacingHorizSpinner.setValue(spacingHoriz);
        }
    }
    private Integer getSpacingHoriz(){
        return Integer.parseInt(spacingHorizSpinner.getValue().toString());
    }

    private void setSpacingVert(Integer spacingVert){
        if(spacingVert != null){
            spacingVertSpinner.setValue(spacingVert);
        }
    }
    private Integer getSpacingVert(){
        return Integer.parseInt(spacingVertSpinner.getValue().toString());
    }

    private void setAngle(Integer angle){
        if(angle != null){
            angleSpinner.setValue(angle);
        }
    }
    private Integer getAngle(){
        return Integer.parseInt(angleSpinner.getValue().toString());
    }

    private void setStickingDist(Integer stickingDist){
        if(stickingDist != null){
            stickingDistSpinner.setValue(stickingDist);
        }
    }
    private Integer getStickingDist(){
        return Integer.parseInt(stickingDistSpinner.getValue().toString());
    }

    private void setShowAs(int showAs){
        if(showAs != SHOW_AS_GRID && showAs != SHOW_AS_INTERSEC){
            showAs = SHOW_AS_LINES;
        }
        showAsComboBox.setSelectedIndex( showAs );
    }

    private Boolean getGrid(){
        return showAsComboBox.getSelectedIndex() == SHOW_AS_GRID;
    }        

    private Boolean getIntersec(){
        return showAsComboBox.getSelectedIndex() == SHOW_AS_INTERSEC;
    }

    private void setDefaultValues(){
        GuidelineSettingPattern settingPattern = settingsModel.getSettingPattern();

        setMainGridVisible(settingPattern.getShowGrid());
        setEdgeVisible(settingPattern.getShowEdge());
        setParallelVisible(settingPattern.getShowParallel());
        setSpacingHoriz(settingPattern.getSpacingHoriz());
        setSpacingVert(settingPattern.getSpacingVert());
        setAngle(settingPattern.getAngle());
        setStickingDist(settingPattern.getStickingDist());

        if(settingPattern.getOnlyIntersec() && settingPattern.getGrid()){
            setShowAs(SHOW_AS_INTERSEC);
        } else if(settingPattern.getGrid()){
            setShowAs(SHOW_AS_GRID);
        } else {
            setShowAs(SHOW_AS_LINES);
        }

        lineSetting.setLineWidth(settingPattern.getWidth());
        lineSetting.setLineDashing(settingPattern.getDashing());
        lineSetting.setLineColor(settingPattern.getColor());
    }

    private void saveSettings(){
        GuidelineSettingPattern newPattern = new GuidelineSettingPattern(
            lineSetting.getLineColor(),
            lineSetting.getLineWidth(),
            lineSetting.getLineDashing(),

            getMainGridVisible(),
            getEdgeVisible(),
            getParallelVisible(),
            getSpacingHoriz(),
            getSpacingVert(),
            getAngle(),
            getStickingDist(),
            getGrid() || getIntersec(),
            getIntersec()
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        parallelVisibleCheckBox = new javax.swing.JCheckBox();
        visibleCheckBox = new javax.swing.JCheckBox();
        edgeVisibleCheckBox = new javax.swing.JCheckBox();
        gridPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        showAsLabel = new javax.swing.JLabel();
        showAsComboBox = new javax.swing.JComboBox();
        spacingHorizLabel = new javax.swing.JLabel();
        spacingHorizSpinner = new javax.swing.JSpinner();
        spacingVertLabel = new javax.swing.JLabel();
        spacingVertSpinner = new javax.swing.JSpinner();
        angleLabel = new javax.swing.JLabel();
        angleSpinner = new javax.swing.JSpinner();
        degLabel = new javax.swing.JLabel();
        stickingDistLabel = new javax.swing.JLabel();
        stickingDistSpinner = new javax.swing.JSpinner();
        pxStickingDistLabel = new javax.swing.JLabel();
        linePanel = new javax.swing.JPanel();
        lineSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        jPanel5 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Guide lines setting");
        setResizable(false);

        generalPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        generalPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        parallelVisibleCheckBox.setText("Show parallelogram lines");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(parallelVisibleCheckBox, gridBagConstraints);

        visibleCheckBox.setText("Show grid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel2.add(visibleCheckBox, gridBagConstraints);

        edgeVisibleCheckBox.setText("Show edge lines");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(edgeVisibleCheckBox, gridBagConstraints);

        generalPanel.add(jPanel2);

        jTabbedPane1.addTab("General", generalPanel);

        gridPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        showAsLabel.setText("Show as");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(showAsLabel, gridBagConstraints);

        showAsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lines", "Grid", "Grid points" }));
        showAsComboBox.setPreferredSize(new java.awt.Dimension(60, 20));
        showAsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAsComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(showAsComboBox, gridBagConstraints);

        spacingHorizLabel.setText("Horizontal spacing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(spacingHorizLabel, gridBagConstraints);

        spacingHorizSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spacingHorizSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(spacingHorizSpinner, gridBagConstraints);

        spacingVertLabel.setText("Vertical spacing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(spacingVertLabel, gridBagConstraints);

        spacingVertSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spacingVertSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(spacingVertSpinner, gridBagConstraints);

        angleLabel.setText("Angle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(angleLabel, gridBagConstraints);

        angleSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 359, 1));
        angleSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(angleSpinner, gridBagConstraints);

        degLabel.setText("deg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(degLabel, gridBagConstraints);

        stickingDistLabel.setText("Sticking distance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(stickingDistLabel, gridBagConstraints);

        stickingDistSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        stickingDistSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(stickingDistSpinner, gridBagConstraints);

        pxStickingDistLabel.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(pxStickingDistLabel, gridBagConstraints);

        gridPanel.add(jPanel1);

        jTabbedPane1.addTab("Grid", gridPanel);

        linePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        linePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linePanel.add(lineSetting);

        jTabbedPane1.addTab("Line", linePanel);

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
                .addContainerGap(65, Short.MAX_VALUE)
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(217, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveSettings();
        dispose();
}//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void showAsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAsComboBoxActionPerformed
        spacingHorizLabel.setEnabled( showAsComboBox.getSelectedIndex() != SHOW_AS_LINES );
        spacingHorizSpinner.setEnabled( showAsComboBox.getSelectedIndex() != SHOW_AS_LINES );
    }//GEN-LAST:event_showAsComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel angleLabel;
    private javax.swing.JSpinner angleSpinner;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel degLabel;
    private javax.swing.JCheckBox edgeVisibleCheckBox;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel linePanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting lineSetting;
    private javax.swing.JCheckBox parallelVisibleCheckBox;
    private javax.swing.JLabel pxStickingDistLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox showAsComboBox;
    private javax.swing.JLabel showAsLabel;
    private javax.swing.JLabel spacingHorizLabel;
    private javax.swing.JSpinner spacingHorizSpinner;
    private javax.swing.JLabel spacingVertLabel;
    private javax.swing.JSpinner spacingVertSpinner;
    private javax.swing.JLabel stickingDistLabel;
    private javax.swing.JSpinner stickingDistSpinner;
    private javax.swing.JCheckBox visibleCheckBox;
    // End of variables declaration//GEN-END:variables

}
