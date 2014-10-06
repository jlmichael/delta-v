package deltav.core;

import static playn.core.PlayN.*;

import deltav.core.config.DeltaVGameConfig;
import deltav.core.model.DeltaVWorld;
import playn.core.*;

public class DeltaV extends Game.Default {

    /**
     * Our game world.
     */
    private DeltaVWorld world;

    /**
     * An ImmediateLayer to render the game in.
     */
    private ImmediateLayer immLayer;

    public DeltaV() {
        super(10);
    }

    @Override
    public void init() {
        // create and add background image layer
        Image bgImage = assets().getImage("images/bg2.jpg");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);
        world = new DeltaVWorld();
        immLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                surface.clear();
                world.paint(surface);
            }
        });
        graphics().rootLayer().add(immLayer);
    }

    @Override
    public void update(int delta) {
        world.updatePhysics(delta);
    }

    @Override
    public void paint(float alpha) {
        // Nothing to do, we paint each update.
    }
}
