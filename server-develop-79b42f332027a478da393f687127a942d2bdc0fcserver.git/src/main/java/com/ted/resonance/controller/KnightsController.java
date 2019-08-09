package com.ted.resonance.controller;

import com.ted.resonance.entity.Team;
import com.ted.resonance.entity.valueobject.Address;
import com.ted.resonance.entity.valueobject.TeamsInfo;
import com.ted.resonance.service.TeamService;
import com.ted.resonance.utils.ContractUtils;
import com.ted.resonance.utils.CouponGenerateStrategy;
import com.ted.resonance.utils.validator.Coin;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * 骑士团接口设计
 */
@RestController
@RequestMapping("/v1/ted/knights/")
public class KnightsController {

    @Value("${ethGasLimit}") private Long GAS_LIMIT;
    @Value("${etcKnightAddr}") private String etcKnightAddr;
    @Value("${ethKnightAddr}") private String ethKnightAddr;
    @Value("${etcWorkerPrivateKey}") private String workerPrivateKey;
    @Autowired
    private TeamService teamService;
//    @ApiOperation("获得骑士团信息")
//    @PostMapping(value = "getTeamsInfo", produces = "application/json")
//    public ResponseEntity<TeamsInfo> getTeamsInfo(@RequestBody @Validated TeamsInfoParams teamsInfoParams) {
//        TeamsInfo teamsInfo = new TeamsInfo();
//        teamsInfo.setBonusPool(BigDecimal.valueOf(888888888));
//        teamsInfo.setStatus(1);
//        teamsInfo.setPhase(teamsInfoParams.getPhase());
//
//        Team team = new Team();
//        team.setLeaderAddress("0x1234");
//        team.setNickname("凤飞飞");
//        team.setEtcFund(BigDecimal.valueOf(21654.0));
//        team.setEthFund(BigDecimal.valueOf(54654.0));
//        team.setId(1);
//        team.setNumber(22);
//        team.setRank(60);
//        team.setMyLeaderFund(BigDecimal.valueOf(5422l));
//        team.setMyTeam(true);
//
//        List<Team> topList = new LinkedList<>();
//        for(int i=0; i<20; i++) {
//            Team team1 = new Team();
//            int addr = i + 1235;
//            team1.setLeaderAddress("0x" + addr );
//            team1.setNickname("test"+i);
//            team1.setEtcFund(BigDecimal.valueOf(21654.0 - i));
//            team1.setEthFund(BigDecimal.valueOf(54654.0 - i));
//            team1.setId(i + 2);
//            team1.setNumber(22);
//            team1.setRank(i + 1);
//            team1.setMyTeam(false);
//            topList.add(team1);
//        }
//        teamsInfo.setMyTeam(team);
//        teamsInfo.setTopTwentyTeams(topList);
//
//
//        return ResponseUtils.makeOkResponse(teamsInfo);
//    }


//    @PostMapping(value = "v2/getTeamsInfo", produces = "application/json")
    @ApiOperation("获得骑士团信息")
    @PostMapping(value = "getTeamsInfo", produces = "application/json")
    public ResponseEntity<TeamsInfo> getTeamsInfo1(@RequestBody @Validated TeamsInfoParams teamsInfoParams) throws Exception{
        return ResponseUtils.makeOkResponse(teamService.getTeamsInfo(teamsInfoParams.getAddress(), teamsInfoParams.type, teamsInfoParams.phase));
    }

    /**
     * 根据地址查询红利, 参考骑士团分红规则
     * @param params
     * @return
     */
    @ApiOperation("根据地址查询奖金")
    @PostMapping(value = "checkBonus", produces = "application/json")
    public ResponseEntity<Bonus> checkBonus(@RequestBody @Validated BonusParams params) throws Exception {
        Bonus bonus = new Bonus();
        BigDecimal reward = teamService.checkReward(params.address, params.phase, params.type);
        bonus.setBonus(reward);
        if(reward.compareTo(BigDecimal.valueOf(0)) == 0) {
            return ResponseUtils.makeOkResponse(bonus);
        }
        bonus.setEtcGasLimit(BigInteger.valueOf(GAS_LIMIT));
        bonus.setEtcGasPrice(Convert.fromWei(BigDecimal.valueOf(20_000_000_000L), Convert.Unit.GWEI));
        bonus.setEthGasLimit(BigInteger.valueOf(GAS_LIMIT));
        bonus.setEthGasPrice(Convert.fromWei(BigDecimal.valueOf(20_000_000_000L), Convert.Unit.GWEI));
        bonus.setEtcContractAddress(etcKnightAddr);
        bonus.setEthContractAddress(ethKnightAddr);
        Credentials credentials = Credentials.create(workerPrivateKey);
        Sign.SignatureData signatureData = ContractUtils.getSignature(params.getPhase(), params.getAddress(), reward.movePointRight(18).toBigInteger(), credentials);
        bonus.setV((int)signatureData.getV());
        bonus.setR(CouponGenerateStrategy.toHexString(signatureData.getR()));
        bonus.setS(CouponGenerateStrategy.toHexString(signatureData.getS()));
        return ResponseUtils.makeOkResponse(bonus);
    }

    @ApiOperation("提交领奖申请")
    @PostMapping(value = "getBonus", produces = "application/json")
    public ResponseEntity getBonus(@RequestBody @Validated GetRewardParams params) throws Exception {
        teamService.getReward(params.getAddress(), params.getPhase(), params.getType(), params.getSignedString());
        return ResponseUtils.makeOkResponse();
    }

    private static class BonusParams {
        @ApiModelProperty("地址")
        @NotBlank
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
        private String address;
        @ApiModelProperty("类型 etc/eth")
        @Coin
        @NotBlank
        private String type;

        @ApiModelProperty("阶段 第一期 1 第二期 2")
        private Integer phase;


        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getPhase() {
            return phase;
        }

        public void setPhase(Integer phase) {
            this.phase = phase;
        }
    }

    private static class GetRewardParams extends BonusParams{
        @ApiModelProperty("已签名的交易")
        private String signedString;

        public String getSignedString() {
            return signedString;
        }

        public void setSignedString(String signedString) {
            this.signedString = signedString;
        }
    }

    private static class Bonus {
        @ApiModelProperty("奖金")
        private BigDecimal bonus;
        @ApiModelProperty("etc gas limit")
        private BigInteger etcGasLimit;
        @ApiModelProperty("eth gas limit")
        private BigInteger ethGasLimit;

        @ApiModelProperty("etc gas price， 单位 gwei")
        private BigDecimal etcGasPrice;
        @ApiModelProperty("eth gas price 单位 gwei")
        private BigDecimal ethGasPrice;
        @ApiModelProperty("etc合约地址")
        private String etcContractAddress;
        @ApiModelProperty("eth合约地址")
        private String ethContractAddress;

        private Integer v;
        private String r;
        private String s;



        public BigDecimal getBonus() {
            return bonus;
        }

        public void setBonus(BigDecimal bonus) {
            this.bonus = bonus;
        }

        public BigInteger getEtcGasLimit() {
            return etcGasLimit;
        }

        public void setEtcGasLimit(BigInteger etcGasLimit) {
            this.etcGasLimit = etcGasLimit;
        }

        public BigInteger getEthGasLimit() {
            return ethGasLimit;
        }

        public void setEthGasLimit(BigInteger ethGasLimit) {
            this.ethGasLimit = ethGasLimit;
        }

        public BigDecimal getEtcGasPrice() {
            return etcGasPrice;
        }

        public void setEtcGasPrice(BigDecimal etcGasPrice) {
            this.etcGasPrice = etcGasPrice;
        }

        public BigDecimal getEthGasPrice() {
            return ethGasPrice;
        }

        public void setEthGasPrice(BigDecimal ethGasPrice) {
            this.ethGasPrice = ethGasPrice;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getEtcContractAddress() {
            return etcContractAddress;
        }

        public void setEtcContractAddress(String etcContractAddress) {
            this.etcContractAddress = etcContractAddress;
        }

        public String getEthContractAddress() {
            return ethContractAddress;
        }

        public void setEthContractAddress(String ethContractAddress) {
            this.ethContractAddress = ethContractAddress;
        }
    }
    private static class TeamsInfoParams {
        @ApiModelProperty("当前钱包用户的地址")
        @NotBlank
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
        private String address;
        @ApiModelProperty("排行榜类型 eth/etc")
        @Coin
        private String type;
        @ApiModelProperty("共两期， 1为第一期，2为第二期")
        @NotNull
        private Integer phase;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getPhase() {
            return phase;
        }

        public void setPhase(Integer phase) {
            this.phase = phase;
        }
    }
}
