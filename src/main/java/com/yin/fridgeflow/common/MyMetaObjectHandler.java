package com.yin.fridgeflow.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器。
 * <p>对实体中标注了 {@code @TableField(fill = FieldFill.INSERT / INSERT_UPDATE)} 的字段自动填充时间：</p>
 * <ul>
 *   <li>INSERT 时填充 createTime / updateTime / joinTime</li>
 *   <li>UPDATE 时填充 updateTime</li>
 * </ul>
 * <p>仅对声明了对应 fill 策略的字段生效（strictXxxFill 会校验字段是否存在）。</p>
 *
 * @author yin
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时自动填充：createTime、updateTime、joinTime 均设为当前时间。
     * @param metaObject MyBatis 反射对象，封装当前实体
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "joinTime", LocalDateTime.class, now);
    }

    /**
     * 修改时自动填充：updateTime 设为当前时间。
     * @param metaObject MyBatis 反射对象，封装当前实体
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
