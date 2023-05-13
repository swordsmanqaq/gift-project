package com.heng.dto;/**
 * @author shkstart
 * @create 2023-05-13 10:04
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/13日10:04
 *@Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCarDTO {

    @NotNull(message = "非法参数")
    private Long skuId;
    @NotNull(message = "非法参数")
    private Integer number;
    private Integer isSelected;
    private Long userId;
    private String username;

}
