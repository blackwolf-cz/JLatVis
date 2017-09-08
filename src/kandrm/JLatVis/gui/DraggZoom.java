package kandrm.JLatVis.gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.LineVisualSettings;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class DraggZoom extends DraggSquare {
    protected List<ChangeListener> finishedListeners = new ArrayList<ChangeListener>();

    protected Zoom zoom;

    protected double oldZoom;
    protected Point2D startPointWithoutZoom;
    protected Point2D endPointWithoutZoom;

    public DraggZoom(LatticeShape lattice, LineVisualSettings visualSettings){
        super(lattice, visualSettings);
        zoom = lattice.getZoom();
    }

    public void addFinishedListener(ChangeListener listener){
        finishedListeners.add(listener);
    }
    public void removeFinishedListener(ChangeListener listener){
        finishedListeners.remove(listener);
    }

    protected void fireFinishedEvent(){
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : finishedListeners) {
            listener.stateChanged(event);
        }
    }
    
    public void setZoom(Zoom zoom){
        this.zoom = zoom;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(startPoint != null && endPoint != null){
            startPointWithoutZoom = zoom.reverse(startPoint);
            endPointWithoutZoom = zoom.reverse(endPoint);
            oldZoom = zoom.getZoom();
            zoom.setZoom(Math.max(lattice.getWidth() / Math.abs( startPoint.getX() - endPoint.getX()), lattice.getHeight() / Math.abs( startPoint.getY() - endPoint.getY())));
            fireFinishedEvent();
        }
        super.mouseReleased(e);
        startPointWithoutZoom = endPointWithoutZoom = null;
        oldZoom = zoom.getZoom();
    }

    public Point2D getStartPoint(){
        return startPoint;
    }
    public Point2D getStartPointWithoutZoom(){
        return startPointWithoutZoom;
    }
    public Point2D getEndPoint(){
        return endPoint;
    }
    public Point2D getEndPointWithoutZoom(){
        return endPointWithoutZoom;
    }

    public Point2D getTopLeftPoint(){
        return new Point2D(Math.min(startPoint.x, endPoint.x), Math.min(startPoint.y, endPoint.y));
    }
    public Point2D getTopLeftPointWithoutZoom(){
        return new Point2D(Math.min(startPointWithoutZoom.x, endPointWithoutZoom.x), Math.min(startPointWithoutZoom.y, endPointWithoutZoom.y));
    }
    public Point2D getBottomRightPoint(){
        return new Point2D(Math.max(startPoint.x, endPoint.x), Math.max(startPoint.y, endPoint.y));
    }
    public Point2D getBottomRightPointWithoutZoom(){
        return new Point2D(Math.max(startPointWithoutZoom.x, endPointWithoutZoom.x), Math.max(startPointWithoutZoom.y, endPointWithoutZoom.y));
    }

    public double getOldZoom(){
        return oldZoom;
    }
}
