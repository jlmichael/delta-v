package deltav.java;

import deltav.core.config.DeltaVGameConfig;
import playn.core.PlayN;
import playn.java.JavaPlatform;

import deltav.core.DeltaV;

public class DeltaVJava {

    public static void main(String[] args) {
        JavaPlatform.Config config = new JavaPlatform.Config();
        // use config to customize the Java platform, if needed
        config.width = DeltaVGameConfig.SCREEN_WIDTH;
        config.height = DeltaVGameConfig.SCREEN_HEIGHT;
        JavaPlatform.register(config);
        PlayN.run(new DeltaV());
    }
}
