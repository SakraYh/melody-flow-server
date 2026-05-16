package cn.edu.seig.melodyflow.service;

import cn.edu.seig.melodyflow.model.dto.AdminDTO;
import cn.edu.seig.melodyflow.model.entity.Admin;
import cn.edu.seig.melodyflow.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sunpingli
 * @since 2025-01-09
 */
public interface IAdminService extends IService<Admin> {

    // 管理员注册
    Result register(AdminDTO adminDTO);

    // 管理员登录
    Result login(AdminDTO adminDTO);

    // 退出登录
    Result logout(String token);
}
