package com.ted.resonance.scheduler;

import com.ted.resonance.constants.ResonanceConstants;
import com.ted.resonance.entity.DataDictBean;
import com.ted.resonance.entity.ExCfgActivityBean;
import com.ted.resonance.enums.ActivityStatusEnum;
import com.ted.resonance.mapper.DataDictBeanMapper;
import com.ted.resonance.mapper.ExCfgActivityBeanMapper;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2019-07-16 14:14
 * @Description: 冒险家活动状态变更job
 */
@Component
@Async
public class ExActivityScheduler implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataDictBeanMapper dataDictBeanMapper;
    @Autowired
    private ExCfgActivityBeanMapper exCfgActivityBeanMapper;

    private static List<Integer> status = new ArrayList<Integer>();


    static {
        status.add(ActivityStatusEnum.NOT_START.getCode());
        status.add(ActivityStatusEnum.STARTING.getCode());
        status.add(ActivityStatusEnum.NOT_REWARD.getCode());

    }

    /**
     * 冒险家活动状态变更
     */
    @Scheduled(cron = "${cron.ex.activity}")
    @Transactional
    public void updateActivityStatus() {
        try {
            logger.info("updateActivityStatus begin");
            DataDictBean dataDictBean = dataDictBeanMapper.queryByKey(ResonanceConstants.ETHHEIGHT);
            if (dataDictBean != null && !StringUtils.isEmpty(dataDictBean.getValue())) {
                Integer ethHight = Integer.valueOf(dataDictBean.getValue());
                List<ExCfgActivityBean> activityBeans = exCfgActivityBeanMapper.queryListByStatus(status);
                if (!CollectionUtils.isEmpty(activityBeans)) {
                    activityBeans.forEach(activityBean -> {
                        this.handleCfgExActivity(activityBean, ethHight);
                    });
                }
            }

        } catch (Exception ex) {
            logger.error("updateActivityStatus exception={}", ex);
        }
        logger.info("updateActivityStatus end");
    }

    /**
     * 处理活动配置
     *
     * @param activityBean
     */
    private void handleCfgExActivity(ExCfgActivityBean activityBean, Integer ethHight) {

        Integer beginBlock = activityBean.getBeginBlock();
        Integer endBlock = activityBean.getEndBlock();
        //更改活动状态未进行中
        if (ethHight >= beginBlock && ethHight < endBlock) {
            exCfgActivityBeanMapper.updateActivityStatus(activityBean.getId(), ActivityStatusEnum.STARTING.getCode());
        }
        //更改活动状态结束未发奖
        if (ethHight >= endBlock) {
            exCfgActivityBeanMapper.updateActivityStatus(activityBean.getId(), ActivityStatusEnum.NOT_REWARD.getCode());

        }
    }


}
