package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class HexagonalTile {
    private double x;
    private double y;
    private int orientation;
    private final double side = 26;
    private double height = Math.sin((Math.PI/180)*30) * side;
    private double r= Math.cos((Math.PI/180)*30) * side;
    private double diagonal = 52;
    private double[] xPoints = new double[6];
    private double[] yPoints = new double[6];
    private Canvas canvas;
    private double xc;
    private double yc;

    HexagonalTile(double x, double y, int orientation, Canvas canvas){
        if(x!=0) {
            this.x = x;
            this.y = y;
            this.orientation = orientation;
            this.canvas = canvas;
            setPoints();
            findCenter();
        }
    }

    private void setPoints(){
        xPoints[0] = x;
        xPoints[1] = x + side;
        xPoints[2] = x + side + height;
        xPoints[3] = x + side;
        xPoints[4] = x;
        xPoints[5] = x-height;

        yPoints[0] = y;
        yPoints[1] = y;
        yPoints[2] = y + r;
        yPoints[3] = y + r + r;
        yPoints[4] = y + r +r;
        yPoints[5] = y + r;

    }

    public void plot() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.setFill(Color.BLUE);
//        gc.fillPolygon(xPoints, yPoints,6);
        switch (orientation) {
            case 1:
                plotOrientation1(gc);
                break;
            case 2:
                plotOrientation2(gc);
                break;
        }
    }
    private void plotOrientation1(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints,yPoints,6);
        gc.setFill(Color.WHITE);
        gc.fillArc(xPoints[0]-13,yPoints[0]-13,26,26,0,-120, ArcType.ROUND); //top left
        gc.fillArc(xPoints[4] - 13,yPoints[4] - 13,26,26,0,120,ArcType.ROUND); // bottom left
        gc.fillArc(xPoints[2] - 13,yPoints[2] - 13,26,26,120,120,ArcType.ROUND); //right
    }

    private void plotOrientation2(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillPolygon(xPoints,yPoints,6);
        gc.setFill(Color.BLACK);
        gc.fillArc(xPoints[0]-13,yPoints[0]-13,26,26,0,-120, ArcType.ROUND); //top left
        gc.fillArc(xPoints[4] - 13,yPoints[4] - 13,26,26,0,120,ArcType.ROUND); // bottom left
        gc.fillArc(xPoints[2] - 13,yPoints[2] - 13,26,26,120,120,ArcType.ROUND); //right
    }

    private void findCenter(){
        double x0 = xPoints[0];
        double x1 = xPoints[3];
        double y0 = yPoints[0];
        double y1 = yPoints[3];
        xc = (x0+x1)/2;
        yc = (y0+y1)/2;
    }

    public double[] getxPoints(){ return xPoints;}
    public double[] getyPoints(){ return yPoints;}
    public int getOrientation(){return orientation;}

    public double[] getCenter(){return new double[]{xc,yc};}

    public boolean checkIfPointInHexagon(double x, double y){
        double distanceFromCenter = Math.sqrt(((xc - x)*(xc - x))+((yc - y)*(yc - y)));
        if(distanceFromCenter <= 26) return true;

        return false;
    }
}
