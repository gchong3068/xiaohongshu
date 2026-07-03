package com.gchong3068.framework.biz.operationlog.aspect;

import com.gchong3068.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author gc
 * @date 2024年05月05日 20:30
 */
@Slf4j
@Aspect
public class ApiOperationLogAspect {
    /** 以自定义 @ApiOperationLog 注解为切点，凡是添加 @ApiOperationLog 的方法，都会执行环绕中的代码 */
    @Pointcut("@annotation(com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog)")
    public void apiOperationLog(){}


    /**
     * 环绕
     * @author gc
     * @date 2024/5/5 20:34
     * @param joinPoint ProceedingJoinPoint
     * @return java.lang.Object
     */
    @Around("apiOperationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        //请求开始时间
        long startTime = System.currentTimeMillis();

        //获取请求类和方法
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        //请求入参
        Object[] args = joinPoint.getArgs();
        String argsJsonStr = Arrays.stream(args).map(toJsonStr()).collect(Collectors.joining(", "));

        //功能描述信息
        String description = getApiOperationLogDescription(joinPoint);

        //打印
        log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} =================================== ",
                description, argsJsonStr, className, methodName);

        //执行切点方法
        Object result = joinPoint.proceed();

        //执行耗时
        long executionTime = System.currentTimeMillis() - startTime;

        //打印出参信息
        log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== ",
                description, executionTime, JsonUtils.toJsonString(result));


        return result;
    }


    /**
     * 获取注解详细信息
     * @author gc
     * @date 2024/5/5 20:48
     * @param joinPoint ProceedingJoinPoint
     * @return java.lang.String
     */
    private String getApiOperationLogDescription(ProceedingJoinPoint joinPoint){
        //从 ProceedingJoinPoint 获取 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //使用 MethodSignature 获取当前被注解的 Method
        Method method = signature.getMethod();

        //从 Method 中提取 LogExecution 注解
        ApiOperationLog apiOperationLog  = method.getAnnotation(ApiOperationLog.class);

        //从 LogExecution 注解中提取 description 属性
        return apiOperationLog.description();
    }


    private Function<Object,String > toJsonStr(){
        return JsonUtils::toJsonString;
    }
}
