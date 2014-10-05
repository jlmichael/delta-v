package deltav.core.model;

import deltav.core.config.DeltaVGameConfig;
import deltav.core.util.GravityUtil;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Surface;

/**
 * A class for representing the state of the player's ship.  This stores
 * things like fuel remaining, velocity, heading, etc.
 */
public class DeltaVShip extends DeltaVObject implements Keyboard.Listener {

    /**
     * The direction the ship is currently pointing, in radians.
     */
    private double heading;

    /**
     * A flag to store if the ship is currently thrusting or not.
     */
    private boolean thrusting;

    /**
     * An int to store which direction the ship is currently rotating.  -1
     * indicates counterclockwise rotation, 0 is no rotation, and 1 means
     * clockwise rotation.
     */
    private int rotationDirection;

    /**
     * The amount of fuel remaining on the ship.
     */
    private int fuelRemaining;

    // Getters and setters.
    public void setHeading(double heading) {
        this.heading = heading;
    }

    public int getFuelRemaining() {
        return fuelRemaining;
    }

    public void setFuelRemaining(int fuelRemaining) {
        this.fuelRemaining = fuelRemaining;
    }

    public void addFuel(int addedFuel) {
        fuelRemaining = Math.min(fuelRemaining + addedFuel, DeltaVGameConfig.MAX_FUEL);
        fuelRemaining = Math.max(fuelRemaining, 0);
    }

    // Keyboard listener implementation
    @Override
    public void onKeyDown(Keyboard.Event event) {
        Key key = event.key();
        if (key.equals(Key.SPACE)) {
            thrusting = true;
        } else if (key.equals(Key.LEFT)) {
            rotationDirection = -1;
        } else if (key.equals(Key.RIGHT)) {
            rotationDirection = 1;
        }
    }

    @Override
    public void onKeyTyped(Keyboard.TypedEvent event) {
        // Nothing needed here, I don't think?
    }

    @Override
    public void onKeyUp(Keyboard.Event event) {
        Key key = event.key();
        if (key.equals(Key.SPACE)) {
            thrusting = false;
        } else if(key.equals(Key.LEFT) || key.equals(Key.RIGHT)) {
            rotationDirection = 0;
        }
    }

    /**
     * The ship's implementation bounds it within the screen and takes
     * the force of the ship's thruster into account.
     *
     * @param deltaT The number of millis since the last update.
     */
    public void updatePhysics(int deltaT) {
        GravityUtil.ForceOfGravity force = GravityUtil.getForceOfGravityOnObject(this);

        // Apply the acceleration to the object.
        velocityInX += force.forceInX * deltaT / 1000D;
        velocityInY += force.forceInY * deltaT / 1000D;

        // If we are rotating, update our heading.
        if(rotationDirection != 0) {
            heading += DeltaVGameConfig.ROTATION_SPEED * rotationDirection;
        }

        // If we are thrusting, apply the thruster force.  Use heading to get
        // the X and Y components.
        // TODO: Honestly, it's a little silly to treat gravity and the thruster
        // separately.  Maybe replace GravityUtil with just a ForcesUtil that
        // would calculate all forces on an object.
        if(thrusting && fuelRemaining > 0) {
            fuelRemaining--;
            double thrusterForceInX = DeltaVGameConfig.THRUSTER_THRUST * Math.sin(heading);
            double thrusterForceInY = DeltaVGameConfig.THRUSTER_THRUST * Math.cos(heading);

            velocityInX += thrusterForceInX * deltaT / 1000D;
            velocityInY += thrusterForceInY * deltaT / -1000D;
        }

        // Apply the velocity to the object.
        xPosition += velocityInX;
        yPosition += velocityInY;

        // Bound the ship within the screen.
        if (xPosition < 0 + imgWidth / 2) {
            xPosition = 0 + imgWidth / 2;
            velocityInX = 0;
        } else if (xPosition > DeltaVGameConfig.SCREEN_WIDTH - imgWidth / 2) {
            xPosition = DeltaVGameConfig.SCREEN_WIDTH - imgWidth / 2;
            velocityInX = 0;
        }
        if (yPosition < 0 + imgHeight / 2) {
            yPosition = 0 + imgHeight / 2;
            velocityInY = 0;
        } else if (yPosition > DeltaVGameConfig.SCREEN_HEIGHT - imgHeight / 2) {
            yPosition = DeltaVGameConfig.SCREEN_HEIGHT - imgHeight / 2;
            velocityInY = 0;
        }
    }

    @Override
    public void draw(Surface surface) {
        imgLayer.setRotation((float)heading);
        super.draw(surface);
    }
}
