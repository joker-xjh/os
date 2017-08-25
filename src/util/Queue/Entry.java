/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Queue;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;

/**
 *
 * @author Administrator
 */
public class Entry implements util.Queue.Queue.EntryInterface
{

    public final static int SIZE = 2;
    protected InnerMemory memory;

    public Entry(InnerMemoryManager innerMemoryManager)
    {
        memory = innerMemoryManager.allocate(SIZE);
    }

    public void init(int index)
    {
        this.setPrevious(index);
        this.setNext(index);
    }

    @Override
    public int getPrevious()
    {
        return this.memory.read(0);
    }

    @Override
    public int getNext()
    {
        return this.memory.read(1);
    }

    @Override
    public void setPrevious(int previous)
    {
        this.memory.write(0, (byte) previous);
    }

    @Override
    public void setNext(int next)
    {
        this.memory.write(1, (byte) next);
    }
}
