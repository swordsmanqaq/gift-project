package com.heng.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * sku表
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_sku")
public class Sku extends Model<Sku> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * sku编号,唯一
     */
    @TableField("sku_no")
    private String skuNo;
    /**
     * sku名称(冗余spu_name)
     */
    @TableField("sku_name")
    private String skuName;
    /**
     * 售价
     */
    private BigDecimal price;
    /**
     * 可用库存
     */
    private Integer stock;
    /**
     * 锁定库存
     */
    @TableField("lock_stock")
    private Integer lockStock;
    /**
     * 商铺id,为0表示自营
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * spu_id
     */
    @TableField("spu_id")
    private Long spuId;
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

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getLockStock() {
        return lockStock;
    }

    public void setLockStock(Integer lockStock) {
        this.lockStock = lockStock;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
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
        return "Sku{" +
        ", id=" + id +
        ", skuNo=" + skuNo +
        ", skuName=" + skuName +
        ", price=" + price +
        ", stock=" + stock +
        ", lockStock=" + lockStock +
        ", tenantId=" + tenantId +
        ", spuId=" + spuId +
        ", gmtCreate=" + gmtCreate +
        ", gmtUpdate=" + gmtUpdate +
        "}";
    }
}
