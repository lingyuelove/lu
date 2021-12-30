package com.luxuryadmin.api.sys;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.vo.sys.VoSysContactUs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanjin145
 * @Date 2020/08/26 19:15
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/contactUs")
@Api(tags = {"A003.【联系我们】模块"}, description = "/sys/contactUs |联系我们")
public class SysContactUsController extends BaseController {


    /**
     * 获取App系统配置;
     *
     * @return Result
     */
    @ApiOperation(
            value = "1.获取版本更新信息",
            notes = "获取版本更新信息;<br/>",
            httpMethod = "POST")
    @RequestMapping(value = "/getContactUsInfo", method = RequestMethod.POST)
    public BaseResult<List<VoSysContactUs>> getContactUsInfo() {
        List<VoSysContactUs> list = new ArrayList<>();
        constructSysContactUsList(list);

        return BaseResult.okResult(list);
    }

    private void constructSysContactUsList(List<VoSysContactUs> list) {
        String[] wx = new String[]{"shedangjia001", "shedangjia002", "shedangjia003", "shedangjia004", "shedangjia009"};
        String[] phone = new String[]{"15394251615", "18072739201", "13018998750", "17376550047", "15267099762"};
        //1.微信公众号
        VoSysContactUs contactWeixinOfficialAccount = new VoSysContactUs();
        contactWeixinOfficialAccount.setKeyWord("weixinOfficialAccount");
        contactWeixinOfficialAccount.setTitle("公众号");
        contactWeixinOfficialAccount.setShowContent("shedangjia");
        contactWeixinOfficialAccount.setIsCanCopy(true);
        contactWeixinOfficialAccount.setIsJumpH5Url(false);
        list.add(contactWeixinOfficialAccount);

        for (int i = 0; i < wx.length; i++) {
            //2-1.微信客服1
            VoSysContactUs contactWeixinCustomerService = new VoSysContactUs();
            contactWeixinCustomerService.setKeyWord("weixinCustomerService");
            contactWeixinCustomerService.setTitle("微信客服" + (i + 1));
            contactWeixinCustomerService.setShowContent(wx[i]);
            contactWeixinCustomerService.setIsCanCopy(true);
            contactWeixinCustomerService.setIsJumpH5Url(false);
            list.add(contactWeixinCustomerService);
        }

        //
        ////3.QQ客服
        //VoSysContactUs contactQqCustomerService = new VoSysContactUs();
        //contactQqCustomerService.setKeyWord("qqCustomerService");
        //contactQqCustomerService.setTitle("QQ客服");
        //contactQqCustomerService.setShowContent("1595139940");
        //contactQqCustomerService.setIsCanCopy(true);
        //contactQqCustomerService.setIsJumpH5Url(false);
        //list.add(contactQqCustomerService);


        for (int i = 0; i < phone.length; i++) {
            //5-1.客服电话1
            VoSysContactUs contactCustomerPhone = new VoSysContactUs();
            contactCustomerPhone.setKeyWord("customerPhone");
            contactCustomerPhone.setTitle("客服电话" + (i + 1));
            contactCustomerPhone.setShowContent(phone[i]);
            contactCustomerPhone.setIsCanCopy(true);
            contactCustomerPhone.setIsJumpH5Url(false);
            list.add(contactCustomerPhone);
        }


        //4.企业邮箱
        VoSysContactUs contactEnterpriseMailbox = new VoSysContactUs();
        contactEnterpriseMailbox.setKeyWord("enterpriseMailbox");
        contactEnterpriseMailbox.setTitle("企业邮箱");
        contactEnterpriseMailbox.setShowContent("service@kuarke.com");
        contactEnterpriseMailbox.setIsCanCopy(true);
        contactEnterpriseMailbox.setIsJumpH5Url(false);
        list.add(contactEnterpriseMailbox);

        //6.官网地址
        VoSysContactUs contactOfficialWebsite = new VoSysContactUs();
        contactOfficialWebsite.setKeyWord("officialWebsite");
        contactOfficialWebsite.setTitle("官网地址");
        contactOfficialWebsite.setShowContent("www.luxuryadmin.com");
        contactOfficialWebsite.setIsCanCopy(false);
        contactOfficialWebsite.setIsJumpH5Url(true);
        list.add(contactOfficialWebsite);


    }
}
