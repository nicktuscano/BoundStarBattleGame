package src;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public abstract class GameObject{


  private int x;
  private int y;
  private int angle;
  private BufferedImage img;
  private Rectangle bounds; //use for collisions
  CollisionHandler cH;
 


  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", angle=" + angle;
  }

  public Rectangle getBounds(){
    return this.bounds;
  }
  void drawImage(Graphics g) {
    AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
    rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.img, rotation, null);
  }


}
