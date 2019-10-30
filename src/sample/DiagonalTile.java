package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DiagonalTile {

    private double x;
    private double y;
    private int orientation;
    private Canvas canvas;
    private int step = 45;

    DiagonalTile(double x, double y, int orientation, Canvas canvas){
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
        plotSquare(gc);
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x,x+step,x}, new double[]{y,y+step,y+step}, 3);
    }

    private void plotOrientation2(GraphicsContext gc){
        plotSquare(gc);
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x,x+step,x}, new double[]{y,y,y+step}, 3);
    }

    private void plotOrientation3(GraphicsContext gc){
        plotSquare(gc);
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x,x+step,x+step}, new double[]{y,y,y+step}, 3);
    }

    private void plotOrientation4(GraphicsContext gc){
        plotSquare(gc);
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x+step,x+step,x}, new double[]{y,y+step,y+step}, 3);
    }

    private void plotSquare(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillPolygon(new double[]{x,x+step,x+step,x}, new double[]{y,y,y+step,y+step}, 4);
    }

}
