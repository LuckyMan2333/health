package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckitemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("checkitem")
public class CheckitemController {


    @Reference
    private CheckitemService checkitemService;

    //查询所有的检查项
    @GetMapping("findAll")
    public Result findAll() {

        List<CheckItem> list = checkitemService.findALl();

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);

    }

    //添加检查项
    @PostMapping("add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {

        //调用服务来添加保存数据
        checkitemService.add(checkItem);

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);

    }

    //分页查询
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务层去分页查询获取分页结果
        PageResult<CheckItem> pageResult = checkitemService.findPage(queryPageBean);

        //返回给页面,查询到的 pageResult 对象
        if (pageResult != null) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
        } else {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }


    }

    //根据id删除数据
    @PostMapping("deleteById")
    public Result deleteById(Integer id) {

        //调用业务层,删除数据
        checkitemService.deleteById(id);

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);

    }

    //编辑表单,根据id去查询数据,并回显给页面
    @GetMapping("findById")
    public Result findById(Integer id) {

        //调用业务层根据id去查询
        CheckItem checkItem = checkitemService.findById(id);

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);

    }

    //更新表单
    @PostMapping("update")
    public Result update(@RequestBody CheckItem checkItem) {
        //调用业务层去更新数据
        checkitemService.update(checkItem);

        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
}
