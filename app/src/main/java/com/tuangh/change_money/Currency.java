package com.tuangh.change_money;

public class Currency {
    private String nameCountry;
    private String code;

    @Override
    public String toString() {
        return nameCountry + " "+ "("+code+")";
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Currency() {
    }

    public Currency(String nameCountry, String code) {
        this.nameCountry = nameCountry;
        this.code = code;
    }
}
