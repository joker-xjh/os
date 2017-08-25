package util;

/**
 * 栈
 * @author Administrator
 */
public class Stack
{

    private int top;
    private int[] stack;

    public Stack(int size)
    {
        this.top = 0;
        this.stack = new int[size];
    }

    public boolean isEmpty()
    {
        return top <= 0;
    }

    public void push(int e)
    {
        stack[top++] = e;
    }

    public int pop()
    {
        int e = 0;
        if (!isEmpty())
        {
            e = stack[--top];
        }
        return e;
    }

    public int top()
    {
        int e = 0;
        if (!isEmpty())
        {
            e = stack[top-1];
        }
        return e;
    }
}
