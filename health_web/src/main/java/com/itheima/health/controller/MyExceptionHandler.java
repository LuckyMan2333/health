package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.LogManager;

@RestControllerAdvice
public class MyExceptionHandler {
    /**
     * info:  打印日志，记录流程性的内容
     * debug: 记录一些重要的数据 id, orderId, userId
     * error: 记录异常的堆栈信息，代替e.printStackTrace();
     * 工作中不能有System.out.println(), e.printStackTrace();
     */

    LogManager logManager;
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * 自定义抛出的异常处理
     * @param
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e) {
        return new Result(false, e.getMessage());
    }


    /**
     * 所有未知的异常处理
     * @param
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleMyException(Exception e) {
        log.error("发生异常了",e);
        return new Result(false, "发生未知异常,请联系管理员");
    }
    /**
     *  无权限异常
     * @Param
     * @return
    **/
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return new Result(false, "您的权限不足!!!");
    }


}
