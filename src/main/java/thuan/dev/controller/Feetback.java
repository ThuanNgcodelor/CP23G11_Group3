package thuan.dev.controller;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Feetback{

    private Connection conn = MyConnection.getConnection();


static private Date date;

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date date) {
        Feetback.date = date;
    }

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView background;



    // Phương thức lưu lựa chọn vào cơ sở dữ liệu
    public void saveChoice() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO feedback(feedback,date) Values(?,?)");
           statement.setInt(1,1);
            //statement.setDate(2, new java.sql.Date(getDate().getTime()));
           //statement.setDate(2,getDate());
           statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            handleShowImage(new ActionEvent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void saveChoice1() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO feedback(feedback,date) Values(?,?)");
            statement.setInt(1,2);
            //statement.setDate(2, new java.sql.Date(getDate().getTime()));
            //statement.setDate(2,getDate());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            handleShowImage(new ActionEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveChoice2() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO feedback(feedback,date) Values(?,?)");
            statement.setInt(1,3);
            //statement.setDate(2, new java.sql.Date(getDate().getTime()));
            //statement.setDate(2,getDate());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            handleShowImage(new ActionEvent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveChoice3() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO feedback(feedback,date) Values(?,?)");
            statement.setInt(1,4);
            //statement.setDate(2, new java.sql.Date(getDate().getTime()));
            //statement.setDate(2,getDate());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            handleShowImage(new ActionEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveChoice4() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO feedback(feedback,date) Values(?,?)");
            statement.setInt(1,5);
            //statement.setDate(2, new java.sql.Date(getDate().getTime()));
            //statement.setDate(2,getDate());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            handleShowImage(new ActionEvent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleShowImage(ActionEvent event) {
        // Hiển thị ảnh và sử dụng FadeTransition để làm cho ảnh biến mất sau giây
        imageView.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5), imageView);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> imageView.setVisible(false));
        fadeTransition.play();
    }



    // Phương thức xử lý sự kiện khi người dùng bấm vào một nút
    @FXML
    public void handleChoice(ActionEvent event) {
        // Lưu lựa chọn vào cơ sở dữ liệu
        saveChoice();
    }

}
