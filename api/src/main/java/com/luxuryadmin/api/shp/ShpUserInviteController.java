package com.luxuryadmin.api.shp;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamCountInvite;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserInviteService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.shp.VoCountInviteShop;
import com.luxuryadmin.vo.shp.VoInviteShop;
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
import java.util.Map;

/**
 * 用户邀请好友
 *
 * @author monkey king
 * @date 2021-04-13 18:52:49
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user")
@Api(tags = {"Z002.【邀请】模块2.5.1"})
public class ShpUserInviteController extends BaseController {


    @Autowired
    private ShpUserInviteService shpUserInviteService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpShopService shpShopService;


    /**
     * 我邀请的人<br/>
     * 通过渠道邀请过来的用户
     *
     * @return Result
     */
    @ApiOperation(
            value = "我邀请的人2.5.1",
            notes = "我邀请的人",
            httpMethod = "GET")
    @GetMapping("/listInviteUser")
    public BaseResult<VoCountInviteShop> listInviteUser(@Valid ParamCountInvite paramCountInvite, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        String token = paramCountInvite.getToken();
        //获取该用户邀请的好友
        List<Integer> userIdList = shpUserInviteService.listBeInviteUserIdByUserId(userId);
        if (LocalUtils.isEmptyAndNull(userIdList)) {
            return BaseResult.okResultNoData();
        }

        //查找被邀请人的信息(包括店铺信息)
        PageHelper.startPage(getPageNum(), PAGE_SIZE_30);
        String time =null;
        if (paramCountInvite.getMonth() != null){
            time = DateUtil.getYyyyMmDd(paramCountInvite.getMonth());
        }

        List<VoInviteShop> listInviteShop = shpShopService.listInviteShop(userIdList.toArray(),time);

        //统计邀请的人的开店情况
        List<Map<String, Object>> listMap = shpShopService.countInviteShop(userIdList.toArray(),time);
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
