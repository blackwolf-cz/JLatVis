package kandrm.JLatVis.gui.settings.visual;

import kandrm.JLatVis.guiConnect.settings.visual.NodeRegularPolygonModel;
import kandrm.JLatVis.guiConnect.settings.visual.NodeModel;
import java.awt.Dialog;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes.Type;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeRegularPolygonSettingPattern;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeSpecialSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeRegularPolygonSettings extends NodeSpecSettings {
    NodeRegularPolygonModel model;

    /** Creates new form NodeShapeRegularPolygonSettings */
    public NodeRegularPolygonSettings(Dialog parent, NodeModel settingsModel, boolean isSearch) {
        super(parent, settingsModel, isSearch);
        initComponents();
        setDefaultValues();
    }

    @Override
    public Type getType() {
        return NodeShapeTypes.Type.REGULAR_POLYGON;
    }

    @Override
    protected void updateModel(){
        model = (NodeRegularPolygonModel) settingsModel.getSpecialModel(NodeShapeTypes.Type.REGULAR_POLYGON);
    }

    private void setN(Integer n){
        SettingUtils.showDisableIfNull(nDisable, n, isSearch);
        if(n != null){
            nSpinner.setValue(n);
        }
    }

    private Integer getN(){
        return nDisable.isSelected() ? null : Integer.parseInt(nSpinner.getValue().toString());
    }
    
    @Override
    protected void setDefaultValues(){
        Integer n = model.getSettingPattern().getN();
        setN(n);
    }

    @Override
    protected ShapeSpecialSettingPattern getSettings(){
        return model.getSettingPattern();
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

        okButton = new javax.swing.JButton();
        stornoButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nSpinner = new javax.swing.JSpinner();
        nDisable = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Počet vrcholů pravidelného n-úhelníku");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        stornoButton.setText("Storno");
        stornoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stornoButtonActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Vertex count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel1.add(jLabel1, gridBagConstraints);

        nSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(3), Integer.valueOf(3), null, Integer.valueOf(1)));
        nSpinner.setPreferredSize(new java.awt.Dimension(50, 20));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, nDisable, org.jdesktop.beansbinding.ELProperty.create("${ ! selected}"), nSpinner, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jPanel1.add(nSpinner, new java.awt.GridBagConstraints());

        nDisable.setText(SettingUtils.getDisableText(isSearch));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(nDisable, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(stornoButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(stornoButton))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        ShapeRegularPolygonSettingPattern newSetting = new ShapeRegularPolygonSettingPattern(getN());
        model.setSettingPattern(newSetting);
        dispose();
}//GEN-LAST:event_okButtonActionPerformed

    private void stornoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stornoButtonActionPerformed
        dispose();
}//GEN-LAST:event_stornoButtonActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox nDisable;
    private javax.swing.JSpinner nSpinner;
    private javax.swing.JButton okButton;
    private javax.swing.JButton stornoButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}