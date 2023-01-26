package com.thales.wazajfx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
public class Chat {

    private int id;

    private String name;

    private List<Message> messages=new ArrayList<>();

    private List<User> users=new ArrayList<>();

}
