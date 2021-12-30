package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.entity.pro.ProTempProductMove;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProTempProductMapper;
import com.luxuryadmin.mapper.pro.ProTempProductMoveMapper;
import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.pro.ParamTempMovePro;
import com.luxuryadmin.param.pro.ParamTempProductMoveQuery;
import com.luxuryadmin.service.pro.ProTempProductMoveService;
import com.luxuryadmin.vo.pro.VoMoveProductLoad;
import com.luxuryadmin.vo.pro.VoProTempProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 *临时仓商品移动历史表 serverImpl
 *@author zhangsai
 *@Date 2021-09-24 17:46:58
 */
@Service
public class ProTempProductMoveServiceImpl implements ProTempProductMoveService {


	/**
	 * 注入dao
	 */
	@Resource
	private ProTempProductMoveMapper proTempProductMoveMapper;

	@Resource
	private ProTempProductMapper proTempProductMapper;
	@Resource
	private ProProductMapper proProductMapper;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String moveProductToTemp(ParamTempMovePro paramTempMovePro) {
		Integer shopId = paramTempMovePro.getShopId();
		List<String> tempProductIdList = Arrays.asList(paramTempMovePro.getTempProductIds().split(";"));
		if (LocalUtils.isEmptyAndNull(tempProductIdList)){
			return null;
		}
		List<String> productNameList = new ArrayList<>();
		tempProductIdList.forEach(s -> {
			VoProTempProduct tempProduct = proTempProductMapper.getNewProTempProduct(shopId,Integer.parseInt(paramTempMovePro.getRemoveTempId()),Integer.parseInt(s));
			if (tempProduct == null){
				throw new MyException("暂无此临时仓商品");
			}
			String productName= this.addMove(paramTempMovePro,tempProduct);
			if (!LocalUtils.isEmptyAndNull(productName)){
				productNameList.add(productName);
			}
		});
		//如果以后要返回异常商品的名字可以直接使用
		if (!LocalUtils.isEmptyAndNull(productNameList)){
			return "存在异常商品";
		}
		return null;
	}
	@Transactional(rollbackFor = Exception.class)
	public String addMove(ParamTempMovePro paramTempMovePro ,VoProTempProduct tempProduct){
		Integer enterTempId = Integer.parseInt(paramTempMovePro.getEnterTempId());

		VoProTempProduct voProTempProduct = proTempProductMapper.getNewProTempProduct(paramTempMovePro.getShopId(),enterTempId, tempProduct.getProId());
		//转入仓库无此商品
		if (LocalUtils.isEmptyAndNull(voProTempProduct)){
			//新增记录 临时仓商品编辑
			ProTempProductMove tempProductMove =new ProTempProductMove();
			tempProductMove.setFkProProductId(tempProduct.getProId());
			tempProductMove.setSurplusNum(0);
			tempProductMove.setRemoveNum(tempProduct.getNum());
			this.addMoveTempProduct(paramTempMovePro,tempProductMove);
			ProTempProduct proTempProduct =new ProTempProduct();
			//临时仓商品 编辑临时仓id
			proTempProduct.setFkProTempId(enterTempId);
			proTempProduct.setId(tempProduct.getId());
			proTempProductMapper.updateObject(proTempProduct);
			return null;
		}else {
			//原仓库有此商品 判断原库存和现库存
			ProProduct proProduct = (ProProduct)proProductMapper.getObjectById(tempProduct.getProId());
			if (proProduct == null){
				throw new MyException("此商品不存在");
			}
			if (LocalUtils.isEmptyAndNull( voProTempProduct.getNum())){
				voProTempProduct.setNum(0);
			}
			if (LocalUtils.isEmptyAndNull(tempProduct.getNum())){
				tempProduct.setNum(0);
			}
			Integer nowTotalNum = voProTempProduct.getNum()+tempProduct.getNum();
			Integer productNum = proProduct.getTotalNum();
			//原库存大于等于要添加的库存
			if (productNum>=nowTotalNum){
				//新增记录 临时仓商品编辑
				ProTempProductMove tempProductMove =new ProTempProductMove();
				tempProductMove.setFkProProductId(tempProduct.getProId());
				tempProductMove.setSurplusNum(0);
				tempProductMove.setRemoveNum(tempProduct.getNum());
				this.addMoveTempProduct(paramTempMovePro,tempProductMove);
				//临时仓商品 编辑移入临时仓商品数量 删除移出临时仓的商品
				ProTempProduct tempProductForMove =new ProTempProduct();
				tempProductForMove.setNum(nowTotalNum);
				tempProductForMove.setId(voProTempProduct.getId());
				proTempProductMapper.updateObject(tempProductForMove);
				proTempProductMapper.deleteObject(tempProduct.getId());

			}else {
				//原库存小于要添加的库存
				//查询多余库存
				Integer surplusNum = nowTotalNum -productNum;
				if (surplusNum <0){
					surplusNum = 0;
				}
				//转入的数量 商品总数 -转入仓原有数量
				Integer removeNum =productNum - voProTempProduct.getNum();
				if (removeNum <0){
					removeNum = 0;
				}
				//新增记录 临时仓商品编辑
				ProTempProductMove tempProductMove =new ProTempProductMove();
				tempProductMove.setFkProProductId(tempProduct.getProId());
				tempProductMove.setSurplusNum(surplusNum);
				tempProductMove.setRemoveNum(removeNum);
				this.addMoveTempProduct(paramTempMovePro,tempProductMove);
				//临时仓商品 编辑移入临时仓商品数量 删除移出临时仓的商品
				ProTempProduct tempProductForMove =new ProTempProduct();
				tempProductForMove.setId(voProTempProduct.getId());
				tempProductForMove.setNum(productNum);
				proTempProductMapper.updateObject(tempProductForMove);
				proTempProductMapper.deleteObject(tempProduct.getId());
				return proProduct.getName();
			}
			return null;
		}

	}
	@Transactional(rollbackFor = Exception.class)
	public void addMoveTempProduct(ParamTempMovePro paramTempMovePro,ProTempProductMove tempProductMove){
		Integer enterTempId = Integer.parseInt(paramTempMovePro.getEnterTempId());
		Integer removeTempId = Integer.parseInt(paramTempMovePro.getRemoveTempId());
		tempProductMove.setFkShpShopId(paramTempMovePro.getShopId());
		tempProductMove.setFkRemoveProTempId(removeTempId);
		tempProductMove.setFkEnterProTempId(enterTempId);
		tempProductMove.setInsertAdmin(paramTempMovePro.getUserId());
		tempProductMove.setInsertTime(new Date());
		proTempProductMoveMapper.saveObject(tempProductMove);
	}
	@Override
	public List<VoMoveProductLoad> listMoveProductToTemp(ParamTempProductMoveQuery paramTempProductMoveQuery) {
		List<VoMoveProductLoad> voProductLoadList = proProductMapper.listMoveProductToTemp(paramTempProductMoveQuery);
		if (LocalUtils.isEmptyAndNull(voProductLoadList)){
			return voProductLoadList;
		}
		voProductLoadList.forEach(voProductLoad -> {
			if (!LocalUtils.isEmptyAndNull(voProductLoad.getMoveUserName())){
				voProductLoad.setMoveUserName("操作人员: "+voProductLoad.getMoveUserName());
			}
			if (!LocalUtils.isEmptyAndNull(voProductLoad.getEnterTempName())){
				voProductLoad.setEnterTempName("现商品所在仓: "+voProductLoad.getEnterTempName());
			}
			if (!LocalUtils.isEmptyAndNull(voProductLoad.getRemoveTempName())){
				voProductLoad.setRemoveTempName("原商品所在仓: "+voProductLoad.getRemoveTempName());
			}
			if (!LocalUtils.isEmptyAndNull(voProductLoad.getMoveTime())){
				voProductLoad.setMoveTime("转出时间: "+voProductLoad.getMoveTime());
			}

		});
		return voProductLoadList;
	}
}
