package org.xiaowu.behappy.canal.client.spring.boot.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.xiaowu.behappy.canal.client.constant.Constant;

/**
 * @author xiaowu
 * Banner初始化
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (applicationContext.getParent() == null && applicationContext.getParent() != applicationContext) {
            String bannerShown = System.getProperty(Constant.BANNER_SHOWN, "true");
            if (!Boolean.parseBoolean(bannerShown)) {
                return;
            }
            String banner = "  ____  ______ _    _          _____  _______     __   _____          _   _          _      \n" +
                    " |  _ \\|  ____| |  | |   /\\   |  __ \\|  __ \\ \\   / /  / ____|   /\\   | \\ | |   /\\   | |     \n" +
                    " | |_) | |__  | |__| |  /  \\  | |__) | |__) \\ \\_/ /  | |       /  \\  |  \\| |  /  \\  | |     \n" +
                    " |  _ <|  __| |  __  | / /\\ \\ |  ___/|  ___/ \\   /   | |      / /\\ \\ | . ` | / /\\ \\ | |     \n" +
                    " | |_) | |____| |  | |/ ____ \\| |    | |      | |    | |____ / ____ \\| |\\  |/ ____ \\| |____ \n" +
                    " |____/|______|_|  |_/_/    \\_\\_|    |_|      |_|     \\_____/_/    \\_\\_| \\_/_/    \\_\\______|\n" +
                    "                                                                                            \n" +
                    "                                                                                            ";
            System.out.println(banner);
            String implementationVersion = this.getClass().getPackage().getImplementationVersion();
            System.out.println("Version:" + implementationVersion);
            System.out.println("Blog:" + "https://wang-xiaowu.github.io");
            System.out.println("Github:" + "https://github.com/wang-xiaowu");
        }
    }

}
