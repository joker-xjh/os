package GUI.desktop;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

/**
 * 桌面图标
 * @author Administrator
 */
public class DesktopIcon extends javax.swing.JPanel
{

    private javax.swing.JButton jButton;
    private javax.swing.JLabel jLabel;

    public DesktopIcon()
    {
        this(null);
    }

    public DesktopIcon(String name)
    {
        initComponents();
        this.jLabel.setText(name);
    }

    private void initComponents()
    {
        this.setOpaque(false);
        jLabel = new javax.swing.JLabel();
        jButton = new javax.swing.JButton();
        jLabel.setMaximumSize(new java.awt.Dimension(75, 25));
        jLabel.setMinimumSize(new java.awt.Dimension(75, 25));
        jLabel.setPreferredSize(new java.awt.Dimension(75, 25));
        jLabel.setForeground(Color.BLACK);
        jLabel.setFont(new java.awt.Font("宋体", 0, 12));
        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jButton.setMaximumSize(new java.awt.Dimension(55, 50));
        jButton.setMinimumSize(new java.awt.Dimension(55, 50));
        jButton.setPreferredSize(new java.awt.Dimension(55, 50));
        jButton.setFont(new java.awt.Font("宋体", 0, 12));
        jButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton.setContentAreaFilled(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(7)
                    .addComponent(jButton, 0, 55, Short.MAX_VALUE).addGap(8))
                    .addComponent(jLabel, 0, 75, Short.MAX_VALUE)
                );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGap(50)
                    .addComponent(jButton, 0, 50, Short.MAX_VALUE)
                    .addGap(50))
                    .addComponent(jLabel, 0, 20, Short.MAX_VALUE)
                );
    }

    public void setIcon(Icon icon)
    {
        this.jButton.setIcon(icon);
    }

    public void setText(String name)
    {
        this.jLabel.setText(name);
    }
    
    public void addActionListener(ActionListener l)
    {
        this.jButton.addActionListener(l);
    }
}
