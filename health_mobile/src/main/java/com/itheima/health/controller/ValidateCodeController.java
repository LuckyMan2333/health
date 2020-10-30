package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {

    //添加log日志
    private static final Logger log = LoggerFactory.getLogger(ValidateCodeUtils.class);

    //将redis注册到容器
    @Autowired
    private JedisPool jedisPool;

    /**
     *  发送手机验证码
     * @Param [telephone]
     * @return com.itheima.health.entity.Result
    **/
    @PostMapping("send4Order")
    public Result send4Order(String telephone) {
        //生成验证码之前要先去redis中查看是否已经发送过验证码
        //获取冲redis池中获取redis
        Jedis jedis = jedisPool.getResource();
        //通过redis获取key查看是否有值,key我为手机号加上指定的数据值
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //通过key获取redis中的值
        String CodeInRedis = jedis.get(key);
        //判断是否存在
        if (CodeInRedis == null) {
            //不存在,使用工具类生成验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
            try {
                //调用工具类发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                log.debug("验证码发送成功 手机号码为:{} 验证码为:{}", telephone, code);
                //发送成功要存入redis数据库中
                jedis.setex(key, 24 * 60 * 60, code);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                log.error("发送验证码失败", e);
                return new Result(false, "验证码发送失败");
            }


        } else {
            //验证码存在,直接返回false
            return new Result(false, "验证码已经存在!!!");
        }


    }

}
