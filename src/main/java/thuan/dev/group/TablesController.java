package thuan.dev.group;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import thuan.dev.controller.TableSingle;
import thuan.dev.models.tables.TableDAO;
import thuan.dev.models.tables.TableImple;
import thuan.dev.models.tables.Tables;

import java.io.IOException;
import java.util.List;

public class TablesController extends Application {

    @FXML
    private GridPane menu_table_gridPane;

    private ObservableList<Tables> tablesList;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Table.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Trang chủ");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        tablesList = FXCollections.observableArrayList();
        showMenuTable();
    }

    public void showMenuTable() {
        TableDAO tableDAO = new TableImple();
        List<Tables> tables = tableDAO.showAllTable();
        tablesList.clear();
        tablesList.addAll(tables);
        menu_table_gridPane.getChildren().clear();

        int row = 0;
        int column = 0;
        for (Tables table : tablesList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/tableSingle.fxml"));
                AnchorPane pane = fxmlLoader.load();
                TableSingle controller = fxmlLoader.getController();
                controller.setTableData(table);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                GridPane.setMargin(pane, new Insets(15));
                menu_table_gridPane.add(pane, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
