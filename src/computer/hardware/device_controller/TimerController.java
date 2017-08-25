package computer.hardware.device_controller;

import computer.hardware.bus.InterruptBus;
import computer.hardware.Power;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.abstract_hardware.DeviceController;
import computer.hardware.timer.TimerInterface;

/**
 * 时钟
 * @author Administrator
 */
public class TimerController extends DeviceController implements TimerInterface
{

    private boolean isRun;
    private int sleeepTime;

    public TimerController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus)
    {
        this(power, addressBus, dataBus, interruptBus, 4);
    }

    public TimerController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, interruptBus, registerSize);
        this.isRun = true;
        this.sleeepTime = 1000;
        register[REGISTER_CONTROL_ARGUMENT] = 0;
        register[REGISTER_DATA] = 0;
    }

    @Override
    public void run()
    {
        int command;
        while (this.power.isOpen())
        {
            command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
            this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
            switch (command)
            {
                case HardDiskController.COMMAND_OPEN:
                    this.isRun = true;
                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_WORK;
                    break;
                case HardDiskController.COMMAND_CLOSE:
                    this.isRun = false;
                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_WORK);
                    break;
            }
            if (isRun && INTR())
            {
                register[TimerController.REGISTER_CONTROL_ARGUMENT]++;
                if (register[TimerController.REGISTER_CONTROL_ARGUMENT] == 0)
                {
                    register[TimerController.REGISTER_DATA]++;
                }
            }
            try
            {
                java.lang.Thread.sleep(this.sleeepTime);
            } catch (InterruptedException ex)
            {
                util.out.println("TimerController-run:睡眠被中断！");
            }
        }
    }

    /**
     * @param sleeepTime the sleeepTime to set
     */
    @Override
    public void setSleeepTime(int sleeepTime)
    {
        this.sleeepTime = sleeepTime;
    }

    @Override
    public int getTime()
    {
        return (((0xFF & register[REGISTER_DATA]) << 8) | (0xFF & register[REGISTER_CONTROL_ARGUMENT]));
    }

    @Override
    public void CLK()
    {
        if ((!isRun) && INTR())
        {
            register[TimerController.REGISTER_CONTROL_ARGUMENT]++;
            if (register[TimerController.REGISTER_CONTROL_ARGUMENT] == 0)
            {
                register[TimerController.REGISTER_DATA]++;
            }
        }
    }

    @Override
    public void RESET()
    {
        this.isRun = false;
        this.sleeepTime = 1000;
        register[REGISTER_CONTROL_ARGUMENT] = 0;
        register[REGISTER_DATA] = 0;
    }

    @Override
    public void START()
    {
        this.isRun = true;
    }

    @Override
    public void STOP()
    {
        this.isRun = false;
    }

    @Override
    public boolean isRun()
    {
        return isRun;
    }

    @Override
    public int getSleeepTime()
    {
        return this.sleeepTime;
    }
}
