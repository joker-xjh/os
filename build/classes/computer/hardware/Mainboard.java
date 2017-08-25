package computer.hardware;

import computer.hardware.bus.InterruptBus;
import computer.hardware.device.HardDisk;
import computer.hardware.device_controller.TimerController;
import computer.hardware.device_controller.HardDiskController;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.ControlBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.cpu.CPU;
import computer.hardware.device.Display;
import computer.hardware.device_controller.ConsoleController;
import computer.hardware.device_controller.DeviceAController;
import computer.hardware.device_controller.DisplayController;
import computer.hardware.device_controller.InterruptController;
import computer.hardware.etc.Port;

/**
 * 主板
 * @author Administrator
 */
public class Mainboard
{

    private Power power;
    private DataBus dataBus;
    private ControlBus controlBus;
    private AddressBus addressBus;
    private InterruptBus innerInterruptBus;
    private InterruptBus outerInterruptBus;
    private CPU cpu;
    private Memory memory;
    private TimerController timerController;
    private DisplayController displayController;
    private HardDiskController hardDiskController;
    private InterruptController interruptController;
    private DeviceAController deviceA1Controller;
    private DeviceAController deviceB1Controller;
    private DeviceAController deviceB2Controller;
    private DeviceAController deviceC1Controller;
    private DeviceAController deviceC2Controller;
    private ConsoleController consoleController;
    private Display display;
    private HardDisk hardDisk;

    public Mainboard()
    {
        //电源
        power = new Power(false);
        //数据、控制、地址、内部中断总线和外部中断总线
        dataBus = new DataBus(16);
        controlBus = new ControlBus();
        addressBus = new AddressBus(16);
        innerInterruptBus = new InterruptBus();
        outerInterruptBus = new InterruptBus();
        //中央处理器、内存和各外设控制器
        cpu = new CPU(power, controlBus, addressBus, dataBus, innerInterruptBus);
        memory = new Memory(power, addressBus, dataBus, 2048);
        timerController = new TimerController(power, addressBus, dataBus, outerInterruptBus);
        displayController = new DisplayController(power, addressBus, dataBus, outerInterruptBus);
        hardDiskController = new HardDiskController(power, addressBus, dataBus, outerInterruptBus);
        interruptController = new InterruptController(power, addressBus, dataBus, innerInterruptBus, outerInterruptBus);
        deviceA1Controller = new DeviceAController(power, addressBus, dataBus, outerInterruptBus);
        deviceB1Controller = new DeviceAController(power, addressBus, dataBus, outerInterruptBus);
        deviceB2Controller = new DeviceAController(power, addressBus, dataBus, outerInterruptBus);
        deviceC1Controller = new DeviceAController(power, addressBus, dataBus, outerInterruptBus);
        deviceC2Controller = new DeviceAController(power, addressBus, dataBus, outerInterruptBus);
        consoleController = new ConsoleController(power, addressBus, dataBus, outerInterruptBus);
        //外部设备
        display = new Display(cpu, timerController);
        hardDisk = new HardDisk("disk", 128, 64);
        //外部设备连接到外设控制器
        displayController.setDisplay(display);
        hardDiskController.setHardDisk(hardDisk);
        //内存、外设控制器连接控制线
        controlBus.connectHardWare(memory);
        controlBus.connectHardWare(timerController);
        controlBus.connectHardWare(displayController);
        controlBus.connectHardWare(hardDiskController);
        controlBus.connectHardWare(interruptController);
        controlBus.connectHardWare(deviceA1Controller);
        controlBus.connectHardWare(deviceB1Controller);
        controlBus.connectHardWare(deviceB2Controller);
        controlBus.connectHardWare(deviceC1Controller);
        controlBus.connectHardWare(deviceC2Controller);
        controlBus.connectHardWare(consoleController);
        //为内存、外设控制器分配端口
        this.memory.setPort(Port.Mask.MEMORY, Port.Number.MEMORY);
        this.hardDiskController.setPort(Port.Mask.HARD_DISK_CONTROLLER, Port.Number.HARD_DISK_CONTROLLER);
        this.timerController.setPort(Port.Mask.TIMER_CONTROLLER, Port.Number.TIMER_CONTROLLER);
        this.displayController.setPort(Port.Mask.DISPLAY_CONTROLLER, Port.Number.DISPLAY_CONTROLLER);
        this.interruptController.setPort(Port.Mask.INTERRUPT_CONTROLLER, Port.Number.INTERRUPT_CONTROLLER);
        this.deviceA1Controller.setPort(Port.Mask.DEVICE_A1_CONTROLLER, Port.Number.DEVICE_A1_CONTROLLER);
        this.deviceB1Controller.setPort(Port.Mask.DEVICE_B1_CONTROLLER, Port.Number.DEVICE_B1_CONTROLLER);
        this.deviceB2Controller.setPort(Port.Mask.DEVICE_B2_CONTROLLER, Port.Number.DEVICE_B2_CONTROLLER);
        this.deviceC1Controller.setPort(Port.Mask.DEVICE_C1_CONTROLLER, Port.Number.DEVICE_C1_CONTROLLER);
        this.deviceC2Controller.setPort(Port.Mask.DEVICE_C2_CONTROLLER, Port.Number.DEVICE_C2_CONTROLLER);
        this.consoleController.setPort(Port.Mask.CONSOLE_CONTROLLER, Port.Number.CONSOLE_CONTROLLER);
        //连接中断总线
        this.innerInterruptBus.connectDevice(interruptController);
        this.outerInterruptBus.connectDevice(consoleController);
        this.outerInterruptBus.connectDevice(timerController);
        this.outerInterruptBus.connectDevice(displayController);
        this.outerInterruptBus.connectDevice(deviceA1Controller);
        this.outerInterruptBus.connectDevice(deviceB1Controller);
        this.outerInterruptBus.connectDevice(deviceB2Controller);
        this.outerInterruptBus.connectDevice(deviceC1Controller);
        this.outerInterruptBus.connectDevice(deviceC2Controller);
        //CPU和外设直接信息交换
        cpu.fatInformation = display.getFatInformation();
        cpu.fileInformation = display.getFileInformation();
        cpu.memoryInformation = display.getMemoryInformation();
        cpu.deviceInformation = display.getDeviceInformation();
        cpu.processInformation = display.getProcessInformation();
    }

    public void start()
    {
        power.setOpen(true);
        new java.lang.Thread(timerController).start();
        new java.lang.Thread(interruptController).start();
        new java.lang.Thread(hardDiskController).start();
        new java.lang.Thread(cpu).start();
    }
}
