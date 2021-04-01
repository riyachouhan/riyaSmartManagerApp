package com.ddkcommunity.interfaces;

import java.math.BigDecimal;

public interface GetBTCUSDTETHPriceCallback {
    public void getValues(BigDecimal btc, BigDecimal eth, BigDecimal usdt,BigDecimal tron);
}
