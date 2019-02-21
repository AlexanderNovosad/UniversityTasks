package sample.servise;

import sample.entity.Project;
import sample.entity.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Devil on 03.12.2018.
 */
public class Calculation {
    public static void calcTaskTimeLine(ArrayList<Task> listOfTasks){
        for(Task t : listOfTasks){
            t.setTimeLine((t.getPositiveGrade()+4*t.getPosibleGrade()+t.getNegativeGrade())/6);
        }
    }

    public static void calcProjectCompliteTime50(ArrayList<Task> listOfTasks, ArrayList<Project> listOfProjects){
        for(Project p : listOfProjects){
            int count = 0;
            for(Task t : listOfTasks){
                if(t.getProjectId() == p.getProjectId()){
                    count = count + t.getTimeLine();
                }
            }
            p.setCompliteTime50(count);
            Calendar newDate = new GregorianCalendar(p.getStartDate().get(Calendar.YEAR),p.getStartDate().get(Calendar.MONTH),p.getStartDate().get(Calendar.DAY_OF_MONTH));
            newDate.add(Calendar.DAY_OF_YEAR,count);
            p.setCompliteTime50Date(newDate);
        }
    }

    public static double calcStandardDeviation(ArrayList<Task> listOfTasks, Project project){
        double count = 0;
        for(Task t : listOfTasks){
            if(t.getProjectId() == project.getProjectId()){
                count = count + Math.pow((t.getNegativeGrade()-t.getPositiveGrade())/6,2);
            }
        }
        return Math.sqrt(count);
    }

    public static void calcProjectCompliteTime95(ArrayList<Task> listOfTasks, ArrayList<Project> listOfProjects){
        for(Project p : listOfProjects){
            double standardDeviation = calcStandardDeviation(listOfTasks,p);
            p.setCompliteTime95((int)Math.round(p.getCompliteTime50()+2*standardDeviation));

            Calendar newDate = new GregorianCalendar(p.getStartDate().get(Calendar.YEAR),p.getStartDate().get(Calendar.MONTH),p.getStartDate().get(Calendar.DAY_OF_MONTH));
            newDate.add(Calendar.DAY_OF_YEAR,p.getCompliteTime95());
            p.setCompliteTime95Date(newDate);
        }
    }

    public static void calcProjectCompliteTime5(ArrayList<Task> listOfTasks, ArrayList<Project> listOfProjects){
        for(Project p : listOfProjects){
            double standardDeviation = calcStandardDeviation(listOfTasks,p);
            p.setCompliteTime5((int)Math.round(p.getCompliteTime50()-2*standardDeviation));

            Calendar newDate = new GregorianCalendar(p.getStartDate().get(Calendar.YEAR),p.getStartDate().get(Calendar.MONTH),p.getStartDate().get(Calendar.DAY_OF_MONTH));
            newDate.add(Calendar.DAY_OF_YEAR,p.getCompliteTime5());
            p.setCompliteTime5Date(newDate);
        }
    }

    public static void convert(ArrayList<Project> listOfProjects){
        for(Project p : listOfProjects){
            p.setStartDateString(""+p.getStartDate().get(Calendar.DAY_OF_MONTH)+"."+(p.getStartDate().get(Calendar.MONTH)+1)+"."+p.getStartDate().get(Calendar.YEAR));
            p.setCompliteTime50DateString(""+p.getCompliteTime50Date().get(Calendar.DAY_OF_MONTH)+"."+(p.getCompliteTime50Date().get(Calendar.MONTH)+1)+"."+p.getCompliteTime50Date().get(Calendar.YEAR));
            p.setCompliteTime5DateString(""+p.getCompliteTime5Date().get(Calendar.DAY_OF_MONTH)+"."+(p.getCompliteTime5Date().get(Calendar.MONTH)+1)+"."+p.getCompliteTime5Date().get(Calendar.YEAR));
            p.setCompliteTime95DateString(""+p.getCompliteTime95Date().get(Calendar.DAY_OF_MONTH)+"."+(p.getCompliteTime95Date().get(Calendar.MONTH)+1)+"."+p.getCompliteTime95Date().get(Calendar.YEAR));
        }
    }

}
