package com.heng.service;


import com.heng.query.GiftDocQuery;
import com.heng.util.AjaxResult;

/**
 * @author shkstart
 * @create 2023-05-10 16:36
 */
public interface IGiftDocService {
    AjaxResult search(GiftDocQuery query);

}
