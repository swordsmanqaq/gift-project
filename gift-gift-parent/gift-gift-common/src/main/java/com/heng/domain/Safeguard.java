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
 * 增值保障
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@TableName("t_goods_safeguard")
public class Safeguard extends Model<Safeguard> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 保障名称
     */
    @TableField("safeguard_name")
    private String safeguardName;
    /**
     * 保障价格
     */
    private BigDecimal price;
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

    public String getSafeguardName() {
        return safeguardName;
    }

    public void setSafeguardName(String safeguardName) {
        this.safeguardName = safeguardName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        return "Safeguard{" +
        ", id=" + id +
        ", safeguardName=" + safeguardName +
        ", price=" + price +
        ", gmtCreate=" + gmtCreate +
        ", gmtUpdate=" + gmtUpdate +
        "}";
    }
}
