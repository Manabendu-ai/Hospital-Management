package com.hospital.riku;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.print.Doc;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Management {

    private Config cfg;
    private Session session;
    private Transaction transaction;
    private Scanner sc;
    private SessionFactory sf;

    public Management(){
        sc = new Scanner(System.in);
        cfg = new Config();
        sf = cfg.buildConfig()
                .addAnnotatedClass(com.hospital.riku.Patient.class)
                .addAnnotatedClass(com.hospital.riku.Doctor.class)
                .addAnnotatedClass(com.hospital.riku.Appointment.class)
                .buildSessionFactory();
        session = sf.openSession();
        transaction = session.beginTransaction();
    }

    public void dashboard(){
        System.out.println("\n================ MK HOSPITAL ================\n");
        System.out.println("1) ADD PATIENT");
        System.out.println("2) VIEW PATIENTS");
        System.out.println("3) ADD DOCTOR");
        System.out.println("4) VIEW DOCTORS");
        System.out.println("5) BOOK APPOINTMENT");
        System.out.println("6) EXIT");
        System.out.print(">> ");
    }

    public void addPatient(){
        int id,age;
        char gender;
        String name;
        try {
            System.out.println("ENTER PATIENT ID: ");
            id = sc.nextInt();
            sc.nextLine();
            if (isExisting(id,1)) {
                System.out.println("PATIENT WITH SAME ID EXISTS!");
                return;
            }
            System.out.println("ENTER PATIENT NAME: ");
            name = sc.nextLine();
            System.out.println("ENTER PATIENT AGE: ");
            age = sc.nextInt();
            sc.nextLine();
            System.out.println("ENTER PATIENT GENDER: ");
            gender = sc.next().charAt(0);
            Patient p = new Patient();
            p.setPatient_id(id);
            p.setPatient_name(name);
            p.setPatient_age(age);
            p.setGender(gender);

            session.persist(p);
            transaction.commit();
            System.out.println("PATIENT ADDED SUCCESSFULLY!");
        } catch (Exception e){
            transaction.rollback();
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
            sc.nextLine();
        }

    }

    public void addDoctor(){
        int id;
        String name,spec;
        try {
            System.out.println("ENTER DOCTOR ID: ");
            id = sc.nextInt();
            sc.nextLine();
            if (isExisting(id,2)) {
                System.out.println("DOCTOR WITH SAME ID EXISTS!");
                return;
            }
            System.out.println("ENTER DOCTOR NAME: ");
            name = sc.nextLine();
            System.out.println("ENTER DOCTOR SPECIALIZATION: ");
            spec = sc.nextLine();
            Doctor d = new Doctor();
            d.setDoctor_id(id);
            d.setDoctor_name(name);
            d.setSpec(spec);
            session.persist(d);
            transaction.commit();
            System.out.println("DOCTOR ADDED SUCCESSFULLY!");
        } catch (Exception e){
            transaction.rollback();
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
            sc.nextLine();
        }

    }

    public boolean isExisting(int id, int m){
        if(m==1) {
            Patient p = session.find(Patient.class, id);
            return p != null;
        } else {
            Doctor d = session.find(Doctor.class, id);
            return d != null;
        }
    }

    public void viewPatients(){
        try {
            List<Patient> patients = session.createQuery("from Patient",Patient.class).getResultList();
            if(patients==null){
                System.out.println("NO PATIENTS IN THE RECORD!!");
                return;
            }
            int idWidth = 12;
            int nameWidth = 20;
            int ageWidth = 12;
            int genderWidth = 10;
            String border = "+" + "-".repeat(idWidth + 2)
                    + "+" + "-".repeat(nameWidth + 2)
                    + "+" + "-".repeat(ageWidth + 2)
                    + "+" + "-".repeat(genderWidth + 2)
                    + "+";

            System.out.println(border);

            System.out.printf("| %-"+idWidth+"s | %-"+nameWidth+"s | %-"+ageWidth+"s | %-"+genderWidth+"s |%n",
                    "Patient ID", "Patient NAME", "Patient AGE", "Gender");
            System.out.println(border);

            for (Patient p : patients) {
                System.out.printf("| %-"+idWidth+"d | %-"+nameWidth+"s | %-"+ageWidth+"d | %-"+genderWidth+"s |%n",
                        p.getPatient_id(), p.getPatient_name(), p.getPatient_age(), p.getGender());
            }
            System.out.println(border);

        } catch (Exception e){
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
        }

    }

    public void viewDoctors(){
        try {
            List<Doctor> doctors = session.createQuery("from Doctor ",Doctor.class).getResultList();
            if(doctors==null){
                System.out.println("NO DOCTORS IN THE RECORD!!");
                return;
            }
            int idWidth = 12;
            int nameWidth = 20;
            int spWidth = 20;
            String border = "+" + "-".repeat(idWidth + 2)
                    + "+" + "-".repeat(nameWidth + 2)
                    + "+" + "-".repeat(spWidth + 2)
                    + "+";

            System.out.println(border);

            System.out.printf("| %-"+idWidth+"s | %-"+nameWidth+"s | %-"+spWidth+"s |%n",
                    "Doctor ID", "Doctor NAME", "SPECIALIZATION");
            System.out.println(border);

            for (Doctor d : doctors) {
                System.out.printf("| %-"+idWidth+"d | %-"+nameWidth+"s | %-"+spWidth+"s |%n",
                        d.getDoctor_id(),d.getDoctor_name(),d.getSpec());
            }
            System.out.println(border);

        } catch (Exception e){
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
        }

    }
    public boolean isFull(int did, String date){
        Query<Long> query = session.createQuery("select count(a) from Appointment a where a.doctor.doctor_id= ?1 and a.app_date = ?2 ",Long.class);
        query.setParameter(1,did);
        query.setParameter(2,date);
        Long count = query.getSingleResult();
        return count >= 5;
    }

    public int getAppId(){
        int app_id = 0;
        Query<Integer> query = session.createQuery("select a.app_id from Appointment a order by a.app_id desc",Integer.class).setMaxResults(1);
        app_id = query.getSingleResult();
        return app_id;
    }
    public void bookAppointment(){
        int pid, did;
        String date;
        Patient patient;
        Doctor doctor;
        try{
            System.out.println("ENTER THE PATIENT ID: ");
            pid = sc.nextInt();
            patient = session.find(Patient.class, pid);
            if(patient==null){
                System.out.println("PATIENT WITH ID: "+pid+" DOES NOT EXIST!");
                return;
            }
            System.out.println("ENTER THE DOCTOR ID: ");
            did = sc.nextInt();
            doctor = session.find(Doctor.class, did);
            if(doctor==null){
                System.out.println("DOCTOR WITH ID: "+did+" DOES NOT EXIST!");
                return;
            }
            sc.nextLine();
            System.out.println("ENTER APPOINTMENT DATE (YYYY-MM-DD): ");
            date = sc.nextLine();
            if(isFull(did,date)){
                System.out.println("BAD LUCK! THE APPOINTMENT IS FULL ON: "+date);
                return;
            }
            Appointment app = new Appointment();
            app.setApp_id(getAppId()+1);
            app.setPatient(patient);
            app.setDoctor(doctor);
            app.setApp_date(date);
            System.out.println("APPOINTMENT BOOKED SUCCESSFULLY!");
        } catch (Exception e){
            transaction.rollback();
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
            sc.nextLine();
        }
    }
    public void viewAppointments(){
        try {
            String q = """
                    from Appointment
                    """;
            List<Appointment> appointments = session.createQuery(q,Appointment.class).getResultList();
            if(appointments==null){
                System.out.println("NO APPOINTMENTS IN THE RECORD!!");
                return;
            }
            int idWidth = 15;
            int pWidth = 20;
            int dWidth = 20;
            int dtWidth = 20;
            String border = "+" + "-".repeat(idWidth + 2)
                    + "+" + "-".repeat(pWidth + 2)
                    + "+" + "-".repeat(dWidth + 2)
                    + "+" + "-".repeat(dtWidth + 2)
                    + "+";

            System.out.println(border);

            System.out.printf("| %-"+idWidth+"s | %-"+pWidth+"s | %-"+dWidth+"s | %-"+dtWidth+"s |\n",
                    "Appointment ID", "Patient NAME", "Doctor NAME", "Appointment DATE");
            System.out.println(border);

            for (Appointment a : appointments) {
                System.out.printf("| %-"+idWidth+"s | %-"+pWidth+"s | %-"+dWidth+"s | %-"+dtWidth+"s |\n",
                        a.getApp_id(),a.getPatient().getPatient_name(),a.getDoctor().getDoctor_name(),a.getApp_date());
            }
            System.out.println(border);

        } catch (Exception e){
            System.out.println("AN EXCEPTION HAS OCCURRED PLEASE RE ENTER!");
        }

    }

    public void exe(){
        int choice;
        while (true){
            dashboard();
            choice = sc.nextInt();
            switch (choice){
                case 1: addPatient();
                    break;
                case 2: viewPatients();
                    break;
                case 3: addDoctor();
                    break;
                case 4: viewDoctors();
                    break;
                case 5: bookAppointment();
                    break;
                case 6:
                    System.out.println("EXITING HOSPITAL MANAGEMENT APP!");
                    sf.close();
                    session.close();
                    System.exit(0);
                default:
                    System.out.println("INVALID OPTION! PLEASE TRY AGAIN!");
            }
        }
    }


    public static void main(String[] args) {
        Management management = new Management();
        management.exe();
    }
}
