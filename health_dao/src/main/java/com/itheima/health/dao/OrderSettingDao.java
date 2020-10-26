package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //批量导入数据-先查看要导入的数据是否已经存在
    OrderSetting findByOrderDate(Date orderDate);

    //批量导入数据-修改导入的数据
    void updateNumber(OrderSetting orderSetting);

    //批量导入数据-导入数据
    void add(OrderSetting orderSetting);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);
}
