package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class HowItWorksWindow {

    public static void display() {
        final String data = "There are 3 types of Tiling’s available: Square with Diagonal, Square with Circle and Hexagon.\n" +
                "Click on the “START” button to navigate to the desired tile page.\n" +
                "There are 4 common features in all the tiling’s. 1: Enter probability of each variant and generate tiling. 2: Generate tiling randomly. 3: Detect mazes in the tiling. 4: Clicking on the tile to change the tile variant.\n" +
                "The sum of probability entered should be 1 for all the tile variants.\n" +
                "The HOME button will take you back to the main screen.\n" +
                "The CLEAR button will reset the entire tiling.\n" +
                "Square with Diagonal Tile has an added feature of viewing a dominoes style arrangement. Click on the “Generate Dominoes” button to learn more.\n" +
                "Hexagon tile has 2 variants with same features as the square tiles. \n" +
                "Once the tiles are generated click on the tiles individually to view the changing in orientation and create patterns with them. Create paths and mazes in the tiling.\n" +
                "Mazes are only enabled from the top left tile to bottom right tile and from top right tile to the bottom left tile. \n" +
                "Get ready to play with the tiles and have fun!\n";

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("How IT Works");

        Label label = new Label(data);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");
        StackPane layout = new StackPane();
        layout.getChildren().add(label);

        window.setScene(new Scene(layout, 900, 700));
        window.showAndWait();
    }
}
