package computer.os.shell;

import computer.os.base.OS;

/**
 *
 * @author Administrator
 */
public class Shell extends computer.os.base.SystemProcess
{

    public Shell(OS os)
    {
        super(os);
    }

    public void create(byte[][] fileName)
    {
        push();
        os.fileManager.create(fileName, (byte) 4);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void delete(byte[][] fileName)
    {
        push();
        os.fileManager.delete(fileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void type(byte[][] fileName)
    {
        push();
        //代码补充处
        os.fileManager.type(fileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void copy(byte[][] srcFileName, byte[][] distFileName)
    {
        push();
        //代码补充处
        os.fileManager.copy(srcFileName, distFileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void mkdir(byte[][] fileName)
    {
        push();
        os.fileManager.mkdir(fileName, (byte) 8);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void rmdir(byte[][] fileName)
    {
        push();
        //补充代码
        os.fileManager.rmdir(fileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void format()
    {
        push();
        os.fileManager.format();
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void chdir(byte[][] srcFileName, byte[][] distFileName)
    {
        push();
        //代码补充处
        os.fileManager.chdir(srcFileName, distFileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void deldir(byte[][] fileName) 
    {
        push();
        os.fileManager.deldir(fileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void move(byte[][] oldfileName, byte[][] nowfileName)
    {
        push();
        //代码补充处
        os.fileManager.move(oldfileName, nowfileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void change(byte[][] fileName, byte attribute)
    {
        push();
        //代码补充处
        os.fileManager.change(fileName, attribute);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void fdisk()
    {
        push();
        //代码补充处
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }

    public void run(byte[][] fileName)
    {
        push();
        os.processManager.create(fileName);
        os.processManager.sendInformation();
        os.displayManager.showProcess();
        pop();
    }

    public void halt()
    {
        push();
        os.processManager.close();
        os.fileManager.flush();
        os.hardDiskDriver.close();
        os.close();
        os.cpu.close();
        System.exit(0);
        pop();
    }
    
    public void edit(byte[][] fileName)
    {
        push();
        os.fileManager.write(fileName);
        os.fileManager.sendInformation();
        os.displayManager.showFile();
        pop();
    }
}
