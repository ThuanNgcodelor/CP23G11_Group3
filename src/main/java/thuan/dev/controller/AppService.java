package thuan.dev.controller;

public class AppService {
    private static AppService instance = new AppService();
    public AdminController adminController;
    public UserController userController;

    private AppService(){}

    public static AppService getInstance(){
        return instance;
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    public AdminController getAdminController(){
        return adminController;
    }

    public UserController getUserController(){
        return userController;
    }

    public void setAdminController(UserController userController) {
        this.userController = userController;
    }
    //Sử dụng lớp singleton hoặc dịch vụ chung để chia sẻ dữ liệu và trạng thái giữa các controller.
}
