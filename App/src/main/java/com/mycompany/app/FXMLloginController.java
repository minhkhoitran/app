/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
/**
 * FXML Controller class
 *
 * @author kimmo
 */
public class FXMLloginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("Images/branded.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("Images/lock_login.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);

    }

    public void loginButtonOnAction(ActionEvent event) {
        if (usernameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
            validateLogin();
        } else if (usernameTextField.getText().isBlank() == true) {
            loginMessageLabel.setText("Please enter username!");
        } else if (passwordPasswordField.getText().isBlank() == true) {
            loginMessageLabel.setText("Please enter password!");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB = connectionNow.getConnection();

        String verifyLogin = "SELECT * FROM student_db.user_account;";
        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while (queryResult.next()) {
                String user = queryResult.getString("username");
                String password = queryResult.getString("password");
                if (usernameTextField.getText().equals(user) && passwordPasswordField.getText().equals(password)) {
                    createHomeManagementForm();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.hide();
                } else if (!(usernameTextField.getText().equals(user)) && passwordPasswordField.getText().equals(password)) {
                    loginMessageLabel.setText("User does not exist!");
                } else if (usernameTextField.getText().equals(user) && !(passwordPasswordField.getText().equals(password))) {
                    loginMessageLabel.setText("Wrong password!");
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            e.getCause();
        }
    }

    public void Key_Handle_Login(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (usernameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
                validateLogin();
            } else  {
                loginMessageLabel.setText("Please enter username and password!");
            }
        }
    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    public void createHomeManagementForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stageHomeManagement = new Stage();
            stageHomeManagement.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 1024, 615);
            scene.getStylesheets().add("css/style.css");
            stageHomeManagement.setScene(scene);
            stageHomeManagement.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
