package computer.os.interrupt_handler;

import GUI.desktop.window.file_manager.FileInformation;
import computer.os.base.OS;
import computer.os.shell.Shell;
import java.util.ArrayList;

/**
 * 时间片结束中断处理
 * @author Administrator
 */
public class DisplayInterruptHandler extends InterruptHandler
{

    private Shell shell;
    private int command;
    private byte[][] argument1, argument2;

    public DisplayInterruptHandler(OS os)
    {
        super(os);
        this.shell = new Shell(os);
        this.argument1 = new byte[1][4];
        this.argument2 = new byte[1][4];
    }

    private void getCommand()
    {
        push();
        ArrayList<byte[]> arg1;
        ArrayList<byte[]> arg2;
        command = os.cpu.fileInformation.command.getCommand();
        arg1 = os.cpu.fileInformation.command.getArgument1();
        arg2 = os.cpu.fileInformation.command.getArgument2();
        if (arg1.size() > 0)
        {
            argument1 = new byte[arg1.size()][4];
            for (int i = 0; i < arg1.size(); i++)
            {
                for (int j = 0; j < argument1[i].length && j < arg1.get(i).length; j++)
                {
                    argument1[i][j] = arg1.get(i)[j];
                }
            }
        } else
        {
            argument1 = new byte[0][0];
        }
        if (arg2.size() > 0)
        {
            argument2 = new byte[arg2.size()][4];
            for (int i = 0; i < arg2.size(); i++)
            {
                for (int j = 0; j < argument2[i].length && j < arg2.get(i).length; j++)
                {
                    argument2[i][j] = arg2.get(i)[j];
                }
            }
        } else
        {
            argument2 = new byte[0][4];
        }
        pop();
    }

    @Override
    public void run()
    {
        push();
        this.os.displayManager.showProcess();
        getCommand();
        switch (command)
        {
            case FileInformation.Command.CREATE:
                shell.create(argument1);
                break;
            case FileInformation.Command.DELETE:
                shell.delete(argument1);
                break;
            case FileInformation.Command.TYPE:
                shell.type(argument1);
                break;
            case FileInformation.Command.COPY:
                shell.copy(argument1, argument2);
                break;
            case FileInformation.Command.MKDIR:
                shell.mkdir(argument1);
                break;
            case FileInformation.Command.RMDIR:
                shell.rmdir(argument1);
                break;
            case FileInformation.Command.CHDIR:
                shell.chdir(argument1, argument2);
                break;
            case FileInformation.Command.DELDIR:
                shell.deldir(argument1);
                break;
            case FileInformation.Command.MOVE:
                shell.move(argument1, argument2);
                break;
            case FileInformation.Command.CHANGE:
                shell.change(argument1, argument2[0][0]);
                break;
            case FileInformation.Command.FORMAT:
                shell.format();
                break;
            case FileInformation.Command.FDISK:
                shell.fdisk();
                break;
            case FileInformation.Command.RUN:
                shell.run(argument1);
                break;
            case FileInformation.Command.HALT:
                shell.halt();
                break;
            case FileInformation.Command.EDIT:
                shell.edit(argument1);
                break;
        }
        pop();
    }
}
