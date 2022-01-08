package Server;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Thread {
    public String col;
    public List<Student> students = new ArrayList<>();

    public Teacher(String col) {
        this.col = col;
    }

    public String goToClass(int classNumber) {
        msg(" arrives and waits the class begin");
        Object start;
        Object end;
        if (this.col.equals("ELA")) {
            end = SubServerThread.ELAObjEnd;
        } else {
            end = SubServerThread.MathObjEnd;
        }
        synchronized (SubServerThread.SessObjStart) {
            try {
                SubServerThread.SessObjStart.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg(this.col + "class " + classNumber + ": " + students.get(0).name + " " + students.get(1).name + " " + students.get(2).name + " " + students.get(3).name);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (end) {
            try {
                end.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.students.clear();
        return "Class " + classNumber + " is over";
    }

    public String takeABreak() {
        msg("Have a rest");

        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Have a rest";
    }


    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] " + this.col + ": " + m);
    }
}
