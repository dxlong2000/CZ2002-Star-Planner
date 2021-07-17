// package stars;

import java.util.List;
import java.util.ArrayList;

/**
 * Lesson information along with its timings.
 * @author Group7
 *
 */
public class Lesson {
	/**
	 * startTime is HH:MM
	 */
	private String startTime;
	/**
	 * endTime is HH:MM
	 */
	private String endTime;
	private String venue;
	/**
	 * Lesson type which is LEC, TUT or LAB
	 */
	private String type;
	private String indexNo;
	private String courseCode;
	/**
	 * day of the week, written 0-Monday, 1-Tuesday, ...
	 */
	private int day;
	private List<Integer> timeslot;
	
	public Lesson() {}
	
	public Lesson(int day, String st, String et, String venue, String index, String courseCode, String type){
		this.day = day;
		this.startTime =st;
		this.endTime = et;
		this.venue = venue;
		this.indexNo = index;
		this.courseCode = courseCode;
		this.type = type;
		this.timeslot = new ArrayList<Integer>();
		this.calTimeslot();
	}
	//get methods
	public String getStartTime() {
		return this.startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}
	
	public String getVenue() {
		return this.venue;
	}
	
	public String getType() {
		return this.type;
	}

	public String getIndexNo() {
		return this.indexNo;
	}

	public int getDay() {
		return this.day;
	}
	
	public List<Integer> getTimeslot() {
		return this.timeslot;
	}
	
	public String getCourseCode() {
		return this.courseCode;
	}
	
	//set methods
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setCourseCode(String code) {
		this.courseCode = code;
	}
	
	/** 
	 * Gets hour given a HH:MM format.
	 * @param time
	 * @return
	 */
	public int getHour(String time) {
	    String [] timeParts = time.split(":");
	    int hour = Integer.parseInt(timeParts[0].trim());
	    return hour;
	}

	/**
	 * Creates a time slot for the purpose of timetable
	 */
	public void calTimeslot() {
		int starthour = getHour(startTime);
		int endhour = getHour(endTime);
		while(starthour<endhour) {
			this.timeslot.add(starthour-8);
			starthour++;
		}
	}
}