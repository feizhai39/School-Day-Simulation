package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class SubServerThread extends Thread {
    private static final String STUDENT = "Student";
    private static final String TEACHER = "Teacher";
    private static final String PRINCIPAL = "Principal";
    private static final String NURSE = "Nurse";

    public static List<Student> students = new ArrayList<>(20);
    public static Teacher[] teachers = new Teacher[2];
    public static int numStudents = 0;
    public static int sickStuNumber = 0;

    public static List<Student> healthCheckStudents = new ArrayList<>();


    private final Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    private Student student;
    private Teacher teacher;
    public static Principal principle;
    private Nurse nurse;

    private static final Object sch = new Object();
    public static final Object questObj = new Object();
    public static final Object nurseObj = new Object();
    public static final Object healthObj = new Object();
    public static final Object EveryoneOK = new Object();
    public static final Object SessObjStart = new Object();
    public static final Object ELAObjEnd = new Object();
    public static final Object MathObjEnd = new Object();
    public static final Object PhyObjEnd = new Object();

    public SubServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            String s = br.readLine();
            msg(s + " Client Connected To the SubServer Thread");

            if (s.startsWith(STUDENT)) {
                student = new Student(s);
            } else if (s.startsWith(PRINCIPAL)) {
                principle = new Principal();
            } else if (s.startsWith(TEACHER)) {
                teacher = new Teacher(s);
                if (s.endsWith("ELA")) {
                    teachers[0] = teacher;
                } else {
                    teachers[1] = teacher;
                }
            } else if (s.startsWith(NURSE)) {
                nurse = new Nurse();
            }
            while (true) {
                if (!socket.isClosed()) {
                    try {
                        String str = br.readLine();
                        if (str == null || str.equals("Over"))
                            break;
                        String threadType = str.split(" ")[0];
                        int number = Integer.parseInt(str.split(" ")[1]);
                        String ret = runMethod(threadType, number);
//                        msg(ret);
                        pw.println(ret);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            msg("Client Socket Closing. Simulations Over");
            try {
                if (pw != null)
                    pw.close();
                if (br != null)
                    br.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String runMethod(String type, int number) {
        String ret = "";
        if (type.startsWith(STUDENT)) {
            switch (number) {
                case 0:
                    ret = student.waitOutside(sch);
                    break;
                case 1:
                    ret = student.checkQuest(questObj);
                    break;
                case 2:
                    ret = student.checkHealth(healthObj);
                    break;
                case 3:
                    ret = student.goToClass(1);
                    break;
                case 4:
                    ret = student.takeABreak();
                    break;
                case 5:
                    ret = student.goToClass(2);
                    break;
                case 6:
                    ret = student.takeABreak();
                    break;
                case 7:
                    ret = student.goToClass(3);
                    break;
                case 8:
                    ret = student.goHome();
                    break;
                default:
                    ret = student.goHome();
            }
        } else if (type.startsWith(PRINCIPAL)) {
            switch (number) {
                case 0:
                    ret = principle.arrive(sch);
                    break;
                case 1:
                    ret = principle.checkQuest(questObj);
                    break;
                case 2:
                    ret = principle.chooseCheck();
                    break;
                case 3:
                    ret = principle.goToClass(1);
                    break;
                case 4:
                    ret = principle.takeABreak();
                    break;
                case 5:
                    ret = principle.goToClass(2);
                    break;
                case 6:
                    ret = principle.takeABreak();
                    break;
                case 7:
                    ret = principle.goToClass(3);
                    break;
            }
        } else if (type.startsWith(NURSE)) {
            switch (number) {
                case 0:
                    ret = nurse.checkHealth();
                    break;
            }
        } else if (type.startsWith(TEACHER)) {
            switch (number) {
                case 0:
                    ret = teacher.goToClass(1);
                    break;
                case 1:
                    ret = teacher.takeABreak();
                    break;
                case 2:
                    ret = teacher.goToClass(2);
                    break;
                case 3:
                    ret = teacher.takeABreak();
                    break;
                case 4:
                    ret = teacher.goToClass(3);
                    break;
            }
        }

        return ret;
    }


    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] SubServerThread: " + m);
    }
}
