package thuan.dev.main;

import javafx.application.Application;
import javafx.stage.Stage;
import thuan.dev.controller.Login;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Login app = new Login();
        app.start(stage);
    }
}
