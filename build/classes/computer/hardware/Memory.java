package computer.hardware;

import computer.hardware.abstract_hardware.DeviceInterface;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.abstract_hardware.HardWare;

/**
 * 内存
 * @author Administrator
 */
public class Memory extends HardWare implements DeviceInterface
{

    private byte[] memory;
    private AddressBus addressBus;
    private DataBus dataBus;
    private int portMask;
    private int portNumber;

    public Memory(Power power, AddressBus addressBus, DataBus dataBus, int memorySize)
    {
        super(power);
        if (memorySize < 512)
        {
            throw new java.lang.IllegalArgumentException("内存大小必须大于等于512字节！");
        }
        this.memory = new byte[memorySize];
        this.addressBus = addressBus;
        this.dataBus = dataBus;
    }

    @Override
    public void read()
    {
        int address = util.NumberTool.getIntFromByteArray(addressBus.getBus());
        if ((portMask & address) == portNumber)
        {
            address &= ~portMask;
            if ((0 <= address && address < memory.length))
            {
//                util.out.println("Memory-work:读-" + address + ":" + memory[address]);
                util.NumberTool.intToByteArray(memory[address], dataBus.getBus());
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
            if ((0 <= address && address < memory.length))
            {
                memory[address] = (byte) (0x000000ff & util.NumberTool.getIntFromByteArray(dataBus.getBus()));
//                util.out.println("Memory-work:写-" + address + ":" + memory[address]);
            }
        }
    }

    public void setPort(int portMask, int portNumber)
    {
        this.portMask = portMask;
        this.portNumber = portNumber & portMask;
    }
}
