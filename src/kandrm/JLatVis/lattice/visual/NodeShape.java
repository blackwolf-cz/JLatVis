package kandrm.JLatVis.lattice.visual;

import com.generationjava.io.xml.XmlWriter;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.editing.SquareHighlight;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.logical.*;
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import kandrm.JLatVis.export.IVectorPaintable;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.IHidable;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventVisibility;
import kandrm.JLatVis.lattice.editing.history.HistoryEventNodeVisual;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import kandrm.JLatVis.lattice.editing.search.IFoundabe;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Vizualni reprezentace uzlu svazu.
 * 
 */
public class NodeShape extends JComponent implements ISelectable, IFoundabe, IHidable, IXmlSerializable, IVectorPaintable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private Zoom zoom;
    private Node logicalNode;
    private NodeShapeTypes types = NodeShapeTypes.getInstance();
    private List<TagShape> tags = new ArrayList<TagShape>();
    private NodeNameShape nodeNameShape;

    private SquareHighlight highlight;
    
    private Point2D center;
    private INodeShape shape;
    private NodeVisualSettings settings;
    
    private boolean selected = false;
    private boolean found = false;
    private boolean highlighted = false;
    
    private boolean highlightedUpper = false;
    private boolean highlightedLower = false;

    
    public NodeShape(Node logicalNode){
        this(logicalNode, new Point2D(0, 0));
    }
    public NodeShape(Node logicalNode, Point2D center){
        this(logicalNode, center, new NodeVisualSettings());
    }
    public NodeShape(Node logicalNode, Point2D center, NodeVisualSettings settings){
        this.logicalNode = logicalNode;
        this.center = center;
        this.settings = settings;
        shape = types.getVisualInstance(settings.getNodeType(), settings, center);
        highlight = new SquareHighlight(settings);
        nodeNameShape = new NodeNameShape(this);
        nodeNameShape.setVisualSettings(settings.getNameSettings(), false);
        setOpaque(false);
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
        logicalNode.addHistoryListener(l);
        for(TagShape tag : tags){
            tag.addHistoryListener(l);
        }
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
        logicalNode.removeHistoryListener(l);
        for(TagShape tag : tags){
            tag.removeHistoryListener(l);
        }
    }
    private void fireHistoryEvent(HistoryEvent e){
        for (IHistoryEventListener l : historyListeners) {
            l.eventPerformed(e);
        }
    }

    public void setZoom(Zoom zoom){
        this.zoom = zoom;
        for(TagShape tag : tags){
            tag.setZoom(zoom);
        }
        nodeNameShape.setZoom(zoom);
        shape.setZoom(zoom);
    }

    
    @Override
    public void addMouseListener(MouseListener listener){
        super.addMouseListener(listener);
        for(TagShape tag : this.tags){
            tag.addMouseListener(listener);
        }
        nodeNameShape.addMouseListener(listener);
    }
    @Override
    public void addMouseMotionListener(MouseMotionListener listener) {
        super.addMouseMotionListener(listener);
        for(TagShape tag : this.tags){
            tag.addMouseMotionListener(listener);
        }
        nodeNameShape.addMouseMotionListener(listener);
    }

    public Node getLogicalNode(){
        return logicalNode;
    }
    
    public void addTag(TagShape tag){
        tags.add(tag);
        tag.setZoom(zoom);
        for(IHistoryEventListener l : historyListeners){
            tag.addHistoryListener(l);
        }
        if(getParent() != null){
            getParent().add(tag);
        }
        for(MouseListener listener : getMouseListeners()){
            tag.addMouseListener(listener);
        }
        for(MouseMotionListener listener : getMouseMotionListeners()){
            tag.addMouseMotionListener(listener);
        }
    }
    public List<TagShape> getTags(){
        return Collections.unmodifiableList(tags);
    }
    public void removeTag(TagShape tag){
        tags.remove(tag);
        getParent().remove(tag);
    }

    public Point2D getCenter() {
        return new Point2D(center);
    }
    public void setCenter(Point2D c) {
        if( ! c.equals(center)){
            center = c;
            shape = types.getVisualInstance(settings.getNodeType(), settings, center);
            shape.setZoom(zoom);
        }
    }
    public void setCenter(int x, int y) {
        setCenter(new Point2D(x, y));
    }

    public NodeVisualSettings getVisualSettings() {
        return new NodeVisualSettings(settings);
    }
    public void setVisualSettings(NodeVisualSettings settings, boolean saveHistory) {
        if( ! this.settings.equals(settings)){
            if(saveHistory){
                fireHistoryEvent( new HistoryEventNodeVisual(this, getVisualSettings(), new NodeVisualSettings(settings)) );
            }
            this.settings = settings;
            shape.setVisualSettings(settings);
            if(shape.getType() != settings.getNodeType()){
                shape = types.getVisualInstance(settings.getNodeType(), settings, center);
                shape.setZoom(zoom);
            }
            nodeNameShape.setVisualSettings(settings.getNameSettings());
            repaint();
        }
    }
    public void setVisualSettings(NodeVisualSettings settings) {
        setVisualSettings(settings, true);
    }
    
    @Override
    public boolean isSelected() {
        return selected;
    }
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isFound(){
        return found;
    }
    @Override
    public void setFound(boolean found){
        this.found = found;
    }


    public boolean isHighlighted() {
        return highlighted;
    }
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlightedLower() {
        return highlightedLower;
    }
    public void setHighlightedLower(boolean highlightedLower) {
        if(this.highlightedLower != highlightedLower){
            this.highlightedLower = highlightedLower;
            if(getParent() != null){
                getParent().repaint();
            }
        }
    }

    public boolean isHighlightedUpper() {
        return highlightedUpper;
    }
    public void setHighlightedUpper(boolean highlightedUpper) {
        if(this.highlightedUpper != highlightedUpper){
            this.highlightedUpper = highlightedUpper;
            if(getParent() != null){
                getParent().repaint();
            }
        }
    }
    

    public void moveTo(Point2D p){
        setCenter(p);
    }
    public void moveBy(Point2D p){
        moveTo(new Point2D(center.getX() + p.getX(), center.getY() + p.getY()));
    }


    public Point2D findEdgeIntersection(Point2D p, int distanceFromNode){
        return shape.findEdgeIntersection(p, distanceFromNode);
    }

    public int getMinimalAllowedY(){
        double minY = 0;
        for(Node parent : logicalNode.getParents()){
            double parentY = parent.getShape().getCenter().getY();
            if(parentY > minY){
                minY = parentY;
            }
        }
        return minY == 0 ? 0 : (int)Math.floor(minY + LatticeShape.MIN_DESC_PARENT_DISTANCE);
    }
    public int getMaximalAllowedY(){
        double maxY = Integer.MAX_VALUE;
        for(Node descendant : logicalNode.getDescendants()){
            double descY = descendant.getShape().getCenter().getY();
            if(descY < maxY){
                maxY = descY;
            }
        }
        return maxY == Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)Math.floor(maxY - LatticeShape.MIN_DESC_PARENT_DISTANCE);
    }

    private void paintNodeName(Graphics2D g2, Box2D box){
        if(settings.getNameSettings().isNameOutside()){
            getParent().add(nodeNameShape, 0);
            nodeNameShape.setVisible(true);
        } else {
            nodeNameShape.setVisible(false);
            Font font = settings.getNameSettings().getTextFont();
            font = zoom.resize(font);
            FontMetrics metrics = g2.getFontMetrics(font);

            Point2D zoomedCenter = zoom.resize(center);

            g2.setColor(settings.getNameSettings().getTextColor());
            g2.setFont(font);
            g2.drawString(
                logicalNode.getName(),
                (int)(zoomedCenter.x - box.getMinX() - metrics.stringWidth(logicalNode.getName())/2 + settings.getMaxHighlightDistance()),
                (int)(zoomedCenter.y - box.getMinY() + metrics.getHeight()/2 + settings.getMaxHighlightDistance() - metrics.getDescent())
            );
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if( ! settings.isVisible() && ! found && ! highlighted){
            return;
        }
        Graphics2D g2 = (Graphics2D)g;

        if( ! settings.isVisible() && ( found || highlighted )){
            VisualUtils.setFoundHiddenTransparent(g2);
        }

        //nakresleni uzlu
        g2.setPaint(settings.getBackgroundColor());
        shape.fill(g2);

        //nakresleni ohraniceni
        g2.setPaint(settings.getBorderColor());
        BasicStroke borders = settings.getBorderStroke();
        borders = zoom.resize(borders);
        g2.setStroke(borders);
        shape.draw(g2);

        Box2D box = shape.getBoundingBox();

        if(settings.isNameVisible()){
            paintNodeName(g2, box);
        } else {
            nodeNameShape.setVisible(false);
        }

        if(selected){
            highlight.paintSelectedHighl(g2, box);
        }
        if(found){
            highlight.paintFoundHighl(g2, box);
        }
        if(highlighted){
            highlight.paintHighlightedHighl(g2, box);
        }
        
        if(highlightedUpper){
            highlight.paintHighlightedUpperHighl(g2, box);
        }
        if(highlightedLower){
            highlight.paintHighlightedLowerHighl(g2, box);
        }
    }


    private void paintNodeNameVector(VectorBuilder vb) throws VectorBuilderException{
        if(settings.getNameSettings().isNameOutside()){
            nodeNameShape.paintVector(vb);
        } else {
            Font font = settings.getNameSettings().getTextFont();
            FontMetrics metrics = getGraphics().getFontMetrics(font);
            vb.write(
                logicalNode.getName(),
                zoom.resize((int)(center.x - metrics.stringWidth(logicalNode.getName())/2)),
                zoom.resize((int)(center.y + metrics.getHeight()/2  - metrics.getDescent())),
                zoom.resize(font),
                settings.getNameSettings().getTextColor());
        }
    }

    @Override
    public void paintVector(VectorBuilder vb) throws VectorBuilderException {
        if( ! settings.isVisible() && ! found && ! highlighted){
            return;
        }
        if( ! settings.isVisible() && ( found || highlighted )){
            VisualUtils.setFoundHiddenTransparent(vb);
        }

        if(settings.getBackgroundColor() != null){
            vb.setDefaultColor(settings.getBackgroundColor());
            shape.fillVector(vb);
        }
        vb.setDefaultColor(settings.getBorderColor());
        vb.setDefaultStroke(zoom.resize(settings.getBorderStroke()));
        shape.drawVector(vb);
        vb.setDefaultColor(null);
        vb.setDefaultStroke(null);

        Box2D box = shape.getBoundingBox();

        if(settings.isNameVisible()){
            paintNodeNameVector(vb);
        }
        if(selected){
            highlight.paintSelectedHighlVector(vb, box);
        }
        if(found){
            highlight.paintFoundHighlVector(vb, box);
        }
        if(highlighted){
            highlight.paintHighlightedHighlVector(vb, box);
        }

        for(TagShape tag : tags){
            tag.paintVector(vb);
        }

        if( ! settings.isVisible() && ( found || highlighted )){
            VisualUtils.resetFoundHiddenTransparent(vb);
        }
    }
    

    @Override
    public void setVisible(boolean visible, boolean saveHistory){
        if(isVisible() != visible){
            if(saveHistory){
                fireHistoryEvent(new HistoryEventVisibility(this, isVisible(), visible));
            }
            settings.setVisible(visible);
        }
    }
    @Override
    public void setVisible(boolean visible){
        setVisible(visible, true);
    }
    @Override
    public boolean isVisible(){
        return settings.isVisible() || found;
    }

    @Override
    public int getX(){
        return (int) shape.getBoundingBox().getMinX() - settings.getMaxHighlightDistance();
    }
    public int getShapeX(){
        return (int) shape.getBoundingBox().getMinX();
    }
    @Override
    public int getY(){
        return (int) shape.getBoundingBox().getMinY() - settings.getMaxHighlightDistance();
    }
    public int getShapeY(){
        return (int) shape.getBoundingBox().getMinY();
    }
    @Override
    public int getWidth(){
        return (int) Math.ceil( shape.getBoundingBox().getWidth() + 2 * settings.getMaxHighlightDistance() );
    }
    public int getShapeWidth(){
        return (int) shape.getBoundingBox().getWidth();
    }
    @Override
    public int getHeight(){
        return (int) Math.ceil( shape.getBoundingBox().getHeight() + 2 * settings.getMaxHighlightDistance() );
    }
    public int getShapeHeight(){
        return (int) shape.getBoundingBox().getHeight();
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
    public Rectangle getShapeBounds(){
        return new Rectangle(getShapeX(), getShapeY(), getShapeWidth(), getShapeHeight());
    }

    public Rectangle getRealBounds(){
        Box2D boundingBox = shape.getBoundingBox();
        Rectangle shapeBound = new Rectangle(
            (int) boundingBox.getMinX(),
            (int) boundingBox.getMinY(),
            (int) boundingBox.getHeight(),
            (int) boundingBox.getWidth()
        );
        if(settings.getNameSettings().isNameOutside()){
            Rectangle.union(shapeBound, nodeNameShape.getBounds(), shapeBound);
        }

        if(tags != null && tags.size() > 0){
            Rectangle result = shapeBound;
            for(TagShape tag : tags){
                if(tag.isVisible()){
                    Rectangle.union(shapeBound, tag.getShapeBounds(), result);
                }
            }
            return result;
        } else {
            return shapeBound;
        }
    }


    public boolean contains(Point2D p){
        return isVisible() ? shape.contains(p) : false;
    }

    @Override
    public boolean contains(int x, int y){
        return contains(new Point2D(x, y));
    }

    /**
     * Register all needed NodeShape subcomponents to container
     * @param c
     */
    public void registerTagsGraphic(Container c){
        for(Tag tag : logicalNode.getTags()){
            tag.setNode(logicalNode);
            tags.add(tag.getShape());
            c.add(tag.getShape());
        }
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Vertex").writeAttribute("elem", logicalNode.getId())
            .writeEntityWithText("X", ((Double)center.x).intValue())
            .writeEntityWithText("Y", ((Double)center.y).intValue());
            if(tags.size() > 0){
                for(TagShape tag : tags){
                    tag.toXml(writer);
                }
            }
            settings.toXml(writer);
        return writer.endEntity();
    }

    public static NodeShape fromXml(Element element, LatticeShape latticeShape) throws XmlInvalidException {
        String nodeId = element.getAttribute("elem");
        if(nodeId.equals("")){
            nodeId = element.getAttribute("id");
        }
        if( ! element.getNodeName().equals("Vertex") || nodeId.equals("") || ! latticeShape.getLogicalLattice().getNodes().containsKey(nodeId)){
            throw new XmlInvalidException();
        }

        NodeShape nodeShape = new NodeShape( latticeShape.getLogicalLattice().getNode(nodeId) );
        nodeShape.getLogicalNode().setShape(nodeShape);
        Element visualSettings = null;
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("X")){
                nodeShape.center.x = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Y")){
                nodeShape.center.y = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Tag")){
                String tagID = el.getAttribute("id");
                if(tagID.equals("")){
                    throw new XmlInvalidException();
                }
                Tag tag = nodeShape.logicalNode.getTagById(Integer.parseInt(tagID));
                if(tag == null){
                    throw new XmlInvalidException();
                }
                tag.fromXmlVisual(el);
            } else {
                visualSettings = el;
            }
        }
        nodeShape.setVisualSettings(NodeVisualSettings.fromXml(visualSettings), false);
        return nodeShape;
    }

    @Override
    public String toString(){
        return "Node: "+logicalNode.getName();
    }
}