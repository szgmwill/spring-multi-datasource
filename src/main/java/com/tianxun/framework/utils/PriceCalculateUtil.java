/**
 * 
 */
package com.tianxun.framework.utils;

import java.math.BigDecimal;
import org.apache.log4j.Logger;

/**
 * @author bellawen
 * 
 */
public class PriceCalculateUtil {
	
	public static final Logger logger = Logger.getLogger(PriceCalculateUtil.class);

	/**
	 * 计算国内政策的价格
	 * 
	 * @param returnPoint
	 *            返点
	 * @param saveFees
	 *            留钱数
	 * @param type
	 *            政策类型（global ： 全局政策, nomal ： 普通政策,nfd ： 特价政策, special特殊政策）
	 * @param priceType
	 *            票面价类型（1： Y舱折扣, 2: 指定票面价 ）
	 * @param priceValue
	 *            对应票面价类型的值
	 * @param yPrice
	 *            Y舱的价格/票面价
	 * @return
	 */
	public static int calculatePrice(Float returnPoint, Integer saveFees, String type, int priceType, Float priceValue, Integer yPrice){
		 int price = 0;
		 if("global".equals(type) || "nomal".equals(type)){
			BigDecimal priceDec = new BigDecimal(yPrice);
			BigDecimal discountRate = new BigDecimal((100 - returnPoint) / 100.0);
			price = priceDec.multiply(discountRate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + saveFees;
		}else if("nfd".equals(type) || "special".equals(type)){
			  BigDecimal priceDec = null;
			if (priceType == 1) {// Y舱折扣
	                priceDec = new BigDecimal(yPrice * (priceValue/10.0));
	                priceDec = priceDec.setScale(-1, BigDecimal.ROUND_UP);
	            } else if (priceType == 2) {// 指定票面价
	                priceDec = new BigDecimal(priceValue);
	            }
	            BigDecimal discountRate = new BigDecimal((100 - returnPoint) / 100.0);
	            price = priceDec.multiply(discountRate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + saveFees;
		}
		return price;
	}
}
