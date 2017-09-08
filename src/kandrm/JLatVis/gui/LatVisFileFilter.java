package kandrm.JLatVis.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class LatVisFileFilter extends FileFilter{
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".lat");
    }
    @Override
    public String getDescription() {
        return "LatVis (.lat) files";
    }
}