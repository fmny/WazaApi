package com.thales.wazajfx.controller;


import com.gluonhq.connect.GluonObservableList;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.thales.wazajfx.model.*;
import com.thales.wazajfx.*;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class UserAddController implements Initializable {
    @FXML
    public TextField txtLogin;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public TextField txtPassword2;
    @FXML
    public Button btnInscription;
    @FXML
    public Label lbErrorLoginExist;
    @FXML
    public Label lbErrorPass;
    @FXML
    public Label lbErrorPasswordNotEqual;

    @FXML
    public TextField txtPseudo;
    @FXML
    public Label lbErrorPass2;
    @FXML
    public Label lbErrorPseudoExist;

    private ObservableList<User> users;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        txtLogin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            lbErrorLoginExist.setVisible(false);
        });

        txtPseudo.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            lbErrorPseudoExist.setVisible(false);
        });
        txtPassword.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent->{
            lbErrorPass.setVisible(false);
            lbErrorPass2.setVisible(false);
            lbErrorPasswordNotEqual.setVisible(false);
        });


        btnInscription.setOnMouseClicked(mouseEvent -> {

            //Ajout d'un user
            //Mettre les contraintes Pseudo,Login,Mot de passe
            User myUser= new User();
            myUser.setPseudo(txtPseudo.getText());
            myUser.setLogin(txtLogin.getText());
            myUser.setPassword(txtPassword.getText());

            //Récupère la liste des user
            GluonObservableList<User> gotList = HttpRequests.getAllUser();

            gotList.setOnSucceeded(connectStateEvent -> {
                        this.users = FXCollections.observableArrayList(gotList);
                        boolean validateInscription=true;

                for (User user: users)
                {
                    //Verification que le login n'existe pas en base
                    if(user.getLogin().equals(txtLogin.getText()))
                    {
                        lbErrorLoginExist.setVisible(true);
                        validateInscription=false;
                    }

                }

                //Verification que le pseudo n'existe pas en base
                for (User user: users) {
                    if(user.getPseudo().equals(txtPseudo.getText()))
                    {
                        lbErrorPseudoExist.setVisible(true);
                        validateInscription=false;
                    }
                }

                //Verification que les deux mot de passes sont identiques
                if (!txtPassword.getText().equals(txtPassword2.getText())){
                    lbErrorPasswordNotEqual.setVisible(true);
                    validateInscription=false;
                }
                //Password correct, exemple: Azerty@1
                if(!passWordIsCorrect(txtPassword.getText())){
                    lbErrorPass.setVisible(true);
                    lbErrorPass2.setVisible(true);
                    validateInscription=false;
                }
                if(validateInscription)
                {
                    //GluonObservableObject<User> potentialConnected = HttpRequests.addUser(myUser);
                    HttpRequests.addUser(myUser);

                    WazaApplication.setScreen("userConnect");
                }
                    });

        });


    }

    //Test de validité du mot de passe
    public boolean passWordIsCorrect(String password){

        final int MIN_Uppercase=1;
        // Specifying the minimum lowercase letters in password
        final int MIN_Lowercase=1;
        // Specifying the number of digits in a password
        final int NUM_Digits=1;
        // Specify the minimum number of special case letters
        final int Special=1;
        // Count number of uppercase letters in a password
        int uppercaseCounter=0;
        // Counter lowercase letters in a password
        int lowercaseCounter=0;
        // Count digits in a password
        int digitCounter=0;
        // count special case letters in a password
        int specialCounter=0;
        //compte le nombre de caractere total
        int sum=0;

        for (int i=0; i < password.length(); i++ ) {
            char c = password.charAt(i);
            if(Character.isUpperCase(c))
                uppercaseCounter++;
            else if(Character.isLowerCase(c))
                lowercaseCounter++;
            else if(Character.isDigit(c))
                digitCounter++;
            if(c>=33&&c<=46||c==64){
                specialCounter++;
            }
            sum=uppercaseCounter+lowercaseCounter+digitCounter+specialCounter;
        }
        System.out.println("maj"+uppercaseCounter);
        System.out.println("min"+lowercaseCounter);
        System.out.println("digit"+digitCounter);
        System.out.println("special"+specialCounter);

        if (  uppercaseCounter >= MIN_Uppercase
                && lowercaseCounter >= MIN_Lowercase && digitCounter >= NUM_Digits && specialCounter >= Special
        &&sum>7) {
            return true;
        }
        return false;
    }


}


