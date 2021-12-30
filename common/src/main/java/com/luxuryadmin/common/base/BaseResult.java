package com.luxuryadmin.common.base;

import com.luxuryadmin.common.constant.enums.EnumCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author monkey king
 * Happy Coding, Happy Life
 * @Description: Result回调方法包装类
 * 约定所有回调信息都通过此方法构建
 * <p>
 * 成功回调:
 * - 默认成功信息返回
 * - 泛型对象成功信息返回
 * - 泛型对象及自定义说明成功信息返回
 * <p>
 * 失败回调:
 * - 默认失败信息返回
 * - 泛型对象失败信息返回
 * - 操作码对象失败信息返回
 * - 操作码对象及泛型对象失败信息返回
 * - 自定义说明失败信息返回
 * <p>
 * 回调类型判断
 * @date 2019-12-02 19:44:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="返回说明")
public class BaseResult<T> implements Serializable {


    /**
     * 状态码
     */
    @ApiModelProperty(value = "返回编码")
    private String code;

    /**
     * 状态码说明
     */
    @ApiModelProperty(value = "描述信息")
    private String msg;

    /**
     * 回调数据对象
     */
    @ApiModelProperty(value = "业务数据")
    private T data;

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String sign;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private String timestamp;


    //**************************************** success ****************************************//

    /**
     * 默认成功信息返回(无返回数据)
     *
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static BaseResult okResult() {
        BaseResult result = new BaseResult<>();
        result.setCode(EnumCode.OK.getCode());
        result.setMsg(EnumCode.OK.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * @return
     */
    public static BaseResult okResultNoData() {
        BaseResult result = new BaseResult<>();
        result.setCode(EnumCode.OK_NO_DATA.getCode());
        result.setMsg(EnumCode.OK_NO_DATA.getMessage());
        return result;
    }

    /**
     * 泛型对象成功信息返回
     *
     * @param data 回调数据对象
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> okResult(D data) {
        BaseResult<D> result = new BaseResult<>();

        result.setCode(EnumCode.OK.getCode());
        result.setMsg(EnumCode.OK.getMessage());
        result.setData(data);

        return result;
    }

    /**
     * 泛型对象及自定义说明成功信息返回
     *
     * @param data    回调数据对象
     * @param message 自定义说明
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> okResult(D data, String message) {
        BaseResult<D> result = new BaseResult<>();
        result.setCode(EnumCode.OK.getCode());
        result.setData(data);
        result.setMsg(message);

        return result;
    }

    /**
     * 全自定义返回类型
     * @param data
     * @param <D>
     * @return
     */
    public static <D> BaseResult<D> okResult(D data, EnumCode enumCode) {
        BaseResult<D> result = new BaseResult<>();
        result.setCode(enumCode.getCode());
        result.setData(data);
        result.setMsg(enumCode.getMessage());
        return result;
    }

    /**
     * 返回ok; 自定义返回消息
     *
     * @param message 自定义说明
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> defaultOkResultWithMsg(String message) {
        BaseResult<D> result = new BaseResult<>();
        result.setCode(EnumCode.OK.getCode());
        result.setMsg(message);
        return result;
    }

    //**************************************** error ****************************************//

    /**
     * 默认失败信息返回(无返回数据)
     *
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorResult() {
        BaseResult<D> result = new BaseResult<>();

        result.setCode(EnumCode.ERROR.getCode());
        result.setMsg(EnumCode.ERROR.getMessage());

        result.setData(null);

        return result;
    }

    /**
     * 泛型对象失败信息返回
     *
     * @param data 泛型对象
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorResult(D data) {
        BaseResult<D> result = new BaseResult<>();

        result.setCode(EnumCode.ERROR.getCode());
        result.setMsg(EnumCode.ERROR.getMessage());

        result.setData(data);

        return result;
    }

    /**
     * 操作码对象失败信息返回
     *
     * @param enumCodeMsg 操作码对象
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorResult(EnumCode enumCodeMsg) {
        if (enumCodeMsg == null) {
            return errorResult();
        }

        BaseResult<D> result = new BaseResult<>();

        result.setCode(enumCodeMsg.getCode());
        result.setMsg(enumCodeMsg.getMessage());
        result.setData(null);

        return result;
    }

    /**
     * 操作码对象及泛型对象失败信息返回
     *
     * @param enumCodeMsg 操作码对象
     * @param data        泛型对象
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorResult(EnumCode enumCodeMsg, D data) {
        if (enumCodeMsg == null) {
            return errorResult(data);
        }

        BaseResult<D> result = new BaseResult<>();

        result.setCode(enumCodeMsg.getCode());
        result.setMsg(enumCodeMsg.getMessage());

        result.setData(data);

        return result;
    }

    /**
     * 操作码对象及泛型对象失败信息返回
     *
     * @param data        泛型对象
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorNeedUpgrade(String msg,D data) {

        BaseResult<D> result = new BaseResult<>();

        result.setCode("need_upgrade");
        result.setMsg(msg);
        result.setData(data);

        return result;
    }

    /**
     * controller层错误拦截异常返回
     *
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> errorControl() {
        BaseResult<D> result = new BaseResult<>();

        result.setCode(EnumCode.ERROR_CONTROL.getCode());
        result.setMsg(EnumCode.ERROR_CONTROL.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 自定义说明失败信息返回
     *
     * @param customMsg 自定义说明
     * @return
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static <D> BaseResult<D> defaultErrorWithMsg(String customMsg) {
        BaseResult<D> result = new BaseResult<>();

        result.setCode(EnumCode.ERROR.getCode());
        result.setMsg(customMsg);
        result.setData(null);

        return result;
    }

    /**
     * 回调类型判断
     *
     * @param result 自定义说明
     * @return ture:成功回调 / false:失败回调
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public static Boolean checkResultState(BaseResult result) {
        return Objects.equals(result.getCode(), EnumCode.OK.getCode());
    }

}
