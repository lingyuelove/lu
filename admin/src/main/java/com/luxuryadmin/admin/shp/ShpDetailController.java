package com.luxuryadmin.admin.shp;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.param.biz.ParamShopValid;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamUploadShopValid;
import com.luxuryadmin.service.shp.ShpDetailService;
import com.luxuryadmin.vo.shp.VoShopValidInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author monkey king
 * @date 2021-08-03 16:58:29
 */

@Slf4j
@RestController
@RequestMapping(value = "/user/shp", method = RequestMethod.POST)
@Api(tags = {"8.【店铺模块】模块"})
public class ShpDetailController extends BaseController {


    @Autowired
    private ShpDetailService shpDetailService;

    @ApiOperation(
            value = "修改店铺认证信息",
            notes = "修改店铺认证信息",
            httpMethod = "POST")
    @PostMapping("/updateValidShopInfo")
    public BaseResult updateValidShopInfo(@Valid ParamShopValid shopValid, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String licenseImgUrl = shopValid.getLicenseImgUrl();
        String validImgUrl = shopValid.getValidImgUrl();
        String validVideoUrl = shopValid.getValidVideoUrl();


        ShpDetail shpDetail = new ShpDetail();
        shpDetail.setUpdateTime(new Date());
        shpDetail.setLicenseImgUrl(licenseImgUrl);
        shpDetail.setValidImgUrl(validImgUrl);
        shpDetail.setValidVideoUrl(validVideoUrl);
        shpDetail.setFkShpShopId(Integer.parseInt(shopValid.getShopId()));
        int num = 0;
        num = LocalUtils.isEmptyAndNull(licenseImgUrl) ? num : ++num;
        num = LocalUtils.isEmptyAndNull(validImgUrl) ? num : ++num;
        num = LocalUtils.isEmptyAndNull(validVideoUrl) ? num : ++num;
        String uploadState;
        switch (num) {
            case 1:
            case 2:
                uploadState = "部分上传";
                break;
            case 3:
                uploadState = "全部上传";
                break;
            default:
                uploadState = "未上传";
                break;
        }
        shpDetail.setUploadState(uploadState);
        shpDetailService.updateShpDetailByShopId(shpDetail);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "获取店铺认证信息",
            notes = "获取店铺认证信息",
            httpMethod = "GET")
    @GetMapping("/getValidShopInfo")
    public BaseResult getValidShopInfo(@Valid ParamShopValid shopValid, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShopValidInfo shopValidInfo = shpDetailService.getShopValidInfo(shopValid.getShopId());
        return LocalUtils.getBaseResult(shopValidInfo);
    }


    /**
     * 认证图片上传
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "认证图片上传;",
            notes = "认证图片上传;",
            httpMethod = "POST")
    @PostMapping("/uploadImg")
    public BaseResult uploadImg(@Valid ParamUploadShopValid param, BindingResult result, HttpServletRequest request) throws Exception {
        servicesUtil.validControllerParam(result);
        StringBuffer dirName = new StringBuffer();
        dirName.append("validInfo/shop/").append(param.getType());
        dirName.append("/").append(param.getShopId());
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/");
        String filePath = OSSUtil.uploadBaseMethod(request, dirName.toString(), 10);
        return BaseResult.okResult(filePath);
    }


    /**
     * 视频上传
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "视频上传;",
            notes = "视频上传;",
            httpMethod = "POST")
    @PostMapping("/uploadVideo")
    public BaseResult uploadVideo(@Valid ParamUploadShopValid param, BindingResult result,
                                  @RequestParam("file") MultipartFile file) throws Exception {
        servicesUtil.validControllerParam(result);
        if (file.isEmpty()) {
            return BaseResult.errorResult("请上传文件!");
        }
        long fileSize = file.getSize();
        if (fileSize > 300 * 1024 * 1024) {
            return BaseResult.errorResult("上传文件大小超出限制300M.");
        }
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"mp4".equalsIgnoreCase(fileType)) {
            return BaseResult.errorResult("只支持mp4格式!");
        }
        StringBuffer dirName = new StringBuffer();
        if (LocalUtils.isEmptyAndNull(param.getType())){
            param.setType("default");
        }
        dirName.append("validInfo/shop/"+param.getType()+"/").append(param.getShopId());
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/");
        String fileName = System.currentTimeMillis() + "." + fileType;
        String relativePath = "/" + dirName + fileName;

        String serverPath = OSSUtil.uploadBytesFile(dirName.toString() + fileName, file.getBytes());
        log.debug("--serverPath: {}", serverPath);
        return BaseResult.okResult(relativePath);
    }


}
