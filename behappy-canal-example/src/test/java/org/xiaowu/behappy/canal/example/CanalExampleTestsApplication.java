package org.xiaowu.behappy.canal.example;

import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Test;

import java.util.Optional;

//@SpringBootTest
public class CanalExampleTestsApplication {

    @Test
    public void test() {
        //System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));//testData
        //System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));//testData
        //System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));//TestData
        //
        //System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));//testdata
        //System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));//test_data
        //System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));//test-data

        //Class<CanalExampleTestsApplication> applicationClass = CanalExampleTestsApplication.class;
        //System.out.println(applicationClass.getSimpleName());
        //System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, applicationClass.getSimpleName()));//test_data

        System.out.println("T_Txxxx_Info".equalsIgnoreCase("t_txxxx_info"));
    }

}
