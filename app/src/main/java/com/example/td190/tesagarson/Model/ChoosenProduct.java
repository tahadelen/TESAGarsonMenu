package com.example.td190.tesagarson.Model;

/**
 * Created by td190 on 26/07/2016.
 */
public class ChoosenProduct {
    private Products product;
    private int piece;
    private double portion;

    public ChoosenProduct(){

    }

    public ChoosenProduct(Products product, int piece, double portion) {
        this.product = product;
        this.piece = piece;
        this.portion = portion;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }
}
