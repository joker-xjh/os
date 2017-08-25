package GUI.desktop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import javax.swing.GroupLayout;

/**
 * 桌面
 * @author Administrator
 */
public class DesktopJPanel extends javax.swing.JPanel implements java.awt.event.ComponentListener
{

    private javax.swing.JPanel[] desktopIcons;
    private DesktopWindow[] deskWindow;
    private Image desktopImage;

    public DesktopJPanel()
    {
        desktopImage = (new javax.swing.ImageIcon(getClass().getResource("/GUI/image/desktop.jpg"))).getImage();
        this.desktopIcons = new javax.swing.JPanel[170];
        for (int i = 0; i < desktopIcons.length; i++)
        {
            desktopIcons[i] = new javax.swing.JPanel();
            desktopIcons[i].setOpaque(false);
        }
        this.deskWindow = new DesktopWindow[10];
        for (int i = 0; i < deskWindow.length; i++)
        {
            this.deskWindow[i] = new DesktopWindow();
            this.deskWindow[i].setVisible(false);
            this.deskWindow[i].setOpaque(false);
        }
        initComponents();
    }

    private void initComponents()
    {
        int row = (this.getHeight()) / 75;
        int col = (this.getWidth()) / 75 - 9 - 6;
        if(col<=0) col=1;
        this.removeAll();
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup pGroup = null;
        //横向分为序列组
        for (int i = 0; i < col; i++)
        {
            pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int j = 0; j < row; j++)
            {
                pGroup.addComponent(this.desktopIcons[i * row + j], 75, 75, 75);
            }
            hGroup.addGroup(pGroup);
        }
        hGroup.addGap(0, 0, Short.MAX_VALUE)
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                   .addGroup(layout.createSequentialGroup()
                        .addComponent(this.deskWindow[5], 0, 430, 430)
                        .addComponent(this.deskWindow[4], 0, 170, 170)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(this.deskWindow[3], 0, 600, 600)
                    )
                    .addComponent(this.deskWindow[6], 0, 600, 600)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.deskWindow[1], 0, 360, 360)
                    .addComponent(this.deskWindow[2], 0, 360, 360)
              )
              .addComponent(this.deskWindow[0], 0, 80, 80);
        for (int j = 0; j < row; j++)
        {
            pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int i = 0; i < col; i++)
            {
                pGroup.addComponent(this.desktopIcons[i * row + j], 75, 75, 75);
            }
            vGroup.addGroup(pGroup);
        }
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(vGroup)
                    .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(this.deskWindow[5], 0, 520, 520)
                                    .addComponent(this.deskWindow[3], 0, 500, 500)
                                    .addComponent(this.deskWindow[4], 0, 500, 500)
                                    .addGap(0, 0, Short.MAX_VALUE)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(this.deskWindow[6], 0, 230, 230)
                                    .addGap(0, 0, Short.MAX_VALUE)
                                )
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(this.deskWindow[1], 0, 500, 500)
                        .addComponent(this.deskWindow[2], 0, 230, 230)
                        .addGap(0, 0, Short.MAX_VALUE)
                    )
                    .addComponent(this.deskWindow[0], 0, 770, Short.MAX_VALUE)
                    .addGap(0, 0, Short.MAX_VALUE)
                );
        this.repaint();
    }

    public void addComponent(int index, Component comp)
    {
        if (0 <= index && index < desktopIcons.length && desktopIcons[index] != null)
        {
            this.desktopIcons[index].removeAll();
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(desktopIcons[index]);
            desktopIcons[index].setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup().addGap(5).addComponent(comp).addGap(5));
            layout.setVerticalGroup(layout.createSequentialGroup().addGap(5).addComponent(comp).addGap(5));
        }
    }

    public DesktopWindow getDesktopWindow(int index)
    {
        if (0 <= index && index < this.deskWindow.length)
        {
            return this.deskWindow[index];
        } else
        {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (desktopImage != null)
        {
            g.drawImage(desktopImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
//            this.setSize(this.getParent().getSize());
        initComponents();
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
