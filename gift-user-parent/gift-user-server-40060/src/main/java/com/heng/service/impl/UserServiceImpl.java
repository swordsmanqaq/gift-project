package com.heng.service.impl;

import com.heng.clients.AuthorizationClient;
import com.heng.constants.BaseConstants;
import com.heng.domain.LoginUser;
import com.heng.domain.User;
import com.heng.domain.UserInfo;
import com.heng.dto.RegisterDTO;
import com.heng.mapper.UserInfoMapper;
import com.heng.mapper.UserMapper;
import com.heng.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.heng.util.AjaxResult;
import com.heng.util.MD5Utils;
import com.heng.util.VerifyCodeUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthorizationClient authorizationClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 手机注册
     * @param registerDTO
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void phoneRegister(RegisterDTO registerDTO) {
        //1、参数非空校验
        ParameterNonnullCheck(registerDTO);
        //密码加密
        //生成随机字符串盐值
        String salt = UUID.randomUUID().toString();
        String md5Password = MD5Utils.encrypByMd5(salt + registerDTO.getPassword() + "user");
        //保存登录信息login_user
        LoginUser loginUser = new LoginUser();
        loginUser.setTel(registerDTO.getPhone());
        loginUser.setType(1);
        loginUser.setUsername(registerDTO.getPhone());
        loginUser.setPassword(md5Password);
        AjaxResult result = authorizationClient.addOrUpdate(loginUser);
        if (!result.isSuccess()){
            throw new RuntimeException("注册失败");
        }
        //获取返回回来的loginUserId
        Long loginUserId = Long.valueOf(result.getResultObj().toString());

        //保存User表中的信息
        User user = new User();
        user.setCreateTime(System.currentTimeMillis());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setNickName(registerDTO.getPhone());
        user.setBitState(1L);
        user.setSecLevel(1);
        user.setLoginId(loginUserId);
        userMapper.insert(user);

        //保存user_info表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setRegChannel(1);
        userInfo.setRegTime(System.currentTimeMillis());
        userInfo.setLevel(0);
        userInfo.setGrowScore(0);
        userInfoMapper.insert(userInfo);

    }

    /**
     * 手机验证码
     * @param phone
     */
    @Override
    public void sendRegisterMessageCode(String phone) {
        //1、参数非空校验
        if(StringUtils.isEmpty(phone)){
            throw new RuntimeException("手机号不能为空");
        }
        //2、给手机号发送验证码，并将验证码存入redis并设置过期时间
        //先从redis中判断手机号是否已经存在验证码
        String redisCodeKey = BaseConstants.MessageCode.REGISTER_SMS_CODE + phone;
        Object valueResult = redisTemplate.opsForValue().get(redisCodeKey);
        //用来存放验证码
        String redisCodeValue = "";
        if (Objects.nonNull(valueResult)){
            //说明redis中存在验证码
            //然后判断是否已过一分钟，即是否可以重发
            Long expireTime = redisTemplate.opsForValue().getOperations().getExpire(redisCodeKey, TimeUnit.MINUTES);
            if (expireTime >= 4){
                //未过重发时间，返回错误信息
                throw new RuntimeException("请不要频繁发送验证码");
            }else {
                //已过重发时间，将redis中的验证码重新设置过期时间在放入redis中
                //将验证码赋值给redisCodeValue
                redisCodeValue = valueResult.toString();
            }
        }else {
            //说明redis中没有验证码
            //随机生成一个六位数的验证码
            redisCodeValue = VerifyCodeUtils.generateVerifyMessageCode(6);
        }
        //存入到redis中，并设置过期时间
        redisTemplate.opsForValue().set(redisCodeKey,redisCodeValue,5,TimeUnit.MINUTES);

        //通过手机短信发送验证码给用户
        String content = String.format("您的注册验证码为%s，有效期为五分钟，请及时输入", redisCodeValue);
//        SmsUtil.sendSms(phone, content);
        System.out.println(content);

    }


    /**
     * 参数非空校验
     *
     * @param dto
     */
    public void ParameterNonnullCheck(RegisterDTO dto) {
        //所有参数的非空校验
        if (StringUtils.isEmpty(dto.getPhone()) || StringUtils.isEmpty(dto.getPassword()) || StringUtils.isEmpty(dto.getSmsCode())) {
            throw new RuntimeException("请填写所有信息");
        }
        //手机验证码比较
        //先判断验证码是否过期
        if (Objects.isNull(redisTemplate.opsForValue().get(BaseConstants.MessageCode.REGISTER_SMS_CODE + dto.getPhone()))) {
            throw new RuntimeException("手机验证码已过期，请重新获取");
        }
        //没有过期在判断是否相等
        if (!dto.getSmsCode().equals(redisTemplate.opsForValue().get(BaseConstants.MessageCode.REGISTER_SMS_CODE + dto.getPhone()))) {
            throw new RuntimeException("手机验证码错误，请重新输入");
        }
    }

}
