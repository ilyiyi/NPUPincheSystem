package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.InfoParam;
import com.devhub.pinchesystemback.service.InfoService;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    @ResponseBody
    public void infoPublish(@RequestBody InfoParam infoParam){
        try {
            User currentUser = redisUtil.getCurrentUser("cur");
            Info info = new Info();
            setInfo(infoParam, currentUser, info);
            if(infoService.publish(info)){
                log.info("发布成功！");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 拼车信息的撤销
     */
    @DeleteMapping("/info/{id}}")
    public void infoCancel(@PathVariable long id) {
        if(infoService.cancel(id)){
            log.info("删除成功！");
        }
    }

    /**
     * 拼车信息的修改
     */
    @PutMapping("/info/modify/{id}")
    public void infoModify(@PathVariable long id, InfoParam infoParam) {
        try {
            User currentUser = redisUtil.getCurrentUser("cur");
            Info info = new Info();
            info.setInfoId(id);
            setInfo(infoParam, currentUser, info);
            if(infoService.modify(info)){
                log.info("修改成功！");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfo(InfoParam infoParam, User currentUser, Info info) {
        info.setOwnerId(currentUser.getId());
        info.setOwnerName(infoParam.getOwnerName());
        info.setDays(infoParam.getDays());
        info.setEnding(infoParam.getEnding());
        info.setCarNum(infoParam.getCarNum());
        info.setPrice(infoParam.getPrice());
        info.setRemain(infoParam.getRemain());
        info.setRemark(infoParam.getRemark());
    }

    /**
     * 查看自己发布的所有拼车信息
     */
    @GetMapping("/info")
    @ResponseBody
    public List<Info> searcherAllInfo(){
        User currentUser = redisUtil.getCurrentUser("cur");
        return infoService.getInfos(currentUser.getId());

    }

    /**
     * 根据id查看拼车信息
     * @param id 拼车信息ID
     */
    @PutMapping("/info/{id}")
    @ResponseBody
    public Info infoSearch(@PathVariable long id){
        return infoService.getInfo(id);
    }

    /**
     * 查看自己的所有订单
     */
    @GetMapping("/order")
    public void getAllOrders(){

    }

    /**
     * 根据id查看订单
     * @param id 订单id
     */
    @GetMapping("/order/{id}")
    public void getOrder(@PathVariable long id){

    }

    /**
     * 审核订单
     */
    @PostMapping("/order/review")
    private void orderReview(){

    }
}
