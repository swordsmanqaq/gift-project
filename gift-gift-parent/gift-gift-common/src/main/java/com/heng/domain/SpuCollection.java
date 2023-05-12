package com.heng.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_spu_collection")
public class SpuCollection extends Model<SpuCollection> {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("spu_id")
    private Long spuId;
    @TableField("spu_name")
    private String spuName;
    @TableField("user_id")
    private Long userId;
    @TableField("create_time")
    private Date createTime;
    @TableField("tenant_id")
    private Long tenantId;
    @TableField("tenant_name")
    private String tenantName;
    private Integer state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SpuCollection{" +
        ", id=" + id +
        ", spuId=" + spuId +
        ", spuName=" + spuName +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", tenantId=" + tenantId +
        ", tenantName=" + tenantName +
        ", state=" + state +
        "}";
    }
}
