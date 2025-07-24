package com.exam.myblogs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author Tomanage
 * @since 2025-07-21
 */

@Data
@TableName("article")
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("summary")
    private String summary;

    @TableField("author_id")
    private Integer authorId;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("status")
    private Integer status; // 0-草稿，1-已发布

    @TableField("view")
    private Integer view;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private String category;

    // 非数据库字段，用于存储标签
    @TableField(exist = false)
    private List<String> tags;

    // 非数据库字段，用于存储元数据
    @TableField(exist = false)
    private Meta meta;

    @Data
    public static class Meta {
        private Integer likes;
    }
}
