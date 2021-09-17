package com.ma.provider;

import com.ma.common.service.FileLinkService;
import com.ma.common.service.ObsService;
import com.ma.common.service.UserLoginService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ProviderApplicationTests {
    @Resource
    private ObsService obsService;
    @Resource
    private FileLinkService fileLinkService;
    @Resource
    private UserLoginService userLoginService;

    @Test
    void testUser(){
        System.out.println(userLoginService.getLoginUser("admin"));
    }
    @Test
    void testGetFileUrl(){
        System.out.println(obsService.getFileUrl("test.png"));
    }
    @Test
    void contextLoads() {
    }

}
