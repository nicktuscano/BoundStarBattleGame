package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class BreakableWall extends GameObject{

  private int x;
  private int y;
  private int angle;
  private int health;
  //private boolean isBreakable;

  private BufferedImage img;
  private Rectangle bounds;           //use for collisions
  private BufferedImage wimg;


  BreakableWall(int x, int y, int angle, BufferedImage img) {
    this.x = x;
    this.y = y;
    this.health = 6;
    this.img = img;
    this.angle = 0;
    this.bounds = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());
  }

  public Rectangle getBounds(){
    return this.bounds;
  }

  /*public void hit(ArrayList<GameObject> collidables){
    this.health -= 3;
    if (this.health == 0) {
      collidables.remove(this);
    }

  }*/

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", angle=" + angle;
  }

  void drawImage(Graphics g) {
    AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
    rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.img, rotation, null);
  }
}