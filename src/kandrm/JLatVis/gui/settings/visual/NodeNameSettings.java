package kandrm.JLatVis.gui.settings.visual;

import java.awt.Color;
import kandrm.JLatVis.guiConnect.settings.visual.NodeNameModel;
import java.awt.Frame;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import kandrm.JLatVis.gui.GuiUtils;
import kandrm.JLatVis.guiConnect.settings.visual.NodeNameLineToModel;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings.LineDests;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeNameSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameSettings extends javax.swing.JDialog {
    private NodeNameModel settingsModel;
    private boolean isSearch;
    private Color color;

    private NodeNameLineToModel lineToModel = new NodeNameLineToModel();

    public NodeNameSettings(Frame parent){
        this(parent, false);
    }
    public NodeNameSettings(Frame parent, boolean isSearch) {
        this(parent, new NodeNameModel(), isSearch);
    }
    public NodeNameSettings(Frame parent, NodeNameModel settings, boolean isSearch) {
        super(parent, true);
        this.isSearch = isSearch;
        initComponents();
        settingsModel = settings;
        setDefaultValues();
    }

    public NodeNameModel getModel(){
        return settingsModel;
    }
    public void setModel(NodeNameModel settings) {
        settingsModel = settings;
    }

    @Override
    public void setVisible(boolean visible){
        if(visible){
            if( ! isSearch && settingsModel.size() < 1){
                GuiUtils.showSetNoSel(getParent(), "nodes");
                return;
            }
            setDefaultValues();
            disableIfInside();
        }
        super.setVisible(visible);
    }

    private void setNameOutside(Boolean outside){
        SettingUtils.showDisableIfNull(nameOutsideDisable, outside, isSearch);
        if(outside != null){
            nameOutsideCheckbox.setSelected(outside);
        }
    }
    private Boolean getNameOutside(){
        return nameOutsideDisable.isSelected() ? null : nameOutsideCheckbox.isSelected();
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

    public Integer getShapePaddingHor(){
        return paddingHorDisable.isSelected() ? null : Integer.valueOf(paddingHorSpinner.getValue().toString());
    }
    public void setShapePaddingHor(Integer width){
        SettingUtils.showDisableIfNull(paddingHorDisable, width, isSearch);
        if(width != null){
            paddingHorSpinner.setValue(width);
        }
    }

    public Integer getShapePaddingVert(){
        return paddingHorDisable.isSelected() ? null : Integer.valueOf(paddingVertSpinner.getValue().toString());
    }
    public void setShapePaddingVert(Integer height){
        SettingUtils.showDisableIfNull(paddingVertDisable, height, isSearch);
        if(height != null){
            paddingVertSpinner.setValue(height);
        }
    }

    public Integer getShapeAngle(){
        return shapeAngleDisable.isSelected() ? null : Integer.valueOf(shapeAngleSpinner.getValue().toString());
    }
    public void setShapeAngle(Integer angle){
        SettingUtils.showDisableIfNull(shapeAngleDisable, angle, isSearch);
        if(angle != null){
            shapeAngleSpinner.setValue(angle);
        }
    }

    public Color getBackgroundColor() {
        return colorDisable.isSelected() ? null : color;
    }
    public void setBackgroundColor(Color color) {
        SettingUtils.showDisableIfNull(colorDisable, color, isSearch);
        if(color != null){
            this.color = color;
            colorButton.setBackground(color);
        }
    }

    public Integer getMinLineDist(){
        return minLineDistDisable.isSelected() ? null : Integer.valueOf(minLineDistSpinner.getValue().toString());
    }
    public void setMinLineDist(Integer minLineDist){
        SettingUtils.showDisableIfNull(minLineDistDisable, minLineDist, isSearch);
        if(minLineDist != null){
            minLineDistSpinner.setValue(minLineDist);
        }
    }


    private void setDefaultValues(){
        NodeNameSettingPattern settingPattern = settingsModel.getSettingPattern();

        setNameOutside(settingPattern.isNameOutside());
        setDistance(settingPattern.getDistanceFromNode());
        setAngle(settingPattern.getAngleFromHoriz());
        setLineTo(settingPattern.getLineTo());
        setLineStartDist(settingPattern.getLineStartDist());
        setMinLineDist(settingPattern.getMinLineDist());

        setShapePaddingHor(settingPattern.getWidth());
        setShapePaddingVert(settingPattern.getHeight());
        setShapeAngle(settingPattern.getAngle());
        setBackgroundColor(settingPattern.getBackgroundColor());

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
        NodeNameSettingPattern newPattern = new NodeNameSettingPattern(
            getShapePaddingHor(),
            getShapePaddingVert(),
            getShapeAngle(),
            getBackgroundColor(),

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

            getDistance(),
            getAngle(),
            getLineTo(),
            getLineStartDist(),
            getNameOutside(),
            getMinLineDist()
        );

        settingsModel.setSettingPattern(newPattern);
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
        jTabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        distanceLabel = new javax.swing.JLabel();
        distanceSpinner = new javax.swing.JSpinner();
        pxDistanceLabel = new javax.swing.JLabel();
        angleLabel = new javax.swing.JLabel();
        angleSpinner = new javax.swing.JSpinner();
        degAngleLabel = new javax.swing.JLabel();
        lineToLabel = new javax.swing.JLabel();
        lineToComboBox = new javax.swing.JComboBox();
        distanceDisable = new javax.swing.JCheckBox();
        angleDisable = new javax.swing.JCheckBox();
        lineToDisable = new javax.swing.JCheckBox();
        lineStartDistLabel = new javax.swing.JLabel();
        lineStartDistDisable = new javax.swing.JCheckBox();
        lineStartDistSpinner = new javax.swing.JSpinner();
        pxLineStartDistLabel = new javax.swing.JLabel();
        nameOutsideCheckbox = new javax.swing.JCheckBox();
        nameOutsideDisable = new javax.swing.JCheckBox();
        minLineDistLabel = new javax.swing.JLabel();
        minLineDistLabel1 = new javax.swing.JLabel();
        minLineDistSpinner = new javax.swing.JSpinner();
        pxMinLineDistLabel = new javax.swing.JLabel();
        minLineDistDisable = new javax.swing.JCheckBox();
        shapePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        paddingHorLabel = new javax.swing.JLabel();
        paddingHorSpinner = new javax.swing.JSpinner();
        pxLabel3 = new javax.swing.JLabel();
        paddingVertLabel = new javax.swing.JLabel();
        paddingVertSpinner = new javax.swing.JSpinner();
        pxLabel4 = new javax.swing.JLabel();
        shapeAngleLabel = new javax.swing.JLabel();
        shapeAngleSpinner = new javax.swing.JSpinner();
        degreesLabel = new javax.swing.JLabel();
        colorLabel = new javax.swing.JLabel();
        colorButton = new javax.swing.JButton();
        paddingHorDisable = new javax.swing.JCheckBox();
        paddingVertDisable = new javax.swing.JCheckBox();
        shapeAngleDisable = new javax.swing.JCheckBox();
        colorDisable = new javax.swing.JCheckBox();
        borderPanel = new javax.swing.JPanel();
        borderSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        linePanel = new javax.swing.JPanel();
        lineSetting = new kandrm.JLatVis.gui.settings.visual.LineSetting(isSearch);
        textPanel = new javax.swing.JPanel();
        textSetting = new kandrm.JLatVis.gui.settings.visual.TextSetting(isSearch);

        jLabel19.setText("jLabel19");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Node name visual settings");
        setResizable(false);

        saveButton.setText(isSearch ? "OK" : "Save");
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(distanceLabel, gridBagConstraints);

        distanceSpinner.setModel(new javax.swing.SpinnerNumberModel());
        distanceSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, distanceDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), distanceSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(distanceSpinner, gridBagConstraints);

        pxDistanceLabel.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(pxDistanceLabel, gridBagConstraints);

        angleLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        angleLabel.setText("Angle");
        angleLabel.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(angleLabel, gridBagConstraints);

        angleSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        angleSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, angleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), angleSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(angleSpinner, gridBagConstraints);

        degAngleLabel.setText("deg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(degAngleLabel, gridBagConstraints);

        lineToLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lineToLabel.setText("Line to");
        lineToLabel.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(lineToLabel, gridBagConstraints);

        lineToComboBox.setModel(lineToModel);
        lineToComboBox.setDoubleBuffered(true);
        lineToComboBox.setEditor(null);
        lineToComboBox.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, lineToDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), lineToComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(lineToComboBox, gridBagConstraints);

        distanceDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(distanceDisable, gridBagConstraints);

        angleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(angleDisable, gridBagConstraints);

        lineToDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(lineToDisable, gridBagConstraints);

        lineStartDistLabel.setText("Line from node");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(lineStartDistLabel, gridBagConstraints);

        lineStartDistDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(lineStartDistDisable, gridBagConstraints);

        lineStartDistSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        lineStartDistSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, lineStartDistDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), lineStartDistSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(lineStartDistSpinner, gridBagConstraints);

        pxLineStartDistLabel.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(pxLineStartDistLabel, gridBagConstraints);

        nameOutsideCheckbox.setText("Outside node");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, nameOutsideDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), nameOutsideCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        nameOutsideCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameOutsideCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel1.add(nameOutsideCheckbox, gridBagConstraints);

        nameOutsideDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(nameOutsideDisable, gridBagConstraints);

        minLineDistLabel.setText("Min. dist. from node");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(minLineDistLabel, gridBagConstraints);

        minLineDistLabel1.setText(" to show line");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel1.add(minLineDistLabel1, gridBagConstraints);

        minLineDistSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        minLineDistSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, minLineDistDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), minLineDistSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel1.add(minLineDistSpinner, gridBagConstraints);

        pxMinLineDistLabel.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(pxMinLineDistLabel, gridBagConstraints);

        minLineDistDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel1.add(minLineDistDisable, gridBagConstraints);

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("General", generalPanel);

        shapePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        shapePanel.setMinimumSize(new java.awt.Dimension(200, 128));
        shapePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel2.setPreferredSize(new java.awt.Dimension(300, 113));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        paddingHorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paddingHorLabel.setText("Horizontal padding");
        paddingHorLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel2.add(paddingHorLabel, gridBagConstraints);

        paddingHorSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, paddingHorDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), paddingHorSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jPanel2.add(paddingHorSpinner, new java.awt.GridBagConstraints());

        pxLabel3.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(pxLabel3, gridBagConstraints);

        paddingVertLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paddingVertLabel.setText("Vertical padding");
        paddingVertLabel.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel2.add(paddingVertLabel, gridBagConstraints);

        paddingVertSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, paddingVertDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), paddingVertSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(paddingVertSpinner, gridBagConstraints);

        pxLabel4.setText("px");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel2.add(pxLabel4, gridBagConstraints);

        shapeAngleLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        shapeAngleLabel.setText("Angle");
        shapeAngleLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel2.add(shapeAngleLabel, gridBagConstraints);

        shapeAngleSpinner.setPreferredSize(new java.awt.Dimension(60, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, shapeAngleDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), shapeAngleSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(shapeAngleSpinner, gridBagConstraints);

        degreesLabel.setText("deg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        jPanel2.add(degreesLabel, gridBagConstraints);

        colorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel.setText("Background");
        colorLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        jPanel2.add(colorLabel, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(colorButton, gridBagConstraints);

        paddingHorDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel2.add(paddingHorDisable, gridBagConstraints);

        paddingVertDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel2.add(paddingVertDisable, gridBagConstraints);

        shapeAngleDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel2.add(shapeAngleDisable, gridBagConstraints);

        colorDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel2.add(colorDisable, gridBagConstraints);

        shapePanel.add(jPanel2);

        jTabbedPane.addTab("Shape", shapePanel);

        borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        borderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        borderPanel.add(borderSetting);

        jTabbedPane.addTab("Border", borderPanel);

        linePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 20));
        linePanel.add(lineSetting);

        jTabbedPane.addTab("Line to node", linePanel);

        textPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        textPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        textPanel.add(textSetting);

        jTabbedPane.addTab("Text", textPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane.getAccessibleContext().setAccessibleName("General");

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

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        Color selectedColor = JColorChooser.showDialog(this, "Choose color", color);
        if(selectedColor != null){
            setBackgroundColor(selectedColor);
        }
}//GEN-LAST:event_colorButtonActionPerformed

    private void nameOutsideCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameOutsideCheckboxActionPerformed
        disableIfInside();
    }//GEN-LAST:event_nameOutsideCheckboxActionPerformed

    
    protected void disableIfInside(){
        if(isSearch){
            return;
        }
        boolean outside = nameOutsideCheckbox.isSelected();

        jTabbedPane.setEnabledAt(1, outside);
        jTabbedPane.setEnabledAt(2, outside);
        jTabbedPane.setEnabledAt(3, outside);

        distanceLabel.setEnabled(outside);

        JFormattedTextField tf = ((JSpinner.DefaultEditor)distanceSpinner.getEditor()).getTextField();
        tf.setEditable(outside);

        distanceDisable.setEnabled(outside);
        pxDistanceLabel.setEnabled(outside);

        angleLabel.setEnabled(outside);
        angleSpinner.setEnabled(outside);
        angleDisable.setEnabled(outside);
        degAngleLabel.setEnabled(outside);

        lineToLabel.setEnabled(outside);
        lineToComboBox.setEnabled(outside);
        lineToDisable.setEnabled(outside);

        lineStartDistLabel.setEnabled(outside);
        lineStartDistSpinner.setEnabled(outside);
        lineStartDistDisable.setEnabled(outside);        
        pxLineStartDistLabel.setEnabled(outside);
        
        minLineDistLabel.setEnabled(outside);
        minLineDistLabel1.setEnabled(outside);
        minLineDistSpinner.setEnabled(outside);
        pxMinLineDistLabel.setEnabled(outside);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox angleDisable;
    private javax.swing.JLabel angleLabel;
    private javax.swing.JSpinner angleSpinner;
    private javax.swing.JPanel borderPanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting borderSetting;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton colorButton;
    private javax.swing.JCheckBox colorDisable;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JLabel degAngleLabel;
    private javax.swing.JLabel degreesLabel;
    private javax.swing.JCheckBox distanceDisable;
    private javax.swing.JLabel distanceLabel;
    private javax.swing.JSpinner distanceSpinner;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JPanel linePanel;
    private kandrm.JLatVis.gui.settings.visual.LineSetting lineSetting;
    private javax.swing.JCheckBox lineStartDistDisable;
    private javax.swing.JLabel lineStartDistLabel;
    private javax.swing.JSpinner lineStartDistSpinner;
    private javax.swing.JComboBox lineToComboBox;
    private javax.swing.JCheckBox lineToDisable;
    private javax.swing.JLabel lineToLabel;
    private javax.swing.JCheckBox minLineDistDisable;
    private javax.swing.JLabel minLineDistLabel;
    private javax.swing.JLabel minLineDistLabel1;
    private javax.swing.JSpinner minLineDistSpinner;
    private javax.swing.JCheckBox nameOutsideCheckbox;
    private javax.swing.JCheckBox nameOutsideDisable;
    private javax.swing.JCheckBox paddingHorDisable;
    private javax.swing.JLabel paddingHorLabel;
    private javax.swing.JSpinner paddingHorSpinner;
    private javax.swing.JCheckBox paddingVertDisable;
    private javax.swing.JLabel paddingVertLabel;
    private javax.swing.JSpinner paddingVertSpinner;
    private javax.swing.JLabel pxDistanceLabel;
    private javax.swing.JLabel pxLabel3;
    private javax.swing.JLabel pxLabel4;
    private javax.swing.JLabel pxLineStartDistLabel;
    private javax.swing.JLabel pxMinLineDistLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox shapeAngleDisable;
    private javax.swing.JLabel shapeAngleLabel;
    private javax.swing.JSpinner shapeAngleSpinner;
    private javax.swing.JPanel shapePanel;
    private javax.swing.JPanel textPanel;
    private kandrm.JLatVis.gui.settings.visual.TextSetting textSetting;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}
