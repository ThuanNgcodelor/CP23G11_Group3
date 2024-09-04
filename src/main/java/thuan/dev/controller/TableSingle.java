package thuan.dev.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import thuan.dev.models.tables.Tables;

import java.net.URL;
import java.util.ResourceBundle;

public class TableSingle implements Initializable {

    private Tables tables;

    @FXML
    private Label table_name;

    @FXML
    private Label table_status;

    private int tableID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setTableData(Tables tb) {
        this.tables = tb;
        this.tableID = tb.getTableID();

        table_name.setText(tables.getTable_name());
        table_status.setText(String.valueOf(tables.getStatus()));
    }
}
