package GUI.desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * 桌面窗口
 * @author Administrator
 */
public class DesktopWindow extends javax.swing.JInternalFrame implements MouseMotionListener
{

    public DesktopWindow()
    {
        this("");
    }

    public DesktopWindow(String title)
    {
        super(title);
        this.setUndecorated(false);
        this.addMouseMotionListener(this);
    }

    public void setUndecorated(boolean flag)
    {
        setBorder(null);
        ((BasicInternalFrameUI) (this.getUI())).setNorthPane(null);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        int x = e.getXOnScreen() - this.getParent().getLocationOnScreen().x;
        int y = e.getYOnScreen() - this.getParent().getLocationOnScreen().y;
        if (0 <= x && x < this.getParent().getWidth() && 0 <= y && y < this.getParent().getHeight())
        {
            this.setLocation(x, y);
        }
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
}
