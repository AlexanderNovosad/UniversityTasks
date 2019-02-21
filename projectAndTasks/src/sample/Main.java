package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.entity.Project;
import sample.entity.Task;
import sample.servise.Calculation;

import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Main extends Application {

    StackPane mainPane;
    ArrayList<Project> listOfProjects = new ArrayList();
    ArrayList<Task> listOfTasks = new ArrayList<>();
    ArrayList<sample.entity.Stage> listOfStages = new ArrayList<>();
    TableView<Task> taskTable;
    ComboBox<Project> projectsComboBox;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        createTestData();
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        VBox root = new VBox();
        VBox workPane = new VBox();
        FlowPane workList = new FlowPane();
        FlowPane createNewDataList = new FlowPane();
        mainPane = new StackPane();
        primaryStage.setTitle("Постановка задачі");
        primaryStage.setScene(new Scene(mainPane, 1000, 700));
        Button createProjectButton = new Button("Створити новий проект");
        createProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createProjectWindow();
            }
        });
        Button createStageButton = new Button("Створити новий етап");
        createStageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createStageWindow();
            }
        });
        Button createTaskButton = new Button("Створити нову задачу");
        createTaskButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createTaskWindow();
            }
        });

        ObservableList<Project> projects = FXCollections.observableArrayList();
        for (Project p : listOfProjects){
            projects.add(p);
        }
        projectsComboBox = new ComboBox<Project>(projects);
        ComboBox<sample.entity.Stage> stagesComboBox = new ComboBox<sample.entity.Stage>();
        projectsComboBox.setValue(projects.get(0));
        projectsComboBox.setConverter(new StringConverter<Project>() {
            @Override
            public String toString(Project p) {
                return p == null ? "" : p.getName();
            }
            @Override
            public Project fromString(String s) {
                Project p = new Project(1, s, new GregorianCalendar(2018,9,10));
                return p;
            }
        });
        stagesComboBox.setConverter(new StringConverter<sample.entity.Stage>() {
            @Override
            public String toString(sample.entity.Stage stage) {
                return stage == null ? "" : stage.getName();
            }
            @Override
            public sample.entity.Stage fromString(String s) {
                sample.entity.Stage stage = new sample.entity.Stage(1,s,1);
                return stage;
            }
        });
        Button gradesForTasksButton = new Button("Виставити оцінки для задач");
        gradesForTasksButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createGradeWindow(stagesComboBox.getValue().getStageId());
            }
        });

        projectsComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<sample.entity.Stage> stages = FXCollections.observableArrayList();
                for(int i=0;i<listOfStages.size();i++){
                    if(listOfStages.get(i).getProjectId()==projectsComboBox.getValue().getProjectId()){
                        stages.add(listOfStages.get(i));
                    }
                }
                stagesComboBox.setItems(stages);
                stagesComboBox.setValue(stages.get(0));
                if(stagesComboBox.getItems() == null){
                    workPane.getChildren().remove(taskTable);
                    workPane.getChildren().remove(gradesForTasksButton);
                }
            }
        });

        stagesComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(taskTable == null){
                    createTaskTable(stagesComboBox.getValue().getStageId());
                    taskTable.setMaxSize(540.0, listOfTasks.size()*30.0+20.0);
                    workPane.getChildren().add(taskTable);
                    workPane.getChildren().add(gradesForTasksButton);
                }
                else {
                    if(!workPane.getChildren().contains(taskTable)){
                        workPane.getChildren().add(taskTable);
                        workPane.getChildren().add(gradesForTasksButton);
                    }
                    ObservableList<Task> list = FXCollections.observableArrayList();
                    for (Task t : listOfTasks){
                        if (t.getStageId() == stagesComboBox.getValue().getStageId()){
                            list.add(t);
                        }
                    }
                    taskTable.setItems(list);
                    if(taskTable.getHeight()<500){
                        taskTable.setMaxSize(540.0, listOfTasks.size()*30.0+20.0);
                    }
                }
            }
        });

        Button projectTimeButton = new Button("Показати тривалість всіх проектів");
        projectTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createProjectTable();
            }
        });
        Button calculationButton = new Button("Розрахувати");
        calculationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Calculation.calcTaskTimeLine(listOfTasks);
                Calculation.calcProjectCompliteTime50(listOfTasks,listOfProjects);
                Calculation.calcProjectCompliteTime95(listOfTasks,listOfProjects);
                Calculation.calcProjectCompliteTime5(listOfTasks,listOfProjects);
                Calculation.convert(listOfProjects);
                workList.getChildren().add(projectTimeButton);
            }
        });

        createNewDataList.setAlignment(Pos.CENTER);
        createNewDataList.getChildren().add(createProjectButton);
        createNewDataList.getChildren().add(createStageButton);
        createNewDataList.getChildren().add(createTaskButton);
        root.getChildren().add(createNewDataList);
        workList.getChildren().add(projectsComboBox);
        workList.getChildren().add(stagesComboBox);
        workList.getChildren().add(calculationButton);
        workList.setAlignment(Pos.CENTER);
        workPane.getChildren().add(workList);
        workPane.setAlignment(Pos.CENTER);
        root.getChildren().add(workPane);
        root.setAlignment(Pos.TOP_CENTER);
        mainPane.getChildren().add(root);


        primaryStage.show();
    }

    public void createGradeWindow(int stageId){
        Stage gradeWindow = new Stage();
        StackPane gradePane = new StackPane();
        VBox box = new VBox();
        gradeWindow.setTitle("Виставлення оцінок");
        gradeWindow.setScene(new Scene(gradePane,300,300));
        gradePane.setStyle("-fx-background-color: white;");
        gradePane.setPrefSize(200,200);

        ObservableList<Task> tasks = FXCollections.observableArrayList();
        for (Task t : listOfTasks){
            if(t.getStageId() == stageId){
                tasks.add(t);
            }
        }
        Label taskLabel = new Label("виберіть задачу");
        ComboBox<Task> tasksComboBox = new ComboBox<Task>(tasks);
        tasksComboBox.setConverter(new StringConverter<Task>() {
            @Override
            public String toString(Task t) {
                return t == null ? "" : t.getName();
            }
            @Override
            public Task fromString(String s) {
                Task t = new Task(s,1,1);
                return t;
            }
        });
        tasksComboBox.setValue(tasks.get(0));

        Label optGradeLabel = new Label("Оптимістична оцінка");
        TextField optGradeField = new TextField();
        Label posibleGradeLabel = new Label("Найбільш можлива оцінка");
        TextField posibleGradeField = new TextField();
        Label negativeGradeLabel = new Label("Песимістична оцінка");
        TextField negativeGradeField = new TextField();
        Label taskNameLabel = new Label("Найменування задачі");
        TextField taskNameField = new TextField(tasksComboBox.getValue().getName());
        Button saveButton = new Button("Зберегти");
        saveButton.setPadding(new Insets(5,5,5,5));
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(Integer.parseInt(optGradeField.getText())<Integer.parseInt(posibleGradeField.getText()) && Integer.parseInt(posibleGradeField.getText())<Integer.parseInt(negativeGradeField.getText())){
                    tasksComboBox.getValue().setPositiveGrade(Integer.parseInt(optGradeField.getText()));
                    tasksComboBox.getValue().setPosibleGrade(Integer.parseInt(posibleGradeField.getText()));
                    tasksComboBox.getValue().setNegativeGrade(Integer.parseInt(negativeGradeField.getText()));
                    tasksComboBox.getValue().setName(taskNameField.getText());
                }
                else {
                    showErrorWindow();
                }
                taskTable.setItems(tasks);
                if(taskTable.getHeight()<500){
                    taskTable.setMaxSize(540.0, listOfTasks.size()*30.0+20.0);
                }
            }
        });

        box.getChildren().addAll(taskLabel,tasksComboBox,optGradeLabel,optGradeField,posibleGradeLabel,posibleGradeField,negativeGradeLabel,negativeGradeField,taskNameLabel,taskNameField,saveButton);
        box.setAlignment(Pos.CENTER);
        gradePane.getChildren().add(box);
        gradePane.setAlignment(Pos.CENTER);
        gradeWindow.show();
        gradePane.setVisible(true);
    }

    public void createTaskTable(int stageId){

        taskTable = new TableView<Task>();

        TableColumn<Task, String> nameCol //
                = new TableColumn<Task, String>("Назва");

        TableColumn<Task, Integer> optCol //
                = new TableColumn<Task, Integer>("Оптимістична оцінка");

        TableColumn<Task, Integer> posCol //
                = new TableColumn<Task, Integer>("Ймовірна оцінка");

        TableColumn<Task, Integer> pesCol //
                = new TableColumn<Task, Integer>("Песимістична оцінка");

        TableColumn<Task, Integer> timeCol //
                = new TableColumn<Task, Integer>("Тривалість");

        nameCol.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        optCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("positiveGrade"));
        posCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("posibleGrade"));
        pesCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("negativeGrade"));
        timeCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("timeLine"));

        ObservableList<Task> list = FXCollections.observableArrayList();
        for (Task t : listOfTasks){
            if (t.getStageId() == stageId){
                list.add(t);
            }
        }
        taskTable.setItems(list);
        taskTable.getColumns().addAll(nameCol,optCol,posCol,pesCol,timeCol);
    }

    public void createProjectTable(){
        Stage tableWindow = new Stage();
        StackPane tablePane = new StackPane();
        VBox tableBox = new VBox();
        tableWindow.setTitle("Таблиця тривалості проектів");
        tableWindow.setScene(new Scene(tablePane,650,400));
        tablePane.setStyle("-fx-background-color: white;");

        TableView<Project> table = new TableView<Project>();

        TableColumn<Project, String> nameCol //
                = new TableColumn<Project, String>("Назва");

        TableColumn<Project, String> startDateCol//
                = new TableColumn<Project, String>("Дата початку");

        TableColumn<Project, String> compliteTime50DateCol//
                = new TableColumn<Project, String>("Дата завершення в 50%");

        TableColumn<Project, String> compliteTime95DateCol//
                = new TableColumn<Project, String>("Дата завершення в 95%");

        TableColumn<Project, String> compliteTime5DateCol//
                = new TableColumn<Project, String>("Дата завершення в 5%");

        nameCol.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("startDateString"));
        compliteTime50DateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("compliteTime50DateString"));
        compliteTime95DateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("compliteTime95DateString"));
        compliteTime5DateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("compliteTime5DateString"));

        ObservableList<Project> list = FXCollections.observableArrayList();
        for (Project p : listOfProjects){
            list.add(p);
        }
        table.setItems(list);
        table.getColumns().addAll(nameCol,startDateCol,compliteTime50DateCol,compliteTime95DateCol,compliteTime5DateCol);

        tableBox.getChildren().add(table);
        tablePane.getChildren().add(tableBox);
        tableBox.setAlignment(Pos.CENTER);
        tablePane.setAlignment(Pos.CENTER);
        tableWindow.show();
        tablePane.setVisible(true);
    }

    public void showErrorWindow(){
        Stage errorWindow = new Stage();
        errorWindow.setTitle("Помилка");
        StackPane errorPane = new StackPane();
        errorWindow.setScene(new Scene(errorPane,200,100));
        errorPane.getChildren().add(new Canvas(200,100));
        Label error = new Label("Перевірте правильність виставлення оцінок");
        errorPane.getChildren().add(error);
        errorPane.setAlignment(Pos.CENTER);
        errorWindow.show();
        errorPane.setVisible(true);
    }

    public void readFile() throws IOException {
        BufferedReader inProjects = new BufferedReader(new FileReader("C:\\projects.txt"));
        BufferedReader inStages = new BufferedReader(new FileReader("C:\\stages.txt"));
        BufferedReader inTasks = new BufferedReader(new FileReader("C:\\tasks.txt"));
        String s = null;
        while ((s=inProjects.readLine())!=null){
            String t[] = s.split("\\.");
            Project project = new Project(Integer.parseInt(t[1]),t[2],new GregorianCalendar(Integer.parseInt(t[3]),Integer.parseInt(t[4])-1,Integer.parseInt(t[5])));
            listOfProjects.add(project);
        }
        inProjects.close();
        while ((s=inStages.readLine())!=null){
            String t[] = s.split("\\.");
            sample.entity.Stage stage = new sample.entity.Stage(Integer.parseInt(t[1]),t[2],Integer.parseInt(t[3]));
            listOfStages.add(stage);
        }
        inStages.close();
        while ((s=inTasks.readLine())!=null){
            String t[] = s.split("\\.");
            Task task = new Task(t[1],Integer.parseInt(t[2]),Integer.parseInt(t[3]));
            listOfTasks.add(task);
        }
        inTasks.close();
    }

    public void saveDataInFile(String filename, String text) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(filename, true), true);
        printStream.println(text);
        printStream.close();

        listOfProjects.clear();
        listOfStages.clear();
        listOfTasks.clear();
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<Project> projects = FXCollections.observableArrayList();
        for (Project p : listOfProjects){
            projects.add(p);
        }
        projectsComboBox.setItems(projects);


    }

    public void createProjectWindow() {
        Stage projectWindow = new Stage();
        StackPane projectPane = new StackPane();
        VBox box = new VBox();
        GridPane dataPane = new GridPane();
        projectWindow.setTitle("Створення проекту");
        projectWindow.setScene(new Scene(projectPane, 300, 300));
        projectPane.setStyle("-fx-background-color: white;");
        projectPane.setPrefSize(200, 200);

        Label projectNameLabel = new Label("Введіть назву проекту");
        TextField projectNameField = new TextField();
        Label dataLabel = new Label("Введіть дату початку проекту");
        Label yearLabel = new Label("Рік");
        TextField yearField = new TextField();
        Label monthLabel = new Label("Місяць");
        TextField monthField = new TextField();
        Label dayLabel = new Label("День");
        TextField dayField = new TextField();
        Button saveButton = new Button("Зберегти");
        saveButton.setPadding(new Insets(5, 5, 5, 5));
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String data = listOfProjects.size()+1 + "." + (listOfProjects.get(listOfProjects.size() - 1).getProjectId() + 1) + "." + projectNameField.getText() + "." + yearField.getText() + "." + monthField.getText() + "." + dayField.getText();
                try {
                    saveDataInFile("C:\\projects.txt", data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                projectWindow.close();
            }
        });

        dataPane.add(yearLabel,0,0);
        dataPane.add(yearField,0,1);
        dataPane.add(monthLabel,1,0);
        dataPane.add(monthField,1,1);
        dataPane.add(dayLabel,2,0);
        dataPane.add(dayField,2,1);
        dataPane.setAlignment(Pos.CENTER);
        box.getChildren().addAll(projectNameLabel,projectNameField,dataLabel,dataPane,saveButton);
        box.setAlignment(Pos.CENTER);
        projectPane.getChildren().add(box);
        projectPane.setAlignment(Pos.CENTER);
        projectWindow.show();
        projectPane.setVisible(true);

    }

    public void createStageWindow(){
        Stage stageWindow = new Stage();
        StackPane stagePane = new StackPane();
        VBox box = new VBox();
        stageWindow.setTitle("Створення етапу");
        stageWindow.setScene(new Scene(stagePane,300,300));
        stagePane.setStyle("-fx-background-color: white;");
        stagePane.setPrefSize(200,200);

        ObservableList<Project> projects = FXCollections.observableArrayList();
        for (Project p : listOfProjects){
            projects.add(p);
        }
        Label projectLabel = new Label("Виберіть проект");
        ComboBox<Project> projectsComboBox = new ComboBox<Project>(projects);
        projectsComboBox.setConverter(new StringConverter<Project>() {
            @Override
            public String toString(Project p) {
                return p == null ? "" : p.getName();
            }
            @Override
            public Project fromString(String s) {
                Project p = new Project(1, s, new GregorianCalendar(2018,9,10));
                return p;
            }
        });
        projectsComboBox.setValue(projects.get(0));

        Label stageNameLabel = new Label("Назва етапу");
        TextField stageNameField = new TextField();
        Button saveButton = new Button("Зберегти");
        saveButton.setPadding(new Insets(5,5,5,5));
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String data = listOfStages.size()+1 + "." + (listOfStages.get(listOfStages.size() - 1).getStageId() + 1)+"."+ stageNameField.getText() + "." + projectsComboBox.getValue().getProjectId();
                try {
                    saveDataInFile("C:\\stages.txt", data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                stageWindow.close();
            }
        });

        box.getChildren().addAll(projectLabel,projectsComboBox,stageNameLabel,stageNameField,saveButton);
        box.setAlignment(Pos.CENTER);
        stagePane.getChildren().add(box);
        stagePane.setAlignment(Pos.CENTER);
        stageWindow.show();
        stagePane.setVisible(true);
    }

    public void createTaskWindow(){
        Stage taskWindow = new Stage();
        StackPane taskPane = new StackPane();
        VBox box = new VBox();
        taskWindow.setTitle("Створення етапу");
        taskWindow.setScene(new Scene(taskPane,300,300));
        taskPane.setStyle("-fx-background-color: white;");
        taskPane.setPrefSize(200,200);

        ObservableList<Project> projects = FXCollections.observableArrayList();
        for (Project p : listOfProjects){
            projects.add(p);
        }
        Label projectLabel = new Label("Виберіть проект");
        ComboBox<Project> projectsComboBox = new ComboBox<Project>(projects);
        Label stageLabel = new Label("Виберіть етап");
        ComboBox<sample.entity.Stage> stagesComboBox = new ComboBox<sample.entity.Stage>();
        projectsComboBox.setValue(projects.get(0));
        projectsComboBox.setConverter(new StringConverter<Project>() {
            @Override
            public String toString(Project p) {
                return p == null ? "" : p.getName();
            }
            @Override
            public Project fromString(String s) {
                Project p = new Project(1, s, new GregorianCalendar(2018,9,10));
                return p;
            }
        });
        stagesComboBox.setConverter(new StringConverter<sample.entity.Stage>() {
            @Override
            public String toString(sample.entity.Stage stage) {
                return stage == null ? "" : stage.getName();
            }
            @Override
            public sample.entity.Stage fromString(String s) {
                sample.entity.Stage stage = new sample.entity.Stage(1,s,1);
                return stage;
            }
        });
        projectsComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<sample.entity.Stage> stages = FXCollections.observableArrayList();
                for(int i=0;i<listOfStages.size();i++){
                    if(listOfStages.get(i).getProjectId()==projectsComboBox.getValue().getProjectId()){
                        stages.add(listOfStages.get(i));
                    }
                }
                stagesComboBox.setItems(stages);
                stagesComboBox.setValue(stages.get(0));
            }
        });

        Label taskNameLabel = new Label("Назва задачі");
        TextField taskNameField = new TextField();
        Button saveButton = new Button("Зберегти");
        saveButton.setPadding(new Insets(5,5,5,5));
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String data = listOfTasks.size()+1 + "." + taskNameField.getText()+"."+ stagesComboBox.getValue().getStageId() + "." + projectsComboBox.getValue().getProjectId();
                try {
                    saveDataInFile("C:\\tasks.txt", data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                taskWindow.close();
            }
        });

        box.getChildren().addAll(projectLabel,projectsComboBox,stageLabel,stagesComboBox,taskNameLabel,taskNameField,saveButton);
        box.setAlignment(Pos.CENTER);
        taskPane.getChildren().add(box);
        taskPane.setAlignment(Pos.CENTER);
        taskWindow.show();
        taskPane.setVisible(true);
    }

//    public void createTestData(){
//        Project project1 = new Project(1, "project1", new GregorianCalendar(2018,9,10));
//        Project project2 = new Project(2,"project2", new GregorianCalendar(2018,10,10));
//        Project project3 = new Project(3,"project3", new GregorianCalendar(2018,11,10));
//        listOfProjects.add(project1); listOfProjects.add(project2); listOfProjects.add(project3);
//
//        sample.entity.Stage stage1 = new sample.entity.Stage(1,"stage1",1);
//        sample.entity.Stage stage2 = new sample.entity.Stage(2,"stage2",1);
//        sample.entity.Stage stage3 = new sample.entity.Stage(3,"stage1",2);
//        sample.entity.Stage stage4 = new sample.entity.Stage(4,"stage2",2);
//        sample.entity.Stage stage5 = new sample.entity.Stage(5,"stage1",3);
//        sample.entity.Stage stage6 = new sample.entity.Stage(6,"stage2",3);
//        listOfStages.add(stage1);
//        listOfStages.add(stage2);
//        listOfStages.add(stage3);
//        listOfStages.add(stage4);
//        listOfStages.add(stage5);
//        listOfStages.add(stage6);
//
//        Task task1 = new Task("task1",1,1);
//        Task task2 = new Task("task2",1,1);
//        Task task3 = new Task("task3",2,1);
//        Task task4 = new Task("task4",2,1);
//        Task task5 = new Task("task1",3,2);
//        Task task6 = new Task("task2",3,2);
//        Task task7 = new Task("task3",4,2);
//        Task task8 = new Task("task4",4,2);
//        Task task9 = new Task("task1",5,3);
//        Task task10 = new Task("task2",5,3);
//        Task task11 = new Task("task3",6,3);
//        Task task12 = new Task("task4",6,3);
//        listOfTasks.add(task1);
//        listOfTasks.add(task2);
//        listOfTasks.add(task3);
//        listOfTasks.add(task4);
//        listOfTasks.add(task5);
//        listOfTasks.add(task6);
//        listOfTasks.add(task7);
//        listOfTasks.add(task8);
//        listOfTasks.add(task9);
//        listOfTasks.add(task10);
//        listOfTasks.add(task11);
//        listOfTasks.add(task12);
//
//    }


    public static void main(String[] args) {
        launch(args);
    }
}
