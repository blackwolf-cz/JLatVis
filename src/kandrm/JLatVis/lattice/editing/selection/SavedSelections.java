package kandrm.JLatVis.lattice.editing.selection;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class SavedSelections implements IXmlSerializable {
    private List<ISavedSelectionsEventListener> changeListeners = new ArrayList<ISavedSelectionsEventListener>();

    private Selector selector;

    private List<Selection> selections = new ArrayList<Selection>();

    public SavedSelections(Selector selector) {
        this.selector = selector;
    }

    public void addHistoryEventListener(ISavedSelectionsEventListener listener) {
        if( ! changeListeners.contains(listener)){
            changeListeners.add(listener);
        }
    }
    public void removeHistoryEventListener(ISavedSelectionsEventListener listener) {
        changeListeners.remove(listener);
    }
    private void fireSavedSelectionsChangeEvent(SavedSelectionsChangeEvent evt){
        for(ISavedSelectionsEventListener l : changeListeners){
            l.savedSelectionsEvenOccurred(evt);
        }
    }

    public void saveSelection(Selection selection){
        selections.add(selection);
        fireSavedSelectionsChangeEvent(
            new SavedSelectionsChangeEvent(SavedSelectionsChangeEvent.Type.Add, selections.size())
        );
    }

    public void deleteSelectionAt(int index){
        selections.remove(index);
        fireSavedSelectionsChangeEvent(
            new SavedSelectionsChangeEvent(SavedSelectionsChangeEvent.Type.Delete, index)
        );
    }

    public Selection getSelectionAt(int index){
        return selections.get(index);
    }
    
    public int getSize() {
        return selections.size();
    }

    public void loadSelectionAt(int index){
        selector.selectItems(getSelectionAt(index).getSelectedItems(), true);
    }

    
    public void fromXml(Element element, LatticeShape lattice) throws XmlInvalidException {
        if( ! element.getNodeName().equals("SavedSelections")){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            selections.add( Selection.fromXml((Element)elNode, lattice) );
        }
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("SavedSelections");
            for(Selection i : selections){
                i.toXml(writer);
            }
        writer.endEntity();
        return writer;
    }
}
