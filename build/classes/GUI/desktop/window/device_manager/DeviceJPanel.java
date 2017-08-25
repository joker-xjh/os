package GUI.desktop.window.device_manager;

import GUI.desktop.window.ApplicationJPanel;
import GUI.util.TableJPanel;
import java.awt.Graphics;
import javax.swing.GroupLayout;

/**
 * 设备管理面板
 * @author Administrator
 */
public class DeviceJPanel extends ApplicationJPanel
{

    private TableJPanel deviceWaitQueue;
    private TableJPanel deviceAllocateTable;
    private DeviceInformation deviceInformation;
    private boolean flag;

    public DeviceJPanel()
    {
        this.deviceWaitQueue = new TableJPanel(10, 3, "设备等待队列", new String[]
                {
                    "设备A", "设备B", "设备C"
                });
        this.deviceAllocateTable = new TableJPanel(5, 3, "设备分配表", new String[]
                {
                    "设备名称", "进程PID", "剩余时间"
                });
        this.deviceAllocateTable.setCellString(0, 0, "设备A");
        this.deviceAllocateTable.setCellString(1, 0, "设备B1");
        this.deviceAllocateTable.setCellString(2, 0, "设备B2");
        this.deviceAllocateTable.setCellString(3, 0, "设备C1");
        this.deviceAllocateTable.setCellString(4, 0, "设备C2");
        initComponents();
    }

    private void initComponents()
    {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "设备管理", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.ORANGE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(deviceAllocateTable).addComponent(deviceWaitQueue));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(deviceAllocateTable).addComponent(deviceWaitQueue));
    }

    public void init()
    {
        this.deviceAllocateTable.init();
        this.deviceAllocateTable.setCellString(0,0,"A0");
        this.deviceAllocateTable.setCellString(1,0,"B1");
        this.deviceAllocateTable.setCellString(2,0,"B2");
        this.deviceAllocateTable.setCellString(3,0,"C1");
        this.deviceAllocateTable.setCellString(4,0,"C2");
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        flag=true;
        if (flag)
        {
            flag = false;
            if (deviceInformation != null)
            {
                this.init();
                this.set(0, this.deviceInformation.getDeviceAllocateTable().get(0, 0),this.deviceInformation.getRemainTime(0,0));
                this.set(1, this.deviceInformation.getDeviceAllocateTable().get(1, 0),this.deviceInformation.getRemainTime(1,0));
                this.set(2, this.deviceInformation.getDeviceAllocateTable().get(1, 1),this.deviceInformation.getRemainTime(1,1));
                this.set(3, this.deviceInformation.getDeviceAllocateTable().get(2, 0),this.deviceInformation.getRemainTime(2,0));
                this.set(4, this.deviceInformation.getDeviceAllocateTable().get(2, 1),this.deviceInformation.getRemainTime(2,1));
                this.deviceWaitQueue.init();
                for (int i = 0; i < this.deviceInformation.getDeviceWaitQueues().length; i++)
                {
                    for (int j = 0; j < this.deviceInformation.getDeviceWaitQueues()[i].getLength(); j++)
                    {
                        this.deviceWaitQueue.setCellString(j, i, "" + this.deviceInformation.getDeviceWaitQueues()[i].get(j));
                    }
                }

            }
        }
    }

    private void set(int i, int pid,int remainTime)
    {
        if (pid == -1 || pid==0)
        {
            this.deviceAllocateTable.setCellString(i, 1, "");
            this.deviceAllocateTable.setCellString(i, 2, "空闲");
        } else if(remainTime>=0)
        {
            this.deviceAllocateTable.setCellString(i, 1, "" + pid);
            this.deviceAllocateTable.setCellString(i, 2, ""+remainTime);
        }else
        {
            this.deviceAllocateTable.setCellString(i, 1, "" + pid);
            this.deviceAllocateTable.setCellString(i, 2, "准备");
        }
    }

    @Override
    public void refresh()
    {
        flag = true;
//        this.repaint();
    }

    /**
     * @param deviceInformation the deviceInformation to set
     */
    public void setDeviceInformation(DeviceInformation deviceInformation)
    {
        this.deviceInformation = deviceInformation;
    }
}
