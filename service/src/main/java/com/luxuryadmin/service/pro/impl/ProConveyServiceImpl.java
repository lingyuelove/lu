package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RandomNumber;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.mapper.pro.ProConveyMapper;
import com.luxuryadmin.mapper.pro.ProConveyProductMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProConveyProductService;
import com.luxuryadmin.service.pro.ProConveyService;
import com.luxuryadmin.vo.pro.VoConvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 *商品传送表 serverImpl
 *@author zhangsai
 *@Date 2021-11-22 15:12:44
 */
@Service
@Transactional
public class ProConveyServiceImpl implements ProConveyService {


	/**
	 * 注入dao
	 */
	@Resource
	private ProConveyMapper proConveyMapper;

	@Resource
	private ProConveyProductMapper proConveyProductMapper;
	@Autowired
	private ProConveyProductService proConveyProductService;
	@Resource
	private ProProductMapper proProductMapper;
	@Autowired
	protected RedisUtil redisUtil;
	@Override
	public void addConvey(ParamConveyAdd conveyAdd) {
		ProConvey convey =new ProConvey();
		convey.setFkSendShopId(conveyAdd.getShopId());
		convey.setInsertAdmin(conveyAdd.getUserId());
		convey.setInsertTime(new Date());
		convey.setSendState("0");
		convey.setName(conveyAdd.getName());
		proConveyMapper.saveObject(convey);
		//设置唯一编号 id+生成英文字母
		Integer id = convey.getId();
		Integer length =8- Integer.toString(id).length();
		String number = RandomNumber.getRandomString(length);
		number =id+number;
		convey.setNumber(number);
		proConveyMapper.updateObject(convey);
	}

	/**
	 * 寄卖传送删除逻辑说明
	 * 未确认删除
	 * 寄卖传送逻辑删除
	 * 关联商品移除此寄卖传送
	 * 仓库商品状态变为普通商品 非寄卖传送发送方
	 * 已确认删除
	 * 假删 发送方删除旨在发送方不显示接收方同理
	 * 假删不删除寄卖传送商品 不修改仓库寄卖传送商品状态
	 * @param conveyDelete
	 */
	@Override
	public void deleteConvey(ParamConveyDelete conveyDelete) {
		ProConvey convey =proConveyMapper.getObjectById(conveyDelete.getId());
		//判断传送是否存在
		if (LocalUtils.isEmptyAndNull(convey)){
			throw new MyException("该传送不存在或已被删除");
		}

		//发送方已确认 发送方 删除 只将寄卖方设置为删除 不删除商品
		if ("2".equals(convey.getSendState()) && "send".equals(conveyDelete.getType())){
			convey.setSendDel("1");
			proConveyMapper.updateObject(convey);
		}else if
		//接收方已确认 接收方 删除 只将接收方设置为删除 不删除商品
		 ("1".equals(convey.getReceiveState()) && "receive".equals(conveyDelete.getType())){
			convey.setReceiveDel("1");
			proConveyMapper.updateObject(convey);
		}else {
			//判断此传送是否已被提取 接收方是否已接收
			//待提取已提取未确定 删除 逻辑彻底删除 已确定删除 假删
			//状态设置删除
			proConveyMapper.deleteObject(convey.getId());
			//删除寄卖转移商品 在寄卖转移中移除商品
			proConveyProductMapper.updateProductForConveyId(convey.getId());
			proConveyProductMapper.removeProductForConveyId(convey.getId());
		}
	}

	/**
	 * 寄卖传送接收逻辑
	 * 每一个寄卖传送只能被一个店铺接收一次（如果以后要改为多个店铺 请谨慎考虑 需要修改多个关联逻辑）
	 * 寄卖传送确认接收逻辑
	 * 接收方店铺新增商品 添加商品寄卖传送类型为寄卖接收
	 * 清空该寄卖传送没有库存的商品
	 * @param conveyUpdate
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveConvey(ParamConveyUpdate conveyUpdate) {
		ProConvey convey =proConveyMapper.getByNumber(conveyUpdate.getNumber());
		//判断传送是否存在
		if (LocalUtils.isEmptyAndNull(convey)){
			throw new MyException("该传送不存在或已被删除");
		}
		//接收
		if(LocalUtils.isEmptyAndNull(conveyUpdate.getReceiveState())){

			if (Objects.equals(convey.getFkSendShopId(),conveyUpdate.getShopId())){
				throw new MyException("发送店铺和接收店铺不能为同一个店铺");
			}
			if (!Objects.equals(convey.getSendState(),"0")){
				throw new MyException("此传送已被接收");
			}

			convey.setSendState("1");
			convey.setReceiveState("0");
			convey.setReceiveUserId(conveyUpdate.getUserId());
			convey.setReceiveTime(new Date());
			convey.setFkReceiveShopId(conveyUpdate.getShopId());
			convey.setUpdateAdmin(conveyUpdate.getUserId());
			convey.setUpdateTime(new Date());
		}else {
			//已确认
			convey.setSendState("2");
			convey.setReceiveState("1");
			convey.setUpdateAdmin(conveyUpdate.getUserId());
			convey.setUpdateTime(new Date());
			//确认之后删除之前无库存的商品 商品同步到接收方的寄卖商品
			//1、移除锁单无库存 开单无库存的商品
			proConveyProductMapper.removeProductForNoNum(convey.getId());
			//确认后把商品数量和商品价格赋值到寄卖传送商品上
			proConveyProductService.updateConveyList(convey.getId());
			//把寄卖传送商品新增至当前店铺的仓库 此商品类型添加为不可编辑数量和类型的寄卖商品
			proConveyProductService.addProductForConvey(convey,conveyUpdate.getPlatform());

		}
		proConveyMapper.updateObject(convey);
	}

	@Override
	public List<VoConvey> listConvey(ParamConveyQuery paramConveyQuery) {
		List<VoConvey> conveyList = proConveyMapper.listConvey(paramConveyQuery);
		if (LocalUtils.isEmptyAndNull(conveyList)){
			return conveyList;
		}
		conveyList.forEach(voConvey -> {
			//给寄卖传送的商品总数量和总价格赋值
			getConvey(voConvey,paramConveyQuery);
		});
		return conveyList;
	}

	/**
	 * 获取单个寄卖传送的结算价*数量 总价格和总数量
	 * @param voConvey
	 * @param paramConveyQuery
	 */
	public void getConvey(VoConvey voConvey,ParamConveyQuery paramConveyQuery){
		//查询数量和价格
		ParamConveyProductQuery conveyProductQuery =new ParamConveyProductQuery();
		conveyProductQuery.setShopId(paramConveyQuery.getShopId());
		conveyProductQuery.setConveyId(voConvey.getId());
		String defaultPrice;
		String conveyId =voConvey.getId().toString();
		//判断redis是否有key值
		if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId))) {
			//获取接收参数---是否为登录后初次请求接口
			defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId));
		} else {
			defaultPrice = "tradePrice";
		}
		conveyProductQuery.setDefaultPrice(defaultPrice);
		if (!LocalUtils.isEmptyAndNull(voConvey.getReceiveState()) && "1".equals(voConvey.getReceiveState())){
			conveyProductQuery.setType(paramConveyQuery.getType());
		}
		//获取寄卖传送商品的总价值和总数量 会跟着库存的变动而变动
		Map<String, Object> objectMap= proProductMapper.getConveyProductPriceCensus(conveyProductQuery);
		BigDecimal totalNum = new BigDecimal(0);
		BigDecimal totalPrice = new BigDecimal(0);
		if (!LocalUtils.isEmptyAndNull(objectMap)) {
			totalNum = (BigDecimal) objectMap.get("totalNum");

			totalPrice = (BigDecimal) objectMap.get("totalPrice");

		}

		voConvey.setTotalCount(totalNum.intValue());
		voConvey.setTotalPrice(totalPrice);
	}
	@Override
	public ProConvey getConveyDetail(Integer id) {
		ProConvey convey =proConveyMapper.getObjectById(id);
		return convey;
	}

}
