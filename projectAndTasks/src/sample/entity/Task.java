package sample.entity;

/**
 * Created by Devil on 28.11.2018.
 */
public class Task {

    private String name;
    private int timeLine;
    private int positiveGrade;
    private int posibleGrade;
    private int negativeGrade;
    private int stageId;
    private int projectId;

    public Task(String name, int stageId, int projectId){
        this.name = name;
        this.stageId = stageId;
        this.projectId = projectId;
        this.posibleGrade = 1;
        this.posibleGrade = 2;
        this.negativeGrade = 3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(int timeLine) {
        this.timeLine = timeLine;
    }

    public int getPositiveGrade() {
        return positiveGrade;
    }

    public void setPositiveGrade(int positiveGrade) {
        this.positiveGrade = positiveGrade;
    }

    public int getPosibleGrade() {
        return posibleGrade;
    }

    public void setPosibleGrade(int posibleGrade) {
        this.posibleGrade = posibleGrade;
    }

    public int getNegativeGrade() {
        return negativeGrade;
    }

    public void setNegativeGrade(int negativeGrade) {
        this.negativeGrade = negativeGrade;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
