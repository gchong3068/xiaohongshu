package com.gchong3068.xiaohongshu.user.biz.runner;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.user.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.PermissionDO;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.RoleDO;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.RolePermissionDO;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.PermissionDOMapper;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.RoleDOMapper;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.RolePermissionDOMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author gc
 * @date 2024年06月04日 22:17
 */

@Component
@Slf4j
public class PushRolePermission2RedisRunner implements ApplicationRunner {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private PermissionDOMapper permissionDOMapper;
    @Resource
    private RolePermissionDOMapper rolePermissionDOMapper;

    //权限同步标记 Key
    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";
    @Override
    public void run(ApplicationArguments args) throws Exception  {
        log.info("==> 服务启动，开始同步角色权限数据到 Redis 中...");
        try{
            // 是否能够同步数据: 原子操作，只有在键 PUSH_PERMISSION_FLAG 不存在时，才会设置该键的值为 "1"，并设置过期时间为 1 天
            boolean canPushed = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS));

            // 如果无法同步权限数据
            if (!canPushed) {
                log.warn("==> 角色权限数据已经同步至 Redis 中，不再同步...");
                return;
            }

            List<RoleDO> roleDOS = roleDOMapper.selectEnabledList();

            if(CollUtil.isNotEmpty(roleDOS)){

                List<Long> roleIds = roleDOS.stream().map(RoleDO::getId).toList();

                List<RolePermissionDO> rolePermissionDOS = rolePermissionDOMapper.selectByRoleIds(roleIds);

                //按角色Id分组
                Map<Long, List<Long>> roleIdPermissionIdsMap = rolePermissionDOS.stream().collect(
                        Collectors.groupingBy(RolePermissionDO::getRoleId,
                                Collectors.mapping(RolePermissionDO::getPermissionId, Collectors.toList()))
                );
                // 查询 APP 端所有被启用的权限
                List<PermissionDO> permissionDOS = permissionDOMapper.selectAppEnabledList();
                // 权限 ID - 权限 DO
                Map<Long, PermissionDO> permissionIdDOMap  = permissionDOS.stream().collect(
                        Collectors.toMap(PermissionDO::getId, permissionDO -> permissionDO)
                );

                //组织 角色Id-权限 关系
                Map<String,List<String>> roleIdPermissionMap  = Maps.newHashMap();

                //循环所有角色
                roleDOS.forEach(roleDO -> {
                    Long roleId = roleDO.getId();
                    //当前用户roleKey
                    String roleKey = roleDO.getRoleKey();
                    //当前角色的权限Id集合
                    List<Long> permissionIds = roleIdPermissionIdsMap.get(roleId);
                    if (CollUtil.isNotEmpty(permissionIds)){
                        List<String> permissionKeys = Lists.newArrayList();
                        permissionIds.forEach(permissionId ->{

                            PermissionDO permissionDO = permissionIdDOMap.get(permissionId);
                            permissionKeys.add(permissionDO.getPermissionKey());
                        });

                        roleIdPermissionMap.put(roleKey,permissionKeys);
                    }
                });

                //同步redis
                roleIdPermissionMap.forEach((roleKey,permissions) -> {
                    String key = RedisKeyConstants.buildRolePermissionsKey(roleKey);
                    redisTemplate.opsForValue().set(key, JsonUtils.toJsonString(permissions));
                });
            }
            log.info("==>同步角色权限数据到 Redis成功...");
        }catch (Exception e){
            log.error("==>同步角色权限数据到 Redis失败...",e);
        }
    }
}
