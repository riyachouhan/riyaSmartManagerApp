package com.ddkcommunity.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    //.......
    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("zip")
    @Expose
    private String zip;

    @SerializedName("gender_name")
    @Expose
    private String gender_name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("country_id")
    @Expose
    private String countryId;

    @SerializedName("mpin")
    @Expose
    private String mpin;

    @SerializedName("remember_token")
    @Expose
    private String rememberToken;

    @SerializedName("device_token")
    @Expose
    private String deviceToken;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("deleted")
    @Expose
    private String deleted;

    @SerializedName("device_type")
    @Expose
    private String deviceType;

    @SerializedName("fcm_id")
    @Expose
    private String fcmId;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("user_image")
    @Expose
    private String userImage;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("ddkcode")
    @Expose
    private String ddkcode;

    @SerializedName("is_login")
    @Expose
    private String isLogin;

    @SerializedName("id_proof_status")
    @Expose
    private String idProofStatus;

    @SerializedName("profile_link")
    @Expose
    private String profileLink;

    @SerializedName("unique_code")
    @Expose
    public String unique_code;

    @SerializedName("phone_code")
    @Expose
    private String phoneCode;

    @SerializedName("card_link")
    @Expose
    private String cardLink;

    @SerializedName("google_authentication")
    @Expose
    private String google_authentication;

    @SerializedName("google_auth_secret")
    @Expose
    private String google_auth_secret;

    @SerializedName("gauth_status")
    @Expose
    private String gauth_status;

    @SerializedName("country")
    @Expose
    public List<CountryData> country;

    @SerializedName("user_alternate_info")
    @Expose
    private UserAlternateInfo userAlternateInfo;

    public UserAlternateInfo getUserAlternateInfo() {
        return userAlternateInfo;
    }

    public void setUserAlternateInfo(UserAlternateInfo userAlternateInfo) {
        this.userAlternateInfo = userAlternateInfo;
    }

    public class UserAlternateInfo implements Serializable
    {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("alt_email")
        @Expose
        private String altEmail;
        @SerializedName("alt_name")
        @Expose
        private String altName;
        @SerializedName("alt_contact_number")
        @Expose
        private String altContactNumber;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = -6281726844578791146L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getAltEmail() {
            return altEmail;
        }

        public void setAltEmail(String altEmail) {
            this.altEmail = altEmail;
        }

        public String getAltName() {
            return altName;
        }

        public void setAltName(String altName) {
            this.altName = altName;
        }

        public String getAltContactNumber() {
            return altContactNumber;
        }

        public void setAltContactNumber(String altContactNumber) {
            this.altContactNumber = altContactNumber;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class CountryData {
        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("sortname")
        @Expose
        public String sortname;

        @SerializedName("phonecode")
        @Expose
        public String phonecode;

        @SerializedName("country")
        @Expose
        public String country;

    }

    public User() {
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getGender_name() {
        return gender_name;
    }

    public void setGender_name(String gender_name) {
        this.gender_name = gender_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGauth_status() {
        return gauth_status;
    }

    public void setGauth_status(String gauth_status) {
        this.gauth_status = gauth_status;
    }

    public User(Integer id, String name, String email, String userImage, String isLogin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userImage = userImage;
        this.isLogin = isLogin;
    }

    public Integer getId() {
        return id;
    }

    public String getGoogle_authentication() {
        return google_authentication;
    }

    public void setGoogle_authentication(String google_authentication) {
        this.google_authentication = google_authentication;
    }

    public String getGoogle_auth_secret() {
        return google_auth_secret;
    }

    public void setGoogle_auth_secret(String google_auth_secret) {
        this.google_auth_secret = google_auth_secret;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDdkcode() {
        return ddkcode;
    }

    public void setDdkcode(String ddkcode) {
        this.ddkcode = ddkcode;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getIdProofStatus() {
        return idProofStatus;
    }

    public void setIdProofStatus(String idProofStatus) {
        this.idProofStatus = idProofStatus;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCardLink() {
        return cardLink;
    }

    public void setCardLink(String cardLink) {
        this.cardLink = cardLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(email);
        dest.writeValue(password);
        dest.writeValue(mobile);
        dest.writeValue(countryId);
        dest.writeValue(mpin);
        dest.writeValue(rememberToken);
        dest.writeValue(deviceToken);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(status);
        dest.writeValue(deleted);
        dest.writeValue(deviceType);
        dest.writeValue(fcmId);
        dest.writeValue(designation);
        dest.writeValue(userImage);
        dest.writeValue(ddkcode);
        dest.writeValue(isLogin);
        dest.writeValue(idProofStatus);
        dest.writeValue(profileLink);
        dest.writeValue(phoneCode);
        dest.writeValue(cardLink);
        dest.writeValue(userId);
    }
    public int describeContents() {
        return 0;
    }*/

}
