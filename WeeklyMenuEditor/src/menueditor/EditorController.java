package menueditor;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static menueditor.Controller.bgImage;
import static menueditor.Controller.canvasReal;

public class EditorController implements Initializable {

    @FXML
    private Canvas canvasEdit;
    @FXML
    private Slider sliderScale;
    @FXML
    private Label labelScale;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnCancel;

    private GraphicsContext gcEdit;

    private double scaleOfCanvasReal;

    private double offsetX;

    private double offsetY;

    private double offsetXSum;

    private double offsetYSum;

    private double startX;

    private double startY;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEditorGraphicsContext();
        canvasEdit.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                offsetXSum += offsetX;
                offsetYSum += offsetY;
                offsetX = 0;
                offsetY = 0;
                startX = event.getScreenX();
                startY = event.getScreenY();

            }
        });
        canvasEdit.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                offsetX = event.getScreenX()-startX;
                offsetY = event.getScreenY()-startY;
                redrawCanvas();
            }
        });
    }

    private void initializeEditorGraphicsContext(){
        gcEdit = canvasEdit.getGraphicsContext2D();
        gcEdit.setStroke(Color.rgb(0,120,255,0.8));
        gcEdit.setFill(Color.rgb(0,120,255,0.3));
        gcEdit.setLineWidth(2.0);
        double padding = 40;
        scaleOfCanvasReal = (canvasEdit.getHeight()-2*padding)/canvasReal.getHeight();
        redrawCanvas();

    }

    private void redrawCanvas(){
        gcEdit.clearRect(0,0,canvasEdit.getWidth(),canvasEdit.getHeight());
        double x = canvasEdit.getWidth()/2-canvasReal.getWidth()/2*scaleOfCanvasReal;
        double y = canvasEdit.getHeight()/2-canvasReal.getHeight()/2*scaleOfCanvasReal;
        double w = canvasReal.getWidth()*scaleOfCanvasReal;
        double h = canvasReal.getHeight()*scaleOfCanvasReal;
        gcEdit.drawImage(bgImage,canvasEdit.getWidth()/2-bgImage.getWidth()/2*scaleOfCanvasReal*sliderScale.getValue()+offsetXSum+offsetX,canvasEdit.getHeight()/2-bgImage.getHeight()/2*scaleOfCanvasReal*sliderScale.getValue()+offsetYSum+offsetY,bgImage.getWidth()*scaleOfCanvasReal*sliderScale.getValue(),bgImage.getHeight()*scaleOfCanvasReal*sliderScale.getValue());
        gcEdit.strokeRect(canvasEdit.getWidth()/2-canvasReal.getWidth()/2*scaleOfCanvasReal,canvasEdit.getHeight()/2-canvasReal.getHeight()/2*scaleOfCanvasReal,canvasReal.getWidth()*scaleOfCanvasReal,canvasReal.getHeight()*scaleOfCanvasReal);

        gcEdit.fillRect(0,0,canvasEdit.getWidth(),y);
        gcEdit.fillRect(0,y,x,h);
        gcEdit.fillRect(x+w,y,x,h);
        gcEdit.fillRect(0,y+h,canvasEdit.getWidth(),y);

    }
    @FXML
    private void onSliderScale(){
        int percent = (int)Math.floor(sliderScale.getValue()*100);
        labelScale.setText(percent+"%");
        redrawCanvas();

    }
    @FXML
    private void onBtnOk(){
        double inverseScale = canvasReal.getHeight()/(canvasEdit.getHeight()-2*40);
        System.out.println("CanvasReal height: "+canvasReal.getHeight());
        System.out.println("CanvasEdit height: "+(canvasEdit.getHeight()-2*40));
        System.out.println("minimizing scale: "+scaleOfCanvasReal);
        System.out.println("inverse scale: "+inverseScale);
        System.out.println("Offsets:\n before X: "+offsetXSum+" after X: "+offsetXSum*inverseScale+"\nbefore Y: "+offsetYSum+" after Y: "+offsetYSum*inverseScale);
        Controller.setImgOffsetX((offsetXSum+offsetX)*inverseScale);
        Controller.setImgOffsetY((offsetYSum+offsetY)*inverseScale);
        Controller.setImgScale(sliderScale.getValue());
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();

    }
    @FXML
    private void onBtnReset(){
        offsetXSum = 0;
        offsetYSum = 0;
        offsetX = 0;
        offsetY = 0;
        sliderScale.setValue(1.0);
        labelScale.setText("100%");
        redrawCanvas();

    }
    @FXML
    private void onBtnCancel(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
