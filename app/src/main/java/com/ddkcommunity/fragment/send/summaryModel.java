package com.ddkcommunity.fragment.send;

public class summaryModel
{
    String headername;
    String headreramount;
    String transaction_type;

    public summaryModel(String headername, String headreramount, String transaction_type) {
        this.headername = headername;
        this.headreramount = headreramount;
        this.transaction_type = transaction_type;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getHeadername() {
        return headername;
    }

    public void setHeadername(String headername) {
        this.headername = headername;
    }

    public String getHeadreramount() {
        return headreramount;
    }

    public void setHeadreramount(String headreramount) {
        this.headreramount = headreramount;
    }
}
