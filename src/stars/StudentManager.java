// package stars;

import java.util.ArrayList;

/**
 * Acts to store and manage all students that exist.
 * @author Group7
 *
 */
public class StudentManager {
	/**
	 * ArrayList of studentUsers, sorted by their matric number
	 */
	private ArrayList<StudentUser> studentRecords;
	private int numStudents = 0;
	
	public StudentManager() {
		// instantiate a student ArrayList
		studentRecords = new ArrayList<StudentUser>();
	}
	
	public ArrayList<StudentUser> getStudentRecords() {
		return studentRecords;
	}

	/**
	 * Adds a student into the student records, and sorts the added student by its matric number.
	 * @param student
	 */
	public void addStudent(StudentUser student) {
		studentRecords.add(student);
		// sorts new student by matric number
		for (int j = numStudents; j > 0; j--) {
			if (studentRecords.get(j).getMatric().compareTo(studentRecords.get(j-1).getMatric()) < 0) {
				student = studentRecords.get(j-1);
				studentRecords.set(j-1, studentRecords.get(j));
				studentRecords.set(j, student);
			}
		}
		numStudents++;
	}

	public int getNumStudents() {
		return numStudents;
	}	
	
	/**
	 * Prints list of all students, sorted by matric number.
	 */
	public void printStudentList() {
		for (StudentUser s:studentRecords) {
			System.out.printf("%s %s\n", s.getMatric(), s.getName());
		}
	}
	
	/**
	 * Gets a StudentUser object from its matric number.
	 * @param matricNum
	 * @return the corresponding student object
	 */
	public StudentUser getStudent(String matricNum) {
		for (StudentUser s:studentRecords) {
			if (s.getMatric().compareTo(matricNum) == 0) {
				return s;
			}
		}
		System.out.printf("Student not found.\n");
		return null;
	}

	/**
	 * Checks if a certain student exists, given its matric number
	 * @param matricNum
	 * @return true if the student exists, otherwise false
	 */
	public boolean studentExists(String matricNum) {
		for (StudentUser s:studentRecords) {
			if (s.getMatric().compareTo(matricNum) == 0) {
				return true;
			}
		}
		return false;
	}
}