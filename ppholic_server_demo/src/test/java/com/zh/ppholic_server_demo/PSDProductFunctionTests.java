package com.zh.ppholic_server_demo;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@SuiteDisplayName("psd product function test")
@RunWith(JUnitPlatform.class)
@SelectPackages("com.zh.ppholic_server_demo")
class PSDProductFunctionTests {

}
