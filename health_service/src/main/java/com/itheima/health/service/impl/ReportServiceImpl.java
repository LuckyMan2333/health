package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    //注入会员的dao
    @Autowired
    private MemberDao memberDao;

    //注入订单的dao
    @Autowired
    private OrderDao orderDao;


    /**
     *  获取运营数据统计
     * @Param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public Map<String, Object> getBusinessReportData() {
        //创建前端需要的参数Map集合
        Map<String, Object> reportData = new HashMap<>();

        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //格式化当前系统时间
        String today = sdf.format(new Date());

        //reportDate 当前系统时间
        String reportDate = today;

        //获取本周一的日期,并格式化
        String thisWeekMonday = sdf.format(DateUtils.getThisWeekMonday());
        
        //获取本周日的日期,并格式化
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());

        //获取本月的一号 并格式化
        String firstDay4ThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());

        //本月最后一天 并格式化
        String lastDayOfThisMonth = sdf.format(DateUtils.getListDay4ThisMonth());


        //=============  会员数量 ============================
        //todayNewMember 今日新增会员
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //totalMember   会员总数
        Integer totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember     本周新增会员
       Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        //thisMonthNewMember    本月新增会员
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //=============  会员数量 ============================

        // ============== 订单统计 ===============
        //todayOrderNumber      今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //todayVisitsNumber
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //thisWeekOrderNumber   本周预约数
         Integer thisWeekOrderNumber = orderDao.findorderCountBetweenDate(thisWeekMonday,sunday);
        //thisWeekVisitsNumber  本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //thisMonthOrderNumber  本月预约数
        Integer thisMonthOrderNumber = orderDao.findorderCountBetweenDate(firstDay4ThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        // =============== 订单统计 ========================

        // ============= 热门套餐 ==============
        //hotSetmeal
        List<Map<String, Object>> hotSetmeal = orderDao.findHotSetmeal();

        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }
}
