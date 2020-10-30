package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettingList) throws MyException;

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    void editNumberByDate(OrderSetting orderSetting) throws MyException;
}
