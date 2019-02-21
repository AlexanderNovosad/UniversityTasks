package sample.entity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Devil on 28.11.2018.
 */
public class Project {

    private int projectId;
    private String name;
    private Calendar startDate;
    private Calendar compliteTime5Date;
    private Calendar compliteTime50Date;
    private Calendar compliteTime95Date;
    private int compliteTime5;
    private int compliteTime50;
    private int compliteTime95;
    private String startDateString;
    private String compliteTime5DateString;
    private String compliteTime50DateString;
    private String compliteTime95DateString;

    public Project(int projectId, String name, GregorianCalendar date){
        this.projectId = projectId;
        this.name = name;
        this.startDate = date;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int id) {
        this.projectId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompliteTime5() {
        return compliteTime5;
    }

    public void setCompliteTime5(int compliteTime5) {
        this.compliteTime5 = compliteTime5;
    }

    public int getCompliteTime50() {
        return compliteTime50;
    }

    public void setCompliteTime50(int compliteTime50) {
        this.compliteTime50 = compliteTime50;
    }

    public int getCompliteTime95() {
        return compliteTime95;
    }

    public void setCompliteTime95(int compliteTime95) {
        this.compliteTime95 = compliteTime95;
    }

    public void setCompliteTime95Date(GregorianCalendar compliteTime95Date) {
        this.compliteTime95Date = compliteTime95Date;
    }


    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getCompliteTime5DateString() {
        return compliteTime5DateString;
    }

    public void setCompliteTime5DateString(String compliteTime5DateString) {
        this.compliteTime5DateString = compliteTime5DateString;
    }

    public String getCompliteTime50DateString() {
        return compliteTime50DateString;
    }

    public void setCompliteTime50DateString(String compliteTime50DateString) {
        this.compliteTime50DateString = compliteTime50DateString;
    }

    public String getCompliteTime95DateString() {
        return compliteTime95DateString;
    }

    public void setCompliteTime95DateString(String compliteTime95DateString) {
        this.compliteTime95DateString = compliteTime95DateString;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getCompliteTime5Date() {
        return compliteTime5Date;
    }

    public void setCompliteTime5Date(Calendar compliteTime5Date) {
        this.compliteTime5Date = compliteTime5Date;
    }

    public Calendar getCompliteTime50Date() {
        return compliteTime50Date;
    }

    public void setCompliteTime50Date(Calendar compliteTime50Date) {
        this.compliteTime50Date = compliteTime50Date;
    }

    public Calendar getCompliteTime95Date() {
        return compliteTime95Date;
    }

    public void setCompliteTime95Date(Calendar compliteTime95Date) {
        this.compliteTime95Date = compliteTime95Date;
    }

}
