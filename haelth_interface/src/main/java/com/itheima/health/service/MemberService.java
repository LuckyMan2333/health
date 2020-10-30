package com.itheima.health.service;

import com.itheima.health.pojo.Member;

public interface MemberService {
    /**
     *  通过手机号码查询会员信息
     * @Param [telephone]
     * @return com.itheima.health.pojo.Member
    **/
    Member findByTelephone(String telephone);

    /**
     *  添加会员
     * @Param [member]
     * @return void
    **/
    void add(Member member);
}
