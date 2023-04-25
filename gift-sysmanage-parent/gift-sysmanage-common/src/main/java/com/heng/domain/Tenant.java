package com.heng.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@TableName("t_tenant")
@NotNull
public class Tenant extends Model<Tenant> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("company_name")
    @NotBlank(message = "{heng.error.sysmange.tenant.companyName.blank}")
    private String companyName;
    @TableField("company_num")
    @NotBlank
    private String companyNum;
    @TableField("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registerTime;
    /**
     * 0待审核，1 审核通过 ， 2审核失败
     */
    private Integer state;
    @NotBlank
    private String address;
    private String logo;
    /**
     * 店铺管理员
     */
    @TableField("admin_id")
    private Long adminId;
    @TableField(exist = false)
    @NotNull
    private Employee admin;
    /**
     * 类型id
     */
    @TableField("type_id")
    private Long typeId;
    @TableField(exist = false)
    private TenantType tenantType;

    @TableField(exist = false)
    @NotNull
    private Long mealId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
    }

    public TenantType getTenantType() {
        return tenantType;
    }

    public void setTenantType(TenantType tenantType) {
        this.tenantType = tenantType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Tenant{" +
        ", id=" + id +
        ", companyName=" + companyName +
        ", companyNum=" + companyNum +
        ", registerTime=" + registerTime +
        ", state=" + state +
        ", address=" + address +
        ", logo=" + logo +
        ", adminId=" + adminId +
        ", typeId=" + typeId +
        "}";
    }
}
