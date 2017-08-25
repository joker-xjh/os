package GUI.desktop.window.banker_algorithm;


import java.awt.Color;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class StartRun implements Runnable {

    EMPanel emp;

    public StartRun(EMPanel emp) {
        this.emp = emp;
    }

    @Override
    public void run() {
        while (emp.getEm().getNum() < 15 && emp.getIsstop() >= 0) {
            while (emp.getIsstop() == 1) {
            }
            if ((emp.getEm().application(emp.getEm().getNum()))==true && emp.getIsstop() >= 0) {
                for (int i = 1; i < 4; i++) {
                    emp.getPanel()[0].setCellString(1, i, "" + emp.getEm().getAvailable(i - 1));
                }
                emp.getPanel()[1].setCellColor((emp.getEm().getNum() - 1),0,Color.green);
                emp.getPanel()[2].setCellString(emp.getEm().getSum() - 1, 0, "" + emp.getEm().getSum());
                for (int i = 1; i < 4; i++) {
                    emp.getPanel()[2].setCellString((emp.getEm().getSum() - 1), i, "" + emp.getEm().getAllocation(emp.getEm().getSum() - 1)[i - 1]);
                }
            } else {
                emp.getPanel()[1].setCellColor((emp.getEm().getNum()-1),0,Color.red);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
        if (emp.getIsstop() >= 0) {
            emp.getButton()[2].setEnabled(false);
            emp.getButton()[1].setEnabled(false);
        }
    }
}
