package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Controller implements Initializable {
    @FXML
    private VBox chosenCard;

    @FXML
    private Label AppName;

    @FXML
    private Label AppRating;

    @FXML
    private ImageView AppImage;

    @FXML
    private Label AppInstalls;

    @FXML
    private Label AppReviews;

    @FXML
    private Label AppSize;

    @FXML
    private Label AppCategory;

    @FXML
    private Label AppType;

    @FXML
    private Label AppContentRating;

    @FXML
    private Label AppLastUpdated;

    @FXML
    private Label AppCurrVersion;

    @FXML
    private Label AppAndroid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private TextField searchfield;

    @FXML
    private ComboBox ctg;

    @FXML
    private ComboBox fee;

    @FXML
    private CheckBox check;

    @FXML
    private ComboBox rtg;

    @FXML
    private Button newApp;

    @FXML
    private HBox suggestedHBOX;

    @FXML
    private Label topChart;

    @FXML
    private ComboBox top5;

    private List<App> apps = new ArrayList<>();
    private List<App> filteredApps = new ArrayList<>();
    private Image image;
    private MyListener myListener;
    private Connection con;
    private App selectedApp;

    public void setData(App app){
        selectedApp = app;
        suggestedHBOX.getChildren().clear();

        AppName.setText(app.getName());
        AppRating.setText(String.valueOf(app.getRating()));
        AppInstalls.setText(app.getInstalls());
        AppReviews.setText(String.valueOf(app.getReviews()));
        AppSize.setText(app.getSize());
        AppContentRating.setText(app.getContentRating());
        AppCategory.setText(app.getCategory());
        AppType.setText(app.getPrice());
        AppLastUpdated.setText(app.getLastUpdated());
        AppCurrVersion.setText(app.getCurrentVer());
        AppAndroid.setText(app.getAndroidVer());
//        System.out.println(app.getImgSrc());
        image = new Image(app.getImgSrc());
        AppImage.setImage(image);
        Rectangle clip = new Rectangle(AppImage.getFitWidth(), AppImage.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        AppImage.setClip(clip);
        topChart.setText(app.getTop());
        if(app.getStatus().equals("GOLD")){
            chosenCard.setStyle("-fx-background-color: #F8F8AE;");
        } else if (app.getStatus().equals("SILVER")){
            chosenCard.setStyle("-fx-background-color: #F5F5F5;");
        } else if (app.getStatus().equals("BRONZE")){
            chosenCard.setStyle("-fx-background-color: #FFE0C1;");
        } else {
            chosenCard.setStyle("-fx-background-color: #FFF;");
        }

        con = getConnection();
        try {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con.createStatement();
            String sql = "SELECT SUBSTR(app_name,0,10), img FROM TABLE(rec_pkg.test_rec('app_name = ''"+ app.getName() +"''')) where img is not null and app_name != '" + app.getName() + "'";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            int i = 0;
            while(rs.next() && i < 4){
                String name = rs.getString(1);
                String imgSrc = rs.getString(2);
                VBox v = new VBox();

                Image img = new Image(getClass().getResource("/img/" + imgSrc).toExternalForm());
                ImageView imgV = new ImageView(img);
                imgV.setFitHeight(40);
                imgV.setFitWidth(40);
                Rectangle clipV = new Rectangle(imgV.getFitWidth(), imgV.getFitHeight());
                clipV.setArcWidth(10);
                clipV.setArcHeight(10);
                imgV.setClip(clipV);

                Label lname = new Label(name + "..");
                v.setAlignment(Pos.CENTER);
                v.getChildren().addAll(imgV, lname);
                suggestedHBOX.getChildren().add(v);
                i++;
            }
            con.close();
        } catch (Exception e) {}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apps = DBConnection();

        if(apps.size() > 0){
            setData(apps.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(App app) {
                    setData(app);
                }
            };
        }
        int col = 0;
        int row = 1;

        System.out.println(apps.size());
        try {
            for (int i = 0; i < apps.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
//                Parent root = FXMLLoader.load(getClass().getResource("yourFXMLFileName.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(apps.get(i), myListener);

                if(col == 4){
                    col = 0;
                    row++;
                }

                grid.add(anchorPane,col++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);


                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ObservableList<String> list1 = FXCollections.observableArrayList("All", "ART_AND_DESIGN", "GAME" ,"BOOKS_AND_REFERENCE", "BUSINESS", "SOCIAL", "ENTERTAINMENT", "LIFESTYLE", "PRODUCTIVITY");
        ObservableList<String> list2 = FXCollections.observableArrayList("Everyone", "Teen", "Mature 17+", "Everyone 10+");
        ObservableList<String> list3 = FXCollections.observableArrayList("Rating", "Reviews");
        ObservableList<String> list4 = FXCollections.observableArrayList("Everyone","Teen", "Free");
        ctg.setItems(list1);
        fee.setItems(list2);
        rtg.setItems(list3);
        top5.setItems(list4);
    }

    @FXML
    void search(KeyEvent event) {
        String s = searchfield.getText().toLowerCase();
        int col = 0;
        int row = 1;
        grid.getChildren().clear();

        for(int i = 0; i < apps.size(); i++){
            if(apps.get(i).getName().toLowerCase().contains(s)){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    ItemController itemController = fxmlLoader.getController();
                    itemController.setData(apps.get(i), myListener);

                    if (col == 4) {
                        col = 0;
                        row++;
                    }
                    grid.add(anchorPane, col++, row);

                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);


                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (Exception e){}
            }
        }
    }

    @FXML
    void ctgSelect(ActionEvent event) {
        String s = String.valueOf(ctg.getSelectionModel().getSelectedItem());

        filteredApps.clear();
        int col = 0;
        int row = 1;
        if(s == "All"){
            apps = DBConnection();
            if(apps.size() > 0){
                setData(apps.get(0));
                myListener = new MyListener() {
                    @Override
                    public void onClickListener(App app) {
                        setData(app);
                    }
                };
            }
            for(int i = 0; i < apps.size(); i++) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();

                        ItemController itemController = fxmlLoader.getController();
                        itemController.setData(apps.get(i), myListener);

                        if (col == 4) {
                            col = 0;
                            row++;
                        }
                        grid.add(anchorPane, col++, row);

                        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        grid.setMaxWidth(Region.USE_PREF_SIZE);

                        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        grid.setMaxHeight(Region.USE_PREF_SIZE);


                        GridPane.setMargin(anchorPane, new Insets(10));
                    } catch (Exception e) {
                    }
                }
        } else {
            grid.getChildren().clear();
            ArrayList<App> list = new ArrayList<>();
            con = getConnection();
            try {
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                con.createStatement();
                String sql = "select * from apps where category = '" + s + "' and img is not null";
                pstmt = con.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
//                System.out.println("works");
                    String appName = rs.getString(1);
                    String category = rs.getString(2);
                    double rating = Double.parseDouble(rs.getString(3));
                    double reviews = Double.parseDouble(rs.getString(4));
                    String size = rs.getString(5);
                    String installs = rs.getString(6);
                    String price = rs.getString(7);
                    String type = rs.getString(8);
                    String contentRating = rs.getString(9);
                    String genre = rs.getString(10);
                    String lastUpdated = rs.getString(11);
                    String currentVersion = rs.getString(12);
                    String androidVersion = rs.getString(13);
                    String status = rs.getString(14);
                    String img = rs.getString(16);
                    String top = rs.getString(17);
//                    String imgSrc = "/img/"+img;
                    if(!img.contains("file")){
                        img = "/img/"+img;
                    }
                    App t = new App(appName, category, rating, reviews, size, installs, price, contentRating, lastUpdated, currentVersion, androidVersion, status, img.trim(), top, type, genre);

                    list.add(t);
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            apps.clear();
            apps = list;
            System.out.println(apps.size());

            for(int i = 0; i < apps.size(); i++) {
                if (apps.get(i).getCategory().equals(s)) {
//                    filteredApps.add(apps.get(i));
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();

                        ItemController itemController = fxmlLoader.getController();
                        itemController.setData(apps.get(i), myListener);

                        if (col == 4) {
                            col = 0;
                            row++;
                        }
                        grid.add(anchorPane, col++, row);

                        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        grid.setMaxWidth(Region.USE_PREF_SIZE);

                        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        grid.setMaxHeight(Region.USE_PREF_SIZE);


                        GridPane.setMargin(anchorPane, new Insets(10));
                    } catch (Exception e) {
                    }
                }
            }
            setData(apps.get(0));
//            System.out.println(filteredApps.size());
        }
    }

    @FXML
    void feeSelect(ActionEvent event) {
        grid.getChildren().clear();
        int col = 0;
        int row = 1;

        String s2 = String.valueOf(fee.getSelectionModel().getSelectedItem());

//        for(int i = 0; i < apps.size(); i++){
//
//        }
            for(int i = 0; i < apps.size(); i++) {
                if (apps.get(i).getContentRating().equals(s2)) {
//                    System.out.println(filteredApps.get(i).getContentRating());
                    filteredApps.add(apps.get(i));
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();

                        ItemController itemController = fxmlLoader.getController();
                        itemController.setData(apps.get(i), myListener);

                        if (col == 4) {
                            col = 0;
                            row++;
                        }
                        grid.add(anchorPane, col++, row);

                        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        grid.setMaxWidth(Region.USE_PREF_SIZE);

                        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        grid.setMaxHeight(Region.USE_PREF_SIZE);


                        GridPane.setMargin(anchorPane, new Insets(10));
                    } catch (Exception e) {
                    }
                } else {

                }
            }

            setData(filteredApps.get(0));
//        System.out.println(filteredApps.size());
    }

//    @FXML
//    void checkSelected(ActionEvent event) {
//
//    }

    @FXML
    void sortSelect(ActionEvent event) {
        String s = String.valueOf(rtg.getSelectionModel().getSelectedItem());
        System.out.println(s);
        if (s.equals("Rating")) {
            Collections.sort(filteredApps, new Comparator<App>() {
                @Override
                public int compare(App o1, App o2) {
                    if (o1.getRating() > o2.getRating()) return -1;
                    else if (o1.getRating() < o2.getRating()) return 1;
                    else return 0;
                }
            });
        } else {
            Collections.sort(filteredApps, new Comparator<App>() {
                @Override
                public int compare(App o1, App o2) {
                    if (o1.getReviews() > o2.getReviews()) return -1;
                    else if (o1.getReviews() < o2.getReviews()) return 1;
                    else return 0;
                }
            });
        }

        int col = 0;
        int row = 1;

        System.out.println(filteredApps.size());
        for (int i = 0; i < filteredApps.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(filteredApps.get(i), myListener);

                if (col == 4) {
                    col = 0;
                    row++;
                }
                grid.add(anchorPane, col++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);


                GridPane.setMargin(anchorPane, new Insets(10));
            } catch (Exception e) {
            }
        }
        setData(filteredApps.get(0));
    }

    @FXML
    void addNewApp(ActionEvent event) {
//        System.out.println("works");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @FXML
    void refreshView(MouseEvent event) {
        grid.getChildren().clear();
        apps = DBConnection();

        if(apps.size() > 0){
            setData(apps.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(App app) {
                    setData(app);
                }
            };
        }
        int col = 0;
        int row = 1;

        System.out.println(apps.size());
        try {
            for (int i = 0; i < apps.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
//                Parent root = FXMLLoader.load(getClass().getResource("yourFXMLFileName.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(apps.get(i), myListener);

                if(col == 4){
                    col = 0;
                    row++;
                }

                grid.add(anchorPane,col++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);


                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<App> DBConnection() {
        ArrayList<App> list = new ArrayList<>();
        con = getConnection();
        try {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con.createStatement();
            String sql = "select * from apps where img is not null";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
//                System.out.println("works");
                String appName = rs.getString(1);
                String category = rs.getString(2);
                double rating = Double.parseDouble(rs.getString(3));
                double reviews = Double.parseDouble(rs.getString(4));
                String size = rs.getString(5);
                String installs = rs.getString(6);
                String price = rs.getString(7);
                String type = rs.getString(8);
                String contentRating = rs.getString(9);
                String genre = rs.getString(10);
                String lastUpdated = rs.getString(11);
                String currentVersion = rs.getString(12);
                String androidVersion = rs.getString(13);
                String status = rs.getString(14);
                String img = rs.getString(16);
                String top = rs.getString(17);
                if(!img.contains("file")){
                    img = "/img/"+img;
                }
//                System.out.println(status);
//                String imgSrc = "/img/f75d94874d855a7fcfcc922d89ac5e80.png";
                App t = new App(appName, category, rating, reviews, size, installs, price, contentRating, lastUpdated, currentVersion, androidVersion, status, img.trim(), top, type, genre);
                list.add(t);

//                String appName = rs.getString(1);
//                String appCategory = rs.getString(2);
//                double appRating = Double.parseDouble(rs.getString(3));
//                String appReviews = rs.getString(4);
//                String appSize = rs.getString(5);
//                String installs = rs.getString(6);
//                String appType = rs.getString(7);
//                String price = rs.getString(8);
//                String contentRating = rs.getString(9);
//                String genres = rs.getString(10);
//                String lastUpdated = rs.getString(11);
//                String current_ver = rs.getString(12);
//                String androidVersion = rs.getString(13);
//
//                Blob b = rs.getBlob(14);
//                Image appImage = new Image(new ByteArrayInputStream(b.getBytes(1,(int)b.length())));

//                System.out.println(list.size());
//                System.out.println(list);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
//        for(int i = 0; i < list.size(); i++){
//            System.out.println(list.get(i).getImgSrc());
//        }
        return list;
    }

    public static Connection getConnection(){
        boolean isDriverLoaded = false;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
//            System.out.println("Driver Loaded");
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
        } catch (SQLException e) {

        }
        return con;
    }

    @FXML
    void statistics(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statistics.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void deleteApp(ActionEvent event) throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedApp.getName() + " ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Connection con = getConnection();
            try  {
                String query = "DELETE FROM APPS WHERE APP_NAME = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, selectedApp.getName());
                stmt.executeQuery();
                apps = DBConnection();
                setData(apps.get(0));
                drawApps(apps);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void editApp(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_app.fxml"));
            Parent root = fxmlLoader.load();
            EditController editController = fxmlLoader.<EditController>getController();
            editController.setApp(selectedApp);
            editController.setController(this);


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("img/logo.png"));
            stage.setTitle("Edit App");
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void drawApps(List<App> apps) throws Exception{
        grid.getChildren().clear();
        int col = 0;
        int row = 1;

        try {
            for (int i = 0; i < apps.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane appPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(apps.get(i), myListener);

                if(col == 4) {
                    col = 0;
                    row++;
                }
                grid.add(appPane,col++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);


                GridPane.setMargin(appPane, new Insets(10));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void topAction(ActionEvent event) {
        grid.getChildren().clear();
        String s = String.valueOf(top5.getSelectionModel().getSelectedItem());
        con = getConnection();
        if(s.equals("Everyone")){
            try {
                con.createStatement();
                String sql = "select distinct app_name from apps where top_t = 2";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int col = 0;
                int row = 1;
                while (rs.next()) {
                    String appName = rs.getString(1);
                    for(int i = 0; i < apps.size(); i++){
                        if(appName.equals(apps.get(i).getName())){
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();

                                ItemController itemController = fxmlLoader.getController();
                                itemController.setData(apps.get(i), myListener);

                                if (col == 4) {
                                    col = 0;
                                    row++;
                                }
                                grid.add(anchorPane, col++, row);

                                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                                grid.setMaxWidth(Region.USE_PREF_SIZE);

                                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                grid.setMaxHeight(Region.USE_PREF_SIZE);


                                GridPane.setMargin(anchorPane, new Insets(10));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (s.equals("Teen")){
            try {
                con.createStatement();
                String sql = "select distinct app_name from apps where top_t = 3";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int col = 0;
                int row = 1;
                while (rs.next()) {
                    String appName = rs.getString(1);
                    for(int i = 0; i < apps.size(); i++){
                        if(appName.equals(apps.get(i).getName())){
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();

                                ItemController itemController = fxmlLoader.getController();
                                itemController.setData(apps.get(i), myListener);

                                if (col == 4) {
                                    col = 0;
                                    row++;
                                }
                                grid.add(anchorPane, col++, row);

                                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                                grid.setMaxWidth(Region.USE_PREF_SIZE);

                                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                grid.setMaxHeight(Region.USE_PREF_SIZE);


                                GridPane.setMargin(anchorPane, new Insets(10));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if(s.equals("Free")) {
            try {
                con.createStatement();
                String sql = "select distinct app_name from apps where top_t = 1";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int col = 0;
                int row = 1;
                while (rs.next()) {
                    String appName = rs.getString(1);
                    for(int i = 0; i < apps.size(); i++){
                        if(appName.equals(apps.get(i).getName())){
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();

                                ItemController itemController = fxmlLoader.getController();
                                itemController.setData(apps.get(i), myListener);

                                if (col == 4) {
                                    col = 0;
                                    row++;
                                }
                                grid.add(anchorPane, col++, row);

                                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                                grid.setMaxWidth(Region.USE_PREF_SIZE);

                                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                grid.setMaxHeight(Region.USE_PREF_SIZE);


                                GridPane.setMargin(anchorPane, new Insets(10));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
