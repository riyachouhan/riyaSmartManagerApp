package com.ddkcommunity.interfaces;

import com.ddkcommunity.model.wallet.WalletResponse;

public interface GetAvailableValue {
    public void getValues(String ddk, WalletResponse successResponse);
}
