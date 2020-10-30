package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {

    //注入redis
    @Autowired
    private JedisPool jedisPool;

    //订阅 member 的服务
    @Reference
    private MemberService memberService;

  @PostMapping("check")
    public Result check(@RequestBody Map<String, String> loginInfo, HttpServletResponse response) {
        //验证码校验,获取redis对象
        Jedis jedis = jedisPool.getResource();
        //从参数集合中获取手机号
        String telephone = loginInfo.get("telephone");
        //获取存入redis中的key,数字加上手机号
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        //去redis中根据key查询,看是否存在验证码
        String codeInRedis = jedis.get(key);
        //如果为空就直接 return 让用户重新获取验证码
        if (codeInRedis == null) {
            return new Result(false, "请重新获取验证码!");
        }
        //如果不为空,就和前端传递的验证码是否一致
        String validateCode = loginInfo.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            //如果不一致,就 return false
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //如果一致,要将验证码删除掉,防止重复提交
        //jedis.del(key);       这里要测试所以不去删除了!!!

        //验证通过后,要根据手机号判断是否为会员
      Member member = memberService.findByTelephone(telephone);
        //如果不是会员,就注册会员
      if (member == null) {
          member = new Member();
          member.setRegTime(new Date()); //注册时间
          member.setRemark("快速登陆创建"); // 备注创建类型
          member.setPhoneNumber(telephone); //用户手机号
          //添加会员
          memberService.add(member);
      }
      //创建Cookie,添加Cookie跟踪
      Cookie cookie = new Cookie("login_member_telephone", telephone);
      cookie.setMaxAge(30 * 24 * 60 * 60);//设置cookie存活时间为 三十天
      cookie.setPath("/");//当用户访问那些路径的时候就会带上这个cookie
      response.addCookie(cookie);//将cookie放到响应中去

      //提示登陆成功
      return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
