package com.luxuryadmin.admin.mongo;

import com.luxuryadmin.admin.mongo.entity.RunoobDO;
import com.luxuryadmin.admin.mongo.entity.TestBaseDO;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.vo.mongo.UserShareAppletCensus;
import com.mongodb.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 添加工作人员,绑定商家联盟小程序代理
 *
 * @author monkey king
 * @date 2021-09-14 19:24:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/mongo")
@Api(tags = {"1.啥时候"}, description = "/op/union | 商家联盟代理 ")
public class TestController extends BaseController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @ApiOperation(
            value = "查询数据;",
            notes = "查询数据;",
            httpMethod = "GET")
    @RequestMapping(value = "/listMongo", method = RequestMethod.GET)
    public BaseResult listMongo(@RequestParam String name) {
        Criteria criteria = new Criteria();
        if (StringUtil.isNotBlank(name)) {
            criteria.and("name").regex("^.*" + name + ".*$");
        }
        Query query = new Query();
        query.addCriteria(criteria);
        List<RunoobDO> mongoDOS = mongoTemplate.find(query, RunoobDO.class);

        /*Aggregation agg = Aggregation.newAggregation(
                Aggregation.group().sum("totalShopCount").as("totalShopCount"));
        AggregationResults<UserShareAppletCensus> results = mongoTemplate.aggregate(agg, "user_share_applet_census",
                UserShareAppletCensus.class);
        System.out.println(results);*/
        Object o = sumMongo("totalShopCount", "totalShopCount", UserShareAppletCensus.class);
        UserShareAppletCensus o1 = (UserShareAppletCensus) o;
        return BaseResult.okResult(o1);
    }

    @ApiOperation(
            value = "查询数据;",
            notes = "查询数据;",
            httpMethod = "GET")
    @RequestMapping(value = "/listMongoNew", method = RequestMethod.GET)
    public BaseResult get(){

        //查询参数
        //Criteria.where("insertTime").gte("2021-11-03 00:00:00").lte("2021-11-11 00:00:00")
        //.and("issuedStatus").is(1)
        MatchOperation aggregation = Aggregation.match(Criteria.where("insertTime").gte(DateUtil.format(DateUtil.getTodayTime())).lte(DateUtil.format(new Date())));
        //group by 用法sum查询数量
        //group by 用法sum查询数量
//        String[] fields = {"userId","buyMemForMonthCount","buyMemShopCount","registerShopCount","buyMemForHundredCount"};
        String[] fields = {"userId"};
        Fields groupFields = Fields.fields(fields );

//        GroupOperation groupOperation =Aggregation.group(fields);
        GroupOperation groupOperation =Aggregation.group(fields).sum("buyMemForMonthCount").as("buyMemForMonthCount").sum("buyMemShopCount").as("buyMemShopCount");
        //order by 用法
//        SortOperation sortOperation =Aggregation.sort(Sort.Direction.DESC, "userId");
        Aggregation agg = Aggregation.newAggregation(aggregation,groupOperation,Aggregation.skip(0L));
        AggregationResults<UserShareAppletCensus> results = mongoTemplate.aggregate(agg, "user_share_applet_census", UserShareAppletCensus.class);

        List<UserShareAppletCensus> mappedResults = results.getMappedResults();


        return BaseResult.okResult(mappedResults);

    }

    /**
     * @param fieldName   列名
     * @param anotherName 别名
     * @param c           类
     * @return
     */
    public <T> Object sumMongo(String fieldName, String anotherName, Class<T> c) {
        if (StringUtil.isBlank(fieldName)) {
            throw new MyException("参数错误");
        }
        Document anno = c.getAnnotation(Document.class);
        if (anno == null) {
            throw new MyException("参数错误");
        }
        String value = anno.value();
        if (StringUtil.isBlank(value)) {
            throw new MyException("属性值错误");
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group().sum(fieldName).as(StringUtil.isBlank(anotherName) ? fieldName : anotherName));
        AggregationResults<T> results = mongoTemplate.aggregate(agg, value, c);
        List<T> mappedResults = results.getMappedResults();
        return mappedResults != null && mappedResults.size() > 0 ? mappedResults.get(0) : c;
    }

    @ApiOperation(
            value = "新增数据;",
            notes = "新增数据;",
            httpMethod = "GET")
    @RequestMapping(value = "/saveMongo", method = RequestMethod.GET)
    public BaseResult getUserInfoByUsername(@RequestParam @NotBlank String name) {
        RunoobDO mongoDO = new RunoobDO();
        mongoDO.setName(name);
        mongoTemplate.save(mongoDO);
        return BaseResult.okResult("成功");
    }

    @ApiOperation(
            value = "新增数据;",
            notes = "新增数据;",
            httpMethod = "GET")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public BaseResult test() {
        TestBaseDO testBaseDO = new TestBaseDO();
        testBaseDO.setName("name");
        testBaseDO.setOne(1);
        testBaseDO.setTwo(2);
        mongoTemplate.save(testBaseDO);
        return BaseResult.okResult("成功");


    }


/*    Query query1 = new Query();

        query1.addCriteria(matchExample);
    List<TenantUserRechargeRecordDO> tenantUserRechargeRecordDOS = mongoTemplate.find(query1, TenantUserRechargeRecordDO.class);

    private Criteria buildBaseCriteria(TenantBusinessBaseStatisticsGetDTO req, String dateFieldName) {
        Criteria matchExample = Criteria.where("tenantId").is(req.getTenantId());
        if (Objects.nonNull(req.getStatisticsDateUpper()) && Objects.nonNull(req.getStatisticsDateLower())) {
            matchExample.and(dateFieldName)
                    .lte(req.getStatisticsDateUpper().getTime())
                    .gte(req.getStatisticsDateLower().getTime());
        } else if (Objects.nonNull(req.getStatisticsDateUpper()) && Objects.isNull(req.getStatisticsDateLower())) {
            matchExample.and(dateFieldName).lte(req.getStatisticsDateUpper().getTime());
        } else if (Objects.nonNull(req.getStatisticsDateLower()) && Objects.isNull(req.getStatisticsDateUpper())) {
            matchExample.and(dateFieldName).gte(req.getStatisticsDateLower().getTime());
        }
        return matchExample;
    }*/


    public static void main(String[] args) {
        String v = SpringVersion.getVersion();
        System.out.println(v);
    }
}
