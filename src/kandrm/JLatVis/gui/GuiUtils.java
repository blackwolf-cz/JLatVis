package kandrm.JLatVis.gui;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Michal Kandr
 */
public class GuiUtils {
    public static void showSetNoSel(Component parentComponent, String entityName){
        JOptionPane.showMessageDialog(parentComponent, "No selected "+entityName+".\nSelect "+entityName+" you want edit.", "No selected "+entityName+".", JOptionPane.WARNING_MESSAGE);
    }
}
