package com.gchong3068.xiaohongshu.count.biz;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 计数服务启动类
 * @Author: gchong3068
 * @Date: 2024/9/29 22:33
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.count.biz.domain.mapper")
@ComponentScan(basePackages = "com.gchong3068.xiaohongshu.count.biz.config")
public class XiaohongshuCountBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuCountBizApplication.class, args);

        //初始化限流
        // initFlowRules();
    }

    /**
     * 初始化限流
     * @Auther: gchong3068
     * @Date: 2025/6/20 19:54
     **/
    private static void initFlowRules() {

        //1. 创建集合，存放所有限流规则
        List<FlowRule> rules = new ArrayList<>();

        //2. 创建一条心的流量控制规则
        FlowRule rule = new FlowRule();

        //3. 设置需要保护的资源名称
        rule.setResource("findUserCountData");

        //4. 设置限流阈值类型
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        //5. 设置允许的QPS阈值
        rule.setCount(5);

        //6. 将当前规则加入规则集
        rules.add(rule);

        // 7. 加载规则列表到 Sentinel 的规则管理器（使规则生效）
        FlowRuleManager.loadRules(rules);
    }
}
