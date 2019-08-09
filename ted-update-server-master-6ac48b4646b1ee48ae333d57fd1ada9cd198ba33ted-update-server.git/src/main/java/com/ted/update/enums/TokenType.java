package com.ted.update.enums;

import java.util.stream.Stream;

public enum TokenType {
    TED("0x13cc8f58a1df7669a05ae073fb89ae3287b22009", 18, "TED", "Token Economy Doin")
    ,TED_BURN("0x643d96a399edba49e86676ee732ee12a5ffad800", 18, "TED", "Token Economy Doin")
    ,TED_GOLD("0x3df08c8b6456f3cc0e12272f76dd938f5d125ad5", 18, "TED", "Token Economy Doin");
    private String contractAddress;
    private Integer decimals;
    private String tokenName;
    private String fullName;

    TokenType(String contractAddress, Integer decimals, String tokenName, String fullName) {
        this.contractAddress = contractAddress;
        this.decimals = decimals;
        this.tokenName = tokenName;
        this.fullName = fullName;
    }

    public Integer decimals() {
        return this.decimals;
    }

    public String tokenName() {
        return this.tokenName;
    }

    public String contractAddress() {
        return contractAddress;
    }

    public static TokenType tokenType(String contractAddress) {
        try {
            return Stream.of(TokenType.values()).filter(token -> token.contractAddress.equals(contractAddress)).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }
}

