package sample.servise;

import sample.entity.Task;

import java.util.ArrayList;

/**
 * Created by Devil on 21.11.2018.
 */
public class Calculation {

    public static void culcEarlyStart(ArrayList<Task> listOfTasks){
        for(int i = 0; i<listOfTasks.size();i++){
            if (listOfTasks.get(i).getNumber().contains(""+1)){
                listOfTasks.get(i).setEarlyStart(0);
            }
            else {
                int count = listOfTasks.indexOf(listOfTasks.get(i));
                Task t = listOfTasks.get(i);
                int countNumber = 0;
                for (int j = count; j>=0;j--){
                    if(t.getNumber().charAt(0)==listOfTasks.get(j).getNumber().charAt(2)){
                        countNumber = countNumber + listOfTasks.get(j).getTimeLine();
                        t = listOfTasks.get(j);
                    }
                }
                listOfTasks.get(i).setEarlyStart(countNumber);
            }
        }
    }

    public static void culcEarlyFinish(ArrayList<Task> listOfTasks){
        for(Task t : listOfTasks){
            t.setEarlyFinish(t.getTimeLine()+t.getEarlyStart());
        }
    }

    public static void culcLateStart(ArrayList<Task> listOfTasks){
        for(Task t : listOfTasks){
            t.setLateStart(t.getLateFinish()-t.getTimeLine());
        }
    }

    public static void culcLateFinish(ArrayList<Task> listOfTasks){
        int number = listOfTasks.get(listOfTasks.size()-1).getNumber().charAt(2);
        int lastNumber = listOfTasks.get(listOfTasks.size()-1).getEarlyFinish();
        if (listOfTasks.size()>2){
            lastNumber = listOfTasks.get(listOfTasks.size()-2).getEarlyFinish();
        }
        for (int i = listOfTasks.size()-1; i>=0; i--){
            Task t = listOfTasks.get(i);
            if(t.getNumber().charAt(2)==number){
                if(t.getEarlyFinish()>=lastNumber){
                    t.setLateFinish(t.getEarlyFinish());
                    lastNumber = t.getEarlyFinish();
                }
                else {
                    t.setLateFinish(lastNumber);
                }
            }
            else {
                for (int j = listOfTasks.size()-1; j>=0; j--){
                    if(t.getNumber().charAt(2)==listOfTasks.get(j).getNumber().charAt(0)){
                        t.setLateFinish(listOfTasks.get(j).getLateFinish()-listOfTasks.get(j).getTimeLine());
                    }
                }
            }
        }
    }

    public static void culcTimeReserve(ArrayList<Task> listOfTasks){
        for(Task t : listOfTasks){
            t.setTimeReserve(t.getLateFinish()-t.getEarlyFinish());
        }
    }
}
