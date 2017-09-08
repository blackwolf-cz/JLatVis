package kandrm.JLatVis.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import kandrm.JLatVis.Application;
import kandrm.JLatVis.export.DrawConnector;
import kandrm.JLatVis.export.ImageExport;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.export.XmlExport;
import kandrm.JLatVis.gui.search.TextSearchEdge;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.gui.search.TextSearchTag;
import kandrm.JLatVis.gui.search.TextSearchNode;
import kandrm.JLatVis.gui.selection.LoadSelection;
import kandrm.JLatVis.gui.selection.SaveSelection;
import kandrm.JLatVis.gui.settings.visual.TagSettings;
import kandrm.JLatVis.gui.settings.visual.EdgeSettings;
import kandrm.JLatVis.gui.settings.visual.LatticeSettings;
import kandrm.JLatVis.gui.settings.logical.NodeTags;
import kandrm.JLatVis.gui.settings.program.FreeSelectionSetting;
import kandrm.JLatVis.gui.settings.visual.GuidelinesSettings;
import kandrm.JLatVis.gui.settings.visual.NodeSettings;
import kandrm.JLatVis.guiConnect.search.visual.SearchTagModel;
import kandrm.JLatVis.guiConnect.search.visual.SearchEdgeModel;
import kandrm.JLatVis.guiConnect.search.visual.SearchNodeModel;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.editing.search.LogicalSearch.OnFlySearch;
import kandrm.JLatVis.lattice.editing.search.OnlineHighlight;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import kandrm.JLatVis.lattice.logical.Tag;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.logical.OrderPair;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeNameShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.Point2D;

/**
 *
 * @author Black Wolf
 */
public class MainGui extends javax.swing.JFrame {
    private static final int DEFAULT_PADDING = 20;

    private static final String TITLE = "JLatVis";

    private Application application;

    private Lattice lattice = null;
    private SaveSelection saveSelectionDialog;
    private LoadSelection loadSelectionDialog;

    private NodeSettings nodeSetting;
    private TagSettings tagSettings;
    private EdgeSettings edgeSettings;
    private GuidelinesSettings guidelinesSettings;

    private kandrm.JLatVis.gui.settings.logical.LatticeSetting latticeTextSetting;
    private kandrm.JLatVis.gui.settings.logical.NodeSetting nodeTextSetting;
    private kandrm.JLatVis.gui.settings.logical.EdgeSetting edgeTextSetting;
    private kandrm.JLatVis.gui.settings.logical.TagSetting tagTextSetting;

    private TextSearchNode searchTextNodes;
    private TextSearchTag searchTextTags;
    private TextSearchEdge searchTextEdges;
    private NodeSettings searchVisualNode;
    private EdgeSettings searchVisualEdge;
    private TagSettings searchVisualTag;

    private LatticeSettings latticeSettings;
    private NodeTags nodeTags;
    private File latticeFile = null;
    private ISelectable popUpActionItem = null;
    LatticeShape latticeShape = null;
    private FreeSelectionSetting selectionSetting;

    DrawConnector drawConnector = null;
    private DrawForm drawForm = null;

    private int lastSaveHistoryHash;


    /** Creates new form MainGui */
    public MainGui(Application application) {
        this.application = application;
        
        nodeSetting = new NodeSettings(this);
        tagSettings = new TagSettings(this);
        edgeSettings = new EdgeSettings(this);
        guidelinesSettings = new GuidelinesSettings(this);
        latticeTextSetting = new kandrm.JLatVis.gui.settings.logical.LatticeSetting(this);
        nodeTextSetting = new kandrm.JLatVis.gui.settings.logical.NodeSetting(this);
        tagTextSetting = new kandrm.JLatVis.gui.settings.logical.TagSetting(this);
        edgeTextSetting = new kandrm.JLatVis.gui.settings.logical.EdgeSetting(this);
        nodeTags = new NodeTags(this);
        selectionSetting = new FreeSelectionSetting(this, null);
        drawConnector = new DrawConnector(lattice);
        drawForm = new DrawForm(drawConnector);
        drawConnector.addLoadedListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                resizeByLattice();
            }
        });

        initComponents();
        helpAboutWindow = new HelpAbout();

        nodeTags.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                statusBar.setText("");
            }
        });

        addWindowListener(MainGuiCloser.getInstance());
    }

    public void setLattice(Lattice l, File file, Integer drawWithProgram){
        if(lattice==null){
            latticeFile = file;
            saveSelectionDialog = new SaveSelection(this, l.getShape().getSelector(), l.getShape().getSavedSelections());
            loadSelectionDialog = new LoadSelection(this, l.getShape().getSavedSelections());
            latticeSettings = new LatticeSettings(this, l.getShape());
            searchTextNodes = new TextSearchNode(this, l.getShape().getTextSearch());
            searchTextTags = new TextSearchTag(this, l.getShape().getTextSearch());
            searchTextEdges = new TextSearchEdge(this, l.getShape().getTextSearch());
            searchVisualNode = new NodeSettings(this, new SearchNodeModel(l.getShape().getVisualSearch()), true);
            searchVisualEdge = new EdgeSettings(this, new SearchEdgeModel(l.getShape().getVisualSearch()), true);
            searchVisualTag = new TagSettings(this, new SearchTagModel(l.getShape().getVisualSearch()), true);
        } else {
            saveSelectionDialog.setSelector(l.getShape().getSelector());
            saveSelectionDialog.setSavedSelections(l.getShape().getSavedSelections());
            loadSelectionDialog.setSavedSelections(l.getShape().getSavedSelections());
            latticeSettings.setLatticeShape(l.getShape());
            searchTextNodes.setTextSearch(l.getShape().getTextSearch());
            searchTextEdges.setTextSearch(l.getShape().getTextSearch());
            searchTextTags.setTextSearch(l.getShape().getTextSearch());
            searchVisualNode.setModel(new SearchNodeModel(l.getShape().getVisualSearch()));
            searchVisualEdge.setModel(new SearchEdgeModel(l.getShape().getVisualSearch()));
            searchVisualTag.setModel(new SearchTagModel(l.getShape().getVisualSearch()));
        }
        lattice = l;

        insertLatticeShape(l.getShape());

        drawConnector.setLattice(lattice);
        
        setTitle(TITLE + (latticeFile != null ? " - " + latticeFile.getName() : "" ));

        // hotfix
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resizeByLattice();
            }
        }, 300);
        
        
        // draw if positions are not set
        executeDrawIfPositionsNotSet(drawWithProgram);
    }
    
    public void setLattice(Lattice l, File file){
        setLattice(l, file, null);
    }
    
    private void executeDrawIfPositionsNotSet(Integer drawWithProgram){
        if(latticeShape == null){
            return;
        }
        boolean notSet = true;
        Point2D zeroNode = latticeShape.getNodes().get(0).getCenter();
        for(NodeShape n : latticeShape.getNodes()){
            if( ! n.getCenter().equals(zeroNode)){
                notSet = false;
                break;
            }
        }
        if(notSet){
            boolean showForm = true;
            if(drawWithProgram != null){
                showForm = ! drawConnector.drawWithProgram(drawWithProgram);
            }
            if(showForm){
                drawForm.setVisible(true, true);
            }
        }
    }

    private void insertLatticeShape(LatticeShape shape){
        latticeShape = shape;
        if(latticeShape != null){
            final History history = latticeShape.getHistory();
            
            nodeSetting.setHistory(history);
            edgeSettings.setHistory(history);
            tagSettings.setHistory(history);

            history.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(lastSaveHistoryHash == history.getActualPositionHash()){
                        setTitle(TITLE + " - " + ( latticeFile != null ? latticeFile.getName() : ""));
                    } else {
                        setTitle(TITLE + " - " + ( latticeFile != null ? latticeFile.getName() : "") + "*");
                    }
                }
            });

            lastSaveHistoryHash = history.getActualPositionHash();

            final Zoom zoom = latticeShape.getZoom();

            zoom.addFinishedListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(lattice != null){
                        lattice.getShape().recountPreferredSize();
                        Dimension newSize = lattice.getShape().getPreferredSize();
                        newSize.setSize(newSize.getWidth() + zoom.resize( DEFAULT_PADDING ), newSize.getHeight() + zoom.resize( DEFAULT_PADDING ));
                        lattice.getShape().setPreferredSize(newSize);
                        repaint();
                    }
                }
            });


            selectionSetting.setSelector(latticeShape.getSelector().getDraggSelector());
            latticeShape.addMouseListener(new MouseAdapter() {
                private void showPopUp(MouseEvent e){
                    if(e.isPopupTrigger()){
                        if(e.getSource() == latticeShape){
                            for (EdgeShape edge : latticeShape.getEdges()){
                                if(edge.containsWithTolerance(e.getX(), e.getY())){
                                    e.setSource(edge);
                                    break;
                                }
                            }
                        }
                        if(e.getSource() == latticeShape){
                            latticePopupMenu.show(latticeShape, e.getX(), e.getY());
                        } else if(e.getSource().getClass() == NodeShape.class || e.getSource() instanceof NodeNameShape){
                            nodePopupMenu.show(latticeShape, e.getX(), e.getY());
                        } else if(e.getSource().getClass() == EdgeShape.class){
                            edgePopupMenu.show(latticeShape, e.getX(), e.getY());
                        } else if(e.getSource().getClass() == TagShape.class){
                            tagPopupMenu.show(latticeShape, e.getX(), e.getY());
                        }
                        if(e.getSource() instanceof ISelectable && ! ((ISelectable)e.getSource()).isSelected()){
                            popUpActionItem = (ISelectable)e.getSource();
                        } else if(e.getSource() instanceof NodeNameShape && ! ((NodeNameShape)e.getSource()).getNode().isSelected() ){
                            popUpActionItem = ((NodeNameShape)e.getSource()).getNode();
                        } else {
                            popUpActionItem = null;
                        }
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    showPopUp(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    showPopUp(e);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    showPopUp(e);
                }
            });
            
            latticeShape.addMouseListener(new MouseAdapter() {
                private int DOUBLE_CLICK_INTERVAL = 400;
                
                private Object lastClickedObject = null;
                private long lastClickTime = 0;
                        
                @Override
                public void mouseClicked(MouseEvent e) {
                    Object source = e.getSource();
                    if(source == latticeShape){
                        for (EdgeShape edge : latticeShape.getEdges()){
                            if(edge.containsWithTolerance(e.getX(), e.getY())){
                                source = edge;
                                break;
                            }
                        }
                    }
                    if(source.getClass() == NodeShape.class || source instanceof NodeNameShape
                            || source.getClass() == EdgeShape.class || source.getClass() == TagShape.class){
                        if(lastClickedObject == source && System.nanoTime()-lastClickTime < DOUBLE_CLICK_INTERVAL*1000000){
                            ActionEvent evt = new ActionEvent(source, 0, "");
                            if(source.getClass() == NodeShape.class || source instanceof NodeNameShape){
                                nodesPropertiesSettingActionPerformed(evt);
                            } else if(source.getClass() == EdgeShape.class){
                                edgesPropertiesSettingActionPerformed(evt);
                            } else if(source.getClass() == TagShape.class){
                                tagsPropertiesSettingActionPerformed(evt);
                            }
                            lastClickTime = 0;
                            lastClickedObject = null;
                        } else {
                            lastClickTime = System.nanoTime();
                            lastClickedObject = source;
                        }
                    }
                }
            });

            MouseAdapter coordsMotionListener = new MouseAdapter() {
                private void showCoords(MouseEvent e){
                    statusBar.setText("x: "+zoom.reverse(e.getX())+", y: "+zoom.reverse(e.getY()));
                }
                @Override
                public void mouseMoved(MouseEvent e) {
                    showCoords(e);
                }
                @Override
                public void mouseDragged(MouseEvent e){
                    showCoords(e);
                }
            };
            latticeShape.addMouseMotionListener(coordsMotionListener);
            for(Component c : latticeShape.getComponents()){
                c.addMouseMotionListener(coordsMotionListener);
            }

            latticeShape.setAutoscrolls(true);
            javax.swing.GroupLayout lattice1Layout = new javax.swing.GroupLayout(latticeShape);
            latticeShape.setLayout(lattice1Layout);
            lattice1Layout.setHorizontalGroup(
                lattice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, latticeShape.getWidth(), Short.MAX_VALUE)
            );
            lattice1Layout.setVerticalGroup(
                lattice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, latticeShape.getHeight(), Short.MAX_VALUE)
            );

            final DraggZoom draggZoom = latticeShape.getDragZoom();
            draggZoom.addFinishedListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    Point2D topLeftPoint = draggZoom.getTopLeftPointWithoutZoom();
                    jScrollPane1.getViewport().setViewPosition(new Point(
                        (int)zoom.resize(topLeftPoint.x),
                        (int)zoom.resize(topLeftPoint.y)));
                }
            });


        }
        jScrollPane1.setViewportView(latticeShape);
        repaint();
    }

    protected void resizeByLattice(){
        if(latticeShape != null){
            latticeShape.moveTo(new Point2D(DEFAULT_PADDING, DEFAULT_PADDING));
            Rectangle latticeBounds = latticeShape.getRealBounds();
            Dimension oldViewportSize = jScrollPane1.getViewport().getSize();
            jScrollPane1.getViewport().setSize(latticeBounds.width + latticeBounds.x, latticeBounds.height + latticeBounds.y);
            Dimension newViewportSize = jScrollPane1.getViewport().getSize();
            setSize(getWidth() - oldViewportSize.width + newViewportSize.width + DEFAULT_PADDING, getHeight() - oldViewportSize.height + newViewportSize.height + DEFAULT_PADDING);
            latticeShape.recountPreferredSize();
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new kandrm.JLatVis.gui.LoadFileChooser();
        exportFileChooser = new kandrm.JLatVis.gui.ExportFileChooser();
        latticePopupMenu = new javax.swing.JPopupMenu();
        latticePopupVisualSetting = new javax.swing.JMenuItem();
        latticePopupPropertiesSetting = new javax.swing.JMenuItem();
        nodePopupMenu = new javax.swing.JPopupMenu();
        nodesPopupVisualSetting = new javax.swing.JMenuItem();
        nodesPopupPropertiesSetting = new javax.swing.JMenuItem();
        nodesPopupTagsSetting = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        nodesPopupVisualSearch = new javax.swing.JMenuItem();
        nodesPopupTextSearch = new javax.swing.JMenuItem();
        edgePopupMenu = new javax.swing.JPopupMenu();
        edgesPopupVisualSetting = new javax.swing.JMenuItem();
        edgesPopupPropertiesSetting = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        edgePopupVisualSearch = new javax.swing.JMenuItem();
        edgePopupTextSearch = new javax.swing.JMenuItem();
        tagPopupMenu = new javax.swing.JPopupMenu();
        tagsPopupVisualSetting = new javax.swing.JMenuItem();
        tagsPopupPropertiesSetting = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        tagsPopupVisualSeach = new javax.swing.JMenuItem();
        tagsPopupTextSeach = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        statusBar = new javax.swing.JLabel();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openFile = new javax.swing.JMenuItem();
        closeFile = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        saveFile = new javax.swing.JMenuItem();
        saveFileAs = new javax.swing.JMenuItem();
        exportFile = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        exit = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undo = new javax.swing.JMenuItem();
        redo = new javax.swing.JMenuItem();
        restore = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        drawLattice = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        zoomIn = new javax.swing.JMenuItem();
        zoomOut = new javax.swing.JMenuItem();
        zoomNormal = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        latticeToWindow = new javax.swing.JMenuItem();
        resizeToWindow = new javax.swing.JMenuItem();
        windowToLAttice = new javax.swing.JMenuItem();
        selectionToWindow = new javax.swing.JCheckBoxMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        highlightMenu = new javax.swing.JMenu();
        logicalSearchMax = new javax.swing.JRadioButtonMenuItem();
        logicalSearchMin = new javax.swing.JRadioButtonMenuItem();
        logicalSearchHighest = new javax.swing.JRadioButtonMenuItem();
        logicalSearchLowest = new javax.swing.JRadioButtonMenuItem();
        logicalSearchBigger = new javax.swing.JRadioButtonMenuItem();
        logicalSearchSmaller = new javax.swing.JRadioButtonMenuItem();
        logicalSearchUperCone = new javax.swing.JRadioButtonMenuItem();
        logicalSearchLowerCone = new javax.swing.JRadioButtonMenuItem();
        logicalSearchSup = new javax.swing.JRadioButtonMenuItem();
        logicalSearchInf = new javax.swing.JRadioButtonMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        logicalSearchPath = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        logicalSearchMouseover = new javax.swing.JCheckBoxMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        Highlighted = new javax.swing.JMenu();
        highlightedSelect = new javax.swing.JMenuItem();
        highlightedAdd = new javax.swing.JMenuItem();
        highlightedRemove = new javax.swing.JMenuItem();
        highlightedHideResults = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        hideSelectedMenu = new javax.swing.JMenu();
        hideSelectedAll = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        hideSelectedNodes = new javax.swing.JMenuItem();
        hideSelectedTags = new javax.swing.JMenuItem();
        showHiddenMenu = new javax.swing.JMenu();
        showHiddenAll = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        showHiddenNodes = new javax.swing.JMenuItem();
        showHiddenTags = new javax.swing.JMenuItem();
        selectionMenu = new javax.swing.JMenu();
        selectionNodesMenu = new javax.swing.JMenu();
        cancelSelectionNodes = new javax.swing.JMenuItem();
        invertSelectionNodes = new javax.swing.JMenuItem();
        selectAllNodes = new javax.swing.JMenuItem();
        selectNodesRelated = new javax.swing.JMenu();
        selectNodesRelatedEdges = new javax.swing.JMenuItem();
        selectNodesRelatedTags = new javax.swing.JMenuItem();
        selectionEdgesMenu = new javax.swing.JMenu();
        cancelSelectionEdges = new javax.swing.JMenuItem();
        invertSelectionEdges = new javax.swing.JMenuItem();
        selectAllEdges = new javax.swing.JMenuItem();
        selectEdgesRelatedNodes = new javax.swing.JMenuItem();
        selectionTagsMenu = new javax.swing.JMenu();
        cancelSelectionTags = new javax.swing.JMenuItem();
        invertSelectionTags = new javax.swing.JMenuItem();
        selectAllTags = new javax.swing.JMenuItem();
        selectTagsRelatedNodes = new javax.swing.JMenuItem();
        selectionAllMenu = new javax.swing.JMenu();
        selectAll = new javax.swing.JMenuItem();
        cancelSelection = new javax.swing.JMenuItem();
        invertSelection = new javax.swing.JMenuItem();
        selectAllRelated = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        saveSelection = new javax.swing.JMenuItem();
        loadSelection = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        freeSelectionSettingsMenuItem = new javax.swing.JMenuItem();
        searchMenu = new javax.swing.JMenu();
        textSearch = new javax.swing.JMenu();
        textSearchNodes = new javax.swing.JMenuItem();
        textSearchTags = new javax.swing.JMenuItem();
        textSearchEdges = new javax.swing.JMenuItem();
        visualSearch = new javax.swing.JMenu();
        visualSearchNodes = new javax.swing.JMenuItem();
        visualSearchTags = new javax.swing.JMenuItem();
        visualSearchEdges = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        found = new javax.swing.JMenu();
        foundSelect = new javax.swing.JMenuItem();
        foundAdd = new javax.swing.JMenuItem();
        foundRemove = new javax.swing.JMenuItem();
        hideSearchResults = new javax.swing.JMenuItem();
        settingMenu = new javax.swing.JMenu();
        latticeSettingMenu = new javax.swing.JMenu();
        latticeVisualSetting = new javax.swing.JMenuItem();
        latticePropertiesSetting = new javax.swing.JMenuItem();
        nodesSettingMenu = new javax.swing.JMenu();
        nodesVisualSetting = new javax.swing.JMenuItem();
        nodesPropertiesSetting = new javax.swing.JMenuItem();
        nodesTagsSetting = new javax.swing.JMenuItem();
        tagsSettingMenu = new javax.swing.JMenu();
        tagsVisualSetting = new javax.swing.JMenuItem();
        tagsPropertiesSetting = new javax.swing.JMenuItem();
        edgesSettingMenu = new javax.swing.JMenu();
        edgesVisualSetting = new javax.swing.JMenuItem();
        edgesPropertiesSetting = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        guidelinesSetting = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpAbout = new javax.swing.JMenuItem();

        latticePopupVisualSetting.setText("Appearance...");
        latticePopupVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latticeVisualSettingActionPerformed(evt);
            }
        });
        latticePopupMenu.add(latticePopupVisualSetting);

        latticePopupPropertiesSetting.setText("Properties...");
        latticePopupPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latticePropertiesSettingActionPerformed(evt);
            }
        });
        latticePopupMenu.add(latticePopupPropertiesSetting);

        nodesPopupVisualSetting.setText("Appearance...");
        nodesPopupVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesVisualSettingActionPerformed(evt);
            }
        });
        nodePopupMenu.add(nodesPopupVisualSetting);

        nodesPopupPropertiesSetting.setText("Properties...");
        nodesPopupPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesPropertiesSettingActionPerformed(evt);
            }
        });
        nodePopupMenu.add(nodesPopupPropertiesSetting);

        nodesPopupTagsSetting.setText("Tags...");
        nodesPopupTagsSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesTagsSettingActionPerformed(evt);
            }
        });
        nodePopupMenu.add(nodesPopupTagsSetting);
        nodePopupMenu.add(jSeparator13);

        nodesPopupVisualSearch.setText("Visual search");
        nodesPopupVisualSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchNodesActionPerformed(evt);
            }
        });
        nodePopupMenu.add(nodesPopupVisualSearch);

        nodesPopupTextSearch.setText("Text search");
        nodesPopupTextSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchNodesActionPerformed(evt);
            }
        });
        nodePopupMenu.add(nodesPopupTextSearch);

        edgesPopupVisualSetting.setText("Appearance...");
        edgesPopupVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgesVisualSettingActionPerformed(evt);
            }
        });
        edgePopupMenu.add(edgesPopupVisualSetting);

        edgesPopupPropertiesSetting.setText("Properties...");
        edgesPopupPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgesPropertiesSettingActionPerformed(evt);
            }
        });
        edgePopupMenu.add(edgesPopupPropertiesSetting);
        edgePopupMenu.add(jSeparator14);

        edgePopupVisualSearch.setText("Visual search");
        edgePopupVisualSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchEdgesActionPerformed(evt);
            }
        });
        edgePopupMenu.add(edgePopupVisualSearch);

        edgePopupTextSearch.setText("Text search");
        edgePopupTextSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchEdgesActionPerformed(evt);
            }
        });
        edgePopupMenu.add(edgePopupTextSearch);

        tagsPopupVisualSetting.setText("Appearance...");
        tagsPopupVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsVisualSettingActionPerformed(evt);
            }
        });
        tagPopupMenu.add(tagsPopupVisualSetting);

        tagsPopupPropertiesSetting.setText("Properties...");
        tagsPopupPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsPropertiesSettingActionPerformed(evt);
            }
        });
        tagPopupMenu.add(tagsPopupPropertiesSetting);
        tagPopupMenu.add(jSeparator15);

        tagsPopupVisualSeach.setText("Visual search");
        tagsPopupVisualSeach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchTagsActionPerformed(evt);
            }
        });
        tagPopupMenu.add(tagsPopupVisualSeach);

        tagsPopupTextSeach.setText("Text search");
        tagsPopupTextSeach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchTagsActionPerformed(evt);
            }
        });
        tagPopupMenu.add(tagsPopupTextSeach);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(TITLE);
        setMinimumSize(new java.awt.Dimension(340, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        statusBar.setPreferredSize(new java.awt.Dimension(500, 15));

        fileMenu.setText("File");

        openFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openFile.setText("Open...");
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });
        fileMenu.add(openFile);

        closeFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        closeFile.setText("Close");
        closeFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFileActionPerformed(evt);
            }
        });
        fileMenu.add(closeFile);
        fileMenu.add(jSeparator2);

        saveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveFile.setText("Save");
        saveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileActionPerformed(evt);
            }
        });
        fileMenu.add(saveFile);

        saveFileAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveFileAs.setText("Save as...");
        saveFileAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileAsActionPerformed(evt);
            }
        });
        fileMenu.add(saveFileAs);

        exportFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportFile.setText("Export...");
        exportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileActionPerformed(evt);
            }
        });
        fileMenu.add(exportFile);
        fileMenu.add(jSeparator3);

        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        fileMenu.add(exit);

        mainMenu.add(fileMenu);

        editMenu.setText("Edit");
        editMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                editMenuMenuSelected(evt);
            }
        });

        undo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undo.setText("Undo");
        undo.setEnabled(false);
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        editMenu.add(undo);

        redo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redo.setText("Redo");
        redo.setEnabled(false);
        redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoActionPerformed(evt);
            }
        });
        editMenu.add(redo);

        restore.setText("Restore");
        restore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreActionPerformed(evt);
            }
        });
        editMenu.add(restore);
        editMenu.add(jSeparator17);

        drawLattice.setText("Draw lattice...");
        drawLattice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawLatticeActionPerformed(evt);
            }
        });
        editMenu.add(drawLattice);

        mainMenu.add(editMenu);

        viewMenu.setText("View");
        viewMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                viewMenuMenuSelected(evt);
            }
        });

        zoomIn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, java.awt.event.InputEvent.CTRL_MASK));
        zoomIn.setText("Zoom in");
        zoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInActionPerformed(evt);
            }
        });
        viewMenu.add(zoomIn);

        zoomOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        zoomOut.setText("Zoom out");
        zoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutActionPerformed(evt);
            }
        });
        viewMenu.add(zoomOut);

        zoomNormal.setText("Original size");
        zoomNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomNormalActionPerformed(evt);
            }
        });
        viewMenu.add(zoomNormal);
        viewMenu.add(jSeparator11);

        latticeToWindow.setText("Zoom lattice to window");
        latticeToWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latticeToWindowActionPerformed(evt);
            }
        });
        viewMenu.add(latticeToWindow);

        resizeToWindow.setText("Resize lattice to window");
        resizeToWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resizeToWindowActionPerformed(evt);
            }
        });
        viewMenu.add(resizeToWindow);

        windowToLAttice.setText("Resize window to lattice");
        windowToLAttice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                windowToLAtticeActionPerformed(evt);
            }
        });
        viewMenu.add(windowToLAttice);

        selectionToWindow.setText("Zoom selection to window");
        selectionToWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionToWindowActionPerformed(evt);
            }
        });
        viewMenu.add(selectionToWindow);
        viewMenu.add(jSeparator19);

        highlightMenu.setText("Highlight");

        logicalSearchMax.setText("Maximum");
        logicalSearchMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchMaxActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchMax);

        logicalSearchMin.setText("Minimum");
        logicalSearchMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchMinActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchMin);

        logicalSearchHighest.setText("Biggest");
        logicalSearchHighest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchHighestActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchHighest);

        logicalSearchLowest.setText("Smallest");
        logicalSearchLowest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchLowestActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchLowest);

        logicalSearchBigger.setText("Upper neighbors");
        logicalSearchBigger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchBiggerActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchBigger);

        logicalSearchSmaller.setText("Lower neighbors");
        logicalSearchSmaller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchSmallerActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchSmaller);

        logicalSearchUperCone.setText("Upper cone");
        logicalSearchUperCone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchUperConeActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchUperCone);

        logicalSearchLowerCone.setText("Lower cone");
        logicalSearchLowerCone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchLowerConeActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchLowerCone);

        logicalSearchSup.setText("Supremum");
        logicalSearchSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchSupActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchSup);

        logicalSearchInf.setText("Infimum");
        logicalSearchInf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchInfActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchInf);
        highlightMenu.add(jSeparator12);

        logicalSearchPath.setText("Paths...");
        logicalSearchPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchPathActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchPath);
        highlightMenu.add(jSeparator18);

        logicalSearchMouseover.setText("Highlight on mouseover");
        logicalSearchMouseover.setEnabled(false);
        logicalSearchMouseover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logicalSearchMouseoverActionPerformed(evt);
            }
        });
        highlightMenu.add(logicalSearchMouseover);
        highlightMenu.add(jSeparator16);

        Highlighted.setText("Apply");

        highlightedSelect.setText("Select");
        highlightedSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightedSelectActionPerformed(evt);
            }
        });
        Highlighted.add(highlightedSelect);

        highlightedAdd.setText("Add to selection");
        highlightedAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightedAddActionPerformed(evt);
            }
        });
        Highlighted.add(highlightedAdd);

        highlightedRemove.setText("Remove from selection");
        highlightedRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightedRemoveActionPerformed(evt);
            }
        });
        Highlighted.add(highlightedRemove);

        highlightedHideResults.setText("Remove highlight");
        highlightedHideResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightedHideResultsActionPerformed(evt);
            }
        });
        Highlighted.add(highlightedHideResults);

        highlightMenu.add(Highlighted);

        viewMenu.add(highlightMenu);
        viewMenu.add(jSeparator7);

        hideSelectedMenu.setText("Hide selected");

        hideSelectedAll.setText("All objects");
        hideSelectedAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSelectedAllActionPerformed(evt);
            }
        });
        hideSelectedMenu.add(hideSelectedAll);
        hideSelectedMenu.add(jSeparator5);

        hideSelectedNodes.setText("Nodes");
        hideSelectedNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSelectedNodesActionPerformed(evt);
            }
        });
        hideSelectedMenu.add(hideSelectedNodes);

        hideSelectedTags.setText("Tags");
        hideSelectedTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSelectedTagsActionPerformed(evt);
            }
        });
        hideSelectedMenu.add(hideSelectedTags);

        viewMenu.add(hideSelectedMenu);

        showHiddenMenu.setText("Show hidden");

        showHiddenAll.setText("All objects");
        showHiddenAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHiddenAllActionPerformed(evt);
            }
        });
        showHiddenMenu.add(showHiddenAll);
        showHiddenMenu.add(jSeparator6);

        showHiddenNodes.setText("Nodes");
        showHiddenNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHiddenNodesActionPerformed(evt);
            }
        });
        showHiddenMenu.add(showHiddenNodes);

        showHiddenTags.setText("Tags");
        showHiddenTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHiddenTagsActionPerformed(evt);
            }
        });
        showHiddenMenu.add(showHiddenTags);

        viewMenu.add(showHiddenMenu);

        mainMenu.add(viewMenu);

        selectionMenu.setText("Selection");

        selectionNodesMenu.setText("Nodes");

        cancelSelectionNodes.setText("Select none");
        cancelSelectionNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSelectionNodesActionPerformed(evt);
            }
        });
        selectionNodesMenu.add(cancelSelectionNodes);

        invertSelectionNodes.setText("Invert selection");
        invertSelectionNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertSelectionNodesActionPerformed(evt);
            }
        });
        selectionNodesMenu.add(invertSelectionNodes);

        selectAllNodes.setText("Select all");
        selectAllNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllNodesActionPerformed(evt);
            }
        });
        selectionNodesMenu.add(selectAllNodes);

        selectNodesRelated.setText("Select related");

        selectNodesRelatedEdges.setText("Edges between nodes");
        selectNodesRelatedEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNodesRelatedEdgesActionPerformed(evt);
            }
        });
        selectNodesRelated.add(selectNodesRelatedEdges);

        selectNodesRelatedTags.setText("Node's tags");
        selectNodesRelatedTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNodesRelatedTagsActionPerformed(evt);
            }
        });
        selectNodesRelated.add(selectNodesRelatedTags);

        selectionNodesMenu.add(selectNodesRelated);

        selectionMenu.add(selectionNodesMenu);

        selectionEdgesMenu.setText("Edges");

        cancelSelectionEdges.setText("Select none");
        cancelSelectionEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSelectionEdgesActionPerformed(evt);
            }
        });
        selectionEdgesMenu.add(cancelSelectionEdges);

        invertSelectionEdges.setText("Invert selection");
        invertSelectionEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertSelectionEdgesActionPerformed(evt);
            }
        });
        selectionEdgesMenu.add(invertSelectionEdges);

        selectAllEdges.setText("Select all");
        selectAllEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllEdgesActionPerformed(evt);
            }
        });
        selectionEdgesMenu.add(selectAllEdges);

        selectEdgesRelatedNodes.setText("Select related nodes");
        selectEdgesRelatedNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectEdgesRelatedNodesActionPerformed(evt);
            }
        });
        selectionEdgesMenu.add(selectEdgesRelatedNodes);

        selectionMenu.add(selectionEdgesMenu);

        selectionTagsMenu.setText("Tags");

        cancelSelectionTags.setText("Select none");
        cancelSelectionTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSelectionTagsActionPerformed(evt);
            }
        });
        selectionTagsMenu.add(cancelSelectionTags);

        invertSelectionTags.setText("Invert selection");
        invertSelectionTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertSelectionTagsActionPerformed(evt);
            }
        });
        selectionTagsMenu.add(invertSelectionTags);

        selectAllTags.setText("Select all");
        selectAllTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllTagsActionPerformed(evt);
            }
        });
        selectionTagsMenu.add(selectAllTags);

        selectTagsRelatedNodes.setText("Select related nodes");
        selectTagsRelatedNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectTagsRelatedNodesActionPerformed(evt);
            }
        });
        selectionTagsMenu.add(selectTagsRelatedNodes);

        selectionMenu.add(selectionTagsMenu);

        selectionAllMenu.setText("All objects");

        selectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAll.setText("Select all");
        selectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllActionPerformed(evt);
            }
        });
        selectionAllMenu.add(selectAll);

        cancelSelection.setText("Select none");
        cancelSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSelectionActionPerformed(evt);
            }
        });
        selectionAllMenu.add(cancelSelection);

        invertSelection.setText("Invert selection");
        invertSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertSelectionActionPerformed(evt);
            }
        });
        selectionAllMenu.add(invertSelection);

        selectAllRelated.setText("Select related");
        selectionAllMenu.add(selectAllRelated);

        selectionMenu.add(selectionAllMenu);
        selectionMenu.add(jSeparator8);

        saveSelection.setText("Save selection...");
        saveSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSelectionActionPerformed(evt);
            }
        });
        selectionMenu.add(saveSelection);

        loadSelection.setText("Load selection...");
        loadSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSelectionActionPerformed(evt);
            }
        });
        selectionMenu.add(loadSelection);
        selectionMenu.add(jSeparator10);

        freeSelectionSettingsMenuItem.setText("Free selection...");
        freeSelectionSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freeSelectionSettingsMenuItemActionPerformed(evt);
            }
        });
        selectionMenu.add(freeSelectionSettingsMenuItem);

        mainMenu.add(selectionMenu);

        searchMenu.setText("Search");

        textSearch.setText("Text");

        textSearchNodes.setText("Nodes...");
        textSearchNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchNodesActionPerformed(evt);
            }
        });
        textSearch.add(textSearchNodes);

        textSearchTags.setText("Tags...");
        textSearchTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchTagsActionPerformed(evt);
            }
        });
        textSearch.add(textSearchTags);

        textSearchEdges.setText("Edges...");
        textSearchEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSearchEdgesActionPerformed(evt);
            }
        });
        textSearch.add(textSearchEdges);

        searchMenu.add(textSearch);

        visualSearch.setText("Visual");

        visualSearchNodes.setText("Nodes...");
        visualSearchNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchNodesActionPerformed(evt);
            }
        });
        visualSearch.add(visualSearchNodes);

        visualSearchTags.setText("Tags...");
        visualSearchTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchTagsActionPerformed(evt);
            }
        });
        visualSearch.add(visualSearchTags);

        visualSearchEdges.setText("Edges...");
        visualSearchEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualSearchEdgesActionPerformed(evt);
            }
        });
        visualSearch.add(visualSearchEdges);

        searchMenu.add(visualSearch);
        searchMenu.add(jSeparator1);

        found.setText("Found");

        foundSelect.setText("Select");
        foundSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foundSelectActionPerformed(evt);
            }
        });
        found.add(foundSelect);

        foundAdd.setText("Add to selection");
        foundAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foundAddActionPerformed(evt);
            }
        });
        found.add(foundAdd);

        foundRemove.setText("Remove from selection");
        foundRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foundRemoveActionPerformed(evt);
            }
        });
        found.add(foundRemove);

        hideSearchResults.setText("Remove highlight");
        hideSearchResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSearchResultsActionPerformed(evt);
            }
        });
        found.add(hideSearchResults);

        searchMenu.add(found);

        mainMenu.add(searchMenu);

        settingMenu.setText("Settings");

        latticeSettingMenu.setText("Lattice");

        latticeVisualSetting.setText("Appearance...");
        latticeVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latticeVisualSettingActionPerformed(evt);
            }
        });
        latticeSettingMenu.add(latticeVisualSetting);

        latticePropertiesSetting.setText("Properties...");
        latticePropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latticePropertiesSettingActionPerformed(evt);
            }
        });
        latticeSettingMenu.add(latticePropertiesSetting);

        settingMenu.add(latticeSettingMenu);

        nodesSettingMenu.setText("Nodes");

        nodesVisualSetting.setText("Appearance...");
        nodesVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesVisualSettingActionPerformed(evt);
            }
        });
        nodesSettingMenu.add(nodesVisualSetting);

        nodesPropertiesSetting.setText("Properties...");
        nodesPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesPropertiesSettingActionPerformed(evt);
            }
        });
        nodesSettingMenu.add(nodesPropertiesSetting);

        nodesTagsSetting.setText("Tags...");
        nodesTagsSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesTagsSettingActionPerformed(evt);
            }
        });
        nodesSettingMenu.add(nodesTagsSetting);

        settingMenu.add(nodesSettingMenu);

        tagsSettingMenu.setText("Tags");

        tagsVisualSetting.setText("Appearance...");
        tagsVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsVisualSettingActionPerformed(evt);
            }
        });
        tagsSettingMenu.add(tagsVisualSetting);

        tagsPropertiesSetting.setText("Properties...");
        tagsPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsPropertiesSettingActionPerformed(evt);
            }
        });
        tagsSettingMenu.add(tagsPropertiesSetting);

        settingMenu.add(tagsSettingMenu);

        edgesSettingMenu.setText("Edges");

        edgesVisualSetting.setText("Appearance...");
        edgesVisualSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgesVisualSettingActionPerformed(evt);
            }
        });
        edgesSettingMenu.add(edgesVisualSetting);

        edgesPropertiesSetting.setText("Properties...");
        edgesPropertiesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgesPropertiesSettingActionPerformed(evt);
            }
        });
        edgesSettingMenu.add(edgesPropertiesSetting);

        settingMenu.add(edgesSettingMenu);
        settingMenu.add(jSeparator4);

        guidelinesSetting.setText("Guide lines...");
        guidelinesSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guidelinesSettingActionPerformed(evt);
            }
        });
        settingMenu.add(guidelinesSetting);

        mainMenu.add(settingMenu);

        helpMenu.setText("Help");

        helpAbout.setText("About...");
        helpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpAboutActionPerformed(evt);
            }
        });
        helpMenu.add(helpAbout);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        if(latticeShape != null){
            latticeShape.getHistory().backward();
            undo.setEnabled(latticeShape.getHistory().canBackward());
            redo.setEnabled(latticeShape.getHistory().canForward());
            repaint();
        }
    }//GEN-LAST:event_undoActionPerformed

    private void redoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoActionPerformed
        if(latticeShape != null){
            latticeShape.getHistory().forward();
            undo.setEnabled(latticeShape.getHistory().canBackward());
            redo.setEnabled(latticeShape.getHistory().canForward());
            repaint();
        }
    }//GEN-LAST:event_redoActionPerformed

    private void restoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreActionPerformed
        if(latticeShape != null){
            latticeShape.getHistory().toBegining();
            undo.setEnabled(false);
            redo.setEnabled(latticeShape.getHistory().canForward());
            repaint();
        }
    }//GEN-LAST:event_restoreActionPerformed

    private void selectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllActionPerformed
        latticeShape.getSelector().selectAll();
    }//GEN-LAST:event_selectAllActionPerformed

    private void cancelSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSelectionActionPerformed
        latticeShape.getSelector().unselectAll();
    }//GEN-LAST:event_cancelSelectionActionPerformed

    private void invertSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionActionPerformed
        latticeShape.getSelector().invertSelection();
    }//GEN-LAST:event_invertSelectionActionPerformed

    private void hideSelectedNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSelectedNodesActionPerformed
        latticeShape.getShowHide().hideSelectedNodes();
    }//GEN-LAST:event_hideSelectedNodesActionPerformed

    private void showHiddenNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHiddenNodesActionPerformed
        latticeShape.getShowHide().showSelectHiddenNodes();
    }//GEN-LAST:event_showHiddenNodesActionPerformed

    private void saveSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSelectionActionPerformed
        if(saveSelectionDialog != null){
            saveSelectionDialog.setVisible(true);
        }
    }//GEN-LAST:event_saveSelectionActionPerformed

    private void loadSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadSelectionActionPerformed
        if(loadSelectionDialog != null){
            loadSelectionDialog.setVisible(true);
        }
    }//GEN-LAST:event_loadSelectionActionPerformed

    private void latticeVisualSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latticeVisualSettingActionPerformed
        if(latticeSettings != null){
            latticeSettings.setVisible(true);
        }
    }//GEN-LAST:event_latticeVisualSettingActionPerformed

    private void nodesVisualSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodesVisualSettingActionPerformed
        if(evt.getSource() == nodesPopupVisualSetting && popUpActionItem != null){
            nodeSetting.getModel().setNodes((NodeShape)popUpActionItem);
        } else {
            nodeSetting.getModel().setNodes(latticeShape.getSelectedNodes());
        }
        nodeSetting.setVisible(true);
        repaint();
    }//GEN-LAST:event_nodesVisualSettingActionPerformed

    private void nodesPropertiesSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodesPropertiesSettingActionPerformed
        Node node = null;
        if(evt.getSource() == nodesPopupPropertiesSetting && popUpActionItem != null){
            node = ((NodeShape)popUpActionItem).getLogicalNode();
        } else {
            List<NodeShape> selectedNodes = latticeShape.getSelectedNodes();
            if(selectedNodes.size() > 0){
                node = selectedNodes.get(0).getLogicalNode();
            }
            Object source = evt.getSource();
            if( node == null && (source.getClass() == NodeShape.class || source instanceof NodeNameShape)){
                node = source.getClass() == NodeShape.class ? ((NodeShape)source).getLogicalNode() : ((NodeNameShape) source).getNode().getLogicalNode();
            }
        }
        if(node != null){
            nodeTextSetting.getModel().setNode(node);
            nodeTextSetting.setVisible(true);
            latticeShape.repaint();
        } else {
            GuiUtils.showSetNoSel(this, "node");
        }
    }//GEN-LAST:event_nodesPropertiesSettingActionPerformed

    private void nodesTagsSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodesTagsSettingActionPerformed
        List<Node> selectedNodes = new ArrayList<Node>();
        if(evt.getSource() == nodesPopupTagsSetting && popUpActionItem != null){
            selectedNodes.add(((NodeShape)popUpActionItem).getLogicalNode());
        } else {
            for(NodeShape node : latticeShape.getSelectedNodes()){
                selectedNodes.add(node.getLogicalNode());
            }
        }
        if(selectedNodes.size() > 0){
            nodeTags.getModel().setNodes(selectedNodes);
            nodeTags.setVisible(true);
            latticeShape.repaint();
        } else {
            GuiUtils.showSetNoSel(this, "node");
        }
    }//GEN-LAST:event_nodesTagsSettingActionPerformed

    private void tagsVisualSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagsVisualSettingActionPerformed
        if(evt.getSource() == tagsPopupVisualSetting && popUpActionItem != null){
            tagSettings.getModel().setTags((TagShape)popUpActionItem);
        } else {
            tagSettings.getModel().setTags(latticeShape.getSelectedTags());
        }
        tagSettings.setVisible(true);
    }//GEN-LAST:event_tagsVisualSettingActionPerformed

    private void tagsPropertiesSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagsPropertiesSettingActionPerformed
        Tag tag = null;
        if(evt.getSource() == tagsPopupPropertiesSetting && popUpActionItem != null){
            tag = ((TagShape)popUpActionItem).getLogicalTag();
        } else {
            List<TagShape> selectedTags = latticeShape.getSelectedTags();
            if(selectedTags.size() > 0){
                tag = selectedTags.get(0).getLogicalTag();
            }
            Object source = evt.getSource();
            if(tag == null && source.getClass() == TagShape.class){
                tag = ((TagShape)source).getLogicalTag();
            }
        }
        if(tag != null){
            tagTextSetting.getModel().setTag(tag);
            tagTextSetting.setVisible(true);
            latticeShape.repaint();
        } else {
            GuiUtils.showSetNoSel(this, "tag");
        }
    }//GEN-LAST:event_tagsPropertiesSettingActionPerformed

    private void edgesVisualSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edgesVisualSettingActionPerformed
        if(evt.getSource() == edgesPopupVisualSetting && popUpActionItem != null){
            edgeSettings.getModel().setEdges((EdgeShape)popUpActionItem);
        } else {
            edgeSettings.getModel().setEdges(latticeShape.getSelectedEdges());
        }
        edgeSettings.setVisible(true);
    }//GEN-LAST:event_edgesVisualSettingActionPerformed

    private void edgesPropertiesSettingActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        OrderPair pair = null;
        if(evt.getSource() == edgesPopupPropertiesSetting && popUpActionItem != null){
            pair = ((EdgeShape)popUpActionItem).getOrderPair();
        } else {
            List<EdgeShape> selectedEdges = latticeShape.getSelectedEdges();
            if(selectedEdges.size() > 0){
                pair = selectedEdges.get(0).getOrderPair();
            }
            Object source = evt.getSource();
            if(pair == null && source.getClass() == EdgeShape.class){
                pair = ((EdgeShape)source).getOrderPair();
            }
        }
        if(pair != null){
            edgeTextSetting.getModel().setEdge(pair);
            edgeTextSetting.setVisible(true);
            latticeShape.repaint();
        } else {
            GuiUtils.showSetNoSel(this, "edge");
        }
    }
    
    private boolean saveFileAs(){
       if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try{
                File file = fileChooser.getSelectedFile();
                boolean save = true;
                if(file.exists()){
                    int option = JOptionPane.showConfirmDialog(this, "The file " + file.getAbsolutePath() + " already exists. Would you like to replace it?", "Save as", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(option == JOptionPane.CANCEL_OPTION){
                        return true;
                    } else {
                        save = option == JOptionPane.OK_OPTION;
                    }
                }
                if(save){
                    XmlExport.save(lattice, file);
                    return true;
                } else {
                    return saveFileAs();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lattice wasn't saved.\nTry to check directory rights.", "Error when saving lattice", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
    
    private boolean doSaveFile(File file){
        try {
            XmlExport.save(lattice, file);
            latticeFile = file;
            lastSaveHistoryHash = latticeShape.getHistory().getActualPositionHash();
            setTitle(TITLE + " - " + latticeFile.getName());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lattice wasn't saved.\nTry to check directory rights.", "Error when saving lattice", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lattice wasn't saved.\nInternal error.", "Error when saving lattice", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private boolean saveFile(){
        if(latticeFile == null || latticeFile.getName().endsWith(".lat")){
            boolean saved = false;
            if(latticeFile.getName().endsWith(".lat")){
                String rawName = latticeFile.getName().replace(".lat", "");
                int result = JOptionPane.showConfirmDialog(this, "Lattice can't be saved as LatVis (.lat) file. Save as JLatVis (.jlat) file \""+rawName+".jlat\".", "Save as JLatVis (.jlat) file?", JOptionPane.YES_NO_OPTION);
                if( result == JOptionPane.YES_OPTION){
                    return doSaveFile( new File( latticeFile.getPath().substring(0, latticeFile.getPath().lastIndexOf(".lat")) + ".jlat" ) );
                } else {
                    saved = saveFileAs();
                }
            } else {
                saved = saveFileAs();
            }
            if(saved){
                lastSaveHistoryHash = latticeShape.getHistory().getActualPositionHash();
                setTitle(TITLE + " - " + ( latticeFile != null ? latticeFile.getName() : ""));
                return true;
            }
        } else {
            return doSaveFile(latticeFile);
        }
        return false;
    }

    private void saveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileActionPerformed
        saveFile();
    }//GEN-LAST:event_saveFileActionPerformed

    private void latticePropertiesSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latticePropertiesSettingActionPerformed
        latticeTextSetting.getModel().setLattice(lattice);
        latticeTextSetting.setVisible(true);
        latticeShape.repaint();
    }//GEN-LAST:event_latticePropertiesSettingActionPerformed

    private void saveFileAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileAsActionPerformed
        saveFileAs();
    }//GEN-LAST:event_saveFileAsActionPerformed

    private void textSearchNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textSearchNodesActionPerformed
        if(searchTextNodes != null){
            searchTextNodes.setVisible(true);
        }
    }//GEN-LAST:event_textSearchNodesActionPerformed

    private void textSearchTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textSearchTagsActionPerformed
        if(searchTextTags != null){
            searchTextTags.setVisible(true);
        }
    }//GEN-LAST:event_textSearchTagsActionPerformed

    private void visualSearchNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualSearchNodesActionPerformed
        if(searchVisualNode != null){
            if(evt.getSource() == nodesPopupVisualSearch && popUpActionItem != null){
                searchVisualNode.getModel().setNodes((NodeShape)popUpActionItem);
            } else {
                searchVisualNode.getModel().setNodes(latticeShape.getSelectedNodes());
            }
            searchVisualNode.setVisible(true);
        }
    }//GEN-LAST:event_visualSearchNodesActionPerformed

    private void visualSearchTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualSearchTagsActionPerformed
        if(searchVisualTag != null){
            if(evt.getSource() == tagsPopupVisualSeach && popUpActionItem != null){
                searchVisualTag.getModel().setTags((TagShape)popUpActionItem);
            } else {
                searchVisualTag.getModel().setTags(latticeShape.getSelectedTags());
            }
            searchVisualTag.setVisible(true);
        }
    }//GEN-LAST:event_visualSearchTagsActionPerformed

    private void visualSearchEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualSearchEdgesActionPerformed
        if(searchVisualEdge != null){
            if(evt.getSource() == edgePopupVisualSearch && popUpActionItem != null){
                searchVisualEdge.getModel().setEdges((EdgeShape)popUpActionItem);
            } else {
                searchVisualEdge.getModel().setEdges(latticeShape.getSelectedEdges());
            }
            searchVisualEdge.setVisible(true);
        }
    }//GEN-LAST:event_visualSearchEdgesActionPerformed

    private void logicalSearchPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchPathActionPerformed
        latticeShape.getLogicalSearch().paths();
        repaint();
    }//GEN-LAST:event_logicalSearchPathActionPerformed

    private void hideSearchResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSearchResultsActionPerformed
        latticeShape.getSearchResults().reset();
        repaint();
    }//GEN-LAST:event_hideSearchResultsActionPerformed

    private void zoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInActionPerformed
        if(latticeShape != null){
            latticeShape.getZoom().increaseByDefaultStep();
            repaint();
        }
    }//GEN-LAST:event_zoomInActionPerformed

    private void zoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutActionPerformed
        if(latticeShape != null){
            latticeShape.getZoom().decreaseByDefaultStep();
            repaint();
        }
    }//GEN-LAST:event_zoomOutActionPerformed

    private void zoomNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomNormalActionPerformed
        if(latticeShape != null){
            latticeShape.getZoom().setZoom( 1 );
            repaint();
        }
    }//GEN-LAST:event_zoomNormalActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if(lattice != null){
                class MyThread extends Thread{
                    public MyThread(){
                        setDaemon(true);
                    }
                    @Override
                    public void run() {
                        Application.main(new String[]{ Application.FILE_ARG + fileChooser.getSelectedFile().getAbsolutePath(), Application.CLOSE_IF_WRONG_FILE + "1" });
                    }
                }
                MyThread t = new MyThread();
                t.setDaemon(true);
                t.start();
            } else {
                application.loadFile(fileChooser.getSelectedFile());
                latticeFile = fileChooser.getSelectedFile();
            }
        }
    }//GEN-LAST:event_openFileActionPerformed

    private void exportFile(){
        if (exportFileChooser.showDialog(this) == JFileChooser.APPROVE_OPTION) {
            try{
                File file = exportFileChooser.getSelectedFile();
                
                boolean save = true;
                if(file.exists()){
                    int option = JOptionPane.showConfirmDialog(this, "The file " + file.getAbsolutePath() + " already exists. Would you like to replace it?", "Save as", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(option == JOptionPane.CANCEL_OPTION){
                        return;
                    } else {
                        save = option == JOptionPane.OK_OPTION;
                    }
                }
                if(save){
                    ImageExport.export(latticeShape, exportFileChooser.getFileType(), file);
                    return;
                } else {
                    exportFile();
                }
            } catch(IOException e){
                JOptionPane.showMessageDialog(this, "Error occured when saving lattice export.\nCheck if selected directory exists and is writable", "Export error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            } catch(com.itextpdf.text.DocumentException e){
                JOptionPane.showMessageDialog(this, "Error occured when saving lattice export.\nCheck if selected directory exists and is writable", "Export error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            } catch(VectorBuilderException e){
                JOptionPane.showMessageDialog(this, "Error occured when saving lattice export.\nCheck if selected directory exists and is writable", "Export error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            }
        }        
    }
    
    private void exportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileActionPerformed
        exportFile();
    }//GEN-LAST:event_exportFileActionPerformed

    private void closeFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFileActionPerformed
        lattice = null;
        insertLatticeShape(null);
        setTitle(TITLE);
        repaint();
    }//GEN-LAST:event_closeFileActionPerformed

    private void foundSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foundSelectActionPerformed
        latticeShape.getSearchResults().setAsSelection();
        repaint();
    }//GEN-LAST:event_foundSelectActionPerformed
    private void foundAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foundAddActionPerformed
        latticeShape.getSearchResults().addToSelection();
        repaint();
    }//GEN-LAST:event_foundAddActionPerformed
    private void foundRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foundRemoveActionPerformed
        latticeShape.getSearchResults().removeFromSelection();
        repaint();
    }//GEN-LAST:event_foundRemoveActionPerformed

    private void helpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpAboutActionPerformed
        helpAboutWindow.setVisible(true);
    }//GEN-LAST:event_helpAboutActionPerformed

    private void cancelSelectionTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSelectionTagsActionPerformed
       latticeShape.getSelector().unselectAllTags();
    }//GEN-LAST:event_cancelSelectionTagsActionPerformed
    private void invertSelectionTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionTagsActionPerformed
       latticeShape.getSelector().invertSelectionTags();
    }//GEN-LAST:event_invertSelectionTagsActionPerformed
    private void selectAllTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllTagsActionPerformed
        latticeShape.getSelector().selectAllTags();
    }//GEN-LAST:event_selectAllTagsActionPerformed

    private void cancelSelectionEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSelectionEdgesActionPerformed
        latticeShape.getSelector().unselectAllEdges();
    }//GEN-LAST:event_cancelSelectionEdgesActionPerformed
    private void invertSelectionEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionEdgesActionPerformed
        latticeShape.getSelector().invertSelectionEdges();
    }//GEN-LAST:event_invertSelectionEdgesActionPerformed
    private void selectAllEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllEdgesActionPerformed
        latticeShape.getSelector().selectAllEdges();
    }//GEN-LAST:event_selectAllEdgesActionPerformed

    private void cancelSelectionNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSelectionNodesActionPerformed
        latticeShape.getSelector().unselectAllNodes();
    }//GEN-LAST:event_cancelSelectionNodesActionPerformed
    private void invertSelectionNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionNodesActionPerformed
        latticeShape.getSelector().invertSelectionNodes();
    }//GEN-LAST:event_invertSelectionNodesActionPerformed
    private void selectAllNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllNodesActionPerformed
        latticeShape.getSelector().selectAllNodes();
    }//GEN-LAST:event_selectAllNodesActionPerformed

    private void guidelinesSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guidelinesSettingActionPerformed
        guidelinesSettings.getModel().setGuidelines(latticeShape.getGuidelines());
        guidelinesSettings.setVisible(true);
        repaint();
}//GEN-LAST:event_guidelinesSettingActionPerformed

    private void hideSelectedAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSelectedAllActionPerformed
        latticeShape.getShowHide().hideSelectedAll();
}//GEN-LAST:event_hideSelectedAllActionPerformed

    private void hideSelectedTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSelectedTagsActionPerformed
        latticeShape.getShowHide().hideSelectedTags();
    }//GEN-LAST:event_hideSelectedTagsActionPerformed

    private void showHiddenAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHiddenAllActionPerformed
        latticeShape.getShowHide().showSelectHiddenAll();
    }//GEN-LAST:event_showHiddenAllActionPerformed

    private void showHiddenTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHiddenTagsActionPerformed
        latticeShape.getShowHide().showSelectHiddenTags();
    }//GEN-LAST:event_showHiddenTagsActionPerformed

    private void freeSelectionSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freeSelectionSettingsMenuItemActionPerformed
        selectionSetting.setVisible(true);
    }//GEN-LAST:event_freeSelectionSettingsMenuItemActionPerformed

    private void viewMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_viewMenuMenuSelected
        if(latticeShape == null){
            return;
        }
        Selector selector = latticeShape.getSelector();
        hideSelectedNodes.setEnabled(selector.getSelectedNodes().size() > 0);
        hideSelectedTags.setEnabled(selector.getSelectedTags().size() > 0);
        hideSelectedAll.setEnabled(hideSelectedNodes.isEnabled() || hideSelectedTags.isEnabled());

        showHiddenNodes.setEnabled(latticeShape.getHiddenNodes().size() > 0);
        showHiddenTags.setEnabled(latticeShape.getHiddenTags().size() > 0);
        showHiddenAll.setEnabled(showHiddenNodes.isEnabled() || showHiddenTags.isEnabled());
    }//GEN-LAST:event_viewMenuMenuSelected

    private void editMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_editMenuMenuSelected
        undo.setEnabled(latticeShape != null && latticeShape.getHistory().canBackward());
        redo.setEnabled(latticeShape != null && latticeShape.getHistory().canForward());
        restore.setEnabled(latticeShape != null && latticeShape.getHistory().canBackward());
    }//GEN-LAST:event_editMenuMenuSelected

    private void latticeToWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latticeToWindowActionPerformed
        latticeShape.moveTo(new Point2D(DEFAULT_PADDING, DEFAULT_PADDING));
        latticeShape.getZoom().zoomToWindow(latticeShape, jScrollPane1, DEFAULT_PADDING);
        lattice.getShape().recountPreferredSize();
        latticeShape.repaint();
    }//GEN-LAST:event_latticeToWindowActionPerformed

    private void windowToLAtticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_windowToLAtticeActionPerformed
        resizeByLattice();
    }//GEN-LAST:event_windowToLAtticeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(latticeShape != null && lastSaveHistoryHash != latticeShape.getHistory().getActualPositionHash()){
            Object[] options = {"Save",
                "Discard",
                "Cancel"};
            int response = JOptionPane.showOptionDialog(this, "File is modified. Save it?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if(response == 0){
                if(saveFileAs()){
                    dispose();
                }
            } else if(response == 1){
                dispose();
            }
        } else {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing


    private void unselectOtherSearchRadioButtons(javax.swing.JRadioButtonMenuItem activeItem){
        for(int i=0; i < highlightMenu.getItemCount(); ++i){
            javax.swing.JMenuItem item = highlightMenu.getItem(i);
            if(item instanceof javax.swing.JRadioButtonMenuItem && item != activeItem){
                ((javax.swing.JRadioButtonMenuItem)item).setSelected(false);
            }
        }
        logicalSearchMouseover.setEnabled( logicalSearchBigger.isSelected() || logicalSearchSmaller.isSelected() || logicalSearchLowerCone.isSelected() || logicalSearchUperCone.isSelected() );
        if( ! logicalSearchMouseover.isEnabled()){
            logicalSearchMouseover.setSelected(false);
        }
    }

    private void logicalSearchMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchMaxActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchMax);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchMax.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Maximum);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchMaxActionPerformed

    private void logicalSearchMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchMinActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchMin);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchMin.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Minimum);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchMinActionPerformed

    private void logicalSearchHighestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchHighestActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchHighest);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchHighest.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Highest);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchHighestActionPerformed

    private void logicalSearchLowestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchLowestActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchLowest);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchLowest.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Lowest);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchLowestActionPerformed

    private void logicalSearchBiggerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchBiggerActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchBigger);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchBigger.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.BiggerNeighbours);
            if(logicalSearchMouseover.isSelected()){
                latticeShape.getOnlineHighlight().setActiveHighlight( OnlineHighlight.NEIGHBORS_UPPER );
            }
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchBiggerActionPerformed

    private void logicalSearchSmallerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchSmallerActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchSmaller);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchSmaller.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.SmallerNeighbours);
            if(logicalSearchMouseover.isSelected()){
                latticeShape.getOnlineHighlight().setActiveHighlight( OnlineHighlight.NEIGHBORS_LOWER );
            }
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchSmallerActionPerformed

    private void logicalSearchUperConeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchUperConeActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchUperCone);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchUperCone.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.UpperCone);
            if(logicalSearchMouseover.isSelected()){
                latticeShape.getOnlineHighlight().setActiveHighlight( OnlineHighlight.CONE_UPPER );
            }
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchUperConeActionPerformed

    private void logicalSearchLowerConeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchLowerConeActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchLowerCone);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchLowerCone.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.LowerCone);
            if(logicalSearchMouseover.isSelected()){
                latticeShape.getOnlineHighlight().setActiveHighlight( OnlineHighlight.CONE_LOWER );
            }
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchLowerConeActionPerformed

    private void logicalSearchSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchSupActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchSup);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchSup.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Supremum);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchSupActionPerformed

    private void logicalSearchInfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchInfActionPerformed
        unselectOtherSearchRadioButtons(logicalSearchInf);
        if(latticeShape == null){
            return;
        }
        if(logicalSearchInf.isSelected()){
            latticeShape.getLogicalSearch().setActiveSearch(OnFlySearch.Infimum);
        } else {
            latticeShape.getLogicalSearch().reset();
        }
        repaint();
    }//GEN-LAST:event_logicalSearchInfActionPerformed

    private void highlightedSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightedSelectActionPerformed
        if(latticeShape == null){
            return;
        }
        latticeShape.getHighlightResults().setAsSelection();
        repaint();
    }//GEN-LAST:event_highlightedSelectActionPerformed

    private void highlightedAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightedAddActionPerformed
        if(latticeShape == null){
            return;
        }
        latticeShape.getHighlightResults().addToSelection();
        repaint();
    }//GEN-LAST:event_highlightedAddActionPerformed

    private void highlightedRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightedRemoveActionPerformed
        if(latticeShape == null){
            return;
        }
        latticeShape.getHighlightResults().removeFromSelection();
        repaint();
    }//GEN-LAST:event_highlightedRemoveActionPerformed

    private void highlightedHideResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightedHideResultsActionPerformed
        if(latticeShape == null){
            return;
        }
        latticeShape.getHighlightResults().reset();
        repaint();
    }//GEN-LAST:event_highlightedHideResultsActionPerformed

    private void selectionToWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionToWindowActionPerformed
        if(selectionToWindow.isSelected()){
            lattice.getShape().showDraggZoom();
        } else {
            lattice.getShape().hideDragZoom();
        }
    }//GEN-LAST:event_selectionToWindowActionPerformed

    private void drawLatticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawLatticeActionPerformed
        drawForm.setVisible(true);
    }//GEN-LAST:event_drawLatticeActionPerformed

    private void logicalSearchMouseoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logicalSearchMouseoverActionPerformed
        if(latticeShape == null){
            return;
        }
        OnlineHighlight highlight = latticeShape.getOnlineHighlight();
        if(logicalSearchMouseover.isSelected()){
            if(logicalSearchBigger.isSelected()){
                highlight.setActiveHighlight(OnlineHighlight.NEIGHBORS_UPPER);
            } else if(logicalSearchSmaller.isSelected()){
                highlight.setActiveHighlight(OnlineHighlight.NEIGHBORS_LOWER);
            } else if(logicalSearchUperCone.isSelected()){
                highlight.setActiveHighlight(OnlineHighlight.CONE_UPPER);
            } else if(logicalSearchLowerCone.isSelected()){
                highlight.setActiveHighlight(OnlineHighlight.CONE_LOWER);
            }
        } else {
            highlight.setActiveHighlight(null);
        }
    }//GEN-LAST:event_logicalSearchMouseoverActionPerformed

    private void resizeToWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resizeToWindowActionPerformed
        latticeShape.moveTo(new Point2D(DEFAULT_PADDING, DEFAULT_PADDING));
        latticeShape.getZoom().rezizeToWindow(latticeShape, jScrollPane1, DEFAULT_PADDING);
        lattice.getShape().recountPreferredSize();
        latticeShape.repaint();
    }//GEN-LAST:event_resizeToWindowActionPerformed

    private void selectNodesRelatedEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNodesRelatedEdgesActionPerformed
        latticeShape.getSelector().addEdgesBetweenNodes();
    }//GEN-LAST:event_selectNodesRelatedEdgesActionPerformed

    private void selectNodesRelatedTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNodesRelatedTagsActionPerformed
        latticeShape.getSelector().addNodesTags();
    }//GEN-LAST:event_selectNodesRelatedTagsActionPerformed

    private void selectEdgesRelatedNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectEdgesRelatedNodesActionPerformed
        latticeShape.getSelector().addEdgesNodes();
    }//GEN-LAST:event_selectEdgesRelatedNodesActionPerformed

    private void selectTagsRelatedNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectTagsRelatedNodesActionPerformed
        latticeShape.getSelector().addTagsNodes();
    }//GEN-LAST:event_selectTagsRelatedNodesActionPerformed

    private void textSearchEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textSearchEdgesActionPerformed
        if(searchTextEdges != null){
            searchTextEdges.setVisible(true);
        }
    }//GEN-LAST:event_textSearchEdgesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenu Highlighted;
    public javax.swing.JMenuItem cancelSelection;
    public javax.swing.JMenuItem cancelSelectionEdges;
    public javax.swing.JMenuItem cancelSelectionNodes;
    public javax.swing.JMenuItem cancelSelectionTags;
    public javax.swing.JMenuItem closeFile;
    public javax.swing.JMenuItem drawLattice;
    public javax.swing.JPopupMenu edgePopupMenu;
    public javax.swing.JMenuItem edgePopupTextSearch;
    public javax.swing.JMenuItem edgePopupVisualSearch;
    public javax.swing.JMenuItem edgesPopupPropertiesSetting;
    public javax.swing.JMenuItem edgesPopupVisualSetting;
    public javax.swing.JMenuItem edgesPropertiesSetting;
    public javax.swing.JMenu edgesSettingMenu;
    public javax.swing.JMenuItem edgesVisualSetting;
    public javax.swing.JMenu editMenu;
    public javax.swing.JMenuItem exit;
    public javax.swing.JMenuItem exportFile;
    public kandrm.JLatVis.gui.ExportFileChooser exportFileChooser;
    public kandrm.JLatVis.gui.LoadFileChooser fileChooser;
    public javax.swing.JMenu fileMenu;
    public javax.swing.JMenu found;
    public javax.swing.JMenuItem foundAdd;
    public javax.swing.JMenuItem foundRemove;
    public javax.swing.JMenuItem foundSelect;
    public javax.swing.JMenuItem freeSelectionSettingsMenuItem;
    public javax.swing.JMenuItem guidelinesSetting;
    public javax.swing.JMenuItem helpAbout;
    public javax.swing.JMenu helpMenu;
    public javax.swing.JMenuItem hideSearchResults;
    public javax.swing.JMenuItem hideSelectedAll;
    public javax.swing.JMenu hideSelectedMenu;
    public javax.swing.JMenuItem hideSelectedNodes;
    public javax.swing.JMenuItem hideSelectedTags;
    public javax.swing.JMenu highlightMenu;
    public javax.swing.JMenuItem highlightedAdd;
    public javax.swing.JMenuItem highlightedHideResults;
    public javax.swing.JMenuItem highlightedRemove;
    public javax.swing.JMenuItem highlightedSelect;
    public javax.swing.JMenuItem invertSelection;
    public javax.swing.JMenuItem invertSelectionEdges;
    public javax.swing.JMenuItem invertSelectionNodes;
    public javax.swing.JMenuItem invertSelectionTags;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JPopupMenu.Separator jSeparator1;
    public javax.swing.JPopupMenu.Separator jSeparator10;
    public javax.swing.JPopupMenu.Separator jSeparator11;
    public javax.swing.JPopupMenu.Separator jSeparator12;
    public javax.swing.JPopupMenu.Separator jSeparator13;
    public javax.swing.JPopupMenu.Separator jSeparator14;
    public javax.swing.JPopupMenu.Separator jSeparator15;
    public javax.swing.JPopupMenu.Separator jSeparator16;
    public javax.swing.JPopupMenu.Separator jSeparator17;
    public javax.swing.JPopupMenu.Separator jSeparator18;
    public javax.swing.JPopupMenu.Separator jSeparator19;
    public javax.swing.JPopupMenu.Separator jSeparator2;
    public javax.swing.JPopupMenu.Separator jSeparator3;
    public javax.swing.JPopupMenu.Separator jSeparator4;
    public javax.swing.JPopupMenu.Separator jSeparator5;
    public javax.swing.JPopupMenu.Separator jSeparator6;
    public javax.swing.JPopupMenu.Separator jSeparator7;
    public javax.swing.JPopupMenu.Separator jSeparator8;
    public javax.swing.JPopupMenu latticePopupMenu;
    public javax.swing.JMenuItem latticePopupPropertiesSetting;
    public javax.swing.JMenuItem latticePopupVisualSetting;
    public javax.swing.JMenuItem latticePropertiesSetting;
    public javax.swing.JMenu latticeSettingMenu;
    public javax.swing.JMenuItem latticeToWindow;
    public javax.swing.JMenuItem latticeVisualSetting;
    public javax.swing.JMenuItem loadSelection;
    public javax.swing.JRadioButtonMenuItem logicalSearchBigger;
    public javax.swing.JRadioButtonMenuItem logicalSearchHighest;
    public javax.swing.JRadioButtonMenuItem logicalSearchInf;
    public javax.swing.JRadioButtonMenuItem logicalSearchLowerCone;
    public javax.swing.JRadioButtonMenuItem logicalSearchLowest;
    public javax.swing.JRadioButtonMenuItem logicalSearchMax;
    public javax.swing.JRadioButtonMenuItem logicalSearchMin;
    public javax.swing.JCheckBoxMenuItem logicalSearchMouseover;
    public javax.swing.JMenuItem logicalSearchPath;
    public javax.swing.JRadioButtonMenuItem logicalSearchSmaller;
    public javax.swing.JRadioButtonMenuItem logicalSearchSup;
    public javax.swing.JRadioButtonMenuItem logicalSearchUperCone;
    public javax.swing.JMenuBar mainMenu;
    public javax.swing.JPopupMenu nodePopupMenu;
    public javax.swing.JMenuItem nodesPopupPropertiesSetting;
    public javax.swing.JMenuItem nodesPopupTagsSetting;
    public javax.swing.JMenuItem nodesPopupTextSearch;
    public javax.swing.JMenuItem nodesPopupVisualSearch;
    public javax.swing.JMenuItem nodesPopupVisualSetting;
    public javax.swing.JMenuItem nodesPropertiesSetting;
    public javax.swing.JMenu nodesSettingMenu;
    public javax.swing.JMenuItem nodesTagsSetting;
    public javax.swing.JMenuItem nodesVisualSetting;
    public javax.swing.JMenuItem openFile;
    public javax.swing.JMenuItem redo;
    public javax.swing.JMenuItem resizeToWindow;
    public javax.swing.JMenuItem restore;
    public javax.swing.JMenuItem saveFile;
    public javax.swing.JMenuItem saveFileAs;
    public javax.swing.JMenuItem saveSelection;
    public javax.swing.JMenu searchMenu;
    public javax.swing.JMenuItem selectAll;
    public javax.swing.JMenuItem selectAllEdges;
    public javax.swing.JMenuItem selectAllNodes;
    public javax.swing.JMenuItem selectAllRelated;
    public javax.swing.JMenuItem selectAllTags;
    public javax.swing.JMenuItem selectEdgesRelatedNodes;
    public javax.swing.JMenu selectNodesRelated;
    public javax.swing.JMenuItem selectNodesRelatedEdges;
    public javax.swing.JMenuItem selectNodesRelatedTags;
    public javax.swing.JMenuItem selectTagsRelatedNodes;
    public javax.swing.JMenu selectionAllMenu;
    public javax.swing.JMenu selectionEdgesMenu;
    public javax.swing.JMenu selectionMenu;
    public javax.swing.JMenu selectionNodesMenu;
    public javax.swing.JMenu selectionTagsMenu;
    public javax.swing.JCheckBoxMenuItem selectionToWindow;
    public javax.swing.JMenu settingMenu;
    public javax.swing.JMenuItem showHiddenAll;
    public javax.swing.JMenu showHiddenMenu;
    public javax.swing.JMenuItem showHiddenNodes;
    public javax.swing.JMenuItem showHiddenTags;
    public javax.swing.JLabel statusBar;
    public javax.swing.JPopupMenu tagPopupMenu;
    public javax.swing.JMenuItem tagsPopupPropertiesSetting;
    public javax.swing.JMenuItem tagsPopupTextSeach;
    public javax.swing.JMenuItem tagsPopupVisualSeach;
    public javax.swing.JMenuItem tagsPopupVisualSetting;
    public javax.swing.JMenuItem tagsPropertiesSetting;
    public javax.swing.JMenu tagsSettingMenu;
    public javax.swing.JMenuItem tagsVisualSetting;
    public javax.swing.JMenu textSearch;
    public javax.swing.JMenuItem textSearchEdges;
    public javax.swing.JMenuItem textSearchNodes;
    public javax.swing.JMenuItem textSearchTags;
    public javax.swing.JMenuItem undo;
    public javax.swing.JMenu viewMenu;
    public javax.swing.JMenu visualSearch;
    public javax.swing.JMenuItem visualSearchEdges;
    public javax.swing.JMenuItem visualSearchNodes;
    public javax.swing.JMenuItem visualSearchTags;
    public javax.swing.JMenuItem windowToLAttice;
    public javax.swing.JMenuItem zoomIn;
    public javax.swing.JMenuItem zoomNormal;
    public javax.swing.JMenuItem zoomOut;
    // End of variables declaration//GEN-END:variables
    public HelpAbout helpAboutWindow;
}
