package computer.os.base;

/**
 * 内部内存管理器
 * @author Administrator
 */
public class InnerMemoryManager
{

    private OS os;
    private int alreadyAllocatedMemorySize;
    private int maxMemorySize;

    public InnerMemoryManager(OS os, int maxMemorySize)
    {
        this.os = os;
        this.alreadyAllocatedMemorySize = 0;
        this.maxMemorySize = maxMemorySize;
    }

    public InnerMemory allocate(int size)
    {
        if (alreadyAllocatedMemorySize + size <= maxMemorySize)
        {
            int startAddress = alreadyAllocatedMemorySize;
            alreadyAllocatedMemorySize += size;
            return new InnerMemory(startAddress, size);
        }
        return null;
    }

    public class InnerMemory
    {

        private int startAddress;
        private int size;
        private Stream stream;

        public InnerMemory(int startAddress, int size)
        {
            this.startAddress = startAddress;
            this.size = size;
            this.stream = new Stream();
        }

        public byte read(int AR)
        {
            return os.read(this.startAddress + AR);
        }

        public void write(int AR, byte b)
        {
            os.write(this.startAddress + AR, b);
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

        public int getSize()
        {
            return size;
        }

        public Stream stream()
        {
            return this.stream;
        }

        public Stream newStream()
        {
            return new Stream();
        }

        public class Stream
        {

            private int cur;

            public void init()
            {
                cur = 0;
            }

            public byte read()
            {
                if (!this.isEOF())
                {
                    return InnerMemoryManager.InnerMemory.this.read(cur++);
                }
                return -1;
            }

            public void write(byte b)
            {
                if (!this.isEOF())
                {
                    InnerMemoryManager.InnerMemory.this.write(cur++, b);
                }
            }

            public boolean isEOF()
            {
                return !(0 <= cur && cur < size);
            }
        }
    }
}
