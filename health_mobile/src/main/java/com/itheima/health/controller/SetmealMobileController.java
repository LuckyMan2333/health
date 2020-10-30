package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有的套餐
     *
     * @return com.itheima.health.entity.Result
     * @Param []
     **/
    @GetMapping("getSetmeal")
    public Result getSetmeal() {
        List<Setmeal> list = setmealService.getSetmeal();
        list.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
    }

    /**
     * 套餐的多表映射查询
     *
     * @return com.itheima.health.entity.Result
     * @Param [id]
     **/
    @GetMapping("findDetailById")
    public Result findDetailById(Integer id) {
        //调用业务去多表映射查询
        Setmeal setmeal = setmealService.findDetailById(id);
        //补全图片路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    /**
     *  查询套餐的基本信息
     * @Param [id]
     * @return com.itheima.health.entity.Result
    **/
    @GetMapping("findById")
    public Result findById(Integer id) {
        //调用业务,去根据套餐id去查询套餐的基本信息
        Setmeal setmeal = setmealService.findById(id);
        //补全套餐的图片路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
