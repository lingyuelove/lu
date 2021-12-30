package com.luxuryadmin.service.biz.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.biz.BizLeaguerAdd;
import com.luxuryadmin.entity.biz.BizShopRecommend;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.mapper.biz.BizLeaguerAddMapper;
import com.luxuryadmin.mapper.biz.BizLeaguerMapper;
import com.luxuryadmin.mapper.biz.BizShopRecommendMapper;
import com.luxuryadmin.param.biz.ParamLeaguerRecommendBySearch;
import com.luxuryadmin.param.biz.ParamRecommendAdminBySearch;
import com.luxuryadmin.param.biz.ParamShopRecommendAdd;
import com.luxuryadmin.service.biz.BizShopRecommendService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.biz.*;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 *添加友商推荐 serverImpl
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Service
public class BizShopRecommendServiceImpl   implements BizShopRecommendService {

	/**
	 * 注入dao
	 */
	@Resource
	private BizShopRecommendMapper bizShopRecommendMapper;

	@Resource
	private BizLeaguerMapper bizLeaguerMapper;
	@Resource
	private BizLeaguerAddMapper bizLeaguerAddMapper;
	@Autowired
	private ShpShopService shpShopService;
	@Autowired
	protected ServicesUtil servicesUtil;
	@Override
	public VoLeaguerRecommendPage getRecommendLeaguerList(ParamLeaguerRecommendBySearch leaguerRecommendBySearch) {
		PageHelper.startPage(Integer.parseInt(leaguerRecommendBySearch.getPageNum()), leaguerRecommendBySearch.getPageSize());
		List<VoLeaguerRecommend> leaguerRecommends = bizShopRecommendMapper.getRecommendLeaguerList(leaguerRecommendBySearch.getShopId());
		if (leaguerRecommends != null && leaguerRecommends.size() >0){
			leaguerRecommends.forEach(leaguerRecommend ->{
//				//缩略图
//				String coverImgUrl = leaguerRecommend.getCoverImgUrl();
//				if (!LocalUtils.isEmptyAndNull(coverImgUrl) && !coverImgUrl.contains("http")) {
//					leaguerShop.setCoverImgUrl(servicesUtil.formatImgUrl(coverImgUrl, true));
//				}
				//缩略图
				String headImgUrl = leaguerRecommend.getHeadImgUrl();
				leaguerRecommend.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl, false));
				BizLeaguerAdd bizLeaguer =bizLeaguerAddMapper.getLeaguerAddByShopIdAndLeaguerShopId(leaguerRecommendBySearch.getShopId(),leaguerRecommend.getShopId());
				if(bizLeaguer == null){
					leaguerRecommend.setLeaguerState("0");
				}else {
					leaguerRecommend.setLeaguerState("10");
				}

			});
		}
		PageInfo<VoOrganizationPageByApp> pageInfo = new PageInfo(leaguerRecommends);
		VoLeaguerRecommendPage leaguerRecommendPage = new VoLeaguerRecommendPage();
		leaguerRecommendPage.setPageNum(pageInfo.getPageNum());
		leaguerRecommendPage.setPageSize(pageInfo.getPageSize());
		if (pageInfo.getNextPage() > 0) {
			leaguerRecommendPage.setHasNextPage(true);
		} else {
			leaguerRecommendPage.setHasNextPage(false);
		}
		leaguerRecommendPage.setList(leaguerRecommends);
		return leaguerRecommendPage;
	}

	@Override
	public List<VoLeaguerRecommend> getByShowRecommend(Integer shopId) {
		List<VoLeaguerRecommend> leaguerRecommends=bizShopRecommendMapper.getByShowRecommend( shopId);
		if (leaguerRecommends != null && leaguerRecommends.size() >0){
			leaguerRecommends.forEach(leaguerRecommend ->{
				//缩略图
				String headImgUrl = leaguerRecommend.getHeadImgUrl();
				leaguerRecommend.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl, true));

			});
		}
		return leaguerRecommends;
	}

	@Override
	public VoRecommendAdminPage getRecommendAdminPage(ParamRecommendAdminBySearch recommendAdminBySearch) {
		PageHelper.startPage(recommendAdminBySearch.getPageNum(), recommendAdminBySearch.getPageSize());
		List<VoRecommendAdminList> recommendAdminLists=bizShopRecommendMapper.getRecommendAdminPage( recommendAdminBySearch);
		PageInfo<VoOrganizationPageByApp> pageInfo = new PageInfo(recommendAdminLists);
		VoRecommendAdminPage recommendAdminPage = new VoRecommendAdminPage();
		recommendAdminPage.setPageNum(pageInfo.getPageNum());
		recommendAdminPage.setPageSize(pageInfo.getPageSize());
		if (pageInfo.getNextPage() > 0) {
			recommendAdminPage.setHasNextPage(true);
		} else {
			recommendAdminPage.setHasNextPage(false);
		}
		recommendAdminPage.setList(recommendAdminLists);
		return recommendAdminPage;
	}

	@Override
	public void addShopRecommend(ParamShopRecommendAdd shopRecommendAdd) {
		List<String> shopId = shopRecommendAdd.getShopId();
		if (shopId != null && shopId.size()>0){
			shopId.forEach(s -> {
				BizShopRecommend recommend =bizShopRecommendMapper.getByShopId(Integer.parseInt(s));
				Boolean addType = true;
				if (recommend != null){
					addType = false;
				}

				ShpShop shpShop = shpShopService.getShpShopById(s);
				if (shpShop == null){
					addType =false;
				}
				if (addType){
					BizShopRecommend shopRecommend = new BizShopRecommend();
					shopRecommend.setFkShpShopId(Integer.parseInt(s));
					shopRecommend.setRecommendNum(-1);
					shopRecommend.setInsertTime(new Date());
					shopRecommend.setInsertAdmin(-1);
					shopRecommend.setFkShpShopNumber(shpShop.getNumber());
					shopRecommend.setState("1");
					bizShopRecommendMapper.saveObject(shopRecommend);
				}

			});
		}


	}

	@Override
	public void deleteShopRecommend(Integer id) {
		bizShopRecommendMapper.deleteObject(id);
	}
}
