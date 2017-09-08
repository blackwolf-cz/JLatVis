package kandrm.JLatVis.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.LineVisualSettings;
import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;

/**
 *
 * @author Michal Kandr
 */
public class DraggSquare extends JComponent implements MouseListener, MouseMotionListener{
    protected LatticeShape lattice;
    /**
     * Vzhled vizuálního znázornění oblasti (obdélníku).
     */
    protected LineVisualSettings settings;

    /**
     * Počáteční bod, ve kterém uživatel začal vyznačovat oblast.
     */
    protected Point2D startPoint = null;
    /**
     * Koncový bod ve kterém bylo označování ukončeno.
     */
    protected Point2D endPoint = null;

    /**
     * Nový ovladač výběru tažením myši.
     *
     * @param lattice
     * @param selector
     * @param settings vizuální vzhled znázornění oblasti (obdélníku)
     */
    public DraggSquare(LatticeShape lattice, LineVisualSettings visualSettings){
        this.lattice = lattice;
        this.settings = visualSettings;
        register();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(settings.getColor());
        g2.setStroke(settings.getStroke());
        new Rectangle2D(
            0,
            0,
            Math.abs(endPoint.getX() - startPoint.getX()),
            Math.abs(endPoint.getY() - startPoint.getY())
        ).draw(g2);
    }

    public final void register(){
        lattice.addMouseListener(this);
        lattice.addMouseMotionListener(this);
        lattice.add(this, 0);
    }
    public void unRegister(){
        lattice.removeMouseListener(this);
        lattice.removeMouseMotionListener(this);
        lattice.remove(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        startPoint = null;
        endPoint = null;
        setVisible(false);
        lattice.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if(startPoint == null){
            if(e.getSource() != lattice){
                return;
            }
            setVisible(true);
            startPoint = new Point2D(e.getX(), e.getY());
            endPoint = startPoint;
        } else {
            endPoint = new Point2D(e.getX(), e.getY());
        }
        lattice.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}



    @Override
    public int getX(){
        return (int) (startPoint == null ? 0 : Math.min(startPoint.getX(), endPoint.getX()));
    }

    @Override
    public int getY(){
        return (int) (startPoint == null ? 0 : Math.min(startPoint.getY(), endPoint.getY()));
    }

    @Override
    public int getWidth(){
        if(startPoint==null || endPoint==null){
            return 0;
        }
        return (int) (Math.abs(endPoint.getX() - startPoint.getX()) + 2*settings.getStroke().getLineWidth());
    }

    @Override
    public int getHeight(){
        if(startPoint==null || endPoint==null){
            return 0;
        }
        return (int) (Math.abs(endPoint.getY() - startPoint.getY()) + 2*settings.getStroke().getLineWidth());
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
