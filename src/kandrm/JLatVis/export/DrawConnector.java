package kandrm.JLatVis.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kandrm.JLatVis.Application;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;

/**
 *
 * @author Michal Kandr
 */
public class DrawConnector {
    public class DrawProgram{
        private String name;
        private String command;
        public DrawProgram(String name, String command) {
            this.name = name;
            this.command = command;
        }
        public String getCommand() {
            return command;
        }
        public String getName() {
            return name;
        }
    }
    
    protected static final String PROGRAMS_DIR = File.separator + "ext" + File.separator + "drawing-tools" + File.separator;
    
    protected List<ChangeListener> loadedListeners = new ArrayList<ChangeListener>();

    private static final String PARAM_NAME = "-v";
    private static final String PARAM_HELP = "-h";
    
    protected Lattice lattice;

    public DrawConnector(Lattice lattice){
        this.lattice = lattice;
    }

    public void setLattice(Lattice lattice){
        this.lattice = lattice;
    }
    public Lattice getLattice(){
        return lattice;
    }
   
    public void addLoadedListener(ChangeListener listener){
        loadedListeners.add(listener);
    }
    public void removeLoadedListener(ChangeListener listener){
        loadedListeners.remove(listener);
    }

    protected void fireLoadedEvent(){
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : loadedListeners) {
            listener.stateChanged(event);
        }
    }
    
    public String getApplicationDir() throws URISyntaxException{
        ClassLoader loader = Application.class.getClassLoader();
        String classPath = loader.getResource( Application.class.getName().replace('.', '/') + ".class" ).toURI().toString().replace("Application.class", "");
        // in jar file - get only jar path
        if(classPath.startsWith("jar:")){
            classPath = classPath.substring(4);
            if(classPath.indexOf("!") > 0){
                classPath = classPath.substring(0, classPath.indexOf("!"));
            }
            classPath = classPath.substring(0, classPath.lastIndexOf("/"));
        }
        classPath = classPath.substring(5); // strip "file:"
        File file = new File(classPath);
        String path = file.getPath();
        try {
            path = URLDecoder.decode(file.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DrawConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path + PROGRAMS_DIR;
    }

    public List<DrawProgram> getAvailablePrograms(){
        List<DrawProgram> programs = new ArrayList<DrawProgram>();
        File dir;
        try {
            dir = new File( getApplicationDir() );
        } catch (URISyntaxException ex) {
            return programs;
        }
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; ++ i){
                String program = files[i].getPath();
                try{
                    String name = "";
                    if(program.endsWith(".jar")){
                        name = getResult(new String[]{"java", "-jar", program, PARAM_NAME});
                        program = "java -jar \"" + program + "\"";
                    } else {
                        name = getResult(program + " " + PARAM_NAME);
                    }
                    if( ! name.equals("")){
                        programs.add(new DrawProgram(name, program));
                    }
                } catch (Exception e){}
            }
        }
        return programs;
    }

    protected Map<Node, Integer> createLineNumbers(){
        Map<Node, Integer> lineNumbers = new HashMap<Node, Integer>();
        Integer i = 1;
        for(Node node : lattice.getNodes().values()){
            lineNumbers.put(node, i);
            ++ i;
        }
        return lineNumbers;
    }

    protected Map<Integer, Node> createNodeLines(){
        Map<Integer, Node> nodeNumbers = new HashMap<Integer, Node>();
        Integer i = 1;
        for(Node node : lattice.getNodes().values()){
            nodeNumbers.put(i, node);
            ++ i;
        }
        return nodeNumbers;
    }

    public String export(){
        StringBuilder result = new StringBuilder();

        Map<Node, Integer> lineNumbers = createLineNumbers();

        for(Node node : lattice.getNodes().values()){
            result.append("\"").append(node.getName().replace("\"", "'")).append("\" ");
            for(Node child : node.getDescendants()){
                result.append(lineNumbers.get(child)).append(" ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    
    private Map<Node, Point2D> loadPositions(List<String> result) throws Exception{
        Map<Node, Point2D> positions = new HashMap<Node, Point2D>();
        
        Map<Integer, Node> nodeNumbers = createNodeLines();
        if(result.size() != nodeNumbers.size()){
            throw new Exception("Number of returned lines don't match lattice nodes.");
        }
        Integer i = 1;
        for(String line : result){
            Node node = nodeNumbers.get(i);
            line = line.trim().replaceFirst("\".*\"\\s*", "");
            String[] coords = line.split("[^\\d+\\-\\.]+");
            if(coords.length != 2){
                throw new Exception("Wrong coordinates specification on line "+i+".");
            }
            positions.put(node, new Point2D(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
            ++ i;
        }
        return positions;
    }
    
    private void transformPositions(Map<Node, Point2D> positions){
        boolean vFlip = false,
                hFlip = false;
        double vStretch = 1,
               hStretch = 1;
        
        // test original positions are zero - then no stretching and horizontal rotation is performed 
        boolean positionsNotSet = true;
        Point2D zeroNode = lattice.getShape().getNodes().get(0).getCenter();
        for(NodeShape n : lattice.getShape().getNodes()){
            if( ! n.getCenter().equals(zeroNode)){
                positionsNotSet = false;
                break;
            }
        }
        
        // vertical flip and stretch
        Node highest = lattice.getHighest(),
             lowest = lattice.getLowest();
        if(positions.get(highest).y > positions.get( lowest ).y){
            vFlip = true;
        }
        if( ! positionsNotSet){
            vStretch = (lowest.getShape().getCenter().y - highest.getShape().getCenter().y) / Math.abs( positions.get(highest).y - positions.get(lowest).y );
        }
        
        // horizontal flip and stretch
        // original lattice left and right node
        Node origLeft = null,
             origRight = null;
        for(Node node : lattice.getNodes().values()){
            if(origLeft == null || origLeft.getShape().getCenter().x > node.getShape().getCenter().x){
                origLeft = node;
            }
            if(origRight == null || origRight.getShape().getCenter().x < node.getShape().getCenter().x){
                origRight = node;
            }
        }
        // horizontal flip
        if( ! positionsNotSet && positions.get(origLeft).x > positions.get(highest).x
                || ( positions.get(origLeft).x == positions.get(highest).x && positions.get(origRight).x < positions.get(highest).x)){
            hFlip = true;
        }
        
        // horizontal stretch
        Node newLeft = null,
             newRight = null;
        for(Map.Entry<Node, Point2D> entry : positions.entrySet()){
            if(newLeft == null || positions.get(newLeft).x > entry.getValue().x){
                newLeft = entry.getKey();
            }
            if(newRight == null || positions.get(newRight).x < entry.getValue().x){
                newRight = entry.getKey();
            }
        }
        if( ! positionsNotSet){
            hStretch = (origRight.getShape().getCenter().x - origLeft.getShape().getCenter().x) / (positions.get(newRight).x - positions.get(newLeft).x);
        }
        
        // transform
        AffineTransform2D transform = AffineTransform2D.createScaling(hStretch, vStretch);
        if(hFlip){
            double hCenter = (positions.get(newRight).x - positions.get(newLeft).x)/2;
            transform = transform.chain( AffineTransform2D.createLineReflection( new Line2D(hCenter, 0, hCenter, 100) ) );
        }
        if(vFlip){
            double vCenter = Math.abs( positions.get(highest).y - positions.get( lowest ).y ) / 2;
            transform = transform.chain( AffineTransform2D.createLineReflection( new Line2D(0, vCenter, 100, vCenter) ) );
        }
        
        for(Point2D center : positions.values()){
            center.setLocation( center.transform(transform) );
        }
    }
    
    private void setPositions(Map<Node, Point2D> positions){
        for(Map.Entry<Node, Point2D> entry : positions.entrySet()){
            entry.getKey().getShape().setCenter( entry.getValue() );
        }
    }
    
    public void load(List<String> result) throws Exception{
        if(result.isEmpty()){
            throw new Exception("Result is empty.");
        }
        Map<Node, Point2D> positions = loadPositions(result);
        transformPositions(positions);
        setPositions(positions);
        CoordsRecount.adjustCoords(lattice);
    }
    
    

    public void execute(String command) throws Exception{
         Process pr = Runtime.getRuntime().exec(command);
         // send data
         OutputStream out = pr.getOutputStream();
         PrintWriter printOut = new PrintWriter(out);
         printOut.print( export() );
         printOut.flush();
         printOut.close();
         // read result
         BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
         List<String> result = new ArrayList<String>();
         String line = null;
         while((line=input.readLine()) != null) {
             result.add(line);
         }
         load(result);
         lattice.getShape().repaint();
         fireLoadedEvent();
    }

    public String getResult(Process pr) throws Exception{
         StringBuilder result = new StringBuilder();
         BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
         String line = null;
         while((line=input.readLine()) != null) {
             result.append(line).append("\n");
         }
        return result.toString();
    }
    public String getResult(String command) throws Exception{
         return getResult(Runtime.getRuntime().exec(command));
    }
    public String getResult(String[] commands) throws Exception{
         return getResult(Runtime.getRuntime().exec(commands));
    }
    
    public String getHelp(String command) throws Exception{
        return getResult(command + " " + PARAM_HELP);
    }
    
    public boolean drawWithProgram(Integer program){
        List<DrawProgram> availablePrograms = getAvailablePrograms();
        if(program < 1 || program > availablePrograms.size()){
            return false;
        }
        try {
            execute( availablePrograms.get(program - 1).getCommand() );
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
