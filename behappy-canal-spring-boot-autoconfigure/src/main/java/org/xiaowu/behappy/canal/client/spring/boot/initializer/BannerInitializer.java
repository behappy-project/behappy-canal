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
            String banner = " ____     ___  __ __   ____  ____  ____  __ __      ____     ___  ___    ____ _____\n" +
                    "|    \\   /  _]|  |  | /    ||    \\|    \\|  |  |    |    \\   /  _]|   \\  |    / ___/\n" +
                    "|  o  ) /  [_ |  |  ||  o  ||  o  )  o  )  |  |    |  D  ) /  [_ |    \\  |  (   \\_ \n" +
                    "|     ||    _]|  _  ||     ||   _/|   _/|  ~  |    |    / |    _]|  D  | |  |\\__  |\n" +
                    "|  O  ||   [_ |  |  ||  _  ||  |  |  |  |___, |    |    \\ |   [_ |     | |  |/  \\ |\n" +
                    "|     ||     ||  |  ||  |  ||  |  |  |  |     |    |  .  \\|     ||     | |  |\\    |\n" +
                    "|_____||_____||__|__||__|__||__|  |__|  |____/     |__|\\_||_____||_____||____|\\___|\n" +
                    "                                                                                   ";
            System.out.println(banner);
            String implementationVersion = this.getClass().getPackage().getImplementationVersion();
            System.out.println("Version:" + implementationVersion);
            System.out.println("Blog:" + "https://wang-xiaowu.github.io");
            System.out.println("Github:" + "https://github.com/wang-xiaowu");
        }
    }

}
