package com.thales.wazajfx.controller;

import com.gluonhq.connect.GluonObservableList;


import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.Chat;
import com.thales.wazajfx.utils.HttpRequests;
import com.thales.wazajfx.utils.NewChatSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable  {

    @FXML
    public Button btnConnect;
    @FXML
    public Button btnDeco;
    @FXML
    public ComboBox cbChat;

    @FXML
    public Label labelLogin;

    @FXML
    //public Label lbNameChat;

    private Chat myChat;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseComboBox();
        initializeButtons();
        initializeText();
    }

    private void initialiseComboBox() {
        //formate texte de la combobox
        Callback<ListView<Chat>, ListCell<Chat>> chatCellFactory =
                new Callback<ListView<Chat>, ListCell<Chat>>() {
                    @Override
                    public ListCell<Chat> call(ListView<Chat> l) {
                        return new ListCell<Chat>() {

                            @Override
                            protected void updateItem(Chat item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getName());
                                }
                            }
                        };
                    }
                };

        this.cbChat.setButtonCell(chatCellFactory.call(null));
        this.cbChat.setCellFactory(chatCellFactory);

        NewChatSingleton.getInstance().addChatProperty().addListener(observable -> {
            GluonObservableList<Chat> mylistChat = HttpRequests.getAllChat();

            mylistChat.setOnSucceeded(connectStateEvent -> {
                this.cbChat.setItems(mylistChat);

                System.out.println(mylistChat);
            });
        });

        GluonObservableList<Chat> mylistChat = HttpRequests.getAllChat();
        mylistChat.setOnSucceeded(connectStateEvent -> {
            this.cbChat.setItems(mylistChat);

        });

        cbChat.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newStr) -> {
            if (cbChat.valueProperty().getValue() != null) {
                myChat= (Chat) cbChat.valueProperty().getValue();

                WazaApplication.setMyChat(myChat);

                //hboxbBtn.setVisible(true);
                //lbNameChat.setText("** " + myChat.getName() + " **");
                //lbNameChat.setVisible(true);
            }
        });


    }

    private void initializeButtons() {

        this.btnConnect.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("chat");
        });

        this.btnDeco.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("userConnect");
        });

    }

    private void initializeText() {
        labelLogin.isVisible();
        this.labelLogin.setText("****");
        WazaApplication.connectedUserProperty().addListener((observableValue, user, t1) -> {
            this.labelLogin.setText("Bienvenue "+WazaApplication.getConnectedUser().getLogin());
        });
        this.labelLogin.setText("Bienvenue "+WazaApplication.getConnectedUser().getLogin());
    }

}
