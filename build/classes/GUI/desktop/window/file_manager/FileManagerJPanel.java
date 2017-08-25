package GUI.desktop.window.file_manager;

import GUI.desktop.window.ApplicationJPanel;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Administrator
 */
public class FileManagerJPanel extends ApplicationJPanel
{

    public FileManagerJPanel()
    {
        initComponents();
        this.setVisible(true);
        this.setSize(500, 350);
        this.root = new DefaultMutableTreeNode("root");
        this.fileJTree = new JTree(root);
        this.JS = new JScrollPane(this.fileJTree);
        this.JS.setSize(120, 500);
        this.treeJPanel.add(this.JS);
        this.promptJTextField.setEditable(false);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.ORANGE, 1, true), "文件管理", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), java.awt.Color.BLACK));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        promptJLabel = new javax.swing.JLabel();
        commandLineJTextField = new javax.swing.JTextField();
        promptJTextField = new javax.swing.JTextField();
        treeJPanel = new javax.swing.JPanel();

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane4.setViewportView(jTextArea);

        promptJLabel.setFont(new java.awt.Font("宋体", 0, 12));
        promptJLabel.setText("输入命令，结束敲回车键");

        commandLineJTextField.setFont(new java.awt.Font("宋体", 0, 12));

        promptJTextField.setFont(new java.awt.Font("宋体", 0, 12));
        promptJTextField.setText("写文件时，请先将内容写入下面文本框内");

        javax.swing.GroupLayout treeJPanelLayout = new javax.swing.GroupLayout(treeJPanel);
        treeJPanel.setLayout(treeJPanelLayout);
        treeJPanelLayout.setHorizontalGroup(
            treeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 114, Short.MAX_VALUE)
        );
        treeJPanelLayout.setVerticalGroup(
            treeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(treeJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(promptJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(commandLineJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(promptJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(treeJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(promptJLabel)
                        .addGap(2, 2, 2)
                        .addComponent(commandLineJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(promptJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField commandLineJTextField;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JLabel promptJLabel;
    private javax.swing.JTextField promptJTextField;
    private javax.swing.JPanel treeJPanel;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JTree fileJTree;
    private DefaultMutableTreeNode root;
    private FileInformation fileInformation;
    javax.swing.JScrollPane JS;

    @Override
    protected void paintComponent(Graphics g)
    {
    }

    public void addActionListenerCommandLineJTextField(ActionListener e)
    {
        this.getCommandLineJTextField().addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean flag = false;
                int number = FileInformation.Command.NULL;
                String text = commandLineJTextField.getText();
                String command = null;
                String argument1 = null;
                String argument2 = null;
                Scanner scanner = new Scanner(text);
                if (scanner.hasNext())
                {
                    command = scanner.next();
                    number = fileInformation.command.getCommandNumber(command);
                    if (number != FileInformation.Command.NULL)
                    {
                        switch (number)
                        {
                            case FileInformation.Command.CREATE:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.DELETE:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.TYPE:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.COPY:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                if (flag && scanner.hasNext())
                                {
                                    flag = false;
                                    argument2 = scanner.next();
                                    if (!argument1.equals(argument2))
                                    {
                                        flag = getFilePath(fileInformation.command.getArgument2(), argument2);
                                    }
                                } else
                                {
                                    flag = false;
                                }
                                break;
                            case FileInformation.Command.MKDIR:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getDirPath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.RMDIR:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getDirPath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.DELDIR:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getDirPath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.CHDIR:
                                break;
                            case FileInformation.Command.MOVE:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                if (flag && scanner.hasNext())
                                {
                                    flag = false;
                                    argument2 = scanner.next();
                                    if (!argument1.equals(argument2))
                                    {
                                        flag = getFilePath(fileInformation.command.getArgument2(), argument2);
                                    }
                                } else
                                {
                                    flag = false;
                                }
                                break;
                            case FileInformation.Command.CHANGE:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                if (flag && scanner.hasNextByte())
                                {
                                    byte[] bt = new byte[4];
                                    bt[0] = scanner.nextByte();
                                    fileInformation.command.getArgument2().clear();
                                    fileInformation.command.getArgument2().add(bt);
                                }
                                break;
                            case FileInformation.Command.FORMAT:
                                flag = true;
                                break;
                            case FileInformation.Command.FDISK:
                                break;
                            case FileInformation.Command.RUN:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                }
                                break;
                            case FileInformation.Command.HALT:
                                flag = true;
                                break;
                            case FileInformation.Command.EDIT:
                                if (scanner.hasNext())
                                {
                                    argument1 = scanner.next();
                                    flag = getFilePath(fileInformation.command.getArgument1(), argument1);
                                    if (flag)
                                    {
                                        String str = jTextArea.getText();
                                        fileInformation.file.init();
                                        for (int i = 0; i < str.length(); i++)
                                        {
                                            fileInformation.file.add((byte) str.charAt(i));
                                        }
                                    }
                                }
                                break;
                        }
                    } else
                    {
                        if ((flag = getFilePath(fileInformation.command.getArgument1(), command)))
                        {
                            number = FileInformation.Command.RUN;
                        }
                    }

                }
                if (flag)
                {
                    fileInformation.command.setCommand(number);
                } else
                {
                    jTextArea.setText("命令错误！");
                    fileInformation.command.setCommand(FileInformation.Command.NULL);
                }
            }

            private boolean getFilePath(ArrayList<byte[]> arg, String argument)
            {
                byte[] bt;
                String[] args;
                arg.clear();
                if (argument.matches("^\\\\(\\w{1,3}\\\\)*\\w{1,3}\\.\\w$"))
                {
                    args = argument.split("\\\\");
                    for (int i = 1; i < args.length - 1; i++)
                    {
                        bt = new byte[4];
                        bt[3] = (byte) ' ';
                        for (int j = 0; j < 3; j++)
                        {
                            if (j < args[i].length())
                            {
                                bt[2 - j] = (byte) args[i].charAt(args[i].length() - 1 - j);
                            } else
                            {
                                bt[2 - j] = (byte) ' ';
                            }
                        }
                        arg.add(bt);
                    }
                    bt = new byte[4];
                    bt[3] = (byte) args[args.length - 1].charAt(args[args.length - 1].length() - 1);
                    for (int j = 0; j < 3; j++)
                    {
                        if (j < args[args.length - 1].length() - 2)
                        {
                            bt[2 - j] = (byte) args[args.length - 1].charAt(args[args.length - 1].length() - 3 - j);
                        } else
                        {
                            bt[2 - j] = (byte) ' ';
                        }
                    }
                    arg.add(bt);
                    for (int i = 0; i < args.length; i++)
                    {
                        util.out.println("" + i + ":" + args[i]);
                    }
                    return true;
                }
                return false;
            }

            private boolean getDirPath(ArrayList<byte[]> arg, String argument)
            {
                byte[] bt;
                String[] args;
                arg.clear();
                if (argument.matches("^\\\\(\\w{1,3}\\\\)*\\w{1,3}$"))
                {
                    args = argument.split("\\\\");
                    for (int i = 1; i < args.length; i++)
                    {
                        bt = new byte[4];
                        bt[3] = (byte) ' ';
                        for (int j = 0; j < 3; j++)
                        {
                            if (j < args[i].length())
                            {
                                bt[2 - j] = (byte) args[i].charAt(args[i].length() - 1 - j);
                            } else
                            {
                                bt[2 - j] = (byte) ' ';
                            }
                        }
                        arg.add(bt);
                    }
                    return true;
                }
                return false;
            }
        });
        this.getCommandLineJTextField().addActionListener(e);
    }

    /**
     * @return the commandLineJTextField
     */
    public javax.swing.JTextField getCommandLineJTextField()
    {
        return commandLineJTextField;
    }

    /**
     * @return the rootJTree
     */
    public javax.swing.JTree getFileTree()
    {
        return fileJTree;
    }

    /**
     * @return the jTextArea
     */
    public javax.swing.JTextArea getjTextArea()
    {
        return jTextArea;
    }

    /**
     * @return the promptJTextField
     */
    public javax.swing.JTextField getPromptJTextField()
    {
        return promptJTextField;
    }

    @Override
    public void refresh()
    {
        Iterator it = this.fileInformation.file.iterator();
        this.jTextArea.setText("");
        while (it.hasNext())
        {
            jTextArea.append("" + (char) ('\0' + ((Byte) it.next())));
        }
        if (this.fileInformation.rootNode != null)
        {
            this.fileJTree.removeAll();
            this.fileJTree.setModel(new DefaultTreeModel(this.fileInformation.rootNode, false));
        } else
        {
            this.fileJTree.setModel(null);
        }
        if(this.fileInformation.error!=null)
        {
            this.commandLineJTextField.setText(this.fileInformation.error);
            this.commandLineJTextField.selectAll();
            this.fileInformation.error=null;
        }
        this.repaint();
        this.fileInformation.file.init();
    }

    /**
     * @param fileInformation the fileInformation to set
     */
    public void setFileInformation(FileInformation fileInformation)
    {
        this.fileInformation = fileInformation;
    }
}
