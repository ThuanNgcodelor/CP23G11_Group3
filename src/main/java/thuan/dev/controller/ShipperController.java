package thuan.dev.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ShipperController extends Application {

    @FXML
    private TableColumn<?, ?> age_shipper_table;

    @FXML
    private TableColumn<?, ?> cccd_shipper_table;

    @FXML
    private TableColumn<?, ?> email_shipper_table;

    @FXML
    private TableColumn<?, ?> id_shipper_table;

    @FXML
    private TableColumn<?, ?> name_shipper_table;

    @FXML
    private TableColumn<?, ?> phone_shipper_table;

    @FXML
    private TextField shipper_cccd;

    @FXML
    private DatePicker shipper_date;

    @FXML
    private TextField shipper_email;

    @FXML
    private TextField shipper_name;

    @FXML
    private TextField shipper_phone;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Shipper.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Trang chủ");
        stage.setScene(scene);
        stage.show();
    }
}
