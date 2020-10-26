package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    //添加注解,通过dubbo去调用提供者发布的方法
    @Reference
    private SetmealService setmealService;


    //使用log日志打印错误信息
    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    /**
     * 上传图片
     *
     * @return com.itheima.health.entity.Result
     * @Param [imgFile]
     **/
    @RequestMapping("upload")
    public Result upload(MultipartFile imgFile) {
        //获取原文件
        String originalFilename = imgFile.getOriginalFilename();
        //截取字符串,获取后缀名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID类拼接后缀名,产生唯一的文件名
        String uniqueName = UUID.randomUUID().toString() + extension;
        //调用七牛云工具类去上传文件
        //通过上传文件的字节流和唯一的文件名,上传到七牛云
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), uniqueName);
            //上传数据成功后,使用map集合返回给前端唯一的名字和七牛云的地址
            Map<String, String> resultMap = new HashMap<>();

            //上传文件的唯一名字
            resultMap.put("imgName", uniqueName);

            //使用七牛云获取地址值
            resultMap.put("domin", QiNiuUtils.DOMAIN);
            //将集合传回给前端
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, resultMap);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }


    }

    /**
     * 添加套餐
     *
     * @return com.itheima.health.entity.Result
     * @Param [setmeal, checkgroupIds]
     **/
    @PostMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        //调用业务去添加套餐
        setmealService.add(setmeal, checkgroupIds);

        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);


    }

    /**
     * 分页查询套餐
     *
     * @return com.itheima.health.entity.Result
     * @Param [queryPageBean]
     **/
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);


    }

    /**
     * 根据id去查询套餐
     *
     * @return com.itheima.health.entity.Result
     * @Param [id]
     **/
    @GetMapping("findById")
    public Result findById(Integer id) {
        //调用业务去查询套餐
        Setmeal setmeal = setmealService.findById(id);
        // 前端要显示图片需要全路径
        // 封装到map中，解决图片路径问题
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //往map集合中添加查询到的套餐
        resultMap.put("setmeal", setmeal);
        //往map集合中添加 七牛云的网址路径,方便前端数据的回显
        resultMap.put("domin", QiNiuUtils.DOMAIN);

        //将封装好的map集合,返回到前端
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);

    }


    /**
     * 调用业务层去查询于套餐关联的检查组
     *
     * @return com.itheima.health.entity.Result
     * @Param [id]
     **/
    @GetMapping("findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(Integer id) {
        //调用业务层去查询于套餐关联的检查组
        List<Integer> list = setmealService.findCheckgroupIdsBySetmealId(id);

        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);

    }


    /**
     * 修改套餐
     *
     * @return com.itheima.health.entity.Result
     * @Param [setmeal, checkgroupIds]
     **/
    @PostMapping("update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        //调用业务层去修改套餐
        setmealService.update(setmeal, checkgroupIds);

        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     *  删除套餐
     * @Param [id]
     * @return com.itheima.health.entity.Result
    **/
    @GetMapping("delete")
    public Result delete(Integer id) {

        //调用业务去删除套餐
        setmealService.delete(id);

        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);

    }
}
