package com.example.td190.tesagarson.Model;

/**
 * Created by td190 on 20/07/2016.
 */
public class Category {

    private int _id;
    private String _catName;

    public Category(String _catName) {

        this._catName = _catName;
    }

    public Category (){

    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_catName(String _catName) {
        this._catName = _catName;
    }

    public int get_id() {

        return _id;
    }

    public String get_catName() {
        return _catName;
    }

}
