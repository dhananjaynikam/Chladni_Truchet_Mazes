package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application{
    private Stage window = new Stage();
    Scene startPageScene;
    Random r = new Random();
    private int height = 600;
    private int width = 800;
    public int canvasHeight = 450;
    public int canvasWidth = 450;
    private Integer[] randomOrientation = new Integer[100];
    private Integer[] hexRandomOrientation = new Integer[105];

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setScene(getStartPageSceneScene());
        window.setTitle("Chladni, Truchet and Mazes");
        window.show();

    }

    public Scene getStartPageSceneScene() throws FileNotFoundException {

        BackgroundImage backgroundImage = new BackgroundImage(new Image(new FileInputStream(new File(Main.class.getResource("background.jpg").getFile()))), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        MenuBar menuBar = new MenuBar();

        Menu startMenu = new Menu("START");
        MenuItem squareMenu = new MenuItem("SQUARE");
        MenuItem hexagonMenu = new MenuItem("HEXAGON");
        startMenu.getItems().addAll(squareMenu,hexagonMenu);

        Menu moreAbout = new Menu("More about project");

        MenuItem learn = new MenuItem("LEARN");
        MenuItem howItWorks = new MenuItem("HOW IT WORKS");

        moreAbout.getItems().addAll(learn,howItWorks);

        menuBar.getMenus().addAll(startMenu,moreAbout);
        menuBar.setStyle("-fx-font-size: 1.2em; -fx-background-color: #ffffff;-fx-border-style: solid inside;-fx-border-width: 2;-fx-border-insets: 5;-fx-border-radius: 5; -fx-border-color: blue; ");
        //get the next 3 scenes
        Scene squareDiagonalScene = getSquareDiagonalScene();
        Scene squareCircleScene = getSquareCircleScene();
        Scene hexagonScene = getHexagonScene();
        squareMenu.setOnAction(e -> {
            String selection = SelectionBox.display("SQUARE", "Select the type of square tile", "Diagonal", "Circle");
            if(selection.equals("Diagonal")){
                window.setScene(squareDiagonalScene);
            }else if(selection.equals("Circle")){
                window.setScene(squareCircleScene);
            }
        });
        hexagonMenu.setOnAction( e -> {
            window.setScene(hexagonScene);
        });

        learn.setOnAction(e -> {
            LearnWindow.display();
        });

        howItWorks.setOnAction(e -> {
            HowItWorksWindow.display();
        });

        //Creating label for the centre pane
        Label introLabel = new Label("Chladni, Truchet and Mazes");
        introLabel.setStyle("-fx-font-size: 4em; -fx-background-color: #ffffff; ");

        VBox centreStartScene = new VBox();
        centreStartScene.setAlignment(Pos.CENTER);
        centreStartScene.setPadding(new Insets(20,10,10,10));
        introLabel.setWrapText(true);
        centreStartScene.getChildren().add(introLabel);

        //BorderPane layout to get all the layouts together
        BorderPane startPageLayout = new BorderPane();
        startPageLayout.setTop(menuBar);
        startPageLayout.setCenter(centreStartScene);
        startPageLayout.setBackground(background);

        startPageScene = new Scene(startPageLayout,width,height);

        return startPageScene;
    }

    public Scene getSquareDiagonalScene() throws FileNotFoundException {
        Hashtable<Point2D,PositionId> diagonalPixelToPid = new Hashtable<>();
        Hashtable<PositionId,DiagonalTile> diagonalPidToTile = new Hashtable<>();
        Graph diagonalTileGraph = new Graph();

        Scene squareDiagonalScene;
        BorderPane borderPane = new BorderPane();
        Pane wrapperPane = new Pane();
        Button home = new Button("HOME");
        Button clear = new Button("CLEAR");
        Button maze = new Button("Check Maze");
        Button dominoes = new Button("Generate Dominoes");

        Image orientationImage1 = new Image(new FileInputStream(new File(Main.class.getResource("squarediagonalorientation1.png").getFile())));
        Image orientationImage2 = new Image(new FileInputStream(new File(Main.class.getResource("squarediagonalorientation2.png").getFile())));
        Image orientationImage3 = new Image(new FileInputStream(new File(Main.class.getResource("squarediagonalorientation3.png").getFile())));
        Image orientationImage4 = new Image(new FileInputStream(new File(Main.class.getResource("squarediagonalorientation4.png").getFile())));
        ImageView imageViewOrientation1 = new ImageView(orientationImage1);
        ImageView imageViewOrientation2 = new ImageView(orientationImage2);
        ImageView imageViewOrientation3 = new ImageView(orientationImage3);
        ImageView imageViewOrientation4 = new ImageView(orientationImage4);

        wrapperPane.setMaxHeight(canvasHeight);
        wrapperPane.setMaxWidth(canvasWidth);
        wrapperPane.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));

        Canvas canvas = new Canvas(canvasWidth,canvasHeight);
        draw(canvas);
        wrapperPane.getChildren().add(canvas);
        borderPane.setLeft(wrapperPane);
        borderPane.setPadding(new Insets(50,10,10,20));
        borderPane.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        //borderPane.set
        VBox rightSide = new VBox(10);

        HBox orientations1 = getHboxDisplays(imageViewOrientation1);
        HBox orientations2 = getHboxDisplays(imageViewOrientation2);
        HBox orientations3 = getHboxDisplays(imageViewOrientation3);
        HBox orientations4 = getHboxDisplays(imageViewOrientation4);

        Button generateTiling = new Button("Generate Tiling");
        Button generateRandomly = new Button("Generate Randomly");
        rightSide.getChildren().addAll(orientations1,orientations2,orientations3,orientations4,generateTiling,generateRandomly,home,clear,maze,dominoes);
        borderPane.setRight(rightSide);
        squareDiagonalScene = new Scene(borderPane, width,height);
        initialiseDiagonalGraph(diagonalTileGraph);

        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            double count = e.getClickCount();
            DiagonalTile tile;
            for(int i =0; i< 450; i= i+45){
                if(x > i && x < i+45) x = i;
            }
            for(int i = 0; i< 450; i = i+45){
                if(y > i && y < i+45) y = i;
            }
            PositionId changedPosition = diagonalPixelToPid.get(new Point2D(x,y));
            for(int i = 1; i<= count; i++){
                int orientation = i %4;
                if(orientation == 0) orientation = 4;
                tile = new DiagonalTile(x,y,orientation,canvas);
                tile.plot();
                diagonalPidToTile.put(changedPosition,tile);
            }
        });

        home.setOnAction( e -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0,0,canvasWidth,canvasHeight);
            diagonalPixelToPid.clear();
            diagonalPidToTile.clear();
            window.setScene(startPageScene);
        });

        generateRandomly.setOnAction( e -> {
            System.out.println("call the maze generator method from here");
            if(diagonalPidToTile.size() != 0){
                diagonalPidToTile.clear();
                diagonalPixelToPid.clear();
            }
            int orientation;
            int k = 0, l =0;
            for(double i=0; i< canvasWidth; i = i+45,k++){
                for(double j = 0; j< canvasHeight; j = j+45,l++){
                    orientation = getRandomOrientation(4,1);
                    DiagonalTile tile = new DiagonalTile(i,j,orientation,canvas);
                    diagonalPixelToPid.put(new Point2D(i,j),new PositionId(k,l));
                    diagonalPidToTile.put(new PositionId(k,l), tile);
                    tile.plot();
                }
                l = 0;
            }
        });

        generateTiling.setOnAction( e -> {
            System.out.println("call the maze generator method from here");
            TextField orientation1Prob =(TextField) orientations1.getChildren().get(2);
            TextField orientation2Prob =(TextField) orientations2.getChildren().get(2);
            TextField orientation3Prob =(TextField) orientations3.getChildren().get(2);
            TextField orientation4Prob =(TextField) orientations4.getChildren().get(2);
            double [] prob = new double[]{Double.parseDouble(orientation1Prob.getText()),Double.parseDouble(orientation2Prob.getText()),Double.parseDouble(orientation3Prob.getText()),Double.parseDouble(orientation4Prob.getText())};
            Boolean validateInput = validate(prob);
            if(diagonalPidToTile.size() != 0){
                diagonalPidToTile.clear();
                diagonalPixelToPid.clear();
            }
            if(validateInput){
                randomGeneration(prob);
                int orientation;
                int counter=0;
                int k = 0, l =0;
                for (double i = 0; i < canvasWidth; i = i + 45,k++) {
                    for (double j = 0; j < canvasHeight; j = j + 45,counter++,l++) {
                        orientation = randomOrientation[counter];
                        DiagonalTile tile = new DiagonalTile(i, j, orientation, canvas);
                        diagonalPixelToPid.put(new Point2D(i,j),new PositionId(k,l));
                        diagonalPidToTile.put(new PositionId(k,l), tile);
                        tile.plot();
                    }
                    l = 0;
                }

            } else{
                String result = SelectionBox.display("ERROR", "The total should be 100","OK", null);
            }
        });

        clear.setOnAction(e -> {
            TextField text1 =(TextField) orientations1.getChildren().get(2);
            TextField text2 =(TextField) orientations2.getChildren().get(2);
            TextField text3 =(TextField) orientations3.getChildren().get(2);
            TextField text4 =(TextField) orientations4.getChildren().get(2);
            text1.clear();
            text2.clear();
            text3.clear();
            text4.clear();
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,450,450);
            diagonalPixelToPid.clear();
            diagonalPidToTile.clear();
            draw(canvas);
        });

        maze.setOnAction(e -> {
            System.out.println("call the detect maze method");
            List<PositionId> path1 = MazeDetector.checkDiagonalMaze(diagonalTileGraph, new PositionId(0,0), new PositionId(9,9), diagonalPidToTile);
            List<PositionId> path2 = MazeDetector.checkDiagonalMaze(diagonalTileGraph, new PositionId(9,0), new PositionId(0,9), diagonalPidToTile);
            if(path1.size() == 0 && path2.size()==0){
                System.out.println("No Maze");
            } else if(path1.size() !=0){
                for(PositionId item : path1){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
                drawGrid(canvas,path1,diagonalPidToTile);
            } else {
                for(PositionId item : path2){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
                drawGrid(canvas,path2,diagonalPidToTile);
            }
        });

        dominoes.setOnAction(e -> {
            createDiagonalDominoes(canvas);
        });

        return squareDiagonalScene;
    }

    public Scene getSquareCircleScene() throws FileNotFoundException {
        Hashtable<Point2D,PositionId> circlePixelToPid = new Hashtable<>();
        Hashtable<PositionId,CircleTile> circlePidToTile = new Hashtable<>();
        Graph circleTileGraph = new Graph();

        initialiseDiagonalGraph(circleTileGraph);

        Scene squareCircleScene;
        BorderPane borderPane = new BorderPane();
        Pane wrapperPane = new Pane();
        Button home = new Button("HOME");
        Button clear = new Button("CLEAR");
        Button maze = new Button("Check Maze");
        home.setOnAction( e -> window.setScene(startPageScene));
        Image orientationImage1 = new Image(new FileInputStream(new File(Main.class.getResource("squarecircleorientation1.png").getFile())));
        Image orientationImage2 = new Image(new FileInputStream(new File(Main.class.getResource("squarecircleorientation2.png").getFile())));
        Image orientationImage3 = new Image(new FileInputStream(new File(Main.class.getResource("squarecircleorientation3.png").getFile())));
        Image orientationImage4 = new Image(new FileInputStream(new File(Main.class.getResource("squarecircleorientation4.png").getFile())));
        ImageView imageViewOrientation1 = new ImageView(orientationImage1);
        ImageView imageViewOrientation2 = new ImageView(orientationImage2);
        ImageView imageViewOrientation3 = new ImageView(orientationImage3);
        ImageView imageViewOrientation4 = new ImageView(orientationImage4);
        imageViewOrientation1.setFitHeight(50);
        imageViewOrientation1.setFitWidth(50);
        imageViewOrientation2.setFitHeight(50);
        imageViewOrientation2.setFitWidth(50);
        imageViewOrientation3.setFitHeight(50);
        imageViewOrientation3.setFitWidth(50);
        imageViewOrientation4.setFitHeight(50);
        imageViewOrientation4.setFitWidth(50);

        wrapperPane.setMaxHeight(canvasHeight);
        wrapperPane.setMaxWidth(canvasHeight);
        wrapperPane.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));

        Canvas canvas = new Canvas(canvasWidth,canvasHeight);
        draw(canvas);
        wrapperPane.getChildren().add(canvas);
        borderPane.setLeft(wrapperPane);
        borderPane.setPadding(new Insets(50,10,10,20));
        borderPane.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        //borderPane.set
        VBox rightSide = new VBox(10);

        HBox orientations1 = getHboxDisplays(imageViewOrientation1);
        HBox orientations2 = getHboxDisplays(imageViewOrientation2);
        HBox orientations3 = getHboxDisplays(imageViewOrientation3);
        HBox orientations4 = getHboxDisplays(imageViewOrientation4);

        Button generateTiling = new Button("Generate Tiling");
        Button generateRandomly = new Button("Generate Randomly");
        rightSide.getChildren().addAll(orientations1,orientations2,orientations3,orientations4,generateTiling,generateRandomly,home,clear,maze);
        borderPane.setRight(rightSide);
        squareCircleScene = new Scene(borderPane, width,height);

        home.setOnAction( e -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0,0,canvasWidth,canvasHeight);
            circlePidToTile.clear();
            circlePixelToPid.clear();
            window.setScene(startPageScene);
        });

        generateTiling.setOnAction( e ->
        {
            System.out.println("call the maze generator method from here");
            if(circlePixelToPid.size() != 0){
                circlePidToTile.clear();
                circlePixelToPid.clear();
            }
            TextField orientation1Prob =(TextField) orientations1.getChildren().get(2);
            TextField orientation2Prob =(TextField) orientations2.getChildren().get(2);
            TextField orientation3Prob =(TextField) orientations3.getChildren().get(2);
            TextField orientation4Prob =(TextField) orientations4.getChildren().get(2);
            double [] prob = new double[]{Double.parseDouble(orientation1Prob.getText()),Double.parseDouble(orientation2Prob.getText()),Double.parseDouble(orientation3Prob.getText()),Double.parseDouble(orientation4Prob.getText())};
            Boolean validateInput = validate(prob);
            if(validateInput) {
                randomGeneration(prob);
                int counter=0;
                int orientation;
                int k=0,l=0;
                for (double i = 0; i < canvasWidth; i = i + 45,k++) {
                    for (double j = 0; j < canvasHeight; j = j + 45,l++,counter++) {
                        orientation = randomOrientation[counter];
                        CircleTile tile = new CircleTile(i, j, orientation, canvas);
                        circlePixelToPid.put(new Point2D(i,j),new PositionId(k,l));
                        circlePidToTile.put(new PositionId(k,l), tile);
                        tile.plot();
                    }
                    l=0;
                }
            }else{
                String result = SelectionBox.display("ERROR", "The total should be 100","OK", null);
            }
        });

        generateRandomly.setOnAction( e -> {
            System.out.println("call the maze generator method from here");
            if(circlePixelToPid.size() != 0){
                circlePidToTile.clear();
                circlePixelToPid.clear();
            }
                int orientation;
                int counter=0;
                int k =0,l=0;
                for (double i = 0; i < canvasWidth; i = i + 45,k++) {
                    for (double j = 0; j < canvasHeight; j = j + 45,l++,counter++) {
                        orientation = getRandomOrientation(4,1);
                        CircleTile tile = new CircleTile(i, j, orientation, canvas);
                        circlePixelToPid.put(new Point2D(i,j),new PositionId(k,l));
                        circlePidToTile.put(new PositionId(k,l), tile);
                        tile.plot();
                    }
                    l=0;
                }
        });

        clear.setOnAction(e -> {
            TextField text1 =(TextField) orientations1.getChildren().get(2);
            TextField text2 =(TextField) orientations2.getChildren().get(2);
            TextField text3 =(TextField) orientations3.getChildren().get(2);
            TextField text4 =(TextField) orientations4.getChildren().get(2);
            text1.clear();
            text2.clear();
            text3.clear();
            text4.clear();
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,450,450);
            circlePidToTile.clear();
            circlePixelToPid.clear();
            draw(canvas);
        });

        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            double count = e.getClickCount();
            CircleTile tile;
            for(int i =0; i< 450; i= i+45){
                if(x > i && x < i+45) x = i;
            }
            for(int i = 0; i< 450; i = i+45){
                if(y > i && y < i+45) y = i;
            }
            PositionId changedPosition = circlePixelToPid.get(new Point2D(x,y));
            for(int i = 1; i<= count; i++){
                int orientation = i %4;
                if(orientation == 0) orientation = 4;
                tile = new CircleTile(x,y,orientation,canvas);
                tile.plot();
                circlePidToTile.put(changedPosition,tile);
            }
        });

        maze.setOnAction(e -> {
            System.out.println("call the detect maze method");
            List<PositionId> path1 = MazeDetector.checkCircleMaze(circleTileGraph, new PositionId(0,0), new PositionId(9,9), circlePidToTile);
            List<PositionId> path2 = MazeDetector.checkCircleMaze(circleTileGraph, new PositionId(9,0), new PositionId(0,9), circlePidToTile);
            if(path1.size() == 0 && path2.size()==0){
                System.out.println("No Maze");
            } else if(path1.size() !=0){
                for(PositionId item : path1){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
                highlightCircleMaze(canvas,path1,circlePidToTile);
            } else {
                for(PositionId item : path2){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
                highlightCircleMaze(canvas,path2,circlePidToTile);
            }

        });

        return squareCircleScene;
    }

    public Scene getHexagonScene() throws FileNotFoundException {
        Scene hexagonalScene;
        Hashtable<Point2D,PositionId> hexPixelToPid = new Hashtable<>();
        Hashtable<PositionId,HexagonalTile> hexPidToTile = new Hashtable<>();
        Graph hexTilesGraph = new Graph();

        initialiseHexGraph(hexTilesGraph);

        double r = Math.cos((Math.PI/180)*30) * 26; //hexagon calculation

        BorderPane borderPane = new BorderPane();
        Pane wrapperPane = new Pane();
        Button home = new Button("HOME");
        Button clear = new Button("CLEAR");
        Button maze = new Button("Check Maze");

        home.setOnAction( e -> window.setScene(startPageScene));
        Image orientationImage1 = new Image(new FileInputStream(new File(Main.class.getResource("hexagon1.png").getFile())));
        Image orientationImage2 = new Image(new FileInputStream(new File(Main.class.getResource("hexagon2.png").getFile())));
        ImageView imageViewOrientation1 = new ImageView(orientationImage1);
        ImageView imageViewOrientation2 = new ImageView(orientationImage2);
        imageViewOrientation1.setFitHeight(50);
        imageViewOrientation1.setFitWidth(50);
        imageViewOrientation2.setFitHeight(50);
        imageViewOrientation2.setFitWidth(50);

        wrapperPane.setMaxHeight(450);
        wrapperPane.setMaxWidth(442);
        wrapperPane.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        Canvas canvas = new Canvas(442,450);
        draw(canvas);
        wrapperPane.getChildren().add(canvas);
        borderPane.setLeft(wrapperPane);
        borderPane.setPadding(new Insets(50,10,10,20));
        borderPane.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        //borderPane.set
        VBox rightSide = new VBox(10);

        HBox orientations1 = getHboxDisplays(imageViewOrientation1);
        HBox orientations2 = getHboxDisplays(imageViewOrientation2);

        Button generateTiling = new Button("Generate Tiling");
        Button generateRandomly = new Button("Generate Randomly");
        rightSide.getChildren().addAll(orientations1,orientations2,generateTiling,generateRandomly,home,clear,maze);
        borderPane.setRight(rightSide);
        hexagonalScene = new Scene(borderPane, width,height);

        home.setOnAction( e -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0,0,canvasWidth,canvasHeight);
            hexPidToTile.clear();
            hexPixelToPid.clear();
            window.setScene(startPageScene);
        });

        clear.setOnAction(e -> {
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,442,450);
            hexPixelToPid.clear();
            hexPidToTile.clear();
        });

        generateTiling.setOnAction( e -> {
            System.out.println("call the maze generator method from here");
            TextField orientation1Prob =(TextField) orientations1.getChildren().get(2);
            TextField orientation2Prob =(TextField) orientations2.getChildren().get(2);
            double [] prob = new double[]{Double.parseDouble(orientation1Prob.getText()),Double.parseDouble(orientation2Prob.getText())};
            Boolean validateInput = validate(prob);
            if(validateInput){
                hexRandomGeneration(prob);
                int columnCounter = 1, columnType = 0;
                int orientation,counter=0;
                int k = 0, l =0;
                for(double x = 13; x < 442; x = x+ 39,k++,columnCounter++){
                    if(columnCounter % 2 == 0 ) columnType =  1;
                    for(double y = 0; y < 427; y = y+45,l++,counter++){
                        orientation = hexRandomOrientation[counter];
                        if(columnType == 1 ) y = y + r;
                        HexagonalTile tile = new HexagonalTile(x,y,orientation,canvas);
                        hexPixelToPid.put(new Point2D(x,y),new PositionId(k,l));
                        hexPidToTile.put(new PositionId(k,l),tile);
                        tile.plot();
                        columnType =0;
                    }
                    l=0;
                }
            }else {
                String result = SelectionBox.display("ERROR", "The total should be 105","OK", null);
            }
        });

        generateRandomly.setOnAction( e -> {
            System.out.println("call the maze generator method from here");
            int columnCounter = 1, columnType = 0;
            int orientation;
            int k = 0, l =0;
            for(double x = 13; x < 442; x = x+ 39,k++,columnCounter++){
                if(columnCounter % 2 == 0 ) columnType =  1;
                for(double y = 0; y < 427; y = y+45,l++){
                    orientation = getRandomOrientation(2,1);
                    if(columnType == 1 ) y = y + r;
                    HexagonalTile tile = new HexagonalTile(x,y,orientation,canvas);
                    hexPixelToPid.put(new Point2D(x,y),new PositionId(k,l));
                    hexPidToTile.put(new PositionId(k,l),tile);
                    tile.plot();
                    columnType =0;
                }
                l=0;
            }

        });

        clear.setOnAction(e -> {
            TextField text1 =(TextField) orientations1.getChildren().get(2);
            TextField text2 =(TextField) orientations2.getChildren().get(2);
            text1.clear();
            text2.clear();
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,442,450);
            hexPixelToPid.clear();
            hexPidToTile.clear();
            draw(canvas);
        });

        canvas.setOnMouseClicked(e -> {
            double xClick = e.getX();
            double yClick = e.getY();
            double count = e.getClickCount();
            double x=0,y=0;
            boolean isClickOnTile = false;
            HexagonalTile tile;
            Set<Point2D> positions = hexPixelToPid.keySet();
            for (Point2D point : positions) {
                HexagonalTile tile1 = hexPidToTile.get(hexPixelToPid.get(point));
                isClickOnTile = tile1.checkIfPointInHexagon(xClick,yClick);
                if(isClickOnTile){
                    x = tile1.getxPoints()[0];
                    y = tile1.getyPoints()[0];
                    PositionId changedPosition = hexPixelToPid.get(new Point2D(x,y));
                    for(int i = 1; i<= count; i++){
                        int orientation = i % 2;
                        if(orientation == 0) orientation = 2;
                        tile = new HexagonalTile(x,y,orientation,canvas);
                        tile.plot();
                        hexPidToTile.put(changedPosition,tile);
                    }
                    break;
                }

            }

        });

        maze.setOnAction(e ->{
            System.out.println("call the detect maze method");
            List<PositionId> path1 = MazeDetector.checkHexagonalMaze(hexTilesGraph, new PositionId(0,0), new PositionId(10,9), hexPidToTile);
            List<PositionId> path2 = MazeDetector.checkHexagonalMaze(hexTilesGraph, new PositionId(10,0), new PositionId(0,9), hexPidToTile);
            if(path1.size() == 0 && path2.size()==0){
                System.out.println("No Maze");
            } else if(path1.size() !=0){
                for(PositionId item : path1){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
               highlightHexMaze(canvas,path1,hexPidToTile);
            } else {
                for(PositionId item : path2){
                    System.out.println("x =" + item.x + ", y=" + item.y);
                }
               highlightHexMaze(canvas,path2,hexPidToTile);
            }
        });
        return hexagonalScene;
    }

    public void draw(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public HBox getHboxDisplays(ImageView imageView){
        HBox row = new HBox(10);
        Label label = new Label("Enter probability:");
        TextField textField = new TextField();
        row.getChildren().addAll(imageView,label, textField);
        return row;
    }

    public int getRandomOrientation(int high, int low){
        int result = r.nextInt((high-low)+1) + low;
        return result;
    }

    public boolean validate(double[] prob){
        double sum = 0;
        for(int i =0; i < prob.length;i++){
            sum = sum + prob[i];
        }
        if(sum != 1) return false;

        return true;
    }

    public void randomGeneration(double[] prob){
        int index = 0;
        for(int i =0; i< prob.length;i++){
            prob[i] = prob[i]*100;
        }
        for(int i =0; i < prob.length;i++){
            for(int j = 1; j <= prob[i];j++,index++){
                randomOrientation[index] = i+1;
            }
        }
        Collections.shuffle(Arrays.asList(randomOrientation));
    }

    public void hexRandomGeneration(double[] prob){
        int index = 0;
        double sum = 0;
        for(int i =0; i< prob.length;i++){
            prob[i] = prob[i]*100;
            sum = sum + prob[i];
        }
        if(sum > 105){
            int random = getRandomOrientation(1,0);
            double difference = sum - 105;
            prob[random] = prob[random] - difference;
        }else if(sum < 105){
            int random = getRandomOrientation(1,0);
            double difference = 105 - sum;
            prob[random] = prob[random] + difference;
        }
        for(int i =0; i < prob.length;i++){
            for(int j = 1; j <= prob[i];j++,index++){
                hexRandomOrientation[index] = i+1;
            }
        }
        Collections.shuffle(Arrays.asList(hexRandomOrientation));
    }

    private void initialiseDiagonalGraph(Graph diagonalTileGraph){
        //initialising the top row for graph
        for(int x = 0; x < 10; x++){
            diagonalTileGraph.addVertex(new PositionId(x,0));
        }
        for(int x = 0; x < 9; x++){
            diagonalTileGraph.addEdge(new PositionId(x,0), new PositionId(x+1,0)); //making edges on the top row
        }

        /*initialise the rest of the graph
            First if the node is the left row node add and connect only to the upper node.
            Then if the node is a normal node connect with the upper and the previous node.
         */
        for(int y = 1; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if(x == 0) {
                    diagonalTileGraph.addVertex(new PositionId(x, y));
                    diagonalTileGraph.addEdge(new PositionId(x, y), new PositionId(x, y - 1));
                }else{
                    diagonalTileGraph.addVertex(new PositionId(x,y)); //new position
                    diagonalTileGraph.addEdge(new PositionId(x,y), new PositionId(x-1,y)); //make edge with the earlier one
                    diagonalTileGraph.addEdge(new PositionId(x,y), new PositionId(x,y-1)); //make edge with the above one
                }
            }
        }
    }

    private void initialiseHexGraph(Graph hexTileGraph){
        int columnType = 1;

        for(int x = 0; x < 11; x++){
            hexTileGraph.addVertex(new PositionId(x,0));
        }
        for(int x = 0; x < 10; x++){
            hexTileGraph.addEdge(new PositionId(x,0), new PositionId(x+1,0)); //making edges on the top row
        }

        for(int y = 1; y < 10; y++){
            int columnCounter = 1;
            for(int x = 0; x <= 10; x++,columnCounter++){
                if(columnCounter % 2 == 0 ) columnType =  0;
                if(x == 0){
                    hexTileGraph.addVertex(new PositionId(x,y));
                    hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x, y-1));
                    hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x+1,y-1)); //right diagonal
                }else{
                    if(columnType == 0 ){
                        hexTileGraph.addVertex(new PositionId(x,y));
                        hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x-1,y)); //left diagonal
                        hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x,y-1)); //upper
                        hexTileGraph.addVertex(new PositionId(x+1,y));
                        hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x+1,y)); // right diagonal
                        columnType =1;
                    } else{
                        hexTileGraph.addVertex(new PositionId(x,y));
                        hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x,y-1)); //upper
                        hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x-1,y-1)); //left diagonal
                        if(x != 10)
                            hexTileGraph.addEdge(new PositionId(x,y), new PositionId(x+1,y-1)); //
                    }

                }
            }
        }
    }

    private void drawGrid(Canvas canvas, List<PositionId> path, Hashtable<PositionId, DiagonalTile> posIdToTile){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.YELLOW);
        for(PositionId item : path){
            int orientation = posIdToTile.get(item).getOrientation();
            double x = posIdToTile.get(item).getX();
            double y = posIdToTile.get(item).getY();
            switch (orientation) {
                case 1:
                    gc.fillPolygon(new double[]{x,x+45,x+45}, new double[]{y,y,y+45}, 3);
                    break;
                case 2:
                    gc.fillPolygon(new double[]{x+45, x+45,x}, new double[]{y,y+45,y+45}, 3);
                    break;
                case 3:
                    gc.fillPolygon(new double[]{x,x,x+45}, new double[]{y,y+45,y+45}, 3);
                    break;
                case 4:
                    gc.fillPolygon(new double[]{x,x+45,x}, new double[]{y,y,y+45}, 3);
                    break;
            }

        }
    }

    private void highlightCircleMaze(Canvas canvas, List<PositionId> path, Hashtable<PositionId, CircleTile> posIdToTile){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.YELLOW);
        for(PositionId item : path){
            int orientation = posIdToTile.get(item).getOrientation();
            double x = posIdToTile.get(item).getX();
            double y = posIdToTile.get(item).getY();
            switch (orientation){
                case 2:
                    gc.strokeLine(x,y,x+45,y+45);
                    break;
                case 4:
                    gc.strokeLine(x+45,y,x,y+45);
                    break;
            }
        }
    }

    private void highlightHexMaze(Canvas canvas, List<PositionId> path, Hashtable<PositionId, HexagonalTile> posIdToTile){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        for(int i =0; i < path.size()-1; i++){
            PositionId tilePosition = path.get(i);
            PositionId nextTilePosition = path.get(i+1);
            HexagonalTile tile1 = posIdToTile.get(tilePosition);
            HexagonalTile tile2 = posIdToTile.get(nextTilePosition);

            double[] tile1Center = tile1.getCenter();
            double[] tile2Center = tile2.getCenter();
            double[] tile1XPoints = tile1.getxPoints();
            double[] tile1YPoints = tile1.getxPoints();

//            if(i == 0){
//                gc.strokeLine(tile1XPoints[5],tile1YPoints[5],tile1Center[0],tile1Center[1]);
//            }
            gc.strokeLine(tile1Center[0],tile1Center[1],tile2Center[0],tile2Center[1]);
        }
        HexagonalTile lastTile = posIdToTile.get(path.get(path.size()-1));
        double[] lastTileXPoints = lastTile.getxPoints();
        double[] lastTileYPoints = lastTile.getyPoints();
        double[] lastTileCenter = lastTile.getCenter();
        gc.strokeLine(lastTileCenter[0],lastTileCenter[1],lastTileXPoints[3],lastTileYPoints[3]);
    }

    private void createDiagonalDominoes(Canvas canvas){
        Hashtable<PositionId, Integer> record = new Hashtable<>();
        record.put(new PositionId(0,0),1);
        for(int y =0,i=0; y<450; y = y+45,i++){
            for(int x =0,k=0; x < 450; x=x+45,k++){
                if(i ==0 && k ==0) {
                    DiagonalTile tile = new DiagonalTile(x, y, 1, canvas);
                    tile.plot();
                }else if(x == 0) {
                    if(record.get(new PositionId(k,i-1)) == 1) {
                        DiagonalTile tile = new DiagonalTile(x,y,3,canvas);
                        tile.plot();
                        record.put(new PositionId(k,i),3);
                    }else {
                        DiagonalTile tile = new DiagonalTile(x,y,1,canvas);
                        tile.plot();
                        record.put(new PositionId(k,i),1);
                    }
                }else {
                    if(record.get(new PositionId(k-1,i)) == 1){
                        DiagonalTile tile = new DiagonalTile(x,y,3,canvas);
                        tile.plot();
                        record.put(new PositionId(k,i),3);
                    }else {
                        DiagonalTile tile = new DiagonalTile(x,y,1,canvas);
                        tile.plot();
                        record.put(new PositionId(k,i),1);
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
