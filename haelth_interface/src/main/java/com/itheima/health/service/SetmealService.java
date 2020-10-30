package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id) throws MyException;

    List<Setmeal> getSetmeal();

    Setmeal findDetailById(Integer id);
}
