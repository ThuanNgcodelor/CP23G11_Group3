module thuan.dev.controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens thuan.dev.models.orders to javafx.base;
    opens thuan.dev.models.products to javafx.base;
    opens thuan.dev.models.brand to javafx.base;
    opens thuan.dev.models.employee to javafx.base;
    exports thuan.dev.controller to javafx.graphics;

    opens thuan.dev.models.bill to javafx.base;
    opens thuan.dev.models.salary to javafx.base;
    opens thuan.dev.models.logintime to javafx.base;
    opens thuan.dev.models.historysalary to javafx.base;
    opens thuan.dev.models.category to javafx.base;
    opens thuan.dev.models.shipper to javafx.base;
    opens thuan.dev.models.news to javafx.base;
    exports thuan.dev.main;
    opens thuan.dev.main to javafx.fxml;
    opens thuan.dev.models.tables to javafx.fxml;
    opens thuan.dev.controller to javafx.base, javafx.fxml;
    exports thuan.dev.models.voucher to javafx.graphics;
    opens thuan.dev.models.voucher to javafx.base, javafx.fxml;

}
