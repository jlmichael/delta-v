package deltav.core.model;

import deltav.core.config.DeltaVGameConfig;
import deltav.core.util.GravityUtil;

import java.util.Random;

/**
 * A class for representing a chunk of debris floating in the game world.
 */
public class DeltaVDebris extends DeltaVObject {

    /**
     * For debris, we don't bound it within the screen, but within a rectangle
     * slightly larger than the size of the screen.  I want the debris to be
     * seen flying off the screen, but if I don't bound it, they tend to remain
     * off-screen for a very long time.
     *
     * @param deltaT The number of millis since the last update.
     */
    public void updatePhysics(int deltaT) {
        super.updatePhysics(deltaT);

        // Unlike other objects, we bound the ship within the screen.
        if (xPosition < 0 - DeltaVGameConfig.SCREEN_WIDTH * .1) {
            xPosition = 0 - DeltaVGameConfig.SCREEN_WIDTH * .1;
            velocityInX = 0;
        } else if (xPosition > DeltaVGameConfig.SCREEN_WIDTH * 1.1) {
            xPosition = DeltaVGameConfig.SCREEN_WIDTH * 1.1;
            velocityInX = 0;
        }
        if (yPosition < 0 - DeltaVGameConfig.SCREEN_HEIGHT * .1) {
            yPosition = 0 - DeltaVGameConfig.SCREEN_HEIGHT * .1;
            velocityInY = 0;
        } else if (yPosition > DeltaVGameConfig.SCREEN_HEIGHT * 1.1) {
            yPosition = DeltaVGameConfig.SCREEN_HEIGHT * 1.1;
            velocityInY = 0;
        }
    }

    public void respawn() {
        Random r = new Random();
        setxPosition(r.nextInt(DeltaVGameConfig.SCREEN_WIDTH));
        setyPosition(r.nextInt(DeltaVGameConfig.SCREEN_HEIGHT));
        setVelocityInX(r.nextInt(1) == 0 ? r.nextInt(5) : r.nextInt(5) * -1);
        setVelocityInY(r.nextInt(1) == 0 ? r.nextInt(5) : r.nextInt(5) * -1);
    }
}
