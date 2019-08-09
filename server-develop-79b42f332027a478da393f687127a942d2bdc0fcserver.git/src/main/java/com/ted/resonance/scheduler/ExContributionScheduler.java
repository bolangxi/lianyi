package com.ted.resonance.scheduler;

import com.ted.resonance.entity.ExActivityAdditionBean;
import com.ted.resonance.entity.ExCfgActivityBean;
import com.ted.resonance.entity.ExTedBurnBean;
import com.ted.resonance.entity.TxBean;
import com.ted.resonance.enums.ConfirmEnum;
import com.ted.resonance.enums.ContributionCalStatus;
import com.ted.resonance.enums.StatusEnum;
import com.ted.resonance.mapper.ExActivityAdditionBeanMapper;
import com.ted.resonance.mapper.ExCfgActivityBeanMapper;
import com.ted.resonance.mapper.ExTedBurnBeanMapper;
import com.ted.resonance.mapper.TxBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:冒险家贡献值计算job
 */
@Component
@Async
public class ExContributionScheduler implements Serializable {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExTedBurnBeanMapper exTedBurnBeanMapper;

    @Autowired
    private TxBeanMapper txBeanMapper;
    @Autowired
    private ExCfgActivityBeanMapper exCfgActivityBeanMapper;
    @Autowired
    private ExActivityAdditionBeanMapper additionBeanMapper;

    /**
     * 冒险家贡献值计算job
     */
    @Scheduled(cron = "${cron.ex.contribution}")
    @Transactional
    public void calExContribution() {
        logger.info("calExContribution begin");
        try {
            List<ExTedBurnBean> beanList = exTedBurnBeanMapper.queryListByStatus(ContributionCalStatus.NOT_CAL.getCode());
            if (!CollectionUtils.isEmpty(beanList)) {
                logger.info("beanList size={}", beanList.size());
                beanList.forEach(exTedBurnBean -> {
                    this.handleExTedBurnBean(exTedBurnBean);
                });
            }
        } catch (Exception ex) {
            logger.error("calExContribution exception={}", ex);
        }
        logger.info("calExContribution end");
    }

    /**
     * @param exTedBurnBean
     */
    private void handleExTedBurnBean(ExTedBurnBean exTedBurnBean) {

        String txHash = exTedBurnBean.getTxHash();
        Long id = exTedBurnBean.getId();
        if (!StringUtils.isEmpty(txHash)) {
            TxBean txBean = txBeanMapper.selectByTxHash(txHash);
            Boolean judgeResult = judgeBurnAmount(txBean, exTedBurnBean);
            if (!judgeResult) {
                exTedBurnBeanMapper.updateIsDelete(id);
                return;
            }
            if (txBean != null && judgeResult) {
                if (StatusEnum.FAIL.getCode() == txBean.getState()) {
                    //更新燃烧表状态未失败
                    exTedBurnBeanMapper.updateStatus(id, StatusEnum.FAIL.getCode());
                    return;
                }
                //链上执行成功
                if (StatusEnum.SUCCESS.getCode() == txBean.getState()) {
                    //链上确认失败
                    if (ConfirmEnum.FAIL.getCode() == txBean.getState()) {
                        exTedBurnBeanMapper.updateStatus(id, StatusEnum.FAIL.getCode());
                        return;
                    }
                    //链上确认成功
                    if (ConfirmEnum.SUCCESS.getCode() == txBean.getState()) {

                        int result = exTedBurnBeanMapper.updateBlockAndStatus(id, txBean.getBlockHash(), txBean.getBlockHeight(), StatusEnum.SUCCESS.getCode());
                        if (result > 0) {
                            //计算贡献值
                            calContributionDegree(exTedBurnBean);
                        }
                    }


                }
            }

        }
    }

    /**
     * 判断金额是否符合
     *
     * @param txBean
     * @param exTedBurnBean
     * @return
     */
    private Boolean judgeBurnAmount(TxBean txBean, ExTedBurnBean exTedBurnBean) {

        if (txBean != null && txBean.getTedAmount() != null && exTedBurnBean != null && exTedBurnBean.getAmount() != null) {
            if (txBean.getTedAmount().compareTo(exTedBurnBean.getAmount()) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 计算贡献值
     *
     * @param tedBurnBean
     */
    private void calContributionDegree(ExTedBurnBean tedBurnBean) {
        Long activityId = tedBurnBean.getActivityId();
        if (activityId != null) {
            ExTedBurnBean burnBean = exTedBurnBeanMapper.selectById(tedBurnBean.getId());
            ExCfgActivityBean cfgActivity = exCfgActivityBeanMapper.selectById(activityId);
            if (burnBean.getBlockHeight() != null) {
                Integer blockHeight = burnBean.getBlockHeight();
                Boolean mid = judgeMidActivityByHeight(cfgActivity, blockHeight);
                //在活动范围内
                if (mid) {
                    List<ExActivityAdditionBean> additions = additionBeanMapper.selectAdditionByActivityId(activityId);
                    BigDecimal additionRate = getAdditionRate(additions, blockHeight);
                    //获得贡献值
                    BigDecimal contribution = burnBean.getAmount();

                    if (additionRate != null && additionRate.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal addition = burnBean.getAmount().multiply(additionRate);
                        contribution = contribution.add(addition);
                    }
                    exTedBurnBeanMapper.updateContributionAndCalStatus(burnBean.getId(), contribution, ContributionCalStatus.HAS_CAL.getCode());


                }
            }

        }


    }

    /**
     * 判断是否在活动范围内
     *
     * @param cfgActivityBean
     * @param blockHeight
     * @return
     */
    private Boolean judgeMidActivityByHeight(ExCfgActivityBean cfgActivityBean, Integer blockHeight) {

        if (cfgActivityBean != null && cfgActivityBean.getBeginBlock() <= blockHeight && cfgActivityBean.getEndBlock() >= blockHeight) {
            return true;
        }
        return false;
    }

    /**
     * 获取区块加成比率
     *
     * @param additions
     * @param blockHeight
     * @return
     */
    private BigDecimal getAdditionRate(List<ExActivityAdditionBean> additions, Integer blockHeight) {
        if (!CollectionUtils.isEmpty(additions)) {
            additions = additions.stream().filter(addition -> {
                if (blockHeight >= addition.getBeginBlock() && blockHeight <= addition.getEndBlock()) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(additions)) {
            ExActivityAdditionBean additionBean = additions.get(0);
            return new BigDecimal(additionBean.getAdditionRate() / 100.0).setScale(2, BigDecimal.ROUND_DOWN);
        }

        return BigDecimal.ZERO;
    }


}
