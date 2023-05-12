package com.heng.controller;/**
 * @author shkstart
 * @create 2023-05-10 16:25
 */

import com.heng.query.GiftDocQuery;
import com.heng.service.IGiftDocService;
import com.heng.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日16:25
 *@Description:
 */
@RestController
@RequestMapping("/giftdoc")
public class GiftDocController {

    @Autowired
    private IGiftDocService giftDocService;

    @PostMapping("/search")
    public AjaxResult search(@RequestBody GiftDocQuery query){
        return giftDocService.search(query);
    }
}
