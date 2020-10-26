package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    //添加检查组
    void add(CheckGroup checkGroup);
    //添加检查组和检查项的关系
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    //分页查询
    Page<CheckGroup> findByCondition(String queryString);


    CheckGroup findById(Integer checkGroupId);

    List<CheckItem> findAll();

    List<Integer> findCheckitemIdsByCheckgroupId(Integer checkGroupId);

    void update(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItem(Integer id);

    void addCheckGroupAndCheckItem(@Param("checkgroupId") Integer id, @Param("checkitemId") Integer checkitemId);

    int findSetmealCountByCheckGroupId(Integer id);

    void deleteCheckGroup(Integer id);

    List<CheckGroup> findByCheckGroup();
}
