package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {

    //查询所有的检查项
    List<CheckItem> findALl();

    //调用服务来添加保存数据
    void add(CheckItem checkItem);
    //先判断这个检查项是否被检查组使用了
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);
    //根据id删除数据
    void deleteById(Integer id) throws MyException;

    //调用业务层根据id去查询
    CheckItem findById(Integer id);
    //更新表单
    void update(CheckItem checkItem);
}
