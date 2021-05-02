package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.input.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NewAppController implements Initializable {
    @FXML
    private ImageView logo;

    @FXML
    private Button loadButton;

    @FXML
    private TextField AppName;

    @FXML
    private ComboBox ctg;

    @FXML
    private TextField Price;

    @FXML
    private TextField Size;

    @FXML
    private TextField AndroidVersion;

    @FXML
    private TextField CurrentVersion;

    @FXML
    private TextField Genre;

    @FXML
    private TextField ContentRating;

    @FXML
    private Button AddButton;

    private String imgSrc;

    @FXML
    void addApp(ActionEvent event) {
        boolean isDriverLoaded = false;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver Loaded");
            isDriverLoaded = true;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String user = "hr";
        String password = "hr";
        Connection con = null;
        try{
            if (isDriverLoaded) {
                con = DriverManager.getConnection(url, user, password);
                System.out.println("Connection established");
            }
            Statement stmt = con.createStatement();

            String appName = AppName.getText();
            String cat = String.valueOf(ctg.getSelectionModel().getSelectedItem());
            String price = Price.getText();
            String size = Size.getText();
            String andVer = AndroidVersion.getText();
            String currVer = CurrentVersion.getText();
            String genre = Genre.getText();
            String cont = ContentRating.getText();
            String type = "";
            if(price.equals("0"))  type += "Free";
            else type = "Paid";

            String sql = "Insert into apps (app_name, category, rating, reviews, app_size, installs, app_type, price, content_rating, genre, last_updated, current_ver, android_ver, img)\n" +
                    "values ('"+appName+"', '"+cat+"', "+0+", "+0+", '"+size+"', '"+0+"' , '"+type+"', '"+price+"', '"+cont+"', '"+genre+"', Sysdate, '"+currVer+"', '"+andVer+"', '"+imgSrc+"')";
            stmt.executeUpdate(sql);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText("SUCCESS");
            alert.setContentText("App inserted successfully!");
            alert.show();

            AppName.setText("");
            Price.setText("");
            Size.setText("");
            AndroidVersion.setText("");
            CurrentVersion.setText("");
            Genre.setText("");
            ContentRating.setText("");

            Stage stage = (Stage) AddButton.getScene().getWindow();
            // do what you have to do
            stage.close();

            con.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failure");
            alert.setHeaderText("Look");
            alert.setContentText(e.toString());
            alert.show();
        }
    }

    @FXML
    void loadLogo(ActionEvent event) throws Exception{

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            imgSrc = selectedFile.getAbsolutePath();
            System.out.println(imgSrc);
            imgSrc = imgSrc.replace('\\', '/');
            imgSrc = "file:///" + imgSrc;
            Image img = new Image(imgSrc);
            Rectangle clip = new Rectangle(logo.getFitWidth(), logo.getFitHeight());
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            logo.setClip(clip);
            logo.setImage(img);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ObservableList<String> list1 = FXCollections.observableArrayList("ART_AND_DESIGN", "GAME" ,"BOOKS_AND_REFERENCE", "BUSINESS", "SOCIAL", "ENTERTAINMENT", "LIFESTYLE", "PRODUCTIVITY");
        ctg.setItems(list1);
    }

}
