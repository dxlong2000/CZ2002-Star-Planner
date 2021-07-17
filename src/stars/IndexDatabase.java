// package stars;

import java.io.*;

/**
 * Imports database of index information into courses in the programme.
 * Creates the index object and fits it into the relevant course.
 * @author Group7
 */
public class IndexDatabase extends GeneralDatabase {
	private String fileName;
	public IndexDatabase(String fileName) {
		this.fileName = fileName;
	}	

	/**
	 * Read info from database and create CourseInformation objects to populate the CourseManager object.
	 * @param courses our CourseManager
	 * @param students our StudentManager
	 * @throws IOException
	 */
	public void read(CourseManager courses, StudentManager students) throws IOException{
		String row = null;
		BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
		csvReader.readLine();
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
	    	Index index = new Index(data[0], Integer.parseInt(data[1]), 
	    			data[2], Integer.parseInt(data[3]), 
	    			Integer.parseInt(data[4]), Integer.parseInt(data[5]));
	    	int iterate = 6;
	    	int numRegis = index.getNumberRegistered() + 6;
			int numWait = numRegis + index.getNumberWaitlisted();
			index.setVacancies(index.getMaxNumberOfStudents());
	    	index.setNumberRegistered(0);
	    	index.setNumberWaitlisted(0);
			while (iterate < numRegis) {
				index.addStudent(students.getStudent(data[iterate]));
				iterate++;
			}
			while (iterate < numWait) {
				index.addWaitlist(students.getStudent(data[iterate]));
				iterate++;
			}
	    	for (CourseInformation c:courses.getCourseRecords()) {
	    		if (c.getCourseCode().compareTo(index.getCourseCode()) == 0) {
	    			c.addIndex(index);
	    			index.setNumAUs(c.getNumAUs());
	    			break;
	    		}
	    	}
	    }
		csvReader.close();
	}
	
	/**
	 * Stores all index information into a database.
	 * @param courses our CourseManager
	 */
	public void write(CourseManager courses) {
		try {
			PrintStream writer = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(writer);
           
            System.out.printf("courseCode,vacancies,indexNo,maxStudents,numberRegistered,numberWaitlisted,registeredStudents,waitlistedStudents\n");
			for (CourseInformation c:courses.getCourseRecords()) {
				for (Index i:c.getIndexes()) {
					System.out.printf("%s,%d,%s,%d,%d,%d", i.getCourseCode(), i.getVacancies(), i.getIndexNumber(), i.getMaxNumberOfStudents(), i.getNumberRegistered(), i.getNumberWaitlisted());
					for (int j = 0; j < i.getNumberRegistered(); j++) {
						System.out.printf(",%s", i.getRegisteredStudents().get(j).getMatric());
					}
					for (int j = 0; j < i.getNumberWaitlisted(); j++) {
						System.out.printf(",%s", i.getWaitlistedStudents().poll().getMatric());
					}
					System.out.printf("\n");
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
		System.out.println("Index database initialized!");
	}
}