package thuan.dev.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import thuan.dev.models.employee.Employees;
import thuan.dev.models.orders.Order;
import thuan.dev.models.orders.OrderDAO;
import thuan.dev.models.orders.OrderImplements;
import thuan.dev.models.products.Product;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;

public class CartController implements Initializable {


    @FXML
    private AnchorPane card_form;

    @FXML
    private Button cart_add;

    @FXML
    private ImageView cart_images;

    @FXML
    private Label cart_name;

    @FXML
    private Label cart_price;

    @FXML
    private Spinner<Integer> cart_spinner;

    private Product product;

    @FXML
    private GridPane menu_gridPane;

    private String prodID;

    private SpinnerValueFactory<Integer> spin;

    Order order;
    Employees emp;

    AdminController controller = new AdminController();


    @FXML
    void addBtn(ActionEvent event) {
        int quantity = cart_spinner.getValue();
        if (quantity == 0) {
            showAlert(Alert.AlertType.ERROR, "Quantity cannot be zero!");
            return;
        }
        //Check stock còn hay ko

        int customerID = Data.customerID;
        if (customerID == -1) {
            showAlert(Alert.AlertType.ERROR, "Customer ID is null");
            return;
        }
        //check id quản lí đặt hàng

        double price = product.getPrice();
        double total = price * quantity;

        Date currentDate = new Date(System.currentTimeMillis());

        ProductDAO productDAO = new ProductImple();
        int currentStock = productDAO.getProductStock(product.getProductID());

        if (quantity > currentStock) {
            showAlert(Alert.AlertType.ERROR, "Số lượng trong quán đã hết!!");
            return;
        }

        Order order = new Order(customerID, product.getProductName(), price, quantity, (int) total, currentDate);
        OrderDAO orderDAO = new OrderImplements();
        boolean addOrder = orderDAO.addOrder(order);

        if (addOrder) {
            productDAO.updateProductStock(product.getProductID(), currentStock - quantity);
            showAlert(Alert.AlertType.INFORMATION, "Thêm sản phẩm thành công!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm sản phẩm thất bại");
        }
        controller.menu();
    }
    //touch vào button để add products

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        cart_spinner.setValueFactory(spin);
    }
    //Update quantity Products

    public void setData(Product pro) {
        this.product = pro;
        this.prodID = pro.getProductName();

        cart_name.setText(product.getProductName());
        cart_price.setText(String.valueOf(product.getPrice()));
        String path = "file:" + product.getImages();
        Image image = new Image(path, 190, 94, false, true);
        cart_images.setImage(image);
        cart_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }
    //Show details sản phẩm ra
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
