package com.thales.wazajfx.utils;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.wazajfx.model.*;

import java.util.ArrayList;
import java.util.List;


public class HttpRequests {
    public static GluonObservableObject<User> tryLogin(User login) {

        //Comprendre ce que fait ce boût de code
        //Comprendre le fonctionnement de ces URL
        //Bien voir Get et Post
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/waza/api/users/connect")
                .connectTimeout(10000)
                .readTimeout(1000)
                .dataString(JsonUtils.getStringJson(login))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }


    public static GluonObservableObject<User> addUser(User myUser) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/waza/api/users/")
                .connectTimeout(10000)
                .readTimeout(1000)
                .dataString(JsonUtils.getStringJson(myUser))
                .contentType("application/json");
        //retourne l'objet user de la méthode addUser de WazaBack
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    public static void deleteUser(String id) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8080/waza/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(1000);
        DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }


    //A transformer + tard par getAllChatByUser
    public static GluonObservableList<Chat> getAllChat() {
        RestClient chat= RestClient.create()
                .method("GET")
                .host("http://localhost:8080/waza/api/chats")
                .connectTimeout(10000)
                .readTimeout(1000);
        return DataProvider.retrieveList(chat.createListDataReader(Chat.class));

    }

//deboguer avec Erwan + tard
    public static GluonObservableList<User> getAllUserByChat(int chatId) {
        RestClient user = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/waza/api/users/find/"+ chatId)
                .connectTimeout(10000)
                .readTimeout(1000);
        return DataProvider.retrieveList(user.createListDataReader(User.class));
    }

    //Voir si ça marche (FM)

    public static GluonObservableList<User> getAllUser() {
        RestClient user = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/waza/api/users/")
                .connectTimeout(10000)
                .readTimeout(1000);
        System.out.println("getAllUserMethode");
        return DataProvider.retrieveList(user.createListDataReader(User.class));
    }


    public static GluonObservableObject<Chat> getAllMessageByChat(int chatId) {
        RestClient chat = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/waza/api/chats/"+ chatId)
                .connectTimeout(10000)
                .readTimeout(1000);
        return DataProvider.retrieveObject(chat.createObjectDataReader(Chat.class));

    }


}
