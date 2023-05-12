package com.heng.DTO;/**
 * @author shkstart
 * @create 2023-05-09 19:07
 */

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/9日19:07
 *@Description:
 */

public class SpuOnsaleDTO {
    private Long id;
    private Long spuId;
    private String cover;
    private String name;
    private Long tenantId;
    private String tenantName;
    private Integer commentCount;
    private Integer saleCount;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String specProperties;
    private String skuProperties;
    private Long typeId;
    private String typeName;
    private Long brandId;
    private String brandName;
    private Date onsaleTime;


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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getOnsaleTime() {
        return onsaleTime;
    }

    public void setOnsaleTime(Date onsaleTime) {
        this.onsaleTime = onsaleTime;
    }

    @Override
    public String toString() {
        return "SpuOnsaleDTO{" +
                "id=" + id +
                ", spuId=" + spuId +
                ", cover='" + cover + '\'' +
                ", name='" + name + '\'' +
                ", tenantId=" + tenantId +
                ", tenantName='" + tenantName + '\'' +
                ", commentCount=" + commentCount +
                ", saleCount=" + saleCount +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", specProperties='" + specProperties + '\'' +
                ", skuProperties='" + skuProperties + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", onsaleTime=" + onsaleTime +
                '}';
    }

}
