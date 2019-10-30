package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LearnWindow {

    public static void display() {
        Stage window = new Stage();

        final String data = "Chladni and Truchet And Mazes \n" +
                "Ernst Chladni -  Hungarian Physicist, Musician\n" +
                "Resonance Frequencies observed by vibrating  a metal plate by tones  What happens - sand moves from points of high vibration (at center)  to points of none creating patterns Can be seen by using a skin of drum as well\n"+
                "“Why study such an obscure mathematical object? Well: they have interesting and subtle statistical properties, not to mention that the sample waves look pretty damn ﬁne. But if that isn’t enough for you, the nodal patterns of boundary-adapted arithmetic random waves are of interest to a much wider group than mathematicians. Quantum physicists like them too: they capture properties of the wave functions of a ‘quantum billiard ball trapped in a box’ with a ﬁxed energy corresponding to the frequency of the wave. And the community of interest is even wider than the combined esoteric worlds of quantum and mathematics. Earthquake scientists; makers of ﬁne musical instruments; all are interested in the properties of nodal lines—the sets which remain stationary when a domain is vibrating at resonant frequencies.” \n" +
                "“Truchet tiles. What are they? Well, start with a square, coloured either black or white. Pick two diagonally- opposite corners and shade them with a quarter-circle of the other colour, as shown. Make several. Then place them however appeals to you! The way they’re set up, it’s practically impossible not to start making patterns: blobs and whorls that seem almost alive.”\n" +
                "“And you can just as easily start asking yourself questions like, what kinds of tiles can you get if you remove the restriction of squareness?”\n" +
                "You will see and work with different variants of the Truchet Tiles in this program. \n"+
                "Square tile with diagonal is one of the examples. Check HOW IT WORKS to know how to navigate through the program\n"+
                "Information contributed by Dr. Hedetniemi";

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("LEARN MORE");
       // window.setMaxWidth(800);
      //  window.setMaxHeight(800);

        Label label = new Label(data);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");
        StackPane layout = new StackPane();
        layout.getChildren().add(label);

        window.setScene(new Scene(layout,900,700));
        window.showAndWait();



    }
}
