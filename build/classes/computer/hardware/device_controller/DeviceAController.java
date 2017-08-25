package computer.hardware.device_controller;

import computer.hardware.Power;
import computer.hardware.abstract_hardware.DeviceController;
import computer.hardware.bus.AddressBus;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.InterruptBus;

/**
 * 设备A控制器
 * @author Administrator
 */
public class DeviceAController extends DeviceController
{

    private boolean open;
    private int sleepTime;
    
    public DeviceAController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus)
    {
        this(power, addressBus, dataBus, interruptBus, 4);
    }

    public DeviceAController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, interruptBus, registerSize);
        this.open = false;
        this.sleepTime = 1;
    }

    @Override
    public void write()
    {
        int command;
        super.write();
        command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
        this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
        executiveCommand(command);
    }

    private void executiveCommand(int command)
    {
        switch (command)
        {

            case DeviceAController.COMMAND_OPEN:
                this.open = true;
                this.register[DeviceAController.REGISTER_STATUS] |= DeviceAController.STATUS_WORK;
                break;
            case DeviceAController.COMMAND_CLOSE:
                this.open = false;
                this.register[DeviceAController.REGISTER_STATUS] &= ~(DeviceAController.STATUS_WORK);
                break;
            case DeviceAController.COMMAND_READ:
                if (this.open)
                {
                    if ((--this.register[DeviceAController.REGISTER_DATA]) <= 0)
                    {
                        this.INTR();
                    }
                }
                break;
            case DeviceAController.COMMAND_WRITE:
                if (this.open)
                {
                    if ((--this.register[DeviceAController.REGISTER_DATA]) <= 0)
                    {
                        this.INTR();
                    }
                }
                break;
        }
    }

    @Override
    public void run()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param sleeepTime the sleeepTime to set
     */
    public void setSleeepTime(int sleeepTime)
    {
        this.sleepTime = sleeepTime;
    }
}
//    @Override
//    public void run()
//    {
//        int command;
//        while (this.power.isOpen())
//        {
//            command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
//            this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
//            switch (command)
//            {
//                case HardDiskController.COMMAND_OPEN:
//                    this.open = true;
//                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_WORK;
//                    break;
//                case HardDiskController.COMMAND_CLOSE:
//                    this.open = false;
//                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_WORK);
//                    break;
//            }
//            if (open)
//            {
//                this.INTR();
//            }
//            try
//            {
//                java.lang.Thread.sleep(this.sleepTime);
//            } catch (InterruptedException ex)
//            {
//                util.out.println("DeviceAController-run:睡眠被中断！");
//            }
//        }
//   }