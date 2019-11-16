package src;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class PowerUp extends GameObject{

  private int x;
  private int y;
  public boolean isSpeed, isLife, isFR;
  private BufferedImage img;
  private Rectangle bounds;

  PowerUp( int x, int y, boolean isSpeed, boolean isLife, boolean isFR, BufferedImage img) {

    this.x = x;
    this.y = y;
    this.img = img;
    this.isSpeed = isSpeed;
    this.isLife = isLife;
    this.isFR = isFR;
    this.bounds = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());

  }

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", angle=" + 0;
  }

  public Rectangle getBounds(){
    return this.bounds;
  }
  void drawImage(Graphics g) {
    AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
    rotation.scale(.5,.5);
    rotation.rotate(Math.toRadians(0), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.img, rotation, null);
  }
}
