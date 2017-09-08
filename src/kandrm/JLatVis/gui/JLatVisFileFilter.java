package kandrm.JLatVis.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import kandrm.JLatVis.Application;

/**
 *
 * @author Michal Kandr
 */
public class JLatVisFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(Application.JLATVIS_FILE_SUFFIX);
    }
    @Override
    public String getDescription() {
        return "JLatVis files ("+Application.JLATVIS_FILE_SUFFIX+")";
    }
}
