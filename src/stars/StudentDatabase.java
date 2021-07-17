// package stars;

import java.io.*;

/**
 * Database containing all student information.
 * @author Kim
 *
 */
public class StudentDatabase extends GeneralDatabase{
	/**
	 * Path of file containing the student database.
	 */
	private String fileName;
	/**
	 * Creates a database object associated to a particular file name.
	 * @param fileName
	 */
	public StudentDatabase(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Reads database to create StudentUser objects to populate the StudentManager students.
	 * @param students
	 */
	public void read(StudentManager students) {
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				// create studentUser object corresponding to each line in database
				String[] tokens = line.split(",");
				
				StudentUser student = new StudentUser(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5],
						tokens[6], Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8]), tokens[9],
						Integer.parseInt(tokens[10]));
				students.addStudent(student);
			}
			br.close();
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * Writes and stores all info of all StudentUsers in the StudentManager into a database.
	 * @param students
	 */
	public void write(StudentManager students) {
		try {
			PrintStream writer = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(writer);
			System.out.printf("name,matricNum,email,networkID,gender,nationality,school,year,sem,programme,AUs,numRegistered,numWaitlisted,registeredCourses,waitlistedCourses\n");
			for (StudentUser s:students.getStudentRecords()) {
				System.out.printf("%s,%s,%s,%s,%s,%s,%s,%d,%d,%s,%d,%d,%d", s.getName(), s.getMatric(), s.getEmail(), s.getNID(),
						s.getGend(), s.getNat(), s.getSch(), s.getYear(), s.getSem(), s.getProg(),
						s.getAU(), s.getRegis(), s.getWait());
				for (Index i:s.getRegisIndexes()) {
					System.out.printf(",%s_%s", i.getCourseCode(), i.getIndexNumber());
				}
				for (Index i:s.getWaitIndexes()) {
					System.out.printf(",%s_%s", i.getCourseCode(), i.getIndexNumber());
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
		System.out.println("Student database initialized!");
	}
}