package kandrm.JLatVis.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFileChooser;
import kandrm.JLatVis.Application;

/**
 *
 * @author Michal Kandr
 */
public class LoadFileChooser extends JFileChooser {
    protected LatVisFileFilter latVisFileFilter;
    JLatVisFileFilter jLatVisFileFilter;

    public LoadFileChooser(){
        super();
        jLatVisFileFilter = new JLatVisFileFilter();
        latVisFileFilter = new LatVisFileFilter();
        addChoosableFileFilter(jLatVisFileFilter);
        addChoosableFileFilter(latVisFileFilter);
        setFileFilter(jLatVisFileFilter);
    }

    @Override
    public File getSelectedFile(){
        File file = super.getSelectedFile();
        if(file != null && ! file.getName().toLowerCase().endsWith(Application.JLATVIS_FILE_SUFFIX) && ! file.getName().toLowerCase().endsWith(Application.LATVIS_FILE_SUFFIX)){
            file = new File(file.getAbsolutePath()+Application.JLATVIS_FILE_SUFFIX);
        }
        return file;
    }

    @Override
    public int showOpenDialog(Component parent) throws HeadlessException {
        setDialogType(JFileChooser.OPEN_DIALOG);
        setDialogTitle("Open");
        removeChoosableFileFilter(latVisFileFilter);
        addChoosableFileFilter(latVisFileFilter);
        setFileFilter(jLatVisFileFilter);
        return super.showOpenDialog(parent);
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        setDialogType(JFileChooser.SAVE_DIALOG);
        setDialogTitle("Save as");
        removeChoosableFileFilter(latVisFileFilter);
        setFileFilter(jLatVisFileFilter);
        return super.showSaveDialog(parent);
    }
}
