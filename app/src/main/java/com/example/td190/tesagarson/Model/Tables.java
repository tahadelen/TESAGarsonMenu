package com.example.td190.tesagarson.Model;

/**
 * Created by td190 on 18/07/2016.
 */
public class Tables {

    private int _id;
    private String _tableName;
    private int _floor;
    private int _tableCustNum;
    private int _tableStatus;

    public Tables(String tableName, int floor, int tableCustNum, int tableStatus) {
        this._tableName = tableName;
        this._floor = floor;
        this._tableCustNum = tableCustNum;
        this._tableStatus = tableStatus;
    }

    public Tables (){

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
