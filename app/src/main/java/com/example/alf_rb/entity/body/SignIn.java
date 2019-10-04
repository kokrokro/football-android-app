package com.example.alf_rb.entity.body;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SignIn {

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

}
