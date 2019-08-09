package com.ted.resonance.controller;

import com.ted.resonance.entity.AddressInfo;
import com.ted.resonance.entity.Coupon;
import com.ted.resonance.entity.valueobject.Address;
import com.ted.resonance.entity.valueobject.Nonce;
import com.ted.resonance.entity.valueobject.TransactionParams;
import com.ted.resonance.service.AddressInfoService;
import com.ted.resonance.service.CouponService;
import com.ted.resonance.service.TransactionService;
import com.ted.resonance.utils.CouponGenerateStrategy;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.validator.Coin;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.utils.Convert;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;

@RestController
@RequestMapping("/v1/ted/resonance/")
public class  ResonanceController {
    private static final Logger LOG = LoggerFactory.getLogger(ResonanceController.class);

    @Autowired
    private CouponService couponService;
    @Autowired
    private AddressInfoService addressInfoService;
    @Autowired
    private TransactionService transactionService;
    @Value("${etcContractAddress}") private String etcContractAddress;
    @Value("${ethContractAddress}") private String ethContractAddress;
    @Value("${ethGasLimit}") private Long GAS_LIMIT;

    @ApiOperation("根据地址获取状态")
    @PostMapping(value = "getInfo", produces = "application/json")
    @Transactional
    public ResponseEntity<Status> getInfo(@RequestBody @Validated Address address) throws Exception {
        Status status = new Status();
        status.setInviterWritable(addressInfoService.inviterWritable(address.getAddress()));
        //根据链更新etc区块信息
        BigInteger etcStartBlock = transactionService.getEtcStartBlock();
        BigInteger etcEndBlock = transactionService.getEtcEndBlock();
        BigInteger etcCurrentBlock = transactionService.getEtcCurrentBlock();
//        System.out.println(etcStartBlock);
//        System.out.println(etcEndBlock);
//        System.out.println(etcCurrentBlock);
        BigInteger etcSumBlock = etcEndBlock.subtract(etcStartBlock);
        if(etcCurrentBlock.compareTo(etcStartBlock) < 0) {
            status.setEtcLeftBlock(etcEndBlock.subtract(etcStartBlock).intValue());
            status.setEtcProcess(0f);
        }else if(etcCurrentBlock.compareTo(etcEndBlock) >=0) {
            status.setEtcLeftBlock(0);
            status.setEtcProcess(100f);
        }else {
            status.setEtcLeftBlock((etcEndBlock.subtract(etcCurrentBlock).intValue()));
            status.setEtcProcess(etcCurrentBlock.subtract(etcStartBlock).floatValue()*100/etcSumBlock.floatValue());
        }

        //根据链更新eth区块信息
        BigInteger ethStartBlock = transactionService.getEthStartBlock();
        BigInteger ethEndBlock = transactionService.getEthEndBlock();
        BigInteger ethCurrentBlock = transactionService.getEthCurrentBlock();

//        System.out.println(ethStartBlock);
//        System.out.println(ethEndBlock);
//        System.out.println(ethCurrentBlock);
        BigInteger ethSumBlock = ethEndBlock.subtract(ethStartBlock);
        if(ethCurrentBlock.compareTo(ethStartBlock) < 0) {
            status.setEthLeftBlock(ethEndBlock.subtract(ethStartBlock).intValue());
            status.setEthProcess(0f);
        }else if(ethCurrentBlock.compareTo(ethEndBlock) >=0) {
            status.setEthLeftBlock(0);
            status.setEthProcess(100f);
        }else {
            status.setEthLeftBlock((ethEndBlock.subtract(ethCurrentBlock).intValue()));
            status.setEthProcess(ethCurrentBlock.subtract(ethStartBlock).floatValue()*100/ethSumBlock.floatValue());
        }

        status.setEtcBalance(new BigDecimal(transactionService.getEtcBalance(address.getAddress()), 18).stripTrailingZeros().toPlainString());
        status.setEthBalance(new BigDecimal(transactionService.getEthBalance(address.getAddress()), 18).stripTrailingZeros().toPlainString());
        status.setEtcGasLimit(BigInteger.valueOf(GAS_LIMIT));
        status.setEtcGasPrice(Convert.fromWei(BigDecimal.valueOf(20_000_000_000L), Convert.Unit.GWEI));
        status.setEthGasLimit(BigInteger.valueOf(GAS_LIMIT));
        status.setEthGasPrice(Convert.fromWei(BigDecimal.valueOf(20_000_000_000L), Convert.Unit.GWEI));
        Page<Coupon> coupons = couponService.getCouponByAddress(address.getAddress(),0, 2);
        List<Coupon> couponList = coupons.getContent();
        status.setCoupons(couponList);

        AddressInfo addressInfo = addressInfoService.getByAddressAndPhase(address.getAddress(), 1);
        if(addressInfo == null || addressInfo.getInviterAddress() == null) {
            status.setInviterAddress("");
        }else {
            status.setInviterAddress(addressInfo.getInviterAddress());
        }

        status.setEtcContractAddress(etcContractAddress);
        status.setEthContractAddress(ethContractAddress);

        return ResponseUtils.makeOkResponse(status);
    }

    @ApiOperation(value = "获取优惠券列表")
    @PostMapping(value = "getCoupons", produces = "application/json")
    public ResponseEntity<List<Coupon>> getCoupons(@ApiParam(value = "请求参数 page 为页码，pageSize 为页大小，address 为地址，\n" +
            "返回参数status 0 有效 1 未生效 2 已失效 3 已使用 ") @RequestBody @Validated CouponRequestParams params) {
//        MyPage<Coupon> myPage = new MyPage<>();
        Page<Coupon> coupons = couponService.getCouponByAddress(params.getAddress(), params.getPage(), params.getPageSize());
//        BeanUtils.copyProperties(coupons, myPage);
        return ResponseUtils.makeOkResponse(coupons.getContent());
    }

    @ApiOperation(value = "查询预期回报")
    @PostMapping(value = "getReward", produces = "application/json")
    public ResponseEntity<Reward> getReward(@ApiParam("没使用优惠券时，根据上传的货币种类和数量， 返回相应的ted回报") @RequestBody @Validated RewardParams params) throws Exception{
        params.setType(params.getType().trim().toLowerCase());
        Reward reward = new Reward();
        Coupon coupon = CouponGenerateStrategy.generateCoupon(params.getType(), params.getPayment());
        if(coupon != null){
            reward.setDiscount(coupon.getDiscount());
        }else {
            reward.setDiscount(1f);
        }
        if(params.getType().equals("etc")) {
            reward.setReward(transactionService.getEtcReward(params.getPayment()).stripTrailingZeros().toPlainString());
            return ResponseUtils.makeOkResponse(reward);
        }else {
            reward.setReward(transactionService.getEthReward(params.getPayment()).stripTrailingZeros().toPlainString());
            return ResponseUtils.makeOkResponse(reward);
        }
    }

    @ApiOperation(value = "发送共振请求")
    @PostMapping(value = "sendResonance", produces = "application/json")
    public ResponseEntity<Coupon> sendResonance(@ApiParam(value = "couponId 优惠券id，可选，\n inviterAddress 邀请人地址，可选") @RequestBody @Validated TransactionParams params) throws Exception{
        if(params.getInviterAddress() != null && !"".equals(params.getInviterAddress())) {
            String regEx = "^0x[a-fA-F0-9]{40}$";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(params.getInviterAddress());
            if(!matcher.matches()) {
                throw new CommonException(999, "addressNotCorrect");
            }
        }
        return ResponseUtils.makeOkResponse(transactionService.sendResonance(params, transactionService.getPhase()));
    }

    @ApiOperation(value = "获取nonce")
    @PostMapping(value = "getNonce", produces = "application/json")
    public ResponseEntity<Nonce> getNonce(@RequestBody @Validated Address address) throws Exception {
        return ResponseUtils.makeOkResponse(transactionService.getNonce(address.getAddress()));
    }


//    @ApiOperation(value = "获取共振进度")
//    @PostMapping(value = "getResonanceInfo", produces = "application/json")
//    public ResponseEntity<ResonanceInfo> getResonanceInfo() {
//        ResonanceInfo resonanceInfo = new ResonanceInfo();
//        resonanceInfo.setProcess(66f);
//        resonanceInfo.setLeftBlock(1024);
//        return ResponseUtils.makeOkResponse(resonanceInfo);
//    }

    private static class Status {
        @ApiModelProperty("是否可填写写邀请人, true 为是")
        private Boolean inviterWritable;
        @ApiModelProperty(notes = "etc百分制共振进度， 66 => 66%")
        private Float etcProcess;
        @ApiModelProperty(notes = "etc剩余区块")
        private Integer etcLeftBlock;
        @ApiModelProperty(notes = "eth百分制共振进度， 66 => 66%")
        private Float ethProcess;
        @ApiModelProperty(notes = "eth剩余区块")
        private Integer ethLeftBlock;
        @ApiModelProperty("两张优惠券")
        private List<Coupon> coupons;

        @ApiModelProperty("etc合约地址")
        private String etcContractAddress;
        @ApiModelProperty("eth合约地址")
        private String ethContractAddress;

        @ApiModelProperty("etc余额")
        private String etcBalance;
        @ApiModelProperty("eth余额")
        private String ethBalance;

        @ApiModelProperty("etc gas limit")
        private BigInteger etcGasLimit;
        @ApiModelProperty("eth gas limit")
        private BigInteger ethGasLimit;

        @ApiModelProperty("etc gas price， 单位 gwei")
        private BigDecimal etcGasPrice;
        @ApiModelProperty("eth gas price 单位 gwei")
        private BigDecimal ethGasPrice;

        @ApiModelProperty("邀请人地址 没有则为空字符串")
        private String inviterAddress;


        public Boolean getInviterWritable() {
            return inviterWritable;
        }

        public void setInviterWritable(Boolean inviterWritable) {
            this.inviterWritable = inviterWritable;
        }

        public List<Coupon> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }

        public Float getEtcProcess() {
            return etcProcess;
        }

        public void setEtcProcess(Float etcProcess) {
            this.etcProcess = etcProcess;
        }

        public Integer getEtcLeftBlock() {
            return etcLeftBlock;
        }

        public void setEtcLeftBlock(Integer etcLeftBlock) {
            this.etcLeftBlock = etcLeftBlock;
        }

        public Float getEthProcess() {
            return ethProcess;
        }

        public void setEthProcess(Float ethProcess) {
            this.ethProcess = ethProcess;
        }

        public Integer getEthLeftBlock() {
            return ethLeftBlock;
        }

        public void setEthLeftBlock(Integer ethLeftBlock) {
            this.ethLeftBlock = ethLeftBlock;
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

        public String getEtcBalance() {
            return etcBalance;
        }

        public void setEtcBalance(String etcBalance) {
            this.etcBalance = etcBalance;
        }

        public String getEthBalance() {
            return ethBalance;
        }

        public void setEthBalance(String ethBalance) {
            this.ethBalance = ethBalance;
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

        public String getInviterAddress() {
            return inviterAddress;
        }

        public void setInviterAddress(String inviterAddress) {
            this.inviterAddress = inviterAddress;
        }
    }


    private static class RewardParams {
        @ApiModelProperty(notes = "类型 ETH/ETC")
        @Coin
        @NotBlank
        private String type;

        @ApiModelProperty(notes = "支付数量")
        @NotNull
        private BigDecimal payment;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getPayment() {
            return payment;
        }

        public void setPayment(BigDecimal payment) {
            this.payment = payment;
        }
    }

    private static class Reward {
        @ApiModelProperty(notes = "获得的 ted 奖励")
        private String reward;

        @ApiModelProperty(notes = "获得的优惠券折扣， 0.75 = 75折")
        private Float discount;

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public Float getDiscount() {
            return discount;
        }

        public void setDiscount(Float discount) {
            this.discount = discount;
        }
    }

    private static class CouponRequestParams {
        @ApiModelProperty(notes = "页码 ，从0开始")
        @NotNull
        private Integer page;
        @ApiModelProperty(notes = "页大小， 必须大于0", example = "10")
        @NotNull
        private Integer pageSize;
        @ApiModelProperty(notes = "ETC/ETH地址", example = "0x1234")
        @NotBlank
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
        private String address;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

}
