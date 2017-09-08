package kandrm.JLatVis.gui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Michal Kandr
 */
public class ExportFileChooser extends JFileChooser {
    private abstract class ExportFileFilter extends FileFilter{
        protected String description;
        protected String typeName;
        protected List<String> allowedSuffixes;

        public ExportFileFilter(){
            allowedSuffixes = new ArrayList<String>();
        }

        @Override
        public boolean accept(File f){
            if(f.isDirectory()){
                return true;
            }
            String fileName = f.getName().toLowerCase();
            for(String suffix : allowedSuffixes){
                if(fileName.endsWith(suffix)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription(){
            return description;
        }

        public String getTypeName(){
            return typeName;
        }
    }

    private class JpgExportFileFilter extends ExportFileFilter{
        public JpgExportFileFilter(){
            super();
            description = "JPG";
            typeName = "jpg";
            allowedSuffixes.add("jpg");
            allowedSuffixes.add("jpeg");
        }
    }

    private class PngExportFileFilter extends ExportFileFilter{
        public PngExportFileFilter(){
            super();
            description = "PNG";
            typeName = "png";
            allowedSuffixes.add("png");
        }
    }

    private class GifExportFileFilter extends ExportFileFilter{
        public GifExportFileFilter(){
            super();
            description = "GIF";
            typeName = "gif";
            allowedSuffixes.add("gif");
        }
    }

    private class BmpExportFileFilter extends ExportFileFilter{
        public BmpExportFileFilter(){
            super();
            description = "BMP";
            typeName = "bmp";
            allowedSuffixes.add("bmp");
        }
    }

    private class PdfExportFileFilter extends ExportFileFilter{
        public PdfExportFileFilter(){
            super();
            description = "PDF";
            typeName = "pdf";
            allowedSuffixes.add("pdf");
        }
    }

    private class SvgExportFileFilter extends ExportFileFilter{
        public SvgExportFileFilter(){
            super();
            description = "SVG";
            typeName = "svg";
            allowedSuffixes.add("svg");
        }
    }

    private class MpExportFileFilter extends ExportFileFilter{
        public MpExportFileFilter(){
            super();
            description = "Metapost";
            typeName = "mp";
            allowedSuffixes.add("mp");
        }
    }


    public ExportFileChooser() {
        super();
        
        setAcceptAllFileFilterUsed(false);
        setDialogType(JFileChooser.SAVE_DIALOG);
        setDialogTitle("Export");
        
        JpgExportFileFilter jpgExportFileFilter = new JpgExportFileFilter();

        addChoosableFileFilter(jpgExportFileFilter);
        addChoosableFileFilter(new PngExportFileFilter());
        addChoosableFileFilter(new GifExportFileFilter());
        addChoosableFileFilter(new BmpExportFileFilter());
        addChoosableFileFilter(new PdfExportFileFilter());
        addChoosableFileFilter(new SvgExportFileFilter());
        addChoosableFileFilter(new MpExportFileFilter());

        setFileFilter(jpgExportFileFilter);

        /*addPropertyChangeListener("fileFilterChanged", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                File file = getSelectedFile();
                String type = getFileType();
                if(file == null){
                    return;
                }
                if(file.getName().toLowerCase().matches(".*\\.[a-z0-9]+$")){
                    file = new File(file.getName().replace("\\.[a-z0-9]+$", '.' + type));
                } else {
                    file = new File(file.getName() + '.' + type);
                }
                setSelectedFile(file);
            }
        });*/
    }

    public String getFileType(){
        return ((ExportFileFilter)getFileFilter()).getTypeName();
    }

    @Override
    public File getSelectedFile(){
       File file = super.getSelectedFile();
       if(file != null && ! file.getName().toLowerCase().matches(".*\\.[a-z0-9]+$")){
            try {
                file = new File(file.getCanonicalPath() + "." + getFileType());
            } catch (IOException ex) {
                Logger.getLogger(ExportFileChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    public int showDialog(Component parent){
        return super.showDialog(parent, "Export");
    }
}
