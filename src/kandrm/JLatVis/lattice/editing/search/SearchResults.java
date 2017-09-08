package kandrm.JLatVis.lattice.editing.search;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.logical.Tag;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.logical.OrderPair;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.TagShape;
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
public class SearchResults implements IXmlSerializable {
    private LatticeShape latticeShape;
    /**
     * kolekce nalezených položek
     */
    private List<IFoundabe> foundItems = new ArrayList<IFoundabe>();
 
    public SearchResults(LatticeShape lattice){
        this.latticeShape = lattice;
    }
    
    /**
     * Uloží nalezené prvky.
     * 
     * @param found nalezené prvky
     */
    public void saveFoundItems(List<IFoundabe> found){
        List<IFoundabe> items = new ArrayList<IFoundabe>(){{
            addAll(latticeShape.getNodes());
            addAll(latticeShape.getTags());
            addAll(latticeShape.getEdges());
        }};
        for(IFoundabe item : items){
            if(item.isFound()){
                item.setFound(false);
            }
        }
        if(found != null){
            for(IFoundabe item : found){
                item.setFound(true);
            }
        }
        foundItems = found;
        latticeShape.repaint();
    }

    /**
     * Uloží nalezené uzly reprezenotavé jejich logickou podobou.
     * Nalezené prvky se ukládají v podobě vizuálních interpretací. Tato funkce
     * převede logickou reprezentaci uzlů na jejich vizuální reprezentaci a uloží výběr.
     *
     * @param found nalezené uzly
     */
    protected void saveFoundNodes(List<Node> found){
        List<IFoundabe> foundNodeShapes = null;
        if(found != null){
            foundNodeShapes = new ArrayList<IFoundabe>();
            for(Node node : found){
                foundNodeShapes.add(node.getShape());
            }
        }
        saveFoundItems(foundNodeShapes);
    }

    /**
     * Uloží nalezené popisky uzlu reprezenotavé jejich logickou podobou.
     * Nalezené prvky se ukládají v podobě vizuálních interpretací. Tato funkce
     * převede logickou reprezentaci popisku na jejich vizuální reprezentaci a uloží výběr.
     *
     * @param found nalezené popisky
     */
    protected void saveFoundTags(List<Tag> found){
        List<IFoundabe> foundTagShapes = null;
        if(found != null){
            foundTagShapes = new ArrayList<IFoundabe>();
            for(Tag tag : found){
                foundTagShapes.add(tag.getShape());
            }
        }
        saveFoundItems(foundTagShapes);
    }

    protected void saveFoundEdges(List<OrderPair> found){
        List<IFoundabe> foundShapes = null;
        if(found != null){
            foundShapes = new ArrayList<IFoundabe>();
            for(OrderPair pair : found){
                foundShapes.add( latticeShape.getEdgeByNodes(pair.getGreater().getShape(), pair.getLess().getShape()) );
            }
        }
        saveFoundItems(foundShapes);
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
        if(foundItems == null){
            return items;
        }
        for(IFoundabe i : foundItems){
            if(i instanceof ISelectable){
                items.add((ISelectable)i);
            }
        }
        return items;
    }

    /**
     * Smaže (vynuluje) výsledky vyhledávání.
     */
    public void reset(){
        saveFoundItems(null);
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
        if( ! element.getNodeName().equals("SearchResults")){
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
                foundItems.add(latticeShape.getNodeById(el.getTextContent()));
            } else if(elName.equals("Edge")){
                String u1 = el.getAttribute("u1");
                String u2 = el.getAttribute("u2");
                if(u1.equals("") || u2.equals("")){
                    throw new XmlInvalidException();
                }
                foundItems.add(latticeShape.getEdgeByNodes(u1, u2));
            } else if(elName.equals("Tag")){
                foundItems.add(latticeShape.getTagByName(el.getTextContent()));
            } else {
                throw new XmlInvalidException();
            }
        }
        for(IFoundabe i : foundItems){
            i.setFound(true);
        }
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("SearchResults");
            if(foundItems != null){
                for(IFoundabe i : foundItems){
                    if(i instanceof NodeShape){
                        writer.writeEntityWithText("Node", ((NodeShape)i).getLogicalNode().getId());
                    } else if(i instanceof EdgeShape){
                        writer.writeEntity("Edge")
                            .writeAttribute("u1", ((EdgeShape)i).getU1().getLogicalNode().getId())
                            .writeAttribute("u2", ((EdgeShape)i).getU2().getLogicalNode().getId())
                        .endEntity();
                    } else if(i instanceof TagShape){
                        writer.writeEntityWithText("Tag", ((TagShape)i).getLogicalTag().getName());
                    }
                }
            }
        writer.endEntity();
        return writer;
    }
}
