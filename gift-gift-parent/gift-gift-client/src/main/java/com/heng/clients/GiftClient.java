package com.heng.clients;


import com.heng.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author shkstart
 * @create 2023-05-13 10:36
 */
@FeignClient(value = "gift-gift-server",fallbackFactory = GiftClientFallbackFactory.class)
public interface GiftClient {
    @GetMapping("/sku/{id}")
    AjaxResult get(@PathVariable("id") Long id);
}
