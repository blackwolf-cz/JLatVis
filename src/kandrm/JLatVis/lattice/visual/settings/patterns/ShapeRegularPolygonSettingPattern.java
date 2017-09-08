package kandrm.JLatVis.lattice.visual.settings.patterns;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import kandrm.JLatVis.lattice.visual.settings.ShapeRegularPolygonVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.ShapeSpecialVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class ShapeRegularPolygonSettingPattern extends ShapeSpecialSettingPattern {
    private Integer n;

    public ShapeRegularPolygonSettingPattern(Integer n){
        this.n = n;
    }
    public ShapeRegularPolygonSettingPattern(ShapeSpecialVisualSettings settings) {
        this( settings == null ? new ShapeRegularPolygonVisualSettings().getN() : ((ShapeRegularPolygonVisualSettings) settings).getN());
    }

    @Override
    public boolean match(ShapeSpecialVisualSettings settings) {
        ShapeRegularPolygonVisualSettings s = (ShapeRegularPolygonVisualSettings) settings;
        return n == null || n.equals(s.getN());
    }

    @Override
    public void updateByPattern(ShapeSpecialVisualSettings settings) {
        ShapeRegularPolygonVisualSettings s = (ShapeRegularPolygonVisualSettings) settings;
        if(n != null){
            s.setN(n);
        }
    }

    @Override
    public void intersect(ShapeSpecialVisualSettings settings){
        ShapeRegularPolygonVisualSettings s = (ShapeRegularPolygonVisualSettings) settings;
        if(n == null || s == null || ! n.equals(s.getN())){
            n = null;
        }
    }

    public Integer getN() {
        return n;
    }

    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntityWithText("N", n);
    }
}
