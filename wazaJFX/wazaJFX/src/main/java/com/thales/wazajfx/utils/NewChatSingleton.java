package com.thales.wazajfx.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class NewChatSingleton {

    private static NewChatSingleton INSTANCE;

    private static BooleanProperty addChat = new SimpleBooleanProperty();

    private NewChatSingleton(){}

    public static NewChatSingleton getInstance(){
        if (INSTANCE ==null){
            INSTANCE = new NewChatSingleton();
        }
        return INSTANCE;
    }

    public boolean isAddChat() {
        return addChat.get();
    }

    public BooleanProperty addChatProperty() {
        return addChat;
    }

    public void setAddChat(boolean addChat) {
        NewChatSingleton.addChat.set(addChat);
    }
}
