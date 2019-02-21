package sample.entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/**
 * Created by Devil on 10.10.2018.
 */
public class Edge extends Canvas {
    private GraphicsContext cicle;
    private double X;
    private double Y;
    private int number;

    public Edge(Canvas canvas, double x, double y, int number){
        this.cicle = canvas.getGraphicsContext2D();
        this.number = number;
        this.X = x;
        this.Y = y;

    }

    public void move(double dx, double dy) {
        cicle.clearRect(X,Y,40,40);
        X = dx;
        Y = dy;
        draw();

    }

    public void draw() {
        cicle.setFill(Color.AQUA);
        cicle.strokeOval(X,Y,40,40);
        cicle.setFont(javafx.scene.text.Font.font("Consolas", FontWeight.BOLD, 18));
        cicle.fillText(""+number, X+15, Y+25);
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public GraphicsContext getCicle() {
        return cicle;
    }
}
