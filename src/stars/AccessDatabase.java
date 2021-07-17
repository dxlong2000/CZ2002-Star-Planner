// package stars;

import java.io.*;

/**
 * Imports database of access period dates and timings into the programme.
 * @author Group7
 */
public class AccessDatabase extends GeneralDatabase {

	/**
	 * Enum of Programmes available.
	 */
	enum Programmes {
		CS,
		MACS,
		MAS
	}

	/**
	 * File name for import.
	 */
	private String fileName;
	
	/**
	 * Create a database object with a file name.
	 * @param fileName
	 */
	public AccessDatabase(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Read info from database into static AccessPeriod class.
	 */
	public void read() {
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				AccessPeriod.setAccessTime(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3], tokens[4], tokens[5]);
			}
			br.close();
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * Saves info from static AccessPeriod class into fileName database.
	 */
	public void write() {
		try {
			PrintStream writer = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(writer);
			System.out.printf("programme,year,sem,dmy,beginHHMM,endHHMM\n");
			String s;
			for (Programmes p:Programmes.values()) {
				s = p.toString();
				for (int year = 1; year <= 4; year++) {
					for (int sem = 1; sem <= 2; sem++) {
						System.out.printf("%s,%d,%d,%s,%s,%s\n", s, year, sem, AccessPeriod.accessDay(s, year, sem),
						AccessPeriod.accessTimeBegin(s, year, sem), AccessPeriod.accessTimeEnd(s, year, sem));
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
		System.out.println("Access Database initialized!");
	}
}