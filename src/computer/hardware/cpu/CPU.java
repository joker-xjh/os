package computer.hardware.cpu;

import GUI.desktop.window.device_manager.DeviceInformation;
import GUI.desktop.window.fat_manager.FATInformation;
import GUI.desktop.window.file_manager.FileInformation;
import GUI.desktop.window.memory_manager.MemoryInformation;
import GUI.desktop.window.process_manager.ProcessInformation;
import computer.hardware.Power;
import computer.hardware.abstract_hardware.HardWare;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.ControlBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.bus.InterruptBus;
import computer.os.base.OS;
import computer.os.kernel.device_management.DeviceManager.Status;
import computer.os.kernel.process_management.PCB;
import java.util.concurrent.Semaphore;

/**
 * 中央处理器
 * @author Administrator
 */
public class CPU extends HardWare implements CPUInterface, java.lang.Runnable
{

    private DataBus dataBus;
    private ControlBus controlBus;
    private AddressBus addressBus;
    private InterruptBus innerInterruptBus;
    public int PSW, time;
    public int RR, LR;
    public int AR, DR;
    public int PC, IR;
    public int AX, BX, CX, DX;

    public CPU(Power power, ControlBus controlBus, AddressBus addressBus, DataBus dataBus, InterruptBus innerInterruptBus)
    {
        super(power);
        this.controlBus = controlBus;
        this.addressBus = addressBus;
        this.dataBus = dataBus;
        this.innerInterruptBus = innerInterruptBus;
        this.mutex = new Semaphore(1);
        this.sleepTime = 1000;
        this.os = new OS(this);
        this.isRun = true;
    }

    public void close()
    {
        this.power.setOpen(false);
    }

    @Override
    public void run()
    {
        os.init();
        time = 5;
        PSW = CPU.PSW_IF_BIT;
        PC = 0;
        PSW |= PSW_IF_BIT;
        while (this.isOpen())
        {
            if (isRun)
            {
                CLK();
            }
            try
            {
                java.lang.Thread.sleep(this.sleepTime);
            } catch (InterruptedException ex)
            {
                util.out.println("CPU-run:睡眠被中断！");
            }
        }
    }

    @Override
    public void CLK()
    {
        checkInterrupt();
        getInstruction();
        this.os.displayManager.showProcess();
        executeInstruction();
        this.os.displayManager.showProcess();
    }

    @Override
    public void RESET()
    {
        os.init();
        time = 5;
        PSW = CPU.PSW_IF_BIT;
        PC = 0;
        PSW |= PSW_IF_BIT;
    }

    /**
     * 检查中断
     */
    private void checkInterrupt()
    {
        if ((PSW & PSW_INNER_INTERRUPT_BIT) != 0)
        {
            dealWithInterrupt();
            PSW &= ~PSW_INNER_INTERRUPT_BIT;
        } else if ((PSW & PSW_IF_BIT) != 0)
        {
            if (innerInterruptBus.getInterVector() != 0)
            {
                PSW |= PSW_OUTER_INTERRUPT_BIT;
            }
            if ((PSW & PSW_OUTER_INTERRUPT_BIT) != 0)
            {
                INTA();
                PSW &= 0xFFFF00FF;
                PSW |= 0x800 | (util.NumberTool.getIntFromByteArray(dataBus.getBus()) << 8);
                dealWithInterrupt();
                INTA();
                PSW &= ~PSW_OUTER_INTERRUPT_BIT;
            }
        }
    }

    private void dealWithInterrupt()
    {
        int interruptNumber = 0xFF & (PSW >> 8);
        //转到中断处理程序
        if (0 <= interruptNumber && interruptNumber < os.interruptHandlers.length && os.interruptHandlers[interruptNumber] != null)
        {
            System.out.println("CPU-dealWithInterrupt：" + interruptNumber + "号中断!");
            os.interruptHandlers[interruptNumber].run();
        } else
        {
            util.err.println("CPU-dealWithInterrupt：缺少" + interruptNumber + "号中断处理程序!");
        }
    }

    /**
     * 取指令
     */
    private void getInstruction()
    {
        AR = PC++;
        read();
        IR = (DR & 0xff);
        os.displayManager.showProcess();
        AR = PC++;
        read();
        IR |= (DR & 0xff) << 8;
        os.displayManager.showProcess();
        AR = PC++;
        read();
        IR |= (DR & 0xff) << 16;
        os.displayManager.showProcess();
    }

    /**
     * 执行指令
     */
    private void executeInstruction()
    {
        if ((IR & 0xffff) == util.NumberTool.getIntFrom2Byte((byte) 'x', (byte) '='))
        {
            AX = ((IR & 0xff0000) >> 16) - (int) '0';
        } else if ((IR & 0xffff) == util.NumberTool.getIntFrom2Byte((byte) 'x', (byte) '+'))
        {
            AX++;
        } else if ((IR & 0xffff) == util.NumberTool.getIntFrom2Byte((byte) 'x', (byte) '-'))
        {
            AX--;
        } else if ((IR & 0xff) == (0xff & ((byte) '!')))
        {
            Status status;
            int device, useTime, pid, number;
            pid = os.processManager.getRunPid();
            device = ((IR & 0xff00) >> 8) - (int) 'A';
            useTime = ((IR & 0xff0000) >> 16) - (int) '0';
            if ((number = os.deviceManager.getDeviceNumber(pid, device)) == -1)
            {
                status = os.deviceManager.allocate(pid, device);
                if (status == Status.SUCCESS)
                {
                    number = os.deviceManager.getDeviceNumber(pid, device);
                    os.deviceDriver[device][number].open();
                    os.deviceDriver[device][number].use(useTime);
                    os.cpu.deviceInformation.setRemainTime(device, number, useTime);
                    os.processManager.block(PCB.STATUS_WAIT_IO);
                    os.processManager.load();
                    os.processManager.sendInformation();
                    os.displayManager.showProcess();
                    os.deviceManager.sendInformation();
                    os.displayManager.showDevice();
                } else if (status == Status.FAILURE)
                {
                    os.cpu.PC -= 3;
                    os.processManager.block(PCB.STATUS_WAIT_DEVICE[device]);
                    os.processManager.load();
                    os.processManager.sendInformation();
                    os.displayManager.showProcess();
                    os.deviceManager.sendInformation();
                    os.displayManager.showDevice();
                } else if (status == Status.ERROR)
                {
                    INT(0);
                }
            } else
            {
                os.deviceDriver[device][number].open();
                os.deviceDriver[device][number].use(useTime);
                os.cpu.deviceInformation.setRemainTime(device, number, useTime);
                os.processManager.block(PCB.STATUS_WAIT_IO);
                os.processManager.load();
                os.processManager.sendInformation();
                os.displayManager.showProcess();
                os.deviceManager.sendInformation();
                os.displayManager.showDevice();
            }
        } else if ((IR & 0xffff) == util.NumberTool.getIntFrom2Byte((byte) 'e', (byte) 'n'))
        {
            INT(0);
        } else if ((IR & 0xff) == (0xff & ((byte) 'J')) || (IR & 0xff) == (0xff & ((byte) 'j')))
        {
            PC = (IR & 0xffff00) >> 8;
        } else
        {
            INT(0);
            util.err.println("CPU-executeInstruction:非法指令，强制结束进程！");
        }
    }

    /**
     * 内中断
     */
    public void INT(int interruptNumber)
    {
        try
        {
            mutex.acquire();
            PSW |= PSW_INNER_INTERRUPT_BIT;
            PSW &= 0xFFFF00FF;
            PSW |= ((interruptNumber & 0xff) << 8);
            mutex.release();
        } catch (InterruptedException ex)
        {
            util.err.printf("CPU-INT:内中断异常！");
        }
    }

    public void read()
    {
        if (0 <= AR && AR < LR)
        {
            util.NumberTool.intToByteArray(RR + AR, addressBus.getBus());
            controlBus.read();
            DR = util.NumberTool.getIntFromByteArray(dataBus.getBus());
        } else if (AR >= 2048)
        {
            PSW |= PSW_SUPER_BIT;
            superRead();
            PSW &= ~(PSW_SUPER_BIT);
        } else
        {
            util.err.printf("CPU-read:非法读内存-" + LR + "-" + AR + "\n");
        }
    }

    public void write()
    {
        if (0 <= AR && AR < LR)
        {
            util.NumberTool.intToByteArray(DR, dataBus.getBus());
            util.NumberTool.intToByteArray(RR + AR, addressBus.getBus());
            controlBus.write();
        } else if (AR >= 2048)
        {
            PSW |= PSW_SUPER_BIT;
            superWrite();
            PSW &= ~(PSW_SUPER_BIT);
        } else
        {
            util.err.printf("CPU-write:非法写内存-" + LR + "-" + AR + "\n");
        }
    }

    public void superRead()
    {
        if ((PSW & PSW_SUPER_BIT) != 0)
        {
            util.NumberTool.intToByteArray(AR, addressBus.getBus());
            controlBus.read();
            DR = util.NumberTool.getIntFromByteArray(dataBus.getBus());
        }
    }

    public void superWrite()
    {
        if ((PSW & PSW_SUPER_BIT) != 0)
        {
            util.NumberTool.intToByteArray(DR, dataBus.getBus());
            util.NumberTool.intToByteArray(AR, addressBus.getBus());
            controlBus.write();
        }
    }

    private void INTA()
    {
        this.innerInterruptBus.INTA(0);
    }
    public final static int PSW_INNER_INTERRUPT_BIT = 1;
    public final static int PSW_OUTER_INTERRUPT_BIT = 2;
    public final static int PSW_IF_BIT = 4;
    public final static int PSW_SUPER_BIT = 8;
    private Semaphore mutex;
    boolean isRun;
    private int sleepTime;
    private OS os;
    public FATInformation fatInformation;
    public FileInformation fileInformation;
    public MemoryInformation memoryInformation;
    public DeviceInformation deviceInformation;
    public ProcessInformation processInformation;

    @Override
    public void setIPS(int ips)
    {
        sleepTime = ips;
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
    public int getPSW()
    {
        return PSW;
    }

    @Override
    public int getIR()
    {
        return IR;
    }

    @Override
    public int getAX()
    {
        return AX;
    }

    @Override
    public int getIPS()
    {
        return sleepTime;
    }

    @Override
    public void init()
    {
       os.init();
    }
}
