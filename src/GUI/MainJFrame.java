package GUI;

import GUI.desktop.window.Application;
import GUI.desktop.window.Console.ConsoleApplication;
import GUI.desktop.window.Console.ConsoleJPanel;
import GUI.desktop.window.banker_algorithm.BankerAlgorithm;
import GUI.desktop.window.device_manager.DeviceJPanel;
import GUI.desktop.window.device_manager.DeviceManagerApplication;
import GUI.desktop.window.fat_manager.FATApplication;
import GUI.desktop.window.fat_manager.FATJPanel;
import GUI.desktop.window.file_manager.FileManagerApplication;
import GUI.desktop.window.file_manager.FileManagerJPanel;
import GUI.desktop.window.memory_manager.MemoryJPanel;
import GUI.desktop.window.memory_manager.MemoryManagerApplication;
import GUI.desktop.window.process_manager.ProcessJPanel;
import GUI.desktop.window.process_manager.ProcessManagerApplication;
import computer.hardware.cpu.CPUInterface;
import computer.hardware.timer.TimerInterface;
import java.awt.Toolkit;

/**
 * 主窗口
 * @author Administrator
 */
public class MainJFrame extends javax.swing.JFrame
{

    private MainJPanel mainJPanel;
    private FATApplication fatApplication;
    private DeviceManagerApplication deviceManagerApp;
    private MemoryManagerApplication memoryManagerApp;
    private ProcessManagerApplication processManagerApplication;
    private BankerAlgorithm bankerAlgorithm;
    private FileManagerApplication fileManagerApplication;
    private ConsoleApplication consoleApplication;

    public MainJFrame(CPUInterface cpu, TimerInterface timer)
    {
        initComponents();
        initApplication(cpu,timer);
        this.setVisible(true);
    }

    private void initComponents()
    {
        mainJPanel = new MainJPanel();
        this.setTitle("模拟操作系统");
        //this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        this.setUndecorated(true);
//        com.sun.awt.AWTUtilities.setWindowOpaque(this,false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
        this.mainJPanel.addComponentListen();
    }

    private void initApplication(CPUInterface cpu, TimerInterface timer)
    {
        this.memoryManagerApp = new MemoryManagerApplication();
        this.addApplication(memoryManagerApp);
        this.processManagerApplication = new ProcessManagerApplication();
        this.addApplication(processManagerApplication);
        this.fatApplication = new FATApplication();
        this.addApplication(fatApplication);
        this.fileManagerApplication = new FileManagerApplication();
        this.addApplication(fileManagerApplication);
        this.deviceManagerApp = new DeviceManagerApplication();
        this.addApplication(deviceManagerApp);
        this.bankerAlgorithm = new BankerAlgorithm();
        this.addApplication(bankerAlgorithm);
        this.consoleApplication = new ConsoleApplication(cpu,timer);
        this.addApplication(consoleApplication);
    }

    public void addApplication(Application aplication)
    {
        this.mainJPanel.addApplication(aplication);
    }

    public FATJPanel getFATJPanel()
    {
        return (FATJPanel) this.fatApplication.getApplicationJPanel();
    }

    public MemoryJPanel getMemoryJPanel()
    {
        return (MemoryJPanel) this.memoryManagerApp.getApplicationJPanel();
    }

    public DeviceJPanel getDeviceJPanel()
    {
        return (DeviceJPanel) this.deviceManagerApp.getApplicationJPanel();
    }

    public ProcessJPanel getProcessJPanel()
    {
        return (ProcessJPanel) this.processManagerApplication.getApplicationJPanel();
    }

    public FileManagerJPanel getFileManagerJPanel()
    {
        return (FileManagerJPanel) this.fileManagerApplication.getApplicationJPanel();
    }

    public ConsoleJPanel getConsoleJPanel()
    {
        return (ConsoleJPanel) this.consoleApplication.getApplicationJPanel();
    }

}
