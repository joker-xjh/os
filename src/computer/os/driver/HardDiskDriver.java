package computer.os.driver;

import computer.hardware.device_controller.HardDiskController;
import computer.hardware.etc.Port;
import computer.os.base.InnerMemoryManager.InnerMemory.Stream;
import computer.os.base.OS;

/**
 * 硬盘驱动程序
 * @author Administrator
 */
public class HardDiskDriver extends Driver
{

    public HardDiskDriver(OS os)
    {
        super(os, Port.Number.HARD_DISK_CONTROLLER, HardDiskController.REGISTER_STATUS, HardDiskController.REGISTER_CONTROL_COMMAND, HardDiskController.REGISTER_CONTROL_ARGUMENT, HardDiskController.REGISTER_DATA);
    }

    @Override
    public void open()
    {
        push();
        super.open();
        pop();
    }

    @Override
    public void close()
    {
        push();
        super.close();
        pop();
    }

    public void readBlock(int blockNumber, int address, int length)
    {
        push();
        int port, offset, status, command;
        port = Port.Number.HARD_DISK_CONTROLLER;
        while (isBusy())
        {
            sleep(1);
        }
        setReady(false);
        offset = HardDiskController.REGISTER_CONTROL_ARGUMENT;
        this.write(port + offset, (byte) blockNumber);
        offset = HardDiskController.REGISTER_CONTROL_COMMAND;
        command = HardDiskController.COMMAND_READ;
        this.write(port + offset, (byte) command);
        while (!isReady())
        {
            sleep(1);
        }
        offset = HardDiskController.REGISTER_DATA;
        for (int i = 0; i < length && i < 64; i++)
        {
            this.write(address + i, this.read(port + offset + i));
        }
        setReady(false);
        pop();
    }

    public void writeBlock(int blockNumber, int address, int length)
    {
        push();
        int port, offset, status, command;
        port = Port.Number.HARD_DISK_CONTROLLER;
        while (isBusy() || isReady())
        {
            sleep(1);
        }
        setBusy(true);
        offset = HardDiskController.REGISTER_DATA;
        for (int i = 0; i < length && i < 64; i++)
        {
            this.write(port + offset + i, this.read(address + i));
        }
        offset = HardDiskController.REGISTER_CONTROL_ARGUMENT;
        this.write(port + offset, (byte) blockNumber);
        offset = HardDiskController.REGISTER_CONTROL_COMMAND;
        command = HardDiskController.COMMAND_WRITE;
        this.write(port + offset, (byte) command);
        while (isBusy())
        {
            sleep(1);
        }
        pop();
    }

    private boolean isReady()
    {
        boolean ready;
        push();
        int port, offset, status;
        port = Port.Number.HARD_DISK_CONTROLLER;
        offset = HardDiskController.REGISTER_STATUS;
        status = this.read(port + offset);
        ready = ((status & HardDiskController.STATUS_READY) != 0) && ((status & HardDiskController.STATUS_BUSY) == 0);
        pop();
        return ready;
    }

    private boolean isBusy()
    {
        boolean busy;
        push();
        int port, offset, status;
        port = Port.Number.HARD_DISK_CONTROLLER;
        offset = HardDiskController.REGISTER_STATUS;
        status = this.read(port + offset);
        busy = ((status & HardDiskController.STATUS_READY) == 0) && ((status & HardDiskController.STATUS_BUSY) != 0);
        pop();
        return busy;
    }

    private void setReady(boolean ready)
    {
        push();
        int port, offset, status;
        port = Port.Number.HARD_DISK_CONTROLLER;
        offset = HardDiskController.REGISTER_STATUS;
        if (ready)
        {
            status = this.read(port + offset) | HardDiskController.STATUS_READY;
        } else
        {
            status = this.read(port + offset) & (~HardDiskController.STATUS_READY);
        }
        this.write(port + offset, (byte) status);
        pop();
    }

    private void setBusy(boolean busy)
    {
        push();
        int port, offset, status;
        port = Port.Number.HARD_DISK_CONTROLLER;
        offset = HardDiskController.REGISTER_STATUS;
        if (busy)
        {
            status = this.read(port + offset) | HardDiskController.STATUS_BUSY;
        } else
        {
            status = this.read(port + offset) & (~HardDiskController.STATUS_BUSY);
        }
        this.write(port + offset, (byte) status);
        pop();
    }

    public void readBlock(int blockNumber, Stream stream, int length)
    {
        push();
        int port, offset, status, command;
        port = Port.Number.HARD_DISK_CONTROLLER;
        while (isBusy())
        {
            sleep(1);
        }
        setReady(false);
        offset = HardDiskController.REGISTER_CONTROL_ARGUMENT;
        this.write(port + offset, (byte) blockNumber);
        offset = HardDiskController.REGISTER_CONTROL_COMMAND;
        command = HardDiskController.COMMAND_READ;
        this.write(port + offset, (byte) command);
        while (!isReady())
        {
            sleep(1);
        }
        offset = HardDiskController.REGISTER_DATA;
        stream.init();
        for (int i = 0; i < length && i < 64; i++)
        {
            stream.write(this.read(port + offset + i));
        }
        setReady(false);
        pop();
    }

    public void writeBlock(int blockNumber, Stream stream, int length)
    {
        push();
        int port, offset, status, command;
        port = Port.Number.HARD_DISK_CONTROLLER;
        while (isBusy() || isReady())
        {
            sleep(1);
        }
        setBusy(true);
        offset = HardDiskController.REGISTER_DATA;
        stream.init();
        for (int i = 0; i < length && i < 64; i++)
        {
            this.write(port + offset + i, stream.read());
        }
        offset = HardDiskController.REGISTER_CONTROL_ARGUMENT;
        this.write(port + offset, (byte) blockNumber);
        offset = HardDiskController.REGISTER_CONTROL_COMMAND;
        command = HardDiskController.COMMAND_WRITE;
        this.write(port + offset, (byte) command);
        while (isBusy())
        {
            sleep(1);
        }
        pop();
    }
}
