package computer.hardware.device_controller;

import computer.hardware.bus.InterruptBus;
import computer.hardware.Power;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.abstract_hardware.DeviceController;

/**
 * 中断控制器
 * @author Administrator
 */
public class InterruptController extends DeviceController
{

    private int status;
    private int command;
    private int currentInterrupt;
    private InterruptBus outerInterruptBus;

    public InterruptController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus innerInterruptBus, InterruptBus outerInterruptBus)
    {
        this(power, addressBus, dataBus, innerInterruptBus, outerInterruptBus, 4);
    }

    public InterruptController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus innerInterruptBus, InterruptBus outerInterruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, innerInterruptBus, registerSize);
        this.outerInterruptBus = outerInterruptBus;
        this.status = 0;
        this.command = InterruptController.COMMAND_NULL;
        this.currentInterrupt = -1;
    }

    @Override
    public void run()
    {
        while (this.power.isOpen())
        {
            if (currentInterrupt < 0)
            {
                for (int i = 0; i < 16; i++)
                {
                    if ((outerInterruptBus.getInterVector() & (1 << i)) != 0)
                    {
                        this.currentInterrupt = i;
                        this.INTR();
                        break;
                    }
                }
            } else if (command == InterruptController.COMMAND_END_INTERRUPT)
            {
                command = InterruptController.COMMAND_NULL;
                this.outerInterruptBus.INTA(currentInterrupt);
                currentInterrupt = -1;
            }
            try
            {
                java.lang.Thread.sleep(1);
            } catch (InterruptedException ex)
            {
                util.out.println("InterruptController-run:睡眠被中断！");
            }
        }
    }

    @Override
    public void INTA()
    {
        if (this.status == 0)
        {
            super.INTA();
            util.NumberTool.intToByteArray(currentInterrupt, dataBus.getBus());
            this.status++;
        } else
        {
            this.outerInterruptBus.INTA(currentInterrupt);
            currentInterrupt = -1;
            this.status = 0;
        }
    }

    @Override
    public void read()
    {
        int address = util.NumberTool.getIntFromByteArray(addressBus.getBus());
        if (super.isAddressLegal(address))
        {
            if ((address & 1) == 0)
            {
                util.NumberTool.intToByteArray(status, dataBus.getBus());
            } else
            {
                util.NumberTool.intToByteArray(command, dataBus.getBus());
            }
        }
    }

    @Override
    public void write()
    {
        int address = util.NumberTool.getIntFromByteArray(addressBus.getBus());
        if (super.isAddressLegal(address))
        {
            if ((address & 1) == 0)
            {
                status = (0x000000ff & util.NumberTool.getIntFromByteArray(dataBus.getBus()));
            } else
            {
                command = (0x000000ff & util.NumberTool.getIntFromByteArray(dataBus.getBus()));
            }
        }
    }
}
