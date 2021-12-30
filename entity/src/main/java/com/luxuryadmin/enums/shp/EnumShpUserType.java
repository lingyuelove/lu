package com.luxuryadmin.enums.shp;

/**
 * 用户的帐号类型
 *
 * @author monkey king
 * @date 2019-12-26 17:46:59
 */
public enum EnumShpUserType {
    //枚举
    BOSS(-9, "经营者", "店铺注册人"),
    SHOPKEEPER(-2, "店长", "店长"),
    ADMIN(-1, "管理员", "管理员"),
    EMPLOYEE(0, "员工", "员工"),
    AGENCY(1, "代理", "代理"),
    GUEST(2, "访客", "访客");

    /**
     * 代码(存入数据库)
     */
    private Integer code;
    /**
     * 代码名称(显示作用)
     */
    private String name;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String description;


    EnumShpUserType(Integer code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public static String getShpUserTypeName(String type) {
        //此强制转换是为了防止多次调用此方法.造成影响判断;
        try {
            type = Integer.parseInt(type) + "";
        } catch (NumberFormatException e) {
            return type;
        }
        switch (type + "") {
            case "-9":
                type = BOSS.getName();
                break;
            case "-2":
                type = SHOPKEEPER.getName();
                break;
            case "-1":
                type = ADMIN.getName();
                break;
            case "0":
                type = EMPLOYEE.getName();
                break;
            case "1":
                type = AGENCY.getName();
                break;
            case "2":
                type = GUEST.getName();
                break;
            default:
                type = "其他";
                break;
        }
        return type;
    }

    /**
     * 获取用户类型
     *
     * @param userType
     * @return
     */
    public static EnumShpUserType getUserType(String userType) {

        switch ("" + userType) {
            case "-9":
                return EnumShpUserType.BOSS;
            case "-2":
                return EnumShpUserType.SHOPKEEPER;
            case "-1":
                return EnumShpUserType.ADMIN;
            case "0":
                return EnumShpUserType.EMPLOYEE;
            case "1":
                return EnumShpUserType.AGENCY;
            default:
                return EnumShpUserType.GUEST;
        }
    }
}
