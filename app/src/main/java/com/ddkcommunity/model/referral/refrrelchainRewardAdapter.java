package com.ddkcommunity.model.referral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class refrrelchainRewardAdapter implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = -4474516099102627109L;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Country implements Serializable
    {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sortname")
        @Expose
        private String sortname;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("phonecode")
        @Expose
        private String phonecode;
        private final static long serialVersionUID = -1775210681043072059L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPhonecode() {
            return phonecode;
        }

        public void setPhonecode(String phonecode) {
            this.phonecode = phonecode;
        }

    }

    public class Datum implements Serializable
    {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("unique_code")
        @Expose
        private String uniqueCode;
        @SerializedName("refferallist")
        @Expose
        private List<Refferallist> refferallist = null;
        @SerializedName("total_reward")
        @Expose
        private Double totalReward;
        private final static long serialVersionUID = -8023960104671339843L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }

        public List<Refferallist> getRefferallist() {
            return refferallist;
        }

        public void setRefferallist(List<Refferallist> refferallist) {
            this.refferallist = refferallist;
        }

        public Double getTotalReward() {
            return totalReward;
        }

        public void setTotalReward(Double totalReward) {
            this.totalReward = totalReward;
        }

    }

    public class Refferallist implements Serializable
    {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("country")
        @Expose
        private Country country;
        @SerializedName("registration_date")
        @Expose
        private String registrationDate;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("level")
        @Expose
        private String level;
        private final static long serialVersionUID = -6718771669403253347L;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }

        public void setRegistrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

    }
}
