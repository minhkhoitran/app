/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DatabaseConnection;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXMLaddstudentController implements Initializable {

    @FXML
    private ImageView studentImageView;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public DatePicker dateofbirthPicker;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton otherRadioButton;

    @FXML
    private ToggleGroup sex;

    @FXML
    public TextField emailField;

    @FXML
    public TextField addressField;

    @FXML
    public TextField phoneField;

    @FXML
    public ComboBox<String> classComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button cleanButton;

    String query = null;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = null;
    ResultSet resultset = null;
    PreparedStatement preparedStatement = null;
    private boolean update;

    ObservableList<String> listClass = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File fileStudent = new File("Images/student.png");
        Image imageStudent = new Image(fileStudent.toURI().toString());
        studentImageView.setImage(imageStudent);

        fillComboBoxChanged();
        classComboBox.setItems(listClass);
    }

    public void fillComboBoxChanged() {
        try {
            connection = databaseConnection.getConnection();
            query = "SELECT classname FROM student_db.class;";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                listClass.add(resultset.getString("classname"));
            }
            preparedStatement.close();
            resultset.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }



    public void saveStudent(ActionEvent event) {
        connection = databaseConnection.getConnection();

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String dob = String.valueOf(dateofbirthPicker.getValue());
        if (maleRadioButton.isSelected()) {
            String sex = maleRadioButton.getText();
        } else if (femaleRadioButton.isSelected()) {
            String sex = femaleRadioButton.getText();
        } else {
            String sex = otherRadioButton.getText();
        }
        String email = emailField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()
                || email.isEmpty() || address.isEmpty()
                || phone.isEmpty()) {
            Alert alertStudent = new Alert(Alert.AlertType.ERROR);
            alertStudent.setHeaderText("Save Student!!!");
            alertStudent.setContentText("Please fill all data!");
            alertStudent.showAndWait();
        } else {
            getQuery();
            insert();
        }
    }

    public void cleanStudent(ActionEvent event){
        firstNameField.setText(null);
        lastNameField.setText(null);
        dateofbirthPicker.setValue(null);
        emailField.setText(null);
        addressField.setText(null);
        phoneField.setText(null);
    }

    public void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, lastNameField.getText());
            preparedStatement.setString(2, firstNameField.getText());
            preparedStatement.setString(3, String.valueOf(dateofbirthPicker.getValue()));
            if (maleRadioButton.isSelected()) {
                preparedStatement.setString(4, maleRadioButton.getText());
            } else if (femaleRadioButton.isSelected()) {
                preparedStatement.setString(4, femaleRadioButton.getText());
            } else {
                preparedStatement.setString(4, otherRadioButton.getText());
            }
            preparedStatement.setString(5, emailField.getText());
            preparedStatement.setString(6, addressField.getText());
            preparedStatement.setString(7, phoneField.getText());
            preparedStatement.setString(8, String.valueOf(classComboBox.getSelectionModel().getSelectedIndex() + 1));
            preparedStatement.execute();


            Alert alertSucces = new Alert(Alert.AlertType.CONFIRMATION);
            alertSucces.setHeaderText("Save Information Student");
            alertSucces.setContentText("Done!");

            ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alertSucces.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            Optional<ButtonType> resultAlert = alertSucces.showAndWait();

            Stage stageStudent = (Stage) addButton.getScene().getWindow();
            if (resultAlert.get() == buttonTypeOK) {
                stageStudent.close();
            } else {
                stageStudent.show();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            exception.getMessage();
        }

    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) cleanButton.getScene().getWindow();
            stage.close();
        }
    }

    private void getQuery() {
        query = "INSERT INTO student_db.student (lastname, firstname, date_of_birth, sex, email, address, phonenumber, class_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }

}
