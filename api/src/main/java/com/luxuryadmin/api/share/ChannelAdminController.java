package com.luxuryadmin.api.share;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpChannel;
import com.luxuryadmin.param.op.ParamOpChannel;
import com.luxuryadmin.service.op.OpChannelService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.shp.VoCountInviteShop;
import com.luxuryadmin.vo.shp.VoInviteShop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 渠道商邀请好友后台;免登陆
 *
 * @author monkey king
 * @date 2020-01-13 17:07:36
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/channel", method = RequestMethod.GET)
@Api(tags = {"Z001.【分享】模块"}, description = "/shop/share | 外部分享,不需要登录")
public class ChannelAdminController extends ProProductBaseController {

    @Autowired
    private OpChannelService opChannelService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpShopService shpShopService;


    /**
     * 获取渠道邀请详情<br/>
     * 通过渠道邀请过来的用户
     *
     * @return Result
     */
    @ApiOperation(
            value = "获取渠道邀请详情",
            notes = "获取渠道邀请详情",
            httpMethod = "GET")
    @GetMapping("/listInviteUser")
    public BaseResult<VoCountInviteShop> listInviteUser(@Valid ParamOpChannel paramOpChannel, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String outsideCode = paramOpChannel.getOutsideCode();
        if (LocalUtils.isEmptyAndNull(outsideCode)) {
            return BaseResult.okResultNoData();
        }
        //查找渠道
        OpChannel channel = opChannelService.getOpChannelByOutsideCode(outsideCode);
        if (LocalUtils.isEmptyAndNull(channel)) {
            return BaseResult.okResultNoData();
        }

        //查找渠道下注册的用户
        Integer channelId = channel.getId();
        List<Integer> userIdList = shpUserService.listUserIdByOpChannelId(channelId);
        if (LocalUtils.isEmptyAndNull(userIdList)) {
            return BaseResult.okResultNoData();
        }

        //查找被邀请人的信息(包括店铺信息)
        PageHelper.startPage(getPageNum(), PAGE_SIZE_30);
        List<VoInviteShop> listInviteShop = shpShopService.listInviteShop(userIdList.toArray(),null);

        //统计邀请的人的开店情况
        List<Map<String, Object>> listMap = shpShopService.countInviteShop(userIdList.toArray(),null);
        int vipShop = 0, tryShop = 0, expiredShop = 0;

        if (!LocalUtils.isEmptyAndNull(listMap)) {
            for (Map<String, Object> map : listMap) {
                String memberState = map.get("memberState") + "";
                Object numObj = map.get("num");
                int num = LocalUtils.isEmptyAndNull(numObj) ? 0 : Integer.parseInt(numObj.toString());
                switch (memberState) {
                    case "3":
                    case "2":
                        vipShop += num;
                        break;
                    case "1":
                        tryShop += num;
                        break;
                    case "0":
                        expiredShop += num;
                        break;
                    default:
                        break;
                }
            }
        }
        VoCountInviteShop inviteShop = new VoCountInviteShop();
        inviteShop.setTotalInviteUser(userIdList.size());
        inviteShop.setVipShop(vipShop);
        inviteShop.setTryShop(tryShop);
        inviteShop.setExpiredShop(expiredShop);
        inviteShop.setListInviteShop(listInviteShop);
        return BaseResult.okResult(inviteShop);
    }

}