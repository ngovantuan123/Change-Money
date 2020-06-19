package com.tuangh.change_money;

import java.util.ArrayList;

public interface CurrencyListener {
    void setTyGia(Double tyGia);
    void data(ArrayList<Currency> data);
}
