// package stars;

import java.util.ArrayList;

/**
 * Acts to store and manage all courses that exist.
 * @author Group7
 *
 */
public class CourseManager {
	/**
	 * ArrayList of all courses that exist.
	 */
	private ArrayList<CourseInformation> courseRecords;
	/**
	 * Creates a course manager object and initializes an empty "course records".
	 */
	public CourseManager() {
		courseRecords = new ArrayList<CourseInformation>();
	}
	/**
	 * Adds a course into our courseRecords.
	 * @param course
	 */
	public void addCourse(CourseInformation course) {
		courseRecords.add(course);
	}
	/**
	 * Checks for the number of vacancies in a index for a course.
	 * @param courseCode
	 * @param indexNo
	 * @return number of vacancies.
	 */
	public int checkVacancies(String courseCode, String indexNo) {
		Index index = this.getIndex(courseCode, indexNo);
		return index.getVacancies();
	}
	/**
	 * Prints list of students in a particular index in a course.
	 * @param courseCode
	 * @param indexNo
	 */
	public void printStudentsInIndex(String courseCode, String indexNo) {
		System.out.printf("List of students in course %s index %s:\n", courseCode, indexNo);
		Index index = this.getIndex(courseCode, indexNo);
		for (StudentUser s:index.getRegisteredStudents()) {
			System.out.printf("%s %s %s\n", s.getName(), s.getGend(), s.getNat());
		}
	}
	/**
	 * Prints list of all students in a particular course.
	 * @param courseCode
	 */
	public void printStudentsInCourse(String courseCode) {
		System.out.printf("List of students in course %s:\n", courseCode);
		CourseInformation course = this.getCourse(courseCode);
		for (Index i:course.getIndexes()) {
			for (StudentUser s:i.getRegisteredStudents()) {
				System.out.printf("%s %s %s\n", s.getName(), s.getGend(), s.getNat());
			}
		}
	}
	public ArrayList<CourseInformation> getCourseRecords() {
		return courseRecords;
	}
	/**
	 * Gets the reference for a course object, given its unique course code.
	 * @param courseCode
	 * @return relevant course object
	 */
	public CourseInformation getCourse(String courseCode) {
		for (CourseInformation c:this.getCourseRecords()) {
			if (c.getCourseCode().compareTo(courseCode) == 0) {
				return c;
			}
		}
		System.out.printf("Course not found.\n");
		return null;
	}
	/**
	 * Gets the reference for an index object, given its unique course code and index number.
	 * @param courseCode
	 * @param indexNo
	 * @return relevant index object
	 */
	public Index getIndex(String courseCode, String indexNo) {
		for (CourseInformation c:this.getCourseRecords()) {
			if (c.getCourseCode().compareTo(courseCode) == 0) {
				for (Index i:c.getIndexes()) {
					if (i.getIndexNumber().compareTo(indexNo) == 0) {
						return i;
					}
				}
				System.out.printf("Index not found.\n");
				return null;
			}
		}
		System.out.printf("Course not found.\n");
		return null;
	}
	/**
	 * Registers a student for a course.
	 * @param student
	 * @param courseCode
	 * @param indexNo
	 */
	public void registerStudent(StudentUser student, String courseCode, String indexNo) {
		Index index = getIndex(courseCode, indexNo);
		index.addStudent(student);		
	}
	/**
	 * Deregisters a student from a course.
	 * @param student
	 * @param courseCode
	 * @param indexNo
	 */
	public void deregisterStudent(StudentUser student, String courseCode, String indexNo) {
		Index index = getIndex(courseCode, indexNo);
		index.removeStudent(student);		
	}
	/**
	 * Swops the index of a course for 2 students who are registered to the course.
	 * @param student1
	 * @param student2
	 * @param courseCode
	 */
	public void swopIndexes(StudentUser student1, StudentUser student2, String courseCode) {
		String firstIndex = student1.getIndexNum(courseCode);
		String secondIndex = student2.getIndexNum(courseCode);
		Index index1 = getIndex(courseCode, firstIndex);
		Index index2 = getIndex(courseCode, secondIndex);
		index1.removeStudentNoWL(student1);
		index2.removeStudentNoWL(student2);
		index1.addStudent(student2);
		index2.addStudent(student1);
	}
	/**
	 * Waitlists a student to a course.
	 * @param student
	 * @param courseCode
	 * @param indexNo
	 */
	public void waitlistStudent(StudentUser student, String courseCode, String indexNo) {
		Index index = getIndex(courseCode, indexNo);
		index.addWaitlist(student);		
	}
	/**
	 * Prints a list of all courses.
	 */
	public void printCourseList() {
		for (CourseInformation c:courseRecords) {
			System.out.printf("%s %s\n", c.getCourseCode(), c.getCoursename());
		}
	}
	/**
	 * Checks whether a course exists given its unique course code.
	 * @param courseCode
	 * @return true if the course exists, false otherwise.
	 */
	public boolean courseExists(String courseCode) {
		for (CourseInformation c:courseRecords) {
			if (c.getCourseCode().compareTo(courseCode) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether an index exists in a certain course, given their unique course code and index number.
	 * @param courseCode
	 * @param indexNo
	 * @return true if the course has the index, otherwise false.
	 */
	public boolean indexExistsInCourse(String courseCode, String indexNo) {
		for (CourseInformation c:courseRecords) {
			if (c.getCourseCode().compareTo(courseCode) == 0) {
				for (Index i:c.getIndexes()) {
					if (i.getIndexNumber().compareTo(indexNo) == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
}