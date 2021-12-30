package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.op
 * @ClassName: VoOpProblemCategory
 * @Author: ZhangSai
 * Date: 2021/9/14 11:00
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="帮助中心问题显示参数", description="帮助中心问题显示参数")
public class VoOpProblemCategory {

    private String problemCategoryName;

    private List<VoOpProblem> list;
}
