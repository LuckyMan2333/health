package com.itheima.health.controller;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Description: 全局异常处理
 * Advice: 通知
 * User: Eric
 * ExceptionHandler 获取的异常 异常的范围从小到大，类似于try catch中的catch
 * 与前端约定好的，返回的都是json数据
 */
@RestControllerAdvice
public class HealExceptionAdvice {
    /**
     * info:  打印日志，记录流程性的内容
     * debug: 记录一些重要的数据 id, orderId, userId
     * error: 记录异常的堆栈信息，代替e.printStackTrace();
     * 工作中不能有System.out.println(), e.printStackTrace();
     */
    private static final Logger log = LoggerFactory.getLogger(HealthException.class);

    /**
     * 自定义抛出的异常处理
     *
     * @param he
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealExceptionAdvice(HealthException he) {
        // 我们自己抛出的异常，把异常信息包装下返回即可
        return new Result(false, he.getMessage());
    }


    /**
     * 所有未知的异常处理
     * @param he
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception he){
        log.error("发生异常",he);
        return new Result(false, "发生未知错误，操作失败，请联系管理员");
    }

}
