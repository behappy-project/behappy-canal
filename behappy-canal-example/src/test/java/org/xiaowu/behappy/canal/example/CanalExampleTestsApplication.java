package org.xiaowu.behappy.canal.example;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaowu.behappy.canal.example.model.User;
import org.xiaowu.behappy.canal.example.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
public class CanalExampleTestsApplication {

    @Autowired
    UserRepository userRepository;

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

    @SneakyThrows
    @Test
    void testBlob(){
        User user = userRepository.getOne(1);
        File img = new File("C:\\Users\\94391\\Pictures\\Camera Roll\\本田.jpg");
        byte[] bytes = fileToByte(img);

        //Files.write(Paths.get("D:\\本田.jpg"), bytes);
        user.setLogo(bytes);
        user.setFlag(false);
        userRepository.save(user);
    }

    static byte[] fileToByte(File img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi;
            bi = ImageIO.read(img);
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
