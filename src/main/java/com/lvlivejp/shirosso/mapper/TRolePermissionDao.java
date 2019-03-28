package com.lvlivejp.shirosso.mapper;

import com.lvlivejp.shirosso.model.TPermission;
import com.lvlivejp.shirosso.model.TRolePermission;

import java.util.List;

/**
 * 通用 Mapper 代码生成器
 *
 * @author mapper-generator
 */
public interface TRolePermissionDao extends tk.mybatis.mapper.common.Mapper<TRolePermission> {

    List<TPermission> getPerNameByUserName(String username);
}




