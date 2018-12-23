package can.appUsed;

public class AppInfo {
    private String myPckName;
    private String myAppName;
    private Long myTotalTime;
    private Integer myLaunchTime;

   public void setMyPckName(String pckName){
        myPckName = pckName;
    }
    public void setMyAppName(String appName){
       myAppName = appName;
    }
    public void setMyTotalTime(Long totalTime){
       myTotalTime = totalTime;
    }
    public void setMyLaunchTime(Integer launchTime){
       myLaunchTime = launchTime;
    }
    public String getMyPckName(){
       return myPckName;
    }
    public String getMyAppName(){
       return myAppName;
    }
    public Long getMyTotalTime(){
       return myTotalTime;
    }
    public Integer getMyLaunchTime(){
       return myLaunchTime;
    }
    public AppInfo(String pckName, String appName, Long totalTime, Integer launchTime){
        super();
        myPckName = pckName;
        myAppName = appName;
        myTotalTime = totalTime;
        myLaunchTime = launchTime;
    }
    public AppInfo(){
       super();
    }
}
