package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Member;

import java.util.List;

public interface MemberDao {
     /**
      * 查询所有会员
      * @Param []
      * @return java.util.List<com.itheima.health.pojo.Member>
     **/
    List<Member> findAll();
    Page<Member> selectByCondition(String queryString);
    /**
     *  添加会员
     * @Param [member]
     * @return void
    **/
    void add(Member member);
    void deleteById(Integer id);
    Member findById(Integer id);
    /**
     *  通过手机号码查询
     * @Param [telephone]
     * @return com.itheima.health.pojo.Member
    **/
    Member findByTelephone(String telephone);
    void edit(Member member);


    Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountByDate(String date);
    Integer findMemberCountAfterDate(String date);
    Integer findMemberTotalCount();

}
