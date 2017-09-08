/*
 * LoadSelection.java
 *
 * Created on 19.6.2009, 15:09:15
 */

package kandrm.JLatVis.gui.selection;

import kandrm.JLatVis.guiConnect.selection.SavedSelectionsModel;
import kandrm.JLatVis.guiConnect.selection.SaveSelectionListener;
import kandrm.JLatVis.lattice.editing.selection.SavedSelections;


/**
 *
 * @author Michal Kandr
 */
public class LoadSelection extends javax.swing.JDialog {
    private SavedSelectionsModel savedSelectionsModel;
    private SaveSelectionListener selectionListener;

    public LoadSelection(java.awt.Frame parent, SavedSelections savedSelections) {
        super(parent, false);
        setSavedSelections(savedSelections);
        initComponents();
        selectionsList.addListSelectionListener(selectionListener);
    }

    public void setSavedSelections(SavedSelections savedSelections){
        if(savedSelections != null){
            savedSelectionsModel = new SavedSelectionsModel(savedSelections);
            selectionListener = new SaveSelectionListener(savedSelections);
        } else {
            savedSelectionsModel = null;
            selectionListener = null;
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if(b){
            selectionsList.clearSelection();
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        selectionsList = new javax.swing.JList();
        okButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selection load");
        setResizable(false);

        jLabel1.setText("Saved selections:");

        selectionsList.setModel(savedSelectionsModel);
        selectionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(selectionsList);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(okButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteButton))
                            .addComponent(jLabel1))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(okButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(savedSelectionsModel.getSize() > 0){
            savedSelectionsModel.deleteSelectionAt(selectionsList.getSelectedIndex());
            selectionsList.repaint();
        }
}//GEN-LAST:event_deleteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JList selectionsList;
    // End of variables declaration//GEN-END:variables

}