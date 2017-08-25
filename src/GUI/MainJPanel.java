package GUI;

import GUI.desktop.DesktopJPanel;
import GUI.desktop.DesktopWindow;
import GUI.desktop.window.Application;
import java.awt.Color;
import java.awt.Component;

/**
 * 主面板
 * @author Administrator
 */
public class MainJPanel extends javax.swing.JPanel
{
    private DesktopJPanel desktop;
    private javax.swing.JPanel toolbar;
    private int count;
    public MainJPanel()
    {
        initComponents();
        this.count=0;
        this.setOpaque(false);
        this.toolbar.setOpaque(false);
    }

    private void initComponents()
    {
        this.desktop = new DesktopJPanel();
        this.toolbar = new javax.swing.JPanel();
        this.toolbar.setBackground(Color.WHITE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(desktop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolbar, 30, 30, 30)
                )
        );
    }
    
    private void initToolbar()
    {
        
    }

    public void addComponentListen()
    {
        this.desktop.addComponentListener(desktop);
    }

    private void addComponent(int index, Component comp)
    {
        desktop.addComponent(index,comp);
    }

    private DesktopWindow getDesktopWindow(int index)
    {
        return this.desktop.getDesktopWindow(index);
    }

    public void addApplication(Application app)
    {
       app.setJInternalFrame(this.getDesktopWindow(count));
       this.addComponent(count,app.getDesktopIcon());
       count++;
    }
}
