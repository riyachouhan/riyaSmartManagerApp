package com.ddkcommunity.model;

import android.graphics.drawable.Drawable;

public class WalletHistory {
    public String amount="";
    public Drawable drawable;
    public String orderNumber="";
    public String dateTime="";
    public boolean minus=false;

    public WalletHistory(String orderNumber, String amount, String dateTime, boolean minus, Drawable drawable){
        this.orderNumber=orderNumber;
        this.drawable=drawable;
        this.amount=amount;
        this.dateTime=dateTime;
        this.minus=minus;
    }
}
