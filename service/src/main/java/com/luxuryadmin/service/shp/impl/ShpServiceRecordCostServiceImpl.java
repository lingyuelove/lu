package com.luxuryadmin.service.shp.impl;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import com.luxuryadmin.mapper.shp.ShpServiceRecordCostMapper;
import com.luxuryadmin.param.shp.ParamShpServiceRecordAdd;
import com.luxuryadmin.service.shp.ShpServiceRecordCostService;
import com.luxuryadmin.vo.shp.VoServiceRecordCost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 *服务成本表 serverImpl
 *@author zhangsai
 *@Date 2021-10-19 14:48:46
 */
@Service
@Transactional
public class ShpServiceRecordCostServiceImpl   implements ShpServiceRecordCostService {


	/**
	 * 注入dao
	 */
	@Resource
	private ShpServiceRecordCostMapper shpServiceRecordCostMapper;


	@Override
	public void addOrUpdate(ParamShpServiceRecordAdd serviceRecordAdd) {
		List<VoServiceRecordCost> shpServiceRecordCosts = shpServiceRecordCostMapper.listCostForServiceRecordId(serviceRecordAdd.getId());
		if (!LocalUtils.isEmptyAndNull(shpServiceRecordCosts)){
			shpServiceRecordCostMapper.deleteByServiceRecordId(serviceRecordAdd.getId());
		}
		if (!LocalUtils.isEmptyAndNull(serviceRecordAdd.getCostAddOrUps())){
			serviceRecordAdd.getCostAddOrUps().forEach(paramServiceRecordCostAddOrUp -> {
				ShpServiceRecordCost serviceRecordCost =new ShpServiceRecordCost();
				serviceRecordCost.setFkShpShopId(serviceRecordAdd.getShopId());
				serviceRecordCost.setInsertTime(new Date());
				serviceRecordCost.setFkShpServiceRecordId(serviceRecordAdd.getId());
				serviceRecordCost.setRepairContent(paramServiceRecordCostAddOrUp.getRepairContent());
				serviceRecordCost.setServiceCost(new Double(paramServiceRecordCostAddOrUp.getServiceCost()));
				shpServiceRecordCostMapper.saveObject(serviceRecordCost);
			});
		}



	}

	@Override
	public List<VoServiceRecordCost> listCostForServiceRecordId(Integer serviceRecordId) {
		List<VoServiceRecordCost> shpServiceRecordCosts = shpServiceRecordCostMapper.listCostForServiceRecordId(serviceRecordId);
		return shpServiceRecordCosts;
	}
}
