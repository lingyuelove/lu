package com.luxuryadmin.service.util.mongo;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.vo.mongo.UserShareAppletCensus;
import com.zaxxer.hikari.util.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Date;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.service.util.mongo
 * @ClassName: BaseSearch
 * @Author: ZhangSai
 * Date: 2021/11/9 14:50
 */
public class BaseSearch {


    public static Criteria buildBaseCriteria(TenantBaseStatistics tenantBaseStatistics, String dateFieldName) {
        Criteria matchExample;
        if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getUserId())){
            matchExample = Criteria.where("del").is("0");
        }else {
            matchExample = Criteria.where(tenantBaseStatistics.getUserIdName()).is(tenantBaseStatistics.getUserId()).and("del").is("0");

        }

        if (!LocalUtils.isEmptyAndNull(tenantBaseStatistics.getStartTime()) && !LocalUtils.isEmptyAndNull(tenantBaseStatistics.getEndTime())) {
            matchExample.and(dateFieldName)
                    .lte(tenantBaseStatistics.getEndTime())
                    .gte(tenantBaseStatistics.getStartTime());
        }
        else if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getStartTime()) && !LocalUtils.isEmptyAndNull(tenantBaseStatistics.getEndTime())) {
            matchExample.and(dateFieldName).lte(tenantBaseStatistics.getEndTime());
        } else if (!LocalUtils.isEmptyAndNull(tenantBaseStatistics.getStartTime()) && LocalUtils.isEmptyAndNull(tenantBaseStatistics.getEndTime())) {
            matchExample.and(dateFieldName).gte(tenantBaseStatistics.getStartTime());
        }
        if (!LocalUtils.isEmptyAndNull(tenantBaseStatistics.getNickname())){
            matchExample.and("nickname").regex("^.*" + tenantBaseStatistics.getNickname() + ".*$");
        }
        if (!LocalUtils.isEmptyAndNull(tenantBaseStatistics.getPhone())){
            matchExample.and("phone").regex("^.*" + tenantBaseStatistics.getPhone() + ".*$");
        }
//        matchExample.and("del").();

        return matchExample;
    }

    /**
     *
     * @param mongoTemplate ??????mongo???
     * @param tenantBaseStatistics ?????????
     * @param
     * @param collectionName mongo?????????
     * @param groupOperation ????????????????????????
     * @param c mongo???
     * @param <T>
     * @return
     */
    public static  <T> Object sumMongo(MongoTemplate mongoTemplate,TenantBaseStatistics tenantBaseStatistics,String collectionName,String[] files,   GroupOperation groupOperation, Class<T> c) {

        if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getStartTime())) {
            throw new MyException("??????????????????");
        }
        if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getEndTime())) {
            throw new MyException("??????????????????");
        }
        Criteria criteria =BaseSearch.buildBaseCriteria(tenantBaseStatistics,tenantBaseStatistics.getDateFieldName());
        MatchOperation aggregation = Aggregation.match(criteria);


        //order by ??????
//        SortOperation sortOperation =Aggregation.sort(Sort.Direction.DESC, "userId");

        Aggregation agg;
        if (files != null){
            //??????????????????
            ProjectionOperation projectionOperation = Aggregation.project(files);
            agg = Aggregation.newAggregation(aggregation,groupOperation,projectionOperation);
        }else {
             agg = Aggregation.newAggregation(aggregation,groupOperation);
        }
        AggregationResults<T> results = mongoTemplate.aggregate(agg, collectionName, c);
        List<T> mappedResults = results.getMappedResults();
        return mappedResults;
    }


    public static  <T> Object sumMongoOld(MongoTemplate mongoTemplate,TenantBaseStatistics tenantBaseStatistics,String collectionName , Class<T> c) {

        if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getStartTime())) {
            throw new MyException("??????????????????");
        }
        if (LocalUtils.isEmptyAndNull(tenantBaseStatistics.getEndTime())) {
            throw new MyException("??????????????????");
        }
        //criteria??????
        Criteria criteria =BaseSearch.buildBaseCriteria(tenantBaseStatistics,tenantBaseStatistics.getDateFieldName());
        MatchOperation aggregation = Aggregation.match(criteria);
        //group ??????fields???????????? ??????????????????.sum(fieldName).as(StringUtil.isBlank(anotherName) ? fieldName : anotherName))
        String[] fields = {"userId","jobWxId"};
        GroupOperation groupOperation = Aggregation.group(fields);
        //order by ??????
        SortOperation sortOperation =Aggregation.sort(Sort.Direction.DESC, "userId");
        Aggregation agg = Aggregation.newAggregation(aggregation,groupOperation,sortOperation);

        AggregationResults<T> results = mongoTemplate.aggregate(agg, collectionName, c);
        List<T> mappedResults = results.getMappedResults();
        return mappedResults;
    }
}
