package Patients;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class MyPatientTask {
    static String file = "dump.txt";
    static MyDequeWrapper listPatientStrings = new MyDequeWrapper();
    static List<Patient> patients = new ArrayList<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        GetStringList stringList = new GetStringList();
        GetPatientsList patientsList = new GetPatientsList();

        Future<MyDequeWrapper> future = executorService.submit(stringList);
        MyDequeWrapper listPatientStrings = future.get();

        Future<List<Patient>> future1 = executorService.submit(patientsList);
        List<Patient> patients = future1.get();

        executorService.shutdown();

        patients.forEach(System.out::println);
    }

    private static class GetStringList implements Callable<MyDequeWrapper>{
        @Override
        public MyDequeWrapper call() throws Exception {
            try (Scanner scanner = new Scanner(new File(file))) {
                String strPatient;
                while (scanner.hasNextLine()) {
                    strPatient = scanner.nextLine();
                    listPatientStrings.add(strPatient);}
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return listPatientStrings;
        }
    }
    private static class GetPatientsList implements Callable<List<Patient>>{
        @Override
        public List<Patient> call() throws Exception {
                String temp;
                while (!listPatientStrings.isEmpty()) {
                    try {
                        temp = listPatientStrings.pop().replaceAll(",", "")
                                .replaceAll("'", "")
                                .replace("(", "")
                                .replace(")", "");
                        Patient newPatient = new Patient(temp);
                        patients.add(newPatient);
                    } catch (InterruptedException e) {
                        System.out.println("Поток чтения из очереди прерван");
                    }
                }
            return patients;
        }
    }
}

class MyDequeWrapper {
    final Deque<String> listPatientStrings;

    public MyDequeWrapper() {
        this.listPatientStrings = new ArrayDeque<>();
    }

    public synchronized void add(String st) {
        listPatientStrings.add(st);
        notify();
    }

    public synchronized String pop() throws InterruptedException {
        while (listPatientStrings.isEmpty()) {
            wait();
        }
        return listPatientStrings.pop();
    }

    public synchronized boolean isEmpty() {
        return listPatientStrings.isEmpty();
    }

}


