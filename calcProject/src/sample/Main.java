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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.entity.Edge;
import sample.entity.Task;
import sample.servise.Calculation;

import java.util.ArrayList;

public class Main extends Application {

    ArrayList<Edge> listOfEdges = new ArrayList();
    ArrayList<Task> listOfTasks = new ArrayList<>();
    Edge activeEdge;
    Canvas canvas;
    StackPane mainPane;
    TableView<Task> taskTable;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        FlowPane buttonLine = new FlowPane();
        mainPane = new StackPane();
        primaryStage.setTitle("Постановка задачі");
        primaryStage.setScene(new Scene(mainPane, 1000, 700));

        canvas = new Canvas(1000, 600);
        Button createButton = new Button("Створити вершину");
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int newNumber;
                double x;
                double y;
                if(listOfEdges.isEmpty()){
                    newNumber = 1;
                    x = 500;
                    y = 300;
                }
                else {
                    newNumber = listOfEdges.size()+1;
                    x = listOfEdges.get(listOfEdges.size()-1).getX()+50;
                    y = listOfEdges.get(listOfEdges.size()-1).getY()-50;
                }
                Edge edge = new Edge(canvas, x,y,newNumber);
                edge.draw();

                edge.getCicle().getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getX()<edge.getX()+20 && event.getX()>edge.getX()-20 && event.getY()<edge.getY()+20 && event.getY()>edge.getY()-20){
                            edge.getCicle().setFill(Color.BLACK);
                            activeEdge = edge;
                        }
                    }
                });
                edge.getCicle().getCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(activeEdge != null){
                            activeEdge.move(event.getX(),event.getY());
                        }
                        activeEdge = null;
                    }
                });

                listOfEdges.add(edge);
                mainPane.getChildren().add(edge);
            }
        });
        Button grafButton = new Button("з'єднати вершини");
        grafButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createGrafPane();
            }
        });
        Button resultButton = new Button("Результати");
        resultButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Calculation.culcEarlyStart(listOfTasks);
                Calculation.culcEarlyFinish(listOfTasks);
                Calculation.culcLateFinish(listOfTasks);
                Calculation.culcLateStart(listOfTasks);
                Calculation.culcTimeReserve(listOfTasks);
                createFinalTable();
            }
        });
        buttonLine.setHgap(10);
        buttonLine.getChildren().add(createButton);
        buttonLine.getChildren().add(grafButton);
        buttonLine.getChildren().add(resultButton);
        root.getChildren().add(buttonLine);
        root.getChildren().add(canvas);
        mainPane.getChildren().add(root);
        buttonLine.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        primaryStage.show();
    }

    public void createGrafPane(){
        Stage grafWindow = new Stage();
        StackPane grafPane = new StackPane();
        VBox box = new VBox();
        grafWindow.setTitle("З'єднання вершин");
        grafWindow.setScene(new Scene(grafPane,300,300));
        grafPane.setStyle("-fx-background-color: white;");
        grafPane.setPrefSize(200,200);

        Label firstEdgeLabel = new Label("перша вершина");
        TextField firstEdgeField = new TextField();
        Label secondEdgeLabel = new Label("друга вершина");
        TextField secondEdgeField = new TextField();
        Label taskLengthLabel = new Label("тривалість");
        TextField taskLengthField = new TextField();
        Label taskNameLabel = new Label("Найменування задачі");
        TextField taskNameField = new TextField();
        Button okButton = new Button("OK");
        okButton.setPadding(new Insets(5,5,5,5));
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean can = true;
                String[] wrongSimbols = {"-","+","/","[","]","{","}",";",":",".","?","!","q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m"};
                if (listOfEdges.size()<Integer.parseInt(firstEdgeField.getText())-1 || listOfEdges.size()<Integer.parseInt(secondEdgeField.getText())-1 || Integer.parseInt(firstEdgeField.getText())-1>=Integer.parseInt(secondEdgeField.getText())-1){
                    showErrorWindow();
                    can = false;
                    grafWindow.close();
                }

                else {
                    for (int i=0;i<wrongSimbols.length;i++){
                        if (taskLengthField.getText().contains(wrongSimbols[i])){
                            showErrorWindow();
                            can = false;
                            grafWindow.close();
                            break;
                        }
                    }
                }

                if (can){
                    Task task = new Task(firstEdgeField.getText()+"-"+secondEdgeField.getText(),Integer.parseInt(taskLengthField.getText()),taskNameField.getText());
                    listOfTasks.add(task);

                    Edge firstEdge = listOfEdges.get(Integer.parseInt(firstEdgeField.getText())-1);
                    Edge secondEdge = listOfEdges.get(Integer.parseInt(secondEdgeField.getText())-1);
                    createLine(firstEdge.getX()+40,firstEdge.getY()+20,secondEdge.getX(),secondEdge.getY()+20);
                    GraphicsContext taskLength = canvas.getGraphicsContext2D();
                    taskLength.fillText(taskLengthField.getText(),(firstEdge.getX()+secondEdge.getX())/2,(firstEdge.getY()+secondEdge.getY())/2);
                    if(taskTable == null){
                        createTaskTable();
                        taskTable.setMaxSize(110.0, listOfTasks.size()*30.0+20.0);
                        StackPane.setAlignment(taskTable,Pos.CENTER_LEFT);
                        mainPane.getChildren().add(taskTable);
                    }
                    else {
                        ObservableList<Task> list = FXCollections.observableArrayList();
                        for (Task t : listOfTasks){
                            list.add(t);
                        }
                        taskTable.setItems(list);
                        if(taskTable.getHeight()<500){
                            taskTable.setMaxSize(110.0, listOfTasks.size()*30.0+20.0);
                        }
                    }
                    grafWindow.close();
                }
            }
        });

        box.getChildren().addAll(firstEdgeLabel,firstEdgeField,secondEdgeLabel,secondEdgeField,taskLengthLabel,taskLengthField,taskNameLabel,taskNameField,okButton);
        box.setAlignment(Pos.CENTER);
        grafPane.getChildren().add(new Canvas(300,300));
        grafPane.getChildren().add(box);
        grafPane.setAlignment(Pos.CENTER);
        grafWindow.show();
        grafPane.setVisible(true);
    }

    public void createLine(double x1, double y1, double x2, double y2){
        GraphicsContext line = canvas.getGraphicsContext2D();
        line.strokeLine(x1,y1,x2,y2);
    }

    public void createTaskTable(){

        taskTable = new TableView<Task>();

        TableColumn<Task, String> indexCol //
                = new TableColumn<Task, String>("Індекс");

        TableColumn<Task, String> nameCol //
                = new TableColumn<Task, String>("Назва");

        indexCol.setCellValueFactory(new PropertyValueFactory<Task, String>("number"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));

        ObservableList<Task> list = FXCollections.observableArrayList();
        for (Task t : listOfTasks){
            list.add(t);
        }
        taskTable.setItems(list);
        taskTable.getColumns().addAll(indexCol,nameCol);
    }

    public void showErrorWindow(){
        Stage errorWindow = new Stage();
        errorWindow.setTitle("Помилка");
        StackPane errorPane = new StackPane();
        errorWindow.setScene(new Scene(errorPane,200,100));
        errorPane.getChildren().add(new Canvas(200,100));
        Label error = new Label("Введені не валідні дані, перевірте та спробуйте ще раз");
        errorPane.getChildren().add(error);
        errorPane.setAlignment(Pos.CENTER);
        errorWindow.show();
        errorPane.setVisible(true);
    }

    public void createFinalTable(){
        Stage tableWindow = new Stage();
        StackPane tablePane = new StackPane();
        VBox tableBox = new VBox();
        tableWindow.setTitle("Фінальна таблиця");
        tableWindow.setScene(new Scene(tablePane,400,400));
        tablePane.setStyle("-fx-background-color: white;");

        TableView<Task> table = new TableView<Task>();

        TableColumn<Task, String> indexCol //
                = new TableColumn<Task, String>("Індекс");

        TableColumn<Task, Integer> timeLineCol//
                = new TableColumn<Task, Integer>("Тривалість");

        TableColumn<Task, Integer> earlyStartCol//
                = new TableColumn<Task, Integer>("t рн");

        TableColumn<Task, Integer> earlyFinishCol //
                = new TableColumn<Task, Integer>("t рк");

        TableColumn<Task, Integer> lateStartCol //
                = new TableColumn<Task, Integer>("t пн");

        TableColumn<Task, Integer> lateFinishCol //
                = new TableColumn<Task, Integer>("t пк");

        TableColumn<Task, Integer> timeReserveCol //
                = new TableColumn<Task, Integer>("Рп");

        indexCol.setCellValueFactory(new PropertyValueFactory<Task, String>("number"));
        timeLineCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("timeLine"));
        earlyStartCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("earlyStart"));
        earlyFinishCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("earlyFinish"));
        lateStartCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("lateStart"));
        lateFinishCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("lateFinish"));
        timeReserveCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("timeReserve"));

        ObservableList<Task> list = FXCollections.observableArrayList();
        for (Task t : listOfTasks){
            list.add(t);
        }
        table.setItems(list);
        table.getColumns().addAll(indexCol,timeLineCol,earlyStartCol,earlyFinishCol,lateStartCol,lateFinishCol,timeReserveCol);

        tablePane.getChildren().add(new Canvas(300,600));
        tableBox.getChildren().add(table);
        tablePane.getChildren().add(tableBox);
        for (Task t : listOfTasks){
            if(t.getTimeReserve()==0){
                Label taskName = new Label("Задача " + t.getNumber()+" ("+t.getName()+")"+" - Критична");
                tableBox.getChildren().add(taskName);
            }
        }
        tableBox.setAlignment(Pos.CENTER);
        tablePane.setAlignment(Pos.CENTER);
        tableWindow.show();
        tablePane.setVisible(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
