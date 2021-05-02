package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EditController {

    @FXML private ImageView app_icon_imageV;
    @FXML private TextField appName_tf;
    @FXML private Button btn_chooseIcon;
    @FXML private Label appIcon_label;
    @FXML private ComboBox<String> category_cb;
    @FXML private Label category_label;
    @FXML private TextField rating_tf;
    @FXML private Label rating_label;
    @FXML private TextField reviews_tf;
    @FXML private Label reviews_label;
    @FXML private TextField size_tf;
    @FXML private Label size_label;
    @FXML private TextField installs_tf;
    @FXML private Label installs_label;
    @FXML private ComboBox<String> type_cb;
    @FXML private Label type_label;
    @FXML private ComboBox<String> contentRating_cb;
    @FXML private Label contentRating_label;
    @FXML
    private ComboBox<String> genres_cb;
    @FXML private Label genres_label;
//    @FXML private TextField lastUpdated_tf;
    @FXML private Label lastUpdated_label;
    @FXML private TextField currVersion_tf;
    @FXML private Label currVersion_label;
    @FXML private TextField androidVersion_tf;
    @FXML private Label androidVersion_label;
    @FXML private Button btn_cancel;
    @FXML private Button btn_apply;
    @FXML private TextField price_tf;

    private App app;
    private Controller controller;
    private Image appIcon;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setApp(App app) {
        this.app = app;

        //set fields
        app_icon_imageV.setImage(new Image(app.getImgSrc()));

        appName_tf.setText(app.getName());
        ObservableList<String> categories = FXCollections.observableArrayList("LIFESTYLE", "GAME" ,"ART_AND_DESIGN",
                "BOOKS_AND_REFERENCE", "BUSINESS", "ENTERTAINMENT", "SOCIAL", "PERSONALIZATION", "PRODUCTIVITY", "VIDEO_PLAYERS", "FAMILY");
        category_cb.setItems(categories);
        category_cb.getSelectionModel().select(app.getCategory());
        rating_tf.setText(String.valueOf(app.getRating()));
        reviews_tf.setText(String.valueOf(app.getReviews()));
        size_tf.setText(app.getSize());
        installs_tf.setText(app.getInstalls());
        price_tf.setText(app.getPrice());

        type_cb.setItems(FXCollections.observableArrayList("Free", "Paid"));
        type_cb.getSelectionModel().select(app.getType());

        contentRating_cb.setItems(FXCollections.observableArrayList("Everyone", "Teen","Mature 17+", "Everyone 10+"));
        contentRating_cb.getSelectionModel().select(app.getContentRating());

        genres_cb.setItems(FXCollections.observableArrayList(
                "Racing", "Entertainment", "Word", "Books & Reference", "Action & Adventure", "Business", "Personalization",
                "Productivity", "Board", "Arcade", "Card", "Art & Design", "Adventure", "Action", "Role Playing", "Puzzle",
                "Casual", "Tools", "Sports", "Strategy", "Simulation", "Lifestyle", "Music", "Trivia"));
        genres_cb.getSelectionModel().select(app.getGenre());

//        lastUpdated_tf.setText(app.getLastUpdated());
        currVersion_tf.setText(app.getCurrentVer());
        androidVersion_tf.setText(app.getAndroidVer());
    }

    @FXML
    void applyChanges(ActionEvent event) throws Exception{
        String query = "update apps SET APP_NAME = ?, CATEGORY = ?, RATING = ?, REVIEWS = ?, APP_SIZE = ?, INSTALLS = ?, APP_TYPE = ?, PRICE = ?,\n" +
                "CONTENT_RATING = ?, GENRE = ?, CURRENT_VER = ?, ANDROID_VER = ? WHERE APP_NAME = ?";
        Connection con = Controller.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, appName_tf.getText());
            stmt.setString(2, category_cb.getValue());
            stmt.setDouble(3, Double.parseDouble(rating_tf.getText()));
            stmt.setInt(4,(int) Double.parseDouble(reviews_tf.getText()));
            stmt.setString(5, size_tf.getText());
            stmt.setString(6, installs_tf.getText());
            stmt.setString(7, type_cb.getValue());
            stmt.setString(8, price_tf.getText());
            stmt.setString(9, contentRating_cb.getValue());
            stmt.setString(10, genres_cb.getValue());
//            stmt.setString(11, lastUpdated_tf.getText());
            stmt.setString(11, currVersion_tf.getText());
            stmt.setString(12, androidVersion_tf.getText());

            stmt.setString(13, app.getName());
            stmt.executeQuery();

            changeAppValues();
            controller.setData(app);
            controller.drawApps(controller.DBConnection());

            Stage thisStage = (Stage) app_icon_imageV.getScene().getWindow();
            thisStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage thisStage = (Stage) app_icon_imageV.getScene().getWindow();
        thisStage.close();
    }

    public void changeAppValues() {
        Connection con = Controller.getConnection();
        String query = "SELECT LAST_UPDATED FROM APPS WHERE APP_NAME = '" + appName_tf.getText() + "'";
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            rs.getString(1);
            app.setLastUpdated(rs.getString(1));
        } catch (Exception e){}

        app.setName(appName_tf.getText());
        app.setCategory(category_cb.getValue());
        app.setSize(size_tf.getText());
        app.setInstalls(installs_tf.getText());

//        app.setLastUpdated(app.getLastUpdated());
        app.setCurrentVer(currVersion_tf.getText());
        app.setAndroidVer(androidVersion_tf.getText());
        app.setGenre(genres_cb.getValue());
        app.setType(type_cb.getValue());
        app.setContentRating(contentRating_cb.getValue());
        app.setRating(Double.parseDouble(rating_tf.getText()));
        app.setReviews(Double.parseDouble(reviews_tf.getText()));
    }
}
