package com.example.den.converterlab.models;

/**
 * Created by Den on 13.09.15.
 */
public class Currenci {
    private String ask;
    private String bid;
    private String idCurrency;
    private String nameCurrency;
    private int askFluc;
    private int bidFluc;

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(String idCurrency) {
        this.idCurrency = idCurrency;
    }

    public int getAskFluc() {
        return askFluc;
    }

    public void setAskFluc(int askFluc) {
        this.askFluc = askFluc;
    }

    public int getBidFluc() {
        return bidFluc;
    }

    public void setBidFluc(int bidFluc) {
        this.bidFluc = bidFluc;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }
}
