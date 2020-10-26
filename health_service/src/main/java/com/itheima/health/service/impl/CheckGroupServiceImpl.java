package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    //添加dao层的注解
    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组
        checkGroupDao.add(checkGroup);
        //获取检查组的id
        Integer checkGroupId = checkGroup.getId();
        //判断用户添加的检查项是否为空
        if (checkitemIds != null) {

            //遍历检查项的id,添加检查项和检查组的关系
            for (Integer checkitemId : checkitemIds) {

                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);

            }

        }


    }

    //分页查询
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //使用 PageHelper 插件工具进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //判断是否有条件,如果有就添加字符进行模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //进行拼接
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //紧接着查询,会自动进行分页查询
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());

        PageResult<CheckGroup> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;


    }

    //回显数据-根据id查询检查组的数据
    @Override
    public CheckGroup findById(Integer checkGroupId) {

        //调用dao层,根据id查询检查组的数据
        return checkGroupDao.findById(checkGroupId);

    }

    //回显数据-去查询所有的检查项
    @Override
    public List<CheckItem> findAll() {

        //调用dao,去查询所有的检查项
        return checkGroupDao.findAll();

    }

    //回显数据-根据检查组的id去查询对应的检查项
    @Override
    public List<Integer> findCheckitemIdsByCheckgroupId(Integer checkGroupId) {

        //调用dao,根据检查组的id去查询对应的检查项
        return checkGroupDao.findCheckitemIdsByCheckgroupId(checkGroupId);


    }

    //编辑修改数据
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {

        //先更新检查组
        checkGroupDao.update(checkGroup);

        //删除旧关系
        checkGroupDao.deleteCheckGroupAndCheckItem(checkGroup.getId());

            //建立新的关系
        if (checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {

                checkGroupDao.addCheckGroupAndCheckItem(checkGroup.getId(), checkitemId);

            }
        }


    }

    //删除检查组
    @Override
    @Transactional
    public void deleteById(Integer id) throws MyException{
        //先去查询,要删除的检查组是否被套餐使用,如果被使用则不能删除
        int cnt = checkGroupDao.findSetmealCountByCheckGroupId(id);

        if (cnt > 0) {
            //被套餐使用就不能删除了
            throw new MyException("此检查组已被套餐使用,不能被删除");

        }
        //没有被使用就删除检查组和检查项的关系
        checkGroupDao.deleteCheckGroupAndCheckItem(id);
        //再去删除检查组
        checkGroupDao.deleteCheckGroup(id);

    }

    /**
     *  查询所有的检查组
     * @Param []
     * @return java.util.List<com.itheima.health.pojo.CheckGroup>
    **/

    @Override
    public List<CheckGroup> findByCheckGroup() {

        return checkGroupDao.findByCheckGroup();

    }
}
