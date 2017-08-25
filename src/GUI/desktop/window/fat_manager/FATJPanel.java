package GUI.desktop.window.fat_manager;

import GUI.desktop.window.ApplicationJPanel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 文件分配表面板
 * @author Administrator
 */
public class FATJPanel extends ApplicationJPanel
{

    private JPanel jPanel;
    private JButton[] jButton;
    private FATInformation fatInformation;
    private boolean flag;

    public FATJPanel()
    {
        this.jPanel = new JPanel();
        this.jButton = new JButton[128];
        for (int i = 0; i < jButton.length; i++)
        {
            jButton[i] = new JButton();
            jButton[i].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.WHITE));
            jButton[i].setBackground(Color.GREEN);
            jButton[i].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            jButton[i].setMargin(new java.awt.Insets(0, 0, 0, 0));
            jButton[i].setFont(new java.awt.Font("宋体", 0, 12));
            jButton[i].setToolTipText("" + i);
            this.jButton[i].setToolTipText("<html>" + "<p>"+"当前块号："+i+"</p> "
                            + "<p>"+"下一块号："+0+"</p>" + "</html>");
        }
        initComponents();
    }

    private void initComponents()
    {
        this.setBackground(Color.WHITE);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "文件分配表", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.BLACK));
//        this.setPreferredSize(new java.awt.Dimension(235,120));
//        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE), "CPU", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateContainerGaps(false);
        layout.setAutoCreateGaps(false);
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup pGroup = null;
        GroupLayout.SequentialGroup sGroup = null;
        hGroup.addGap(0, 210, Short.MAX_VALUE);
        for (int i = 0; i < 8; i++)
        {
            sGroup = layout.createSequentialGroup();
//            sGroup.addGap(0,0,Short.MAX_VALUE);
            for (int j = 0; j < 16; j++)
            {
                sGroup.addComponent(jButton[i * 16 + j], 0, 22, Short.MAX_VALUE);
            }
//            sGroup.addGap(0,0,Short.MAX_VALUE);
            hGroup.addGroup(sGroup);
        }
        hGroup.addGap(0, 210, Short.MAX_VALUE);
        vGroup.addGap(0, 0, 1);
        for (int i = 0; i < 8; i++)
        {
            pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
//            pGroup.addGap(0,13,Short.MAX_VALUE);
            for (int j = 0; j < 16; j++)
            {
                pGroup.addComponent(jButton[i * 16 + j], 0, 22, Short.MAX_VALUE);
            }
//            pGroup.addGap(0,13,Short.MAX_VALUE);
            vGroup.addGroup(pGroup);
        }
        vGroup.addGap(0, 0, 1);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
    }

    private void initComponents2()
    {
        this.setBackground(Color.WHITE);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "文件分配表", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        java.awt.GridLayout jPanellayout = new java.awt.GridLayout(8, 16, 0, 0);
        this.jPanel.setLayout(jPanellayout);
        for (int i = 0; i < jButton.length && i < 128; i++)
        {
            this.jPanel.add(jButton[i]);
        }
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        hGroup.addComponent(jPanel, 0, 0, Short.MAX_VALUE);
        vGroup.addComponent(jPanel, 0, 0, Short.MAX_VALUE);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (flag)
        {
//            flag = false;
            if (fatInformation != null)
            {
                for (int i = 0; i < 128; i++)
                {
                    if (fatInformation.get(i) == 0)
                    {
                        this.jButton[i].setBackground(Color.GREEN);
                    } else
                    {
                        this.jButton[i].setBackground(Color.ORANGE);
                    }
                    this.jButton[i].setToolTipText("<html>" + "<p>"+"当前块号："+i+"</p> "
                            + "<p>"+"下一块号："+fatInformation.get(i)+"</p>" + "</html>");


                }
            }
        }
    }

    @Override
    public void refresh()
    {
        if (fatInformation != null)
        {
            flag = true;
//            this.repaint();
        }
    }

    /**
     * @param fatInformation the fatInformation to set
     */
    public void setFatInformation(FATInformation fatInformation)
    {
        this.fatInformation = fatInformation;
    }
}