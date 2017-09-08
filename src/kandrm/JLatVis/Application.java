package kandrm.JLatVis;

import java.io.BufferedReader;
import kandrm.JLatVis.export.XmlInvalidException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import kandrm.JLatVis.gui.LatVisFileFilter;
import kandrm.JLatVis.export.XmlExport;
import kandrm.JLatVis.export.LatVisImport;
import kandrm.JLatVis.gui.MainGui;
import kandrm.JLatVis.gui.MainGuiCloser;
import kandrm.JLatVis.lattice.logical.Lattice;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Kandr
 */
public class Application {
    public static final String FILE_ARG = "--file=";
    public static final String FILE_FORMAT_ARG = "--file-format=";
    public static final String CLOSE_IF_WRONG_FILE = "--close-if-wrong-file";
    public static final String DIAGRAM_ARG = "--diagram";
    public static final String DIAGRAM_ARG_SHORT = "-d";

    public static final String FILE_FORMAT_JLATVIS = "jlatvis";
    public static final String FILE_FORMAT_LATVIS = "latvis";

    public static final String JLATVIS_FILE_SUFFIX = ".jlat";
    public static final String LATVIS_FILE_SUFFIX = ".lat";

    private MainGui mainGui;

    public Application(String file, String format, boolean closeIfWrongFile, Integer drawWithProgram){
        if( ! closeIfWrongFile){
            mainGui = new MainGui(this);
            mainGui.setVisible(true);
        }
        if(file != null){
            loadFile(file, format, closeIfWrongFile, drawWithProgram);
        }
    }
    
    public Application(String xml, Integer drawWithProgram){
        loadXml(xml, drawWithProgram);
    }
    
    public Application(String file, String format, boolean closeIfWrongFile){
        this(file, format, closeIfWrongFile, null);
    }
    
    public Application(String file, String format){
        this(file, format, false);
    }


    public final void loadFile(File file, String format, boolean closeIfWrongFile, Integer drawWithProgram){
        Lattice l = null;
        try {
            if(FILE_FORMAT_LATVIS.equals(format) || new LatVisFileFilter().accept(file)){
                l = LatVisImport.load(file);
            } else {
                l = XmlExport.load(file);
            }
            if(closeIfWrongFile){
                mainGui = new MainGui(this);
                mainGui.setVisible(true);
            }
            mainGui.setLattice(l, file, drawWithProgram);
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nInternal error.", "Opening file error.", JOptionPane.ERROR_MESSAGE);
            if(closeIfWrongFile && mainGui != null){
                mainGui.dispose();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nFile don't exists or is unreadable.", "Opening file error.", JOptionPane.ERROR_MESSAGE);
            if(closeIfWrongFile && mainGui != null){
                mainGui.dispose();
            }
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nFile is in wrong format.", "Opening file error.", JOptionPane.ERROR_MESSAGE);
            if(closeIfWrongFile && mainGui != null){
                mainGui.dispose();
            }
        } catch (XmlInvalidException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nFile is in wrong format", "Opening file error.", JOptionPane.ERROR_MESSAGE);
            if(closeIfWrongFile && mainGui != null){
                mainGui.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nInternal error.", "Opening file error.", JOptionPane.ERROR_MESSAGE);
            //System.out.print(ex);
            if(closeIfWrongFile && mainGui != null){
                mainGui.dispose();
            }
        }
    }
    public final void loadXml(String xml, Integer drawWithProgram){
        Lattice l = null;
        try {
            boolean loaded = false;
            try{
                l = XmlExport.load(xml);
                loaded = true;
            } catch (SAXException ex) {
            } catch (XmlInvalidException ex) {
            }
            if( ! loaded){
                l = LatVisImport.load(xml);
            }
            mainGui = new MainGui(this);
            mainGui.setVisible(true);
            mainGui.setLattice(l, null, drawWithProgram);
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nXML input is in wrong format.", "Reading input error.", JOptionPane.ERROR_MESSAGE);
            if(mainGui != null){
                mainGui.dispose();
            }
        } catch (XmlInvalidException ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nXML input is in wrong format", "Reading input error.", JOptionPane.ERROR_MESSAGE);
            if(mainGui != null){
                mainGui.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainGui, "Error when opening lattice.\nInternal error.", "Reading input error.", JOptionPane.ERROR_MESSAGE);
            if(mainGui != null){
                mainGui.dispose();
            }
        }
    }
    
    public final void loadFile(File file){
        loadFile(file, null, false, null);
    }
    public final void loadFile(String file, String format, boolean closeIfWrongFile){
        loadFile(new File(file), format, closeIfWrongFile, null);
    }
    public final void loadFile(File file, String format, boolean closeIfWrongFile){
        loadFile(file, format, closeIfWrongFile, null);
    }
    public final void loadFile(String file, String format, boolean closeIfWrongFile, Integer drawWithProgram){
         loadFile(new File(file), format, closeIfWrongFile, drawWithProgram);
    }
    
     private static String readStdIn() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder result = new StringBuilder();
        while(in.ready()){
            result.append(in.readLine());
        }
        return result.toString();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // load cmd parameters
                List<String> files = new ArrayList<String>();
                String format = null;
                boolean nextIsDiagramProgram = false;
                Integer diagramProgram = null;
                boolean closeIfWrongFile = false;
                for(String arg : args){
                    if(arg.startsWith(Application.FILE_ARG)){
                        files.add(arg.substring(Application.FILE_ARG.length()));
                    } else if(arg.startsWith(Application.FILE_FORMAT_ARG)){
                        format = arg.substring(Application.FILE_FORMAT_ARG.length());
                    } else if(arg.startsWith(Application.CLOSE_IF_WRONG_FILE)){
                        String substParam = arg.substring(Application.CLOSE_IF_WRONG_FILE.length());
                        closeIfWrongFile = substParam.equals("1") || substParam.equals("true") || substParam.equals("t");
                    } else if(arg.startsWith(Application.DIAGRAM_ARG) || arg.startsWith(Application.DIAGRAM_ARG_SHORT)){
                        nextIsDiagramProgram = true;
                    } else if(nextIsDiagramProgram && arg.matches("^\\d+$")){
                        diagramProgram = Integer.parseInt(arg);
                    } else {
                        files.add(arg);
                    }
                }
                if(nextIsDiagramProgram && diagramProgram == null){
                    diagramProgram = 1;
                }
                
                // execute program with files
                if(files.size() > 0){
                    for(String file : files){
                        file = file.replaceAll("\"", "");
                        if(format == null){
                            format = file.endsWith(LATVIS_FILE_SUFFIX) ? FILE_FORMAT_LATVIS : FILE_FORMAT_JLATVIS;
                        }
                        Application application = new Application(file, format, closeIfWrongFile, diagramProgram);
                        MainGuiCloser.getInstance().registerWindow( application.mainGui );
                    }
                // execute without files from cmd
                } else {
                    try {
                        String stdin = readStdIn();
                        if(stdin.length() > 1){
                            Application application = new Application(stdin, diagramProgram);
                            MainGuiCloser.getInstance().registerWindow( application.mainGui );
                            return;
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error when reading stdin input.", "Input reading error.", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Application application = new Application(null, format, closeIfWrongFile, diagramProgram);
                    MainGuiCloser.getInstance().registerWindow( application.mainGui );
                }
            }
        });
    }
}
