package computer.os.kernel.memory_management;

/**
 * 最先适应法内存分配器
 * @author Administrator
 */
public class FirstFitAlgorithm implements MemoryAllocateAlgorithm
{

    private MemoryAllocateTable allocateTable;

    public FirstFitAlgorithm(MemoryAllocateTable allocateTable)
    {
        this.allocateTable = allocateTable;
    }

    @Override
    public int allocate(int length)
    {
        int pre, cur, len, offset, startAddress, index = 0;
        pre = this.allocateTable.getUnUseHead();
        cur = this.allocateTable.get(pre).getNext();
        while (cur != 0)
        {
            len = this.allocateTable.get(cur).getLength();
//            util.out.println("FFA-A:" + cur + ":" + this.allocateTable.getStartAddress(cur) + ":" + len);
            if (len >= length)
            {
                index = this.allocateTable.getEmptyMCB();
                if (index != 0 && len > length)
                {
                    startAddress = this.allocateTable.get(cur).getStartAddress();
                    this.allocateTable.get(cur).setStartAddress(startAddress + length);
                    this.allocateTable.get(cur).setLength(len - length);
                    this.allocateTable.get(index).setStartAddress(startAddress);
                    this.allocateTable.get(index).setLength(length);
                    this.allocateTable.get(index).setLeft(this.allocateTable.get(cur).getLeft());
                    this.allocateTable.get(index).setRight(cur);
                    this.allocateTable.get(this.allocateTable.get(index).getLeft()).setRight(index);
                    this.allocateTable.get(cur).setLeft(index);
                } else
                {
                    index = cur;
                    this.allocateTable.removeFromUnUseQueue(cur);
                }
                this.allocateTable.get(index).setUse(MemoryAllocateTable.USE);
                break;
            }
            pre = cur;
            cur = this.allocateTable.get(cur).getNext();
        }
        return index;
    }

    @Override
    public void collect(int index)
    {
        int left, right, length;
        if (index > 3 && this.allocateTable.get(index).getUse() == MemoryAllocateTable.USE)
        {
            left = this.allocateTable.get(index).getLeft();
            right = this.allocateTable.get(index).getRight();
            if (right != 0 && this.allocateTable.get(right).getUse() != MemoryAllocateTable.USE)
            {
                this.allocateTable.removeFromUnUseQueue(right);
                length = this.allocateTable.get(right).getLength();
                this.allocateTable.get(index).setRight(this.allocateTable.get(right).getRight());
                this.allocateTable.get(this.allocateTable.get(right).getRight()).setLeft(index);
                this.allocateTable.get(index).setLength(length + this.allocateTable.get(index).getLength());
                this.allocateTable.get(index).setUse(MemoryAllocateTable.FREE);
                this.allocateTable.insertToEmptyQueue(right);
            }
            if (left != 0 && this.allocateTable.get(left).getUse() != MemoryAllocateTable.USE)
            {
                this.allocateTable.removeFromUnUseQueue(left);
                length = this.allocateTable.get(index).getLength();
                this.allocateTable.get(left).setRight(this.allocateTable.get(index).getRight());
                this.allocateTable.get(this.allocateTable.get(index).getRight()).setLeft(left);
                this.allocateTable.get(left).setLength(length + this.allocateTable.get(left).getLength());
                this.allocateTable.insertToEmptyQueue(index);
                index = left;
            }
            insertUnUseQueue(index);
            this.allocateTable.get(index).setUse(MemoryAllocateTable.FREE);
        }
    }

    protected void insertUnUseQueue(int index)
    {
        int pre, cur, length, address;
        pre = this.allocateTable.getUnUseHead();
        cur = this.allocateTable.get(pre).getNext();
        address = this.allocateTable.get(index).getStartAddress();
        while (cur != 0)
        {
            if (address < this.allocateTable.get(cur).getStartAddress())
            {
                break;
            }
            pre = cur;
            cur = this.allocateTable.get(cur).getNext();
        }
        if (pre != 0)
        {
            this.allocateTable.get(pre).setNext(index);
        }
        this.allocateTable.get(index).setPre(pre);
        if (cur != 0)
        {
            this.allocateTable.get(cur).setPre(index);
        }
        this.allocateTable.get(index).setNext(cur);
    }
}
