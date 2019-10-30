package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class CircleTile {
    private double x;
    private double y;
    private int orientation;
    private Canvas canvas;
    private int step = 45;

    CircleTile(double x, double y, int orientation, Canvas canvas){
        this.x=x;
        this.y=y;
        this.orientation=orientation;
        this.canvas=canvas;
    }

    public int getOrientation(){
        return orientation;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void plot(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (orientation){
            case 1:
                plotOrientation1(gc);
                break;
            case 2:
                plotOrientation2(gc);
                break;
            case 3:
                plotOrientation3(gc);
                break;
            case 4:
                plotOrientation4(gc);
                break;
        }
    }

    private void plotOrientation1(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x,x+step,x+step,x}, new double[]{y,y,y+step,y+step},4);
        gc.setFill(Color.WHITE);
        gc.fillArc(x + (step/2),y - (step/2),45,45,180,90, ArcType.ROUND); //top right
        gc.fillArc(x- (step/2),y + (step/2),45,45,0,90,ArcType.ROUND); // left bottom
    }

    private void plotOrientation2(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillPolygon(new double[]{x,x+step,x+step,x}, new double[]{y,y,y+step,y+step},4);
        gc.setFill(Color.BLACK);
        gc.fillArc(x + (step/2),y - (step/2),45,45,180,90, ArcType.ROUND); //top right
        gc.fillArc(x- (step/2),y + (step/2),45,45,0,90,ArcType.ROUND); // left bottom
    }

    private void plotOrientation3(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x,x+step,x+step,x}, new double[]{y,y,y+step,y+step},4);
        gc.setFill(Color.WHITE);
        gc.fillArc(x - (step/2),y - (step/2) ,45,45,0,-90,ArcType.ROUND); //top left
        gc.fillArc(x + (step/2),y + (step/2),45,45,90,90,ArcType.ROUND); // bottom right
    }

    private void plotOrientation4(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillPolygon(new double[]{x,x+step,x+step,x}, new double[]{y,y,y+step,y+step},4);
        gc.setFill(Color.BLACK);
        gc.fillArc(x - (step/2),y - (step/2) ,45,45,0,-90,ArcType.ROUND); //top left
        gc.fillArc(x + (step/2),y + (step/2),45,45,90,90,ArcType.ROUND); // bottom right
    }
}
