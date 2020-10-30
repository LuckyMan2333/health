package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    //将redis注册到容器中
    @Autowired
    private JedisPool jedisPool;

    //使用dubbo,去订阅调用服务提供者
    @Reference
    private OrderService orderService;

    /**
     * 提交 预约 验证码校验
     *
     * @return
     * @Param
     **/
    @PostMapping("submit")
    public Result submit(@RequestBody Map<String, String> parMap) {
        //验证码校验,获取redis对象
        Jedis jedis = jedisPool.getResource();
        //从参数集合中获取手机号
        String telephone = parMap.get("telephone");
        //获取存入redis中的key,数字加上手机号
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //去redis中根据key查询,看是否存在验证码
        String codeInRedis = jedis.get(key);
        //如果为空就直接 return 让用户重新获取验证码
        if (codeInRedis == null) {
            return new Result(false, "请重新获取验证码!");
        }
        //如果不为空,就和前端传递的验证码是否一致
        String validateCode = parMap.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            //如果不一致,就 return false
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //如果一致,要将验证码删除掉,防止重复提交
        //jedis.del(key);       这里要测试所以不去删除了!!!
        //设置预约的类型,这里是从微信供公众号中来的,可以是写死为微信
        parMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        //预约成功后要返回 Order 对象 页面展示时要用到
        Order order = orderService.submit(parMap);

        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS, order);

    }

    /**
     *  预约成功后使用订单的id去查询数据
     * @Param [id]
     * @return com.itheima.health.entity.Result
    **/
    @GetMapping("findById")
    public Result findById(Integer id) {
        //调用业务去使用订单id去查询数据
        Map<String, Object> orderInfo = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderInfo);
    }


    /**
     *  发送登陆手机验证码
     * @Param [telephone]
     * @return com.itheima.health.entity.Result
    **/
    @PostMapping("send4Login")
    public Result send4Order(String telephone) {
        //生成验证码之前要先去redis中查看是否已经发送过验证码
        //获取冲redis池中获取redis
        Jedis jedis = jedisPool.getResource();
        //通过redis获取key查看是否有值,key我为手机号加上指定的数据值
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        //通过key获取redis中的值
        String CodeInRedis = jedis.get(key);
        //判断是否存在
        if (CodeInRedis == null) {
            //不存在,使用工具类生成验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
            try {
                //调用工具类发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                //发送成功要存入redis数据库中
                jedis.setex(key, 24 * 60 * 60, code);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                return new Result(false, "验证码发送失败");
            }
        } else {
            //验证码存在,直接返回false
            return new Result(false, "验证码已经存在!!!");
        }


    }



}
