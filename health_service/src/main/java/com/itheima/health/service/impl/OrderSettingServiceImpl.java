package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    //使用注解将dao注册进容器
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     *  批量导入预约数据
     * @Param [orderSettingList]
     * @return void
    **/
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) throws MyException {

        //先遍历Excel表,去查询要导入的数据是否已经存在
        for (OrderSetting orderSetting : orderSettingList) {
            OrderSetting orderInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

            //如果要导入的数据已经存在,就判断最大的预约是是否小于已经预约数,如果小于就直接报错
            if (orderInDB != null) {
                //判断最大的预约是是否小于已经预约数,如果小于就直接报错
                if (orderInDB.getNumber() < orderInDB.getReservations()) {
                    //报错
                    throw new MyException("最大预约数,不能小于已经预约数!!!");
                }

                //如果最大的预约是是否大于已经预约数,就去可以去修改数据
                orderSettingDao.updateNumber(orderSetting);
            } else {
                //如果要导入的数据不存在,就去添加
                orderSettingDao.add(orderSetting);
            }


        }

    }

    /**
     *  根据日期去查询预约的数据
     * @Param [month]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Integer>>
    **/
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {

        month += "%";

        return orderSettingDao.getOrderSettingByMonth(month);


    }


}
