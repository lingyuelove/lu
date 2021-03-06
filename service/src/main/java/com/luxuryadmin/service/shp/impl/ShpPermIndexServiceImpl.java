package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermIndex;
import com.luxuryadmin.mapper.shp.ShpPermIndexMapper;
import com.luxuryadmin.mapper.shp.ShpRolePermissionRefMapper;
import com.luxuryadmin.service.shp.ShpPermIndexService;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 16:45:56
 */
@Slf4j
@Service
public class ShpPermIndexServiceImpl implements ShpPermIndexService {

    @Resource
    private ShpPermIndexMapper shpPermIndexMapper;

    @Resource
    private ShpRolePermissionRefMapper shpRolePermissionRefMapper;

    @Override
    public int saveShpPermIndex(ShpPermIndex shpPermission) {
        shpPermIndexMapper.saveObject(shpPermission);
        return shpPermission.getId();
    }

    @Override
    public int updateShpPermIndex(ShpPermIndex shpPermission) {
        shpPermIndexMapper.updateObject(shpPermission);
        return shpPermission.getId();
    }

    @Override
    public boolean existsShpPermIndex(int parentId, String name) {
        return shpPermIndexMapper.existsShpPermission(parentId, name) > 0;
    }

    @Override
    public boolean existsShpPermIndexCode(String code) {
        return shpPermIndexMapper.existsShpPermissionCode(code) > 0;
    }


    @Override
    public ShpPermIndex getShpPermIndexById(Integer id) {
        return (ShpPermIndex) shpPermIndexMapper.getObjectById(id);
    }

    @Override
    public List<VoShpPermIndex> groupByShpPermIndex(String showOnApp, String platform, String appVersion) throws Exception {
        appVersion = LocalUtils.isEmptyAndNull(appVersion) ? null : appVersion.replaceAll("\\.", "");
        List<VoShpPermIndex> voShpPermissions = shpPermIndexMapper.listAppShpPermission(showOnApp, platform, appVersion);

        List<VoShpPermIndex> privatePermList = new ArrayList<>();
        VoShpPermIndex privatePerm = new VoShpPermIndex();
        privatePerm.setName("????????????????????????");
        privatePerm.setParentId(0);
        privatePerm.setType(0);
        privatePerm.setDisplay(0);
        privatePerm.setIsPrivate("1");
        privatePerm.setSort(99);
        privatePerm.setId(1);

        int tradePermId = -1;
        //??????????????????
        List<VoShpPermIndex> firstPermList = new ArrayList<>();
        for (VoShpPermIndex allPerm : voShpPermissions) {
            int firstParent = allPerm.getParentId();
            if (firstParent == 0) {
                firstPermList.add(allPerm);
            }
            //??????????????????,?????????????????????????????????
            //if (ConstantCommon.ONE.equals(allPerm.getIsPrivate())) {
            //    //??????????????????
            //    if ("??????????????????".equals(allPerm.getName())) {
            //        tradePermId = allPerm.getId();
            //        continue;
            //    }
            //    //if(allPerm.getParentId()==tradePermId || )
            //    allPerm.setType(1);
            //    allPerm.setParentId(-1);
            //    privatePermList.add(allPerm);
            //    if (LocalUtils.isEmptyAndNull(privatePerm.getCode())) {
            //        //???????????????????????????
            //        privatePerm.setColor(allPerm.getColor());
            //    }
            //}
            if (ConstantCommon.ONE.equals(allPerm.getIsPrivate())) {
                // privatePermList.add(allPerm);
                if (LocalUtils.isEmptyAndNull(privatePerm.getCode())) {
                    //???????????????????????????
                    privatePerm.setColor(allPerm.getColor());
                }
            }
        }
        privatePerm.setPermIndexList(privatePermList);
        //??????????????????;??????????????????;???????????????
        for (VoShpPermIndex firstPerm : firstPermList) {
            List<VoShpPermIndex> secondPermList = new ArrayList<>();
            //???????????????id,??????????????????????????????
            Integer firstPermId = firstPerm.getId();
            //??????????????????;
            for (VoShpPermIndex allPerm : voShpPermissions) {
                int secondParentId = allPerm.getParentId();
                if (firstPermId == secondParentId) {
                    secondPermList.add(allPerm);
                }
            }

            for (VoShpPermIndex secondPerm : secondPermList) {
                List<VoShpPermIndex> thirdPermList = new ArrayList<>();
                //???????????????id,??????????????????????????????
                Integer secondPermId = secondPerm.getId();
                //??????????????????;
                for (VoShpPermIndex allPerm : voShpPermissions) {
                    int thirdParentId = allPerm.getParentId();
                    if (secondPermId == thirdParentId) {
                        thirdPermList.add(allPerm);
                    }
                }
                if (!LocalUtils.isEmptyAndNull(thirdPermList)) {
                    secondPerm.setPermIndexList(thirdPermList);
                }
            }
            if (!LocalUtils.isEmptyAndNull(secondPermList)) {
                firstPerm.setPermIndexList(secondPermList);
            }
        }

        //??????????????????,?????????????????????(??????????????????);?????????????????????,?????????????????????????????????????????????;
        List<VoShpPermIndex> newPermList = LocalUtils.deepCopy(firstPermList);
        for (VoShpPermIndex obj : newPermList) {
            if ("??????????????????".equals(obj.getName())) {
                continue;
            }
            List<VoShpPermIndex> secondList = obj.getPermIndexList();
            if (!LocalUtils.isEmptyAndNull(secondList)) {
                for (VoShpPermIndex secondObj : secondList) {
                    if (ConstantCommon.ONE.equals(secondObj.getIsPrivate())) {
                        secondObj.setType(1);
                        //secondObj.setParentId(1);
                        privatePermList.add(secondObj);
                    }
                    List<VoShpPermIndex> threeList = secondObj.getPermIndexList();
                    if (!LocalUtils.isEmptyAndNull(threeList)) {
                        for (VoShpPermIndex threeObj : threeList) {
                            if (ConstantCommon.ONE.equals(threeObj.getIsPrivate())) {
                                threeObj.setType(1);
                                //threeObj.setParentId(1);
                                privatePermList.add(threeObj);
                            }
                        }
                    }
                    secondObj.setPermIndexList(null);
                }
            }
        }
        //??????????????????
        firstPermList.add(privatePerm);
        return firstPermList;
    }

    @Override
    public List<VoUsualFunction> listShpPermissionByIds(String ids) {
        return shpPermIndexMapper.listShpPermissionByIds(ids);
    }

    @Override
    public List<Integer> listAppShpPermissionId() {
        return shpPermIndexMapper.listAppShpPermissionId();
    }

    @Override
    public void deleteShpPermIndex(String inSql) {
        shpPermIndexMapper.deleteObjects(inSql);
        deleteShpPermissionRole();
    }

    @Override
    public void deleteShpPermissionRole() {
        shpRolePermissionRefMapper.deleteObjects();
    }

    @Override
    public boolean existsPerms(String permission) {
        return shpPermIndexMapper.existsPerms(permission) > 0;
    }

    @Override
    public ShpPermIndex getShpPermissionByPermission(String permission) {
        return shpPermIndexMapper.getShpPermissionByPermission(permission);
    }

    @Override
    public List<VoShpPermIndex> listAppFunction() {
        return shpPermIndexMapper.listAppFunction();
    }


    @Override
    public List<String> listPermIndexByPermission(String[] permissions) {
        String perms = LocalUtils.packString(permissions);
        return shpPermIndexMapper.listPermIndexByPermission(perms);
    }
}
