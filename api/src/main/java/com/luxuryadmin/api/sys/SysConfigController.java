package com.luxuryadmin.api.sys;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProDownloadImg;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinShopRecordTypeService;
import com.luxuryadmin.service.pro.ProDownloadImgService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpAfterSaleGuaranteeService;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.stat.StatDailyCountService;
import com.luxuryadmin.service.sys.SysConfigService;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys")
@Api(tags = {"A000.【初始化】模块"}, description = "/sys |app初始化和参数配置相关")
public class SysConfigController extends BaseController {


    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpAfterSaleGuaranteeService shpAfterSaleGuaranteeService;

    @Autowired
    private ProDownloadImgService proDownloadImgService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpServiceService shpServiceService;

    @Autowired
    private FinShopRecordTypeService finShopRecordTypeService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private StatDailyCountService statDailyCountService;


    /**
     * 获取App系统配置;
     * 配置加密开关对此接口不影响;该接口返回的数据;是经过AES对称加密;<br/>
     * 接口参数map可分区平台是android还是iOS
     *
     * @param params
     * @return Result
     */
    @ApiOperation(
            value = "1.获取App系统配置",
            notes = "配置加密开关对此接口不影响;该接口返回的数据;是经过AES对称加密;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
    })
    @RequestMapping(value = "/getAppSysConfig", method = RequestMethod.GET)
    public BaseResult getAppSysConfig(@RequestParam Map<String, String> params) {
        return BaseResult.okResult(sysConfigService.initSysConfig());
    }

    /**
     * 重置boss的店铺关系
     *
     * @param params
     * @return Result
     */
    @ApiOperation(
            value = "重置boss的店铺关系",
            notes = "重置boss的店铺关系;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
    })
    @RequestMapping(value = "/restartBossShpUserShopRef", method = RequestMethod.GET)
    public BaseResult restartBossShpUserShopRef(@RequestParam Map<String, String> params) {
        int userId = getUserId();
        if (userId != 10000 && !params.get("token").equals("15112304365")) {
            return BaseResult.defaultErrorWithMsg("无操作权限");
        }
        //初始化所有店铺的权限;
        List<VoUserShopBase> shopList = shpShopService.listAllShopIdAndUserId();
        if (!LocalUtils.isEmptyAndNull(shopList)) {
            for (VoUserShopBase shop : shopList) {
                sysConfigService.restartBossShpUserShopRef(shop.getShopId(), shop.getUserId());
            }
            String key = ConstantRedisKey.SHP_PERM_VERSION;
            String permVersion = redisUtil.get(key);
            int i = Integer.parseInt(permVersion);
            redisUtil.set(key, ++i + "");
        }
        return BaseResult.okResult();
    }

    /**
     * 初始化店铺基础数据;<br/>
     * 商品分类;商品来源;订单类型;销售渠道等等
     *
     * @param params
     * @return Result
     */
    @ApiOperation(
            value = "初始化店铺基础数据;",
            notes = "初始化店铺全部权限;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
    })
    @RequestMapping(value = "/initShopBaseData", method = RequestMethod.GET)
    public BaseResult initShopBaseData(@RequestParam Map<String, String> params) {
        String token = params.get("token");
        validPerm(token);
        sysConfigService.initShopBaseData();
        return BaseResult.okResult();
    }

    /**
     * 初始化未下载图片标识符(一次性使用)
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "初始化未下载图片标识符",
            notes = "初始化未下载图片标识符",
            httpMethod = "GET")
    @RequestMapping(value = "/initNotDownloadProduct", method = RequestMethod.GET)
    public BaseResult initNotDownloadProduct(@RequestParam String token) {
        validPerm(token);
        int i = 1;
        for (; ; ) {
            PageHelper.startPage(i, 500);
            List<ProDownloadImg> proDownloadImgList = proDownloadImgService.listProDownloadImg();
            if (LocalUtils.isEmptyAndNull(proDownloadImgList)) {
                break;
            }
            for (ProDownloadImg downloadImg : proDownloadImgList) {
                String bizId = downloadImg.getFkProProductBizId();
                Integer shopId = downloadImg.getFkShpShopId();
                Integer userId = downloadImg.getFkShpUserId();
                String key = ConstantRedisKey.getNotDownloadProductKey(shopId, bizId);
                String keyValue = LocalUtils.returnEmptyStringOrString(redisUtil.get(key));
                if (LocalUtils.isEmptyAndNull(keyValue) || !keyValue.contains(userId + "")) {
                    keyValue += userId + ",";
                }
                redisUtil.set(key, keyValue);
            }
            i++;
        }
        return BaseResult.okResult(i);
    }

    /**
     * 格式化商品的描述和名称,去除不可见字符;
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "格式化商品的描述和名称,去除不可见字符;",
            notes = "格式化商品的描述和名称,去除不可见字符;",
            httpMethod = "GET")
    @RequestMapping(value = "/formatProductDescription", method = RequestMethod.GET)
    public BaseResult formatProductDescription(@RequestParam String token) {
        validPerm(token);
        int i = 1;
        for (; ; ) {
            PageHelper.startPage(i, 500);
            List<ProProduct> proList = proProductService.listAllShopProProduct();
            if (LocalUtils.isEmptyAndNull(proList)) {
                break;
            }
            for (ProProduct pro : proList) {
                String description = pro.getDescription();
                if (!LocalUtils.isEmptyAndNull(description)) {
                    description = description.replaceAll("\\p{C}", "");
                }
                String name = pro.getName();
                if (!LocalUtils.isEmptyAndNull(name)) {
                    name = name.replaceAll("\\p{C}", "");
                }
                pro.setName(name);
                pro.setDescription(description);
                proProductService.updateProProduct(pro);
            }
            i++;
        }
        return BaseResult.okResult(i);
    }

    /**
     * 添加店铺售后保障
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "初始化所有老店铺的售后保障",
            notes = "初始化所有老店铺的售后保障",
            httpMethod = "GET")
    @RequestMapping(value = "/initAllShpAfterSaleGuarantee", method = RequestMethod.GET)
    public BaseResult initAllShpAfterSaleGuarantee(@RequestParam String token) {
        validPerm(token);
        Integer result = shpAfterSaleGuaranteeService.initAllShpAfterSaleGuarantee();
        return BaseResult.okResult(result);
    }

    /**
     * 添加店铺售后保障
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "初始化所有老店铺的【财务记录类型】",
            notes = "初始化所有老店铺的【财务记录类型】",
            httpMethod = "GET")
    @RequestMapping(value = "/initAllShpFinRecordType", method = RequestMethod.GET)
    public BaseResult initAllShpFinRecordType(@RequestParam String token) {
        //validPerm(token);
        Integer result = finShopRecordTypeService.initAllShpFinShopRecordType();
        return BaseResult.okResult(result);
    }

    /**
     * 初始化所有老店铺的默认服务类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "初始化所有老店铺的【默认服务类型】",
            notes = "初始化所有老店铺的【默认服务类型】",
            httpMethod = "GET")
    @RequestMapping(value = "/initAllShpServiceType", method = RequestMethod.GET)
    public BaseResult initAllShpServiceType(@RequestParam String token) {
        validPerm(token);
        Integer result = shpServiceService.initAllShpServiceType();
        return BaseResult.okResult(result);
    }

    /**
     * 初始化所有老店铺的默认服务类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "测试【添加一条操作日志记录】",
            notes = "测试【添加一条操作日志记录】",
            httpMethod = "GET")
    @RequestMapping(value = "/testSaveShpOperateLog", method = RequestMethod.GET)
    public BaseResult testSaveShpOperateLog(HttpServletRequest httpServletRequest) {
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(10078);
        paramAddShpOperateLog.setOperateUserId(10065);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("操作日志测试");
        paramAddShpOperateLog.setOperateContent("操作日志测试-内容");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(httpServletRequest);

        //Integer result = shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult(-1);
    }

    /**
     * 升级用户邀请码到7位;不足7位前面补9
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "升级用户邀请码到7位;不足7位前面补9",
            notes = "升级用户邀请码到7位;不足7位前面补9",
            httpMethod = "GET")
    @RequestMapping(value = "/updateInviteNumber", method = RequestMethod.GET)
    public BaseResult updateInviteNumber(@RequestParam String token) {
        validPerm(token);
        shpUserService.updateUserNumberForMillion();
        //Integer result = shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult(-1);
    }

    /**
     * 生成初始化运营统计数据
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "生成初始化运营统计数据",
            notes = "生成初始化运营统计数据",
            httpMethod = "GET")
    @RequestMapping(value = "/testStatDailyCount", method = RequestMethod.GET)
    public BaseResult testStat(@RequestParam String token) {
        //validPerm(token);
        if ("bdhc919283798dase31eSsf".equals(token)) {
            shpUserService.updateUserNumberForMillion();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = "2020-01-18";
            String end = DateUtil.formatShort(new Date());
            Calendar st = Calendar.getInstance();
            Calendar ed = Calendar.getInstance();
            try {
                st.setTime(sdf.parse(start));
                ed.setTime(sdf.parse(end));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            while (!st.after(ed)) {
                log.info("****************开始统计数据: " + sdf.format(st.getTime()) + "*******************");
                statDailyCountService.dailyCountStat(st.getTime());
                st.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return BaseResult.okResult(-1);
    }


    /**
     * 临时开通会员渠道
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "临时开通会员渠道",
            notes = "临时开通会员渠道",
            httpMethod = "GET")
    @RequestMapping(value = "/updateShopMember", method = RequestMethod.GET)
    public BaseResult updateShopMember(@RequestParam String token, String shopNumber, String member) {
        validPerm(token);
        Integer shopId = shpShopService.getShopIdByShopNumber(shopNumber);
        if (LocalUtils.isEmptyAndNull(shopId)) {
            return BaseResult.errorResult("shop not exists");
        }
        if (LocalUtils.isEmptyAndNull(member) || !"yes,no".contains(member)) {
            return BaseResult.errorResult("[member]param error");
        }
        return BaseResult.okResult(shpShopService.updateShopMember(shopId, member));
    }


    /**
     * 初始化vip到期时间
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "初始化vip到期时间",
            notes = "初始化vip到期时间",
            httpMethod = "GET")
    @RequestMapping(value = "/initVipExpire", method = RequestMethod.GET)
    public BaseResult initVipExpire(@RequestParam String token) {
        validPerm(token);
        sysConfigService.initVipExpire();
        return BaseResult.okResult();
    }


    private void validPerm(String value) {
        int userId = getUserId();
        if (userId != 10000 && !value.equals("15112304365")) {
            throw new MyException("无操作权限");
        }
    }
}
