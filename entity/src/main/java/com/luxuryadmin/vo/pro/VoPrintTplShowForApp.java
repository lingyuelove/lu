package com.luxuryadmin.vo.pro;

import com.luxuryadmin.vo.op.VoMessageSubType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoPrintTplShowForApp
 * @Author: ZhangSai
 * Date: 2021/8/12 11:08
 */
@Data
@ApiModel(description = "打印模板-app端集合显示")
public class VoPrintTplShowForApp {
    private  List<VoProPrintTpl> printTplList;

    private  List<VoMessageSubType> printTplCnName;
}
