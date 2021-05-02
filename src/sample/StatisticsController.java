package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController {

    @FXML
    private BarChart<?, ?> categories_chart;
    @FXML
    private PieChart content_chart;
    @FXML
    private ComboBox<String> category_combobox;

    @FXML
    public void initialize() {
        drawCategoriesChart();

        ObservableList<String> categoriesList = FXCollections.observableArrayList("All", "Lifestyle", "Game" ,"Art and Design",
                "Books and Reference", "Business", "Entertainment", "Personalization", "Productivity", "Video Players", "Social", "Tools");

        category_combobox.setItems(categoriesList);
        category_combobox.getSelectionModel().select("All");

        drawContentChart();
    }

    public void drawCategoriesChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("App Categories");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity of apps");
        XYChart.Series dataSeries = new XYChart.Series();

        List<String> categories = new ArrayList<>();
        Connection con = Controller.getConnection();
        String query = "select distinct category from apps";
        try {
            Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String category : categories) {
            int quantity;
            try {
                CallableStatement cstmt = con.prepareCall("CALL charts_pkg.getvalue(?) INTO ?");
                cstmt.setString(1, category);
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.executeUpdate();
                quantity = cstmt.getInt(2);
            } catch (SQLException e) {
                e.printStackTrace();
                quantity = 0;
            }
            dataSeries.getData().add(new XYChart.Data(category, quantity));
        }
        categories_chart.getData().add(dataSeries);
    }

    public void drawContentChart() {
        String category = String.valueOf(category_combobox.getSelectionModel().getSelectedItem());
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Connection con = Controller.getConnection();
        if(category.equals("All")) {
            try {
                CallableStatement cstmt = con.prepareCall("CALL charts_pkg.default_pieChart(?) INTO ?");
                cstmt.setString(1, "Everyone");
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Everyone", cstmt.getInt(2)));

                cstmt.setString(1, "Teen");
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Teen", cstmt.getInt(2)));

                cstmt.setString(1, "Other");
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Other", cstmt.getInt(2)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                CallableStatement cstmt = con.prepareCall("CALL charts_pkg.pieChart(?,?) INTO ?");
                cstmt.setString(1, "Everyone");
                cstmt.setString(2, category.replaceAll(" ", "_").toUpperCase());
                cstmt.registerOutParameter(3, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Everyone", cstmt.getInt(3)));

                cstmt.setString(1, "Teen");
                cstmt.setString(2, category.replaceAll(" ", "_").toUpperCase());
                cstmt.registerOutParameter(3, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Teen", cstmt.getInt(3)));

                cstmt.setString(1, "Other");
                cstmt.setString(2, category.replaceAll(" ", "_").toUpperCase());
                cstmt.registerOutParameter(3, Types.INTEGER);
                cstmt.executeUpdate();
                pieChartData.add( new PieChart.Data("Other", cstmt.getInt(3)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        content_chart.setData(pieChartData);
    }
}

