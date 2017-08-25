package computer.os.kernel.memory_management;

/**
 * 内存分配器
 * @author Administrator
 */
public interface MemoryAllocateAlgorithm
{
    public int allocate(int memoryLength);
    public void collect(int index);
}
