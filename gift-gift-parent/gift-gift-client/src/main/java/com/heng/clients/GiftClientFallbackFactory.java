package com.heng.clients;/**
 * @author shkstart
 * @create 2023-05-13 10:44
 */

import com.heng.util.AjaxResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/13日10:44
 *@Description:
 */
@Component
public class GiftClientFallbackFactory implements FallbackFactory<GiftClient> {

    @Override
    public GiftClient create(Throwable throwable) {
        return new GiftClient() {
            @Override
            public AjaxResult get(Long id) {
                System.out.println("熔断降级：" + throwable.getMessage());
                return AjaxResult.me().setSuccess(false).setMessage(throwable.getMessage());
            }
        };
    }
}
