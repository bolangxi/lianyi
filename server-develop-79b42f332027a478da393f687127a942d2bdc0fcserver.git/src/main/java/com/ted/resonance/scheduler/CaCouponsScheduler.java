package com.ted.resonance.scheduler;

import com.alibaba.fastjson.JSON;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.AddressMinBalanceBean;
import com.ted.resonance.entity.CaCashConfigBean;
import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.CaDrawChanceBean;
import com.ted.resonance.mapper.AddressMinBalanceBeanMapper;
import com.ted.resonance.mapper.CaCashConfigBeanMapper;
import com.ted.resonance.mapper.CaCollarCouponsBeanMapper;
import com.ted.resonance.mapper.CaDrawChanceBeanMapper;
import com.ted.resonance.service.CaCollarCouponsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 计算收益卡收益
 */
@Component
@Async
public class CaCouponsScheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(CaCouponsScheduler.class);

    @Autowired
    private AddressMinBalanceBeanMapper addressMinBalanceBeanMapper;
    @Autowired
    private CaDrawChanceBeanMapper caDrawChanceBeanMapper;
    @Autowired
    private CaCashConfigBeanMapper caCashConfigBeanMapper;
    @Autowired
    private CaCollarCouponsBeanMapper caCollarCouponsBeanMapper;
    @Autowired
    private CaCollarCouponsService collarCouponsService;

//    @Scheduled(fixedDelay = 2000)
    @Scheduled(cron = "${cron.ca.DrawCoupons}")
    @Transactional
    public void doDrawCoupons() {
        long jobStartTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        LOGGER.info("【计算锁仓+机会+领金券开始】");
        try {
            // 锁仓逻辑
            List<AddressMinBalanceBean> addressMinBalanceBeanList = addressMinBalanceBeanMapper.selectByDayTime(c.getTime());
            if (addressMinBalanceBeanList == null || addressMinBalanceBeanList.size() == 0) {
                LOGGER.warn("【没有锁仓数据】，数据可能有异常");
                return;
            }
            String tedThreshold1 = DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD1);
            if (tedThreshold1 == null || "".equalsIgnoreCase(tedThreshold1)) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少门槛数据， data_dict表key为:{}", DataDictConstant.TEDTHRESHOLD1);
                return;
            }
            int tedThreshold1Int = Integer.valueOf(tedThreshold1);
            String tedThreshold2 = DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD2);
            if (tedThreshold2 == null || "".equalsIgnoreCase(tedThreshold2)) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少门槛数据， data_dict表key为:{}", DataDictConstant.TEDTHRESHOLD2);
                return;
            }
            int tedThreshold2Int = Integer.valueOf(tedThreshold2);
            String tedThreshold3 = DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD3);
            if (tedThreshold3 == null || "".equalsIgnoreCase(tedThreshold3)) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少门槛数据， data_dict表key为:{}", DataDictConstant.TEDTHRESHOLD3);
                return;
            }
            int tedThreshold3Int = Integer.valueOf(tedThreshold3);
            String tedThreshold4 = DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD4);
            if (tedThreshold4 == null || "".equalsIgnoreCase(tedThreshold4)) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少门槛数据， data_dict表key为:{}", DataDictConstant.TEDTHRESHOLD4);
                return;
            }
            int tedThreshold4Int = Integer.valueOf(tedThreshold4);

            Calendar startTime = Calendar.getInstance();
            startTime.setTime(currentDate);
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.set(Calendar.MILLISECOND, 0);

            Calendar endTime = Calendar.getInstance();
            endTime.setTime(currentDate);
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            endTime.set(Calendar.SECOND, 59);
            endTime.set(Calendar.MILLISECOND, 0);

            // 检查机会是否生成，如果生成，就直接返回，不用在处理
            List<CaDrawChanceBean> isExistsDay = caDrawChanceBeanMapper.selectByStartTime(startTime.getTime());
            if (isExistsDay != null && isExistsDay.size() > 0) {
                LOGGER.warn("【机会已经生成了，请勿重复执行】， 已经存在的机会日期为:{}", sdf.format(startTime.getTime()));
                return;
            }

            // 机会逻辑
            List<CaDrawChanceBean> caDrawChanceBeanList = new ArrayList<>();
            for (AddressMinBalanceBean bean : addressMinBalanceBeanList) {
                if (bean.getBalance().longValue() >= tedThreshold4Int) { // 4次机会
                    CaDrawChanceBean caDrawChanceBean = new CaDrawChanceBean();
                    caDrawChanceBean.setUserId("0");
                    caDrawChanceBean.setAddr(bean.getAddress());
                    caDrawChanceBean.setStartTime(startTime.getTime());
                    caDrawChanceBean.setExpiresTime(endTime.getTime());
                    caDrawChanceBean.setChanceTimes(4);
                    caDrawChanceBean.setDrawStatus(1);
                    caDrawChanceBean.setRemark("");
                    caDrawChanceBean.setDel(Short.valueOf("0"));
                    caDrawChanceBean.setCreateTime(currentDate);
                    caDrawChanceBean.setUpdateTime(currentDate);
                    caDrawChanceBeanList.add(caDrawChanceBean);
                } else if (bean.getBalance().longValue() >= tedThreshold3Int) { // 3次机会
                    CaDrawChanceBean caDrawChanceBean = new CaDrawChanceBean();
                    caDrawChanceBean.setUserId("0");
                    caDrawChanceBean.setAddr(bean.getAddress());
                    caDrawChanceBean.setStartTime(startTime.getTime());
                    caDrawChanceBean.setExpiresTime(endTime.getTime());
                    caDrawChanceBean.setChanceTimes(3);
                    caDrawChanceBean.setDrawStatus(1);
                    caDrawChanceBean.setRemark("");
                    caDrawChanceBean.setDel(Short.valueOf("0"));
                    caDrawChanceBean.setCreateTime(currentDate);
                    caDrawChanceBean.setUpdateTime(currentDate);
                    caDrawChanceBeanList.add(caDrawChanceBean);
                } else if (bean.getBalance().longValue() >= tedThreshold2Int) { // 2次机会
                    CaDrawChanceBean caDrawChanceBean = new CaDrawChanceBean();
                    caDrawChanceBean.setUserId("0");
                    caDrawChanceBean.setAddr(bean.getAddress());
                    caDrawChanceBean.setStartTime(startTime.getTime());
                    caDrawChanceBean.setExpiresTime(endTime.getTime());
                    caDrawChanceBean.setChanceTimes(2);
                    caDrawChanceBean.setDrawStatus(1);
                    caDrawChanceBean.setRemark("");
                    caDrawChanceBean.setDel(Short.valueOf("0"));
                    caDrawChanceBean.setCreateTime(currentDate);
                    caDrawChanceBean.setUpdateTime(currentDate);
                    caDrawChanceBeanList.add(caDrawChanceBean);
                } else if (bean.getBalance().longValue() >= tedThreshold1Int) { // 1次机会
                    CaDrawChanceBean caDrawChanceBean = new CaDrawChanceBean();
                    caDrawChanceBean.setUserId("0");
                    caDrawChanceBean.setAddr(bean.getAddress());
                    caDrawChanceBean.setStartTime(startTime.getTime());
                    caDrawChanceBean.setExpiresTime(endTime.getTime());
                    caDrawChanceBean.setChanceTimes(1);
                    caDrawChanceBean.setDrawStatus(1);
                    caDrawChanceBean.setRemark("");
                    caDrawChanceBean.setDel(Short.valueOf("0"));
                    caDrawChanceBean.setCreateTime(currentDate);
                    caDrawChanceBean.setUpdateTime(currentDate);
                    caDrawChanceBeanList.add(caDrawChanceBean);
                }
            }
            if (caDrawChanceBeanList.size() > 0) {
                LOGGER.info("【满足机会的用户】, 数据:{}", JSON.toJSONString(caDrawChanceBeanList));
                caDrawChanceBeanMapper.batchSaveOrUpdate(caDrawChanceBeanList);
            } else {
                LOGGER.info("【没有用户满足机会】");
                return;
            }

            String ethRate = DataDictCache.getDataValue(DataDictConstant.ETHRATE);
            String etcRate = DataDictCache.getDataValue(DataDictConstant.ETCRATE);
            String tedRate = DataDictCache.getDataValue(DataDictConstant.TEDRATE);
            if (ethRate == null || "".equalsIgnoreCase(ethRate)
                    || etcRate == null || "".equalsIgnoreCase(etcRate)
                    || tedRate == null || "".equalsIgnoreCase(tedRate)) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少eth, etc, ted汇率数据，eth={}, etc={}, ted={}",
                        ethRate, etcRate, tedRate);
                return;
            }
            // 获取该天所有机会
            List<CaDrawChanceBean> caDrawChanceList = caDrawChanceBeanMapper.selectByStartTime(startTime.getTime());
            // 总机会
            int drawCount = caDrawChanceList.stream().mapToInt(CaDrawChanceBean::getChanceTimes).sum();
            // 查询资金配置
            CaCashConfigBean caCashConfigBean = caCashConfigBeanMapper.selectByTime(startTime.getTime());
            if (caCashConfigBean == null) {
                LOGGER.error("【计算锁仓+机会+领金券】异常，缺少可用的资金配置，时间={}",
                        sdf.format(startTime.getTime()));
                return;
            }
            // eth / eth + etc
            BigDecimal ethUsdt = caCashConfigBean.getEthNums().multiply(new BigDecimal(ethRate));
            BigDecimal etcUsdt = caCashConfigBean.getEtcNums().multiply(new BigDecimal(etcRate));
            BigDecimal sumUsdt = ethUsdt.add(etcUsdt);
            // 资金占比
            BigDecimal ethCount = ethUsdt.divide(sumUsdt, 2, BigDecimal.ROUND_HALF_UP);
            // BigDecimal etcCount = BigDecimal.ONE.subtract(ethCount);
            // 机会占比
            BigDecimal ethDrawCount = BigDecimal.valueOf(drawCount).multiply(ethCount).setScale(0, BigDecimal.ROUND_HALF_UP);
            BigDecimal etcDrawCount = BigDecimal.valueOf(drawCount).subtract(ethDrawCount);

            BigDecimal avgEth = BigDecimal.ZERO;
            BigDecimal avgEtc = BigDecimal.ZERO;
            // 奖励的平均ETH个数
            if (ethDrawCount.longValue() != 0) {
                avgEth = caCashConfigBean.getEthNums().divide(ethDrawCount, 4, BigDecimal.ROUND_DOWN);
            }
            // 奖励的平均ETC个数
            if (etcDrawCount.longValue() != 0) {
                avgEtc = caCashConfigBean.getEtcNums().divide(etcDrawCount, 4, BigDecimal.ROUND_DOWN);
            }

            List<CaCollarCouponsBean> couponsList = new ArrayList<>();
            // 领金券核心逻辑计算
            for (CaDrawChanceBean bean : caDrawChanceList) {
                Map<String, Object> result = doCoreCoupons(bean, avgEth, avgEtc, ethDrawCount, etcDrawCount
                        , ethRate, etcRate, tedRate, currentDate, startTime.getTime(), endTime.getTime());
                couponsList.addAll((List<CaCollarCouponsBean>) result.get("list"));
                ethDrawCount = new BigDecimal(result.get("ethDrawCount") + "");
                etcDrawCount = new BigDecimal(result.get("etcDrawCount") + "");
            }
            // 批量插入优惠券
            caCollarCouponsBeanMapper.batchSaveOrUpdate(couponsList);

            long jobEndTime = System.currentTimeMillis();
            LOGGER.info("【计算锁仓+机会+领金券结束】, 耗时：{}毫秒", jobEndTime - jobStartTime);
        } catch (Exception e) {
            LOGGER.error("【计算锁仓+机会+领金券异常】, 异常信息:{}", e);
        }
    }

    /**
     * 领金券核心处理逻辑
     *
     * @param bean
     * @param avgEth
     * @param avgEtc
     * @param ethDrawCount
     * @param etcDrawCount
     * @param ethRate
     * @param etcRate
     * @param tedRate
     * @param currentDate
     * @return
     */
    private Map<String, Object> doCoreCoupons(CaDrawChanceBean bean, BigDecimal avgEth, BigDecimal avgEtc,
                                              BigDecimal ethDrawCount, BigDecimal etcDrawCount,
                                              String ethRate, String etcRate, String tedRate, Date currentDate,
                                              Date startTime, Date endTime) {
        Map<String, Object> result = new HashMap<>();
        int[] rate = {3, 5, 8};
        String[] coinType = {"ETH", "ETC"};

        List<CaCollarCouponsBean> list = new ArrayList<>();
        for (int i = 0; i < bean.getChanceTimes().intValue(); i++) {
            CaCollarCouponsBean caCollarCouponsBean = new CaCollarCouponsBean();
            int rateIndex = (int) (Math.random() * rate.length);
            int randomRate = rate[rateIndex];
            int coinIndex = (int) (Math.random() * coinType.length);
            String randomCoin = coinType[coinIndex];
            caCollarCouponsBean.setDrawId(bean.getId()); // 机会的ID
            caCollarCouponsBean.setUserId(bean.getUserId());
            caCollarCouponsBean.setTxHash("");
            caCollarCouponsBean.setAddr(bean.getAddr());
            caCollarCouponsBean.setPayCoinType("ETH");
            caCollarCouponsBean.setRewardTime(startTime);
            caCollarCouponsBean.setExpiresTime(endTime);
            // ETH + TED 支付的占比
            BigDecimal payPercentage = BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(randomRate));

            // 随机币种
            if ("ETH".equalsIgnoreCase(randomCoin)) {
                if (ethDrawCount.intValue() > 0) {
                    ethDrawCount = doEthCoupon(ethDrawCount, ethRate, tedRate, avgEth, caCollarCouponsBean, randomRate, payPercentage);
                } else { // ETC
                    etcDrawCount = doEtcCoupon(etcDrawCount, ethRate, etcRate, tedRate, avgEtc, caCollarCouponsBean, randomRate, payPercentage);
                }
            } else { // ETC
                if (etcDrawCount.intValue() > 0) {
                    etcDrawCount = doEtcCoupon(etcDrawCount, ethRate, etcRate, tedRate, avgEtc, caCollarCouponsBean, randomRate, payPercentage);
                } else { // ETH
                    ethDrawCount = doEthCoupon(ethDrawCount, ethRate, tedRate, avgEth, caCollarCouponsBean, randomRate, payPercentage);
                }
            }
            caCollarCouponsBean.setRewardStatus("1"); // 状态（1:待领取, 2:已领取, 3:使用中, 4:已使用, 5:已过期）
            caCollarCouponsBean.setDel(Short.valueOf("0"));
            caCollarCouponsBean.setCreateTime(currentDate);
            caCollarCouponsBean.setUpdateTime(currentDate);
            caCollarCouponsBean.setRemark("");
            list.add(caCollarCouponsBean);
        }
        result.put("list", list);
        result.put("ethDrawCount", ethDrawCount);
        result.put("etcDrawCount", etcDrawCount);
        return result;
    }

    /**
     * 处理etc奖励类型的领金券
     *
     * @param etcDrawCount
     * @param ethRate
     * @param etcRate
     * @param tedRate
     * @param avgEtc
     * @param caCollarCouponsBean
     * @param randomRate
     * @param payPercentage
     * @return
     */
    private BigDecimal doEtcCoupon(BigDecimal etcDrawCount, String ethRate, String etcRate, String tedRate, BigDecimal avgEtc, CaCollarCouponsBean caCollarCouponsBean, int randomRate, BigDecimal payPercentage) {
        // 平均ETH(最大) * (100-随机数) / 100 * 0.1 == 平均ETH(最大) * (100-随机数) / 1000
        BigDecimal payCoinEtc = avgEtc.multiply(payPercentage).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_DOWN);
        BigDecimal payEtcNums = payCoinEtc.divide(BigDecimal.valueOf(10), 4, BigDecimal.ROUND_DOWN);
        // 把ETC换算成ETH    etc --> usdt --> eth
        BigDecimal payEthNums = payEtcNums.multiply(new BigDecimal(etcRate)).divide(new BigDecimal(ethRate), 4, BigDecimal.ROUND_DOWN);
        caCollarCouponsBean.setPayCoinNums(payEthNums); // 10%主链币
        // TED个数
        BigDecimal tedNum = payCoinEtc.subtract(payEtcNums).multiply(new BigDecimal(etcRate)).divide(new BigDecimal(tedRate), 2, BigDecimal.ROUND_DOWN);
        caCollarCouponsBean.setPayTedNums(tedNum); // 需要支付的TED个数
        caCollarCouponsBean.setRewardCoinType("ETC"); // 奖励的主链币种类型
        caCollarCouponsBean.setRewardCoinNums(avgEtc); // 奖励的主链币种个数
        caCollarCouponsBean.setEarningRate(Long.valueOf(randomRate)); // 收益率(%)
        // 机会要减少一次
        etcDrawCount = etcDrawCount.subtract(BigDecimal.ONE);
        return etcDrawCount;
    }

    /**
     * 处理eth奖励类型的领金券
     *
     * @param ethDrawCount
     * @param ethRate
     * @param tedRate
     * @param avgEth
     * @param caCollarCouponsBean
     * @param randomRate
     * @param payPercentage
     * @return
     */
    private BigDecimal doEthCoupon(BigDecimal ethDrawCount, String ethRate, String tedRate, BigDecimal avgEth, CaCollarCouponsBean caCollarCouponsBean, int randomRate, BigDecimal payPercentage) {
        // 平均ETH(最大) * (100-随机数) / 100 * 0.1 == 平均ETH(最大) * (100-随机数) / 1000
        BigDecimal payCoinEth = avgEth.multiply(payPercentage).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_DOWN);
        BigDecimal payEthNums = payCoinEth.divide(BigDecimal.valueOf(10), 4, BigDecimal.ROUND_DOWN);
        caCollarCouponsBean.setPayCoinNums(payEthNums); // 10%主链币
        // TED个数
        BigDecimal tedNum = payCoinEth.subtract(payEthNums).multiply(new BigDecimal(ethRate)).divide(new BigDecimal(tedRate), 2, BigDecimal.ROUND_DOWN);
        caCollarCouponsBean.setPayTedNums(tedNum); // 需要支付的TED个数
        caCollarCouponsBean.setRewardCoinType("ETH"); // 奖励的主链币种类型
        caCollarCouponsBean.setRewardCoinNums(avgEth); // 奖励的主链币种个数
        caCollarCouponsBean.setEarningRate(Long.valueOf(randomRate)); // 收益率(%)
        // 机会要减少一次
        ethDrawCount = ethDrawCount.subtract(BigDecimal.ONE);
        return ethDrawCount;
    }

    /**
     * 炼金师收益入账
     */
    @Scheduled(cron = "${cron.ca.coupon}")
    public void calCouponIncome() {
        LOGGER.info("calCouponIncome begin");
        try {
            collarCouponsService.calCouponIncome();
        } catch (Exception ex) {
            LOGGER.error("calCouponIncome exception={}", ex);
        }
        LOGGER.info("calCouponIncome end");
    }

    /**
     * 定时跑批处理已过期的领金券数据状态
     */
    @Scheduled(cron = "${cron.ca.status}")
    public void updateRewardStatus() {
        LOGGER.info("caCoupons updateRewardStatus begin");
        try {
            caCollarCouponsBeanMapper.updateRewardStatusJob();
        } catch (Exception ex) {
            LOGGER.error("caCoupons updateRewardStatus exception={}", ex);
        }
        LOGGER.info("caCoupons updateRewardStatus end");
    }

}


