package com.gchong3068.xiaohongshu.gateway.auto;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gchong3068.xiaohongshu.gateway.constant.RedisKeyConstant;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * 权限认证接口拓展
 * @author gc
 * @date 2024年06月13日 19:14
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;



    /**
     * @Description: 获取用户权限
     * @Auther: gchong3068
     * @Date: 2024/6/17 10:52
     */
    @Override
    @SneakyThrows
    public List<String> getPermissionList(Object loginId, String loginType) {

        String userRolesKey = RedisKeyConstant.buildUserROleKey(Long.valueOf(loginId.toString()));

        // 根据用户 ID ，从 Redis 中获取该用户的角色集合
        String userRolesValue = redisTemplate.opsForValue().get(userRolesKey);

        if (StringUtils.isBlank(userRolesValue)){
            return null;
        }
        // 将 JSON 字符串转换为 List<String> 角色集合
        List<String> userRoleKeys = objectMapper.readValue(userRolesValue, new TypeReference<>() {});
        if (CollUtil.isNotEmpty(userRoleKeys)){
            //角色-权限 Redis Key 集合
            List<String> rolePermissionKeys = userRoleKeys.stream()
                    .map(RedisKeyConstant::buildRolePermissionKey)
                    .toList();

            List<String> rolePermissionsValues = redisTemplate.opsForValue().multiGet(rolePermissionKeys);

            if (CollUtil.isNotEmpty(rolePermissionsValues)){
                List<String> permissions = Lists.newArrayList();

                //遍历所有角色权限集合，添加到permissions集合
                rolePermissionsValues.forEach(jsonValue ->{
                    try {
                        List<String> rolePermissions = objectMapper.readValue(jsonValue, new TypeReference<>() {});
                        permissions.addAll(rolePermissions);
                    } catch (JsonProcessingException e) {
                        log.error("==> JSON 解析错误: ",e);
                    }
                });
                return permissions;
            }
        }
        return null;
    }


    /**
     * @Description: 获取用户角色
     * @Auther: gchong3068
     * @Date: 2024/6/17 10:52
     */
    @SneakyThrows
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        //构建用户-角色 Redis Key
        String userROleKey = RedisKeyConstant.buildUserROleKey(Long.valueOf(loginId.toString()));
        // 根据用户 ID ，从 Redis 中获取该用户的角色集合
        String useRolesValue = redisTemplate.opsForValue().get(userROleKey);

        if (StringUtils.isBlank(useRolesValue)){
            return null;
        }
        return objectMapper.readValue(useRolesValue, new TypeReference<>() {});
    }
}
