package kandrm.JLatVis.lattice.visual;

import com.generationjava.io.xml.XmlWriter;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.editing.Zoom;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import kandrm.JLatVis.export.IVectorPaintable;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.IHidable;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventEdgeVisual;
import kandrm.JLatVis.lattice.editing.history.HistoryEventVisibility;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.visual.settings.EdgeVisualSettings;
import kandrm.JLatVis.lattice.editing.search.IFoundabe;
import kandrm.JLatVis.lattice.logical.OrderPair;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Vizuální reprezentace hrany mezi dvěma uzly.
 * Hrana nemá logickou reprezentaci - na logické úrovni je jen relace mezi prvky, žádná hrana.
 *
 * @author Michal Kandr
 */
public class EdgeShape extends JPanel implements ISelectable, IFoundabe, IHidable, IVectorPaintable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private OrderPair orderPair;

    private Zoom zoom;

    /**
     * Uzel na jednom konci hrany.
     */
    private NodeShape u1;
    /**
     * Uzel na druhem konci hrany.
     */
    private NodeShape u2;
    
    private EdgeVisualSettings settings;

    /**
     * Zda byla hrana vybrána uživatelem.
     */
    private boolean selected = false;
    /**
     * Zda byla hrana nalezena při posledním vyhledávání
     */
    private boolean found = false;

    /**
     * Nová hrana mezi uzly u1 a u2 s defaltním vizuálním nastavením.
     * @param from
     * @param to
     */
    public EdgeShape(OrderPair orderPair){
        this(orderPair, new EdgeVisualSettings());
    }
    /**
     * Nová hrana mezi uzly u1 a u2 se zadanym vizuálním nastavením.
     * @param u1
     * @param u2
     * @param settings
     */
    public EdgeShape(OrderPair orderPair, EdgeVisualSettings settings){
        this.u1 = orderPair.getGreater().getShape();
        this.u2 = orderPair.getLess().getShape();
        this.settings = settings;
        this.orderPair = orderPair;
        setOpaque(false);
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
    }
    private void fireHistoryEvent(HistoryEvent e){
        for (IHistoryEventListener l : historyListeners) {
            l.eventPerformed(e);
        }
    }

    public void setZoom(Zoom zoom){
        this.zoom = zoom;
    }

    /**
     * @return vizuální nastavení hrany
     */
    public EdgeVisualSettings getVisualSettings() {
        return new EdgeVisualSettings(settings);
    }
    /**
     * Nastaví vizuální nastavení hrany§
     * @param settings vizuální nastavení hrany
     * @param saveHistory uložit událost do historie
     */
    public void setVisualSettings(EdgeVisualSettings settings, boolean saveHistory) {
        if( ! this.settings.equals(settings)){
            if(saveHistory){
                fireHistoryEvent( new HistoryEventEdgeVisual(this, this.settings, new EdgeVisualSettings(settings)) );
            }
            this.settings = settings;
            repaint(0, 0, getWidth(), getHeight());
        }
    }
    /**
     * Nastaví vizuální nastavení hrany a uloží tuto událost do historie.
     *
     * @param settings vizuální nastavení hrany
     */
    public void setVisualSettings(EdgeVisualSettings settings) {
        setVisualSettings(settings, true);
    }

    /**
     * @return uzel na jednom konci hrany
     */
    public NodeShape getU1() {
        return u1;
    }
    /**
     * @return uzel na druhém konci hrany
     */
    public NodeShape getU2() {
        return u2;
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

    public OrderPair getOrderPair(){
        return orderPair;
    }

    /**
     * Vytvoří tvar hrany podle uzlů a vizuálního nastavení.
     *
     * @return tvar hrany
     */
    private Line2D createShape(){
        Point2D fromP = u1.findEdgeIntersection(u2.getCenter(), settings.getDistanceFromNode());
        Point2D toP = u2.findEdgeIntersection(u1.getCenter(), settings.getDistanceFromNode());
        if(fromP != null && toP != null){
            Line2D shape = new Line2D(fromP, toP);
            shape = zoom.resize(shape);
            return shape;
        } else {
            return null;
        }
    }

    private Vector2D createTransVector(Line2D shape, double distance){
        return shape.getVector().getNormalizedVector()
            .transform(AffineTransform2D.createRotation(90))
            .times(settings.getStroke().getLineWidth()/2 + distance);
    }

    private Vector2D getBoundingTransVector(Line2D shape){
        return createTransVector(shape,
                Math.max(settings.getSelectedHighlightStroke().getLineWidth(), settings.getFoundHighlightStroke().getLineWidth())
                + Math.max(settings.getHighlightDistance(), settings.getFoundHighlightDistance()));
    }

    private Box2D createBoundingShape(){
        Line2D shape = createShape();
        if(shape == null){
            return null;
        }

        Vector2D translation = getBoundingTransVector(shape);

        Line2D top = shape.transform(AffineTransform2D.createTranslation(translation));
        Line2D bottom = shape.transform(AffineTransform2D.createTranslation(translation.getOpposite()));

        return top.getBoundingBox().merge( bottom.getBoundingBox() ).getBoundingBox();
    }
    
    /**
     * Nakreslí zvýraznění uživatelem vybrané hrany.
     * 
     * @param g2
     * @param shape tvar hrany
     */
    private void paintSelectedHighlight(Graphics2D g2, Line2D shape){
        g2.setPaint(settings.getSelectedHighlightColor());
        g2.setStroke(settings.getSelectedHighlightStroke());

        Vector2D translation = createTransVector(shape, settings.getHighlightDistance());

        Line2D selFrame = shape.transform(AffineTransform2D.createTranslation(translation));
        selFrame.draw(g2);
        selFrame = shape.transform(AffineTransform2D.createTranslation(translation.getOpposite()));
        selFrame.draw(g2);
    }

    private void paintFoundHighlight(Graphics2D g2, Line2D shape){
        g2.setPaint(settings.getFoundHighlightColor());
        g2.setStroke(settings.getFoundHighlightStroke());

        Vector2D translation = createTransVector(shape, settings.getFoundHighlightDistance());

        Line2D selFrame = shape.transform(AffineTransform2D.createTranslation(translation));
        selFrame.draw(g2);
        selFrame = shape.transform(AffineTransform2D.createTranslation(translation.getOpposite()));
        selFrame.draw(g2);
    }

    @Override
    public void paintComponent(Graphics g) {
        if( ( ! u1.isVisible() || ! u2.isVisible() || ! settings.isVisible()) && ! found){
            return;
        }

        Graphics2D g2 = (Graphics2D)g;

        if( ( ! u1.isVisible() || ! u2.isVisible()) && found){
            VisualUtils.setFoundHiddenTransparent(g2);
        }
        
        Line2D shape = createShape();
        if(shape == null){
            return;
        }

        Box2D box = createBoundingShape().getBoundingBox();
        shape = shape.transform(AffineTransform2D.createTranslation( - box.getMinX(), - box.getMinY()));

        g2.setPaint(settings.getColor());
        BasicStroke borders = settings.getStroke();
        borders = zoom.resize(borders);
        g2.setStroke(borders);
        shape.draw(g2);

        if(selected){
            paintSelectedHighlight(g2, shape);
        }
        if(found){
            paintFoundHighlight(g2, shape);
        }
    }


    private void paintSelectedHighlightSvg(VectorBuilder vb, Line2D shape) throws VectorBuilderException{
        Vector2D translation = createTransVector(shape, settings.getHighlightDistance());

        Line2D selFrame = shape.transform(AffineTransform2D.createTranslation(translation));
        vb.draw(selFrame, settings.getSelectedHighlightStroke(), settings.getSelectedHighlightColor());
        selFrame = shape.transform(AffineTransform2D.createTranslation(translation.getOpposite()));
        vb.draw(selFrame, settings.getSelectedHighlightStroke(), settings.getSelectedHighlightColor());
    }

    private void paintFoundHighlightSvg(VectorBuilder vb, Line2D shape) throws VectorBuilderException{
        Vector2D translation = createTransVector(shape, settings.getFoundHighlightDistance());

        Line2D selFrame = shape.transform(AffineTransform2D.createTranslation(translation));
        vb.draw(selFrame, settings.getFoundHighlightStroke(), settings.getFoundHighlightColor());
        selFrame = shape.transform(AffineTransform2D.createTranslation(translation.getOpposite()));
        vb.draw(selFrame, settings.getFoundHighlightStroke(), settings.getFoundHighlightColor());
    }

    @Override
    public void paintVector(VectorBuilder vb) throws VectorBuilderException {
        if( ( ! u1.isVisible() || ! u2.isVisible()) && found){
            VisualUtils.setFoundHiddenTransparent(vb);
        }

        Line2D shape = createShape();
        if(shape == null){
            return;
        }

        vb.draw(shape, zoom.resize(settings.getStroke()), settings.getColor());

        if(selected){
            paintSelectedHighlightSvg(vb, shape);
        }
        if(found){
            paintFoundHighlightSvg(vb, shape);
        }

        if( ( ! u1.isVisible() || ! u2.isVisible()) && found){
            VisualUtils.resetFoundHiddenTransparent(vb);
        }
    }
    

    /**
     * Zjištění, zda zadaný bod je uvnitř hrany, nebo ve vzdálenosti nižší,
     * než je nastavená tolerance pro kliknutí.
     * Metoda je určena pro detekci kliknutí myší.
     *
     * @param p zkoumaný bod
     *
     * @return zda zadaný bod je uvnitř hrany nebo v její blízkosti
     */
	public boolean containsWithTolerance(Point2D p){
        Line2D shape = createShape();
        if(shape == null){
            return false;
        } else {
            float maxDistance = settings.getStroke().getLineWidth()/2 + EdgeVisualSettings.CLICK_TOLERANCE;
            return shape.getDistance(p) <= maxDistance;
        }
    }
    /**
     * Zjištění, zda zadaný bod je uvnitř hrany, nebo ve vzdálenosti nižší,
     * než je nastavená tolerance pro kliknutí.
     * Metoda je určena pro detekci kliknutí myší.
     *
     * @param x x souřadnice bodu
     * @param y y souřadnice bodu
     *
     * @return zda zadaný bod je uvnitř hrany nebo v její blízkosti
     */
    public boolean containsWithTolerance(int x, int y){
        return containsWithTolerance(new Point2D(x, y));
    }

    @Override
    public int getX(){
        Box2D shape = createBoundingShape();
        if(shape == null){
            return 0;
        } else {
            return (int) shape.getMinX();
        }
    }
    @Override
    public int getY(){
        Box2D shape = createBoundingShape();
        if(shape == null){
            return 0;
        } else {
            return (int) shape.getMinY();
        }
    }
    @Override
    public int getWidth(){
        Box2D shape = createBoundingShape();
        if(shape == null){
            return 0;
        } else {
            return (int) Math.ceil( shape.getWidth() );
        }
    }
    @Override
    public int getHeight(){
        Box2D shape = createBoundingShape();
        if(shape == null){
            return 0;
        } else {
            return (int) Math.ceil( shape.getHeight() );
        }
    }

    @Override
    public Rectangle getBounds(){
        return createBoundingShape().getAsAWTRectangle();
    }

    /**
     * Zjištění, zda hrana obsahuje bod.
     *
     * @param p zkoumaný bod
     *
     * @return zda hrana obsahuje bod
     */
    public boolean contains(Point2D p){
        Line2D shape = createShape();
        if(shape == null){
            return false;
        } else {
            return shape.getDistance(p) <= settings.getStroke().getLineWidth()/2;
        }
    }

    @Override
    public boolean contains(Point p){
        return contains(new Point2D(p.x, p.y));
    }
    @Override
    public boolean contains(int x, int y){
        return contains(new Point2D(x, y));
    }
    

    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Edge")
            .writeAttribute("from", u1.getLogicalNode().getId())
            .writeAttribute("to", u2.getLogicalNode().getId());
            settings.toXml(writer);
        return writer.endEntity();
    }

    public static EdgeShape fromXml(Element element, LatticeShape lattice) throws XmlInvalidException {
        NodeShape from = lattice.getNodeById(element.getAttribute("from"));
        NodeShape to = lattice.getNodeById(element.getAttribute("to"));
        if( ! element.getNodeName().equals("Edge") || from == null || to == null){
            throw new XmlInvalidException();
        }
        OrderPair pair = lattice.getLogicalLattice().getOrderPair(from.getLogicalNode().getName(), to.getLogicalNode().getName());
        if(pair == null){
            throw new XmlInvalidException();
        }
        EdgeShape edgeShape = new EdgeShape(pair);
        
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            edgeShape.settings = EdgeVisualSettings.fromXml((Element)elNode);
        }
        
        return edgeShape;
    }

    @Override
    public String toString(){
        return "Edge: "+u1.getLogicalNode().getName()+" - "+u2.getLogicalNode().getName();
    }
}
