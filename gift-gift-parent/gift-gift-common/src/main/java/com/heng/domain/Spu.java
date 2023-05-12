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
 * spu表
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_spu")
public class Spu extends Model<Spu> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品编号，唯一
     */
    @TableField("spu_no")
    private String spuNo;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 最低售价
     */
    @TableField("low_price")
    private BigDecimal lowPrice;
    @TableField("high_price")
    private BigDecimal highPrice;
    /**
     * 分类id
     */
    @TableField("category_id")
    private Long categoryId;
    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 店铺id
     */
    @TableField("tenant_id")
    private Long tenantId;
    @TableField("tenant_name")
    private String tenantName;
    @TableField("gmt_update")
    private Date gmtUpdate;
    /**
     * 0 待审核  1 审核通过 -1 审核失败 2 上架  3 下架
     */
    private Integer state;
    /**
     * 封面
     */
    private String cover;
    @TableField("onsale_time")
    private Date onsaleTime;
    @TableField("offsale_time")
    private Date offsaleTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpuNo() {
        return spuNo;
    }

    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
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

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Date getOnsaleTime() {
        return onsaleTime;
    }

    public void setOnsaleTime(Date onsaleTime) {
        this.onsaleTime = onsaleTime;
    }

    public Date getOffsaleTime() {
        return offsaleTime;
    }

    public void setOffsaleTime(Date offsaleTime) {
        this.offsaleTime = offsaleTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Spu{" +
        ", id=" + id +
        ", spuNo=" + spuNo +
        ", goodsName=" + goodsName +
        ", lowPrice=" + lowPrice +
        ", highPrice=" + highPrice +
        ", categoryId=" + categoryId +
        ", brandId=" + brandId +
        ", gmtCreate=" + gmtCreate +
        ", tenantId=" + tenantId +
        ", tenantName=" + tenantName +
        ", gmtUpdate=" + gmtUpdate +
        ", state=" + state +
        ", cover=" + cover +
        ", onsaleTime=" + onsaleTime +
        ", offsaleTime=" + offsaleTime +
        "}";
    }
}
