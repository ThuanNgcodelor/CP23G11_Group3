package thuan.dev.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import thuan.dev.models.bill.BillDAO;
import thuan.dev.models.bill.BillImple;
import thuan.dev.models.bill.Bills;
import thuan.dev.models.brand.BrandDAO;
import thuan.dev.models.brand.BrandImple;
import thuan.dev.models.brand.Brands;
import thuan.dev.models.category.Category;
import thuan.dev.models.category.CategoryDAO;
import thuan.dev.models.category.CategoryImple;
import thuan.dev.models.orders.Order;
import thuan.dev.models.orders.OrderDAO;
import thuan.dev.models.orders.OrderImplements;
import thuan.dev.models.products.Product;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AdminController {

    @FXML
    private AnchorPane staff;

    @FXML
    private AnchorPane checkbill;

    @FXML
    private Button staffbutton;

    @FXML
    private ComboBox<?> role;

    @FXML
    private Button card_remove;


    @FXML
    private Label card_total;

    @FXML
    private Label cart_quantity;

    @FXML
    private TableColumn<Product, String> showcard_order;

    @FXML
    private TableColumn<Product, Double> showcard_price;

    @FXML
    private TableColumn<Product, Integer> showcard_quantity;

    private ObservableList<Order> ordersList;

    @FXML
    private TableView<Order> card_display_table;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private ComboBox<Category> add_categoryID;

    @FXML
    private ComboBox<Brands> add_brandID;

    @FXML
    private TextField add_stock;

    @FXML
    private TextField add_price;

    @FXML
    private TextField sale_code;

    @FXML
    private TextField add_productName;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, String> categoryName;
    @FXML
    private TableColumn<Product, Double> price;
    @FXML
    private TableColumn<Product, String> images;
    @FXML
    private TableColumn<Product, Integer> stock;
    @FXML
    private TableColumn<Product, String> brandName;

    private FilteredList<Product> filteredList;

    private ObservableList<Product> productList;

    @FXML
    private AnchorPane add;

    @FXML
    private AnchorPane home;

    @FXML
    private Button homeButton;

    @FXML
    private Button buttonbill;

    @FXML
    private Button logout;

    @FXML
    private Button manage;

    @FXML
    private AnchorPane order;

    @FXML
    private Button orderButton;

    @FXML
    private TableView<Bills> table_orders;

    @FXML
    private TextField search_products;

    @FXML
    private Label total_bill;

    @FXML
    private Label total_price;

    @FXML
    private Label price_inDate;

    @FXML
    private ImageView images_add;

    @FXML
    private TableColumn<Bills, Integer> bill_customers;

    @FXML
    private TableColumn<Bills, Date> bill_date;

    @FXML
    private TableColumn<Bills, Integer> bill_id;

    @FXML
    private TableColumn<Bills, BigDecimal> bill_total_price;

    @FXML
    private Label username;

    private ObservableList<Bills> billsList;

    public void menu() throws IOException {
        CategoryComboBox();
        showProduct();
        search();
        displayUsername();
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayProductDetails(newValue);
            }
        });
        totalPrice();
        totalCustomers();
        showListBill();
        BrandComboBox();
    }

    public void showListBill() {
        BillDAO billDAO = new BillImple();
        List<Bills> bills = billDAO.getAllBills();
        billsList = FXCollections.observableArrayList(bills);

        bill_id.setCellValueFactory(new PropertyValueFactory<>("billID"));
        bill_total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bill_customers.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        bill_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table_orders.setItems(billsList);
    }
    //Dashbound

    public void showDisplayCard() {
        OrderDAO orderDAO = new OrderImplements();
        List<Order> orders = orderDAO.showDisplayCard();
        ordersList = FXCollections.observableArrayList(orders);

        showcard_order.setCellValueFactory(new PropertyValueFactory<>("productName"));
        showcard_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        showcard_price.setCellValueFactory(new PropertyValueFactory<>("total"));

        card_display_table.setItems(ordersList);
        totalPrice();
    }

    @FXML
    private void initialize(){
        AppService.getInstance().setAdminController(this);
    }
    //gọi lai biến AppServiive để có thể nhân

    public void displayUsername() {
        username.setText(String.valueOf(Data.fullname));
    }
    //Hiển thị ID khi đăng nhập

    @FXML
    public void updateCart(ActionEvent event) {

        OrderDAO orderDAO = new OrderImplements();
        Order selectedOrder = card_display_table.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đơn hàng để cập nhật!");
            return;
        }
        orderDAO.updateOrder(selectedOrder.getOrderID());

        Bills bills = new Bills();

        // Loại bỏ ký tự không phải là số và khoảng trắng trong chuỗi giá tiền
        String totalPriceString = card_total.getText().replaceAll("[^\\d.]", "").trim();

        bills.setTotalPrice(Double.parseDouble(totalPriceString));
        bills.setCustomerID(Data.customerID);
        bills.setDate(new Date());

        BillDAO billDAO = new BillImple();
        boolean selectBill = billDAO.addBill(bills);
        showAlert(Alert.AlertType.INFORMATION, "Cập nhật đơn hàng thành công!");
        showDisplayCard();
        menuRestart();
    }

    public void menuRestart() {
        card_display_table.getSelectionModel().getSelectedItem();
        card_total.setText("0.0 ");
        cart_quantity.setText("");
    }

    public void totalPrice() {
        double total = 0;
        int quantity = 0;

        if (ordersList != null) {
            for (Order order : ordersList) {
                if (order != null) {
                    total += order.getTotal();
                    quantity += order.getQuantity();
                }
            }
        }

        double finalTotal = total;
        int finalQuantity = quantity;

        Platform.runLater(() -> {
            card_total.setText(String.format("%,.0f", finalTotal));
            cart_quantity.setText(String.format("%d", finalQuantity));
        });
    }

    //Tính toán số tiền và hiển thị quantity

    public void totalCustomers() {
        BillDAO billDAO = new BillImple();
        List<Bills> selectedBills = billDAO.getAllBills();
        int totalPrice = 0;
        int customerID = 0;

        if (selectedBills != null) {
            for (Bills bills : selectedBills) {
                if (bills != null) {
                    customerID++;
                    totalPrice += bills.getTotalPrice();
                }
            }
        }
        double finalTotal = totalPrice;
        Platform.runLater(() -> total_price.setText(String.format("$%.2f", finalTotal)));
        total_bill.setText(String.valueOf(customerID));
    }
    //Tính toán quantity customers

    private void displayProductDetails(Product product) {
        add_productName.setText(product.getProductName());
        add_price.setText(String.valueOf(product.getPrice()));
        add_stock.setText(String.valueOf(product.getStock()));

        String images = product.getImages();
        if (images != null && !images.isEmpty()) {
            try {
                Image productImages = new Image(new File(images).toURI().toString(), 220, 220, false, true);
                images_add.setImage(productImages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            images_add.setImage(null);
        }
    }
    //Khi touch vào product sẽ hiện lại input ở để chỉnh sửa

    @FXML
    private void buttonClear(ActionEvent event) {
        clearInputFields();
    }

    @FXML
    private void selectAllProducts(ActionEvent event) {
        table.getSelectionModel().selectAll();
    }
    //Chọn tất cả sản phẩm để đặt hàng

    @FXML
    private void search() {
        search_products.setOnKeyReleased(e -> {
            String keyword = search_products.getText().toLowerCase();
            filteredList.setPredicate(product -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                return product.getProductName().toLowerCase().contains(keyword)
                        || product.getCategoryName().toLowerCase().contains(keyword)
                        || product.getBrandName().toLowerCase().contains(keyword);
            });
        });
    }
    //search products


    private void CategoryComboBox() {
        if (add_categoryID != null) {
            CategoryDAO categoryDAO = new CategoryImple();
            List<Category> categories = categoryDAO.getAllCategory();
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
            add_categoryID.setItems(categoryList);
        }
    }

    private void BrandComboBox() {
        if (add_brandID != null) {
            BrandDAO brandDAO = new BrandImple();
            List<Brands> brands = brandDAO.getAllBrand();
            ObservableList<Brands> brandsObservableList = FXCollections.observableArrayList(brands);
            add_brandID.setItems(brandsObservableList);
        }
    }

    public void showProduct() {
        ProductDAO productDAO = new ProductImple();
        List<Product> products = productDAO.show();
        productList = FXCollections.observableArrayList(products);
        filteredList = new FilteredList<>(productList, e -> true);

        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        images.setCellValueFactory(new PropertyValueFactory<>("images"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));

        table.setItems(filteredList);
    }

    public void addProducts() {
        try {
            // Get product name and validate
            String name = add_productName.getText();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Tên sản phẩm là bắt buộc!");
                return;
            }

            // Get selected category and validate
            Category selectedCategory = add_categoryID.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showAlert(Alert.AlertType.ERROR, "Loại sản phẩm là bắt buộc!");
                return;
            }
            int categoryID = selectedCategory.getId();

            // Get selected brand and validate
            Brands selectedBrand = add_brandID.getSelectionModel().getSelectedItem();
            if (selectedBrand == null) {
                showAlert(Alert.AlertType.ERROR, "Nhãn hàng là bắt buộc!");
                return;
            }
            int brandID = selectedBrand.getBrandID();

            // Get price and validate
            String priceText = add_price.getText();
            if (priceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Giá là bắt buộc!");
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Giá không hợp lệ!");
                return;
            }

            // Validate image path
            if (Data.path == null || Data.path.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Vui lòng thêm ảnh");
                return;
            }
            String images = Data.path;

            // Get stock and validate
            String stockText = add_stock.getText();
            if (stockText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Trạng thái nổi bật là bắt buộc!");
                return;
            }
            int stock;
            try {
                stock = Integer.parseInt(stockText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Trạng thái không hợp lệ!");
                return;
            }

            // Create new product
            Product newProducts = new Product();
            newProducts.setProductName(name);
            newProducts.setCategoryID(categoryID);
            newProducts.setPrice(price);
            newProducts.setImages(images);
            newProducts.setStock(stock);
            newProducts.setBrandID(brandID);

            // Save product
            ProductDAO productDAO = new ProductImple();
            boolean addProducts = productDAO.addProduct(newProducts);

            // Handle result
            if (addProducts) {
                showProduct();
                clearInputFields();
                showAlert(Alert.AlertType.INFORMATION, "Thêm sản phẩm thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Thêm sản phẩm thất bại!!!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    //Add product và check from tát cả input
    private Image image;

    public void addImages() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(add.getScene().getWindow());
        if (file != null) {
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 220, 220, false, true);
            images_add.setImage(image);
        }
    }

    @FXML
    private void handleUpdateProduct() {
        try {
            Product selectedProduct = table.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Vui lòng chọn sản phẩm để cập nhật");
                return;
            }

            // Update fields only if they are not empty
            String name = add_productName.getText();
            if (!name.isEmpty()) {
                selectedProduct.setProductName(name);
            }

            String priceText = add_price.getText();
            if (!priceText.isEmpty()) {
                Double price = Double.parseDouble(priceText);
                selectedProduct.setPrice(price);
            }

            String images = (Data.path != null && !Data.path.isEmpty()) ? Data.path : selectedProduct.getImages();
            selectedProduct.setImages(images);

            String stockText = add_stock.getText();
            if (!stockText.isEmpty()) {
                Integer stock = Integer.parseInt(stockText);
                selectedProduct.setStock(stock);
            } else {
                showAlert(Alert.AlertType.ERROR, "Vui lòng chỉnh lại ảnh");
                return;
            }

            ProductDAO productDAO = new ProductImple();
            productDAO.updateProduct(selectedProduct);

            table.refresh();
            clearInputFields();
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Đã xảy ra lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //update product và check form khi update

    @FXML
    public void handleDeleteProducts() {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn sản phẩm để xóa!!");
            return;
        }
        ProductDAO productDAO = new ProductImple();
        boolean deleteProducts = productDAO.deleteProduct(selectedProduct.getProductID());
        if (deleteProducts) {
            productList.remove(selectedProduct);
            showAlert(Alert.AlertType.INFORMATION, "Xóa sản phẩm thành công!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Xóa sản phẩm thất bại!");
        }
    }
    //Xóa products

    public void menuDisplayCard() {
        ProductDAO productDAO = new ProductImple();
        productList.clear();
        productList.addAll(productDAO.show());
        int row = 0;
        int column = 0;
        menu_gridPane.getChildren().clear();

        for (Product product : productList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/thuan/dev/controller/cardProduct.fxml"));
                AnchorPane pane = fxmlLoader.load();
                CartController cartController = fxmlLoader.getController();
                cartController.setData(product);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                GridPane.setMargin(pane, new Insets(15));
                menu_gridPane.add(pane, column++, row);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //Show menu products

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void clearInputFields() {
        add_productName.clear();
        add_categoryID.getSelectionModel().clearSelection();
        add_price.clear();
        add_stock.clear();
        if (images_add != null) {
            images_add.setImage(null);
        }
        add_brandID.getSelectionModel().clearSelection();
        add_categoryID.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
            Stage loginStage = new Stage();
            Login login = new Login();
            login.start(loginStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Đăng xuất

    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == homeButton) {
            home.setVisible(true);
            add.setVisible(false);
            order.setVisible(false);
            staff.setVisible(false);
            checkbill.setVisible(false);
        } else if (event.getSource() == manage) {
            home.setVisible(false);
            add.setVisible(true);
            order.setVisible(false);
            staff.setVisible(false);
            checkbill.setVisible(false);
            showProduct();
        } else if (event.getSource() == orderButton) {
            home.setVisible(false);
            add.setVisible(false);
            order.setVisible(true);
            staff.setVisible(false);
            checkbill.setVisible(false);
            menuDisplayCard();
            showDisplayCard();
        } else if (event.getSource() == staffbutton) {
            staff.setVisible(true);
            home.setVisible(false);
            add.setVisible(false);
            order.setVisible(false);
            checkbill.setVisible(false);
        } else if (event.getSource() == buttonbill) {
            staff.setVisible(false);
            home.setVisible(false);
            add.setVisible(false);
            order.setVisible(false);
            checkbill.setVisible(true);
            showListBill();
        }
    }
    //Thanh navbar

    @FXML
    private void buttonCategory(ActionEvent event) {
        try {
            CategoryController categoryController = new CategoryController();
            Stage stage = new Stage();
            categoryController.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void buttonBrand(ActionEvent event) {
        try {
            BrandController brandController = new BrandController();
            Stage stage = new Stage();
            brandController.start(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
