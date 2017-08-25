package GUI.desktop.window.memory_manager;

import GUI.desktop.window.ApplicationJPanel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class MemoryJPanel extends ApplicationJPanel
{

    private JLabel jLabel0;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton jButton1;
    private JButton jButton2;
    private MemoryInformation memoryInformation;
    private StringBuilder toolTipText;
    private boolean flag;

    public MemoryJPanel()
    {
        flag = true;
        toolTipText=new StringBuilder();
        this.jLabel0 = new JLabel("内存使用情况");
        this.jLabel1 = new JLabel("未使用");
        this.jLabel2 = new JLabel("已使用");
        this.jButton1 = new JButton("1024");
        this.jButton2 = new JButton("1024");
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setFont(new java.awt.Font("宋体", 0, 12));
        jButton2.setFont(new java.awt.Font("宋体", 0, 12));
        this.jLabel0.setFont(new java.awt.Font("宋体", 0, 12));
        this.jLabel1.setFont(new java.awt.Font("宋体", 0, 12));
        this.jLabel2.setFont(new java.awt.Font("宋体", 0, 12));
        jLabel0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.jButton1.setBackground(Color.GREEN);
        this.jButton2.setBackground(Color.ORANGE);
        this.jButton1.setBorderPainted(false);
        this.jButton2.setBorderPainted(false);
        initComponents();
    }

    private void initComponents()
    {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel0, 0, 75, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, 0, 45,45)
                        .addGap(2,2,2)
                        .addComponent(jButton1, 0, 30,Short.MAX_VALUE)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, 0, 45,45)
                        .addGap(2,2,2)
                        .addComponent(jButton2, 0, 30,Short.MAX_VALUE)
                    )
                    .addGap(0, 75, Short.MAX_VALUE)
                );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(jLabel0, 15, 15, 15)
                    .addGap(1,1,1)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel1, 15, 15, 15)
                        .addComponent(jButton1, 15, 15, 15)
                    )
                    .addGap(1,1,1)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel2, 15, 15, 15)
                        .addComponent(jButton2, 15, 15, 15)
                    )
                    .addGap(0, 700, Short.MAX_VALUE)
                );
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        int uCount = 0, nCount = 0;
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, w - 1, h - 1);
        g.setColor(Color.GRAY);
        g.drawLine(0, 15, w, 15);
        g.drawLine(0, 31, w, 31);
        g.drawLine(0, 47, w, 47);
        g.drawLine(46, 16, 46, 47);
        g.drawRect(5, 53, w - 10, h - 58);
        if (flag)
        {
//            flag = false;
            if (memoryInformation != null)
            {
                toolTipText.delete(0,toolTipText.length());
                toolTipText.append("<html>"+"<table border=\"1\">");
                toolTipText.append("<tr>"+"<td>内存标号</td>"+"<td>起始地址</td>"+"<td>内存大小</td>"+"<td>状态</td>"+"</tr>");
                int count = memoryInformation.getCount();
                for (int i = 0; i < count; i++)
                {
                    if (memoryInformation.getUse(i) == 1)
                    {
                        uCount += memoryInformation.getLength(i);
                        g.setColor(Color.ORANGE);
                        this.paintRect(g, memoryInformation.getStartAddress(i), memoryInformation.getLength(i));
                        this.printItem(memoryInformation.getIndex(i),memoryInformation.getStartAddress(i),memoryInformation.getLength(i),true);
                    } else
                    {
                        nCount += memoryInformation.getLength(i);
                        g.setColor(Color.GREEN);
                        this.paintRect(g, memoryInformation.getStartAddress(i), memoryInformation.getLength(i));
                        this.printItem(memoryInformation.getIndex(i),memoryInformation.getStartAddress(i),memoryInformation.getLength(i),false);
                    }
                }
                toolTipText.append("</table>"+"</html>");
                this.setToolTipText(toolTipText.toString());
                for (int i = 0; i < count; i++)
                {

                    this.paintLine(g, memoryInformation.getStartAddress(i), memoryInformation.getLength(i));

                }
                this.jButton1.setText("" + nCount);
                this.jButton2.setText("" + uCount);
            }else
            {
                this.setToolTipText("");
            }
        }
    }
    
    private void printItem(int index,int startAddress,int length,boolean isUse)
    {
         int count;
         toolTipText.append("<tr>");
         toolTipText.append(("<td>"+index+"</td>"));
         toolTipText.append(("<td>"+startAddress+"</td>"));
         toolTipText.append(("<td>"+length+"</td>"));
         if(isUse)
         {
             toolTipText.append(("<td>使用</td>"));
         }else
         {
             toolTipText.append(("<td>空闲</td>"));
         }
         toolTipText.append("</tr>");
    }

    private void paintRect(Graphics g,int startAddress,int length)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        int x = 5;
        int y = h - (startAddress + length) * (h - 58) / 2048 - 5;
        g.fillRect(x, y, w - 10, length * (h - 58) / 2048+1);
    }
    
    private void paintLine(Graphics g,int startAddress,int length)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        int x = 5;
        int y = h - (startAddress + length) * (h - 58) / 2048 - 5;
        g.setColor(Color.BLACK);
        g.drawLine(5, y, w - 5, y);
    }

    public void setMemoryInformation(MemoryInformation memoryInformation)
    {
        this.memoryInformation = memoryInformation;
    }

    @Override
    public void refresh()
    {
        if (memoryInformation != null)
        {
            flag = true;
//            this.repaint();
        }
    }
}
