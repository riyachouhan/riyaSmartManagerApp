package com.ddkcommunity.model;

public class BankAccounts {
    public String bankAcNumber = "";
    public String bankHolderName = "";
    public String mobileNumber = "";

    public BankAccounts(String bankHolderName, String bankAcNumber, String mobileNumber) {
        this.bankAcNumber = bankAcNumber;
        this.bankHolderName = bankHolderName;
        this.mobileNumber = mobileNumber;
    }
}
