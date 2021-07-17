// package stars;

import java.io.IOException;
import java.util.*; 

/**
 * Index class containing all relevant info for an index.
 * @author Group7
 *
 */
public class Index {
	private String courseCode;
	private int vacancies;
	private String indexNo;
	private int maxNumberOfStudents;
	private int numberRegistered;
	private ArrayList<StudentUser> registeredStudents;
	private ArrayList<Lesson> lessons;
	private Queue<StudentUser> waitlistedStudents;
	private int numberWaitlisted;
	private int numAUs;
	
	Index(){
		waitlistedStudents = new LinkedList<>();
		lessons = new ArrayList<Lesson>();
		registeredStudents = new ArrayList<StudentUser>();
	}
	Index(String courseCode, int vacancies, String indexNo, int maxNumberOfStudents, int numberRegistered
			, int numberWaitlisted) throws IOException{
		waitlistedStudents = new LinkedList<>();
		lessons = new ArrayList<Lesson>();
		registeredStudents = new ArrayList<StudentUser>();
		this.courseCode = courseCode;
		this.vacancies = vacancies;
		this.indexNo = indexNo;
		this.maxNumberOfStudents = maxNumberOfStudents;
		this.numberRegistered = numberRegistered;
		this.numberWaitlisted = numberWaitlisted;
	}
	public String getCourseCode() {
		return this.courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public int getVacancies() {
		return this.vacancies;
	}
	public void setVacancies(int vacancies) {
		this.vacancies = vacancies;
	}
	public String getIndexNumber() {
		return this.indexNo;
	}
	public void setIndexNumber (String indexNo) {
		this.indexNo = indexNo;
	}
	public int getMaxNumberOfStudents() {
		return this.maxNumberOfStudents;
	}
	public void setMaxNumberOfStudents(int maxNumberOfStudents) {
		this.maxNumberOfStudents = maxNumberOfStudents;
	}
	public int getNumberWaitlisted() {
		return this.numberWaitlisted;
	}
	public void setNumberWaitlisted(int numberWaitlisted) {
		this.numberWaitlisted = numberWaitlisted;
	}
	public int getNumberRegistered() {
		return this.numberRegistered;
	}
	public void setNumberRegistered(int numberRegistered) {
		this.numberRegistered = numberRegistered;
	}
	public ArrayList<Lesson> getLessons(){
		return lessons;
	}
	/**
	 * Adds a student to a course.
	 * @param student
	 */
	public void addStudent(StudentUser student) {
		registeredStudents.add(student);
		student.addRegisIndex(this);
		student.setAU(student.getAU() + numAUs);
		numberRegistered++;
		vacancies--;
	}
	/**
	 * Removes a student from the course without affecting the waitlist.
	 * Used in swopping indexes.
	 * @param student
	 */
	public void removeStudentNoWL(StudentUser student) {
		registeredStudents.remove(student);
		student.removeRegisIndex(this);
		student.setAU(student.getAU() - numAUs);
		numberRegistered--;
		vacancies++;
	}
	/**
	 * Removes a student from the course and allocates the first waitlisted queue student to the slot.
	 * @param student
	 */
	public void removeStudent(StudentUser student) {
		registeredStudents.remove(student);
		student.removeRegisIndex(this);
		student.setAU(student.getAU() - numAUs);
		numberRegistered--;
		vacancies++;
		if (numberWaitlisted > 0) {
			removeWaitlist();
		}
	}
	/**
	 * Adds a student to the waitlist.
	 * @param student
	 */
	public void addWaitlist(StudentUser student) {
		waitlistedStudents.add(student);
		student.addWaitIndex(this);
		numberWaitlisted++;
	}
	/**
	 * Allocates a waitlisted student to be registered.
	 * Notifies the waitlisted student by email that they have been allocated the course.
	 */
	public void removeWaitlist() {
		StudentUser student = waitlistedStudents.poll();
		this.addStudent(student);
		SendEmail.notify(student.getEmail(), courseCode, indexNo);
		student.removeWaitIndex(this);
		numberWaitlisted--;
	}
	/**
	 * Add lesson to this index.
	 * @param newLesson
	 */
	public void addLesson(Lesson newLesson) {
		lessons.add(newLesson);
	}
	/**
	 * @return ArrayList of registered students
	 */
	public ArrayList<StudentUser> getRegisteredStudents() {
		return registeredStudents;
	}
	/**
	 * @return Queue of waitlisted students.
	 */
	public Queue<StudentUser> getWaitlistedStudents() {
		return waitlistedStudents;
	}
	public int getNumAUs() {
		return numAUs;
	}
	public void setNumAUs(int numAUs) {
		this.numAUs = numAUs;
	}
}