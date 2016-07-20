package com.example.td190.tesagarson;

/**
 * Created by td190 on 19/07/2016.
 */
public class Users {

    private int _id;
    private String _name;
    private String _password;

    public Users (){
    }

    public Users(String name, String password) {
        this._name = name;
        this._password = password;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_password() {
        return _password;
    }

    public void set_name(String name) {
        this._name = name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_password(String password) {
        this._password = password;
    }

}
