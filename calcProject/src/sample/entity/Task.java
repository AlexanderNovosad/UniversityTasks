package sample.entity;

/**
 * Created by Devil on 10.10.2018.
 */
public class Task {
    String name;
    String number;
    int timeLine;
    int earlyStart;
    int earlyFinish;
    int lateStart;
    int lateFinish;
    int timeReserve;

    public Task(String number, int timeLine, String name){
        this.number = number;
        this.timeLine = timeLine;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(int timeLine) {
        this.timeLine = timeLine;
    }

    public int getEarlyStart() {
        return earlyStart;
    }

    public void setEarlyStart(int earlyStart) {
        this.earlyStart = earlyStart;
    }

    public int getEarlyFinish() {
        return earlyFinish;
    }

    public void setEarlyFinish(int earlyFinish) {
        this.earlyFinish = earlyFinish;
    }

    public int getLateStart() {
        return lateStart;
    }

    public void setLateStart(int lateStart) {
        this.lateStart = lateStart;
    }

    public int getLateFinish() {
        return lateFinish;
    }

    public void setLateFinish(int lateFinish) {
        this.lateFinish = lateFinish;
    }

    public int getTimeReserve() {
        return timeReserve;
    }

    public void setTimeReserve(int timeReserve) {
        this.timeReserve = timeReserve;
    }


}
