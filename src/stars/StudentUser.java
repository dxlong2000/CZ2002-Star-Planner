// package stars;

import java.util.*;

public class StudentUser {
	private String name=""; //name of student
	private String matricNum=""; //matriculation number of student
	/**
	 * Taken to be networkID@e.ntu.edu.sg for new added students
	 * This reflects NTU's email naming system.
	 */
	private String email="";
	private String networkID=""; //network ID of student (the portion in front of NTU email to log into NTU network)
	/**
	 * F/M
	 */
	private String gender="";
	private String nationality="";
	private String school=""; //e.g SCSE or SPMS
	/** 
	 * Current study year
	 */
	private int year=0; //current study year
	/**
	 * semester to be taken for the voting eg.voting after semester 1 break for coming sem means student is sem2 of the same year
	 */
	private int sem=0; //semester to be taken for the voting eg.voting after semester 1 break for coming sem means student is sem2 of the same year
	/**
	 * CS, MACS, or MAS
	 */
	private String programme=""; //computer science(CS), mathematics(MAS), comsci and math(MACS) etc.
	private ArrayList<Index> regisIndexes = new ArrayList<Index>();
	private ArrayList<Index> waitIndexes = new ArrayList<Index>();
	private int totalAU=0; //Total AU student has thus far
	/**
	 * Number of registered courses.
	 */
	private int regis=0; //Total registered courses student has thus far
	/**
	 * Number of waitlisted courses.
	 */
	private int wait=0; //Total waitlisted courses student has thus far
	
	/**
	 * Returns beginning date and time of access period
	 * @return beginning date and time of access period
	 */
	public String accessBegin() {
		return AccessPeriod.accessDay(programme, year, sem) + " " + AccessPeriod.accessTimeBegin(this.programme,this.year,this.sem);
	}
	/**
	 * Returns ending date and time of access period
	 * @return ending date and time of access period
	 */
	public String accessEnd() {
		return AccessPeriod.accessDay(programme, year, sem) + " " + AccessPeriod.accessTimeEnd(this.programme,this.year,this.sem);
	}
	
	public StudentUser() {}

	public StudentUser(String name, String matricNum, String email, String networkID,String gender,String nationality,String school,int year,int sem,String programme,int totalAU) {
		this.name = name;
		this.matricNum = matricNum;
		this.email = email;
		this.networkID = networkID;
		this.gender = gender;
		this.nationality = nationality;
		this.school = school;
		this.year = year;
		this.sem = sem;
		this.programme = programme;
		this.totalAU = totalAU;
	}
	
	//get methods
	public String getName()
	{
		return this.name;
	}
	public String getMatric()
	{
		return this.matricNum;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNID()
	{
		return this.networkID;
	}
	public String getGend()
	{
		return this.gender;
	}
	public String getNat()
	{
		return this.nationality;
	}
	public String getSch()
	{
		return this.school;
	}
	public int getYear()
	{
		return this.year;
	}
	public int getSem()
	{
		return this.sem;
	}
	public String getProg()
	{
		return this.programme;
	}
	public int getAU()
	{
		return this.totalAU;
	}
	public int getRegis() //return NUMBER of registered courses
	{
		return this.regis;
	}
	public int getWait() //return NUMBER of waitlisted courses
	{
		return this.wait;
	}
	/**
	 * Gets the index number for a course that the student is registered to.
	 * @param courseCode
	 * @return registered index number
	 */
	public String getIndexNum(String courseCode) {
		for (Index i:regisIndexes) {
			if (i.getCourseCode().compareTo(courseCode) == 0) {
				return i.getIndexNumber();
			}
		}
		System.out.printf("Course not found.\n");
		return null;
	}
	//set methods
	public void setName(String Name)
	{
		this.name=Name;
	}
	public void setMatric(String Mat)
	{
		this.matricNum=Mat;
	}
	public void setNID(String NID)
	{
		this.networkID=NID;
	}
		public void setGend(String Gen)
	{
		this.gender=Gen;
	}
	public void setNat(String Nat)
	{
		this.nationality=Nat;
	}
	public void setSch(String sch)
	{
		this.school=sch;
	}
	public void setYear(int yr)
	{
		this.year=yr;
	}
	public void setSem(int Sem)
	{
		this.sem=Sem;
	}
	public void setProg(String Prog)
	{
		this.programme=Prog;
	}
	public void setAU(int tAU)
	{
		this.totalAU=tAU;
	}
	public void setRegis(int sR) //set NUMBER of registered courses
	{
		this.regis=sR;
	}
	public void setWait(int sW) //set NUMBER of waitlisted courses
	{
		this.wait=sW;
	}
	/**
	 * Obtains access period for course registration.
	 */
	public String access() {
		return AccessPeriod.getAccessTime(this.programme,this.year,this.sem);
	}
	public void addRegisIndex(Index index) {
		regisIndexes.add(index);
		regis++;
	}
	public void addWaitIndex(Index index) {
		waitIndexes.add(index);
		wait++;
	}
	public void removeRegisIndex(Index index) {
		regisIndexes.remove(index);
		regis--;
	}
	public void removeWaitIndex(Index index) {
		waitIndexes.remove(index);
		wait--;
	}
	public ArrayList<Index> getRegisIndexes() {
		return regisIndexes;
	}
	public ArrayList<Index> getWaitIndexes() {
		return waitIndexes;
	}
}