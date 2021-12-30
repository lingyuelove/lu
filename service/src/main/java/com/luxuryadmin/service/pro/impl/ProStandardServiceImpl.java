package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProStandard;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.mapper.pro.ProClassifyTypeMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProStandardMapper;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductClassifyAdd;
import com.luxuryadmin.param.pro.ParamProductClassifySunAddList;
import com.luxuryadmin.service.pro.ProStandardService;
import com.luxuryadmin.vo.pro.VoClassifyTypeSon;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import com.luxuryadmin.vo.pro.VoProClassify;
import org.jacoco.agent.rt.internal_1f1cc91.core.internal.flow.IFrame;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *商品规格表 serverImpl
 *@author zhangsai
 *@Date 2021-09-16 17:48:52
 */
@Service
@Transactional
public class ProStandardServiceImpl implements ProStandardService {


	/**
	 * 注入dao
	 */
	@Resource
	private ProStandardMapper proStandardMapper;
	@Resource
	private ProClassifyTypeMapper classifyTypeMapper;
	@Resource
	private ProProductMapper proProductMapper;
	@Resource
	private ProClassifyMapper proClassifyMapper;
	@Override
	public void addProductClassifyTypeList(ParamProductClassifyAdd productClassifyAdd) {
		Integer productId =productClassifyAdd.getProductId();
		if (LocalUtils.isEmptyAndNull(productId)){
			throw new MyException("商品id不能为空");
		}
		ProStandard proStandard = proStandardMapper.getByProductId(productId);
		ProStandard proStandardNew = getStandardByProductClassifyAdd(productClassifyAdd);
		//实体类为空则新增
		if (LocalUtils.isEmptyAndNull(proStandard)){

			if (!LocalUtils.isEmptyAndNull(proStandardNew)){
				proStandardNew.setFkShpShopId(productClassifyAdd.getShopId());
				proStandardNew.setFkProProductId(productClassifyAdd.getProductId());
				proStandardNew.setInsertTime(new Date());
				proStandardNew.setInsertAdmin(productClassifyAdd.getUserId());
				proStandardMapper.saveObject(proStandardNew);
			}

		}else {
			//如果不为空则编辑
			if (!LocalUtils.isEmptyAndNull(proStandardNew)){
				proStandardNew.setId(proStandard.getId());
				proStandardNew.setUpdateTime(new Date());
				proStandardNew.setUpdateAdmin(productClassifyAdd.getUserId());
				proStandardMapper.updateObject(proStandardNew);
			}
		}
	}

	@Override
	public VoClassifyTypeSon getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch) {
		String bizId = classifyTypeSearch.getBizId();
		Integer proId = classifyTypeSearch.getProductId();
		ProProduct product =null;
		if (!LocalUtils.isEmptyAndNull(bizId) && LocalUtils.isEmptyAndNull(proId)) {
			product = proProductMapper.getProProductByShopIdBizId(classifyTypeSearch.getShopId(), bizId);
			if (product != null) {
				classifyTypeSearch.setProductId(product.getId());
			}
		}else if (!LocalUtils.isEmptyAndNull(proId)){
			product = (ProProduct)proProductMapper.getObjectById(proId);
		}
		//判断商品是否已删除
		if (LocalUtils.isEmptyAndNull(product)){
		    return null;
        }
		ProStandard proStandard = proStandardMapper.getByProductId(product.getId());
		VoClassifyTypeSon voClassifyTypeSon =new VoClassifyTypeSon();
		if (LocalUtils.isEmptyAndNull(proStandard)){
			if ( product != null){
				List<VoClassifyTypeSonList> classifyTypeSons =getClassifyTypeSonListForProduct(product);
				voClassifyTypeSon.setClassifyTypeSonLists(classifyTypeSons);
				return voClassifyTypeSon;
			}
			return null;
		}
		List<VoClassifyTypeSonList> classifyTypeSonLists = getClassifyTypeSonList(proStandard);

		voClassifyTypeSon.setClassifyTypeSonLists(classifyTypeSonLists);
		voClassifyTypeSon.setPublicPrice(proStandard.getPublicPrice());
		if ( product != null){
			List<VoClassifyTypeSonList> classifyTypeSons =getClassifyTypeSonListForProduct(product);
			classifyTypeSons.addAll(classifyTypeSonLists);
			voClassifyTypeSon.setClassifyTypeSonLists(classifyTypeSons);
			return voClassifyTypeSon;
		}
		return voClassifyTypeSon;
	}

	@Override
	public void deleteStandard(Integer productId) {
		ProStandard proStandard = proStandardMapper.getByProductId(productId);
		if(!LocalUtils.isEmptyAndNull(proStandard)){
			proStandardMapper.deleteObject(proStandard.getId());
		}
	}

	public  List<VoClassifyTypeSonList> getClassifyTypeSonListForProduct(ProProduct product){
		List<VoClassifyTypeSonList> classifyTypeSonLists = new ArrayList<>();
		VoClassifyTypeSonList classifyTypeSonList1 =new VoClassifyTypeSonList();
		classifyTypeSonList1.setName("分类");
//        classifyTypeSonList1.setContent(product.getFkProClassifyCode());
		VoProClassify classify=proClassifyMapper.getProClassifyByType(product.getFkShpShopId(), null,product.getFkProClassifyCode());
		if (classify != null){
			classifyTypeSonList1.setContent(classify.getName());
		}
		classifyTypeSonLists.add(classifyTypeSonList1);
		if (!LocalUtils.isEmptyAndNull(product.getFkProClassifySubName())){
			VoClassifyTypeSonList classifyTypeSonList2 =new VoClassifyTypeSonList();
			classifyTypeSonList2.setName("品牌");
			classifyTypeSonList2.setContent(product.getFkProClassifySubName());
			classifyTypeSonLists.add(classifyTypeSonList2);
		}
		if (!LocalUtils.isEmptyAndNull(product.getFkProSubSeriesName())){
			VoClassifyTypeSonList classifyTypeSonList3 =new VoClassifyTypeSonList();
			classifyTypeSonList3.setName("系列");
			classifyTypeSonList3.setContent(product.getFkProSubSeriesName());
			classifyTypeSonLists.add(classifyTypeSonList3);
		}
		if (!LocalUtils.isEmptyAndNull(product.getFkProSeriesModelName())){
			VoClassifyTypeSonList classifyTypeSonList4 =new VoClassifyTypeSonList();
			classifyTypeSonList4.setName("型号");
			classifyTypeSonList4.setContent(product.getFkProSeriesModelName());
			classifyTypeSonLists.add(classifyTypeSonList4);
		}
		return classifyTypeSonLists;
	}
	/**
	 * 商品参数标准的转换
	 * @param proStandard
	 * @return
	 */
	public List<VoClassifyTypeSonList> getClassifyTypeSonList(ProStandard proStandard){
		List<VoClassifyTypeSonList> classifyTypeSonLists = new ArrayList<>();
		if (!LocalUtils.isEmptyAndNull(proStandard.getWatchCoreType())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("机芯类型");
			classifyTypeSonList.setContent(proStandard.getWatchCoreType());
			classifyTypeSonLists.add(classifyTypeSonList);
		}
		if (!LocalUtils.isEmptyAndNull(proStandard.getWatchcase())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("表壳材质");
			classifyTypeSonList.setContent(proStandard.getWatchcase());
			classifyTypeSonLists.add(classifyTypeSonList);

		}
		if (!LocalUtils.isEmptyAndNull(proStandard.getWatchcaseSize())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("表盘直径");
			classifyTypeSonList.setContent(proStandard.getWatchcaseSize());
			classifyTypeSonLists.add(classifyTypeSonList);
		}
		if (!LocalUtils.isEmptyAndNull(proStandard.getMaterial())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("材质");
			classifyTypeSonList.setContent(proStandard.getMaterial());
			classifyTypeSonLists.add(classifyTypeSonList);
		}
		if (!LocalUtils.isEmptyAndNull(proStandard.getObjectSize())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("尺寸");
			classifyTypeSonList.setContent(proStandard.getObjectSize());
			classifyTypeSonLists.add(classifyTypeSonList);
		}
		if (!LocalUtils.isEmptyAndNull(proStandard.getClothesSize())){
			VoClassifyTypeSonList classifyTypeSonList =new VoClassifyTypeSonList();
			classifyTypeSonList.setName("尺码");
			classifyTypeSonList.setContent(proStandard.getClothesSize());
			classifyTypeSonLists.add(classifyTypeSonList);
		}

		return classifyTypeSonLists;
	}

	/**
	 * 商品参数标准的转换
	 * @param productClassifyAdd
	 * @return
	 */
	public ProStandard getStandardByProductClassifyAdd(ParamProductClassifyAdd productClassifyAdd){
		ProStandard proStandardNew = new ProStandard();
		proStandardNew.setWatchCoreType("");
		proStandardNew.setWatchcase("");
		proStandardNew.setWatchcaseSize("");
		proStandardNew.setMaterial("");
		proStandardNew.setObjectSize("");
		proStandardNew.setClothesSize("");
		proStandardNew.setPublicPrice("");
		List<ParamProductClassifySunAddList> productClassifySunAddLists = productClassifyAdd.getProductClassifySunAddLists();
		productClassifySunAddLists.forEach(paramProductClassifySunAddList -> {
			Integer classifyTypeDetailId = paramProductClassifySunAddList.getId();
			ProClassifyType classifyTypeDetail = classifyTypeMapper.getObjectById(classifyTypeDetailId);
			if (!LocalUtils.isEmptyAndNull(classifyTypeDetail)){
				if ("机芯类型".equals(classifyTypeDetail.getName())){
					proStandardNew.setWatchCoreType(paramProductClassifySunAddList.getContent());
				}
				if ("表壳材质".equals(classifyTypeDetail.getName())){
					proStandardNew.setWatchcase(paramProductClassifySunAddList.getContent());
				}
				if ("表盘直径".equals(classifyTypeDetail.getName())){
					proStandardNew.setWatchcaseSize(paramProductClassifySunAddList.getContent());
				}
				if ("材质".equals(classifyTypeDetail.getName())){
					proStandardNew.setMaterial(paramProductClassifySunAddList.getContent());
				}
				if ("尺寸".equals(classifyTypeDetail.getName())){
					proStandardNew.setObjectSize(paramProductClassifySunAddList.getContent());
				}
				if ("尺码".equals(classifyTypeDetail.getName())){
					proStandardNew.setClothesSize(paramProductClassifySunAddList.getContent());
				}
				if ("官方指导价".equals(classifyTypeDetail.getName())){
					proStandardNew.setPublicPrice(paramProductClassifySunAddList.getContent());
				}
			}
		});
		if (LocalUtils.isEmptyAndNull(proStandardNew)){
			return null;
		}

		return proStandardNew;
	}
}
