package menueditor;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import javafx.scene.text.*;
import java.util.ResourceBundle;


public class Controller implements Initializable{
    @FXML
    private Button btnChooseBg;
    @FXML
    private Button btnEditBg;

    @FXML
    private ColorPicker colorPickerTheme;
    @FXML
    private ColorPicker colorPickerFont;

    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;

    @FXML
    private ImageView logo;

    @FXML
    private TextField timeStart;
    @FXML
    private TextField timeEnd;
    @FXML
    private TextField price;

    @FXML
    private AnchorPane anchorCanvas;
    @FXML
    private Canvas canvasPreview;



    @FXML
    private Tab tabPon;
    @FXML
    private Tab tabWt;
    @FXML
    private Tab tabSr;
    @FXML
    private Tab tabCzw;
    @FXML
    private Tab tabPt;
    @FXML
    private Tab tabSob;
    @FXML
    private Tab tabNie;

    private Tab[] tabs=new Tab[7];
    @FXML
    private TextArea txtarPon;
    @FXML
    private TextArea txtarWt;
    @FXML
    private TextArea txtarSr;
    @FXML
    private TextArea txtarCzw;
    @FXML
    private TextArea txtarPt;
    @FXML
    private TextArea txtarSob;
    @FXML
    private TextArea txtarNie;

    private TextArea[] txtareas = new TextArea[7];

    @FXML
    private  Button btnSave;

    @FXML
    private Label info;

    private GraphicsContext gc;

    public static Canvas canvasReal = new Canvas(1241,1754);

    private GraphicsContext gcReal = canvasReal.getGraphicsContext2D();

    private static double imgScale = 1.0;

    private static double imgOffsetX;

    private static double imgOffsetY;

    public static Image bgImage;

    private Font HeaderFont;
    private Font NormalFont;
    private Font BigFont;
    private Font SmallFont;

    public static void setImgScale(double imgScale) {
        Controller.imgScale = imgScale;
    }

    public static void setImgOffsetX(double imgOffsetX) {
        Controller.imgOffsetX = imgOffsetX;
    }

    public static void setImgOffsetY(double imgOffsetY) {
        Controller.imgOffsetY = imgOffsetY;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFonts();

        anchorCanvas.setStyle("-fx-background-color: #ddd");
        colorPickerTheme.setValue(Color.valueOf("bbc89a"));
        colorPickerFont.setValue(Color.valueOf("4d4d4d"));

        loadLogo();




        initializeGraphicsContext();


        initializeTabsArray();
        initializeTxtAreaArray();
    }

    private void loadFonts(){
//        try {
        HeaderFont = Font.loadFont(loadResource("/resources/HeaderFont.otf"), /*57*/ 0.059375f*canvasReal.getHeight());
        NormalFont = Font.loadFont(loadResource("/resources/NormalFont.otf"), /*18*/ 0.01875f*canvasReal.getHeight());
        BigFont = Font.loadFont(loadResource("/resources/BigFont.otf"), /*20*/ 0.020833f*canvasReal.getHeight());
        SmallFont = Font.loadFont(loadResource("/resources/NormalFont.otf"), /*8*/ 0.008333333f*canvasReal.getHeight());

//            HeaderFont = Font.loadFont(new FileInputStream(new File("resources/HeaderFont.otf")), 57);
//            NormalFont = Font.loadFont(new FileInputStream(new File("resources/NormalFont.otf")), 18);
//            BigFont = Font.loadFont(new FileInputStream(new File("resources/BigFont.otf")), 20);
//            SmallFont = Font.loadFont(new FileInputStream(new File("resources/NormalFont.otf")), 8);




//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    private InputStream loadResource(String path){

        URL res = getClass().getResource(path);
        if(res!=null){
            if (res.getProtocol().equals("jar")) {
                return getClass().getResourceAsStream(path);
            }
        }

        return null;
    }
    private void loadLogo(){
        URL res = getClass().getResource("/resources/logo.jpg");
        if(res!=null){
            if (res.getProtocol().equals("jar")) {
                Image logoImage = new Image(loadResource("/resources/logo.jpg"),80,80,true,true);
                if(logoImage!=null){
                    logo.setImage(logoImage);
                }
            }
        }

    }

    private void initializeGraphicsContext(){
        gc = canvasPreview.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(
                "Podgląd",
                Math.round(canvasPreview.getWidth()  / 2),
                Math.round(canvasPreview.getHeight() / 2)
        );
    }



    @FXML
    private void onBtnEditBg(){

        FXMLLoader fxmlLoader = null;
        Stage stage = new Stage();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("layoutEditBG.fxml"));
            Parent root1 =  fxmlLoader.load();
            stage.setTitle("Zmiana pozycji tła");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void onPreviewClicked(){
        drawOnRealCanvas();
        FXMLLoader fxmlLoader = null;
        Stage stage = new Stage();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("layoutPreview.fxml"));
            Parent root1 =  fxmlLoader.load();
            stage.setTitle("Podgląd");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTabsArray(){
        tabs[0]=tabPon;
        tabs[1]=tabWt;
        tabs[2]=tabSr;
        tabs[3]=tabCzw;
        tabs[4]=tabPt;
        tabs[5]=tabSob;
        tabs[6]=tabNie;
    }
    private void initializeTxtAreaArray(){
        txtareas[0]=txtarPon;
        txtareas[1]=txtarWt;
        txtareas[2]=txtarSr;
        txtareas[3]=txtarCzw;
        txtareas[4]=txtarPt;
        txtareas[5]=txtarSob;
        txtareas[6]=txtarNie;
    }

    @FXML
    private void onPriceChange(){
        if(!info.getText().trim().isEmpty()){
            info.setText("");
        }
        drawPreview();
    }
    @FXML
    private void onColorPicked(){
        drawPreview();
    }

    @FXML
    private void onTimeChanged(){
        drawPreview();
    }
    @FXML
    private void onTextChanged(){
        drawPreview();
    }

    @FXML
    private synchronized void onBtnChooseBg(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Obrazy", "*.jpg","*.jpeg", "*.png"));
        chooser.setTitle("Wybierz obraz tła");
        File file = chooser.showOpenDialog(btnChooseBg.getScene().getWindow());
        if (file != null) {
            bgImage = new Image(file.toURI().toString());


            drawPreview();


            btnEditBg.setDisable(false);


        }
    }




    private void drawPreview(){
        drawOnRealCanvas();
        double scale = canvasPreview.getHeight()/canvasReal.getHeight();

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = canvasReal.snapshot(params, null);
        canvasPreview.getGraphicsContext2D().setFill(Color.WHITE);
        canvasPreview.getGraphicsContext2D().fillRect(0,0,canvasPreview.getWidth(),canvasPreview.getHeight());
        double tmpX = canvasPreview.getWidth()/2-image.getWidth()*scale/2;
        canvasPreview.getGraphicsContext2D().drawImage(image, tmpX, 0,image.getWidth()*scale,image.getHeight()*scale);
    }

    @FXML
    private void onDatePick(){
        if(dateStart.getValue()!=null) {
            if (dateEnd.isDisabled()||dateEnd.getValue()==null) {
                dateEnd.setDisable(false);
                LocalDate tmp = dateStart.getValue();
                dateEnd.setValue(tmp.plusDays(7 - tmp.getDayOfWeek().getValue()));
            }
            LocalDate lStart = dateStart.getValue();
            LocalDate lEnd = dateEnd.getValue();
            if (lStart.isAfter(lEnd)) {
                dateEnd.setValue(lStart);
                lEnd = dateEnd.getValue();
            }

            disableDayTabs();

            int dayOfStart = lStart.getDayOfWeek().getValue();
            int dayOfEnd = lEnd.getDayOfWeek().getValue();

            for (int i = dayOfStart; i <= dayOfEnd; ++i) {
                tabs[i - 1].setDisable(false);
            }

            if (info.getText() != null) {
                info.setText("");
            }
            drawPreview();
        }

    }
    private String extractHours(TextField tf){
        if(tf.getText().trim().length()==5){
            return tf.getText(0,2);
        }
        if(tf.getText().trim().length()==4){
            return tf.getText(0,1);
        }
        if(tf.getText().trim().length()==1){
            return tf.getText(0,1);
        }
        if(tf.getText().trim().length()==2){
            return tf.getText(0,2);
        }
        info.setText("Nie podano czasu");
        return "ERROR";

    }

    private  String extractMinutes(TextField tf){
        if(tf.getText().trim().length()==5){
            return tf.getText(3,5);
        }
        if(tf.getText().trim().length()==4){
            return tf.getText(2,4);
        }
        if(tf.getText().trim().length()==1){
            return "00";
        }
        if(tf.getText().trim().length()==2){
            return "00";
        }
        info.setText("Nie podano czasu");
        return "ERROR";
    }

    private void disableDayTabs(){
        for(Tab i:tabs){
            i.setDisable(true);
        }
    }

    private void drawOnRealCanvas(){

        gcReal.clearRect(0, 0, canvasReal.getWidth(), canvasReal.getHeight());
        if(bgImage!=null){
            double imgX = canvasReal.getWidth()/2-bgImage.getWidth()/2*imgScale+imgOffsetX;
            double imgY = canvasReal.getHeight()/2-bgImage.getHeight()/2*imgScale+imgOffsetY;
            double imgW = bgImage.getWidth()*imgScale;
            double imgH = bgImage.getHeight()*imgScale;
            gcReal.drawImage(bgImage,imgX,imgY,imgW,imgH);
        }

        Font defaultFont = gcReal.getFont();
        gcReal.setFont(HeaderFont);
        gcReal.setFill(colorPickerTheme.getValue());
        gcReal.fillRect(0.4044f*canvasReal.getWidth(), 0.03125f*canvasReal.getHeight(), canvasReal.getWidth()-(0.4044f*canvasReal.getWidth()), 0.135416f*canvasReal.getHeight());
        gcReal.setStroke(colorPickerFont.getValue());
        gcReal.setLineWidth(0.0026f*canvasReal.getHeight());
        gcReal.moveTo(-1, 0.1078125*canvasReal.getHeight());
        gcReal.lineTo(canvasReal.getWidth(), 0.1078125*canvasReal.getHeight());
        gcReal.stroke();
        gcReal.setFill(colorPickerFont.getValue());
        gcReal.fillText("Tygodniowe menu", 0.42352f*canvasReal.getWidth(), 0.090625f*canvasReal.getHeight());
        gcReal.setFont(defaultFont);


        gcReal.setLineWidth(0.00104166f*canvasReal.getHeight());
        double linesStart = 0.203125f*canvasReal.getHeight();
        double linesSpacing = 0.1177083f*canvasReal.getHeight();

        for (int i = 0; i < tabs.length; ++i) {
            if (!tabs[i].isDisabled()) {
                gcReal.moveTo(-1, linesStart);
                gcReal.lineTo(0.4779411f*canvasReal.getWidth(), linesStart);
                gcReal.stroke();
                linesStart += linesSpacing;
            }

        }



        gcReal.setFont(NormalFont);
        gcReal.fillText("*dostępne w godzinach: " + extractHours(timeStart), 0.43676f*canvasReal.getWidth(), 0.15833f*canvasReal.getHeight());
        Text widthMeasure = new Text("*dostępne w godzinach: " + extractHours(timeStart));
        widthMeasure.setFont(NormalFont);
        double textWidth = widthMeasure.getBoundsInLocal().getWidth()+1;



        gcReal.setFont(SmallFont);
        widthMeasure.setFont(SmallFont);
        gcReal.fillText(extractMinutes(timeStart), 0.43676f*canvasReal.getWidth() + textWidth, 0.14895f*canvasReal.getHeight());
        widthMeasure.setText(extractMinutes(timeStart));
        textWidth += widthMeasure.getBoundsInLocal().getWidth()+1;
        gcReal.setFont(NormalFont);
        widthMeasure.setFont(NormalFont);
        gcReal.fillText("-" + extractHours(timeEnd), 0.43676f*canvasReal.getWidth() + textWidth, 0.15833f*canvasReal.getHeight());
        widthMeasure.setText("-" + extractHours(timeEnd));
        textWidth += widthMeasure.getBoundsInLocal().getWidth()+1;
        gcReal.setFont(SmallFont);
        gcReal.fillText(extractMinutes(timeEnd), 0.43676f*canvasReal.getWidth() + textWidth, 0.14895f*canvasReal.getHeight());

        gcReal.setFont(BigFont);

        if(!price.getText().trim().isEmpty()){
            gcReal.fillText("cena " + price.getText() + " zł", 0.77941f*canvasReal.getWidth(), 0.13541f*canvasReal.getHeight());
        }else{
            info.setText("Nie podano ceny");
        }



        if(dateStart.getValue()!=null) {
            String weekDate = "";

            if (dateStart.getValue().getDayOfMonth() < 10) {
                weekDate += "0" + dateStart.getValue().getDayOfMonth();
            } else {
                weekDate += dateStart.getValue().getDayOfMonth();
            }


            if (dateStart.getValue().getYear() == dateEnd.getValue().getYear() && dateStart.getValue().getMonthValue() != dateEnd.getValue().getMonthValue()) {
                if (dateStart.getValue().getMonthValue() < 10) {
                    weekDate += ".0" + dateStart.getValue().getMonthValue();
                } else {
                    weekDate += "." + dateStart.getValue().getMonthValue();
                }
            }
            if (dateStart.getValue().getYear() != dateEnd.getValue().getYear()) {
                if (dateStart.getValue().getMonthValue() < 10) {
                    weekDate += ".0" + dateStart.getValue().getMonthValue() + "." + dateStart.getValue().getYear();
                } else {
                    weekDate += "." + dateStart.getValue().getMonthValue() + "." + dateStart.getValue().getYear();
                }
            }

            if (dateEnd.getValue().getDayOfMonth() < 10) {
                weekDate += "-0" + dateEnd.getValue().getDayOfMonth();
            } else {
                weekDate += "-" + dateEnd.getValue().getDayOfMonth();
            }
            if (dateEnd.getValue().getMonthValue() < 10) {
                weekDate += ".0" + dateEnd.getValue().getMonthValue();
            } else {
                weekDate += "." + dateEnd.getValue().getMonthValue();
            }
            weekDate += "." + dateEnd.getValue().getYear();

            gcReal.fillText(weekDate, 0.43676f*canvasReal.getWidth(),0.13541f*canvasReal.getHeight());





            gcReal.setFont(NormalFont);
            double daysSpacing = 0;
            LocalDate tmp = dateStart.getValue();
            for (int i = 0; i < tabs.length; ++i) {
                if (!tabs[i].isDisabled()) {
                    String day = "";
                    switch (i) {
                        case 0:
                            day += "Poniedziałek ";
                            break;
                        case 1:
                            day += "Wtorek ";
                            break;
                        case 2:
                            day += "Środa ";
                            break;
                        case 3:
                            day += "Czwartek ";
                            break;
                        case 4:
                            day += "Piątek ";
                            break;
                        case 5:
                            day += "Sobota ";
                            break;
                        case 6:
                            day += "Niedziela ";
                            break;

                    }
                    if (tmp.getDayOfMonth() < 10) {
                        day += "0" + tmp.getDayOfMonth();
                    } else {
                        day += tmp.getDayOfMonth();
                    }
                    if (tmp.getMonthValue() < 10) {
                        day += ".0" + tmp.getMonthValue();
                    } else {
                        day += "." + tmp.getMonthValue();
                    }
                    day += "." + tmp.getYear();

                    gcReal.fillText(day, 0.2058823f*canvasReal.getWidth(), 0.2f *canvasReal.getHeight() + daysSpacing);

                    gcReal.fillText(txtareas[i].getText(), 0.2058823f*canvasReal.getWidth(), 0.2239583f*canvasReal.getHeight() + daysSpacing);

                    tmp = tmp.plusDays(1);
                    daysSpacing += 0.1177083f*canvasReal.getHeight();
                }
            }



        }
    }

    @FXML
    private void onBtnSave()
    {
        if(dateStart.getValue()!=null) {

            drawOnRealCanvas();


            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(btnSave.getScene().getWindow());

            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(((int) canvasReal.getWidth()), ((int) canvasReal.getHeight()));
                    canvasReal.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("ERROR");
                }
            }
        } else{
            info.setText("Nie wybrano daty");
        }


    }



}
