package GUI.desktop.window.Console;

import GUI.desktop.window.ApplicationJPanel;
import GUI.util.TableJPanel;
import computer.hardware.cpu.CPUInterface;
import computer.hardware.timer.TimerInterface;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

/**
 * 设备管理面板
 * @author Administrator
 */
public class ConsoleJPanel extends ApplicationJPanel
{

    private TableJPanel table2;
    private TableJPanel table1;
    private CPUInterface cpu;
    private TimerInterface timer;

    public ConsoleJPanel()
    {
        this.table2 = new TableJPanel(4, 2, "", new String[]
                {
                    "名称", "内容"
                });
        this.table1 = new TableJPanel(7, 6, "CPU与时钟", new String[]
                {
                    "名称", "当前方式", "切换方式", "手动", "睡眠时间", "更改睡眠"
                });
        this.table1.setCellString(0, 0, "CPU");
        this.table1.setCellString(1, 0, "时钟");
        this.table1.setCellString(0, 1, "自动");
        this.table1.setCellString(1, 1, "自动");
        this.table1.setCellString(0, 2, "切换手动");
        this.table1.setCellString(1, 2, "切换手动");
        this.table1.setCellColor(0, 2, Color.GREEN);
        this.table1.setCellColor(1, 2, Color.GREEN);
        this.table1.setCellString(0, 3, "执行指令");
        this.table1.setCellString(1, 3, "发送脉冲");
        this.table1.getCell(0, 3).setEnabled(false);
        this.table1.getCell(1, 3).setEnabled(false);
        this.table1.setCellColor(0, 3, Color.GREEN);
        this.table1.setCellColor(1, 3, Color.GREEN);
        this.table1.setCellString(0, 5, "单击设置");
        this.table1.setCellString(1, 5, "单击设置");
        this.table1.setCellString(2, 5, "重启系统");
        this.table1.setCellColor(0, 5, Color.GREEN);
        this.table1.setCellColor(1, 5, Color.GREEN);
        this.table1.setCellColor(2, 5, Color.GREEN);
        this.table2.setCellString(0, 0, "绝对时钟");
        this.table2.setCellString(1, 0, "当前指令");
        this.table2.setCellString(2, 0, "处理事件");
        this.table2.setCellString(3, 0, "执行结果");
        initComponents();
        addListener();
    }

    private void initComponents()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "控制面板", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        this.table1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0)));
        this.table2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        hGroup.addComponent(table1, 0, 430, Short.MAX_VALUE);
        hGroup.addComponent(table2, 0, 170, Short.MAX_VALUE);
        vGroup.addComponent(table1, 0, 200, Short.MAX_VALUE);
        vGroup.addComponent(table2, 0, 200, Short.MAX_VALUE);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

    }

    private void addListener()
    {
        this.table1.getCell(0, 2).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (cpu.isRun())
                {
                    cpu.STOP();
                    table1.getCell(0, 3).setEnabled(true);
                } else
                {
                    table1.getCell(0, 3).setEnabled(false);
                    cpu.START();
                }
            }
        });
        this.table1.getCell(1, 2).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (timer.isRun())
                {
                    timer.STOP();
                    table1.getCell(1, 3).setEnabled(true);
                } else
                {
                    table1.getCell(1, 3).setEnabled(false);
                    timer.START();
                }
            }
        });
        this.table1.getCell(0, 3).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!cpu.isRun())
                {
                    cpu.CLK();
                }
            }
        });
        this.table1.getCell(1, 3).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!timer.isRun())
                {
                    timer.CLK();
                }
            }
        });
        this.table1.getCell(0, 5).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int time = -1;
                String input = JOptionPane.showInputDialog(null, "请输入睡眠时间,单位为毫秒(范围0-2000)", "1000");
                if (input != null)
                {
                    Scanner scanner = new Scanner(input);
                    if (scanner.hasNextInt())
                    {
                        time = scanner.nextInt();
                        if (0 <= time && time <= 2000)
                        {
                            cpu.setIPS(time);
                        } else
                        {
                            time = -1;
                        }
                    }
                    if (time < 0)
                    {
                        JOptionPane.showMessageDialog(null, "对不起设置有误！", "错误", JOptionPane.ERROR_MESSAGE);
                    } else
                    {
                        JOptionPane.showMessageDialog(null, "设置成功！", "信息", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                refresh();
            }
        });
        this.table1.getCell(1, 5).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int time = -1;
                String input = JOptionPane.showInputDialog(null, "请输入睡眠时间,单位为毫秒(范围0-2000)", "1000");
                if (input != null)
                {
                    Scanner scanner = new Scanner(input);
                    if (scanner.hasNextInt())
                    {
                        time = scanner.nextInt();
                        if (0 <= time && time <= 2000)
                        {
                            timer.setSleeepTime(time);
                        } else
                        {
                            time = -1;
                        }
                    }
                    if (time < 0)
                    {
                        JOptionPane.showMessageDialog(null, "对不起设置有误！", "错误", JOptionPane.ERROR_MESSAGE);
                    } else
                    {
                        JOptionPane.showMessageDialog(null, "设置成功！", "信息", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                refresh();
            }
        });
        this.table1.getCell(2, 5).addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                cpu.init();
                refresh();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
//        super.paintComponent(g);
    }

    @Override
    public void refresh()
    {
        if ((cpu.getPSW() & 3) != 0)
        {
            int iv = (cpu.getPSW() & 0xFF00) >> 8;
            switch (iv)
            {
                case 9:
                    this.table2.getCell(2, 1).setText("时钟信号中断");
                    break;
                case 11:
                    this.table2.getCell(2, 1).setText("设备A中断");
                    break;
                case 12:
                    this.table2.getCell(2, 1).setText("设备B1中断");
                    break;
                case 13:
                    this.table2.getCell(2, 1).setText("设备B2中断");
                    break;
                case 14:
                    this.table2.getCell(2, 1).setText("设备C1中断");
                    break;
                case 15:
                    this.table2.getCell(2, 1).setText("设备C2中断");
                    break;
                case 0:
                    this.table2.getCell(2, 1).setText("进程结束中断");
                    break;
                case 1:
                    this.table2.getCell(2, 1).setText("时间片完中断");
                    break;
                case 10:
                    this.table2.getCell(2, 1).setText("控制台命令");
                    break;
                default:
                    this.table2.getCell(2, 1).setText("" + iv + "号中断");
            }
        } else
        {
            this.table2.getCell(2, 1).setText("");
        }
        int ir = cpu.getIR();
        if ((char) (cpu.getIR() & 0xff) == 'j')
        {
            this.table2.getCell(1, 1).setText((char) (cpu.getIR() & 0xff) + "" + (char) (((int) ('0') & 0xff) + ((cpu.getIR() & 0xffff00) >> 8)));
        } else if ((char) (cpu.getIR() & 0xff) == '!')
        {
            this.table2.getCell(1, 1).setText((char) (cpu.getIR() & 0xff) + "" + (char) ((cpu.getIR() & 0xff00) >> 8) + "" + (char) (((cpu.getIR() & 0xff0000) >> 16)));
        } else
        {
            this.table2.getCell(1, 1).setText((char) (cpu.getIR() & 0xff) + "" + (char) ((cpu.getIR() & 0xff00) >> 8) + "" + (char) ((cpu.getIR() & 0xff0000) >> 16));
        }
        this.table2.getCell(0, 1).setText("" + timer.getTime());
        this.table2.getCell(3, 1).setText("" + cpu.getAX());
        this.table1.setCellString(0, 4, "" + cpu.getIPS() + "毫秒");
        this.table1.setCellString(1, 4, "" + timer.getSleeepTime() + "毫秒");
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(CPUInterface cpu)
    {
        this.cpu = cpu;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(TimerInterface timer)
    {
        this.timer = timer;
    }
}
