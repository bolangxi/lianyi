package com.ted.resonance.enums;

public enum TradeType {
   CA_REWARD("炼金师奖励"),
   EX_REWARD("冒险家奖励"), WITHDRAW("提现");

    private String name;

    TradeType( String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        System.out.println("args = [" + TradeType.CA_REWARD + "]");
    }
}
