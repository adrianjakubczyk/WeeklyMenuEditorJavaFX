package menueditor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

import static menueditor.Controller.canvasReal;

public class PreviewController implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane anchorPane;

    public void initialize(URL location, ResourceBundle resources) {

        double maxHeight = Screen.getPrimary().getBounds().getMaxY()-200;
        double scale = 1.0;
        System.out.println(maxHeight);
        if(maxHeight<canvasReal.getHeight()){
            scale = maxHeight/canvasReal.getHeight();
            anchorPane.setPrefWidth(canvasReal.getWidth()*scale);
            anchorPane.setPrefHeight(canvasReal.getHeight()*scale);
            canvas.setWidth(canvasReal.getWidth()*scale);
            canvas.setHeight(canvasReal.getHeight()*scale);
        } else{
            anchorPane.setPrefWidth(canvasReal.getWidth());
            anchorPane.setPrefHeight(canvasReal.getHeight());
            canvas.setWidth(canvasReal.getWidth());
            canvas.setHeight(canvasReal.getHeight());
        }


        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = canvasReal.snapshot(params, null);


        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(image, 0, 0,image.getWidth()*scale,image.getHeight()*scale);


    }

}
