// Author: Fei Zhai
// Date: 04 / 2021

package Client;

import java.io.PrintWriter;

public class Client {
    private static int numStudent = 20;
    public static void main(String[] args) throws InterruptedException {
        
        for(int i=0;i<numStudent;i++){
            StudentClient sc = new StudentClient();
            sc.setNames("Student" + i);
            sc.start();
        }

        Thread.sleep(6000);

        PrincipalClient pc = new PrincipalClient();
        pc.setNames("Principal");
        pc.start();

        Thread.sleep(1000);

        NurseClient nc = new NurseClient();
        nc.setNames("Nurse");
        nc.start();

        TeacherClient tc1 = new TeacherClient();
        tc1.setNames("TeacherELA");
        tc1.start();

        TeacherClient tc2 = new TeacherClient();
        tc2.setNames("TeacherMath");
        tc2.start();

    }
}

