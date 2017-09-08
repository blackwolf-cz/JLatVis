package kandrm.JLatVis.lattice.editing;

import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventVisibility;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;

/**
 *
 * @author Michal Kandr
 */
public class ShowHide implements IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private LatticeShape latticeShape;

    public ShowHide(LatticeShape lattice) {
        this.latticeShape = lattice;
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
    }
    private void fireHistoryEvent(HistoryEvent e){
        for (IHistoryEventListener l : historyListeners) {
            l.eventPerformed(e);
        }
    }

    public List<? extends IHidable> showHidden(List<? extends IHidable> items, boolean saveHistory){
        List<IHidable> hidden = new ArrayList<IHidable>();
        for(IHidable n : items){
            if( ! n.isVisible()){
                n.setVisible(true, false);
                hidden.add(n);
            }
        }
        if(saveHistory){
            fireHistoryEvent(new HistoryEventVisibility(hidden, false, true));
        }
        latticeShape.recountPreferredSize();
        latticeShape.repaint();
        return hidden;
    }

    public List<? extends IHidable> showHiddenNodes(boolean saveHistory){
        return showHidden(latticeShape.getNodes(), saveHistory);
    }
    public List<? extends IHidable> showHiddenNodes(){
        return showHiddenNodes(true);
    }
    public List<? extends IHidable> showSelectHiddenNodes(boolean saveHistory){
        List<NodeShape> hidden = (List<NodeShape>) showHiddenNodes(saveHistory);
        latticeShape.getSelector().selectItems(hidden, true);
        return hidden;
    }
    public List<? extends IHidable> showSelectHiddenNodes(){
        return showSelectHiddenNodes(true);
    }

    public List<? extends IHidable> showHiddenTags(boolean saveHistory){
        List<IHidable> items = new ArrayList<IHidable>();
        for(NodeShape n : latticeShape.getNodes()){
            items.addAll(n.getTags());
        }
        return showHidden(items, saveHistory);
    }
    public List<? extends IHidable> showHiddenTags(){
        return showHiddenTags(true);
    }
    public List<? extends IHidable> showSelectHiddenTags(boolean saveHistory){
        List<TagShape> hidden = (List<TagShape>) showHiddenTags(saveHistory);
        latticeShape.getSelector().selectItems(hidden, true);
        return hidden;
    }
    public List<? extends IHidable> showSelectHiddenTags(){
        return showSelectHiddenTags(true);
    }

    public List<? extends IHidable> showHiddenAll(boolean saveHistory){
        List<IHidable> items = new ArrayList<IHidable>();
        items.addAll(latticeShape.getNodes());
        //items.addAll(lattice.getEdges());
        for(NodeShape n : latticeShape.getNodes()){
            items.addAll(n.getTags());
        }
        return showHidden(items, saveHistory);
    }
    public List<? extends IHidable> showHiddenAll(){
        return showHiddenAll();
    }
    public List<? extends IHidable> showSelectHiddenAll(boolean saveHistory){
        List<IHidable> hidden = new ArrayList<IHidable>(showHiddenAll(saveHistory));
        List<ISelectable> hiddenSelectables = new ArrayList<ISelectable>();
        for(Object o : hidden){
            hiddenSelectables.add((ISelectable) o);
        }
        latticeShape.getSelector().selectItems(hiddenSelectables, true);
        return hidden;
    }
    public List<? extends IHidable> showSelectHiddenAll(){
        return showSelectHiddenAll(true);
    }

    

    private List<IHidable> hideSelected(List<IHidable> items, boolean saveHistory){
        for(IHidable n : items){
            n.setVisible(false, false);
        }
        if(saveHistory){
            fireHistoryEvent(new HistoryEventVisibility(items, true, false));
        }
        latticeShape.recountPreferredSize();
        latticeShape.repaint();
        return items;
    }

    public List<IHidable> hideSelectedNodes(boolean saveHistory){
        return hideSelected(new ArrayList<IHidable>(latticeShape.getSelector().getSelectedNodes()), saveHistory);
    }
    public List<IHidable> hideSelectedNodes(){
        return hideSelectedNodes(true);
    }

    public List<IHidable> hideSelectedTags(boolean saveHistory){
        return hideSelected(new ArrayList<IHidable>(latticeShape.getSelector().getSelectedTags()), saveHistory);
    }
    public List<IHidable> hideSelectedTags(){
        return hideSelectedTags(true);
    }

    public List<IHidable> hideSelectedAll(boolean saveHistory){
        List<IHidable> selected = new ArrayList<IHidable>(latticeShape.getSelector().getSelectedNodes());
        selected.addAll(latticeShape.getSelector().getSelectedTags());
        return hideSelected(selected, saveHistory);
    }
    public List<IHidable> hideSelectedAll(){
        return hideSelectedAll(true);
    }
}