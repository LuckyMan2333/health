package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("checkgroup")
public class CheckGroupController {

    //添加注解,通过dubbo去调用消费提供者发布的业务方法
    @Reference
    private CheckGroupService checkGroupService;

    //添加检查组
    @PostMapping("add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {

        //调用业务服务
        checkGroupService.add(checkGroup, checkitemIds);
        //响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);

    }

    //分页查询
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务进行分页查询
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    //回显数据-根据id查询检查组的数据
    @GetMapping("findById")
    public Result findById(Integer checkGroupId) {
        //调用业务去根据id查询检查组的数据
        CheckGroup checkGroup = checkGroupService.findById(checkGroupId);

        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);

    }

    //回显数据-去查询所有的检查项
    @GetMapping("findAll")
    public Result findAll() {

        List<CheckItem> list = checkGroupService.findAll();

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);

    }

    //回显数据-根据检查组的id去查询对应的检查项
    @GetMapping("findCheckitemIdsByCheckgroupId")
    public Result findCheckitemIdsByCheckgroupId(Integer checkGroupId) {
        //调用服务去根据检查组的id去查询对应的检查项
        List<Integer> checkitemIds = checkGroupService.findCheckitemIdsByCheckgroupId(checkGroupId);

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitemIds);

    }

    //编辑修改数据
    @PostMapping("update")
    public Result update(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        //调用业务去修改数据
        checkGroupService.update(checkGroup, checkitemIds);

        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }



}
