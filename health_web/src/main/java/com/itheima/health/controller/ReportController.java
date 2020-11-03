package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.HTTP;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    //订阅会员业务
    @Reference
    private MemberService memberService;

    //订阅套餐业务
    @Reference
    private SetmealService setmealService;

    //订阅运营数据业务
    @Reference
    private ReportService reportService;

    /**
     * 会员折线图 获取过去一年会员的注册数据
     *
     * @return com.itheima.health.entity.Result
     * @Param []
     **/
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        //创建当前日期,使用Calendar来操作日历对象
        Calendar calendar = Calendar.getInstance();
        //设置过去的一年时间
        calendar.add(Calendar.YEAR, -1);
        //创建转换日期类型对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //创建月份集合来保存过去12个月的数据
        List<String> months = new ArrayList<>();
        //设置12个月的数据
        for (int i = 0; i < 12; i++) {
            //这里的日期已经被设置为过去的一年,所以每次要将月份+1
            calendar.add(Calendar.MONTH, 1);
            //获取设置好的过去的日期
            Date time = calendar.getTime();
            //将获取到的数据保存到月份集合中,指定日期格式为 2020-01
            months.add(sdf.format(time));
        }

        //调用服务去查询过去12个月会员的数据,将设置好的过去的月份集合当作参数
        List<Integer> memberCount = memberService.getMemberReport(months);

        //将设置好的月份和获取到的会员数据,放入Map集合中 返回给前端
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("months", months);
        resultMap.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);
    }


    /**
     * 套餐预约占比饼图 获取预约套餐的比例
     *
     * @return com.itheima.health.entity.Result
     * @Param []
     **/
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //调用服务查询
        //获取预约套餐的比例集合
        List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
        //套餐名称集合
        List<String> semtealName = new ArrayList<>();
        //在预约套餐的比例集合中抽取 套餐的名称
        if (setmealCount != null) {
            //遍历预约套餐的比例集合,抽取套餐的名称
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                //取出套餐名称,放入套餐名称集合中
                semtealName.add(name);
            }
        }
        //封装预约套餐的比例集合和套餐名称集合到Map中
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("setmealNames", semtealName);
        resultMap.put("setmealCount", setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);


    }


    /**
     *  获取运营数据统计
     * @Param []
     * @return com.itheima.health.entity.Result
    **/
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        Map<String, Object> businessReport = reportService.getBusinessReportData();

        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, businessReport);

    }


    /**
     * 导出运营统计表为Excel格式
     *
     * @return
     * @Param
     **/
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //获取模板的路径
        String templatePath = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        //获取报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();

        //创建 XSSFWorkbook 模板所在路径
        try(XSSFWorkbook wk = new XSSFWorkbook(templatePath)) {
            //获取工作表
            XSSFSheet sht = wk.getSheetAt(0);
            // 日期 坐标 2,5
            sht.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());
            //======================== 会员 ===========================
            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));

            //=================== 预约 ============================
            sht.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>> )reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String)setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long)setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String)setmealMap.get("remark"));
                row++;
            }
            // 解决下载的文件名 中文乱码
            String filename = "运营统计数据报表.xlsx";
            // 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            //设置响体内容的格式
            response.setContentType("application/vnd.ms-excel");
            // 告诉浏览器，是带附件的，文件下载
            response.setHeader("Content-Disposition","attachement;filename=report_business.xlsx" );
            //响应到输出流中
            wk.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  导出运营统计表为PDF格式
     * @Param [req, res]
     * @return void
    **/
    @GetMapping("/exportBusinessReportPdf")
    public void exportBusinessReportPdf(HttpServletRequest req, HttpServletResponse res) {
        //获取模板路径
        String templatePath = req.getSession().getServletContext().getRealPath("/template");
        //获取模板 jrxml的路径
        String jrxml = templatePath + File.separator + "health_business3.jrxml";
        //获取模板 编译后的存放位置 jasper路径
        String jasper = templatePath + File.separator + "health_business3.jasper";


        try {
            //编译模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            //获取运营统计数据 里面的数据相当于 paramters 里的数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();

            //获取热门套餐
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) businessReportData.get("hotSetmeal");

            //往模板中填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, businessReportData, new JRBeanCollectionDataSource(hotSetmeal));

            //告诉浏览器导出数据的类型
            res.setContentType("application/pdf");
            //告诉浏览器要下载导出数据,并指定导出文件的名字
            res.setHeader("Content-Disposition","attachement;filename=businessReport.pdf");

            //使用request的输出流导出数据
            JasperExportManager.exportReportToPdfStream(jasperPrint, res.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}


