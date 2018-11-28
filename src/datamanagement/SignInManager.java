package datamanagement;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import exceptions.StudentAlreadyExistsException;
import exceptions.StudentDoesNotExistException;
import utilities.Utils;

import java.io.IOException;
import java.util.Date;

public class SignInManager {

    private final static String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Help", "Group Work"
    };

    private final static String[] courses = new String[] {
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare"
    };

    private Database database;

    public SignInManager(Database database) {
        this.database = database;
    }

    public static String[] getReasons() {
        return reasons;
    }

    public static String[] getCourses() {
        return courses;
    }

    public boolean addStudent(int id, String firstName, String lastName, int grade) throws StudentAlreadyExistsException {

        Student existingStudent;

        try {
            existingStudent = database.findStudent(id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (existingStudent != null) {
            throw new StudentAlreadyExistsException();
        }

        Student newStudent = new Student(id, firstName, lastName, grade);
        return database.addStudent(newStudent);
    }

    public boolean removeStudent(int id) throws StudentDoesNotExistException {

        Student existingStudent;

        try {
            existingStudent = database.findStudent(id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (existingStudent == null) {
            throw new StudentDoesNotExistException();
        }

        return database.removeStudent(id);

    }

    public boolean signIn(int id, String courseWork, String reason, String courseMissed) throws InvalidIdException, AlreadyLoggedInException {

        //check if student exists
        Student student;

        try {
            student = database.findStudent(id);
        } catch (IOException e) {
            return false;
        }

        if (student == null) {
            throw new InvalidIdException(id);
        }

        try {
            if (database.isStudentSignedIn(id)) {
                throw new AlreadyLoggedInException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Session session = new Session(student, new Date(Utils.getTime()), reason, courseWork, courseMissed);
        return database.signIn(session);
    }

    public boolean signOut(int id) throws InvalidIdException, NotLoggedInException {

        //check if student exists
        Student student;

        try {
            student = database.findStudent(id);
        } catch (IOException e) {
            return false;
        }

        if (student == null) {
            throw new InvalidIdException(id);
        }

        try {
            if (!database.isStudentSignedIn(id)) {
                throw new NotLoggedInException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return database.signOut(id);
    }

    public void close() {
        database.close();
    }

}