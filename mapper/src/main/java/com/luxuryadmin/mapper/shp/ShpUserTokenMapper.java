package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.vo.admin.shp.VoShpUserToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ShpUserTokenMapper extends BaseMapper {


    /**
     * 根据token查找ShpUserToken
     *
     * @param token token
     * @return {@link ShpUserToken}
     */
    ShpUserToken getShpUserTokenByToken(String token);

    /**
     * 根据用户id查询用户注册信息
     *
     * @param userId
     * @return
     */
    ShpUserToken getShpUserTokenByUserId(Integer userId);

    /**
     * 更新用户的token
     *
     * @param userId
     * @return
     */
    int updateShpUserTokenState(Integer userId);

    /**
     * 获取用户的登录记录
     *
     * @param userId
     * @return
     */
    List<VoShpUserToken> listShpUserToken(Integer userId);

}