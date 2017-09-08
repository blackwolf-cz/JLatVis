package kandrm.JLatVis.lattice.editing;

import com.generationjava.io.xml.XmlWriter;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.lattice.editing.history.HistoryEventNodePosition;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.SimplePolygon2D;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class Zoom implements IXmlSerializable {
    protected static final float ZOOM_STEP = 0.1f;
    protected static final double DEFAULT_ZOOM = 1;

    protected List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    private static Zoom instance = null;
    private double zoom;

    public Zoom(double zoom){
        this.zoom = zoom;
    }

    public Zoom(){
        this(DEFAULT_ZOOM);
    }

    public void addFinishedListener(ChangeListener listener){
        changeListeners.add(listener);
    }
    public void removeFinishedListener(ChangeListener listener){
        changeListeners.remove(listener);
    }

    protected void fireChangeEvent(){
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(event);
        }
    }

    public double getZoom() {
        return zoom;
    }
    public void setZoom(double zoom) {
        if(this.zoom != zoom){
            this.zoom = zoom;
            fireChangeEvent();
        }
    }

    public void increaseByDefaultStep(){
        setZoom(zoom + zoom * ZOOM_STEP);
    }
    public void decreaseByDefaultStep(){
        setZoom(zoom - zoom * ZOOM_STEP);
    }

    public void zoomToWindow(LatticeShape latticeShape, Component parentComponent, int padding){
        Rectangle latticeBounds = latticeShape.getRealBounds();
        double rateX = (double)parentComponent.getWidth() / (double)(latticeBounds.width + latticeBounds.x + padding);
        double rateY = (double)parentComponent.getHeight() / (double)(latticeBounds.height + latticeBounds.y + padding);
        zoom = zoom * Math.min(rateX, rateY);
    }
    public void zoomToWindow(LatticeShape latticeShape, Component parentComponent){
        zoomToWindow(latticeShape, parentComponent, 0);
    }
    
    public void rezizeToWindow(LatticeShape latticeShape, Component parentComponent, int padding){
        Rectangle latticeBounds = latticeShape.getRealBounds();
        
        double minCenterX = Integer.MAX_VALUE;
        double minCenterY = Integer.MAX_VALUE;
        double maxCenterX = Integer.MIN_VALUE;
        double maxCenterY = Integer.MIN_VALUE;
        for(NodeShape node : latticeShape.getNodes()){
            if(node.getCenter().x < minCenterX){
                minCenterX = node.getCenter().x;
            }
            if(node.getCenter().y < minCenterY){
                minCenterY = node.getCenter().y;
            }
            if(node.getCenter().x > maxCenterX){
                maxCenterX = node.getCenter().x;
            }
            if(node.getCenter().y > maxCenterY){
                maxCenterY = node.getCenter().y;
            }
        }
        double nodesWidthDif = latticeBounds.width - maxCenterX + minCenterX;
        double nodesHeightDif = latticeBounds.height - maxCenterY + minCenterY;
        
        double rateX = (double)(parentComponent.getWidth() - nodesWidthDif - padding*2) / (double)(maxCenterX - minCenterX);
        double rateY = (double)(parentComponent.getHeight() - nodesHeightDif - padding*2) / (double)(maxCenterY - minCenterY);
        
        Map <NodeShape, Point2D> oldNodePositions = new HashMap<NodeShape, Point2D>();
        Map <NodeShape, Point2D> newNodePositions = new HashMap<NodeShape, Point2D>();
        AffineTransform2D transform = AffineTransform2D.createScaling(new Point2D((maxCenterX - minCenterX)/2, (maxCenterY - minCenterY)/2), rateX, rateY);
        for(NodeShape node : latticeShape.getNodes()){
            oldNodePositions.put(node, node.getCenter());
            node.setCenter( node.getCenter().transform(transform) );
            newNodePositions.put(node, node.getCenter());
        }
        latticeShape.moveTo(new Point2D(padding, padding));
        latticeShape.getHistory().eventPerformed( new HistoryEventNodePosition(latticeShape.getNodes(), oldNodePositions, newNodePositions) );
    }

    private AffineTransform2D getTransform(){
        return AffineTransform2D.createScaling(zoom, zoom);
    }

    private boolean needResize(){
        return zoom != 1;
    }

    public SimplePolygon2D resize(SimplePolygon2D shape){
        return (shape == null || ! needResize()) ? shape : shape.transform(getTransform());
    }
    public Point2D resize(Point2D p){
        return (p == null || ! needResize()) ? p : p.transform(getTransform());
    }
    public java.awt.Point resize(java.awt.Point p){
        if(p != null && needResize()){
            p.setLocation(zoom * p.getX(), zoom * p.getY());
        }
        return p;
    }
    public Box2D resize(Box2D box){
        return (box == null || ! needResize()) ? box : box.transform(getTransform());
    }
    public Line2D resize(Line2D line){
        return (line == null || ! needResize()) ? line : line.transform(getTransform());
    }
    public Ellipse2D resize(Ellipse2D ellipse){
        return (ellipse == null || ! needResize()) ? ellipse : ellipse.transform(getTransform());
    }
    public Dimension resize(Dimension dimensions){
        return (dimensions == null || ! needResize()) ? dimensions : new Dimension((int)(dimensions.width * zoom), (int)(dimensions.height * zoom));
    }
    public int resize(int i){
        return (int) (i * zoom);
    }
    public float resize(float i){
        return (float) (i * zoom);
    }
    public double resize(double i){
        return (double) (i * zoom);
    }
    public Font resize(Font font){
        if(font == null || ! needResize()){
            return font;
        } else {
            Map fontAttributes = font.getAttributes();
            fontAttributes.put(TextAttribute.SIZE, font.getSize() * zoom);
            return font.deriveFont(fontAttributes);
        }
    }
    public BasicStroke resize(BasicStroke stroke){
        if(stroke == null || ! needResize()){
            return stroke;
        } else {
            float newWidth = (float) (stroke.getLineWidth() * zoom);
            return new BasicStroke(
                newWidth,
                stroke.getEndCap(),
                stroke.getLineJoin(),
                stroke.getMiterLimit(),
                stroke.getDashArray() != null
                    ?
                        new DashingPattern(stroke.getDashArray())
                            .createNormalizedDashing(stroke.getLineWidth())
                            .getRecountedDashing(newWidth)
                    :
                        null,
                stroke.getDashPhase());
        }
    }


    public Point2D reverse(Point2D p){
        return (p == null || ! needResize()) ? p : p.transform(getTransform().invert());
    }
    public int reverse(int i){
        return ! needResize() ? i : (int) (i / zoom);
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntityWithText("Zoom", zoom);
        return writer;
    }
    
    public static Zoom fromXml(Element element){
        Zoom zoomInst = new Zoom();
        NodeList elements = element.getElementsByTagName("Zoom");
        if(elements.getLength() == 1){
            zoomInst.setZoom( Double.valueOf(elements.item(0).getTextContent()) );
        }
        return zoomInst;
    }
}
