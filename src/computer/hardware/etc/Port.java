package computer.hardware.etc;

/**
 * 端口配置
 * @author Administrator
 */
public class Port
{

    public class Mask
    {

        public final static int MEMORY = 0xF800;
        public final static int HARD_DISK_CONTROLLER = 0xFFFFFF00;
        public final static int TIMER_CONTROLLER = 0xFFFFFFFC;
        public final static int DISPLAY_CONTROLLER = 0xFFFFFFFC;
        public final static int INTERRUPT_CONTROLLER = 0xFFFFFFFC;
        public final static int DEVICE_A1_CONTROLLER = 0xFFFFFFFC;
        public final static int DEVICE_B1_CONTROLLER = 0xFFFFFFFC;
        public final static int DEVICE_B2_CONTROLLER = 0xFFFFFFFC;
        public final static int DEVICE_C1_CONTROLLER = 0xFFFFFFFC;
        public final static int DEVICE_C2_CONTROLLER = 0xFFFFFFFC;
        public final static int CONSOLE_CONTROLLER = 0xFFFFFFFC;
    }

    public class Number
    {

        public final static int MEMORY = 0;
        public final static int HARD_DISK_CONTROLLER = 0x800;
        public final static int TIMER_CONTROLLER = 0x900;
        public final static int DISPLAY_CONTROLLER = 0x904;
        public final static int INTERRUPT_CONTROLLER = 0x908;
        public final static int DEVICE_A1_CONTROLLER = 0x90C;
        public final static int DEVICE_B1_CONTROLLER = 0x910;
        public final static int DEVICE_B2_CONTROLLER = 0x914;
        public final static int DEVICE_C1_CONTROLLER = 0x918;
        public final static int DEVICE_C2_CONTROLLER = 0x91C;
        public final static int CONSOLE_CONTROLLER = 0x920;
    }
}
