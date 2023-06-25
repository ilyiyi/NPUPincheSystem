package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.InfoParam;
import com.devhub.pinchesystemback.pararm.OrderReviewParam;
import com.devhub.pinchesystemback.pararm.PageParam;
import com.devhub.pinchesystemback.service.InfoService;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.CommonResult;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/owner")
@Slf4j
public class OwnerController {

    @Autowired
    private InfoService infoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 拼车信息的发布
     */
    @PostMapping("/info")
    @PreAuthorize("hasAnyRole('ROLE_OWNER')")
    @ResponseBody
    public CommonResult infoPublish(@RequestBody @Valid InfoParam infoParam) {
        try {
            User currentUser = redisUtil.getCurrentUser("cur");
            Info info = new Info();
            setInfo(infoParam, currentUser, info);
            if (infoService.publish(info)) {
                log.info("发布成功！");
                return CommonResult.success();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failure("发布失败，请稍后再试");
    }

    /**
     * 拼车信息的撤销
     */
    @DeleteMapping("/info/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER')")
    @ResponseBody
    public CommonResult infoCancel(@PathVariable("id") long id) {
        System.out.println("hh");
        if (!infoService.cancel(id)) {
            log.info("删除失败！");
        }
        log.info("删除成功！");
        new CommonResult();
        return CommonResult.success();
    }

    /**
     * 拼车信息的修改
     */
    @PutMapping("/info/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER')")
    @ResponseBody
    public void infoModify(@PathVariable long id, @RequestBody InfoParam infoParam) {
        try {
            User currentUser = redisUtil.getCurrentUser("cur");
            Info info = new Info();
            info.setInfoId(id);
            setInfo(infoParam, currentUser, info);
            if (infoService.modify(info)) {
                log.info("修改成功！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfo(InfoParam infoParam, User currentUser, Info info) throws ParseException {
        info.setOwnerId(currentUser.getId());
        info.setOwnerName(infoParam.getOwnerName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(infoParam.getDays());
        info.setDays(date);
        info.setEnding(infoParam.getEnding());
        info.setCarNum(infoParam.getCarNum());
        info.setPrice(infoParam.getPrice());
        info.setRemain(infoParam.getRemain());
        info.setRemark(infoParam.getRemark());
    }

    /**
     * 分页查看自己发布的所有拼车信息
     */
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ROLE_OWNER')")
    @ResponseBody
    public PageInfo<Info> searcherAllInfo(@RequestBody PageParam pageParam) {
        User currentUser = redisUtil.getCurrentUser("cur");
        return infoService.getInfos(currentUser.getId(), pageParam.getCurrentPage(), pageParam.getPageSize());
    }

    @GetMapping("allInfo")
    @ResponseBody
    public CommonResult listAllInfo() {
        List<Info> list = infoService.getAllInfos();
        return CommonResult.success(list);
    }


    /**
     * 根据id查看拼车信息
     *
     * @param id 拼车信息ID
     */
    @GetMapping("/info/{id}")
    @ResponseBody
    public Info infoSearch(@PathVariable long id) {
        return infoService.getInfo(id);
    }

    /**
     * 查看自己的所有订单
     */
    @GetMapping("/order")
    @ResponseBody
    public PageInfo<List<Order>> getAllOrders(@RequestBody PageParam pageParam) {
        User currentUser = redisUtil.getCurrentUser("cur");
        List<Info> infos = infoService.getInfos(currentUser.getId());
        List<Long> infoIds = new ArrayList<>();
        for (Info info : infos) {
            infoIds.add(info.getInfoId());
        }
        return orderService.selectOrders(infoIds, pageParam.getCurrentPage(), pageParam.getPageSize());
    }

    /**
     * 根据id查看订单
     *
     * @param id 订单id
     */
    @GetMapping("/order/{id}")
    @ResponseBody
    public Order getOrder(@PathVariable long id) {
        return orderService.getOrder(id);
    }

    /**
     * 审核订单
     */
    @PostMapping("/review")
    @PreAuthorize("hasAnyRole('ROLE_OWNER')")
    @ResponseBody
    private CommonResult orderReview(@RequestBody OrderReviewParam orderReviewParam) {
        orderService.reviewOrder(orderReviewParam.getOrderId(), orderReviewParam.getOrderState());
        return CommonResult.success();
    }
}
