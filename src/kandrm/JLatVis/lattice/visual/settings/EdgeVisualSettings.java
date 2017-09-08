package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class EdgeVisualSettings extends LineVisualSettings implements IXmlSerializable {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1);
    private static final int DEFAULT_DISTANCE_FROM_NODE = 10;
    private static final boolean DEFAULT_VISIBLE = true;
    public static final int CLICK_TOLERANCE = 4;
    
    private boolean visible;
    private int distanceFromNode;
    private SelectedHighlightVisualSettings selectedHighlight;
    private FoundHighlightVisualSettings foundHighlight;

    public EdgeVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE, DEFAULT_VISIBLE, DEFAULT_DISTANCE_FROM_NODE);
    }
    public EdgeVisualSettings(Color color, BasicStroke stroke, boolean visible, int distanceFromNode){
        super(color, stroke);
        this.visible = visible;
        this.distanceFromNode = distanceFromNode;
        this.selectedHighlight = new SelectedHighlightVisualSettings();
        this.foundHighlight = new FoundHighlightVisualSettings();
    }

    public EdgeVisualSettings(EdgeVisualSettings e){
        super(e);
        visible = e.visible;
        distanceFromNode = e.distanceFromNode;
        selectedHighlight = new SelectedHighlightVisualSettings(e.selectedHighlight);
        foundHighlight = new FoundHighlightVisualSettings(e.foundHighlight);
    }
    public EdgeVisualSettings(LineVisualSettings e){
        super(e);
        visible = DEFAULT_VISIBLE;
        distanceFromNode = DEFAULT_DISTANCE_FROM_NODE;
        selectedHighlight = new SelectedHighlightVisualSettings();
        foundHighlight = new FoundHighlightVisualSettings();
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        EdgeVisualSettings e = (EdgeVisualSettings)o;
        return super.equals(e)
            && visible == e.visible
            && distanceFromNode == e.distanceFromNode
            && selectedHighlight.equals(e.selectedHighlight)
            && foundHighlight.equals(e.foundHighlight);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * super.hashCode();
        hash = 37 * hash + (this.visible ? 1 : 0);
        hash = 37 * hash + this.distanceFromNode;
        hash = 37 * hash + this.selectedHighlight.hashCode();
        hash = 37 * hash + this.foundHighlight.hashCode();
        return hash;
    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getDistanceFromNode() {
        return distanceFromNode;
    }
    public void setDistanceFromNode(int distanceFromNode) {
        this.distanceFromNode = distanceFromNode;
    }
    

    public Color getSelectedHighlightColor() {
        return selectedHighlight.getColor();
    }
    public void setSelectedHighlightColor(Color color) {
        this.selectedHighlight.setColor(color);
    }

    public BasicStroke getSelectedHighlightStroke() {
        return selectedHighlight.getStroke();
    }
    public void setSelectedHighlightStroke(BasicStroke stroke) {
        this.selectedHighlight.setStroke(stroke);
    }

    public int getHighlightDistance(){
        return selectedHighlight.getDistance();
    }
    public void setHighlightDistance(int highlightDistanceFromNode){
        this.selectedHighlight.setDistance(highlightDistanceFromNode);
    }


    public Color getFoundHighlightColor() {
        return foundHighlight.getColor();
    }
    public void setFoundHighlightColor(Color color) {
        this.foundHighlight.setColor(color);
    }

    public BasicStroke getFoundHighlightStroke() {
        return foundHighlight.getStroke();
    }
    public void setFoundHighlightStroke(BasicStroke stroke) {
        this.foundHighlight.setStroke(stroke);
    }

    public int getFoundHighlightDistance(){
        return this.foundHighlight.getDistance();
    }
    public void setFoundHighlightDistance(int foundHighlightDistanceFromNode){
        this.foundHighlight.setDistance(foundHighlightDistanceFromNode);
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Style");
            super.toXml(writer)
            .writeEntityWithText("Visible", visible)
            .writeEntityWithText("DistanceFromNode", distanceFromNode);
        return writer.endEntity();
    }

    public static EdgeVisualSettings fromXml(Element element) throws XmlInvalidException {
        if( ! element.getNodeName().equals("Style")){
            throw new XmlInvalidException();
        }
        EdgeVisualSettings settings = new EdgeVisualSettings( LineVisualSettings.fromXml(element) );

        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("DistanceFromNode")){
                settings.distanceFromNode = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Visible")){
                settings.visible = Boolean.parseBoolean(el.getTextContent());
            }
        }
        
        return settings;
    }
}
