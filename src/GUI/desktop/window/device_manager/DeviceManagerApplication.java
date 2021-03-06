package GUI.desktop.window.device_manager;

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
public class DeviceManagerApplication implements Application,java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private DeviceJPanel  deviceJPanel;
    private DesktopWindow deviceWindow;

    public DeviceManagerApplication()
    {
        visible = true;
        desktopIcon = new DesktopIcon("设备管理");
        deviceJPanel = new DeviceJPanel();
//        deviceJPanel.setOpaque(false);
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/device.PNG")));
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
