package com.heng.doc;/**
 * @author shkstart
 * @create 2023-05-10 15:25
 */

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日15:25
 *@Description:
 */
@Data
@Document(indexName = "gift-shop", type = "gift")
public class GiftDoc {

    @Id
    private Long id;      //spuId
    @Field(type = FieldType.Keyword)
    private String cover;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer ="ik_smart" )
    private String name;
    @Field(type = FieldType.Long)
    private Long tenantId;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer ="ik_smart")
    private String tenantName;
    @Field(type = FieldType.Long)
    private Long typeId;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer ="ik_smart")
    private String typeName;
    @Field(type = FieldType.Long)
    private Long brandId;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer ="ik_smart")
    private String brandName;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    @Field(type = FieldType.Integer)
    private Integer saleCount;
    @Field(type = FieldType.Double)
    private BigDecimal maxPrice;
    @Field(type = FieldType.Double)
    private BigDecimal minPrice;
    @Field(type = FieldType.Keyword)
    private String specProperties;
    @Field(type = FieldType.Keyword)
    private String skuProperties;
    @Field(type = FieldType.Date)
    private Date onsaleTime = new Date();

}
