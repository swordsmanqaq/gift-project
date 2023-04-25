package com.heng.clients;/**
 * @author shkstart
 * @create 2023-04-25 13:06
 */

import com.heng.domain.LoginUser;
import com.heng.util.AjaxResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *@Auther:Jarvis
 *@Date:2023年04月2023/4/25日13:06
 *@Description:
 */
@Component
@Slf4j
public class AuthorizationClientFallbackFactory implements FallbackFactory<AuthorizationClient> {

    @Override
    public AuthorizationClient create(Throwable throwable) {
        return new AuthorizationClient() {
            @Override
            public AjaxResult addOrUpdate(LoginUser loginUser) {
                log.error("熔断降级");
                return AjaxResult.me().setSuccess(false).setMessage(throwable.getMessage()).setResultObj(loginUser);
            }
        };
    }
}
