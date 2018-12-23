package can.aboutsqlite;

import com.google.gson.Gson;

public class User {
    private int  user_id;
    private String user_password;
    private String user_tel;
    private int  trip_on;
    private int  trip_priority;
    private int  trip_paper;
    private int  parcel_on;
    private int  parcel_priority;
    private int  parcel_paper;
    private int  analysis_on;
    private int  analysis_priority;
    private int  analysis_paper;
    private int  weather_on;
    private int  weather_priority;
    private int  weather_paper;
    private int  widget;


    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    /*trip_on*/
    public void setTrip_on(int trip_on){
        this.trip_on=trip_on;
    }
    public int getTrip_on(){
        return trip_on;
    }
    /*trip_priority*/
    public void setTrip_priority(int trip_priority){
        this.trip_priority=trip_priority;
    }
    public int getTrip_priority(){
        return trip_priority;
    }
    /*trip_paper*/
    public void setTrip_paper(int trip_paper){
        this.trip_paper=trip_paper;
    }
    public int getTrip_paper(){
        return trip_paper;
    }
    /*parcel_on*/
    public void setParcel_on(int parcel_on){
        this.parcel_on=parcel_on;
    }
    public int getParcel_on(){
        return parcel_on;
    }
    /*parcel_priority*/
    public void setParcel_priority(int parcel_priority){
        this.parcel_priority=parcel_priority;
    }
    public int getParcel_priority(){
        return parcel_priority;
    }
    /*parcel_paper*/
    public void setParcel_paper(int parcel_paper) {
        this.parcel_paper = parcel_paper;
    }
    public int getParcel_paper(){
        return parcel_paper;
    }
    /*analysis_on*/
    public void setAnalysis_on(int analysis_on) {
        this.analysis_on = analysis_on;
    }
    public int getAnalysis_on(){
        return analysis_on;
    }
    /*analysis_priority*/
    public void setAnalysis_priority(int analysis_priority) {
        this.analysis_priority = analysis_priority;
    }
    public int getAnalysis_priority(){
        return analysis_priority;
    }
    /*analysis_paper*/
    public void setAnalysis_paper(int analysis_paper) {
        this.analysis_paper = analysis_paper;
    }
    public int getAnalysis_paper(){
        return analysis_paper;
    }
    /*weather_on*/
    public void setWeather_on(int weather_on) {
        this.weather_on = weather_on;
    }
    public int getWeather_on(){
        return weather_on;
    }
    /*weather_priority*/
    public void setWeather_priority(int weather_priority) {
        this.weather_priority = weather_priority;
    }
    public int getWeather_priority(){
        return weather_priority;
    }
    /*weather_paper*/
    public void setWeather_paper(int weather_paper) {
        this.weather_paper = weather_paper;
    }
    public int getWeather_paper(){
        return weather_paper;
    }
    public int getWidget() {
        return widget;
    }
    public void setWidget(int widget) {
        this.widget = widget;
    }
    public String toString()
    {
        Gson gson=new Gson();
        String s=gson.toJson(this);
        return s;
    }

}
