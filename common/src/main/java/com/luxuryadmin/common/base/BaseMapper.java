package com.luxuryadmin.common.base;


import java.util.List;
import java.util.Map;

/**
 * 基础Mapper(还需在XML文件里，有对应的SQL语句)<br/>
 * 所有的Mapper层; 继承此类; 特殊情况除外(需说明)<br/>
 * 获取单个对象的方法用 get 做前缀<br/>
 * 获取多个对象的方法用 list 做前缀<br/>
 * 获取统计值的方法用 count 做前缀<br/>
 * 插入的方法用 save 做前缀<br/>
 * 删除的方法用 delete 做前缀<br/>
 * 修改的方法用 update 做前缀
 *
 * @author monkey king
 * @version @param <T>
 * @date: 2019-12-01 04:30:16 <br/>
 */
public interface BaseMapper<T> {

    /**
     * 保存对象
     *
     * @param t 对象
     * @return: long 返回受影响行数
     * @author: monkey king
     */
    int saveObject(T t);

    /**
     * 保存对象
     *
     * @param map Map
     * @return: long 返回受影响行数
     * @author: monkey king
     */
    int saveObject(Map<String, Object> map);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int saveBatch(List<T> list);


    /**
     * 更新对象
     *
     * @param t 对象
     * @return: int
     * @author: monkey king
     */
    int updateObject(T t);

    /**
     * 更新对象
     *
     * @param map Map
     * @return: int
     * @author: monkey king
     */
    int updateObject(Map<String, Object> map);

    /**
     * 删除对象
     *
     * @param id ID
     * @return: int
     * @author: monkey king
     */
    int deleteObject(Object id);

    /**
     * 删除对象
     *
     * @param map Map
     * @return: int
     * @author: monkey king
     */
    int deleteObject(Map<String, Object> map);


    /**
     * 获得一个对象
     *
     * @param id id
     * @return: T 对象
     * @author: monkey king
     */
    T getObjectById(Object id);


    /**
     * 根据id获取多个对象;
     *
     * @param id
     * @return: java.util.List<T> 多个对象
     * @author: monkey king
     */
    List<T> listObject(Object id);


    /**
     * 获取多个对象
     *
     * @param map Map
     * @return: java.util.List<T> 多个对象
     * @author: monkey king
     */
    List<T> listObject(Map<String, Object> map);


    /**
     * 统计对象数量
     *
     * @param map Map
     * @return: int 总数
     * @author: monkey king
     */
    int countObject(Map<String, Object> map);

    /**
     * 统计对象数量
     *
     * @param t 对象
     * @return: int 总数
     * @author: monkey king
     */
    int countObject(T t);

    /**
     * 统计对象总数
     *
     * @return: int 总数
     * @author: monkey king
     */
    int countObject();
}
