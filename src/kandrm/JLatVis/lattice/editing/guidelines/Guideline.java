package kandrm.JLatVis.lattice.editing.guidelines;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;

/**
 * Obecná vodící linka. Je z ní tvořena mřížka vodících linek a linky pro zarovnání
 * prvků do přímek a rovnoběžníků.
 *
 * @author Michal Kandr
 */
public class Guideline extends JComponent implements IGuideline {
    private GuidelineVisualSettings visualSettings;
    private LatticeShape lattice;
    private Zoom zoom;

    /**
     * Vizuální reprezentace vodící linky - čára.
     */
    private Line2D shape;

    /**
     * Vytvoří novou vodící linku s defaultním vzhledem.
     *
     * @param lattice
     * @param shape vizuální tvar linky (čára)
     */
    public Guideline(LatticeShape lattice, Line2D shape){
        this(lattice, new GuidelineVisualSettings(), shape);
    }
    /**
     * Vytvoří novou vodící linku se specifikovaným vzhledem.
     *
     * @param lattice
     * @param visualSettings nastavení vzhledu linky
     * @param shape vizuální tvar linky (čára)
     */
    public Guideline(LatticeShape lattice, GuidelineVisualSettings visualSettings, Line2D shape){
        this.lattice = lattice;
        this.visualSettings = visualSettings;
        this.shape = shape;
        setOpaque(false);
        zoom = lattice.getZoom();
    }

    @Override
    public boolean isInStickRange(Point2D p){
        return zoom.resize(shape).getDistance(p) <= zoom.resize(visualSettings.getStickingDist());
    }

    @Override
    public Point2D findNearestPoint(Point2D p){
        if(isInStickRange(p)){
            Line2D shapeResized = zoom.resize(shape);
            return shapeResized.transform(AffineTransform2D.createRotation(Math.toRadians(90))).getParallel(p).getIntersection(shapeResized);
        } else {
            return null;
        }
    }

    @Override
    public GuidelineVisualSettings getVisualSettings() {
        return visualSettings;
    }
    @Override
    public void setVisualSettings(GuidelineVisualSettings visualSettings) {
        this.visualSettings = visualSettings;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(visualSettings.getColor());
        g2.setStroke(zoom.resize(visualSettings.getStroke()));
        zoom.resize(shape).draw(g2);
    }

    @Override
    public int getX(){
        return 0;
    }
    @Override
    public int getY(){
        return 0;
    }
    @Override
    public int getWidth(){
        return lattice.getWidth();
    }
    @Override
    public int getHeight(){
        return lattice.getHeight();
    }
}
