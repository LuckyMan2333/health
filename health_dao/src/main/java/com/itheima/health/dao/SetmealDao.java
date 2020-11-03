package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    /**
     *  添加套餐
     * @Param [setmeal]
     * @return void
    **/
    void add(Setmeal setmeal);


    /**
     *      添加套餐和检查项的关系
     * @Param [setmealId, checkgroupId]
     * @return void
    **/
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    /**
     *  分页查询套餐
     * @Param [queryString]
     * @return com.github.pagehelper.Page<com.itheima.health.pojo.Setmeal>
    **/
    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    void update(Setmeal setmeal);

    void deleteByCheckGroup(Integer id);

    void addSetmealAndCheckGroup(@Param("setemalId") Integer setemalId,@Param("checkgroupId") Integer checkgroupId);

    int findOrderCountBySetmealId(Integer id);

    void delete(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findDetailById(Integer id);

    List<Map<String, Object>> findSetmaelCount();

}
