package thuan.dev.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

    @FXML
    private Label cart_quantity;

    private Product product;

    @FXML
    private GridPane menu_gridPane;

    private Integer prodID;

    private SpinnerValueFactory<Integer> spin;

    Order order;
    Employees emp;

    @FXML
    void addBtn(ActionEvent event) {
        int quantity = cart_spinner.getValue();
        if (quantity == 0) {
            showAlert(Alert.AlertType.ERROR, "Quantity cannot be zero!");
            return;
        }

        int customerID = Data.customerID;
        if (customerID == -1) {
            showAlert(Alert.AlertType.ERROR, "Không nhận được customerID");
            return;
        }

        double price = product.getPrice();
        double total = price * quantity;

        Date currentDate = new Date(System.currentTimeMillis());

        ProductDAO productDAO = new ProductImple();
        int currentStock = productDAO.getProductStock(product.getProductID());

        if (quantity > currentStock) {
            showAlert(Alert.AlertType.ERROR, "Số lượng trong quán đã hết!!");
            return;
        }

        OrderDAO orderDAO = new OrderImplements();
        Order existingOrder = orderDAO.getOrder(customerID, product.getProductID());

        if (existingOrder != null) {
            int newQuantity = existingOrder.getQuantity()+quantity;
            double newPrice = newQuantity * price;
            orderDAO.update(product.getProductID(),newQuantity,(int)newPrice);
        } else {
            Order newOrder = new Order(customerID, product.getProductName(), price, quantity, (int) total, currentDate, product.getProductID());
            boolean addOrder = orderDAO.addOrder(newOrder);

            if (addOrder) {
                showAlert(Alert.AlertType.INFORMATION, "Thêm sản phẩm thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Thêm sản phẩm thất bại");
                return;
            }
        }
        productDAO.updateProductStock(product.getProductID(), currentStock - quantity);

        UserController userController = AppService.getInstance().userController;
        userController.showDisplayCard();
        userController.menuDisplayCard();
    }



    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        cart_spinner.setValueFactory(spin);
    }

    public void setData(Product pro) {
        this.product = pro;
        this.prodID = pro.getProductID();

        cart_name.setText(product.getProductName());
        cart_quantity.setText(String.valueOf(product.getStock()));
        cart_price.setText(String.valueOf(product.getPrice()));
        String path = "file:" + product.getImages();
        Image image = new Image(path, 190, 94, false, true);
        cart_images.setImage(image);
        cart_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }

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
