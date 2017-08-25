package GUI.desktop.window;

import GUI.desktop.DesktopIcon;
import GUI.desktop.DesktopWindow;


/**
 * 桌面应用基类
 * @author Administrator
 */
public interface  Application
{
    public DesktopIcon getDesktopIcon();
    public ApplicationJPanel getApplicationJPanel();
    public void setJInternalFrame(DesktopWindow desktopWindow);
}
