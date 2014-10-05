package deltav.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import deltav.core.DeltaV;

public class DeltaVHtml extends HtmlGame {

    @Override
    public void start() {
        HtmlPlatform.Config config = new HtmlPlatform.Config();
        // use config to customize the HTML platform, if needed
        HtmlPlatform platform = HtmlPlatform.register(config);
        platform.assets().setPathPrefix("deltav/");
        PlayN.run(new DeltaV());
    }
}
