package kandrm.JLatVis.gui.settings.visual;

import kandrm.JLatVis.guiConnect.settings.visual.NodeModel;
import java.awt.Dialog;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeSpecialSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public abstract class NodeSpecSettings extends javax.swing.JDialog {
    protected NodeModel settingsModel;
    protected boolean isSearch;

    public NodeSpecSettings(Dialog parent, NodeModel settingsModel, boolean isSearch){
        super(parent, true);
        this.settingsModel = settingsModel;
        updateModel();
        this.isSearch = isSearch;
    }
    
    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
    }

    public abstract NodeShapeTypes.Type getType();

    protected abstract void updateModel();

    protected abstract void setDefaultValues();

    protected abstract ShapeSpecialSettingPattern getSettings();
}
