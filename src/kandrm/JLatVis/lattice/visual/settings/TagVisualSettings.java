package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.guiConnect.settings.visual.LineTypesModel;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class TagVisualSettings extends SelectabeleTextShapeVisualSettings implements IXmlSerializable {
    /**
     * Možná místa popisku, do kterých vede hrana z uzlu.
     */
    public enum LineDests {
        /**
         * Spojnice vede do nejbližšího z rohů popisku.
         */
        CORNER,
        /**
         * Spojnice vede do středu nejbližší hrany popisku.
         */
        CENTER
    }
    private static final Dimension DEFAULT_DIMENSIONS = /*new Dimension(100, 50) */ null;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final boolean DEFAULT_VISIBLE = false;
    private static final Color DEFAULT_CONNECT_LINE_COLOR = Color.BLACK;
    private static final BasicStroke DEFAULT_CONNECT_LINE_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, LineTypesModel.DASHING_DASHED.getDashing(), 0.0f);
    private static final int DEFAULT_DISTANCE_FROM_NODE = 30;
    private static final int DEFAULT_ANGLE_FROM_HORIZ = 280;
    private static final LineDests DEFAULT_LINE_TO = LineDests.CENTER;
    private static final int DEFAULT_LINE_START_DIST = 0;

    private LineVisualSettings connectLine;
    private boolean visible;
    private float distanceFromNode;
    private float angleFromHoriz;
    //spojnice s vrcholem
    private LineDests lineTo;
    /**
     * Vzrálenost začátku spojovací čáry od vrcholu
     */
    private int lineStartDist;
    
    public TagVisualSettings(){
        super(DEFAULT_DIMENSIONS, DEFAULT_BACKGROUND_COLOR);
        setDefault();
    }

    public TagVisualSettings(Dimension dimensions,
            Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont, TextVisualSettings.TextAlignment textAlignment,
            Color connectLineColor, BasicStroke connectLineStroke,
            boolean visible,
            float distanceFromNode, float angleFromHoriz, LineDests lineTo, int lineStartDist){
        super(dimensions, 0, backgroundColor, borderColor, borderStroke, textColor, textFont, textAlignment);
        this.connectLine = new LineVisualSettings(connectLineColor, connectLineStroke);
        this.visible = visible;
        this.distanceFromNode = distanceFromNode;
        this.angleFromHoriz = angleFromHoriz;
        this.lineTo = lineTo;
        this.lineStartDist = lineStartDist;
    }

    public TagVisualSettings(TagVisualSettings c){
        super(c);
        connectLine = new LineVisualSettings(c.connectLine);
        visible = c.visible;
        distanceFromNode = c.distanceFromNode;
        angleFromHoriz = c.angleFromHoriz;
        lineTo = c.lineTo;
        lineStartDist = c.lineStartDist;
    }
    public TagVisualSettings(SelectabeleTextShapeVisualSettings c){
        super(c);
        setDefault();
    }
    
    private void setDefault(){
        if(getDimensions() == null){
            setDimensions(DEFAULT_DIMENSIONS);
        }
        connectLine = new LineVisualSettings(DEFAULT_CONNECT_LINE_COLOR, DEFAULT_CONNECT_LINE_STROKE);
        visible = DEFAULT_VISIBLE;
        distanceFromNode = DEFAULT_DISTANCE_FROM_NODE;
        angleFromHoriz = DEFAULT_ANGLE_FROM_HORIZ;
        lineTo = DEFAULT_LINE_TO;
        lineStartDist = DEFAULT_LINE_START_DIST;        
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        TagVisualSettings c = (TagVisualSettings)o;
        return super.equals(o)
            && connectLine.equals(c.connectLine)
            && visible == c.visible
            && distanceFromNode == c.distanceFromNode
            && angleFromHoriz == c.angleFromHoriz
            && lineTo == c.lineTo
            && lineStartDist == c.lineStartDist;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + super.hashCode();
        hash = 41 * hash + (this.connectLine != null ? this.connectLine.hashCode() : 0);
        hash = 41 * hash + (this.visible ? 1 : 0);
        hash = (int) (41 * hash + this.distanceFromNode);
        hash = (int) (41 * hash + this.angleFromHoriz);
        hash = 41 * hash + (this.lineTo != null ? this.lineTo.hashCode() : 0);
        hash = 41 * hash + this.lineStartDist;
        return hash;
    }


    public Color getConnectLineColor() {
        return connectLine.getColor();
    }
    public void setConnectLineColor(Color color) {
        connectLine.setColor(color);
    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BasicStroke getConnectLineStroke() {
        return connectLine.getStroke();
    }
    public void setConnectLineStroke(BasicStroke stroke) {
        connectLine.setStroke(stroke);
    }

    public float getAngleFromHoriz() {
        return angleFromHoriz;
    }
    public void setAngleFromHoriz(float angleFromHoriz) {
        this.angleFromHoriz = angleFromHoriz;
    }

    public float getDistanceFromNode() {
        return distanceFromNode;
    }
    public void setDistanceFromNode(float distanceFromNode) {
        this.distanceFromNode = distanceFromNode;
    }

    public int getLineStartDist() {
        return lineStartDist;
    }
    public void setLineStartDist(int lineStartDist) {
        this.lineStartDist = lineStartDist;
    }
    
    public LineDests getLineTo() {
        return lineTo;
    }
    public void setLineTo(LineDests lineTo) {
        this.lineTo = lineTo;
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Style");
            super.toXml(writer);
            writer.writeEntity("Line");
                connectLine.toXml(writer)
            .endEntity()
            .writeEntityWithText("Visible", visible)
            .writeEntityWithText("DistanceFromNode", distanceFromNode)
            .writeEntityWithText("AngleFromHoriz", angleFromHoriz)
            .writeEntityWithText("LineTo", lineTo)
            .writeEntityWithText("LineStartDist", lineStartDist);
        return writer.endEntity();
    }

    public static TagVisualSettings fromXml(Element element) throws XmlInvalidException {
        TagVisualSettings settings = new TagVisualSettings( SelectabeleTextShapeVisualSettings.fromXml(element) );
        if( element == null){
            return settings;
        }
        if(! element.getNodeName().equals("Style")){
            throw new XmlInvalidException();
        }
        NodeList distance = element.getElementsByTagName("DistanceFromNode");
        if(distance.getLength() != 1){
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
            if(elName.equals("Line")){
                settings.connectLine = LineVisualSettings.fromXml(el);
            } else if(elName.equals("Visible")){
                settings.visible = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("DistanceFromNode")){
                settings.distanceFromNode = Float.parseFloat(el.getTextContent());
            } else if(elName.equals("AngleFromHoriz")){
                settings.angleFromHoriz = Float.parseFloat(el.getTextContent());
            } else if(elName.equals("LineTo")){
                try{
                    settings.lineTo = LineDests.valueOf(el.getTextContent());
                } catch(IllegalArgumentException e){
                    throw new XmlInvalidException();
                }
            } else if(elName.equals("LineStartDist")){
                settings.lineStartDist = Integer.parseInt(el.getTextContent());
            }
        }
        return settings;
    }
}
