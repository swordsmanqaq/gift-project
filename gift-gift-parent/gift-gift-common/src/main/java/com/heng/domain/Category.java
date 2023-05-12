package com.heng.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_category")
public class Category extends Model<Category> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;
    @TableField("gmt_create")
    private Date gmtCreate;
    @TableField("gmt_update")
    private Date gmtUpdate;
    private Long pid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Category{" +
        ", id=" + id +
        ", categoryName=" + categoryName +
        ", gmtCreate=" + gmtCreate +
        ", gmtUpdate=" + gmtUpdate +
        ", pid=" + pid +
        "}";
    }
}
