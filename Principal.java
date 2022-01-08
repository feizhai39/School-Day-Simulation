// Author: Fei Zhai
// Date: 04 / 2021

package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Principal extends Thread {
    public List<Student> phyStudents = new ArrayList<>();

    public Principal() {

    }

    public String arrive(Object obj) {
        synchronized (obj) {
            msg("Principal arrives");
            obj.notifyAll();
            msg("lets students in");
        }
        return "Principal arrives and lets students in";
    }

    public String checkQuest(Object obj) {
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int leaveNumber = 0;
        msg("checks the Questionnaire");
        for (int i = 0; i < SubServerThread.numStudents; i++) {
            if (!SubServerThread.students.get(i).getQuest()) {
                SubServerThread.students.remove(i);
                i--;
                SubServerThread.numStudents--;
                leaveNumber++;
            }
        }
        msg(leaveNumber + " Students have not finished Quest");

        synchronized (obj) {
            obj.notifyAll();
        }
        return leaveNumber + " Students have not finished Quest";
    }

    public String chooseCheck() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("choose students to check health");
        synchronized (SubServerThread.students) {
            for (int i = 0; i < SubServerThread.numStudents; i++) {
                Random r = new Random();
                int ran = r.nextInt(3);
                if (ran == 1) {
                    SubServerThread.healthCheckStudents.add(SubServerThread.students.get(i));
                    msg(SubServerThread.students.get(i).name + "  selected to check health");
                }
            }
        }
        msg("notify the nurse to have a health check");
        synchronized (SubServerThread.nurseObj) {
            SubServerThread.nurseObj.notifyAll();
        }

        return "Choose Some students to have a health check";
    }

    public String goToClass(int number) {
        synchronized (SubServerThread.EveryoneOK) {
            try {
                SubServerThread.EveryoneOK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("notifyAll to begin a session");
        synchronized (SubServerThread.SessObjStart) {
            SubServerThread.SessObjStart.notifyAll();
        }
        try {
            sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("Class " + number + " is over");
        this.phyStudents.clear();
        synchronized (SubServerThread.ELAObjEnd) {
            SubServerThread.ELAObjEnd.notifyAll();
        }
        synchronized (SubServerThread.MathObjEnd) {
            SubServerThread.MathObjEnd.notifyAll();
        }
        synchronized (SubServerThread.PhyObjEnd) {
            SubServerThread.PhyObjEnd.notifyAll();
        }

        return "Class" + number + "is over";
    }

    public String takeABreak() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("Have a rest");
        return "Have a rest";
    }


    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] Principal: " + m);
    }
}
