package com.example.felix.appalimentacio.Model;

/**
 * Created by Felix on 04/06/2017.
 */

public class ItemListCompraModel {

    private String nomRecepta;
    private String cantitat;
    private boolean itemCheck;

    public ItemListCompraModel(String nomRecepta, String cantitat, boolean itemCheck){
        this.nomRecepta = nomRecepta;
        this.cantitat=cantitat;
        this.itemCheck=itemCheck;
    }

    public ItemListCompraModel(){

    }

    public String getNomRecepta() {
        return nomRecepta;
    }

    public void setNomRecepta(String nomRecepta) {
        this.nomRecepta = nomRecepta;
    }

    public String getCantitat() {
        return cantitat;
    }

    public void setCantitat(String cantitat) {
        this.cantitat = cantitat;
    }

    public boolean isItemCheck() {
        return itemCheck;
    }

    public void setItemCheck(boolean itemCheck) {
        this.itemCheck = itemCheck;
    }
}


