package kandrm.JLatVis.lattice.visual.settings;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 *
 * @author Michal Kandr
 */
public class FoundHighlightVisualSettings extends HighlightVisualSettings {
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(
        3,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
        new float[]{3, 5, 3, 5}, 1
    );
    private static final int DEFAULT_DIST = 8;

    public FoundHighlightVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE, DEFAULT_DIST);
    }
    public FoundHighlightVisualSettings(Color color, BasicStroke stroke, int distance){
        super(color, stroke, distance);
    }
    public FoundHighlightVisualSettings(HighlightVisualSettings h){
        super(h);
    }
}