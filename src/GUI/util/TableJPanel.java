package GUI.util;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JButton;

/**
 *
 * @author Administrator
 */
public class TableJPanel extends javax.swing.JPanel
{

    private int row;
    private int column;
    private String name;
    private JButton[] titleJButton;
    private JButton[][] cellJButton;

    public TableJPanel()
    {
        this(10, 3, "设备等待队列", new String[]
                {
                    "设备A", "设备B", "设备C"
                });
    }

    public TableJPanel(int row, int column, String tableName, String[] titleName)
    {
        this.row = row;
        this.column = column;
        this.name = tableName;
        this.titleJButton = new JButton[column];
        this.cellJButton = new JButton[row][column];
        for (int i = 0; i < titleJButton.length; i++)
        {
            this.titleJButton[i] = new JButton();
            this.titleJButton[i].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.YELLOW));
            this.titleJButton[i].setBackground(Color.ORANGE);
            this.titleJButton[i].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            this.titleJButton[i].setMargin(new java.awt.Insets(0, 0, 0, 0));
            this.titleJButton[i].setFont(new java.awt.Font("宋体", 0, 12));
        }
        for (int i = 0; i < this.cellJButton.length; i++)
        {
            for (int j = 0; j < this.cellJButton[i].length; j++)
            {
                this.cellJButton[i][j] = new JButton();
                this.cellJButton[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE));
                this.cellJButton[i][j].setBackground(Color.WHITE);
                this.cellJButton[i][j].setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                this.cellJButton[i][j].setMargin(new java.awt.Insets(0, 0, 0, 0));
                this.cellJButton[i][j].setFont(new java.awt.Font("宋体", 0, 12));
            }
        }
        for (int i = 0; i < this.titleJButton.length; i++)
        {
            if (i < titleName.length)
            {
                this.titleJButton[i].setText(titleName[i]);
            } else
            {
                this.titleJButton[i].setText("");
            }
        }
        initComponents();
    }

    private void initComponents()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), name, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        GroupLayout.ParallelGroup pGroup = null;
        GroupLayout.SequentialGroup sGroup = null;

        pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        sGroup = layout.createSequentialGroup();
        for (int i = 0; i < this.titleJButton.length; i++)
        {
            sGroup.addComponent(titleJButton[i], 0, 50, Short.MAX_VALUE);
        }
        pGroup.addGroup(sGroup);
        for (int i = 0; i < this.cellJButton.length; i++)
        {
            sGroup = layout.createSequentialGroup();
            for (int j = 0; j < this.cellJButton[i].length; j++)
            {
                sGroup.addComponent(cellJButton[i][j], 0, 50, Short.MAX_VALUE);
            }
            pGroup.addGroup(sGroup);
        }
        layout.setHorizontalGroup(pGroup);

        sGroup = layout.createSequentialGroup();
        pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (int i = 0; i < this.titleJButton.length; i++)
        {
            pGroup.addComponent(titleJButton[i], 0, 25, Short.MAX_VALUE);
        }
        sGroup.addGroup(pGroup);
        for (int i = 0; i < this.cellJButton.length; i++)
        {
            pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int j = 0; j < this.cellJButton[i].length; j++)
            {
                pGroup.addComponent(cellJButton[i][j], 0, 25, Short.MAX_VALUE);
            }
            sGroup.addGroup(pGroup);
        }
        layout.setVerticalGroup(sGroup);


    }

    @Override
    protected void paintComponent(Graphics g)
    {
    }

    public void init()
    {
        for (int i = 0; i < this.cellJButton.length; i++)
        {
            for (int j = 0; j < this.cellJButton[i].length; j++)
            {
                this.setCellString(i, j, "");
            }
        }
    }

    public void refresh()
    {
//        this.repaint();
    }

    public void setCellString(int r, int c, String string)
    {
        this.cellJButton[r][c].setText(string);
    }

    public void setCellColor(int r, int c, Color color)
    {
        this.cellJButton[r][c].setBackground(color);
    }

    public JButton getCell(int r, int c)
    {
        return this.cellJButton[r][c];
    }

    public void setTitle(int c,String title)
    {
        this.titleJButton[c].setText(title);
    }
}
