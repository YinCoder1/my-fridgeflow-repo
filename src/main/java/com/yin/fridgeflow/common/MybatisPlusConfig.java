package com.yin.fridgeflow.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 插件配置。
 * <p>注册两个内部拦截器：</p>
 * <ul>
 *   <li>{@link PaginationInnerInterceptor} - 分页插件，使 selectPage 生效（数据库类型 MySQL）</li>
 *   <li>{@link OptimisticLockerInnerInterceptor} - 乐观锁插件，使 {@code @Version} 字段生效
 *       （采购项的认领/取消/完成依赖此插件保证并发安全）</li>
 * </ul>
 *
 * @author yin
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 注册 MyBatis-Plus 拦截器链，加入分页与乐观锁内部拦截器。
     * @return 配置好的 MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页（MySQL）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
