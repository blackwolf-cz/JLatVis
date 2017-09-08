package kandrm.JLatVis.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michal Kandr
 */
public class MainGuiCloser implements WindowListener {
    private static MainGuiCloser instance = new MainGuiCloser();

    private List<MainGui> windows = new ArrayList<MainGui>();

    private MainGuiCloser(){}
    public static MainGuiCloser getInstance(){
        return instance;
    }

    public void registerWindow(MainGui window){
        windows.add(window);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {
        if(windows.size() == 1){
            System.exit(0);
        } else {
            windows.remove((MainGui)e.getSource());
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
