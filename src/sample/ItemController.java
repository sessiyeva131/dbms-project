package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.File;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class ItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private ImageView img;

    @FXML
    public void click(MouseEvent mouseEvent){
        myListener.onClickListener(app);
    }

    private App app;
    private MyListener myListener;

    public void setData(App app, MyListener myListener) throws Exception{
        this.app = app;
        this.myListener = myListener;
        nameLabel.setText(app.getName());
        ratingLabel.setText(String.valueOf(app.getRating()));
        String src = app.getImgSrc();
//        System.out.println(src);
        Image image = new Image(src);

//        System.out.println(src);
        img.setFitWidth(70);
        img.setFitHeight(70);
        Rectangle clip = new Rectangle(img.getFitWidth(), img.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        img.setClip(clip);
        img.setImage(image);
//        gegometry_dash.jpeg
    }
}
