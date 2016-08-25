package com.example.td190.tesagarson.Model;

import android.graphics.Bitmap;

/**
 * Created by td190 on 18/07/2016.
 */
public class Tables {

    private int _id;
    private String _tableName;
    private int _floor;
    private int _tableCustNum;

    public Tables(int _id, String _tableName, int _floor, int _tableCustNum, int _tableStatus, byte[] _image) {
        this._id = _id;
        this._tableName = _tableName;
        this._floor = _floor;
        this._tableCustNum = _tableCustNum;
        this._tableStatus = _tableStatus;
        this._image = _image;
    }

    private int _tableStatus;
    private byte[] _image;

    public Tables(String tableName, int floor, int tableCustNum, int tableStatus, byte[] image) {
        this._tableName = tableName;
        this._floor = floor;
        this._tableCustNum = tableCustNum;
        this._tableStatus = tableStatus;
        this._image = image;
    }

    public Tables (){

    }

    public void set_image(byte[] _image){
        this._image = _image;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_tableName(String _tableName) {
        this._tableName = _tableName;
    }

    public void set_floor(int _floor) {
        this._floor = _floor;
    }

    public void set_tableCustNum(int _tableCustNum) {
        this._tableCustNum = _tableCustNum;
    }

    public void set_tableStatus(int _tableStatus) {
        this._tableStatus = _tableStatus;
    }

    public byte[] get_image(){
        return _image;
    }

    public int get_id() {

        return _id;
    }

    public String get_tableName() {
        return _tableName;
    }

    public int get_floor() {
        return _floor;
    }

    public int get_tableCustNum() {
        return _tableCustNum;
    }

    public int get_tableStatus() {
        return _tableStatus;
    }
}
