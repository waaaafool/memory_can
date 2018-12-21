package can.sms.smsMatch;

public class TimeHandle {
	private static int[] monthLength = {0, 31, 28, 31, 30, 31, 30
			, 31, 31, 30, 31, 30, 31};
	private static int yearNum;
	private static int monthNum;
	private static int dayNum;
	
	private TimeHandle() {}

	private static void handleLeapYear() {
		boolean isLeapYear = false;
		if (yearNum % 4 == 0 && yearNum % 100 != 0) {
			isLeapYear = true;
		} else if (yearNum % 400 == 0) {
			isLeapYear = true;
		}
		if (isLeapYear == true) {
			monthLength[2] = 29;
		} else {
			monthLength[2] = 28;
		}
	}
	
	private static void handleDay() {
		dayNum += 3;
		
		if (dayNum > monthLength[monthNum]) {
			dayNum -= monthLength[monthNum];
			monthNum++;
			if (monthNum > 12) {
				monthNum %= 12;
				yearNum++;
			}
		}
	}
	
	private static void transTime(String time) {
		yearNum = Integer.parseInt(time.substring(0, 4));
		monthNum = Integer.parseInt(time.substring(5, 7));
		dayNum = Integer.parseInt(time.substring(8, 10));
//		System.out.println(yearNum);
//		System.out.println(monthNum);
//		System.out.println(dayNum);
	}
	
	public static String handleTime(String time) {
		transTime(time);
		handleLeapYear();
		handleDay();
		String year = String.valueOf(yearNum);
		String month = String.valueOf(monthNum);
		String day = String.valueOf(dayNum);
		if (day.length() < 2) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day + " 21:00:00";
	}
}
