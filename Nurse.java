// Author: Fei Zhai
// Date: 04 / 2021

package Server;

public class Nurse extends Thread {
    public String checkHealth() {
        msg("arrives at school, and waits for the check list");
        synchronized (SubServerThread.nurseObj) {
            try {
                SubServerThread.nurseObj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        msg("begin to test");
        int sickNumber = 0;
        synchronized (SubServerThread.healthCheckStudents) {
            if (!SubServerThread.healthCheckStudents.isEmpty()) {
                for (int i = 0; i < SubServerThread.healthCheckStudents.size(); i = i + 2) {
                    //msg("Round " + i / 2 + 1);
                	msg("Round " + i / 2 );
                    Student stu = SubServerThread.healthCheckStudents.get(i);
                    msg(stu.name);
                    if (!stu.isHealth) {
                        sickNumber++;
                        SubServerThread.students.remove(stu);
                        SubServerThread.sickStuNumber--;
                    }
                    if (i + 1 < SubServerThread.healthCheckStudents.size()) {
                        Student stu1 = SubServerThread.healthCheckStudents.get(i + 1);
                        msg(stu1.name);
                        if (!stu1.isHealth) {
                            sickNumber++;
                            SubServerThread.students.remove(stu1);
                            SubServerThread.sickStuNumber--;
                        }
                    }
                }
                msg(sickNumber + " Students are sick");
            }
        }

        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("notify all health student to go to class");
        synchronized (SubServerThread.healthObj) {
            SubServerThread.healthObj.notifyAll();
        }

        return sickNumber + " Students are sick";
    }


    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] Nurse: " + m);
    }
}

