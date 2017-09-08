package kandrm.JLatVis.lattice.editing.guidelines;

import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import math.geom2d.Point2D;

/**
 * Interface pro vodící linky. Musí jej implementovat všechny třídy,
 * které mají být používány jako vodící linky.
 *
 * @author Michal Kandr
 */
public interface IGuideline {

    /**
     * Zjistí, zda je bod v oblasti, ve které má dojít k přitáhnutí k lince.
     *
     * @param p testovaný bod
     * @return zda má dojít k přitáhnutí
     */
    public boolean isInStickRange(Point2D p);

    /**
     * Najde bod na vodící lince, který je bejblíže zadanému bodu.
     *
     * @param p testovaný bod
     * @return nejbližší bod na lince
     */
    public Point2D findNearestPoint(Point2D p);

    /**
     *
     * @return nastavení vizuálních vlastností uzlu
     */
    public GuidelineVisualSettings getVisualSettings();

    /**
     * Nastaví vizuální vlastnosti uzlu.
     *
     * @param visualSettings
     */
    public void setVisualSettings(GuidelineVisualSettings visualSettings);
}
