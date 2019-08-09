package com.ted.update.core;

import com.ted.update.entity.Block;
import com.ted.update.service.BlockHandlerService;
import com.ted.update.service.BlockService;
import com.ted.update.util.DataUtil;
import com.ted.update.util.TedLogger;
import com.ted.update.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class BlockInfoCore implements TedLogger {
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockHandlerService blockHandlerService;
    @Value("${blockchain.block.number.limit: 10}")
    private Integer blockNumberLimit = 10;
    @Value("${blockchain.block.confirm: 10}")
    private Integer blockConfirm = 10;
    @Value("${blockchain.block.init.height: 1}")
    private Integer initBlockHeight;

    @Value("${blockchain.asset}")
    private String blockchainAsset;

    public void blockInfoHandler() {
        try{
            //获取链当前的高度
            Integer currentBlockNumber = blockHandlerService.getBlockNumber();
            VerifyUtil.objectNotNull(currentBlockNumber, "get current block number failed");

            //获取数据库最大高度
            Integer dbMaxBlockNumber = blockService.selectMaxHeight();
            VerifyUtil.objectNotNull(dbMaxBlockNumber, "get db max block number failed");

            if (currentBlockNumber > dbMaxBlockNumber) {
                //得到本次获取多少个高度的信息
                Integer limitBlockNumber = currentBlockNumber - dbMaxBlockNumber > blockNumberLimit ? dbMaxBlockNumber + blockNumberLimit : currentBlockNumber;
                List<Integer> blockNumberList = DataUtil.generateLongList(dbMaxBlockNumber, limitBlockNumber);

                //获取高度信息blockhash
                List<Block> blockHashList = blockHandlerService.getBlocks(blockNumberList);

                //将获取的blockhash存入数据库相映的表
                Integer rowNum = blockService.insertBlocks(blockHashList);
                VerifyUtil.objectNotNull(rowNum, "insert block hash failed");
                VerifyUtil.numberNotZore(rowNum, "insert block hash failed");
            }
        }catch(Exception e) {
            logger.error("update block error, exception:{}", e.getLocalizedMessage());
        }
    }

    @PostConstruct
    public void init() {
        logger.info("init block height start");
        //查看blockhash表是否有记录
        Integer count = blockService.selectCountBlock();
        VerifyUtil.objectNotNull(count, "init height failed");
        //没有记录，则初始化一条记录
        if(count <= 0) {
            //根据配置的高度，获取高度信息
            List<Block> blockHashList = blockHandlerService.getBlocks(Arrays.asList(initBlockHeight));

            //将获取的高度信息入库
            Integer rowNum = blockService.insertBlocks(blockHashList);
            VerifyUtil.objectNotNull(rowNum, "insert block hash failed");
            VerifyUtil.numberNotZore(rowNum, "insert block hash failed");
        }

        logger.info("init block height complete");
    }
}
