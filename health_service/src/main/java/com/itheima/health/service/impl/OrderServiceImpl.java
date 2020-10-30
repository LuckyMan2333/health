package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    //添加log日志
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    //注入操作预约表的dao
    @Autowired
    private OrderDao orderDao;

    //注入操作预约设置的dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    //注入操作会员表的dao
    @Autowired
    private MemberDao memberDao;

    @Override
    public Order submit(Map<String, String> parMap) throws MyException {

        //设置日期的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将前端传递的字符串日期,格式转为日期格式
        Date orderDate = null;
        try {
            orderDate = sdf.parse(parMap.get("orderDate"));
        } catch (ParseException e) {
            //e.printStackTrace();
            log.debug("日期格式错了呦!!!");
            throw new MyException("日期格式有误,请选择正确的日期呦");
        }
        //通过日期去查询,预约设置信息
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //如果查询不到预约设置的信息,则说明当前没有预约设置,直接报错
        if (orderSetting == null) {
            throw new MyException("所选的日期不能预约,请选择其他的日期呦");
        }
        //如果不为空,则要判断预约是否已满,如果预约人数已满则要报错
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            throw new MyException("所选的日期预约已满了呦,请选择其他日期");
        }

        //要判断是否重复预约了,获取前端传来的手机号
        String telephone = parMap.get("telephone");
        //根据手机号查询会员的信息
        Member member = memberDao.findByTelephone(telephone);

        //创建会员实体类,预约成功后要返回给前端显示
        Order order = new Order();
        //存入用户选择的预约日期
        order.setOrderDate(orderDate);
        //获取前端传入的用户选择的套餐id,并存如到会员实体类中
        order.setSetmealId(Integer.valueOf(parMap.get("setmealId")));

        //判断当前用户是否是会员
        if (member != null) {

            //会员存在,设置会员的id
            order.setMemberId(member.getId());

            //是会员要查询是否重复预约
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                throw new MyException("改套餐您已经预约过了,请不要重复预约呦");
            }

        } else {
            //如果会员不存在,就去添加会员,创建会员实体类
            member = new Member();
            member.setName(parMap.get("name")); //从前端获取,会员的名字
            member.setSex(parMap.get("sex")); //从前端获取,会员的性别
            member.setIdCard(parMap.get("idCard")); //从前端获取,会员的身份证号
            member.setPhoneNumber(telephone); //从前端获取,会员的手机号
            member.setRegTime(new Date()); //设置当前系统的事件
            member.setPassword("55555"); //生成一个初始的密码
            member.setRemark("由预约而注册上来的");// remark 自动注册
            //调用dao去去添加
            memberDao.add(member);
            //设置会员的id
            order.setMemberId(member.getId());
        }
        //可预约,设置预约的类型
        order.setOrderType(parMap.get("orderType"));
        //设置预约的状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加会员的预约信息
        orderDao.add(order);
        //更新已预约人数, 更新成功则返回1，数据没有变更则返回0
        int affectedCount = orderSettingDao.editReservationsByOrderDate(orderSetting);
        //判断更新预约人数是否成功
        if (affectedCount == 0) {
            throw new MyException(MessageConstant.ORDERSETTING_FAIL);
        }
        return order;
    }

    /**
     *  根据订单的id去查询数据
     * @Param [id]
     * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }

}
