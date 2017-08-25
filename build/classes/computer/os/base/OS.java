package computer.os.base;

import computer.hardware.cpu.CPU;
import computer.hardware.etc.Port;
import computer.os.driver.DisplayDriver;
import computer.os.driver.HardDiskDriver;
import computer.os.driver.IODeviceDriver;
import computer.os.interrupt_handler.DisplayInterruptHandler;
import computer.os.interrupt_handler.IODeviceInterruptHandler;
import computer.os.interrupt_handler.InterruptHandler;
import computer.os.interrupt_handler.TimerInterruptHandler;
import computer.os.kernel.display_management.DisplayManager;
import computer.os.kernel.memory_management.MemoryManager;
import computer.os.kernel.process_management.ProcessManager;
import computer.os.interrupt_handler.ProcessEndInterruptHandler;
import computer.os.interrupt_handler.TimePieceEndInterruptHandler;
import computer.os.kernel.device_management.DeviceManager;
import computer.os.kernel.file_management.FileManager;
import util.Stack;

/**
 * 操作系统
 * @author Administrator
 */
public class OS
{

    public CPU cpu;
    public Stack stack;
    public FileManager fileManager;
    public MemoryManager memoryManager;
    public ProcessManager processManager;
    public DisplayManager displayManager;
    public DeviceManager deviceManager;
    public DisplayDriver displayDriver;
    public HardDiskDriver hardDiskDriver;
    public IODeviceDriver[][] deviceDriver;
    public InterruptHandler[] interruptHandlers;
    public OS(CPU cpu)
    {
        this.cpu = cpu;
        this.stack = new Stack(1024);
        this.displayDriver = new DisplayDriver(this);
        this.hardDiskDriver = new HardDiskDriver(this);
        this.deviceDriver = new IODeviceDriver[3][];
        this.deviceDriver[0] = new IODeviceDriver[1];
        this.deviceDriver[1] = new IODeviceDriver[2];
        this.deviceDriver[2] = new IODeviceDriver[2];
        this.deviceDriver[0][0] = new IODeviceDriver(this, Port.Number.DEVICE_A1_CONTROLLER);
        this.deviceDriver[1][0] = new IODeviceDriver(this, Port.Number.DEVICE_B1_CONTROLLER);
        this.deviceDriver[1][1] = new IODeviceDriver(this, Port.Number.DEVICE_B2_CONTROLLER);
        this.deviceDriver[2][0] = new IODeviceDriver(this, Port.Number.DEVICE_C1_CONTROLLER);
        this.deviceDriver[2][1] = new IODeviceDriver(this, Port.Number.DEVICE_C2_CONTROLLER);
        this.memoryManager = new MemoryManager(this);
        this.fileManager = new FileManager(this);
        this.processManager = new ProcessManager(this);
        this.displayManager = new DisplayManager(this);
        this.deviceManager = new DeviceManager(this);
        this.interruptHandlers = new InterruptHandler[64];
        this.interruptHandlers[0] = new ProcessEndInterruptHandler(this);
        this.interruptHandlers[1] = new TimePieceEndInterruptHandler(this);
        this.interruptHandlers[9] = new TimerInterruptHandler(this);
        this.interruptHandlers[10] = new DisplayInterruptHandler(this);
        this.interruptHandlers[11] = new IODeviceInterruptHandler(this, 0, 0);
        this.interruptHandlers[12] = new IODeviceInterruptHandler(this, 1, 0);
        this.interruptHandlers[13] = new IODeviceInterruptHandler(this, 1, 1);
        this.interruptHandlers[14] = new IODeviceInterruptHandler(this, 2, 0);
        this.interruptHandlers[15] = new IODeviceInterruptHandler(this, 2, 1);
    }

    public void init()
    {
        memoryManager.init();
        processManager.setMCBIndex(memoryManager.allocate(ProcessManager.SIZE));
        fileManager.setMCBIndex(memoryManager.allocate(FileManager.SIZE));
        deviceManager.setMCBIndex(memoryManager.allocate(DeviceManager.SIZE));
//        memoryManager.allocate(380);
        fileManager.init();
        deviceManager.init();
        processManager.init();
        processManager.load();
        fileManager.sendInformation();
        processManager.sendInformation();
        displayManager.showFile();
    }

    public void close()
    {
        this.fileManager.flush();
        this.hardDiskDriver.close();
    }
    
    public byte read(int AR)
    {
        this.cpu.AR = AR;
        this.cpu.read();
        return (byte) (0x000000ff & this.cpu.DR);
    }

    public void write(int AR, byte b)
    {
        this.cpu.AR = AR;
        this.cpu.DR = b;
        this.cpu.write();
    }

    public int read2Byte(int AR)
    {
        int b;
        b = 0x000000ff & this.read(AR + 1);
        b = (b << 8);
        b |= (0x000000ff & this.read(AR));
        return b;
    }

    public void write2Byte(int AR, int b)
    {
        this.write(AR, (byte) (b & 0x000000ff));
        b = b >> 8;
        this.write(AR + 1, (byte) (b & 0x000000ff));
    }

    public void push(int e)
    {
        stack.push(e);
    }

    public int pop()
    {
        return stack.pop();
    }

    public int top()
    {
        return stack.top();
    }
}
