package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllAvailableBankList {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<BankData> data = null;
    @SerializedName("image_path")
    @Expose
    public String image_path;

    public static class BankData {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("country_id")
        @Expose
        public Integer country_id;
        @SerializedName("bank_name")
        @Expose
        public String bank_name;
        @SerializedName("image")
        @Expose
        public String image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCountry_id() {
            return country_id;
        }

        public void setCountry_id(Integer country_id) {
            this.country_id = country_id;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
