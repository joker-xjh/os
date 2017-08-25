package GUI.desktop.window.memory_manager;

import GUI.desktop.DesktopIcon;
import GUI.desktop.DesktopWindow;
import GUI.desktop.window.Application;
import GUI.desktop.window.ApplicationJPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 *
 * @author Administrator
 */
public class MemoryManagerApplication implements Application,java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private MemoryJPanel memoryJPanel;
    private DesktopWindow memoryWindow;

    public MemoryManagerApplication()
    {
        visible = true;
        desktopIcon = new DesktopIcon("内存管理");
        memoryJPanel = new MemoryJPanel();
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/memory.jpg")));
        desktopIcon.addActionListener(this);
    }

    @Override
    public DesktopIcon getDesktopIcon()
    {
        return desktopIcon;
    }

    @Override
    public ApplicationJPanel getApplicationJPanel()
    {
        return memoryJPanel;
    }

    @Override
    public void setJInternalFrame(DesktopWindow desktopWindow)
    {
        this.memoryWindow = desktopWindow;
        this.memoryWindow.add(memoryJPanel);
        this.memoryWindow.setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (memoryWindow != null)
        {
            if (visible)
            {
                int x = memoryWindow.getParent().getWidth() - memoryWindow.getWidth();
                memoryWindow.setLocation(x, 0);
                memoryWindow.repaint();
            }
            this.memoryWindow.setVisible(visible);
            visible = !visible;
        }
    }
}
