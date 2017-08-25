package computer.hardware.abstract_hardware;

import computer.hardware.bus.AddressBus;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.InterruptBus;
import computer.hardware.Power;

/**
 * 抽象设备控制器
 * @author Administrator
 */
public abstract class DeviceController extends HardWare implements IODeviceInterface, java.lang.Runnable
{

    protected AddressBus addressBus;
    protected DataBus dataBus;
    protected InterruptBus interruptBus;
    protected byte[] register;

    public DeviceController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power);
        this.register = new byte[registerSize];
        this.addressBus = addressBus;
        this.dataBus = dataBus;
        this.interruptBus = interruptBus;
        this.register[DeviceController.REGISTER_STATUS] = 0;
    }

    @Override
    public void read()
    {
        int address = util.NumberTool.getIntFromByteArray(addressBus.getBus());
        if ((portMask & address) == portNumber)
        {
            address &= ~portMask;
            if ((0 <= address && address < register.length))
            {
                util.out.println("DeviceController-work:读-" + address + ":" + register[address]);
                util.NumberTool.intToByteArray(register[address], dataBus.getBus());
            }
        }
    }

    @Override
    public void write()
    {
        int address = util.NumberTool.getIntFromByteArray(addressBus.getBus());
        if ((portMask & address) == portNumber)
        {
            address &= ~portMask;
            if ((0 <= address && address < register.length))
            {
                register[address] = (byte) (0x000000ff & util.NumberTool.getIntFromByteArray(dataBus.getBus()));
                util.out.println("DeviceController-work:写-" + address + ":" + register[address]);
            }
        }
    }

    @Override
    public void INTA()
    {
        if ((register[REGISTER_STATUS] & STATUS_INTERRUPT) != 0)
        {
            this.interruptBus.INTX(this);
            register[REGISTER_STATUS] &= ~STATUS_INTERRUPT;
            util.out.println("DeviceController-INTA:" + this.portNumber);
        }
    }

    protected boolean INTR()
    {
        if ((register[REGISTER_STATUS] & STATUS_INTERRUPT) == 0)
        {
            util.out.println("DeviceController-INTR:" + this.portNumber);
            register[REGISTER_STATUS] |= STATUS_INTERRUPT;
            this.interruptBus.INTR(this);
            return true;
        }
        return false;
    }
    private int portMask;
    private int portNumber;

    protected boolean isAddressLegal(int address)
    {
        return (portMask & address) == portNumber;
    }

    public void setPort(int portMask, int portNumber)
    {
        this.portMask = portMask;
        this.portNumber = portNumber & portMask;
    }
    public final static int REGISTER_STATUS = 0;
    public final static int REGISTER_CONTROL_COMMAND = 1;
    public final static int REGISTER_CONTROL_ARGUMENT = 2;
    public final static int REGISTER_DATA = 3;
    public final static int STATUS_READY = 1;
    public final static int STATUS_BUSY = 2;
    public final static int STATUS_FINISH = 4;
    public final static int STATUS_WORK = 8;
    public final static int STATUS_INTERRUPT = 16;
    public final static int COMMAND_NULL = 0;
    public final static int COMMAND_OPEN = 1;
    public final static int COMMAND_CLOSE = 2;
    public final static int COMMAND_READ = 3;
    public final static int COMMAND_WRITE = 4;
    public final static int COMMAND_END_INTERRUPT = 5;
}