package sample.entity;

/**
 * Created by Devil on 28.11.2018.
 */
public class Stage {

    private int stageId;
    private String name;
    private int projectId;

    public Stage(int stageId, String name, int projectId){
        this.stageId = stageId;
        this.name = name;
        this.projectId = projectId;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
