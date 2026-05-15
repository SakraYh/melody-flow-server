package cn.edu.seig.vibemusic.service.impl;

import cn.edu.seig.vibemusic.constant.JwtClaimsConstant;
import cn.edu.seig.vibemusic.constant.MessageConstant;
import cn.edu.seig.vibemusic.enumeration.RoleEnum;
import cn.edu.seig.vibemusic.mapper.AdminMapper;
import cn.edu.seig.vibemusic.model.dto.AdminDTO;
import cn.edu.seig.vibemusic.model.entity.Admin;
import cn.edu.seig.vibemusic.result.Result;
import cn.edu.seig.vibemusic.service.IAdminService;
import cn.edu.seig.vibemusic.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sunpingli
 * @since 2025-01-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 管理员注册
     *
     * @param adminDTO 管理员信息
     * @return 结果
     */
    @Override
    public Result register(AdminDTO adminDTO) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminDTO.getUsername()));
        if (admin != null) {
            return Result.error(MessageConstant.USERNAME + MessageConstant.ALREADY_EXISTS);
        }

        String passwordMD5 = DigestUtils.md5DigestAsHex(adminDTO.getPassword().getBytes());
        Admin adminRegister = new Admin();
        adminRegister.setUsername(adminDTO.getUsername()).setPassword(passwordMD5);

        if (adminMapper.insert(adminRegister) == 0) {
            return Result.error(MessageConstant.REGISTER + MessageConstant.FAILED);
        }
        return Result.success(MessageConstant.REGISTER + MessageConstant.SUCCESS);
    }

    /**
     * 管理员登录
     *
     * @param adminDTO 管理员信息
     * @return 结果
     */
    @Override
    public Result login(AdminDTO adminDTO) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminDTO.getUsername()));
        if (admin == null) {
            return Result.error(MessageConstant.USERNAME + MessageConstant.ERROR);
        }

        if (admin.getPassword().equals(DigestUtils.md5DigestAsHex(adminDTO.getPassword().getBytes()))) {
            // 登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.ROLE, RoleEnum.ADMIN.getRole());
            claims.put(JwtClaimsConstant.ADMIN_ID, admin.getAdminId());
            claims.put(JwtClaimsConstant.USERNAME, admin.getUsername());
            String token = JwtUtil.generateToken(claims);

            // 将token存入redis
            stringRedisTemplate.opsForValue().set(token, token, 6, TimeUnit.HOURS);

            return Result.success(MessageConstant.LOGIN + MessageConstant.SUCCESS, token);
        }

        return Result.error(MessageConstant.PASSWORD + MessageConstant.ERROR);
    }

    /**
     * 登出
     *
     * @param token 认证token
     * @return 结果
     */
    @Override
    public Result logout(String token) {
        // 注销token
        Boolean result = stringRedisTemplate.delete(token);
        if (result != null && result) {
            return Result.success(MessageConstant.LOGOUT + MessageConstant.SUCCESS);
        } else {
            return Result.error(MessageConstant.LOGOUT + MessageConstant.FAILED);
        }
    }
}
