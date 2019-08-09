-- 所有的表
-- 1. address_min_balance
-- 2. ca_cash_config
-- 3. ca_collar_coupons
-- 4. ca_draw_chance
-- 5. ca_record_info
-- 6. ex_activity_addition
-- 7. ex_cfg_activity
-- 8. ex_ted_burn
-- 9. user_acc
-- 10.user_acc_detail
-- 11.broadcast_transaction
-- 12.withdraw_order_log
-- 13.withdraw_order_record
-- 14.tx
-- 15.block
-- 16.sync_error

-- 初始化数据
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'ethPoundage', '0.006', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'caSwitch', '1', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'tedThreshold1', '200000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'tedThreshold2', '300000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'tedThreshold3', '1000000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'tedThreshold4', '3000000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'ethHeight', '10000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'etcHeight', '10000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'ethRate', '230', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'etcRate', '3', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'tedRate', '0.01', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'etcPoundage', '0.001', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'ethGasLimit', '300000', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'ethGasPrice', '20', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'WITHDRAW_ETH_CONFIRM', '12', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'WITHDRAW_ETC_CONFIRM', '20', '1');
INSERT INTO `exchange`.`data_dict`(`category`, `type`, `key`, `value`, `enabled`) VALUES (1, 0, 'etcPoundage', '0.01', '0');