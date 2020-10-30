package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    /**
     *  提交预约
     * @Param [parMap]
     * @return com.itheima.health.pojo.Order
    **/
    Order submit(Map<String, String> parMap) throws MyException;

    /**
     *  根据订单id去查询数据
     * @Param [id]
     * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> findById(Integer id);
}
