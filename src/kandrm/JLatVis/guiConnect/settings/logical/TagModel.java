package kandrm.JLatVis.guiConnect.settings.logical;

import kandrm.JLatVis.lattice.editing.history.HistoryEventTagLogical;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.logical.Tag;

/**
 *
 * @author Michal Kandr
 */
public class TagModel {
    private Tag tag = null;

    private String name = null;
    private String text = null;

    public TagModel(){}

    public TagModel(Tag tag){
        setTag(tag);
    }

    public final void setTag(Tag tag) {
        this.tag = tag;
        name = tag.getName();
        text = tag.getText();
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }


    public void apply(){
        if(tag != null){
            HistoryEventTagLogical e = new HistoryEventTagLogical(tag, tag.getName(), name, tag.getText(), text);
            for (IHistoryEventListener l : tag.getHistoryListeners()) {
                l.eventPerformed( e );
            }
            tag.setName(name);
            tag.setText(text);
        }
    }
}
