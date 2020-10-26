package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckitemDao {

    //查询所有的检查项
    List<CheckItem> findAll();

    //调用服务来添加保存数据
    void add(CheckItem checkItem);

    //分页查询
    Page<CheckItem> findByCondition(String queryString);

    //删除数据前先查询 检查组是否使用
    int findCountByCheckitemId(Integer id);

    //根据id删除检查项
    void deleteById(Integer id) throws MyException;

    //编辑表单,根据id去查询数据,并回显给页面
    CheckItem findById(Integer id);

    //更新表单
    void update(CheckItem checkItem);
}
