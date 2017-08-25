package GUI.desktop.window.process_manager;

import GUI.desktop.window.ApplicationJPanel;
import computer.os.kernel.process_management.PCB;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class PCBJPanel extends ApplicationJPanel
{

    private JPanel[] jPanel;
    private JButton[][] jLabel;
    private JButton[][][] jButton;
    private ProcessInformation processInformation;
    private boolean flag;
    private int readyCount;
    private int waitCount;

    public PCBJPanel()
    {
        flag = true;
        this.jPanel = new JPanel[2];
        for (int i = 0; i < jPanel.length; i++)
        {
            jPanel[i] = new JPanel();
            jPanel[i].setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "就绪进程", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        }
        this.jLabel = new JButton[2][3];
        for (int i = 0; i < jLabel.length; i++)
        {
            for (int j = 0; j < jLabel[i].length; j++)
            {
                jLabel[i][j] = new JButton();
                jLabel[i][j].setBackground(Color.ORANGE);
                jLabel[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.YELLOW));
                jLabel[i][j].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                jLabel[i][j].setMargin(new java.awt.Insets(0, 0, 0, 0));
                jLabel[i][j].setFont(new java.awt.Font("宋体", 0, 12));
            }
            jLabel[i][0].setText("映象名称");
            jLabel[i][1].setText("进程PID");
        }
        jLabel[0][2].setText("内存标号");
        jLabel[1][2].setText("等待原因");
        this.jButton = new JButton[2][10][3];
        for (int i = 0; i < jButton.length; i++)
        {
            for (int j = 0; j < jButton[i].length; j++)
            {
                for (int k = 0; k < jButton[i][j].length; k++)
                {
                    jButton[i][j][k] = new JButton();
                    jButton[i][j][k].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE));
                    jButton[i][j][k].setBackground(Color.WHITE);
                    jButton[i][j][k].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                    jButton[i][j][k].setMargin(new java.awt.Insets(0, 0, 0, 0));
                    jButton[i][j][k].setFont(new java.awt.Font("宋体", 0, 12));
                }
            }
        }
        initComponents();
    }

    private void initComponents()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "进程表", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE)); // NOI18N
        jPanel[0].setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "就绪进程", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.BLACK));
        jPanel[1].setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "等待进程", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.BLACK));
        GroupLayout.ParallelGroup pGroup = null;
        GroupLayout.SequentialGroup sGroup = null;
        javax.swing.GroupLayout[] jPanellayout = new javax.swing.GroupLayout[2];
        for (int i = 0; i < jPanel.length; i++)
        {
            jPanellayout[i] = new javax.swing.GroupLayout(jPanel[i]);
            jPanel[i].setLayout(jPanellayout[i]);
            GroupLayout.ParallelGroup hGroup = jPanellayout[i].createParallelGroup(GroupLayout.Alignment.LEADING);
            GroupLayout.SequentialGroup vGroup = jPanellayout[i].createSequentialGroup();
            sGroup = jPanellayout[i].createSequentialGroup();
            sGroup.addComponent(jLabel[i][0], 0, 50, Short.MAX_VALUE).addComponent(jLabel[i][1], 0, 50, Short.MAX_VALUE).addComponent(jLabel[i][2], 0, 50, Short.MAX_VALUE);
            hGroup.addGroup(sGroup);
            for (int j = 0; j < jButton[i].length; j++)
            {
                sGroup = jPanellayout[i].createSequentialGroup();
                sGroup.addComponent(jButton[i][j][0], 0, 50, Short.MAX_VALUE).addComponent(jButton[i][j][1], 0, 50, Short.MAX_VALUE).addComponent(jButton[i][j][2], 0, 50, Short.MAX_VALUE);
                hGroup.addGroup(sGroup);
            }
            pGroup = jPanellayout[i].createParallelGroup(GroupLayout.Alignment.LEADING);
            pGroup.addComponent(jLabel[i][0], 0, 25, Short.MAX_VALUE).addComponent(jLabel[i][1], 0, 25, Short.MAX_VALUE).addComponent(jLabel[i][2], 0, 25, Short.MAX_VALUE);
            vGroup.addGroup(pGroup);
            for (int j = 0; j < jButton[i].length; j++)
            {
                pGroup = jPanellayout[i].createParallelGroup(GroupLayout.Alignment.LEADING);
                pGroup.addComponent(jButton[i][j][0], 0, 25, Short.MAX_VALUE).addComponent(jButton[i][j][1], 0, 25, Short.MAX_VALUE).addComponent(jButton[i][j][2], 0, 25, Short.MAX_VALUE);
                vGroup.addGroup(pGroup);
            }
            jPanellayout[i].setHorizontalGroup(hGroup);
            jPanellayout[i].setVerticalGroup(vGroup);
        }
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(jPanel[0], 0, 150, Short.MAX_VALUE).addGap(5, 5, 5).addComponent(jPanel[1], 0, 150, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel[0], 0, 175, Short.MAX_VALUE).addGap(0, 175, Short.MAX_VALUE).addComponent(jPanel[1], 0, 175, Short.MAX_VALUE));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (flag)
        {
            flag = false;
            if (processInformation != null)
            {
                this.initReadyPCBTable();
                this.initWaitPCBTable();
                int i = 0, j = 0, count = this.processInformation.getCount();
                for (int k = 0; k < count; k++)
                {
                    int status = processInformation.getStatus(k);
                    if (status == PCB.STATUS_READY)
                    {
                        this.addReadyPCB(processInformation.getName(k), processInformation.getIndex(k), processInformation.getMCBIndex(k));
                    } else if (status != PCB.STATUS_RUN && status != PCB.STATUS_END)
                    {
                        this.addWaitPCB(processInformation.getName(k), processInformation.getIndex(k), status);
                    }
                }
            }
        }
    }

    @Override
    public void refresh()
    {
        flag = true;
        // this.repaint();
    }

    public void setProcessInformation(ProcessInformation processInformation)
    {
        this.processInformation = processInformation;
    }

    public void initReadyPCBTable()
    {
        this.readyCount = 0;
        initPCBTable(0);
    }

    public void initWaitPCBTable()
    {
        this.waitCount = 0;
        initPCBTable(1);
    }

    private void initPCBTable(int index)
    {
        for (int i = 0; i < this.jButton[index].length; i++)
        {
            for (int j = 0; j < this.jButton[index][i].length; j++)
            {
                this.jButton[index][i][j].setText("");
            }
        }
    }

    private void addReadyPCB(int name, int pid, int mcbIndex)
    {
        String strName = "";
        strName += (char) (name & 0xff);
        name >>= 8;
        strName += (char) (name & 0xff);
        name >>= 8;
        strName += (char) (name & 0xff);
        strName += '.';
        name >>= 8;
        strName += (char) (name & 0xff);
        if (pid == 0)
        {
            this.jButton[0][readyCount][0].setText("闲逛");
        } else
        {
            this.jButton[0][readyCount][0].setText(strName);
        }
        this.jButton[0][readyCount][1].setText("" + pid);
        this.jButton[0][readyCount][2].setText("" + mcbIndex);
        readyCount++;
    }

    private void addWaitPCB(int name, int pid, int waitReason)
    {
        String strName = "";
        strName += (char) (name & 0xff);
        name >>= 8;
        strName += (char) (name & 0xff);
        name >>= 8;
        strName += (char) (name & 0xff);
        strName += '.';
        name >>= 8;
        strName += (char) (name & 0xff);
        if (pid == 0)
        {
            this.jButton[0][readyCount][0].setText("闲逛");
        } else
        {
            this.jButton[1][waitCount][0].setText(strName);
        }
        this.jButton[1][waitCount][1].setText("" + pid);
        switch (waitReason)
        {
            case PCB.STATUS_WAIT_IO:
                this.jButton[1][waitCount][2].setText("正在IO");
                break;
            case PCB.STATUS_WAIT_DEVICE_A:
                this.jButton[1][waitCount][2].setText("等待A");
                break;
            case PCB.STATUS_WAIT_DEVICE_B:
                this.jButton[1][waitCount][2].setText("等待B");
                break;
            case PCB.STATUS_WAIT_DEVICE_C:
                this.jButton[1][waitCount][2].setText("等待C");
                break;
        }
        waitCount++;
    }
}
