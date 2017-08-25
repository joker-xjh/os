package GUI.desktop.window.process_manager;

import GUI.desktop.window.ApplicationJPanel;
import computer.hardware.cpu.CPU;
import java.awt.Graphics;
import javax.swing.GroupLayout;

/**
 *
 * @author Administrator
 */
public class ProcessJPanel extends ApplicationJPanel
{

    private CPUJPanel cpuJPanel;
    private PCBJPanel pcbJPanel;
    private boolean flag;

    public ProcessJPanel()
    {
        this.cpuJPanel = new CPUJPanel();
        this.pcbJPanel = new PCBJPanel();
        initComponents();
    }

    private void initComponents()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE), "进程管理", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
//        layout.setAutoCreateContainerGaps(true);
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        hGroup.addComponent(cpuJPanel, 0, 60, Short.MAX_VALUE)
                .addComponent(pcbJPanel, 0, 60, Short.MAX_VALUE);
        vGroup.addComponent(cpuJPanel, 0, 200, Short.MAX_VALUE)
                .addComponent(pcbJPanel, 0, 300, Short.MAX_VALUE);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
    }

    @Override
    public void refresh()
    {
        this.cpuJPanel.refresh();
        this.pcbJPanel.refresh();
    }

    public void setProcessInformation(CPU cpu, ProcessInformation processInformation)
    {
        this.cpuJPanel.setProcessInformation(cpu, processInformation);
        this.pcbJPanel.setProcessInformation(processInformation);
    }
}
