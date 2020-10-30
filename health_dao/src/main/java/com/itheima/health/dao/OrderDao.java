package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    /**
     *  添加订单
     * @Param [order]
     * @return void
    **/
    void add(Order order);
    /**
     *  条件查询
     * @Param [order]
     * @return java.util.List<com.itheima.health.pojo.Order>
    **/
    List<Order> findByCondition(Order order);

    /**
     *  获取订单的详细信息
     * @Param [id]
     * @return java.util.Map
    **/
    Map findById4Detail(Integer id);


    Integer findOrderCountByDate(String date);
    Integer findOrderCountAfterDate(String date);
    Integer findVisitsCountByDate(String date);
    Integer findVisitsCountAfterDate(String date);

    /**
     *  获取热门套餐
     * @Param []
     * @return java.util.List<java.util.Map>
    **/
    List<Map> findHotSetmeal();

    /**
     *  根据订单id去查询数据
     * @Param [id]
     * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> findById(Integer id);
}
