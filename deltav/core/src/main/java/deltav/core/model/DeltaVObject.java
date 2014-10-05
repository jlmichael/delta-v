package deltav.core.model;

import deltav.core.util.GravityUtil;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Surface;

import static playn.core.PlayN.graphics;

/**
 * An abstract base class representing anything in the game world that is under
 * the effects of gravity.
 */
public abstract class DeltaVObject {

    /**
     * The image for this object.
     */
    private Image img;

    /**
     * An ImageLayer to store the object on.  This will let us rotate.
     */
    protected ImageLayer imgLayer;

    /**
     * The dimensions of our image.
     */
    protected float imgWidth;
    protected float imgHeight;

    /**
     * The object's current position along the X axis.
     */
    protected double xPosition;

    /**
     * The object's current position along the Y axis.
     */
    protected double yPosition;

    /**
     * The x-component of the object's current velocity.
     */
    protected double velocityInX;

    /**
     * The y-component of the object's current velocity.
     */
    protected double velocityInY;

    // Getters and setters.

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getVelocityInX() {
        return velocityInX;
    }

    public void setVelocityInX(double velocityInX) {
        this.velocityInX = velocityInX;
    }

    public double getVelocityInY() {
        return velocityInY;
    }

    public void setVelocityInY(double velocityInY) {
        this.velocityInY = velocityInY;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
        this.imgWidth = img.width();
        this.imgHeight = img.height();
        this.imgLayer = graphics().createImageLayer(img);
        imgLayer.setOrigin(imgWidth / 2, imgHeight / 2);
    }

    /**
     * Default implementation ignores bounding and only takes gravity into
     * account.  This should be overridden by the subclasses.
     *
     * @param deltaT The number of millis since the last update.
     */
    public void updatePhysics(int deltaT) {
        GravityUtil.ForceOfGravity force = GravityUtil.getForceOfGravityOnObject(this);

        // Apply the acceleration to the object.
        velocityInX += force.forceInX * deltaT / 1000D;
        velocityInY += force.forceInY * deltaT / 1000D;

        // Apply the velocity to the object.
        xPosition += velocityInX;
        yPosition += velocityInY;
    }

    public void draw(Surface surface) {
        imgLayer.setTranslation((float)xPosition, (float)yPosition);
        surface.drawLayer(imgLayer);
    }

    public boolean isBoundedBy(double topLeftX, double bottomRightX, double topLeftY, double bottomRightY) {
        return xPosition > topLeftX &&
                xPosition <= bottomRightX &&
                yPosition > topLeftY &&
                yPosition <= bottomRightY;
    }
}
