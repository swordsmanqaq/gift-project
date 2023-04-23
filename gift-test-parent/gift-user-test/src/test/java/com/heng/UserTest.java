package com.heng;



import com.heng.domain.User;
import com.heng.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserStart.class)
public class UserTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testInsert() throws Exception{
        User user = new User(null, "ww");
        userService.insert(user);
        System.out.println(user);
    }

    @Test
    public void testDelete() throws Exception{
        List<User> users = userService.selectList(null);
        users.forEach(System.out::println);
        userService.deleteById(1L);
        System.out.println("====================================");
        users = userService.selectList(null);
        users.forEach(System.out::println);

    }

    @Test
    public void testUpdate() throws Exception{
        User user = userService.selectById(2L);
        System.out.println(user);
        user.setName(user.getName()+"-edit");
        userService.updateById(user);
        userService.selectById(2L);
        System.out.println(user);

    }

    @Test
    public void testLoadAll() throws Exception{

        List<User> users = userService.selectList(null);
        users.forEach(System.out::println);

    }

    @Test
    public void testLoadById() throws Exception{
        System.out.println(userService.selectById(1L));
    }

}
