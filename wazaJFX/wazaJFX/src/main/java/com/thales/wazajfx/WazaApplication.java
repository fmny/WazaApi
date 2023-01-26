package com.thales.wazajfx;


import com.thales.wazajfx.model.Chat;
import com.thales.wazajfx.model.User;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;



public class WazaApplication extends Application {

    public static AnchorPane root;
    public static Map<String, Node> screens = new HashMap<>();
    private static String currentScreen = "userConnect";
    public static int APPWIDTH = 1050;
    public static int APPLENGHT = 595;

    //private static User connectedUser;

    private static ObjectProperty<User> connectedUser= new SimpleObjectProperty<User>();
    private static  ObjectProperty<Chat> myChat= new SimpleObjectProperty<Chat>();


    public static User getConnectedUser() {
        return connectedUser.get();
    }

    public static ObjectProperty<User> connectedUserProperty() {
        return connectedUser;
    }

    public static void setConnectedUser(User connectedUser) {
        WazaApplication.connectedUser.set(connectedUser);
    }

    public static void setMyChat(Chat myChat) {
        WazaApplication.myChat.set(myChat);

    }

    public static Chat getMyChat() {
        return myChat.get();
    }

    public static ObjectProperty<Chat>  connectedChatProperty() {
            return myChat;
    }


    @Override
    public void start(Stage stage) throws IOException {
        root = (AnchorPane) FXMLLoader.load(getClass().getResource("root.fxml"));
        screens.put("userConnect",(BorderPane) FXMLLoader.load(getClass().getResource("userConnect.fxml")));

        root.getChildren().add(screens.get(currentScreen));

        Scene scene = new Scene(root, APPWIDTH, APPLENGHT);
        stage.setTitle("Waza");
        stage.setScene(scene);
        stage.show();
    }
    public static void setScreen(String screen){

        if (!screens.keySet().contains(screen)) {
            try {
                screens.put(screen, FXMLLoader.load(WazaApplication.class.getResource(screen + ".fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        root.getChildren().remove(screens.get(currentScreen));
        root.getChildren().add(screens.get(screen));
        currentScreen = screen;
    }

    public static void main(String[] args) {
        launch();
    }
}

