package kandrm.JLatVis.gui.search;

/**
 * Komponenta pro zadání textového vyhledávání popisku.
 * Stejný formulář se používá jak v hledání popisku, tak v hledávní uzlu
 * (uzel lze hledat na základě textu v popisku).
 *
 * @author Michal Kandr
 */
public class TextSearchTagPanel extends javax.swing.JPanel {

    /**
     * Nový fomulář
     */
    public TextSearchTagPanel() {
        initComponents();
    }

    /**
     * @return text hledaný v názvu
     */
    public String getNameValue(){
        return nameField.getText();
    }

    /**
     * @return je text hledaný v názvu regulární výraz
     */
    public boolean isNameRegExp(){
        return nameRegExpCheckBox.isSelected();
    }

    /**
     * @return text hledaný v textu
     */
    public String getTextValue(){
        return textField.getText();
    }

    /**
     * @return je text hledaný v textu regulární výraz
     */
    public boolean isTextRegExp(){
        return textRegExpCheckBox.isSelected();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        textField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        nameRegExpCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        textRegExpCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        textField.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(textField, gridBagConstraints);

        jLabel2.setText("Find in text");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        add(jLabel2, gridBagConstraints);

        nameField.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(nameField, gridBagConstraints);

        nameRegExpCheckBox.setText("regular expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(nameRegExpCheckBox, gridBagConstraints);

        jLabel1.setText("Find in name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(jLabel1, gridBagConstraints);

        textRegExpCheckBox.setText("regular expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(textRegExpCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField nameField;
    private javax.swing.JCheckBox nameRegExpCheckBox;
    private javax.swing.JTextField textField;
    private javax.swing.JCheckBox textRegExpCheckBox;
    // End of variables declaration//GEN-END:variables

}
