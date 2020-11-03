package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    //使用注解将dao,注册到容器
    @Autowired
    private SetmealDao setmealDao;

    /**
     *  添加套餐
     * @Param [setmeal, checkgroupIds]
     * @return void
    **/
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息
        setmealDao.add(setmeal);
        //获取添加套餐的id
        Integer setmealId = setmeal.getId();

        //判断前端是否选中于检查组的关系
        if (checkgroupIds != null) {
            //添加套餐于检查组的关系
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }

    }

    /**
     *      分页查询套餐
     * @Param [queryPageBean]
     * @return com.itheima.health.entity.PageResult<com.itheima.health.pojo.Setmeal>
    **/
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //使用 PageHelper 插件工具类进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件,如果有就进行模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //进行字符拼接
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");

        }
        //进行条件查询,这个查询语句会被PageHelper 插件工具类进行分页查询
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());

        //创建结果集类,添加查询到的数据
        PageResult<Setmeal> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;

    }

    /**
     *  根据id查询套餐
     * @Param [id]
     * @return com.itheima.health.pojo.Setmeal
    **/
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     *  调用业务层去查询于套餐关联的检查组
     * @Param [id]
     * @return java.util.List<java.lang.Integer>
    **/
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }


    /**
     *  修改套餐
     * @Param [setmeal, checkgroupIds]
     * @return void
    **/
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {

        //先修改套餐
        setmealDao.update(setmeal);
        //再删除于检查组旧的关系
        setmealDao.deleteByCheckGroup(setmeal.getId());
        //再添加与检查组的新关系
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {

                setmealDao.addSetmealAndCheckGroup(setmeal.getId(), checkgroupId);

            }
        }

    }

    /**
     *  删除套餐
     * @Param [id]
     * @return void
    **/
    @Override
    public void delete(Integer id) throws MyException {
        //删除套餐前先判断是否被下过订单,如果被下了单则不能删除
        int cnt = setmealDao.findOrderCountBySetmealId(id);

        if (cnt > 0) {
            //如果被下了单则不能删除
            throw new MyException("此套餐以被下单,不能删除");
        }
        //先删除套餐与检查组的关系
        setmealDao.deleteByCheckGroup(id);

        //再删除套餐
        setmealDao.delete(id);
    }

    /**
     *  查询所有套餐
     * @Param []
     * @return java.util.List<com.itheima.health.pojo.Setmeal>
    **/
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     *  套餐的多表映射查询
     * @Param [id]
     * @return com.itheima.health.pojo.Setmeal
    **/
    @Override
    public Setmeal findDetailById(Integer id) {
        return setmealDao.findDetailById(id);
    }

    /**
     *  套餐预约占比饼图 获取预约套餐的比例
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmaelCount();
    }


}
