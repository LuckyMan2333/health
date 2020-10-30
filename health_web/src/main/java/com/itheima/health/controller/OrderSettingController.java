package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入预约数据
     *
     * @return com.itheima.health.entity.Result
     * @Param [excelFile]
     **/
    @PostMapping("upload")
    public Result upload(MultipartFile excelFile) throws IOException, ParseException {
        try {
            //读取Excel表的内容,读取为字符串数组集合
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //将获取的字符数组集合转为javabean集合
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //设置日期的解析格式
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            //创建javabean对象
            OrderSetting os = null;
            //遍历字符数组集合
            for (String[] dataArr : strings) {
                //将Excel的日期解析成字符串
                Date orderDate = sdf.parse(dataArr[0]);
                //将Excel的数字解析成int类型
                Integer number = Integer.valueOf(dataArr[1]);
                //将解析好的数据放入javabean中
                os = new OrderSetting(orderDate, number);
                //将javabean对象放入集合中
                orderSettingList.add(os);
            }
            //调用业务去添加数据
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 通过日期来查询预约信息
     *
     * @return
     * @Param
     **/

    @GetMapping("getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month) {
        //调用服务段查询
        List<Map<String, Integer>> data = orderSettingService.getOrderSettingByMonth(month);

        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, data);


    }


    /**
     *  修改预约的最大数量
     * @Param [orderSetting]
     * @return com.itheima.health.entity.Result
    **/
    @PostMapping("editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        //调用业务去修改
        orderSettingService.editNumberByDate(orderSetting);

        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);

    }
}


