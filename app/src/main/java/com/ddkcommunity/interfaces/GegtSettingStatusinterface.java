package com.ddkcommunity.interfaces;

import com.ddkcommunity.model.getSettingModel;

import org.json.JSONObject;

import retrofit2.Response;

public interface GegtSettingStatusinterface
{
    public void getResponse(Response<getSettingModel> response);
}
