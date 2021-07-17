// package stars;

import java.util.*;
import java.io.*;

/**
 * Basic course class containing all essential information for a course. Represents a course.
 * @author Group7
 *
 */
class CourseInformation {
	private String courseName;
	private String courseCode;
	private String sch;
	private String examSchedule;
	private int numAUs;
	private ArrayList<Index> indexes;
	private ArrayList<String> availableFor;
	
	public CourseInformation() {
		indexes = new ArrayList<Index>();
		availableFor = new ArrayList<String>();
	}
	public CourseInformation(String courseName, String courseCode, String sch, String examSchedule, int numAUs) throws IOException {
		this.courseName = courseName;
		this.courseCode = courseCode;
		this.sch = sch;
		this.examSchedule = examSchedule;
		this.numAUs = numAUs;
		indexes = new ArrayList<Index>();
		availableFor = new ArrayList<String>();
	}
	public String getCoursename() {
		return this.courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public void setSchool(String sch) {
		this.sch = sch;
	}
	public void setExam(String examSchedule) {
		this.examSchedule = examSchedule;
	}
	public void setAUs(int numAUs) {
		this.numAUs = numAUs;
		for (Index i:indexes) {
			i.setNumAUs(numAUs);
		}
	}
	public String getCourseCode() {
		return this.courseCode;
	}
	public String getSchool() {
		return this.sch;
	}
	public String getExamSchedule() {
		return this.examSchedule;
	}
	public int getNumAUs() {
		return this.numAUs;
	}
	public ArrayList<Index> getIndexes() {
		return indexes;
	}
	public void addIndex(Index newIndex) {
		indexes.add(newIndex);
	}	
	/**
	 * @return an ArrayList of programmes this course is available to.
	 */
	public ArrayList<String> getProgrammeAvailable (){
		return availableFor;
	}
	/**
	 * Make this course available for a certain programme, progr.
	 * @param progr Programme this course will become available to
	 */
	public void setAvailableFor(String progr) {
		for(String s:this.availableFor) {
			if(s.compareTo(progr)==0) {
				return;
			}
		}
		availableFor.add(progr);
	}	
	/**
	 * Make this course no longer available for a programme, progr.
	 * @param progr Programme this course will no longer be available to.
	 */
	public void removeAvailableFor(String progr) {
		for(String s:this.availableFor) {
			if(s.compareTo(progr)==0) {
				return;
			}
		}
		availableFor.remove(progr);
	}	
}