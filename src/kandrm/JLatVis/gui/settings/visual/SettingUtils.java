package kandrm.JLatVis.gui.settings.visual;

/**
 *
 * @author Michal Kandr
 */
public class SettingUtils {
    public static String getDisableText(boolean isSearch){
        return "exclude";
    }

    public static void showDisableIfNull(javax.swing.JCheckBox checkbox, Object testedObject, boolean isSearch){
        if(isSearch || testedObject == null){
            checkbox.setVisible(true);
            checkbox.setText(getDisableText(isSearch));
        } else {
            checkbox.setVisible(false);
        }
        checkbox.setSelected( testedObject == null );
    }
}
