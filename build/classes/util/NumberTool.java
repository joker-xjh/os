/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Administrator
 */
public class NumberTool
{

    public static int getIntFrom2Byte(byte low, byte hig)
    {
        int a = 0, b = 0;
        a = low;
        a = a & 0x000000ff;
        b = hig;
        b = b & 0x000000ff;
        b = (b << 8);
        return a | b;
    }

    public static int getIntFromByteArray(byte[] bt)
    {
        int a, b, c;
        a = 0;
        c = 0;
        for (int i = 0; i < 4 && i < bt.length; i++)
        {
            b = bt[i];
            b = b & 0x000000ff;
            b = (b << c);
            a = a | b;
            c += 8;
        }
        return a;
    }

    public static int getIntFrom3Byte(byte a, byte b, byte c)
    {
        int i = 0, j = 0;
        j = 0x000000ff & c;
        i = i | j;
        i = (i << 8);
        j = 0x000000ff & b;
        i = i | j;
        i = (i << 8);
        j = 0x000000ff & a;
        i = i | j;
        return i;
    }
    
    public static int getIntFrom4Byte(byte a, byte b, byte c, byte d)
    {
        int i = 0, j = 0;
        j = 0x000000ff & d;
        i = i | j;
        i = (i << 8);
        j = 0x000000ff & c;
        i = i | j;
        i = (i << 8);
        j = 0x000000ff & b;
        i = i | j;
        i = (i << 8);
        j = 0x000000ff & a;
        i = i | j;
        return i;
    }

    public static byte getLowByteFromInt(int i)
    {
        byte a = 0;
        int b = i & 0x000000ff;
        a = (byte) b;
        return a;
    }

    public static byte getHigByteFromInt(int i)
    {
        byte a = 0;
        int b = i & 0x0000ff00;
        b = (b >> 8);
        a = (byte) b;
        return a;
    }

    public static void intToByteArray(int in, byte[] bt)
    {
        int a;
        a = in;
        for (int i = 0; i < 4 && i < bt.length; i++)
        {
            bt[i] = (byte) (a & 0x000000ff);
            a = (a >> 8);
        }
    }

    public static int count(int i)
    {
        int count=1;
        while(i>9)
        {
            i/=10;
            count++;
        }
        return count;
    }
}
