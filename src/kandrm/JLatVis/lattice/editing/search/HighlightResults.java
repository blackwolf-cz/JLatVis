package kandrm.JLatVis.lattice.editing.search;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Třída sloužící k uchovávání výsledků vyhledávání a práci s nimi. Uchovává si
 * kolekci nalezených prvků a umožňuje s nimi provádět operace vztažené k výběru
 * (označit, odznačit atp.).
 *
 * Třída slouží k propojení tříd realizujících vyhledávání a Slectoru starajícím se
 * o výběr prvků.
 *
 * @author Michal Kandr
 */
public class HighlightResults implements IXmlSerializable {
    private LatticeShape latticeShape;
    /**
     * kolekce nalezených položek
     */
    private List<NodeShape> highlightedItems = new ArrayList<NodeShape>();

    public HighlightResults(LatticeShape lattice){
        this.latticeShape = lattice;
    }

    /**
     * Uloží nalezené prvky.
     *
     * @param found nalezené prvky
     * @param saveHistory uložit operaci do historie
     */
    public void saveFoundItems(List<NodeShape> found){
        for(NodeShape item : latticeShape.getNodes()){
            item.setHighlighted(false);
        }
        if(found != null){
            for(NodeShape item : found){
                item.setHighlighted(true);
            }
        }
        highlightedItems = found;
        latticeShape.repaint();
    }

    protected void saveFoundNodes(List<Node> found){
        List<NodeShape> foundNodeShapes = null;
        if(found != null){
            foundNodeShapes = new ArrayList<NodeShape>();
            for(Node node : found){
                foundNodeShapes.add(node.getShape());
            }
        }
        saveFoundItems(foundNodeShapes);
    }
    /**
     * Smaže (vynuluje) výsledky vyhledávání.
     */
    public void reset(){
        saveFoundItems(null);
    }

    /**
     * Vybere z uložených (nalezených) prvků prvky určitého typu.
     * Operace výběru lze provádět zvlášť pro různé typy nalezených prvků a vyhledávání
     * může najít prvky různých typů. Metoda vybere jen ty požadovaného typu.
     *
     * @param type typ prvků, které mají výt vybrány
     *
     * @return nalezené prvky příslušného typu
     */
    private List<ISelectable> getItemsToSelect(){
        List<ISelectable> items = new ArrayList<ISelectable>();
        if(highlightedItems == null){
            return items;
        }
        for(NodeShape i : highlightedItems){
            if(i instanceof ISelectable){
                items.add((ISelectable)i);
            }
        }
        return items;
    }

    /**
     * Přidá nalezené prvky k výběru. Označí je jako vybrané a zachová předchozí výběr.
     *
     * @param type typ položek, pro které se má operace provést
     */
    public void addToSelection(){
        latticeShape.getSelector().selectItems(getItemsToSelect(), false);
    }

    /**
     * Nastaví nalezevé položky jako výběr. Aktuální výběr zruší a výběrem se stanou nalezené položky.
     *
     * @param type typ položek, pro které se má operace provést
     */
    public void setAsSelection(){
        latticeShape.getSelector().selectItems(getItemsToSelect(), true);
    }
    /**
     * Odstraní nalezené položky z výběru. Všechny nalezené položky označí jako nevybrané.
     *
     * @param type typ položek, pro které se má operace provést
     */
    public void removeFromSelection(){
        latticeShape.getSelector().unselectItems(getItemsToSelect());
    }

    public void fromXml(Element element) throws XmlInvalidException {
        if( ! element.getNodeName().equals("HighlightResults")){
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
            if(elName.equals("Node")){
                highlightedItems.add(latticeShape.getNodeById(el.getTextContent()));
            } else {
                throw new XmlInvalidException();
            }
        }
        for(NodeShape i : highlightedItems){
            i.setHighlighted(true);
        }
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("HighlightResults");
            if(highlightedItems != null){
                for(NodeShape i : highlightedItems){
                    if(i instanceof NodeShape){
                        writer.writeEntityWithText("Node", ((NodeShape)i).getLogicalNode().getId());
                    }
                }
            }
        writer.endEntity();
        return writer;
    }
}
