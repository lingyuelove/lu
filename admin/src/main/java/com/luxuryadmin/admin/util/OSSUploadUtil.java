package com.luxuryadmin.admin.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.Contended;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Contended
@Component
public class OSSUploadUtil {

    /**
     * 设置文件的大小
     */
    private static final Integer MAX_SIZE = 1024 * 1024 * 10;

    /**
     * 文件存储目录
     */
    private static String filedir = "project/niuqixt/";

    private static String endpoint;

    private static String bucket;

    private static String accessId;

    private static String bucketUrl;

    private static String bucketName;

    private static String domain;

    @Value("${oss.domain}")
    public void setDomain(String domain) {
        OSSUploadUtil.domain = domain;
    }

    @Value("${oss.bucketName}")
    public void setBucketName(String bucketName) {
        OSSUploadUtil.bucketName = bucketName;
    }

    @Value("${oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OSSUploadUtil.accessKeySecret = accessKeySecret;
    }

    private static String accessKeySecret;

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OSSUploadUtil.endpoint = endpoint;
    }

    @Value("${oss.bucketName}")
    public void setBucket(String bucket) {
        OSSUploadUtil.bucket = bucket;
    }

    @Value("${oss.accessKeyId}")
    public void setAccessId(String accessId) {
        OSSUploadUtil.accessId = accessId;
    }

    @Value("${oss.bucketUrl}")
    public void setBucketUrl(String bucketUrl) {
        OSSUploadUtil.bucketUrl = bucketUrl;
    }

    /**
     * @param file
     * @param fileType 文件后缀
     * @return String 文件地址
     * @MethodName: uploadFile
     * @Description: OSS单文件上传
     */
    public static String uploadFile(File file, String fileType, String dirName) {
        //config = config == null ? new OSSConfig():config;
        //通过UUID生成文件名
        //String dir = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
        //String dir = "project/niuqixt/";
        String fileName = dirName
                + UUID.randomUUID().toString().toUpperCase()
                .replace("-", "")
                + "." + fileType;
        return putFile(file, fileType, fileName);
    }

    /**
     * @param file
     * @param fileType
     * @param oldUrl
     * @return String
     * @MethodName: updateFile
     * @Description: 更新文件:只更新内容，不更新文件名和文件地址。
     * (因为地址没变，可能存在浏览器原数据缓存，不能及时加载新数据，例如图片更新，请注意)
     */
    public static String updateFile(File file, String fileType, String oldUrl) {
        String fileName = getFileName(oldUrl);
        if (fileName == null) {
            return null;
        }
        return putFile(file, fileType, fileName);
    }

    /**
     * @param file
     * @param fileType 文件后缀
     * @param oldUrl   需要删除的文件地址
     * @return String 文件地址
     * @MethodName: replaceFile
     * @Description: 替换文件:删除原文件并上传新文件，文件名和地址同时替换
     * 解决原数据缓存问题，只要更新了地址，就能重新加载数据)
     */
    public static String replaceFile(File file, String fileType, String oldUrl, String dirName) {
        boolean flag = deleteFile(oldUrl);      //先删除原文件
        if (!flag) {
            //更改文件的过期时间，让他到期自动删除。
        }
        return uploadFile(file, fileType, dirName);
    }

    /**
     * @param fileUrl 需要删除的文件url
     * @return boolean 是否删除成功
     * @MethodName: deleteFile
     * @Description: 单文件删除
     */
    public static boolean deleteFile(String fileUrl) {
        //config = config == null ? new OSSConfig():config;

        //根据url获取bucketName
        //String bucketName = OSSUploadUtil.getBucketName(fileUrl);
        //根据url获取fileName
        String fileName = OSSUploadUtil.getFileName(fileUrl);
        if (bucketName == null || fileName == null) {
            return false;
        }
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endpoint, accessId, accessKeySecret);
            GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(fileName);
            ossClient.deleteObject(request);
        } catch (Exception oe) {
            oe.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
        return true;
    }

    /**
     * @param fileUrls 需要删除的文件url集合
     * @return int 成功删除的个数
     * @MethodName: batchDeleteFiles
     * @Description: 批量文件删除(较快)：适用于相同endPoint和BucketName
     */
    public static int deleteFile(List<String> fileUrls) {
        //成功删除的个数
        int deleteCount = 0;
        //根据url获取bucketName
        //String bucketName = OSSUploadUtil.getBucketName(fileUrls.get(0));
        //根据url获取fileName
        List<String> fileNames = OSSUploadUtil.getFileName(fileUrls);
        if (bucketName == null || fileNames.size() <= 0) {
            return 0;
        }
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endpoint, accessId, accessKeySecret);
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(fileNames);
            DeleteObjectsResult result = ossClient.deleteObjects(request);
            deleteCount = result.getDeletedObjects().size();
        } catch (OSSException oe) {
            oe.printStackTrace();
            throw new RuntimeException("OSS服务异常:", oe);
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new RuntimeException("OSS客户端异常:", ce);
        } finally {
            ossClient.shutdown();
        }
        return deleteCount;

    }

    /**
     * @param fileUrls 需要删除的文件url集合
     * @return int 成功删除的个数
     * @MethodName: batchDeleteFiles
     * @Description: 批量文件删除(较慢)：适用于不同endPoint和BucketName
     */
    public static int deleteFiles(List<String> fileUrls) {
        int count = 0;
        for (String url : fileUrls) {
            if (deleteFile(url)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param file
     * @param fileType
     * @param fileName
     * @return String
     * @MethodName: putFile
     * @Description: 上传文件
     */
    private static String putFile(File file, String fileType, String fileName) {
        //config = config==null?new OSSConfig():config;
        //config = new OSSConfig();
        //默认null
        String url = null;
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endpoint, accessId, accessKeySecret);
            InputStream input = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 设置上传内容类型
            meta.setContentType(OSSUploadUtil.contentType(fileType));
            // 被下载时网页的缓存行为
            meta.setCacheControl("no-cache");
            //创建上传请求
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, input, meta);
            ossClient.putObject(request);
            // 设置URL过期时间为10年  3600L* 1000*24*365*10
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            //上传成功再返回的文件路径
            url = ossClient.generatePresignedUrl(bucketName, fileName, expiration)
                    .toString()
                    .replaceFirst(bucketUrl, domain);
        } catch (OSSException | FileNotFoundException | ClientException oe) {
            oe.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }

    /**
     * @param fileType
     * @return String
     * @MethodName: contentType
     * @Description: 获取文件类型
     */
    private static String contentType(String fileType) {
        fileType = fileType.toLowerCase();
        String contentType = "";
        switch (fileType) {
            case "bmp":
                contentType = "image/bmp";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "png":
            case "jpeg":
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "html":
                contentType = "text/html";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "vsd":
                contentType = "application/vnd.visio";
                break;
            case "ppt":
            case "pptx":
                contentType = "application/vnd.ms-powerpoint";
                break;
            case "doc":
            case "docx":
                contentType = "application/msword";
                break;
            case "xml":
                contentType = "text/xml";
                break;
            case "mp4":
                contentType = "video/mp4";
                break;
            default:
                contentType = "application/octet-stream";
                break;
        }
        return contentType;
    }

    /**
     * @param fileUrl 文件url
     * @return String bucketName
     * @MethodName: getBucketName
     * @Description: 根据url获取bucketName
     */
    private static String getBucketName(String fileUrl) {
        String http = "http://";
        String https = "https://";
        int httpIndex = fileUrl.indexOf(http);
        int httpsIndex = fileUrl.indexOf(https);
        int startIndex = 0;
        if (httpIndex == -1) {
            if (httpsIndex == -1) {
                return null;
            } else {
                startIndex = httpsIndex + https.length();
            }
        } else {
            startIndex = httpIndex + http.length();
        }
        int endIndex = fileUrl.indexOf(".oss-");
        if (endIndex != -1) {
            return fileUrl.substring(startIndex, endIndex);
        } else {
            fileUrl = fileUrl.replaceFirst(domain, bucketUrl);
            endIndex = fileUrl.indexOf(".oss-");
            return fileUrl.substring(startIndex, endIndex);
        }
    }

    /**
     * @param fileUrl 文件url
     * @return String fileName
     * @MethodName: getFileName
     * @Description: 根据url获取fileName
     */
    private static String getFileName(String fileUrl) {
        String str = "aliyuncs.com/";
        int beginIndex = fileUrl.indexOf(str);
        if (beginIndex == -1) {
            fileUrl = fileUrl.replaceFirst(domain, bucketUrl);
            beginIndex = fileUrl.indexOf(str);
            if (beginIndex == -1) {
                return null;
            }
            return fileUrl.substring(beginIndex + str.length());
        }
        return fileUrl.substring(beginIndex + str.length());
    }

    /**
     * @param fileUrls 文件url
     * @return List<String>  fileName集合
     * @MethodName: getFileName
     * @Description: 根据url获取fileNames集合
     */
    private static List<String> getFileName(List<String> fileUrls) {
        List<String> names = new ArrayList<>();
        for (String url : fileUrls) {
            names.add(getFileName(url));
        }
        return names;
    }


    /**
     * 指定文件名为Id
     */
    public static String uploadImg2Oss(MultipartFile file, Long id, String dirName) throws Exception {

        if (file.getSize() > MAX_SIZE) {
            throw new Exception();
        }
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        //判断图片格式是否正确
        /*if (!substring.equals(".jpg") && !substring.equals(".jpeg") && !substring.equals(".png")) {
            throw new Exception();
        }*/
        //设置文件名
        String name = id + substring;
        try {
            InputStream inputStream = file.getInputStream();
            //上传文件
            uploadFile2OSS(inputStream, name, dirName);
            return name;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String uploadFile2OSS(InputStream instream, String fileName, String dirName) throws Exception {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            OSSClient ossClient = new OSSClient(endpoint, accessId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(bucketName, dirName + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 手动拼接访问路径
     *
     * @param name
     * @return
     */
    public static String getImageUrl(String name, String dirname) {

        return domain + "/" + dirname + name;
    }
}
