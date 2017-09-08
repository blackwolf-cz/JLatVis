package kandrm.JLatVis.lattice.visual;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.editing.SquareHighlight;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.logical.*;
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextMeasurer;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import kandrm.JLatVis.export.IVectorPaintable;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.IHidable;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventTagVisual;
import kandrm.JLatVis.lattice.editing.history.HistoryEventVisibility;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings;
import kandrm.JLatVis.lattice.editing.search.IFoundabe;
import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import org.w3c.dom.Element;

/**
 * Vizuální reprezentace popisku. Obsahuje informace o vizuální podobě popisku a umí se nakreslit.
 *
 * @author Michal Kandr
 */
public class TagShape extends JComponent implements ISelectable, IFoundabe, IHidable, IXmlSerializable, IVectorPaintable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private Zoom zoom;

    /**
     * Logická reprezentace popisku.
     */
    private Tag logicalTag;
    /**
     * Nastavení vizuálních vlastností.
     */
    private TagVisualSettings settings;
    /**
     * Zda je uzel vybraný (označený) uživatelem.
     */
    private boolean selected = false;
    /**
     * Zda je uzel nalezený při vyhledávání.
     */
    private boolean found = false;

    /**
     * Zvýraznění označeného a nalezeného uzlu.
     */
    private SquareHighlight highlight;

    /**
     * Nová viuální reprezentace popisku s defaultním vzhledem.
     *
     * @param node
     * @param logicalTag
     */
    public TagShape(Tag logicalTag){
        this(logicalTag, new TagVisualSettings());
    }
    private TagShape(Tag logicalTag, TagVisualSettings settings){
        this.logicalTag = logicalTag;
        this.settings = settings;
        this.highlight = new SquareHighlight(settings);

        setOpaque(false);
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
        logicalTag.addHistoryListener(l);
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
        logicalTag.removeHistoryListener(l);
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
     * Logická reprezentace popisku.
     * @return
     */
    public Tag getLogicalTag() {
        return logicalTag;
    }

    /**
     * @return Nastavení vizuálních vlastností.
     */
    public TagVisualSettings getVisualSettings() {
        return new TagVisualSettings(settings);
    }
    /**
     * Změní nastavení vizuálních vlastností.
     *
     * @param settings nové nastavení
     * @param saveHistory uložit operaci do historie
     */
    public void setVisualSettings(TagVisualSettings settings, boolean saveHistory) {
        if( ! this.settings.equals(settings)){
            if(saveHistory){
                fireHistoryEvent( new HistoryEventTagVisual( this, getVisualSettings(), new TagVisualSettings(settings)) );
            }
            this.settings = settings;
            if(getParent() != null){
                getParent().repaint();
            } else {
                repaint(0, 0, getWidth(), getHeight());
            }
        }
    }
    /**
     * Změní nastavení vizuálních vlastností. Změna bude uložena do historie.
     *
     * @param settings nové nastavení
     */
    public void setVisualSettings(TagVisualSettings settings){
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

    public NodeShape getNode(){
        return logicalTag.getNode().getShape();
    }

    public Dimension getTextDimensions(){
        Dimension dimension = settings.getDimensions();
        if(dimension == null || dimension.width == 0 || dimension.height == 0){
            Font font = settings.getTextFont();
            Graphics2D g2 = (Graphics2D) getGraphics();
            if(g2 != null){
                g2.setFont(font);
                FontMetrics metrics = g2.getFontMetrics(font);
                dimension = new Dimension(
                    (int)(metrics.stringWidth( logicalTag.getText() ) + 2 * settings.getBorderStroke().getLineWidth()),
                    (int)(metrics.getHeight() + 2 * settings.getBorderStroke().getLineWidth()));
            }
        }
        return dimension;
    }

    /**
     * Vypočítá souřadnice bodu, do kterého vede spojnice popisku s vrcholem.
     *
     * @return bodu do kterého vede spojnice popisku s vrcholem
     */
    private Point2D countLineEndPoint(){
        Point2D nodeCenter = getNode().getCenter();
        double x = 0.0;
        double y = 0.0;
        float angleFromHoriz = settings.getAngleFromHoriz();
        float distanceFromNode = settings.getDistanceFromNode();

        // uhel mezi vodorovnou osou a spojnici vrcholu s popiskem
        float alpha = angleFromHoriz;
        if(angleFromHoriz>90 && angleFromHoriz<180){
            alpha = 180 - alpha;
        } else if(angleFromHoriz>180 && angleFromHoriz<270){
            alpha -= 180;
        } else if(angleFromHoriz>270 && angleFromHoriz<360){
            alpha = 360 - alpha;
        }

        // posun souradnic popisku oproti souradnicim vrcholu
        if(angleFromHoriz == 0){
            x = nodeCenter.x + distanceFromNode;
            y = nodeCenter.y;
        } else if(angleFromHoriz == 90){
            x = nodeCenter.x;
            y = nodeCenter.y - distanceFromNode;
        } else if(angleFromHoriz == 180){
            x = nodeCenter.x - distanceFromNode;
            y = nodeCenter.y;
        } else if(angleFromHoriz == 270){
            x = nodeCenter.x;
            y = nodeCenter.y + distanceFromNode;
        } else {
            double xShift = distanceFromNode * Math.cos(Math.toRadians(alpha));
            double yShift = distanceFromNode * Math.sin(Math.toRadians(alpha));
            if(angleFromHoriz>=0 && angleFromHoriz<=90){
                x = nodeCenter.x + xShift;
                y = nodeCenter.y - yShift;
            } else if(angleFromHoriz>90 && angleFromHoriz<180){
                x = nodeCenter.x - xShift;
                y = nodeCenter.y - yShift;
            } else if(angleFromHoriz>180 && angleFromHoriz<270){
                x = nodeCenter.x - xShift;
                y = nodeCenter.y + yShift;
            } else if(angleFromHoriz>270 && angleFromHoriz<360){
                x = nodeCenter.x + xShift;
                y = nodeCenter.y + yShift;
            }
        }

        return new Point2D(x, y);
    }

    /**
     * Vypočítá souřadnice levého horního rohu popisku.
     *
     * @param lineEndPoint bod do kterého vede spojnice popisku s vrcholem
     *
     * @return souřadnice levého horního rohu popisku
     */
    private Point2D countTopLeftCorner(Point2D lineEndPoint){
        double x = 0.0;
        double y = 0.0;
        float angleFromHoriz = settings.getAngleFromHoriz();
        Dimension dimensions = getTextDimensions();

        if(settings.getLineTo() == TagVisualSettings.LineDests.CORNER){
            if(angleFromHoriz>=0 && angleFromHoriz<=90){
                x = lineEndPoint.x;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>90 && angleFromHoriz<=180){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>180 && angleFromHoriz<270){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y;
            } else if(angleFromHoriz>=270 && angleFromHoriz<360){
                x = lineEndPoint.x;
                y = lineEndPoint.y;
            }
        } else {
            if(angleFromHoriz == 0){
                x = lineEndPoint.x;
                y = lineEndPoint.y - dimensions.height / 2;
            } else if(angleFromHoriz==180){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y - dimensions.height / 2;
            } else if(angleFromHoriz>0 && angleFromHoriz<=90){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>90 && angleFromHoriz<180){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>180 && angleFromHoriz<=270){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y;
            } else if(angleFromHoriz>270 && angleFromHoriz<360){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y;
            }
        }

        return new Point2D(x, y);
    }

    /**
     * Vypočítá souřadnice bodu u vrcholu, kde začíná spojnice vrcholu s popiskem.
     *
     * @param lineEndPoint bod popisku do kterého vede spojnice popisku s vrcholem
     *
     * @return bod u vrcholu, kde začíná spojnice vrcholu s popiskem
     */
    private Point2D countLineStartpoint(Point2D lineEndPoint){
        return getNode().findEdgeIntersection(lineEndPoint, settings.getLineStartDist());
    }

    @Override
    public void paintComponent(Graphics g) {
        if( ( ! settings.isVisible() || ! getNode().isVisible()) && ! found){
            return;
        }
        
        Graphics2D g2 = (Graphics2D)g;

        if( ( ! settings.isVisible() || ! getNode().isVisible()) && found){
            VisualUtils.setFoundHiddenTransparent(g2);
        }

        Point2D position = new Point2D(getX(), getY());
        Point2D lineEndPoint = countLineEndPoint();
        // position je jiz posunuta dle zoomu, takze pro vypocet se musi vratit na puvodni misto
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint).minus( zoom.reverse(position) );
        Dimension dimensions = getTextDimensions();
        Box2D tagShape = new Box2D(topLeftCorner, dimensions.width, dimensions.height);
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);

        lineEndPoint = zoom.resize(lineEndPoint);
        topLeftCorner = zoom.resize(topLeftCorner);
        dimensions = zoom.resize(dimensions);
        tagShape = zoom.resize(tagShape);
        lineStartPoint = zoom.resize(lineStartPoint);

        //pozadi
        g2.setPaint(settings.getBackgroundColor());
        tagShape.fill(g2);

        //border
        BasicStroke borderStroke = settings.getBorderStroke();
        g2.setPaint(settings.getBorderColor());
        borderStroke = zoom.resize(borderStroke);
        g2.setStroke(borderStroke);
        tagShape.draw(g2);
 
        //spojnice s vrcholem
        if(lineStartPoint != null && settings.getConnectLineStroke().getLineWidth() > 0){
            g2.setPaint(settings.getConnectLineColor());
            BasicStroke borders = settings.getConnectLineStroke();
            borders = zoom.resize(borders);
            g2.setStroke(borders);
            new Line2D(lineStartPoint.minus(position), lineEndPoint.minus(position)).draw(g2);
        }

        //paint caption text
        if(logicalTag.getText().length() > 0){
            Font font = zoom.resize(settings.getTextFont());
            String text = logicalTag.getText();
            g2.setFont(font);
            g2.setColor(settings.getTextColor());
            FontMetrics metrics = g2.getFontMetrics(font);
            TextMeasurer measurer = new TextMeasurer(new AttributedString(text).getIterator(), g2.getFontRenderContext());
            int yOffset = (int) (topLeftCorner.y + borderStroke.getLineWidth() + 1 + font.getSize());
            float width = (float) (dimensions.getWidth() - 1.5 * borderStroke.getLineWidth());
            int x = (int) (topLeftCorner.x + borderStroke.getLineWidth() + 1);
            int lineHeight = metrics.getHeight();
            for(int i=1, actualPosition = 0; actualPosition < text.length() && i*lineHeight < dimensions.height; ++i){
                int breakingIndex = measurer.getLineBreakIndex(actualPosition, width);
                int newLineIndex = text.indexOf("\n", actualPosition);
                boolean newLine = false;
                if(newLineIndex > 0 && newLineIndex < breakingIndex){
                    breakingIndex = newLineIndex;
                    newLine = true;
                }
                g2.drawString(text.subSequence( actualPosition, Math.min(breakingIndex, text.length()) ).toString(), x, yOffset);
                actualPosition = breakingIndex;
                if(newLine){
                    ++ actualPosition;
                }
                yOffset += lineHeight;
            }
        }

        Graphics2D newG2 = (Graphics2D) g2.create();
        newG2.translate(topLeftCorner.x - settings.getMaxHighlightDistance(), topLeftCorner.y - settings.getMaxHighlightDistance());

        Box2D box = new Box2D(topLeftCorner, dimensions.width, dimensions.height);
        if(selected){
            highlight.paintSelectedHighl(newG2, box);
        }
        if(found){
            highlight.paintFoundHighl(newG2, box);
        }
    }

    @Override
    public void paintVector(VectorBuilder vb) throws VectorBuilderException {
        if( ( ! settings.isVisible() || ! getNode().isVisible()) && ! found){
           return;
        }

        if( ( ! settings.isVisible() || ! getNode().isVisible()) && found){
            VisualUtils.setFoundHiddenTransparent(vb);
        }

        Point2D lineEndPoint = countLineEndPoint();
        // position je jiz posunuta dle zoomu, takze pro vypocet se musi vratit na puvodni misto
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Dimension dimensions = getTextDimensions();
        Box2D tagShape = new Box2D(topLeftCorner, dimensions.width, dimensions.height);
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);

        lineEndPoint = zoom.resize(lineEndPoint);
        topLeftCorner = zoom.resize(topLeftCorner);
        dimensions = zoom.resize(dimensions);
        tagShape = zoom.resize(tagShape);
        lineStartPoint = zoom.resize(lineStartPoint);

        if(settings.getBackgroundColor() != null){
            vb.fill(tagShape, settings.getBackgroundColor());
        }

        //border
        BasicStroke borderStroke = settings.getBorderStroke();
        borderStroke = zoom.resize(borderStroke);

        vb.draw(tagShape, borderStroke, settings.getBorderColor());

        //spojnice s vrcholem
        if(lineStartPoint != null && settings.getConnectLineStroke().getLineWidth() > 0){
            vb.draw(new Line2D(lineStartPoint, lineEndPoint), zoom.resize(settings.getConnectLineStroke()), settings.getConnectLineColor());
        }

        if(logicalTag.getText().length() > 0){
            Font font = zoom.resize(settings.getTextFont());
            vb.write(logicalTag.getText(),
                (int) (topLeftCorner.x + borderStroke.getLineWidth() + 2),
                (int) (topLeftCorner.y + borderStroke.getLineWidth() + 2 + font.getSize()),
                (float) (dimensions.getWidth() - 1.5 * borderStroke.getLineWidth()),
                (float) (dimensions.getHeight() - 1.5 * borderStroke.getLineWidth()),
                 font,
                 settings.getTextColor());
        }


        Box2D box = new Box2D(topLeftCorner, dimensions.width, dimensions.height);
        if(selected){
            highlight.paintSelectedHighlVector(vb, box);
        }
        if(found){
            highlight.paintFoundHighlVector(vb, box);
        }

        if( ( ! settings.isVisible() || ! getNode().isVisible()) && found){
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
        Container parent = getParent();
        parent.repaint();
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
    public Rectangle getBounds(){
        Rectangle shapeBounds = getShapeBounds();
        Box2D bounds = new Box2D(
            new Point2D(
                shapeBounds.x - settings.getMaxHighlightDistance(),
                shapeBounds.y - settings.getMaxHighlightDistance()
            ),
            shapeBounds.width  + zoom.resize(2 * settings.getMaxHighlightDistance()),
            shapeBounds.height  + zoom.resize(2 * settings.getMaxHighlightDistance())
        );
        return bounds.getAsAWTRectangle();
    }
    /**
     * @return hranice tvaru popisku bez započítání místa pro zvýraznění vybraného a nalezeného.
     */
    public Rectangle getShapeBounds(){
        Point2D lineEndPoint = countLineEndPoint();
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);
        if(lineStartPoint == null){
            lineStartPoint = lineEndPoint;
        }

        int distanceX = (int) (lineStartPoint.getX() - topLeftCorner.getX());
        int width;
        int distanceY = (int) (lineStartPoint.getY() - topLeftCorner.getY());
        int height;
        Dimension dimensions = getTextDimensions();

        if(distanceX < 0){
            width = dimensions.width - distanceX;
        } else if(distanceX > dimensions.width){
            width = distanceX;
        } else {
            width = dimensions.width;
        }

        if(distanceY < 0){
            height = dimensions.height - distanceY;
        } else if(distanceY > dimensions.height){
            height = distanceY;
        } else {
            height = dimensions.height;
        }

        Box2D bounds = new Box2D(
            new Point2D(Math.min(lineStartPoint.getX(), topLeftCorner.getX()), Math.min(lineStartPoint.getY(), topLeftCorner.getY())),
            width + 1,
            height + 2
        );
        return zoom.resize(bounds).getAsAWTRectangle();
    }
    /**
     * Hranice popisku bez započítání místa pro spojnici s vrcholem a místa pro zvýraznění vybraného a nalezeného.
     *
     * @return hranice tvaru s textem
     */
    public Rectangle getBoundsWithoutLine(){
        Point2D lineEndPoint = countLineEndPoint();
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Dimension dimensions = getTextDimensions();
        Box2D bounds = new Box2D(
            new Point2D(topLeftCorner.getX(), topLeftCorner.getY()),
            dimensions.width,
            dimensions.height
        );
        bounds = zoom.resize(bounds);

        return bounds.getAsAWTRectangle();
    }


    @Override
    public boolean contains(int x, int y){
        return isVisible() ? getBoundsWithoutLine().contains(x, y) : false;
    }

    @Override
    public int getX(){
        return (int) getBounds().getX();
    }
    @Override
    public int getY(){
        return (int) getBounds().getY();
    }
    @Override
    public int getWidth(){
        return (int) getBounds().getWidth();
    }
    @Override
    public int getHeight(){
        return (int) getBounds().getHeight();
    }


    public void moveBy(Point2D p){
        Point2D origin = getNode().getCenter();
        Point2D p1 = new Point2D(
                settings.getDistanceFromNode() * Math.cos(Math.toRadians(settings.getAngleFromHoriz())),
                settings.getDistanceFromNode() * Math.sin(Math.toRadians(settings.getAngleFromHoriz()))).plus(origin);
        Point2D p2 = new Point2D(p1.x + p.x, p1.y - p.y);
        settings.setAngleFromHoriz((float) Math.toDegrees( Angle2D.getHorizontalAngle(p2.minus(origin)) ));
        settings.setDistanceFromNode((float) Point2D.getDistance(origin, p2));
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Tag").writeAttribute("id", logicalTag.getId());
        settings.toXml(writer);
        return writer.endEntity();
    }

    public static TagShape fromXml(Element element, Tag logicalTag) throws XmlInvalidException {
        TagShape tag = new TagShape(logicalTag);
        tag.settings = TagVisualSettings.fromXml(element);
        return tag;
    }

    @Override
    public String toString(){
        return "Tag "+logicalTag.getName();
    }
}