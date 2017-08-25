package GUI.desktop.window.banker_algorithm;

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
public class BankerAlgorithm implements Application,java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private DeviceManagerJPanel  deviceJPanel;
    private DesktopWindow deviceWindow;

    public BankerAlgorithm()
    {
        visible = true;
        desktopIcon = new DesktopIcon("银行家算法");
        deviceJPanel = new DeviceManagerJPanel();
//        deviceJPanel.setOpaque(false);
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/banker_algorithm.JPG")));
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
        return deviceJPanel;
    }

    @Override
    public void setJInternalFrame(DesktopWindow desktopWindow)
    {
        this.deviceWindow = desktopWindow;
        this.deviceWindow.add(deviceJPanel);
        this.deviceWindow.setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (deviceWindow != null)
        {
            if (visible)
            {
                int x = deviceWindow.getParent().getWidth() - deviceWindow.getWidth();
                deviceWindow.setLocation(x, 0);
                deviceWindow.repaint();
            }
            this.deviceWindow.setVisible(visible);
            visible = !visible;
        }
    }
}
