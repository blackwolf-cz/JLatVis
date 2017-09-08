package kandrm.JLatVis.guiConnect.settings.logical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventNodeTags;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.logical.Tag;
import kandrm.JLatVis.lattice.logical.Node;

/**
 *
 * @author Michal Kandr
 */
public class NodeTagsModel extends AbstractTableModel implements IHistoryEventSender {
    private class NodeTagsTableRow{
        public String name;
        public Boolean visible;
        public Tag tag;

        public NodeTagsTableRow(){
            this(new String("tag name"), false, null);
        }
        public NodeTagsTableRow(String name, boolean visible, Tag tag) {
            this.name = name;
            this.visible = visible;
            this.tag = tag;
        }

        public Object getByIndex(int i){
            Object ret = null;
            switch(i){
                case 0:
                    ret = name;
                    break;
                case 1:
                    ret = visible;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
            return ret;
        }

        public void setValueAt(int i, Object value){
            switch(i){
                case 0:
                    name = (String)value;
                    break;
                case 1:
                    visible = (Boolean)value;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }
    }
    
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private String[] colNames = {"Name", "Visible"};
    private Class[] colTypes = {String.class, Boolean.class};

    private List<Node> nodes;
    private List<NodeTagsTableRow> rows = new ArrayList<NodeTagsTableRow>();


    public NodeTagsModel(){
        this((List<Node>)null);
    }
    public NodeTagsModel(Node node){
        this(Arrays.asList(node));
    }
    public NodeTagsModel(List<Node> nodes){
        setNodes(nodes);
    }

    public final void setNodes(List<Node> nodes){
        this.nodes = nodes;
        rows.clear();
        if(nodes != null){
            for(Node node : nodes){
                for(Tag tag : node.getTags()){
                    rows.add(new NodeTagsTableRow(tag.getName(), tag.getShape().isVisible(), tag));
                }
            }
        }
        fireTableDataChanged();
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

    public void save(){
        List<Tag> addedTags = new ArrayList<Tag>();
        List<Tag> removedTags = new ArrayList<Tag>();
        List<Tag> tagsInRows = new ArrayList<Tag>();
        //change settings and add tags
        for ( NodeTagsTableRow row : rows) {
            if(row.tag == null){
                for(Node node : nodes){
                    Tag newTag = node.addTag(row.name, "tag text");
                    newTag.getShape().setVisible(row.visible, false);
                    tagsInRows.add(newTag);
                    addedTags.add(newTag);
                }
            } else {
                row.tag.setName(row.name);
                row.tag.getShape().setVisible(row.visible);
                tagsInRows.add(row.tag);
            }
        }
        // remove tags
        List<Tag> tags = new ArrayList<Tag>();
        for(Node node : nodes){
            tags.addAll(node.getTags());
        }
        tags.removeAll(tagsInRows);
        for(Tag tag : tags){
            tag.getNode().removeTag(tag);
            removedTags.add(tag);
        }
        setNodes(nodes);
        if(addedTags.size() > 0 || removedTags.size() > 0){
            fireHistoryEvent(new HistoryEventNodeTags(addedTags, removedTags));
        }
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return colTypes[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getByIndex(columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        rows.get(rowIndex).setValueAt(columnIndex, value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addEmptyRow(){
        rows.add( new NodeTagsTableRow() );
        fireTableRowsInserted( rows.size() - 1, rows.size() - 1);
    }

    public void removeRow(int i){
        rows.remove(i);
        fireTableRowsDeleted(i, i);
    }
}
