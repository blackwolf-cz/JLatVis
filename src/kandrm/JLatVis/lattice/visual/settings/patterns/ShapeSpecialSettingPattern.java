package kandrm.JLatVis.lattice.visual.settings.patterns;

import kandrm.JLatVis.lattice.visual.settings.ShapeSpecialVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public abstract class ShapeSpecialSettingPattern {
    public abstract boolean match(ShapeSpecialVisualSettings settings);
    public abstract void updateByPattern(ShapeSpecialVisualSettings settings);
    public abstract void intersect(ShapeSpecialVisualSettings settings);
}
