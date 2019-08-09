package com.ted.resonance.controller;

import com.ted.resonance.entity.Account;
import com.ted.resonance.entity.valueobject.Address;
import com.ted.resonance.service.AccountService;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/v1/ted/account/")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ApiOperation("查询昵称重名")
    @PostMapping(value = "checkNickName", produces = "application/json")
    public ResponseEntity<NicknameInfo> checkNickName(@RequestBody @Validated CheckNicknameParams checkNicknameParams) {
        NicknameInfo nicknameInfo = new NicknameInfo();
        nicknameInfo.setUsed(accountService.isUsed(checkNicknameParams.getNickname(), checkNicknameParams.getAddress()));
        return ResponseUtils.makeOkResponse(nicknameInfo);
    }

    @ApiOperation("更改昵称")
    @PostMapping(value = "updateNickName", produces = "application/json")
    public ResponseEntity updateNickName(@RequestBody @Validated CheckNicknameParams params){
        Account account = accountService.updateAccount(params.getNickname(), params.getAddress());
        if(account == null) {
            return ResponseUtils.makeErrorResponse("更改昵称失败");
        }
        return ResponseUtils.makeOkResponse();
    }

    @ApiOperation("返回账户信息")
    @PostMapping(value = "getAccountInfo", produces = "application/json")
    public ResponseEntity<Account> getAccountInfo(@ApiParam("若账户未创建过，则后台根据默认语言新建一个并返回") @RequestBody @Validated AccountInfoParams infoParams) {
        Account account = accountService.checkAccount(infoParams.getAddress(), infoParams.getNation());
        return ResponseUtils.makeOkResponse(account);
    }

    @ApiOperation("更改国家")
    @PostMapping(value="updateNation", produces = "application/json")
    public ResponseEntity<Account> updateNation(@RequestBody @Validated NationParams nationParams) {
        return ResponseUtils.makeOkResponse(accountService.updateNation(nationParams.getAddress(), nationParams.getNation()));
    }

    private static class AccountInfoParams {
        @ApiModelProperty("地址")
        @NotBlank
        private String address;
        @ApiModelProperty("手机app语言所属国家 限制 China/Korea/Japan/Malaysia/Russia/Other")
        @Pattern(regexp = "China|Korea|Japan|Malaysia|Russia|Other", flags = Pattern.Flag.CASE_INSENSITIVE)
        private String nation;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }
    }

    private static class NationParams  {
        @ApiModelProperty("地址")
        @NotBlank
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
        private String address;
        @ApiModelProperty("要替换的国家 限制 China/Korea/Japan/Malaysia/Russia/Other")
        @Pattern(regexp = "China|Korea|Japan|Malaysia|Russia|Other", flags = Pattern.Flag.CASE_INSENSITIVE)
        private String nation;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }
    }

    private static class CheckNicknameParams {
        @ApiModelProperty("地址")
        @NotBlank
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
        private String address;
        @ApiModelProperty("昵称")
        @NotBlank
        private String nickname;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    private static class NicknameInfo {
        private Boolean isUsed;

        public Boolean getUsed() {
            return isUsed;
        }

        public void setUsed(Boolean used) {
            isUsed = used;
        }
    }

}
