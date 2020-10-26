package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer checkGroupId);

    List<CheckItem> findAll();

    List<Integer> findCheckitemIdsByCheckgroupId(Integer checkgroupId);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(Integer id) throws MyException;

    List<CheckGroup> findByCheckGroup();
}
