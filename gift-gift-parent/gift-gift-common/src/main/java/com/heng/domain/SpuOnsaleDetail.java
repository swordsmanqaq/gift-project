package com.heng.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("t_goods_spu_onsale_detail")
public class SpuOnsaleDetail extends Model<SpuOnsaleDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("spu_id")
    private Long spuId;
    /**
     * 封面
     */
    private String cover;
    /**
     * 名称
     */
    private String name;
    /**
     * 店铺id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 店铺名称
     */
    @TableField("tenant_name")
    private String tenantName;
    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;
    /**
     * 销量
     */
    @TableField("sale_count")
    private Integer saleCount;
    /**
     * 最大价格
     */
    @TableField("max_price")
    private BigDecimal maxPrice;
    /**
     * 最小价格
     */
    @TableField("min_price")
    private BigDecimal minPrice;
    @TableField("spec_properties")
    private String specProperties;
    @TableField("sku_properties")
    private String skuProperties;


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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public String getSpecProperties() {
        return specProperties;
    }

    public void setSpecProperties(String specProperties) {
        this.specProperties = specProperties;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SpuOnsaleDetail{" +
        ", id=" + id +
        ", spuId=" + spuId +
        ", cover=" + cover +
        ", name=" + name +
        ", tenantId=" + tenantId +
        ", tenantName=" + tenantName +
        ", commentCount=" + commentCount +
        ", saleCount=" + saleCount +
        ", maxPrice=" + maxPrice +
        ", minPrice=" + minPrice +
        ", specProperties=" + specProperties +
        ", skuProperties=" + skuProperties +
        "}";
    }
}
