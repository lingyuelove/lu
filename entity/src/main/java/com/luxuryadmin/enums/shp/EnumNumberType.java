package com.luxuryadmin.enums.shp;

/**
 * 随机编码靓号类型的枚举类
 *
 * @author monkey king
 * @date 2019-12-26 17:25:10
 */
public enum EnumNumberType {

    /**
     * 普通号
     */
    NORMAL("10", "普通号", "普通号"),
    /**
     * 豹子号(所有都一样)
     */
    SAME("20", "豹子号", "所有都一样"),
    /**
     * 全部升序或者降序
     */
    ASC_OR_DESC("21", "连号", "全部升序或者降序"),
    /**
     * 多带1; eg: aaaaab | bbbbba
     */
    MORE_WITH_ONE("22", "多带1", "eg:aaaaab或bbbbba"),
    /**
     * 多带多; eg: aaaabb | bbbbaa
     */
    MORE_WITH_MORE("23", "多带多", "eg:aaaabb或bbbbaa"),
    /**
     * 特殊靓号; eg: 5201314
     */
    SPECIAL("50", "特殊靓号", "eg:5201314"),
    ;

    /**
     * 代码(存入数据库)
     */
    private String code;
    /**
     * 代码名称(显示作用)
     */
    private String name;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String description;


    EnumNumberType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
