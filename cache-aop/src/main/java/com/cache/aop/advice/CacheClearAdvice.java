package com.cache.aop.advice;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.cache.aop.advice.common.SingleCacheAdvice;
import com.cache.aop.annotation.CacheCleaner;
import com.cache.aop.vo.CacheAnnotationData;
import com.cache.handler.CacheBasicService;

/**
 * 缓存清除注解的处理
 * @author yangyang.xu
 *
 */
@Component
@Aspect
public class CacheClearAdvice extends SingleCacheAdvice<CacheCleaner> {

    public CacheClearAdvice() {
        super(CacheCleaner.class);
    }
    
    @Pointcut("@annotation(com.cache.aop.annotation.CacheCleaner)")
    public void cleanCache() {
    }
    
    @Around("cleanCache()")
    public Object cleanCache(final ProceedingJoinPoint pjp) throws Throwable {
        CacheAnnotationData cacheAnnotationData = getAnnotationData(pjp); 
        CacheBasicService service = getCacheBaseService(cacheAnnotationData);
        Map<String, Object> keyList = getCacheKey(cacheAnnotationData, pjp.getArgs());
        if (keyList != null && keyList.size() > 0) {
            for (String key : keyList.keySet()) {
                service.delete(key, service.getOptTimeOut());
            }
        }
        Object result = pjp.proceed();
        return result;
    }

}
