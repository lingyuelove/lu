package com.luxuryadmin.admin.util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/util")
@Api(tags = {"0.【工具类】模块"}, description = "/util/upload | 【文件上传】 ")
public class OssController {


    @Autowired
    OSSUploadUtil ossUploadUtil;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucketName}")
    private String bucket;

    @Value("${oss.accessKeyId}")
    private String accessId;

    @Value("${oss.bucketUrl}")
    private String bucketUrl;

    @Value("${oss.domain}")
    private String domain;


    /**
     * Banner图片上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/oss/upload/banner")
    @ApiOperation(
            value = "上传文件;",
            notes = "上传文件;",
            httpMethod = "GET")
    public R uploadBanner(HttpServletRequest request) throws Exception {
        String dirName = "banner";

        return uploadBaseMethod(request,dirName);
    }

    /**
     * 图片上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/oss/upload")
    @ApiOperation(
            value = "上传文件;",
            notes = "上传文件;",
            httpMethod = "GET")
    public R uploadFiles(HttpServletRequest request) throws Exception {
        String dirName = "help/h5/";

        return uploadBaseMethod(request,dirName);
    }

    /**
     * 上传消息推送用户Excel文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/oss/upload/uploadPushUserExcel")
    @ApiOperation(
            value = "上传文件;",
            notes = "上传文件;",
            httpMethod = "GET")
    public R uploadPushUserExcel(HttpServletRequest request) throws Exception {
        String dirName = "push/excel/";

        //上传图片
        return uploadBaseMethod(request,dirName);
    }

    private R uploadBaseMethod(HttpServletRequest request,String dirName) throws Exception {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        Map parameterMap = request.getParameterMap();
        if (CollectionUtils.isEmpty(files)) {
            return R.error().put("0", "上传文件数量为空！");
        }
        //上传图片
        List<String> newurls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty() || file.getSize() == 0) {
                return R.error();
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return R.error();
            }
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            //OSS单文件上传,返回上传成功后的oss存储服务器中的url
            String name = OSSUploadUtil.uploadImg2Oss(file, System.nanoTime(), dirName);
            String fileName = OSSUploadUtil.getImageUrl(name, dirName);
            //String fileName = OSSUploadUtil.uploadFile(FileUtil.multipartFileToFile(file),fileType);
            newurls.add(fileName);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("file", newurls);
        log.info("上传成功，返回url:" + map);
        return R.ok().put("data", map);
    }

}
