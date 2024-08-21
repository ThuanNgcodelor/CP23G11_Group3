package thuan.dev.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import thuan.dev.models.bill.BillDAO;
import thuan.dev.models.bill.BillImple;
import thuan.dev.models.bill.Bills;
import thuan.dev.models.brand.BrandDAO;
import thuan.dev.models.brand.BrandImple;
import thuan.dev.models.brand.Brands;
import thuan.dev.models.category.Category;
import thuan.dev.models.category.CategoryDAO;
import thuan.dev.models.category.CategoryImple;
import thuan.dev.models.employee.EmployeeDAO;
import thuan.dev.models.employee.EmployeeImp;
import thuan.dev.models.employee.Employees;
import thuan.dev.models.products.Product;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminController {

    @FXML
    private TextField phone;

    @FXML
    private TextField cccd;

    @FXML
    private DatePicker birthdays;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private TextField fullname;

    @FXML
    private AnchorPane staff;

    @FXML
    private AnchorPane checkbill;

    @FXML
    private Button staffbutton;

    @FXML
    private ComboBox<Integer> role;

    @FXML
    private ComboBox<Category> add_categoryID;

    @FXML
    private ComboBox<Brands> add_brandID;

    @FXML
    private TextField add_stock;

    @FXML
    private TextField add_price;

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

    private FilteredList<Employees> employeesFilteredList;

    ObservableList<Employees> employeesList;

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
    private TableColumn<Employees, String> table_cccd;

    @FXML
    private TableColumn<Employees, Date> table_date;

    @FXML
    private TableColumn<Employees, String> table_email;

    @FXML
    private TableColumn<Employees, String> table_name;

    @FXML
    private TableColumn<Employees, String> table_phone;

    @FXML
    private TableColumn<Employees, Integer> table_role;

    @FXML
    private TableColumn<Employees, Integer> table_id;

    @FXML
    private TextField table_search;

    @FXML
    private TableView<Employees> table_staff;

    @FXML
    private Label username;

    ObservableList<Bills> billsList;

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
        totalCustomers();
        showListBill();
        BrandComboBox();
        RoleComBoBox();
        showStaff();
        searchStaff();
        table_staff.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayCustomerDetails(newValue);
            }
        });

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

    @FXML
    private void initialize() {
        AppService.getInstance().setAdminController(this);
    }
    //gọi lai biến AppServiive để có thể nhân

    public void displayUsername() {
        username.setText(String.valueOf(Data.fullname));
    }
    //Hiển thị ID khi đăng nhập

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

//-----------------------------------------STAFF,Customer-----------------------------------------------------------------------------------------------------------------------------------------------
    private void RoleComBoBox() {
        role.getItems().addAll(0, 1);

        role.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer role) {
                return role == 0 ? "Admin" : "Staff";
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        });

        role.setPromptText("Select Role");
    }

    @FXML
    private void signUpAction(ActionEvent event) {
        String phoneF = phone.getText();
        String cccdF = cccd.getText();
        LocalDate localDate = birthdays.getValue();
        String emailF = email.getText();
        String passwordF = password.getText();
        String fullnameF = fullname.getText();
        Integer roleF = role.getValue();

        if (phoneF.isEmpty() || cccdF.isEmpty() || localDate == null || emailF.isEmpty() || passwordF.isEmpty() || fullnameF.isEmpty() || roleF == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter complete data");
            return;
        }
        if (!phoneF.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be a number");
            return;
        }
        if (!emailF.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email");
            return;
        }
        if (!cccdF.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid passport number");
            return;
        }
        if (!localDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Date of birth must be before the current date");
            return;
        }

        Date birthF = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm registration");
        confirmationAlert.setHeaderText("Do you want to add this employee?");
        confirmationAlert.setContentText("Employee: " + fullnameF);

        ButtonType buttonYes = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            Employees emp = new Employees(phoneF, cccdF, birthF, emailF, passwordF, fullnameF, roleF);
            EmployeeDAO employeeDAO = new EmployeeImp();

            boolean flag = employeeDAO.addEmployee(emp);
            if (flag) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Add employee successfully");
                clearFormStaff();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee");
            }
        }
        showStaff();
    }

    private void clearFormStaff() {
        phone.clear();
        cccd.clear();
        email.clear();
        password.clear();
        fullname.clear();
        birthdays.setValue(null);
        role.getSelectionModel().clearSelection();
    }

    private void showStaff() {
        EmployeeDAO employeeDAO = new EmployeeImp();
        List<Employees> customer = employeeDAO.show();
        employeesList = FXCollections.observableList(customer);
        employeesFilteredList = new FilteredList<>(employeesList, e -> true);

        table_id.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        table_name.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        table_cccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        table_date.setCellValueFactory(new PropertyValueFactory<>("birth"));
        table_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        table_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table_role.setCellValueFactory(new PropertyValueFactory<>("role"));

        table_staff.setItems(employeesFilteredList);
    }

    @FXML
    private void searchStaff() {
        table_search.setOnKeyReleased(e -> {
            String keyword = table_search.getText().toLowerCase();

            employeesFilteredList.setPredicate(employee -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                String fullname = employee.getFullname() != null ? employee.getFullname().toLowerCase() : "";
                String email = employee.getEmail() != null ? employee.getEmail().toLowerCase() : "";
                String phone = employee.getPhone() != null ? employee.getPhone().toLowerCase() : "";

                return fullname.contains(keyword) || email.contains(keyword) || phone.contains(keyword);
            });
        });
    }

//-----------------------------------------STAFF,Customer-----------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------PRODUCTS--------------------------------------------------------------------------------------------------------------------------------------------------
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

    private void displayCustomerDetails(Employees emp) {

        fullname.setText(emp.getFullname());
        email.setText(emp.getEmail());
        phone.setText(emp.getPhone());
        birthdays.setPromptText(emp.getBirth().toString());
        cccd.setText(emp.getCccd());
        password.setText(emp.getPassword());
        role.getSelectionModel().getSelectedItem();

    }

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

    public void addProducts() {
        try {
            String name = add_productName.getText();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product name is required!");
                return;
            }

            Category selectedCategory = add_categoryID.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product type is required!");
                return;
            }
            int categoryID = selectedCategory.getId();

            Brands selectedBrand = add_brandID.getSelectionModel().getSelectedItem();
            if (selectedBrand == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Supplier is required!");
                return;
            }
            int brandID = selectedBrand.getBrandID();

            String priceText = add_price.getText();
            if (priceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Price is required!");
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid price!");
                return;
            }

            if (Data.path == null || Data.path.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please add photo!");
                return;
            }
            String images = Data.path;

            String stockText = add_stock.getText();
            if (stockText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity is required!");
                return;
            }
            int stock;
            try {
                stock = Integer.parseInt(stockText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity!");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác nhận thêm sản phẩm");
            confirmationAlert.setHeaderText("Bạn có muốn thêm sản phẩm này?");
            confirmationAlert.setContentText("Sản phẩm: " + name);

            ButtonType buttonYes = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {
                Product newProducts = new Product();
                newProducts.setProductName(name);
                newProducts.setCategoryID(categoryID);
                newProducts.setPrice(price);
                newProducts.setImages(images);
                newProducts.setStock(stock);
                newProducts.setBrandID(brandID);

                ProductDAO productDAO = new ProductImple();
                boolean addProducts = productDAO.addProduct(newProducts);

                if (addProducts) {
                    showProduct();
                    clearInputFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Add product failed!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    Image image;

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
                showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng chọn sản phẩm để cập nhật");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác nhận cập nhật");
            confirmationAlert.setHeaderText("Bạn có muốn cập nhật sản phẩm này?");
            confirmationAlert.setContentText("Sản phẩm: " + selectedProduct.getProductName());

            ButtonType buttonYes = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {
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
                    showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng chỉnh lại ảnh");
                    return;
                }

                ProductDAO productDAO = new ProductImple();
                productDAO.updateProduct(selectedProduct);

                table.refresh();
                clearInputFields();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Cập nhật thành công!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Đã xảy ra lỗi khi cập nhật sản phẩm: " + e.getMessage());
            System.out.println("Error: ");
        }
    }

    //update product và check form khi update
    @FXML
    public void handleDeleteProducts() {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng chọn sản phẩm để xóa!!");
            return;
        }
        Alert comfirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        comfirmAlert.setTitle("Delete product ?");
        comfirmAlert.setHeaderText("Do you want to delete this product?");
        comfirmAlert.setContentText("Product: " + selectedProduct.getProductName());

        ButtonType buttonYes = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        comfirmAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = comfirmAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            ProductDAO productDAO = new ProductImple();
            boolean deleteProducts = productDAO.deleteProduct(selectedProduct.getProductID());
            if (deleteProducts) {
                productList.remove(selectedProduct);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Delete product successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Delete product failed!");
            }
        }
    }
    //Xóa products

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
    private void buttonClear(ActionEvent event) {
        clearInputFields();
    }

// -----------------------------------------PRODUCTS-------------------------------------------------------
    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == homeButton) {
            home.setVisible(true);
            add.setVisible(false);
            staff.setVisible(false);
            checkbill.setVisible(false);
        } else if (event.getSource() == manage) {
            home.setVisible(false);
            add.setVisible(true);
            staff.setVisible(false);
            checkbill.setVisible(false);
            showProduct();
        } else if (event.getSource() == staffbutton) {
            staff.setVisible(true);
            home.setVisible(false);
            add.setVisible(false);
            checkbill.setVisible(false);
        } else if (event.getSource() == buttonbill) {
            staff.setVisible(false);
            home.setVisible(false);
            add.setVisible(false);
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

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
