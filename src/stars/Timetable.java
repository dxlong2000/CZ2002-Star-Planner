// package stars;

import java.util.*;

/**
 * Generates a timetable of registered courses given a list of indexes.
 * @author Group7
 *
 */
public class Timetable {
	/**
	 * Indexes to be included in the timetable.
	 */
	public List<Index> indexes;
	/**
	 * Lessons to be included, coming from the indexes.
	 */
	private ArrayList<ArrayList<ArrayList<Lesson>>> timetable;
	/**
	 * List of schooling days, limited from Monday to Friday
	 */
	private String[]daylist = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
	
	/**
	 * Creates a new blank timetable.
	 */
	public Timetable() {
		this.indexes = new ArrayList<Index>();
		generateTimetable();
	}

	/**
	 * Adds an index to the timetable.
	 * @param index
	 */
	public void addIndex(Index index) {
		indexes.add(index);
		generateTimetable();
	}

	/**
	 * Removes an index from the timetable.
	 * @param index
	 */
	public void removeIndex(Index index) {
		indexes.remove(index);
		generateTimetable();
	}

	/**
	 * Generates the timetable.
	 */
	public void generateTimetable() {
		this.timetable = new ArrayList<ArrayList<ArrayList<Lesson>>>();
		for(int i=0;i<5;i++) {
			ArrayList<ArrayList<Lesson>> day = new ArrayList<ArrayList<Lesson>>();
			for(int j=0;j<15;j++) {
				ArrayList<Lesson>slot = new ArrayList<Lesson>();
				day.add(slot);
			}
			timetable.add(day);
		}
		for (int i=0; i<indexes.size(); i++) {
			ArrayList<Lesson>lessons = indexes.get(i).getLessons();
			for (int j=0; j<lessons.size(); j++) {
				int day = lessons.get(j).getDay();
				List<Integer> timeslot = lessons.get(j).getTimeslot();
				for (int k = 0; k<timeslot.size();k++) {
					timetable.get(day).get(timeslot.get(k)).add(lessons.get(j));
				}
			}
		}
	}
	
	/**
	 * Checks for clashes.
	 * @return true if clash occurs, false if no clash.
	 */
	public boolean checkClash() {
		boolean clash = false;
		for (int i=0; i<5; i++) {
			for (int j=0; j<15; j++) {
				if (timetable.get(i).get(j).size()>1){
					clash = true;
					System.out.println("clashed course:");
					for (int k = 0; k<timetable.get(i).get(j).size();k++) {
						String courseCode = timetable.get(i).get(j).get(k).getCourseCode();
						String grpindex = timetable.get(i).get(j).get(k).getIndexNo();
						String Venue = timetable.get(i).get(j).get(k).getVenue();
						String time = Integer.toString(j+8)+":30-"+Integer.toString(j+9)+":30";
						System.out.println(time+" "+courseCode +" "+ grpindex +" " + Venue);
					}
				}
			}
		}
		return clash;
	}

	/**
	 * Visualises the timetable.
	 */
	public void printTimetable() {
		for (int i=0; i<5; i++) {
			System.out.println(daylist[i]);
			for (int j=0; j<timetable.get(i).size(); j++) {
				for (int k = 0; k<timetable.get(i).get(j).size();k++) {
					String courseCode = timetable.get(i).get(j).get(k).getCourseCode();
					String grpindex = timetable.get(i).get(j).get(k).getIndexNo();
					String Venue = timetable.get(i).get(j).get(k).getVenue();
					String type = timetable.get(i).get(j).get(k).getType();
					String time = Integer.toString(j+8)+":30-"+Integer.toString(j+9)+":30";
					System.out.println(time+" "+courseCode +" "+type+" "+ grpindex +" " + Venue);
				}

			}
		}
	}
}