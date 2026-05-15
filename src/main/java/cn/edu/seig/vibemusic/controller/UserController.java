package cn.edu.seig.vibemusic.controller;


import cn.edu.seig.vibemusic.constant.MessageConstant;
import cn.edu.seig.vibemusic.model.dto.*;
import cn.edu.seig.vibemusic.model.vo.UserVO;
import cn.edu.seig.vibemusic.result.Result;
import cn.edu.seig.vibemusic.service.IUserService;
import cn.edu.seig.vibemusic.service.MinioService;
import cn.edu.seig.vibemusic.util.BindingResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "用户认证", description = "注册、登录、个人信息管理、密码修改")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private MinioService minioService;

    @Operation(summary = "发送邮箱验证码")
    @GetMapping("/sendVerificationCode")
    public Result sendVerificationCode(
            @Parameter(description = "邮箱地址") @RequestParam @Email String email) {
        return userService.sendVerificationCode(email);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterDTO userRegisterDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        boolean isCodeValid = userService.verifyVerificationCode(userRegisterDTO.getEmail(), userRegisterDTO.getVerificationCode());
        if (!isCodeValid) {
            return Result.error(MessageConstant.VERIFICATION_CODE + MessageConstant.INVALID);
        }
        return userService.register(userRegisterDTO);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return userService.login(userLoginDTO);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserVO> getUserInfo() {
        return userService.userInfo();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return userService.updateUserInfo(userDTO);
    }

    @Operation(summary = "更新用户头像")
    @PatchMapping("/updateUserAvatar")
    public Result updateUserAvatar(
            @Parameter(description = "头像图片文件") @RequestParam("avatar") MultipartFile avatar) {
        String avatarUrl = minioService.uploadFile(avatar, "users");
        return userService.updateUserAvatar(avatarUrl);
    }

    @Operation(summary = "修改密码（需旧密码）")
    @PatchMapping("/updateUserPassword")
    public Result updateUserPassword(@RequestBody @Valid UserPasswordDTO userPasswordDTO,
                                     @Parameter(description = "Bearer token") @RequestHeader("Authorization") String token,
                                     BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return userService.updateUserPassword(userPasswordDTO, token);
    }

    @Operation(summary = "重置密码（需验证码）")
    @PatchMapping("/resetUserPassword")
    public Result resetUserPassword(@RequestBody @Valid UserResetPasswordDTO userResetPasswordDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        boolean isCodeValid = userService.verifyVerificationCode(userResetPasswordDTO.getEmail(), userResetPasswordDTO.getVerificationCode());
        if (!isCodeValid) {
            return Result.error(MessageConstant.VERIFICATION_CODE + MessageConstant.INVALID);
        }
        return userService.resetUserPassword(userResetPasswordDTO);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result logout(
            @Parameter(description = "Bearer token") @RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }

    @Operation(summary = "注销账号")
    @DeleteMapping("/deleteAccount")
    public Result deleteAccount() {
        return userService.deleteAccount();
    }

}
