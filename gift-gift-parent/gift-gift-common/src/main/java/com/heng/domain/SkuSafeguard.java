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
 * sku增值保障
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_sku_safeguard")
public class SkuSafeguard extends Model<SkuSafeguard> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * sku_id
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * safeguard_id
     */
    @TableField("safeguard_id")
    private Long safeguardId;
    @TableField("gmt_create")
    private Date gmtCreate;
    @TableField("gmt_update")
    private Date gmtUpdate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSafeguardId() {
        return safeguardId;
    }

    public void setSafeguardId(Long safeguardId) {
        this.safeguardId = safeguardId;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SkuSafeguard{" +
        ", id=" + id +
        ", skuId=" + skuId +
        ", safeguardId=" + safeguardId +
        ", gmtCreate=" + gmtCreate +
        ", gmtUpdate=" + gmtUpdate +
        "}";
    }
}
