package computer.hardware.device;

import GUI.MainJFrame;
import GUI.desktop.window.Console.ConsoleJPanel;
import GUI.desktop.window.device_manager.DeviceInformation;
import GUI.desktop.window.device_manager.DeviceJPanel;
import GUI.desktop.window.fat_manager.FATInformation;
import GUI.desktop.window.fat_manager.FATJPanel;
import GUI.desktop.window.file_manager.FileInformation;
import GUI.desktop.window.file_manager.FileManagerJPanel;
import GUI.desktop.window.memory_manager.MemoryInformation;
import GUI.desktop.window.memory_manager.MemoryJPanel;
import GUI.desktop.window.process_manager.ProcessInformation;
import GUI.desktop.window.process_manager.ProcessJPanel;
import computer.hardware.cpu.CPU;
import computer.hardware.timer.TimerInterface;
import java.awt.event.WindowEvent;

/**
 * 显示器设备
 * @author Administrator
 */
public class Display
{

    private MainJFrame mainJFrame;
    private FATJPanel fatJPanel;
    private FileManagerJPanel fileManagerJPanel;
    private MemoryJPanel memoryJPanel;
    private DeviceJPanel deviceJPanel;
    private ProcessJPanel processJPanel;
    private ConsoleJPanel consoleJPanel;
    private FATInformation fatInformation;
    private FileInformation fileInformation;
    private MemoryInformation memoryInformation;
    private DeviceInformation deviceInformation;
    private ProcessInformation processInformation;
    private CPU cpu;
    private TimerInterface timer;

    public Display(CPU cpu, TimerInterface timer)
    {
        this.cpu = cpu;
        this.timer = timer;
        this.mainJFrame = new MainJFrame(cpu, timer);
        this.fatInformation = new FATInformation();
        this.fileInformation = new FileInformation();
        this.memoryInformation = new MemoryInformation();
        this.deviceInformation = new DeviceInformation();
        this.processInformation = new ProcessInformation();
        this.fatJPanel = mainJFrame.getFATJPanel();
        this.memoryJPanel = mainJFrame.getMemoryJPanel();
        this.deviceJPanel = mainJFrame.getDeviceJPanel();
        this.processJPanel = mainJFrame.getProcessJPanel();
        this.fileManagerJPanel = mainJFrame.getFileManagerJPanel();
        this.consoleJPanel = mainJFrame.getConsoleJPanel();
        this.fatJPanel.setFatInformation(fatInformation);
        this.fileManagerJPanel.setFileInformation(fileInformation);
        this.memoryJPanel.setMemoryInformation(memoryInformation);
        this.deviceJPanel.setDeviceInformation(deviceInformation);
        this.processJPanel.setProcessInformation(cpu, processInformation);
    }

    public FATInformation getFatInformation()
    {
        return fatInformation;
    }

    public FileInformation getFileInformation()
    {
        return fileInformation;
    }

    public MemoryInformation getMemoryInformation()
    {
        return memoryInformation;
    }

    public DeviceInformation getDeviceInformation()
    {
        return deviceInformation;
    }

    public ProcessInformation getProcessInformation()
    {
        return processInformation;
    }

    public void refreshMemoryWindow()
    {
        if (memoryJPanel != null && memoryJPanel.isVisible())
        {
            this.memoryJPanel.refresh();
            this.mainJFrame.repaint();
        }
    }

    public void refreshProcessWindow()
    {
        if (processJPanel != null && processJPanel.isVisible())
        {
            processJPanel.refresh();
            this.mainJFrame.repaint();
        }
        if(consoleJPanel!=null && consoleJPanel.isVisible())
        {
            consoleJPanel.refresh();
            this.mainJFrame.repaint();
        }
    }

    public void refreshDeviceWindow()
    {
        if (deviceJPanel != null && deviceJPanel.isVisible())
        {
            deviceJPanel.refresh();
            this.mainJFrame.repaint();
        }
    }

    public void refreshFileWindow()
    {
        if (fatJPanel != null && fatJPanel.isVisible())
        {
            fatJPanel.refresh();
            this.mainJFrame.repaint();
        }
        if (fileManagerJPanel != null)
        {
            fileManagerJPanel.refresh();
            this.mainJFrame.repaint();
        }
    }

    public void addActionListenerForCommandLine(java.awt.event.ActionListener l)
    {
        this.fileManagerJPanel.addActionListenerCommandLineJTextField(l);
    }

    public void addWindowListener(java.awt.event.WindowListener l)
    {
        this.mainJFrame.addWindowListener(new java.awt.event.WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                cpu.START();
                fileInformation.command.setCommand(FileInformation.Command.HALT);
            }
        });
        this.mainJFrame.addWindowListener(l);
    }
}
