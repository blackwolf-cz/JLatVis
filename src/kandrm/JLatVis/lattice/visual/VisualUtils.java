package kandrm.JLatVis.lattice.visual;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.lattice.visual.settings.LatticeVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class VisualUtils {
    public static void setFoundHiddenTransparent(Graphics2D g2){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, LatticeVisualSettings.FOUND_HIDDEN_TRANSPARENCY));
    }

    static void setFoundHiddenTransparent(VectorBuilder vb) {
        vb.setDefaultOpacity(LatticeVisualSettings.FOUND_HIDDEN_TRANSPARENCY);
    }

    static void resetFoundHiddenTransparent(VectorBuilder vb) {
        vb.setDefaultOpacity(null);
    }
}
