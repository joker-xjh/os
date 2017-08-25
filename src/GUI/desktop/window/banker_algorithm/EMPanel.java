package GUI.desktop.window.banker_algorithm;

import GUI.desktop.window.ApplicationJPanel;
import GUI.util.TableJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 *
 * @author Administrator
 */
public class EMPanel extends  ApplicationJPanel
{

    private EquipmentManagement em;
    private TableJPanel[] panel = new TableJPanel[3];
    private JButton[] button = new JButton[3];
    Thread thread;
    private int isstop;

    public EMPanel()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "银行家算法", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        this.setLayout(new BorderLayout());
        panel[0] = new TableJPanel(2, 4, "设备管理", new String[]
                {
                    "", "A  设  备", "B  设  备", "C  设  备"
                });
        panel[0].setCellString(0, 0, "总  数  量");
        panel[0].setCellString(1, 0, "剩  余  量");
        for (int i = 1; i < 4; i++)
        {
            panel[0].setCellString(0, i, "40");
            panel[0].setCellString(1, i, "40");
        }
        add(panel[0], BorderLayout.NORTH);
        panel[1] = new TableJPanel(15, 4, "进程队列", new String[]
                {
                    "序号", "A设备", "B设备", "C设备"
                });
        add(panel[1], BorderLayout.WEST);
        panel[2] = new TableJPanel(15, 4, "设备分配队列", new String[]
                {
                    "序号", "A设备", "B设备", "C设备"
                });
        add(panel[2], BorderLayout.EAST);
        button[0] = new JButton("初始化");
        button[1] = new JButton("开  始");
        button[2] = new JButton("暂  停");
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        p.setOpaque(false);
        for (int i = 0; i < 3; i++)
        {
            button[i].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.YELLOW));
            button[i].setBackground(Color.GREEN);
            button[i].setPreferredSize(new Dimension(70, 25));
            button[i].setFont(new java.awt.Font("宋体", 1, 15));
            p.add(button[i]);
        }
        add(p, BorderLayout.SOUTH);
        button[1].setEnabled(false);
        button[2].setEnabled(false);
        this.isstop = 0;

        button[0].addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                getButton()[1].setEnabled(true);
                getButton()[2].setEnabled(false);
                isstop = -1;
                setEm(new EquipmentManagement());
                for (int i = 1; i < 4; i++)
                {
                    getPanel()[0].setCellString(0, i, "40");
                    getPanel()[0].setCellString(1, i, "40");
                }
                for (int i = 0; i < 15; i++)
                {
                    getPanel()[1].setCellString(i, 0, "" + (i + 1));
                    getPanel()[1].setCellColor(i, 0, Color.WHITE);
                    getPanel()[2].setCellString(i, 0, "");
                    for (int j = 1; j < 4; j++)
                    {
                        getPanel()[1].setCellString(i, j, "" + getEm().getClaim(i)[j - 1]);
                        getPanel()[2].setCellString(i, j, "");
                    }
                }
                StartRun r = new StartRun(EMPanel.this);
                thread = new Thread(r);
            }
        });

        button[1].addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                getButton()[2].setEnabled(true);
                getButton()[2].setText("暂  停");
                getButton()[1].setEnabled(false);
                isstop = 0;
                thread.start();
            }
        });

        button[2].addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (getIsstop() == 0)
                {
                    setIsstop(1);
                    getButton()[2].setText("继  续");
                } else
                {
                    setIsstop(0);
                    getButton()[2].setText("暂  停");
                }
            }
        });
    }

    /**
     * @return the isstop
     */
    public int getIsstop()
    {
        return isstop;
    }

    /**
     * @param isstop the isstop to set
     */
    public void setIsstop(int isstop)
    {
        this.isstop = isstop;
    }

    /**
     * @return the em
     */
    public EquipmentManagement getEm()
    {
        return em;
    }

    /**
     * @param em the em to set
     */
    public void setEm(EquipmentManagement em)
    {
        this.em = em;
    }

    /**
     * @return the panel
     */
    public TableJPanel[] getPanel()
    {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(TableJPanel[] panel)
    {
        this.panel = panel;
    }

    /**
     * @return the button
     */
    public JButton[] getButton()
    {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(JButton[] button)
    {
        this.button = button;
    }

    @Override
    public void refresh()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
