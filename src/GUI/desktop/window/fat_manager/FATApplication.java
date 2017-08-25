package GUI.desktop.window.fat_manager;

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
public class FATApplication implements Application, java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private FATJPanel fatJPanel;
    private DesktopWindow fatWindow;

    public FATApplication()
    {
        visible = true;
        desktopIcon = new DesktopIcon("FAT管理");
        fatJPanel = new FATJPanel();
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/file.jpg")));
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
        return fatJPanel;
    }

    @Override
    public void setJInternalFrame(DesktopWindow desktopWindow)
    {
        this.fatWindow = desktopWindow;
        this.fatWindow.add(fatJPanel);
        this.fatWindow.setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (fatWindow != null)
        {
            if (visible)
            {
                int x = fatWindow.getParent().getWidth() - fatWindow.getWidth();
                fatWindow.setLocation(x, 0);
                fatWindow.repaint();
            }
            this.fatWindow.setVisible(visible);
            visible = !visible;
        }
    }
}
