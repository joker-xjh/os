package GUI.desktop.window.file_manager;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Administrator
 */
public class FileInformation
{

    public File file;
    public Command command;
    public DefaultMutableTreeNode rootNode;
    public String error;

    public FileInformation()
    {
        this.file = new FileInformation.File();
        this.command = new FileInformation.Command();
        this.rootNode = null;
    }

    public class Command
    {

        public final static int NULL = 0;
        public final static int CREATE = 1;
        public final static int DELETE = 2;
        public final static int TYPE = 3;
        public final static int COPY = 4;
        public final static int MKDIR = 5;
        public final static int RMDIR = 6;
        public final static int CHDIR = 7;
        public final static int DELDIR = 8;
        public final static int MOVE = 9;
        public final static int CHANGE = 10;
        public final static int FORMAT = 11;
        public final static int FDISK = 12;
        public final static int RUN = 13;
        public final static int HALT = 14;
        public final static int EDIT = 15;
        private int command;
        private ArrayList<byte[]> argument1;
        private ArrayList<byte[]> argument2;

        public Command()
        {
            this.command = 0;
            this.argument1 = new ArrayList<byte[]>();
            this.argument2 = new ArrayList<byte[]>();
            this.argument1.clear();
            this.argument2.clear();
        }

        public void init()
        {
            this.command = NULL;
            this.argument1.clear();
            this.argument2.clear();
        }

        /**
         * @return the command
         */
        public int getCommand()
        {
            return command;
        }

        /**
         * @param command the command to set
         */
        public void setCommand(int command)
        {
            this.command = command;
        }

        public void addArgument1(byte[] bt)
        {
            this.argument1.add(bt);
        }

        public void addArgument2(byte[] bt)
        {
            this.argument2.add(bt);
        }

        public ArrayList<byte[]> getArgument1()
        {
            return this.argument1;
        }

        public ArrayList<byte[]> getArgument2()
        {
            return this.argument2;
        }

        public int getCommandNumber(String command)
        {
            int number = NULL;
            if ("create".equalsIgnoreCase(command))
            {
                number = Command.CREATE;
            } else if ("delete".equalsIgnoreCase(command))
            {
                number = Command.DELETE;
            } else if ("type".equalsIgnoreCase(command))
            {
                number = Command.TYPE;
            } else if ("copy".equalsIgnoreCase(command))
            {
                number = Command.COPY;
            } else if ("mkdir".equalsIgnoreCase(command))
            {
                number = Command.MKDIR;
            } else if ("rmdir".equalsIgnoreCase(command))
            {
                number = Command.RMDIR;
            } else if ("chdir".equalsIgnoreCase(command))
            {
                number = Command.CHDIR;
            } else if ("deldir".equalsIgnoreCase(command))
            {
                number = Command.DELDIR;
            } else if ("move".equalsIgnoreCase(command))
            {
                number = Command.MOVE;
            } else if ("change".equalsIgnoreCase(command))
            {
                number = Command.CHANGE;
            } else if ("format".equalsIgnoreCase(command))
            {
                number = Command.FORMAT;
            } else if ("fdisk".equalsIgnoreCase(command))
            {
                number = Command.FDISK;
            } else if ("run".equalsIgnoreCase(command))
            {
                number = Command.RUN;
            } else if ("halt".equalsIgnoreCase(command))
            {
                number = Command.HALT;
            }else if ("write".equalsIgnoreCase(command))
            {
                number = Command.EDIT;
            }
            return number;
        }
    }

    public class File
    {

        private ArrayList<Byte> body;

        public File()
        {
            this.body = new ArrayList<Byte>();
            this.body.clear();
        }

        public void init()
        {
            this.body.clear();
        }

        public void add(byte b)
        {
            this.body.add(b);
        }

        public Iterator iterator()
        {
            return this.body.iterator();
        }
    }
}
