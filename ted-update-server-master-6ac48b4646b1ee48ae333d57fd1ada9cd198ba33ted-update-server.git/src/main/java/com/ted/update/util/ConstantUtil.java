package com.ted.update.util;

public interface ConstantUtil {
    int TABLE_BLOCK_SYNCED = 1;
    int TABLE_BLOCK_UNSYNCED = 0;
    int TABLE_BLOCK_IS_NOT_ISOLATED = 0;
    int TABLE_BLOCK_IS_ISOLATED = 1;
    int TABLE_BLOCK_UNCONFIRM = 0;
    int TABLE_BLOCK_CONFIRMED = 1;
    int TBALE_PLAT_TYPE_SEND = 0;
    int TBALE_PLAT_TYPE_NOT_SEND = 1;
    int TBALE_TX_ATOKEN_ALL = 2;
    int TABLE_TX_STATUS_UNBLOCK = 0;
    int TABLE_TX_STATUS_UNCONFIRM = 1;
    int TABLE_TX_STATUS_CONFIRMED = 2;
    int TABLE_DELETED = 1;
    int TABLE_UNDELETED = 0;
    String ASSERT_TYPE_MAIN = "MAIN";
    String REDIS_PREFIX_EXTERNAL_PURSE_NAME_ONT = "ExternalPurseNameOnt";
    String REDIS_KEY_BLOCK_DETAIL = "block_detail";

    int ATOM_VALIDATOR_STATUS_BONDED = 2;
}
