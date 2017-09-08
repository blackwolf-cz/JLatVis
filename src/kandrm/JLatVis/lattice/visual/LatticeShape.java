package kandrm.JLatVis.lattice.visual;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import javax.swing.event.ChangeEvent;
import kandrm.JLatVis.lattice.editing.NodeMover;
import kandrm.JLatVis.lattice.logical.*;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import kandrm.JLatVis.export.IVectorPaintable;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.gui.DraggZoom;
import kandrm.JLatVis.lattice.editing.TagMover;
import kandrm.JLatVis.lattice.editing.NodeNameMover;
import kandrm.JLatVis.lattice.editing.ShowHide;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.editing.guidelines.GuidelinesHolder;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventLatticeVisual;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.editing.search.HighlightResults;
import kandrm.JLatVis.lattice.visual.settings.LatticeVisualSettings;
import kandrm.JLatVis.lattice.editing.search.LogicalSearch;
import kandrm.JLatVis.lattice.editing.search.OnlineHighlight;
import kandrm.JLatVis.lattice.editing.search.SearchResults;
import kandrm.JLatVis.lattice.editing.search.TextSearch;
import kandrm.JLatVis.lattice.editing.search.VisualSearch;
import kandrm.JLatVis.lattice.editing.selection.SavedSelections;
import kandrm.JLatVis.lattice.visual.settings.LineVisualSettings;
import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Vizuální reprezentace prvku svazu - uzel hasseova diagramu. Obsahuje informace
 * potřebné pro vizualizaci prvku a umí se nakreslit.
 *
 * @author Michal Kandr
 */
public class LatticeShape extends JPanel implements IXmlSerializable, IVectorPaintable, IHistoryEventSender {
    public static final int MIN_DESC_PARENT_DISTANCE = 1;

    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    /**
     * Logická reprezentace prvku.
     */
    private Lattice logicalLattice;

    /**
     * Viuální reprezentace uzlů diagramu.
     */
    private List<NodeShape> nodes = new ArrayList<NodeShape>();
    /**
     * Vizuální reprezentace hran mezi uzly.
     */
    private List<EdgeShape> edges = new ArrayList<EdgeShape>();

    /**
     * Nastavení vizuálních vlastností.
     */
    private LatticeVisualSettings settings;

    /**
     * Objekt zajišťující možnost přesunu uzlů.
     */
    private NodeMover nodeMover;
    private TagMover tagMover;
    private NodeNameMover nodeNameMover;
    /**
     * Objekt umožňující uživateli vybírat prvky.
     */
    private Selector selector;
    private OnlineHighlight onlineHighlight;
    /**
     * Kontejner pro uložené výběry.
     */
    private SavedSelections savedSelections;

    private GuidelinesHolder guidelines;

    /**
     * Výsledky vyhledávání.
     */
    private SearchResults searchResults;
    private HighlightResults highlightResults;
    /**
     * Logické vyhledávání
     */
    private LogicalSearch logicalSearch;
    /**
     * Textové vyhledávání.
     */
    private TextSearch textSearch;
    private VisualSearch visualSearch;

    private ShowHide showHide;

    private DraggZoom draggZoom;

    private Zoom zoom;
    private History history;


    public LatticeShape(){
        this(null);
    }
    /**
     * Nová vizuální repezentace svazu s defaultním vizuálním nastavením.
     *
     * @param logicalLattice logická reprezentace svazu
     */
    public LatticeShape(Lattice logicalLattice){
        this(logicalLattice, new LatticeVisualSettings());
    }
    /**
     * Nová vizuální repezentace svazu s nastavenou vizuální podobou.
     *
     * @param logicalLattice logická reprezentace svazu
     * @param settings vizuální nastavení
     */
    public LatticeShape(Lattice logicalLattice, LatticeVisualSettings settings){
        zoom = new Zoom();
        history = new History();
        addHistoryListener(history);
        
        this.logicalLattice = logicalLattice;
        this.logicalLattice.addHistoryListener(history);
        this.settings = settings;

        selector = new Selector(this);
        savedSelections = new SavedSelections(selector);
        nodeMover = new NodeMover(this);
        nodeMover.addHistoryListener(history);
        nodeNameMover = new NodeNameMover(this);
        nodeNameMover.addHistoryListener(history);
        tagMover = new TagMover(this);
        tagMover.addHistoryListener(history);
        guidelines = new GuidelinesHolder(this);

        searchResults = new SearchResults(this);
        textSearch = new TextSearch(logicalLattice, selector, searchResults);
        visualSearch = new VisualSearch(logicalLattice, selector, searchResults);

        highlightResults = new HighlightResults(this);
        logicalSearch = new LogicalSearch(logicalLattice, selector, highlightResults);
        onlineHighlight = new OnlineHighlight(logicalSearch);

        showHide = new ShowHide(this);
        showHide.addHistoryListener(history);

        draggZoom = new DraggZoom(this, new LineVisualSettings(Color.BLUE, new BasicStroke(1)));
        draggZoom.addFinishedListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                recountPreferredSize();
            }
        });
        draggZoom.unRegister();

        setLayout(null);
        add(guidelines);
        super.addMouseListener(selector);
        setBackground(settings.getBackgroundColor());
    }

    @Override
    public final void addHistoryListener(IHistoryEventListener l){
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


    public Zoom getZoom(){
        return zoom;
    }

    public History getHistory(){
        return history;
    }

    public void setZoom(Zoom zoom){
        this.zoom = zoom;
        for(NodeShape node : nodes){
            node.setZoom(zoom);
        }
        for(EdgeShape edge : edges){
            edge.setZoom(zoom);
        }
        draggZoom.setZoom(zoom);
    }

    /**
     * Vloží nový uzel.
     *
     * @param n logická repezentace uzlu
     */
    public void addNode(Node n){
        NodeShape nodeShape = n.getShape();
        if(nodeShape == null){
            nodeShape = new NodeShape(n);
            n.setShape(nodeShape);
        }
        addNode(nodeShape);
    }
    public void addNode(NodeShape n){
        nodes.add(n);
        add(n, 0);
        n.registerTagsGraphic(this);
        n.setZoom(zoom);
        n.addMouseListener(nodeMover);
        n.addMouseMotionListener(nodeMover);
        n.addMouseListener(tagMover);
        n.addMouseMotionListener(tagMover);
        n.addMouseListener(nodeNameMover);
        n.addMouseMotionListener(nodeNameMover);
        n.addMouseListener(onlineHighlight);
        recountPreferredSize();
        for(MouseListener l : getMouseListeners()){
            n.addMouseListener(l);
        }
        n.addHistoryListener(history);
    }

    private int getMaxNodeComponentIndex(){
        int maxIndex = 0;
        for(Component c : getComponents()){
            if(c instanceof NodeShape && getComponentZOrder(c) > maxIndex){
                maxIndex = getComponentZOrder(c);
            }
        }
        return maxIndex;
    }

    /**
     * Vloží novou hranu mezi dvěma uzly.
     *
     * @param u1
     * @param u2
     */
    public void addEdge(OrderPair pair){
        addEdge(new EdgeShape(pair));
    }
    public void addEdge(EdgeShape edge){
        edges.add(edge);
        add(edge, getMaxNodeComponentIndex() + 1);
        edge.addMouseListener(selector);
        for(MouseListener l : getMouseListeners()){
            edge.addMouseListener(l);
        }
        for(IHistoryEventListener l : historyListeners){
            edge.addHistoryListener(l);
        }
        edge.setZoom(zoom);
    }
    public void removeEdge(OrderPair pair){
        for(EdgeShape edge : edges){
            if((edge.getU1() == pair.getLess().getShape() && edge.getU2() == pair.getGreater().getShape())
                    || edge.getU1() == pair.getGreater().getShape() && edge.getU2() == pair.getLess().getShape()){
                edges.remove(edge);
                remove(edge);
                break;
            }
        }
    }

    public void moveTo(Point2D target){
        Rectangle bounds = getRealBounds();
        moveBy(new Point2D(target.getX() - zoom.reverse(bounds.x), target.getY() - zoom.reverse(bounds.y)));
    }

    public void moveBy(Point2D moveBy){
        for(NodeShape node : nodes){
            node.moveBy(moveBy);
        }
    }

    public Lattice getLogicalLattice(){
        return logicalLattice;
    }

    /**
     * @return všechny uzly
     */
    public List<NodeShape> getNodes(){
        return Collections.unmodifiableList(nodes);
    }

    public NodeShape getNodeById(String name){
        NodeShape found = null;
        for(NodeShape node : nodes){
            if(node.getLogicalNode().getId().equals(name)){
                found = node;
                break;
            }
        }
        return found;
    }

    public List<NodeShape> getHiddenNodes(){
        List<NodeShape> hiddenNodes = new ArrayList<NodeShape>();
        for(NodeShape node : nodes){
            if( ! node.isVisible()){
                hiddenNodes.add(node);
            }
        }
        return hiddenNodes;
    }

    /**
     * @return všechny popisky všech uzlů
     */
    public List<TagShape> getTags(){
        List<TagShape> tags = new ArrayList<TagShape>();
        for(NodeShape node : getNodes()){
            tags.addAll(node.getTags());
        }
        return tags;
    }

    public List<TagShape> getHiddenTags(){
        List<TagShape> tags = new ArrayList<TagShape>();
        for(NodeShape node : getNodes()){
            for(TagShape tag : node.getTags()){
                if( ! tag.isVisible()){
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public TagShape getTagByName(String name){
        TagShape found = null;
        for(TagShape tag : getTags()){
            if(tag.getLogicalTag().getName().equals(name)){
                found = tag;
                break;
            }
        }
        return found;
    }

    /**
     * @return všechny hrany
     */
    public List<EdgeShape> getEdges(){
        return Collections.unmodifiableList(edges);
    }

    public EdgeShape getEdgeByNodes(String u1, String u2){
        for(EdgeShape edge : getEdges()){
            if(edge.getU1().getLogicalNode().getId().equals(u1) && edge.getU2().getLogicalNode().getId().equals(u2)){
                return edge;
            }
        }
        return null;
    }
    public EdgeShape getEdgeByNodes(NodeShape u1, NodeShape u2){
        for(EdgeShape edge : getEdges()){
            if(edge.getU1() == u1 && edge.getU2() == u2){
                return edge;
            }
        }
        return null;
    }

    /**
     * @return obsluha výsledků vyhledávání
     */
    public SearchResults getSearchResults(){
        return searchResults;
    }

    public HighlightResults getHighlightResults(){
        return highlightResults;
    }

    /**
     * @return obsluha logického vyhledávání
     */
    public LogicalSearch getLogicalSearch(){
        return logicalSearch;
    }

    /**
     * @return obsluha textového vyhledávání
     */
    public TextSearch getTextSearch(){
        return textSearch;
    }

    public VisualSearch getVisualSearch(){
        return visualSearch;
    }

    public ShowHide getShowHide(){
        return showHide;
    }

    /**
     * @return uživatelem vybrané uzly
     */
    public List<NodeShape> getSelectedNodes(){
        return selector.getSelectedNodes();
    }

    /**
     * @return uživatelem vybrané popisky
     */
    public List<TagShape> getSelectedTags(){
        return selector.getSelectedTags();
    }

    /**
     * @return uživatelem vybrané hrany
     */
    public List<EdgeShape> getSelectedEdges(){
        return selector.getSelectedEdges();
    }

    /**
     * @return obsluha výběru prvků
     */
    public Selector getSelector(){
        return selector;
    }

    public OnlineHighlight getOnlineHighlight() {
        return onlineHighlight;
    }

    /**
     * @return obsluha uložených výběrů
     */
    public SavedSelections getSavedSelections(){
        return savedSelections;
    }

    public GuidelinesHolder getGuidelines() {
        return guidelines;
    }

    public NodeMover getNodeMover(){
        return nodeMover;
    }


    public DraggZoom getDragZoom(){
        return draggZoom;
    }

    public void showDraggZoom(){
        draggZoom.register();
        selector.getDraggSelector().unRegister();
    }

    public void hideDragZoom(){
        draggZoom.unRegister();
        selector.getDraggSelector().register();
    }


    /**
     * @return vizuální nastavení uzlu
     */
    public LatticeVisualSettings getVisualSettings() {
        return new LatticeVisualSettings(settings);
    }
    /**
     * Změní vizuální nastavení uzlu.
     *
     * @param settings nové vizuální nastavení
     * @param saveHistory uložit změnu do historie
     */
    public void setVisualSettings(LatticeVisualSettings settings, boolean saveHistory) {
        if( ! this.settings.equals(settings)){
            if(saveHistory){
                fireHistoryEvent( new HistoryEventLatticeVisual(this, getVisualSettings(), new LatticeVisualSettings(settings) ) );
            }
            this.settings = settings;
            setBackground(settings.getBackgroundColor());
            repaint();
        }
    }
    /**
     * Změní vizuální nastavení uzlu. Změna bude automaticky uložena do historie.
     *
     * @param settings nové vizuální nastavení
     */
    public void setVisualSettings(LatticeVisualSettings settings){
        setVisualSettings(settings, true);
    }


    /**
     * Přepočítá preferovanou velikost svazu. Metoda se volá při každé změně,
     * která může vést ke změně rozměrů svazu.
     * Do rozměrů jsou zahrnuty všechny komponenty svazu (uzly, popisky a hrany)
     * a nepočítá se do nich prostor potřebný pro zobrazení zvýraznění vybraných
     * a nalezených prvků.
     * Výsledná velikost je pak nastavena jako preferredSize komponenty.
     */
    public void recountPreferredSize(){
        int maxX = 0;
        int maxY = 0;

        for(NodeShape n : nodes){
            Rectangle bounds = n.getRealBounds();
            if(maxX < bounds.getX() + bounds.getWidth()){
                maxX = (int) (bounds.getX() + bounds.getWidth());
            }
            if(maxY < bounds.getY() + bounds.getHeight()){
                maxY = (int) (bounds.getY() + bounds.getHeight());
            }
        }

        Dimension newPrefSize = new Dimension(maxX, maxY);
        if( ! newPrefSize.equals(getPreferredSize())){
            setSize(newPrefSize);
            setPreferredSize(newPrefSize);
            revalidate();
        }
    }

    public Rectangle getBoundsWithoutNode(NodeShape node){
        double minX = Integer.MAX_VALUE,
            maxX = Integer.MIN_VALUE,
            minY = Integer.MAX_VALUE,
            maxY = Integer.MIN_VALUE;

        for(NodeShape n : nodes){
            if(n == node){
                continue;
            }
            Rectangle bounds = n.getRealBounds();
            if(minX > bounds.getX()){
                minX = bounds.getX();
            }
            if(maxX < bounds.getX() + bounds.getWidth()){
                maxX = bounds.getX() + bounds.getWidth();
            }
            if(minY > bounds.getY()){
                minY = bounds.getY();
            }
            if(maxY < bounds.getY() + bounds.getHeight()){
                maxY = bounds.getY() + bounds.getHeight();
            }
        }
        return new Rectangle((int)Math.ceil(minX), (int)Math.ceil(minY), (int)Math.ceil(maxX - minX), (int)Math.ceil(maxY - minY));
    }

    public Rectangle getRealBounds(){
        return getBoundsWithoutNode(null);
    }

    @Override
    public void addMouseListener(MouseListener l){
        super.addMouseListener(l);
        for(NodeShape n : nodes){
            n.addMouseListener(l);
        }
        for(EdgeShape e : edges){
            e.addMouseListener(l);
        }
    }

    @Override
    public void paintVector(VectorBuilder vb) throws VectorBuilderException {
        Rectangle bounds = getRealBounds();

        vb.setPadding( - bounds.x, - bounds.y);

        if(settings.getBackgroundColor() != null){
            vb.fill(new Rectangle2D(0, 0, bounds.width + bounds.x, bounds.height + bounds.y), settings.getBackgroundColor());
        }

        for(EdgeShape edge : edges){
            edge.paintVector(vb);
        }
        for(NodeShape node : nodes){
            node.paintVector(vb);
        }
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Diagram");
            if(nodes.size() > 0){
                writer.writeEntity("Verteces");
                for(NodeShape node : nodes){
                    node.toXml(writer);
                }
                writer.endEntity()
                .writeEntity("Edges");
                for(EdgeShape edge : edges){
                    edge.toXml(writer);
                }
                writer.endEntity();
            }
            settings.toXml(writer);
        writer.endEntity();
        return writer;
    }

    public XmlWriter toXmlAppSpecific(XmlWriter writer) throws IOException {
        selector.toXml(writer);
        searchResults.toXml(writer);
        savedSelections.toXml(writer);
        guidelines.toXml(writer);
        return writer;
    }

    private static void fromXmlVerteces(Element element, LatticeShape latticeShape) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            if(elNode.getNodeName().equals("Vertex")){
                NodeShape n = NodeShape.fromXml((Element)elNode, latticeShape);
                latticeShape.addNode( n );
                n.getLogicalNode().setShape(n);
            } else {
                throw new XmlInvalidException();
            }
        }
    }

    private static void fromXmlEdges(Element element, LatticeShape latticeShape) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            if(elNode.getNodeName().equals("Edge")){
                latticeShape.addEdge( EdgeShape.fromXml((Element)elNode, latticeShape) );
            } else {
                throw new XmlInvalidException();
            }
        }
    }

    public static LatticeShape fromXml(Element element, Lattice lattice) throws XmlInvalidException {
        if( ! element.getNodeName().equals("Diagram")){
            throw new XmlInvalidException();
        }

        LatticeShape latticeShape = new LatticeShape(lattice);
        latticeShape.logicalLattice = lattice;
        Element visualSettings = null;
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Verteces")){
                fromXmlVerteces(el, latticeShape);
            } else if(elName.equals("Edges")){
                fromXmlEdges(el, latticeShape);
            } else {
                visualSettings = el;
            }
        }
        latticeShape.settings = LatticeVisualSettings.fromXml(visualSettings);

        return latticeShape;
    }

    public void fromXmlAppSpecific(Element element) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Selection")){
                selector.fromXml(el);
            } else if(elName.equals("SearchResults")){
                searchResults.fromXml(el);
            } else if(elName.equals("SavedSelections")){
                savedSelections.fromXml(el, this);
            } else if(elName.equals("Guidelines")){
                guidelines.fromXml(el);
            }
        }
    }
}