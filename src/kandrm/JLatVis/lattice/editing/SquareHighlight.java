package kandrm.JLatVis.lattice.editing;

import java.awt.Graphics2D;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.lattice.visual.settings.SelectabeleTextShapeVisualSettings;
import math.geom2d.Box2D;
import math.geom2d.polygon.Rectangle2D;

/**
 *
 * @author Michal Kandr
 */
public class SquareHighlight {
    SelectabeleTextShapeVisualSettings visualSettings;

    public SquareHighlight(SelectabeleTextShapeVisualSettings visualSettings){
        this.visualSettings = visualSettings;
    }

    private int defaultHighlXY(int distanceFromNode){
        return (distanceFromNode < visualSettings.getMaxHighlightDistance()) ? (visualSettings.getMaxHighlightDistance() - distanceFromNode) : 0;
    }

    public void paintSelectedHighl(Graphics2D g2, Box2D box){
        int distanceFromNode = visualSettings.getHighlightDistance();
        int defaultXY = defaultHighlXY(distanceFromNode);

        g2.setPaint(visualSettings.getSelectedHighlightColor());
        g2.setStroke(visualSettings.getSelectedHighlightStroke());

        new Rectangle2D(defaultXY, defaultXY, box.getWidth() + 2 * distanceFromNode, box.getHeight() + 2 * distanceFromNode).draw(g2);
    }

    public void paintFoundHighl(Graphics2D g2, Box2D box){
        int distance = visualSettings.getFoundHighlightDistance();
        int defaultXY = defaultHighlXY(distance);

        g2.setPaint(visualSettings.getFoundHighlightColor());
        g2.setStroke(visualSettings.getFoundHighlightStroke());

        new Rectangle2D(defaultXY, defaultXY, box.getWidth() + 2 * distance, box.getHeight() + 2 * distance ).draw(g2);
    }

    public void paintHighlightedHighl(Graphics2D g2, Box2D box){
        int distance = visualSettings.getHighlightDistance();
        int defaultXY = defaultHighlXY(distance);

        g2.setPaint(visualSettings.getHighlightedHighlightColor());
        g2.setStroke(visualSettings.getHighlightedHighlightStroke());

        new Rectangle2D(defaultXY, defaultXY, box.getWidth() + 2 * distance, box.getHeight() + 2 * distance ).draw(g2);
    }
    
    public void paintHighlightedUpperHighl(Graphics2D g2, Box2D box){
        int distance = visualSettings.getUpperHighlightDistance();
        int defaultXY = defaultHighlXY(distance);

        g2.setPaint(visualSettings.getUpperHighlightColor());
        g2.setStroke(visualSettings.getUpperHighlightStroke());

        new Rectangle2D(defaultXY, defaultXY, box.getWidth() + 2 * distance, box.getHeight() + 2 * distance ).draw(g2);
    }
    
    public void paintHighlightedLowerHighl(Graphics2D g2, Box2D box){
        int distance = visualSettings.getLowerHighlightDistance();
        int defaultXY = defaultHighlXY(distance);

        g2.setPaint(visualSettings.getLowerHighlightColor());
        g2.setStroke(visualSettings.getLowerHighlightStroke());

        new Rectangle2D(defaultXY, defaultXY, box.getWidth() + 2 * distance, box.getHeight() + 2 * distance ).draw(g2);
    }
    

    public void paintSelectedHighlVector(VectorBuilder svg, Box2D box) throws VectorBuilderException {
        int distanceFromNode = visualSettings.getHighlightDistance();
        int defaultXY = defaultHighlXY(distanceFromNode);

        svg.draw(new Rectangle2D(
            box.getMinX() - defaultXY,
            box.getMinY() - defaultXY,
            box.getWidth() + 2 * distanceFromNode,
            box.getHeight() + 2 * distanceFromNode
        ), visualSettings.getSelectedHighlightStroke(), visualSettings.getSelectedHighlightColor());
    }

    public void paintFoundHighlVector(VectorBuilder svg, Box2D box) throws VectorBuilderException {
        int distanceFromNode = visualSettings.getFoundHighlightDistance();
        int defaultXY = defaultHighlXY(distanceFromNode);

        svg.draw(new Rectangle2D(
            box.getMinX() - defaultXY,
            box.getMinY() - defaultXY,
            box.getWidth() + 2 * distanceFromNode,
            box.getHeight() + 2 * distanceFromNode
        ), visualSettings.getFoundHighlightStroke(), visualSettings.getFoundHighlightColor());
    }

    public void paintHighlightedHighlVector(VectorBuilder svg, Box2D box) throws VectorBuilderException {
        int distanceFromNode = visualSettings.getHighlightDistance();
        int defaultXY = defaultHighlXY(distanceFromNode);

        svg.draw(new Rectangle2D(
            box.getMinX() - defaultXY,
            box.getMinY() - defaultXY,
            box.getWidth() + 2 * distanceFromNode,
            box.getHeight() + 2 * distanceFromNode
        ), visualSettings.getHighlightedHighlightStroke(), visualSettings.getHighlightedHighlightColor());
    }
}
