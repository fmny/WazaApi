package com.thales.wazajfx.controller;
import com.gluonhq.connect.GluonObservableObject;

import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.User;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class UserConnectController implements Initializable {
    @FXML
    public TextField txtLogin;
    @FXML
    public PasswordField txtPassWord;
    @FXML
    public Button btnConnect;
    public Label lbError;
    @FXML
    public Button btnInscription;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnConnect.setOnMouseClicked(mouseEvent -> {
            GluonObservableObject<User> potentialConnected =
                    HttpRequests.tryLogin(new User("",txtLogin.getText(), txtPassWord.getText()));

            potentialConnected.setOnSucceeded(connectStateEvent -> {
                WazaApplication.setConnectedUser(potentialConnected.get());
                WazaApplication.setScreen("accueil");
                txtPassWord.clear();
                txtLogin.clear();
                lbError.setVisible(false);
            });
            potentialConnected.setOnFailed(connectStateEvent -> {
                lbError.setVisible(true); //Affiche un message d erreur si le login et le mot de passe ne sont pas dans la base
            });
        });


        btnInscription.setOnMouseClicked(mouseEvent -> {
                WazaApplication.setScreen("userAdd");
            });


    }
}
