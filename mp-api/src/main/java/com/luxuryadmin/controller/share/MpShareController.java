package com.luxuryadmin.controller.share;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.invite.ParamUserInviteList;
import com.luxuryadmin.service.MpUserInviteService;
import com.luxuryadmin.service.ShareService;
import com.luxuryadmin.vo.invite.VOUserInviteList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 分享 controller
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/share")
@Api(tags = "MP.分享", description = "分享")
public class MpShareController extends BaseController {


    @Autowired
    private ShareService shareService;

    @GetMapping("/getShareQRCode")
    @ApiOperation(value = "获取分享二维码", httpMethod = "GET")
    public BaseResult getShareQRCode() {
        String shareQRCode = shareService.getShareQRCode();
        return BaseResult.okResult("data:image/png;base64,"+shareQRCode);
    }
}
