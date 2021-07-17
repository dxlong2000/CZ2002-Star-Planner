// package stars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Database containing all lesson information.
 * @author Group7
 *
 */
public class LessonDatabase extends GeneralDatabase {
	/**
	 * File name which is the path of the database file.
	 */
	private String fileName;
	/**
	 * Creates a database object with a particular fileName.
	 * @param fileName
	 */
	public LessonDatabase(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Reads data from database to create lesson objects for a particular index and course
	 * Used to populate indexes of the courses within the CourseManager courses
	 * @param courses our CourseManager
	 * @throws IOException
	 */
	public void read(CourseManager courses) throws IOException {
		Lesson outputlesson = null;
		String row = null;
		BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
		csvReader.readLine();
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    outputlesson = new Lesson(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], data[5], data[6]);
		    for (CourseInformation c:courses.getCourseRecords()) {
		    	if (c.getCourseCode().compareTo(data[5]) == 0) {
		    		for (Index i:c.getIndexes()) {
		    			if (i.getIndexNumber().compareTo(data[4]) == 0) {
		    				i.addLesson(outputlesson);
		    			}
		    		}
		    	}
		    }
		}
		csvReader.close();
	}

	/**
	 * Stores lesson information from our programme into a .csv database
	 * Gets the lesson information from the lessons within the indexes within the courses in our CourseManager.
	 * @param courses
	 */
	public void write(CourseManager courses) {
		try {
			PrintStream writer = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(writer);

            System.out.printf("day,startTime,endTime,venue,indexNo,courseCode,type\n");
			for (CourseInformation c:courses.getCourseRecords()) {
				for (Index i:c.getIndexes()) {
					if (i.getLessons() != null) {
						for (Lesson l:i.getLessons()) {
							System.out.printf("%d,%s,%s,%s,%s,%s,%s\n", l.getDay(), l.getStartTime(), l.getEndTime(), l.getVenue(), l.getIndexNo(), l.getCourseCode(), l.getType());
						}
					}
				}
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
		// TODO Auto-generated method stub
		System.out.println("Lesson database initialized!");
	}
}
