package Patients;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class PatientTask {
    static String file = "dump.txt";
    static DequeWrapper newPatientStrings = new DequeWrapper();
    static List<Patient> patients = new ArrayList<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {


//        MyRunnable runnable = new PatientString();
        Thread thread1 = new Thread(new PatientString());
        Thread thread2 = new Thread(new Patients());

        thread1.start();
        thread2.start();

        thread1.join();
//      thread1.interrupt();
        thread2.join(5000);

        patients.forEach(System.out::println);

//        System.out.println(newPatientStrings.size());
//        System.out.println(newPatientStrings);
    }


    static class PatientString implements Runnable {

        @Override
        public void run() {
            //try (Scanner scanner = new Scanner(new FileReader(file))) {
            try (Scanner scanner = new Scanner(new File(file))) {
                String newPatientString;
                while (scanner.hasNextLine()) {  //|| scanner.nextLine() != null

                    newPatientString = scanner.nextLine();
 //                   if(newPatientString != null){
                    newPatientStrings.add(newPatientString);}
//                    else {
//                        System.out.println("yes");
//                        break;
//                    }

//                }

//                scanner.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Patients implements Runnable {
        @Override
        public void run() {
            while (true) {
                String temp;
                try {
                    temp = newPatientStrings.pop().replaceAll(",", "")
                            .replaceAll("'", "")
                            .replace("(", "")
                            .replace(")", "");
                    Patient newPatient = new Patient(temp);
                    patients.add(newPatient);
                } catch (InterruptedException e) {
                    System.out.println("Поток чтения из очереди прерван");
                }
            }
        }
    }
}

class DequeWrapper {
    final Deque<String> newPatientStrings;

    public DequeWrapper() {
        this.newPatientStrings = new ArrayDeque<>();
    }

    public synchronized void add(String st) {
        newPatientStrings.add(st);
        notify();
    }

    public synchronized String pop() throws InterruptedException {
        while (newPatientStrings.isEmpty()) {
            wait();
        }
        return newPatientStrings.pop();
    }

    public synchronized boolean isEmpty() {
        return newPatientStrings.isEmpty();
    }

}


//
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.*;
//import java.util.stream.Stream;
//
//public class PatientTask {
//    static List<Patient> patients;
//    public static void main(String[] args) throws InterruptedException {
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Load load = new Load();
//        Future<List<Patient>> future = executorService.submit(load);
//        try {
//            patients = future.get();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        finally {
//            executorService.shutdown();
//        }
//        System.out.println(patients);
//    }
//}
//
//class Dump {
//    static String file = "dump.txt";
//    public static List<Patient> getDump() {
//        try(Stream<String> strings = Files.lines(Paths.get(file))) {
//            return strings
//                    .map(e -> e
//                            .replaceAll(",", "")
//                            .replaceAll("'", "")
//                            .replace("(", "")
//                            .replace(")", ""))
//                    .map(Patient::new)
//                    .toList();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//
//class Load implements Callable<List<Patient>> {
//    @Override
//    public List<Patient> call() throws Exception {
//        return Dump.getDump();
//    }
//}
//class PatientString implements Callable<String>{
//    @Override
//    public String call() throws Exception {
//        String file = "dump.txt";
//        Scanner scanner = new Scanner(new FileReader(file));
//        String newPatientString =
//        return null;
//    }
//}
