package com.thales.wazajfx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    private int id;

    @NonNull
    private String pseudo;

    @NonNull
    private String login;

    @NonNull
    private String password;

    @JsonIgnore
    private List<Message> messagesWrote =new ArrayList<>();

   /* @JsonIgnore
    private List<Message> messagesReceived=new ArrayList<>();*/

    private List<Chat> chats=new ArrayList<>();

    public User(@NonNull String login, @NonNull String password) {
        this.login = login;
        this.password = password;
    }


}
