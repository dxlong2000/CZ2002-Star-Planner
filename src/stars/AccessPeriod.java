// package stars;

/**
 * Static class containing all access period dates and timings for each programme.
 * Has arrays containing the access date, beginning time, and ending time of access for each programme.
 * Arrays are stored like: [y1s1,y1s2,y2s1,...,y4s2] for a particular course which is named.
 * @author Group7
 */
public class AccessPeriod {
	//array containing access day for y1s1,y1s2,y2s1,...
	private static String [] dayMAS=new String[] {"12/11/2020","12/11/2020","14/11/2020","14/11/2020","14/11/2020","14/11/2020","14/11/2020","14/11/2020"};//for MAS
	private static String [] dayCS=new String[] {"12/11/2020","12/11/2020","14/11/2020","25/11/2020","14/11/2020","14/11/2020","14/11/2020","14/11/2020"};//for CS
	private static String [] dayMACS=new String[] {"12/11/2020","12/11/2020","14/11/2020","25/11/2020","14/11/2020","14/11/2020","14/11/2020","14/11/2020"};//for MACS
	//for beginning time of day, from 00:00-23:59
	private static String [] beginMAS=new String[] {"09:30:00","9:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00"}; 
	private static String [] beginCS=new String[] {"09:30:00","9:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00"}; 
	private static String [] beginMACS=new String[] {"09:30:00","9:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00","09:30:00"}; 
	//for beginning time of day, from 00:00-23:59
	private static String [] endMAS=new String[] {"15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00"}; 
	private static String [] endCS=new String[] {"15:30:00","15:30:00","15:30:00","21:30:00","15:30:00","15:30:00","15:30:00","15:30:00"}; 
	private static String [] endMACS=new String[] {"15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00","15:30:00"};
	
	public AccessPeriod() {	}

	/**
	 * Returning appropriate array position.
	 * @param year
	 * @param sem
	 * @return p Position in array.
	 */
	private static int arrayPos(int year, int sem) {
		int p;
		switch(year){
		case 1:
			if (sem==1) p= 0;
			else p= 1;
			break;
		case 2:
			if (sem==1) p=2;
			else p=3;
			break;
		case 3:
			if (sem==1) p=4;
			else p=5;
			break;
		case 4:
			if (sem==1) p=6;
			else p=7;
			break;
		default:
			p = 8;
		}
		return p;
	}

	/**
	 * Returns access date.
	 * @param programme
	 * @param year
	 * @param sem
	 */
	public static String accessDay(String programme,int year,int sem) {
		int p;
		p = arrayPos(year,sem);
		switch(programme) {
		case "CS":
			return dayCS[p];
		case "MACS":
			return dayMACS[p];
		case "MAS":
			return dayMAS[p];
		}
		return "";
	}

	/**
	 * Returns beginning of access time.
	 * @param programme
	 * @param year
	 * @param sem
	 */
	public static String accessTimeBegin(String programme,int year,int sem) {
		int p;
		p = arrayPos(year,sem);
		switch(programme) {
		case "CS":
			return beginCS[p];
		case "MACS":
			return beginMACS[p];
		case "MAS":
			return beginMAS[p];
		}
		return "";
	}

	/**
	 * Returns ending of access time.
	 * @param programme
	 * @param year
	 * @param sem
	 */
	public static String accessTimeEnd(String programme,int year,int sem) {
		int p;
		p = arrayPos(year,sem);
		switch(programme) {
		case "CS":
			return endCS[p];
		case "MACS":
			return endMACS[p];
		case "MAS":
			return endMAS[p];
		}
		return "";
	}
	
	/**
	 * Returns all of access date and time.
	 * @param programme
	 * @param year
	 * @param sem
	 */
	public static String getAccessTime(String programme,int year,int sem) {
		String beginTime, endTime, day, accper = "";
		int p;
		p = arrayPos(year,sem);
		switch(programme) {
		case "CS":
			day = dayCS[p];
			beginTime = beginCS[p];
			endTime = endCS[p];
			accper = day + " " + beginTime + "-" + endTime;
			break;
		case "MACS":
			day = dayMACS[p];
			beginTime = beginMACS[p];
			endTime = endMACS[p];
			accper = day + " " + beginTime + "-" + endTime;
			break;
		case "MAS":
			day = dayMAS[p];
			beginTime = beginMAS[p];
			endTime = endMAS[p];
			accper = day + " " + beginTime + "-" + endTime;
			break;
		}
		return accper;
	}
	
	/**
	 * Edit access date and time for a programme, year, sem.
	 * @param programme
	 * @param year
	 * @param sem
	 * @param dmy
	 * @param beginHHMM
	 * @param endHHMM
	 */
	public static String setAccessTime(String programme,int year,int sem,String dmy,String beginHHMM,String endHHMM) {
		String accper = "";
		int p;
		p = arrayPos(year,sem);
		switch(programme) {
		case "CS":
			dayCS[p] = dmy;
			beginCS[p] = beginHHMM;
			endCS[p] = endHHMM;
			accper = dayCS[p] + " " + beginCS[p] + "-" + endCS[p];
			break;
		case "MACS":
			dayMACS[p] = dmy;
			beginMACS[p] = beginHHMM;
			endMACS[p] = endHHMM;
			accper = dayMACS[p] + " " + beginMACS[p] + "-" + endMACS[p];
			break;
		case "MAS":
			dayMAS[p] = dmy;
			beginMAS[p] = beginHHMM;
			endMAS[p] = endHHMM;
			accper = dayMAS[p] + " " + beginMAS[p] + "-" + endMAS[p];
			break;
		}
		return accper;
	}
}