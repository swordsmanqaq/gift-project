package com.heng.clients;
import com.heng.domain.LoginUser;
import com.heng.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;


/**
 * @author shkstart
 * @create 2023-04-19 16:44
 */
@FeignClient(value = "gift-authorization-server",fallbackFactory = AuthorizationClientFallbackFactory.class)
public interface AuthorizationClient {

    @PutMapping("/loginUser")
    AjaxResult addOrUpdate(@RequestBody LoginUser loginUser);

}
