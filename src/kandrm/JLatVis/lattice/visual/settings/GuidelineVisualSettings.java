package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import kandrm.JLatVis.export.XmlInvalidException;

/**
 *
 * @author Michal Kandr
 */
public class GuidelineVisualSettings extends LineVisualSettings {
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204);
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1);
    private static final boolean DEFAULT_SHOWN_GRID = false;
    private static final boolean DEFAULT_SHOWN_EDGE = false;
    private static final boolean DEFAULT_SHOWN_PARALLEL = false;
    private static final int DEFAULT_SPACING_HORIZ = 30;
    private static final int DEFAULT_SPACING_VERT = 30;
    private static final int DEFAULT_ANGLE = 0;
    private static final int DEFAULT_STICKING_DISTANCE = 3;
    private static final boolean DEFAULT_GRID = true;
    private static final boolean DEFAULT_ONLY_INTERSEC = false;

    private boolean showGrid;
    private boolean showEdge;
    private boolean showParallel;

    private int spacingHoriz;
    private int spacingVert;
    private int angle;
    private int stickingDist;

    private boolean grid;
    private boolean onlyIntersec;

    public GuidelineVisualSettings(){
        super(DEFAULT_COLOR, DEFAULT_STROKE);
        setDefault();
    }
    public GuidelineVisualSettings(Color color, BasicStroke stroke,
            boolean showGrid, boolean showEdge, boolean showParallel,
            int spacingHoriz, int spacingVert, int angle, int stickingDist, boolean grid, boolean onlyIntersec){
        super(color, stroke);
        this.showGrid = showGrid;
        this.showEdge = showEdge;
        this.showParallel = showParallel;
        this.spacingHoriz = spacingHoriz;
        this.spacingVert = spacingVert;
        this.angle = angle;
        this.stickingDist = stickingDist;
        this.grid = grid;
        this.onlyIntersec = onlyIntersec;
    }
    public GuidelineVisualSettings(GuidelineVisualSettings g){
        super(g);
        this.showGrid = g.showGrid;
        this.showEdge = g.showEdge;
        this.showParallel = g.showParallel;
        this.spacingHoriz = g.spacingHoriz;
        this.spacingVert = g.spacingVert;
        this.angle = g.angle;
        this.stickingDist = g.stickingDist;
        this.grid = g.grid;
        this.onlyIntersec = g.onlyIntersec;
    }

    public GuidelineVisualSettings(LineVisualSettings n){
        super(n);
        setDefault();
    }

    private void setDefault(){
        showGrid = DEFAULT_SHOWN_GRID;
        showEdge = DEFAULT_SHOWN_EDGE;
        showParallel = DEFAULT_SHOWN_PARALLEL;
        spacingHoriz = DEFAULT_SPACING_HORIZ;
        spacingVert = DEFAULT_SPACING_VERT;
        angle = DEFAULT_ANGLE;
        stickingDist = DEFAULT_STICKING_DISTANCE;
        grid = DEFAULT_GRID;
        onlyIntersec = DEFAULT_ONLY_INTERSEC;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        GuidelineVisualSettings g = (GuidelineVisualSettings)o;
        return super.equals(g)
            && showGrid == g.showGrid
            && showEdge == g.showEdge
            && showParallel == g.showParallel
            && spacingHoriz == g.spacingHoriz
            && spacingVert == g.spacingVert
            && angle == g.angle
            && stickingDist == g.stickingDist
            && grid == g.grid
            && onlyIntersec == g.onlyIntersec;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + super.hashCode();
        hash = 13 * hash + (this.showGrid ? 1 : 0);
        hash = 13 * hash + (this.showEdge ? 1 : 0);
        hash = 13 * hash + (this.showParallel ? 1 : 0);
        hash = 13 * hash + this.spacingHoriz;
        hash = 13 * hash + this.angle;
        hash = 13 * hash + this.stickingDist;
        hash = 13 * hash + (this.grid ? 1 : 0);
        hash = 13 * hash + (this.onlyIntersec ? 1 : 0);
        return hash;
    }

    public int getAngle() {
        return angle;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }

    public boolean isShowGrid() {
        return showGrid;
    }
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowEdge() {
        return showEdge;
    }
    public void setShowEdge(boolean showEdge) {
        this.showEdge = showEdge;
    }

    public boolean isShowParallel() {
        return showParallel;
    }
    public void setShowParallel(boolean showParallel) {
        this.showParallel = showParallel;
    }


    public boolean isGrid() {
        return grid;
    }
    public void setGrid(boolean grid) {
        this.grid = grid;
    }

    public int getSpacingHoriz() {
        return spacingHoriz;
    }
    public void setSpacingHoriz(int spacingHoriz) {
        this.spacingHoriz = spacingHoriz;
    }

    public int getSpacingVert() {
        return spacingVert;
    }
    public void setSpacingVert(int spacingVert) {
        this.spacingVert = spacingVert;
    }

    public int getStickingDist() {
        return stickingDist;
    }
    public void setStickingDist(int stickingDist) {
        this.stickingDist = stickingDist;
    }

    public boolean isOnlyIntersec(){
        return onlyIntersec;
    }
    public void setOnlyIntersec(boolean onlyIntersec){
        this.onlyIntersec = onlyIntersec;
    }

    
    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Style");
            super.toXml(writer);
            writer.writeEntityWithText("ShowGrid", showGrid)
            .writeEntityWithText("ShowEdge", showEdge)
            .writeEntityWithText("ShowParallel", showParallel)
            .writeEntityWithText("SpacingHoriz", spacingHoriz)
            .writeEntityWithText("SpacingVert", spacingVert)
            .writeEntityWithText("Angle", angle)
            .writeEntityWithText("StickingDist", stickingDist)
            .writeEntityWithText("Grid", grid)
            .writeEntityWithText("OnlyIntersec", onlyIntersec);
        return writer.endEntity();
    }

    public static GuidelineVisualSettings fromXml(Element element) throws XmlInvalidException {
        GuidelineVisualSettings settings = new GuidelineVisualSettings( LineVisualSettings.fromXml(element) );
        if(element == null){
            return settings;
        }
        if( ! element.getNodeName().equals("Style")){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("ShowGrid")){
                settings.showGrid = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("ShowEdge")){
                settings.showEdge = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("ShowParallel")){
                settings.showParallel = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("SpacingHoriz")){
                settings.spacingHoriz = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("SpacingVert")){
                settings.spacingVert = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Angle")){
                settings.angle = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("StickingDist")){
                settings.stickingDist = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Grid")){
                settings.grid = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("OnlyIntersec")){
                settings.onlyIntersec = Boolean.parseBoolean(el.getTextContent());
            }
        }
        return settings;
    }
}
