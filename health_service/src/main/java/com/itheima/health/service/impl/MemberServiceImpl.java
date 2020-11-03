package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     *  通过手机号查询会员信息
     * @Param [telephone]
     * @return com.itheima.health.pojo.Member
    **/
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     *  添加会员
     * @Param [member]
     * @return void
    **/
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> getMemberReport(List<String> months) {
        //创建储存会员数据的集合
        List<Integer> memberCount = new ArrayList<>();

        //判断月份参数是否为空
        if (months != null) {
            //循环查询12个月的会员数据
            for (String month : months) {
                //查询当前月份注册的会员,并且拼接上 "-31"
                Integer count = memberDao.findMemberCountBeforeDate(month + "-31");
                //将查询到的会员数据存入到集合中
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
