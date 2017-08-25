package util.Queue;

import java.util.NoSuchElementException;

/**
 * 队列
 * @author Administrator
 */
public class Queue
{

    private int header;
    private EntryInterface[] table;
    private Queue.Iterator iterator;

    public Queue(int header, EntryInterface[] table)
    {
        if (table == null || header < 0 || header > table.length)
        {
            throw new NoSuchElementException();
        }
        this.table = table;
        this.header = header;
        this.iterator = new Queue.Iterator();
    }

    public void clear()
    {
        table[header].setPrevious(header);
        table[header].setNext(header);
    }

    public boolean isEmpty()
    {
        return (header == table[header].getNext() || header == table[header].getPrevious());
    }

    public int getFirst()
    {
        return table[header].getNext();
    }

    public int getLast()
    {
        return table[header].getPrevious();
    }

    public int removeFirst()
    {
        return removeQuickly(table[header].getNext());
    }

    public int removeLast()
    {
        return removeQuickly(table[header].getPrevious());
    }

    public void addFirst(int index)
    {
        int next;
        if (index != header)
        {
            next = table[header].getNext();
            table[index].setPrevious(header);
            table[index].setNext(next);
            table[header].setNext(index);
            table[next].setPrevious(index);
        }
    }

    public void addLast(int index)
    {
        int prev;
        if (index != header)
        {
            prev = table[header].getPrevious();
            table[index].setPrevious(prev);
            table[index].setNext(header);
            table[prev].setNext(index);
            table[header].setPrevious(index);
        }
    }

    public void remove(int index)
    {
        if (index != header)
        {
            Iterator it;
            (it = iterator()).init();
            while (it.hasNext())
            {
                if (it.next() == index)
                {
                    it.removeAndPrevious();
                }
            }
        }
    }

    public Iterator iterator()
    {
        iterator.init();
        return iterator;
    }

    public Iterator newIterator()
    {
        return new Iterator();
    }

    private int removeQuickly(int index)
    {
        int prev, next;
        if (index != header)
        {
            prev = table[index].getPrevious();
            next = table[index].getNext();
            table[prev].setNext(next);
            table[next].setPrevious(prev);
            table[index].setPrevious(index);
            table[index].setNext(index);
        }
        return index;
    }

    public interface EntryInterface
    {

        public int getPrevious();

        public int getNext();

        public void setPrevious(int previous);

        public void setNext(int next);
    }

    public class Iterator
    {

        int cur;

        public Iterator()
        {
            this.cur = header;
        }

        public void init()
        {
            cur = header;
        }

        public int next()
        {
            cur = table[cur].getNext();
            return cur;
        }

        public int previous()
        {
            cur = table[cur].getPrevious();
            return cur;
        }

        public boolean hasNext()
        {
            return table[cur].getNext() != header;
        }

        public boolean hasPrevious()
        {
            return table[cur].getPrevious() != header;
        }

        public int removeAndPrevious()
        {
            int prev;
            prev = table[cur].getPrevious();
            Queue.this.removeQuickly(cur);
            cur = prev;
            return cur;
        }

        public int removeAndNext()
        {
            int next;
            next = table[cur].getNext();
            Queue.this.removeQuickly(cur);
            cur = next;
            return cur;
        }

        public void addBefore(int index)
        {
            int prev = table[cur].getPrevious();
            table[index].setPrevious(prev);
            table[index].setNext(cur);
            table[prev].setNext(index);
            table[cur].setPrevious(index);
        }

        public void addAfter(int index)
        {
            int next = table[cur].getNext();
            table[index].setPrevious(cur);
            table[index].setNext(next);
            table[cur].setNext(index);
            table[next].setPrevious(index);
        }
    }
}
