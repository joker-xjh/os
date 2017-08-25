package GUI.desktop.window.process_manager;

import GUI.desktop.window.ApplicationJPanel;
import computer.hardware.cpu.CPU;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JButton;

/**
 *
 * @author Administrator
 */
public class CPUJPanel extends ApplicationJPanel
{

    private boolean flag;
    private CPU cpu;
    private JButton[] jLabel;
    private JButton[] jButton;
    private ProcessInformation processInformation;

    public CPUJPanel()
    {
        flag = true;
//        this.setBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE), "CPU", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        this.jLabel = new JButton[16];
        for (int i = 0; i < jLabel.length; i++)
        {
            jLabel[i] = new JButton();
            jLabel[i].setBackground(Color.ORANGE);
            jLabel[i].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.YELLOW));
            jLabel[i].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            jLabel[i].setMargin(new java.awt.Insets(0, 0, 0, 0));
            jLabel[i].setFont(new java.awt.Font("宋体", 0, 12));
        }
        this.jButton = new JButton[16];
        for (int i = 0; i < jLabel.length; i++)
        {
            jButton[i] = new JButton();
            jButton[i].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE));
            jButton[i].setBackground(Color.WHITE);
            jButton[i].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            jButton[i].setMargin(new java.awt.Insets(0, 0, 0, 0));
            jButton[i].setFont(new java.awt.Font("宋体", 0, 12));
        }
        jLabel[0].setText("进程PID：");
        jLabel[1].setText("相对时钟：");
        jLabel[2].setText("中断标志：");
        jLabel[3].setText("中断类型：");
        jLabel[4].setText("重定位寄存器");
        jLabel[5].setText("限长寄存器：");
        jLabel[6].setText("AR：");
        jLabel[7].setText("DR：");
        jLabel[8].setText("PC：");
        jLabel[9].setText("IR：");
        jLabel[10].setText("PSW：");
        jLabel[11].setText("x：");
        jLabel[12].setText("AX：");
        jLabel[13].setText("BX：");
        jLabel[14].setText("CX：");
        jLabel[15].setText("DX：");
        initComponents();
    }

    private void initComponents()
    {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup pGroup = null;
        GroupLayout.SequentialGroup sGroup = null;
        for (int i = 0; i < 8; i++)
        {
            if (i != 0)
            {
                hGroup.addGap(0, 0, Short.MAX_VALUE);
            }
            sGroup = layout.createSequentialGroup();
            sGroup.addComponent(jLabel[i * 2], 0, 85, Short.MAX_VALUE).addComponent(jButton[i * 2], 0, 80, Short.MAX_VALUE).addGap(5, 5, 5).addComponent(jLabel[i * 2 + 1], 0, 85, Short.MAX_VALUE).addComponent(jButton[i * 2 + 1], 0, 80, Short.MAX_VALUE);
            hGroup.addGroup(sGroup);
        }
        for (int i = 0; i < 8; i++)
        {
            if (i != 0)
            {
                if (i == 4)
                {
                    vGroup.addGap(5, 5, 5);
                } else
                {
                    vGroup.addGap(2, 2, 2);
                }
            }
            pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            pGroup.addComponent(jLabel[i * 2], 0, 20, 24).addComponent(jButton[i * 2], 0, 20, 24).addGap(0, 15, Short.MAX_VALUE).addComponent(jLabel[i * 2 + 1], 0, 20, 24).addComponent(jButton[i * 2 + 1], 0, 20, 24);
            vGroup.addGroup(pGroup);
        }
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (flag)
        {
            flag = false;
            if (processInformation != null)
            {
                int i = 0;
                int count = processInformation.getCount();
                this.jButton[0].setText(processInformation.getIndex(i) + "");
                this.jButton[1].setText(cpu.time + "");
                if ((cpu.PSW & 3) != 0)
                {
                    this.jButton[2].setText("有");
                    int iv = (cpu.PSW&0xFF00)>>8;
                    switch (iv)
                    {
                        case 9:
                            this.jButton[3].setText("时钟信号中断");
                            break;
                        case 11:
                            this.jButton[3].setText("设备A中断");
                            break;
                        case 12:
                            this.jButton[3].setText("设备B1中断");
                            break;
                        case 13:
                            this.jButton[3].setText("设备B2中断");
                            break;
                        case 14:
                            this.jButton[3].setText("设备C1中断");
                            break;
                        case 15:
                            this.jButton[3].setText("设备C2中断");
                            break;
                        case 0:
                            this.jButton[3].setText("进程结束中断");
                            break;
                        case 1:
                            this.jButton[3].setText("时间片完中断");
                            break;
                         case 10:
                            this.jButton[3].setText("控制台命令");
                            break;
                        default:
                            this.jButton[3].setText(""+iv+"号中断");
                    }
                } else
                {
                    this.jButton[2].setText("无");
                    this.jButton[3].setText("");
                }
                this.jButton[4].setText(cpu.RR + "");
                this.jButton[5].setText(cpu.LR + "");
                this.jButton[6].setText(cpu.AR + "");
                this.jButton[7].setText(cpu.DR + "");
                this.jButton[8].setText(cpu.PC + "");
                int ir = cpu.IR;
                if ((char)(cpu.IR & 0xff) == 'j')
                {
                    this.jButton[9].setText((char) (cpu.IR & 0xff) + "" + (char) (((int) ('0') & 0xff) + ((cpu.IR & 0xffff00) >> 8)));
                } else if ((char)(cpu.IR & 0xff) == '!')
                {
                    this.jButton[9].setText((char) (cpu.IR & 0xff) + "" + (char) ((cpu.IR & 0xff00) >> 8) + "" + (char) (((cpu.IR & 0xff0000) >> 16)));
                }else
                {
                    this.jButton[9].setText((char) (cpu.IR & 0xff) + "" + (char) ((cpu.IR & 0xff00) >> 8) + "" + (char) ((cpu.IR & 0xff0000) >> 16));
                }
                this.jButton[10].setText(cpu.PSW + "");
                this.jButton[11].setText(cpu.AX + "");
                this.jButton[12].setText(cpu.AX + "");
                this.jButton[13].setText(cpu.BX + "");
                this.jButton[14].setText(cpu.CX + "");
                this.jButton[15].setText(cpu.DX + "");
            }
        }
    }

    @Override
    public void refresh()
    {
        flag = true;
       // this.repaint();
    }

    public void setProcessInformation(CPU cpu, ProcessInformation processInformation)
    {
        this.cpu = cpu;
        this.processInformation = processInformation;
    }
}
