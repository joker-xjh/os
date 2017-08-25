package GUI.desktop.window.Console;

import GUI.desktop.DesktopIcon;
import GUI.desktop.DesktopWindow;
import GUI.desktop.window.Application;
import GUI.desktop.window.ApplicationJPanel;
import computer.hardware.cpu.CPUInterface;
import computer.hardware.timer.TimerInterface;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 *
 * @author Administrator
 */
public class ConsoleApplication implements Application,java.awt.event.ActionListener
{

    private boolean visible;
    private DesktopIcon desktopIcon;
    private ConsoleJPanel  consoleJPanel;
    private DesktopWindow consoleWindow;

    public ConsoleApplication(CPUInterface cpu, TimerInterface timer)
    {
        visible = true;
        desktopIcon = new DesktopIcon("控制面板");
        consoleJPanel = new ConsoleJPanel();
        consoleJPanel.setCpu(cpu);
        consoleJPanel.setTimer(timer);
        desktopIcon.setSize(65, 65);
        desktopIcon.setMinimumSize(new Dimension(65, 65));
        desktopIcon.setFont(new java.awt.Font("宋体", 0, 12));
        desktopIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/image/control_panel.JPG")));
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
        return consoleJPanel;
    }

    @Override
    public void setJInternalFrame(DesktopWindow desktopWindow)
    {
        this.consoleWindow = desktopWindow;
        this.consoleWindow.add(consoleJPanel);
        this.consoleWindow.setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (consoleWindow != null)
        {
            if (visible)
            {
                int x = consoleWindow.getParent().getWidth() - consoleWindow.getWidth();
                consoleWindow.setLocation(x, 0);
                consoleWindow.repaint();
            }
            this.consoleWindow.setVisible(visible);
            visible = !visible;
        }
    }
}
