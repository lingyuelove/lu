package com.luxuryadmin.gateway.app.shiro;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证shiro权限的工具类
 *
 * @author QiaoLiQiang
 * @time 2018年11月3日下午9:30:46
 */
public class ShiroUtils {}
//public class ShiroUtils {
//
//    private static final Logger log = LoggerFactory.getLogger(ShiroPermissionsUtils.class);
//
//    private ShiroPermissionsUtils() {
//
//    }
//
//    /**
//     * 检查当前用户是否有权限(任意一项)
//     *
//     * @param permissionCodes
//     *            任意权限
//     */
//    public static void checkPerissionAny(String... permissionCodes) {
//        if (permissionCodes == null || permissionCodes.length == 0) {
//            return;
//        }
//
//        // 获取用户信息
//        Subject currentUser = SecurityUtils.getSubject();
//        for (String permission : permissionCodes) {
//            boolean permitted = currentUser.isPermitted(permission);// 判断是否有权限
//            if (permitted) {
//                return;
//            }
//        }
//
//        // 没权限就抛出一个异常
//        Object principal = currentUser.getPrincipal();
//        if (principal instanceof User) {
//            User user = (User) principal;
//            log.error("user {} no permission !", user.getUsername());
//        }
//        throw new NoPermissionException("no permission ");
//    }
//
//    /**
//     * 检查当前用户是否有权限(所有的)
//     *
//     * @param permissionCodes
//     *            任意权限
//     * @throws NoPermissionException
//     */
//    public static void checkPerissionAll(String... permissionCodes) {
//        if (permissionCodes == null || permissionCodes.length == 0) {
//            return;
//        }
//
//        // 获取用户信息
//        Subject currentUser = SecurityUtils.getSubject();
//        for (String permission : permissionCodes) {
//            boolean permitted = currentUser.isPermitted(permission);// 判断是否有权限
//            if (!permitted) {
//                // 没权限就抛出一个异常
//                Object principal = currentUser.getPrincipal();
//                if (principal instanceof User) {
//                    User user = (User) principal;
//                    log.error("user {} no permission !", user.getUsername());
//                }
//                throw new NoPermissionException("no permission ");
//            }
//        }
//    }
//}