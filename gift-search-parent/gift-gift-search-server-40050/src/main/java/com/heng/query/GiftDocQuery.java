package com.heng.query;/**
 * @author shkstart
 * @create 2023-05-10 16:27
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日16:27
 *@Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftDocQuery extends BaseQuery{

    //================筛选条件========= ===========
    private Long id;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
    private Long brandId;
    private Long typeId;
    private Long tenantId;
    private BigDecimal price;
    //================筛选条件====================

    //================排序条件====================
    //================排序条件====================
    //================分页条件====================
    //================分页条件====================
}
