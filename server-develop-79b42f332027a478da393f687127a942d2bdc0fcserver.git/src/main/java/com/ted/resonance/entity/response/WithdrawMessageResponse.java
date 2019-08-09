package com.ted.resonance.entity.response;

import java.io.Serializable;
import java.util.Arrays;

public class WithdrawMessageResponse implements Serializable {
    private String refNo;
    private Integer status;
    private String message;
    private String txId;
    private String addressFrom;

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    @Override
    public String toString() {
        return "WithdrawMessageResponse{" +
                "refNo='" + refNo + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", txId='" + txId + '\'' +
                ", addressFrom='" + addressFrom + '\'' +
                '}';
    }

    public enum StatusEnum {
        PAYING(0, "打币中"),
        AUTO_CONFIRM(1, "程序待确认"),
        BLOCKCHAIN_CONFIRM(2, "区块链待确认"),
        SUCCESS(3, "成功"),
        FAILURE(4, "失败");//交易打包到区块中，但是交易状态是失败;


        StatusEnum(Integer index, String name) {
            this.index = index;
            this.name = name;
        }

        private Integer index;
        private String name;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public static StatusEnum valueof(Integer index) {
            return Arrays.stream(StatusEnum.values()).filter(p -> p.index.equals(index)).findFirst().orElse(null);
        }
    }

}
