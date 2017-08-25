package GUI.desktop.window.process_manager;

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
public class ProcessManagerApplication implements Application,java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private ProcessJPanel ProcessJPanel;
    private DesktopWindow memoryWindow;

    public ProcessManagerApplication()
    {
        visible = true;
        desktopIcon = new DesktopIcon("进程管理");
        ProcessJPanel = new ProcessJPanel();
//        ProcessJPanel.setOpaque(false);
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/process.jpg")));
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
        return ProcessJPanel;
    }

    @Override
    public void setJInternalFrame(DesktopWindow desktopWindow)
    {
        this.memoryWindow = desktopWindow;
        this.memoryWindow.add(ProcessJPanel);
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
