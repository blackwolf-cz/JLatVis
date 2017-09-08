package kandrm.JLatVis.guiConnect.settings.visual;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.LatticeVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class LatticeModel {
    private LatticeShape lattice;

    private Color backgroundColor;

    public LatticeModel(LatticeShape lattice){
        this.lattice = lattice;
    }

    private void initData(){
        this.backgroundColor = lattice.getVisualSettings().getBackgroundColor();
    }

    public void apply(){
        LatticeVisualSettings setting = lattice.getVisualSettings();
        setting.setBackgroundColor(backgroundColor);
        lattice.setVisualSettings(setting);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
