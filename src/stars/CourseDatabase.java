// package stars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Imports database of course information into the programme.
 * @author Group7
 */
public class CourseDatabase extends GeneralDatabase {
	/**
	 * File name for import.
	 */
	private String fileName;
	public CourseDatabase(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Read info from database and create CourseInformation objects to populate the CourseManager object.
	 * @param courses CourseManager of our programme.
	 */
	public void read(CourseManager courses) throws IOException{
		String row = null;
		BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
		csvReader.readLine();
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
	    	CourseInformation course = new CourseInformation(data[0],data[1], data[2], data[3], Integer.parseInt(data[4]));
	    	try {
		    	String[] available = data[5].split("_");
		    	for (String s:available) {
		    		course.setAvailableFor(s);
		    	}	    		
	    	}
	    	catch (Exception e) {}
	    	courses.addCourse(course);
	    }
		csvReader.close();
	}
	
	/**
	 * Writes course informations from our CourseManager into a .csv database.
	 * @param courses CourseManager of our programme.
	 */
	public void write(CourseManager courses) {
		try {
			PrintStream writer = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(writer);
            
            System.out.printf("courseName,courseCode,school,examSchedule,numAU,availableFor\n");
			for (CourseInformation c:courses.getCourseRecords()) {
				System.out.printf("%s,%s,%s,%s,%d,", c.getCoursename(), c.getCourseCode(), c.getSchool(), c.getExamSchedule(), c.getNumAUs());
				for (String s:c.getProgrammeAvailable()) {
					System.out.printf("%s_", s);
				}
				System.out.printf("\n");
			}
			System.setOut(console);
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		System.out.println("Course database initialized!");
	}
}
