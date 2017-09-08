package kandrm.JLatVis.lattice.visual;

import java.awt.Graphics2D;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import math.geom2d.Box2D;
import math.geom2d.Point2D;

/**
 * Interface pro konkrétní tvary uzlů. Samotný uzel neobsahuje funkčnost související s vykreslením,
 * tu deleguje na instance konkrétních tvarů - tříd implementujících tento interface.
 *
 * @author Michal Kandr
 */
public interface INodeShape {

    /**
     * Najde bod ležící na spojnici středu uzlu se zadaným bodem a nacházející se
     * <code>distanceFromNode</code> od průsečíku této přímky s okrajem uzlu.
     * Použití pro výpočet počátku hrany (zadaný bod je střed uzlu na druhém konci hrany).
     * 
     * @param p bod, do kterého vede přímka
     * @param distanceFromNode vzdálenost, ve které má být bod od průsečíku přímky s okrajem uzlu
     *
     * @return nalezený bod
     */
    public Point2D findEdgeIntersection(Point2D p, int distanceFromNode);

    /**
     * @return Typ (tvar) uzlu.
     */
    public NodeShapeTypes.Type getType();

    /**
     * Nastaví vizuální nastavení uzlu.
     *
     * @param visualSetings nové vizuální nastavení
     */
    public void setVisualSettings(NodeVisualSettings visualSetings);

    /**
     * Nakreslí uzel jako obrys.
     * @param g2
     */
    public void draw(Graphics2D g2);

    public void drawVector(VectorBuilder vb) throws VectorBuilderException;

    /**
     * Nakreslí uzel jako plný tvar (vyplněný obrys).
     * @param g2
     */
    public void fill(Graphics2D g2);

    public void fillVector(VectorBuilder vb) throws VectorBuilderException;

    /**
     * Zjištění, zda tvar uzlu obsahuje zadaný bod.
     * @param p zkoumaný bod
     * @return zda tvar obsahuje zadaný bod.
     */
    public boolean contains(Point2D p);

    /**
     * @return ohraničení (bouding box) uzlu
     */
    public Box2D getBoundingBox();

    public void setZoom(Zoom zoom);
}
