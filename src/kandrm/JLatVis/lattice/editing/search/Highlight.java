package kandrm.JLatVis.lattice.editing.search;


import java.util.LinkedList;
import java.util.List;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;

public class Highlight {
    protected Lattice lattice;
    protected Selector selector;
    protected HighlightResults results;

    protected Highlight(Lattice lattice, Selector selector, HighlightResults results){
        this.lattice = lattice;
        this.selector = selector;
        this.results = results;
    }

    protected List<Node> getSelectedNodes() {
        List<Node> selectedNodes = new LinkedList<Node>();
        for (NodeShape node : selector.getSelectedNodes()) {
            selectedNodes.add(node.getLogicalNode());
        }
        return selectedNodes;
    }

    public void reset(){
        results.reset();
    }
}
