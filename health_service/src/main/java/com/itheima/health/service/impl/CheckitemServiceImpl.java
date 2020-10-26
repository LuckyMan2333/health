package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckitemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service(interfaceClass = CheckitemService.class)
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemDao checkitemDao;

    //查询所有的检查项
    @Override
    public List<CheckItem> findALl() {

        return checkitemDao.findAll();

    }

    //调用服务来添加保存数据
    @Override
    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用 PageHelper的工具   进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //使用模糊查询
        //判断是否有查询的条件
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //如果有条件的话就拼接上 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //后面要紧接着查询语句,会被分页
        Page<CheckItem> page = checkitemDao.findByCondition(queryPageBean.getQueryString());

        //封装到分页结果对象中
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());

        return pageResult;
    }

    //删除数据
    @Override
    public void deleteById(Integer id) throws MyException {
        //先判断这个检查项是否被检查组使用了
        //调用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录

        int cnt = checkitemDao.findCountByCheckitemId(id);

        //如果被检查组使用了,则不能删除
        if (cnt > 0) {
            throw new MyException(MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        //没被检查组使用就可删除
        checkitemDao.deleteById(id);

    }


    //编辑表单,根据id去查询数据,并回显给页面
    @Override
    public CheckItem findById(Integer id) {

        return checkitemDao.findById(id);

    }

    //更新表单
    @Override
    public void update(CheckItem checkItem) {

        checkitemDao.update(checkItem);

    }
}
