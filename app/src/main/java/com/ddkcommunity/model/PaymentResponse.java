package com.ddkcommunity.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentResponse implements Parcelable {

    public final static Creator<PaymentResponse> CREATOR = new Creator<PaymentResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PaymentResponse createFromParcel(Parcel in) {
            return new PaymentResponse(in);
        }

        public PaymentResponse[] newArray(int size) {
            return (new PaymentResponse[size]);
        }

    };
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Payment payment;

    protected PaymentResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.payment = ((Payment) in.readValue((Payment.class.getClassLoader())));
    }

    public PaymentResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(payment);
    }

    public int describeContents() {
        return 0;
    }

}
