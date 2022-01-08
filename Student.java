// Author: Fei Zhai
// Date: 04 / 2021

package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student extends Thread {
    public String name;
    public boolean isHealth;
    public boolean hasFinishedQuest;
    private List<String> classes;

    public Student(String name) {
        this.name = name;

        isHealth = healthInit();
        hasFinishedQuest = questInit();
        classes = new ArrayList<>(3);
    }

    private boolean healthInit() {
        msg(" Init HealthInfo");
        Random r = new Random(2);
        int ran = r.nextInt(100);
        if (ran < 3)
            return false;
        else
            return true;
    }

    private boolean questInit() {
        msg("Init Questionnaire Finishment");
        Random r = new Random(3);
        int ran = r.nextInt(100);
        if (ran < 15)
            return false;
        else
            return true;
    }

    public String waitOutside(Object sch) {
        Random r = new Random();
        int ran = r.nextInt(5000);
        try {
            sleep(ran);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SubServerThread.students.add(this);
        SubServerThread.numStudents++;
        msg("arrives, and waits outside school");

        synchronized (sch) {
            try {
                sch.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String statement = "enters the school and wait for the coming check";
        msg(statement);
        return statement;
    }

    public boolean getQuest() {
        if (hasFinishedQuest)
            return true;
        else {
            this.goHome();
            return false;
        }
    }

    public String checkQuest(Object obj) {
        msg(" ready to checkQuest");
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.hasFinishedQuest) {
            msg("finish checkQuest");
            return " finish checkQuest";
        } else {
            msg("GO HOME");
            return "GO HOME";
        }
    }


    public String checkHealth(Object obj) {
        msg("ready to healthCheck");
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (SubServerThread.healthCheckStudents.contains(this) && !this.isHealth) {
            return "GO Home";
        } else {
            msg("PASS HealthCheck");
            return " PASS HealthCheck";
        }
    }

    public String goToClass(int number) {
        String c = this.chooseClass();
        this.classes.add(c);
        msg("Go to Class " + number + "  " + c);

        if (SubServerThread.teachers[0].students.size() == 4 &&
                SubServerThread.teachers[1].students.size() == 4 &&
                SubServerThread.principle.phyStudents.size() + 8 == SubServerThread.numStudents) {
            synchronized (SubServerThread.EveryoneOK) {
                SubServerThread.EveryoneOK.notify();
            }
        }

        synchronized (SubServerThread.SessObjStart) {
            try {
                SubServerThread.SessObjStart.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (c.equals("ELA")) {
            synchronized (SubServerThread.ELAObjEnd) {
                try {
                    SubServerThread.ELAObjEnd.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (c.equals("Math")) {

            synchronized (SubServerThread.MathObjEnd) {
                try {
                    SubServerThread.MathObjEnd.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (c.equals("PhysicalEdu")) {
            synchronized (SubServerThread.PhyObjEnd) {
                try {
                    SubServerThread.PhyObjEnd.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        msg("Class " + number + " is Over");
        String str = " go to class " + c + ". And Class is Over";
        return str;
    }

    private synchronized String chooseClass() {
        if (classes.contains("ELA") && classes.contains("Math")) {
            SubServerThread.principle.phyStudents.add(this);
            return "PhysicalEdu";
        } else if (classes.contains("ELA") && !classes.contains("Math")) {
            if (SubServerThread.teachers[1].students.size() < 4) {
                SubServerThread.teachers[1].students.add(this);
                return "Math";
            } else {
                SubServerThread.principle.phyStudents.add(this);
                return "PhysicalEdu";
            }
        } else if (!classes.contains("ELA") && classes.contains("Math")) {

            if (SubServerThread.teachers[0].students.size() < 4) {
                SubServerThread.teachers[0].students.add(this);
                return "ELA";
            } else {
                SubServerThread.principle.phyStudents.add(this);
                return "PhysicalEdu";
            }

        } else {
            if (SubServerThread.teachers[0].students.size() < 4) {
                SubServerThread.teachers[0].students.add(this);

                return "ELA";
            } else if (SubServerThread.teachers[1].students.size() < 4) {
                SubServerThread.teachers[1].students.add(this);
                return "Math";
            } else {
                SubServerThread.principle.phyStudents.add(this);
                return "PhysicalEdu";
            }
        }
    }

    public String takeABreak() {
        msg("Have a rest");
        Random r = new Random();
        int ran = r.nextInt(1000);
        try {
            sleep(1000 + ran);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Have a rest";
    }

    public String goHome() {
        this.showClasses();
        return "GO HOME";
    }

    public void showClasses() {
        msg("A school day is Over! 3 classes are " + classes.get(0) + "  " + classes.get(1) + "  " + classes.get(2));
    }


    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] " + name + ": " + m);
    }
}

