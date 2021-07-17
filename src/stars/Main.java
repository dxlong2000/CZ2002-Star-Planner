// package stars;

import java.util.*;
import javax.crypto.*;

import java.io.BufferedReader;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.text.*;

/**
 * Main programme body for our MySTARS application.
 * @author Group7
 *
 */
public class Main {
	public static void printseg() {
		System.out.println("=================================================================================");
	}
	enum Programmes {
		CS,
		MACS,
		MAS
	}
	public static void getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date custDate = new Date();
		System.out.println(sdf.format(custDate));
	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
	NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			IOException, ParseException {
		String choice;
		boolean quit = false;
		getDateTime();
		
		// create student records
		StudentDatabase studentDB = new StudentDatabase("StudentsDB.csv");
		StudentManager studentManager = new StudentManager();
		studentDB.read(studentManager);
		
		// create course records
		CourseDatabase courseDB = new CourseDatabase("CourseDB.csv");
		CourseManager courseManager = new CourseManager();
		courseDB.read(courseManager);

		// populate course records with indexes
		IndexDatabase indexDB = new IndexDatabase("IndexDB.csv");
		indexDB.read(courseManager, studentManager);
		
		// populate course indexes with lessons
		LessonDatabase lessonDB = new LessonDatabase("LessonDB.csv");
		lessonDB.read(courseManager);
		
		// get access times
		AccessDatabase accessDB = new AccessDatabase("AccessDB.csv");
		accessDB.read();
		
		// generics
		StudentUser student0 = new StudentUser();
		CourseInformation course0 = new CourseInformation();
		Index index0 = new Index();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("WELCOME TO MYSTARS");
		String domain = "";
		String username = "1";
		do {
			printseg();
			String[] a = LoginOut.login();
			username = a[0];
			domain = a[1];
			if(domain == "STUDENT" || domain == "STAFF")
				break;
		}while (domain=="");
		 
		if (domain == "STUDENT") {
			boolean access = false;
			// printseg();
			System.out.println(username);
			StudentUser studentNow = student0;
			StudentUser studentI = student0;
			for (StudentUser s:studentManager.getStudentRecords()) {
				if (s.getNID().compareTo(username) == 0) {
					studentNow = s;
				}
			}
			String string1 = studentNow.accessBegin();
		    Date time1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(string1);
		    Calendar calendar1 = Calendar.getInstance();
		    calendar1.setTime(time1);
		    calendar1.add(Calendar.DATE, 1);

		    String string2 = studentNow.accessEnd();
		    Date time2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(string2);
		    Calendar calendar2 = Calendar.getInstance();
		    calendar2.setTime(time2);
		    calendar2.add(Calendar.DATE, 1);
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date custDate = new Date();
		    String currentTime = sdf.format(custDate);
		    Date d = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(currentTime);
		    Calendar calendar3 = Calendar.getInstance();
		    calendar3.setTime(d);
		    calendar3.add(Calendar.DATE, 1);

		    Date x = calendar3.getTime();
		    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
				access = true;
				System.out.printf("Login is in the allowed period. Welcome %s %s!\n", domain, username);
				printseg();
		    }
		    else {
		    	System.out.println("You are not allowed to access the system.");
		    	System.out.println("Current time: " + currentTime);
		    	System.out.println("Allowed access time: " + time1 + " to " + time2);
		    	printseg();
			}
		    // printseg();
			if (access == true) {
				do {
					System.out.println("(1) Add course");
					System.out.println("(2) Drop course");
					System.out.println("(3) Check/Print Courses Registered");
					System.out.println("(4) Check Vacancies Available");
					System.out.println("(5) Change Index Number of Course");
					System.out.println("(6) Swop Index Number with Another Student");
					System.out.println("(7) Check Waitlisted courses");
					System.out.println("(8) Quit the programme");
					Timetable ttb = new Timetable();
					System.out.print("Enter choice: ");
					choice = sc.nextLine();
					printseg();
					String courseCode;
					String indexNo;

					switch(choice) {
						case "1":
							System.out.printf("Which course would you like to add? ");
							courseCode = sc.nextLine().toUpperCase();
							while (true)  {
								 if (courseManager.courseExists(courseCode)) {
									 break;
								 }
								 else {
									System.out.printf("Course does not exist. Enter course code again: ");
									courseCode = sc.nextLine().toUpperCase();
								 }
							}
							course0 = courseManager.getCourse(courseCode);
							System.out.printf("Indexes for this course: \n");
							for (Index i:course0.getIndexes()) {
								System.out.printf("%s, %d vacancies\n", i.getIndexNumber(), i.getVacancies());
							}
							System.out.printf("Which index would you like to add? ");
							indexNo = sc.nextLine().toUpperCase();
							index0 = courseManager.getIndex(courseCode, indexNo);
							while (true) {
								if (courseManager.indexExistsInCourse(courseCode, indexNo)) {
									break;
								}
								else {
									System.out.printf("Index does not exist. Enter index again: ");
									indexNo = sc.nextLine().toUpperCase();
									index0 = courseManager.getIndex(courseCode, indexNo);
								}
							}
							boolean already = false;
							//iterate through student's registered courses to find if there is any matches
							for (Index i:studentNow.getRegisIndexes()) {
								if (i.getCourseCode().compareTo(courseCode) == 0) {
									System.out.println("Error ! You have already registered for this course.");
									printseg();
									already = true;
									break;
								}
							}
							boolean available = false;
							for (String p:course0.getProgrammeAvailable()) {
								if (studentNow.getProg().compareTo(p) == 0) {
									available = true;
								}
							}
							if (available == false) {
								System.out.println("This course is not available for your programme.\n");
								printseg();
								break;
							}
							boolean exceed = false;
							if (studentNow.getAU() + course0.getNumAUs() > 21) {
								exceed = true;
								System.out.println("21AU limit exceeded ! You cannot register for this course.");
								printseg();
								break;
							}
							if(courseManager.checkVacancies(courseCode, indexNo) > 0) {
								if (already==false & available==true & exceed==false) {
									ttb = new Timetable();
									for (Index i:studentNow.getRegisIndexes()) {
										ttb.addIndex(i);
									}
									ttb.addIndex(courseManager.getIndex(courseCode,indexNo));
									if (ttb.checkClash()==true) {
										System.out.println("Timetable clashed! You cannot register for this course.");
										printseg();
										break;
									}
									else {
										courseManager.registerStudent(studentNow, courseCode, indexNo);
										System.out.println("Course added !");
										printseg();
										System.out.println("Current Timetable:");
										ttb.printTimetable();
										printseg();
									}

								}
							}
							else {
								System.out.println("This index has no vacancies, you are put into the waitlist");
								index0.addWaitlist(studentNow);
								printseg();
							}
							break;
						case "2":
							boolean rC = false;
							boolean rI = false;
							System.out.println("Which course would you like to drop ?");
							courseCode = sc.nextLine().toUpperCase();
							while (true)  {
								 if (courseManager.courseExists(courseCode)) {
									 break;
								 }
								 else {
									System.out.printf("Course does not exist. Enter course code again: ");
									courseCode = sc.nextLine().toUpperCase();
								 }
							}
							for (Index i: studentNow.getRegisIndexes()) {
								if (i.getCourseCode().compareTo(courseCode) == 0) {
									rC = true;
									break;
								}
							}
							if (rC == false) {
								System.out.println("You are not registered for " + courseCode);
							}
							else {
								indexNo = studentNow.getIndexNum(courseCode);
								courseManager.deregisterStudent(studentNow, courseCode, indexNo);
								System.out.println("You have dropped " + courseCode + " with index " + indexNo);
							}
							printseg();
							break;
						case "3":
							System.out.println("These are your registered courses");
							for (Index i: studentNow.getRegisIndexes()) {
								System.out.println(i.getCourseCode() + " with index " + i.getIndexNumber());
							}printseg();
							ttb = new Timetable();
							for (Index i:studentNow.getRegisIndexes()) {
								ttb.addIndex(i);
							}
							System.out.println("Current Timetable:");
							ttb.printTimetable();
							printseg();
							break;
						case "4":
							System.out.println("Enter course and index number to check vacancy:");
							courseCode = sc.nextLine().toUpperCase();
							while (true)  {
								 if (courseManager.courseExists(courseCode)) {
									 break;
								 }
								 else {
									System.out.printf("Course does not exist. Enter course code again: ");
									courseCode = sc.nextLine().toUpperCase();
								 }
							}
							indexNo = sc.nextLine().toUpperCase();
							while (true) {
								if (courseManager.indexExistsInCourse(courseCode, indexNo)) {
									break;
								}
								else {
									System.out.printf("Index does not exist. Enter index again: ");
									indexNo = sc.nextLine().toUpperCase();
								}
							}
							System.out.println("Vacancies available = " + courseManager.checkVacancies(courseCode,indexNo));
							printseg();
							break;
						case "5":
							rC = false;
							System.out.println("Which course would you like to change index ?");
							courseCode = sc.nextLine().toUpperCase();
							while (true)  {
								 if (courseManager.courseExists(courseCode)) {
									 break;
								 }
								 else {
									System.out.printf("Course does not exist. Enter course code again: ");
									courseCode = sc.nextLine().toUpperCase();
								 }
							}
							for (Index i: studentNow.getRegisIndexes()) {
								if (i.getCourseCode().compareTo(courseCode) == 0) {
									rC = true;
									break;
								}
							}
							if (rC == false) {
								System.out.println("You are not registered for " + courseCode);
								printseg();
							}
							else {
								course0 = courseManager.getCourse(courseCode);
								indexNo = studentNow.getIndexNum(courseCode);
								System.out.printf("Your current index is %s\n", indexNo);
								System.out.printf("Indexes for this course: \n");
								for (Index i:course0.getIndexes()) {
									System.out.printf("%s, %d vacancies\n", i.getIndexNumber(), i.getVacancies());
								}
								System.out.printf("New index: ");
								String newIndexNo = sc.nextLine().toUpperCase();
								while (true) {
									if (courseManager.indexExistsInCourse(courseCode, newIndexNo)) {
										if (courseManager.checkVacancies(courseCode, indexNo) > 0) {
											courseManager.deregisterStudent(studentNow, courseCode, indexNo);
											courseManager.registerStudent(studentNow, courseCode, newIndexNo);
											System.out.println("Index has been changed from " + indexNo + " to " + newIndexNo);
											printseg();
										}
										else {
											System.out.println("Sorry, time clash!");
											printseg();
										}
										break;
									}
									else {
										System.out.printf("Index does not exist. Enter index again: ");
										newIndexNo = sc.nextLine().toUpperCase();
									}
								}
							}
							break;
						case "6":
							System.out.println("Which course would you like to swop index");
							courseCode = sc.nextLine();
							course0 = courseManager.getCourse(courseCode);
							while (true) {
								try {
									course0.getCourseCode();
									break;
								}
								catch (Exception e) {
									System.out.printf("Course not found. Enter course code: ");
									courseCode = sc.nextLine().toUpperCase();
									course0 = courseManager.getCourse(courseCode);
								}
							}
							System.out.println("Which student would you like to swop index with? Matric number: ");
							String student6 = sc.nextLine();
							studentI = studentManager.getStudent(student6);
							while(true){
								try{
									studentI.getName();
									break;
								}catch(Exception e){
									System.out.print("Invalid user! \n");
									System.out.println("Which student would you like to swop index with? Matric number: ");
									student6 = sc.nextLine();
									studentI = studentManager.getStudent(student6);
								}
							}

							int value = -1;
							for (Index i6: studentNow.getRegisIndexes()) {
								if (i6.getCourseCode().compareTo(courseCode) == 0) {
									value += 1;
									break;
								}
							}
							for (Index i6: studentI.getRegisIndexes()) {
								if (i6.getCourseCode().compareTo(courseCode) == 0) {
									value += 1;
									break;
								}
							}
							if (value == 1) {
								courseManager.swopIndexes(studentI, studentNow, courseCode);
								System.out.println("Swopping successful! " + studentNow.getName() + " has been allocated index " + studentNow.getIndexNum(courseCode));
								printseg();
							}
							else {
								System.out.println("Swopping invalid ! Please make sure that both students are registered to " + courseCode);
								printseg();
							}
							break;						
						case "7":
							System.out.printf("Waitlisted courses: \n");
							for (Index w:studentNow.getWaitIndexes()) {
								System.out.printf("Course: %s, Index: %s\n", w.getCourseCode(), w.getIndexNumber());
							}
							printseg();
							break;
						case "8":
							quit = true;
							break;
						default:
							break;
					}		
				} while (quit == false);
			}
		}
		else if (domain == "STAFF") {
			System.out.printf("Welcome %s %s!\n", domain, username);
			printseg();
			do {
				String tempLine = "";
				boolean tempCheck = true;
				System.out.println("(1) Edit student access period");
				System.out.println("(2) Add a student");
				System.out.println("(3) Add a course");
				System.out.println("(4) Update a course");
				System.out.println("(5) Check available slot for an index number");
				System.out.println("(6) Print student list by index number");
				System.out.println("(7) Print student list by course");
				System.out.println("(8) Quit the programme");
				System.out.printf("Enter choice: ");
				choice = sc.nextLine();
				printseg();
				int num;
				String courseCode;
				String indexNo;
				switch(choice) {
					case "1":
						System.out.println("Change access period...");
						String programme;
						do {
							System.out.printf("Programme: ");
							programme = sc.nextLine().toUpperCase();
						} while (programme.compareTo("CS") != 0 & programme.compareTo("MACS") != 0 & programme.compareTo("MAS") != 0);
						
						int year = -1;
						do {
							try {
								System.out.printf("Year (1-4): ");
								year = sc.nextInt();
							}
							catch (InputMismatchException e) {
								System.out.printf("Enter an integer from 1 to 4.\n");
								sc.nextLine();
							}
						} while (year < 1 || year > 4);
						int sem = -1;
						do {
							try {
								System.out.printf("Semester (1/2): ");
								sem = sc.nextInt();
							}
							catch (InputMismatchException e) {
								System.out.printf("Enter either 1 or 2.\n");
								sc.nextLine();
							}
						} while (sem != 1 & sem != 2);
						System.out.printf("Current access period: ");
						System.out.println(AccessPeriod.getAccessTime(programme, year, sem));
						System.out.printf("Enter new access day (DD/MM/YYYY): ");
						sc.nextLine();
						String dmy = sc.nextLine();
						System.out.printf("Enter new start of access time (HH:MM:ss): ");
						String beginHHMM = sc.nextLine();
						System.out.printf("Enter new end of access time (HH:MM:ss): ");
						String endHHMM = sc.nextLine();
						AccessPeriod.setAccessTime(programme, year, sem, dmy, beginHHMM, endHHMM);
						System.out.printf("Updated access period: ");
						System.out.println(AccessPeriod.getAccessTime(programme, year, sem));
						printseg();
						break;		
					
					case "2":
						StudentUser student = new StudentUser();
						System.out.println("Creating new student...\nEnter student details:");
						System.out.printf("Name: ");
						student.setName(sc.nextLine());
						System.out.printf("Matriculation number: ");
						student.setMatric(sc.nextLine().toUpperCase());

						tempCheck = !studentManager.studentExists(student.getMatric());
						if (studentManager.studentExists(student.getMatric()) == true) {
							tempCheck = false;
							System.out.println("Cannot add, this student was added!");
							printseg();
						}
						if(tempCheck){
							System.out.printf("Network ID: ");
							student.setNID(sc.nextLine());
							student.setEmail(student.getNID()+"e.ntu.edu.sg");
							do {
								System.out.printf("Gender(F/M): ");
								student.setGend(sc.nextLine().toUpperCase());
							} while (student.getGend().compareTo("F") != 0 & student.getGend().compareTo("M") != 0);
							System.out.printf("Nationality: ");
							student.setNat(sc.nextLine());
							System.out.printf("School: ");
							student.setSch(sc.nextLine());
							num = -1;
							do {
								try {
									System.out.printf("Year (1-4): ");
									num = sc.nextInt();
								}
								catch (InputMismatchException e) {
									System.out.printf("Enter an integer from 1 to 4.\n");
									sc.nextLine();
								}
							} while (num < 1 || num > 4);
							student.setYear(num);
							num = -1;
							System.out.printf("Semester (1/2): ");
							do {
								try {
									System.out.printf("Semester (1/2): ");
									num = sc.nextInt();
								}
								catch (InputMismatchException e) {
									System.out.printf("Enter either 1 or 2.\n");
									sc.nextLine();
								}
							} while (num != 1 & num != 2);
							student.setSem(num);
							sc.nextLine();
							System.out.printf("Programme: ");
							student.setProg(sc.nextLine());
							// REGISTERED AND WAITLISTED COURSES
							System.out.printf("Enter registered courses...\n");
							while (student.getAU() <= 21) {
								System.out.printf("Course code (N if no more): ");
								courseCode = sc.nextLine().toUpperCase();
								if (courseCode.compareToIgnoreCase("N") == 0) {
									break;
								}
								System.out.printf("Index: ");
								indexNo = sc.nextLine();
								try {
									courseManager.registerStudent(student, courseCode, indexNo);
								}
								catch (Exception e) {
									System.out.printf("Error! Could not register.\n");
									printseg();
								}
								if (student.getAU() > 21) {
									courseManager.deregisterStudent(student, courseCode, indexNo);
									System.out.printf("Error! Max AU exceeded. Course not added.\n");
									printseg();
								}
							}
							System.out.printf("Enter waitlisted courses...\n");
							while (true) {
								System.out.printf("Course code (N if no more): ");
								courseCode = sc.nextLine();
								if (courseCode.compareToIgnoreCase("N") == 0) {
									break;
								}
								System.out.printf("Index: ");
								indexNo = sc.nextLine();
								try {
									courseManager.waitlistStudent(student, courseCode, indexNo);
								}
								catch (Exception e) {
									System.out.printf("Error! Could not waitlist.\n");
									printseg();
								}
							}
							studentManager.addStudent(student);
							System.out.println("Student added!\nUpdated list of students: ");
							studentManager.printStudentList();
							printseg();
						}
						//if new1.getNID() == 
						//login_out.addUser("John", "Student", "234869");
						break;
					case "3":
						CourseInformation course = new CourseInformation();
						System.out.println("Creating new course...\nEnter course details:");
						System.out.printf("Course name: ");
						course.setCourseName(sc.nextLine());
						System.out.printf("Course code: ");
						course.setCourseCode(sc.nextLine().toUpperCase());
						
						tempLine = "";
						tempCheck = true;
						
						if (courseManager.courseExists(course.getCourseCode())) {
							tempCheck = false;
							System.out.println("Cannot add, this course was added!");
							printseg();
						}

						if(tempCheck){
							System.out.printf("School: ");
							course.setSchool(sc.nextLine().toUpperCase());
							System.out.printf("Exam schedule (DD/MM/YY HHMM-HHMM): ");
							course.setExam(sc.nextLine());
							num = -1;
							while (num < 0 || num > 10) {
								try {
									System.out.printf("Number of AUs (0-10): ");
									num = sc.nextInt();
								}
								catch (InputMismatchException e) {
									System.out.printf("Enter an integer between 0 and 10.\n");
									sc.nextLine();
								}
							}
							course.setAUs(num);
							String available;
							sc.nextLine();
							for (Programmes s:Programmes.values()) {
								do {
									System.out.printf("Available for %s? (Y/N): ", s.toString());
									available = sc.nextLine();
									if (available.compareToIgnoreCase("Y") == 0) {
										course.setAvailableFor(s.toString());
									}
								} while (available.compareToIgnoreCase("Y") != 0 & available.compareToIgnoreCase("N") != 0);
							}
							while (true) {
								System.out.printf("Creating new index... Enter N to cancel.\n");
								System.out.printf("Index number: ");
								indexNo = sc.nextLine();
								if (indexNo.compareToIgnoreCase("N") == 0) {
									break;
								}
								Index index = new Index();
								course.addIndex(index);
								index.setCourseCode(course.getCourseCode());
								index.setIndexNumber(indexNo);
								index.setNumAUs(course.getNumAUs());
								num = -1;
								do {
									try {
										System.out.printf("Max number of students: ");
										num = sc.nextInt();
									}
									catch (InputMismatchException e) {
										System.out.printf("Enter a positive integer.\n");
										sc.nextLine();
									}
								} while (num <= 0);
								index.setMaxNumberOfStudents(num);
								index.setVacancies(index.getMaxNumberOfStudents());
								sc.nextLine();
								String matricNum;
								int i = 0;
								StudentUser student1;
								while (i < index.getMaxNumberOfStudents()) {
									System.out.printf("Matric number of registered students (N if no more): ");
									matricNum = sc.nextLine();
									if (matricNum.compareToIgnoreCase("N") == 0) {
										break;
									}
									try {
										student1 = studentManager.getStudent(matricNum);
										index.addStudent(student1);
										i++;
									}
									catch (Exception e) {
										System.out.printf("Error! Student does not exist.\n");
										printseg();
									}
								}
								if (i == index.getMaxNumberOfStudents()) {
									while (true) {
										System.out.printf("Matric number of waitlisted students (N if no more): ");
										matricNum = sc.nextLine();
										if (matricNum.compareToIgnoreCase("N") == 0) {
											break;
										}
										try {
											student1 = studentManager.getStudent(matricNum);
											index.addWaitlist(student1);
											i++;
										}
										catch (Exception e) {
											System.out.printf("Error! Student does not exist.\n");
											printseg();
										}
									}
								}	
								String type = null;
								while (true) {
									System.out.printf("Creating new lesson for this index... Enter N to cancel.\n");
									do {
										System.out.printf("Lesson type (LEC-Lecture, TUT-Tutorial, LAB-Lab): ");
										type = sc.nextLine();
									} while (type.compareToIgnoreCase("N") != 0 & type.compareToIgnoreCase("LEC") != 0 & type.compareToIgnoreCase("TUT") != 0 & type.compareToIgnoreCase("LAB") != 0);
									if (type.compareToIgnoreCase("N") == 0) {
										break;
									}
									Lesson lesson = new Lesson();
									lesson.setCourseCode(course.getCourseCode());
									lesson.setIndexNo(index.getIndexNumber());
									lesson.setType(type.toUpperCase());
									num = -1;
									while (num < 0 || num > 6) {
										try {
											System.out.printf("Day (0-Monday, 1-Tuesday, 2-Wednesday, 3-Thursday, 4-Friday, 5-Saturday, 6-Sunday): ");
											num = sc.nextInt();
										}
										catch (InputMismatchException e) {
											System.out.printf("Enter an integer between 0 and 6.\n");
											sc.nextLine();
										}
									}
									lesson.setDay(num);
									sc.nextLine();
									System.out.printf("Venue: ");
									lesson.setVenue(sc.nextLine());
									System.out.printf("Start time (HHMM, 24 hour time): ");
									lesson.setStartTime(sc.nextLine());
									System.out.printf("End time (HHMM, 24 hour time): ");
									lesson.setEndTime(sc.nextLine());
									index.addLesson(lesson);
								}							
							}
							courseManager.addCourse(course);
							System.out.println("Course added!");
							courseManager.printCourseList();
							printseg();
						}
						break;
					case "4":
						System.out.printf("Edit a course...\n");
						System.out.printf("Enter course code: ");
						courseCode = sc.nextLine().toUpperCase();
						while (true)  {
							 if (courseManager.courseExists(courseCode)) {
								 break;
							 }
							 else {
								System.out.printf("Course does not exist! Enter course code again: ");
								courseCode = sc.nextLine().toUpperCase();
							 }
						}
						course0 = courseManager.getCourse(courseCode);
						printseg();
						String option = "";
						System.out.printf("(1) Edit course name\n"
								+ "(2) Edit school\n"
								+ "(3) Edit exam schedule\n"
								+ "(4) Edit number of AUs\n"
								+ "(5) Edit programmes available\n"
								+ "(6) Add index\n"
								+ "(7) Edit index\n"
								+ "(8) Exit\n");
						option = sc.nextLine();
						printseg();
						switch (option) {
						case "1":
							System.out.printf("Course name is %s.\nNew course name: ", course0.getCoursename());
							course0.setCourseName(sc.nextLine());
							printseg();
							break;
						case "2":
							System.out.printf("School is %s.\nNew school: ", course0.getSchool());
							course0.setSchool(sc.nextLine());
							printseg();
							break;
						case "3":
							System.out.printf("Exam schedule is %s.\nNew exam schedule (DD/MM/YY HHMM-HHMM): ", course0.getExamSchedule());
							course0.setExam(sc.nextLine());
							printseg();
							break;
						case "4":
							System.out.printf("Number of AUs is %d.\n", course0.getNumAUs());
							num = -1;
							while (num < 0 || num > 10) {
								try {
									System.out.printf("New number of AUs (0-10): ");
									num = sc.nextInt();
								}
								catch (InputMismatchException e) {
									System.out.printf("Enter an integer between 0 and 10.\n");
									sc.nextLine();
								}
							}
							course0.setAUs(num);
							printseg();
							break;
						case "5":
							String available = "";
							for (Programmes s:Programmes.values()) {
								do {
									System.out.printf("Available for %s? (Y/N): ", s.toString());
									available = sc.nextLine();
									if (available.compareToIgnoreCase("Y") == 0) {
										course0.setAvailableFor(s.toString());
									}
									if (available.compareToIgnoreCase("N") == 0) {
										course0.removeAvailableFor(s.toString());
									}
								} while (available.compareToIgnoreCase("Y") != 0 & available.compareToIgnoreCase("N") != 0);
							}
							printseg();
							break;
						case "6":
							System.out.printf("Creating new index...\n");
							System.out.printf("Index number: ");
							indexNo = sc.nextLine();
							Index index = new Index();
							course0.addIndex(index);
							index.setCourseCode(course0.getCourseCode());
							index.setIndexNumber(indexNo);
							index.setNumAUs(course0.getNumAUs());
							num = -1;
							do {
								try {
									System.out.printf("Max number of students: ");
									num = sc.nextInt();
								}
								catch (InputMismatchException e) {
									System.out.printf("Enter a positive integer.\n");
									sc.nextLine();
								}
							} while (num <= 0);
							index.setMaxNumberOfStudents(num);
							index.setVacancies(index.getMaxNumberOfStudents());
							sc.nextLine();
							String matricNum;
							int i = 0;
							StudentUser student1;
							while (i < index.getMaxNumberOfStudents()) {
								System.out.printf("Matric number of registered students (N if no more): ");
								matricNum = sc.nextLine();
								if (matricNum.compareToIgnoreCase("N") == 0) {
									break;
								}
								try {
									student1 = studentManager.getStudent(matricNum);
									index.addStudent(student1);
									i++;
								}
								catch (Exception e) {
									System.out.printf("Error! Student does not exist.\n");
									printseg();
								}
							}
							if (i == index.getMaxNumberOfStudents()) {
								while (true) {
									System.out.printf("Matric number of waitlisted students (N if no more): ");
									matricNum = sc.nextLine();
									if (matricNum.compareToIgnoreCase("N") == 0) {
										break;
									}
									try {
										student1 = studentManager.getStudent(matricNum);
										index.addWaitlist(student1);
										i++;
									}
									catch (Exception e) {
										System.out.printf("Error! Student does not exist.\n");
										printseg();
									}
								}
							}	
							String type = null;
							while (true) {
								System.out.printf("Creating new lesson for this index... Enter N to cancel.\n");
								do {
									System.out.printf("Lesson type (LEC-Lecture, TUT-Tutorial, LAB-Lab): ");
									type = sc.nextLine();
								} while (type.compareToIgnoreCase("N") != 0 & type.compareToIgnoreCase("LEC") != 0 & type.compareToIgnoreCase("TUT") != 0 & type.compareToIgnoreCase("LAB") != 0);
								if (type.compareToIgnoreCase("N") == 0) {
									break;
								}
								Lesson lesson = new Lesson();
								lesson.setCourseCode(course0.getCourseCode());
								lesson.setIndexNo(index.getIndexNumber());
								lesson.setType(type.toUpperCase());
								num = -1;
								while (num < 0 || num > 6) {
									try {
										System.out.printf("Day (0-Monday, 1-Tuesday, 2-Wednesday, 3-Thursday, 4-Friday, 5-Saturday, 6-Sunday): ");
										num = sc.nextInt();
									}
									catch (InputMismatchException e) {
										System.out.printf("Enter an integer between 0 and 6.\n");
										sc.nextLine();
									}
								}
								lesson.setDay(num);
								sc.nextLine();
								System.out.printf("Venue: ");
								lesson.setVenue(sc.nextLine());
								System.out.printf("Start time (HHMM, 24 hour time): ");
								lesson.setStartTime(sc.nextLine());
								System.out.printf("End time (HHMM, 24 hour time): ");
								lesson.setEndTime(sc.nextLine());
								index.addLesson(lesson);
							}			
							course0.addIndex(index);
							printseg();
							break;
						case "7":
							System.out.printf("Edit an index...\n");
							System.out.printf("Enter index number: ");
							indexNo = sc.nextLine().toUpperCase();
							while (true) {
								if (courseManager.indexExistsInCourse(courseCode, indexNo)) {
									break;
								}
								else {
									System.out.printf("Enter index again: ");
									indexNo = sc.nextLine().toUpperCase();
								}
							}
							index0 = courseManager.getIndex(courseCode, indexNo);
							option = "";
							System.out.printf("(1) Edit max number of students\n"
									+ "(2) Exit");
							option = sc.nextLine();
							switch (option) {
							case "1": 
								int regis = index0.getNumberRegistered();
								System.out.printf("Current max number of students is %d, current number of students registered is %d.\n", index0.getMaxNumberOfStudents(), regis);
								num = -1;
								while (num < regis) {
									try {
										System.out.printf("New max number of students (greater than %d): ", regis);
										num = sc.nextInt();
									}
									catch (InputMismatchException e) {
										System.out.printf("Enter an integer greater than %d.\n", regis);
										sc.nextLine();
									}
								}
								index0.setMaxNumberOfStudents(num);
								printseg();
								break;
							default:
								break;
							}
						default:
							break;
						}
						break;
					case "5":
						System.out.println("Enter course code and index to check vacancies...");
						System.out.printf("Enter course code: ");
						courseCode = sc.nextLine().toUpperCase();
						while (true) {
							if (courseManager.courseExists(courseCode)) {
								break;
							}
							else {
								System.out.printf("Course does not exist. Enter course code again: ");
								courseCode = sc.nextLine().toUpperCase();
							}
						}
						System.out.printf("Enter index: ");
						indexNo = sc.nextLine().toUpperCase();
						while (true) {
							if (courseManager.indexExistsInCourse(courseCode, indexNo)) {
								break;
							}
							else {
								System.out.printf("Index does not exist. Enter index again: ");
								indexNo = sc.nextLine().toUpperCase();
							}
						}
						System.out.printf("Vacancies: %d\n", courseManager.checkVacancies(courseCode, indexNo));
						break;
					case "6":
						System.out.println("Enter course code and index to print list of students...");
						System.out.printf("Enter course code: ");
						courseCode = sc.nextLine().toUpperCase();
						while (true) {
							if (courseManager.courseExists(courseCode)) {
								break;
							}
							else {
								System.out.printf("Course does not exist. Enter course code again: ");
								courseCode = sc.nextLine().toUpperCase();
							}
						}
						System.out.printf("Enter index: ");
						indexNo = sc.nextLine().toUpperCase();
						while (true) {
							if (courseManager.indexExistsInCourse(courseCode, indexNo)) {
								break;
							}
							else {
								System.out.printf("Index does not exist. Enter index again: ");
								indexNo = sc.nextLine().toUpperCase();
							}
						}
						courseManager.printStudentsInIndex(courseCode, indexNo);	
						break;
					case "7":
						System.out.println("Enter course code to print list of students...");
						System.out.printf("Enter course code: ");
						courseCode = sc.nextLine().toUpperCase();
						while (true)  {
							 if (courseManager.courseExists(courseCode)) {
								 break;
							 }
							 else {
								System.out.printf("Course does not exist. Enter course code again: ");
								courseCode = sc.nextLine().toUpperCase();
							 }
						}
						courseManager.printStudentsInCourse(courseCode);
						printseg();
						break;
					case "8":
						quit = true;
						break;
					default:
						break;
				}
				
			} while (quit == false);
		}
		System.out.printf("Quitting...");
		studentDB.write(studentManager);
		lessonDB.write(courseManager);
		indexDB.write(courseManager);
		courseDB.write(courseManager);
		accessDB.write();
		sc.close();
  }	
}