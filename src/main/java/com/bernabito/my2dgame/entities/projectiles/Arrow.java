package com.bernabito.my2dgame.entities.projectiles;

import com.bernabito.my2dgame.engine.Collidable;
import com.bernabito.my2dgame.entities.GenericEntity;
import com.bernabito.my2dgame.utils.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class Arrow extends Projectile {

    private static BufferedImage arrowImage;

    static {
        try {
            arrowImage = Objects.requireNonNull(ImageIO.read(Arrow.class.getResourceAsStream("/Images/Arrow.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Rectangle2D.Float hitbox;
    private Shape rotatedHitbox;

    public Arrow(GenericEntity source, float x, float y, Vector2f speedVector, int dmg) {
        super(source, x, y, speedVector, dmg);
        hitbox = new Rectangle2D.Float(x, y, arrowImage.getWidth(), arrowImage.getHeight());
        AffineTransform rotationTransform = AffineTransform.getRotateInstance(speedVector.getX(), speedVector.getY(), x, y);
        rotatedHitbox = rotationTransform.createTransformedShape(hitbox);
    }

    @Override
    public void updateState() {
        super.updateState();
        hitbox.x += speedVector.getX();
        hitbox.y += speedVector.getY();
        AffineTransform rotationTransform = AffineTransform.getRotateInstance(speedVector.getX(), speedVector.getY(), hitbox.x, hitbox.y);
        rotatedHitbox = rotationTransform.createTransformedShape(hitbox);
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform backupTransform = g.getTransform();
        AffineTransform transform = AffineTransform.getRotateInstance(speedVector.getX(), speedVector.getY(), hitbox.x, hitbox.y);
        transform.preConcatenate(backupTransform);
        g.setTransform(transform);
        int x = Math.round(hitbox.x);
        int y = Math.round(hitbox.y);
        g.drawImage(arrowImage, x, y, null);
        g.setTransform(backupTransform);
    }

    @Override
    public Rectangle2D getHitbox() {
        // Non riflette il vero hitbox della freccia
        return rotatedHitbox.getBounds2D();
    }

    @Override
    public boolean hasCollided(Collidable collider) {
        return rotatedHitbox.intersects(collider.getHitbox());
    }
}
