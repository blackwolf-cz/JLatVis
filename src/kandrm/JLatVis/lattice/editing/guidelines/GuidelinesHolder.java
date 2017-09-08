package kandrm.JLatVis.lattice.editing.guidelines;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JPanel;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import math.geom2d.Point2D;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class GuidelinesHolder extends JPanel implements IGuideline, IXmlSerializable {
    private GuidelineVisualSettings settings;
    private LatticeShape lattice;
    
    private MainGrid mainGrid;
    private LatticeGrid latticeGrid;

    public GuidelinesHolder(LatticeShape lattice){
        this(lattice, new GuidelineVisualSettings());
    }

    public GuidelinesHolder(LatticeShape lattice, GuidelineVisualSettings visualSettings){
        this.lattice = lattice;
        this.settings = visualSettings;
        mainGrid = new MainGrid(lattice);
        latticeGrid = new LatticeGrid(lattice);

        add(mainGrid);
        add(latticeGrid);
        setOpaque(false);
    }

    @Override
    public boolean isInStickRange(Point2D p) {
        return (mainGrid.isShown() && mainGrid.isInStickRange(p)) || (latticeGrid.isShown() && latticeGrid.isInStickRange(p));
    }

    @Override
    public Point2D findNearestPoint(Point2D p) {
        Point2D nearest = null;
        if(latticeGrid.isShown()){
            nearest = latticeGrid.findNearestPoint(p);
        }
        if(nearest == null && mainGrid.isShown()){
            nearest = mainGrid.findNearestPoint(p);
        }
        return nearest;
    }

    @Override
    public GuidelineVisualSettings getVisualSettings() {
        return new GuidelineVisualSettings(settings);
    }

    @Override
    public void setVisualSettings(GuidelineVisualSettings visualSettings) {
        this.settings = visualSettings;
        mainGrid.setVisualSettings(settings);
        latticeGrid.setVisualSettings(settings);
    }

    public LatticeGrid getLatticeGrid() {
        return latticeGrid;
    }

    public MainGrid getMainGrid() {
        return mainGrid;
    }

    public boolean isShown(){
        return mainGrid.isShown() || latticeGrid.isShown();
    }

    public void refresh(List<NodeShape> exclude, Point2D shift){
        mainGrid.refresh(shift);
        latticeGrid.refresh(exclude);
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

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Guidelines");
            settings.toXml(writer)
            .writeEntity("CenterPoint").writeAttribute("x", mainGrid.getCenterPoint().x).writeAttribute("y", mainGrid.getCenterPoint().y)
            .endEntity();
        writer.endEntity();
        return writer;
    }

    public void fromXml(Element element) throws XmlInvalidException {
        if( ! element.getNodeName().equals("Guidelines")){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Style")){
                setVisualSettings(GuidelineVisualSettings.fromXml(el));
            } else if(elName.equals("CenterPoint")){
                String x = el.getAttribute("x");
                String y = el.getAttribute("y");
                if( ! x.equals("") && ! y.equals("")){
                    mainGrid.setCenterPoint(new Point2D(Double.parseDouble(x), Double.parseDouble(y)));
                }
            }
        }
    }
}
