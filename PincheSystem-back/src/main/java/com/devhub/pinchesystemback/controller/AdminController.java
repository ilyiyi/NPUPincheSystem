package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.service.AdminService;
import com.devhub.pinchesystemback.vo.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 根据开始结束时间或车主id查询全部拼车记录
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @param id    车主id
     * @return 拼车记录
     */
    @GetMapping("outRecord")
    public CommonResult recordsOut(Date begin, Date end, Long id) {
        if (end.before(begin)) {
            return CommonResult.failure("开始时间晚于结束时间!");
        }

        CommonResult result;

        try {
            List<Info> infos = adminService.getAllInfosByTime(begin, end, id);
            result = CommonResult.success(infos);
        } catch (Exception e) {
            e.printStackTrace();
            result = CommonResult.failure("操作异常");
        }
        return result;
    }

    @PostMapping("/rank")
    public CommonResult ownerRank(Date begin, Date end) {

        if (end.before(begin)) {
            return CommonResult.failure("开始时间晚于结束时间!");
        }

        CommonResult result;

        try {
            List<User> ownerRank = adminService.getOwnerRank(begin, end);
            result = CommonResult.success(ownerRank);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
