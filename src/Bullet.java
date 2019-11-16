package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet extends GameObject {

  private int x;
  private int y;
  private int vx;
  private int vy;
  private int R = 3;
  private int angle;
  private final int speed = 3;
  public boolean isAlive;
  private BufferedImage img;
  private Rectangle bounds;//use for collisions
  public Tank tank;
  CollisionHandler cH;

  public Bullet(Tank tank, int x, int y, int angle, BufferedImage img) {
    this.tank = tank;
    this.x = x;
    this.y = y;
    this.img = img;
    this.angle = angle;
    this.bounds = new Rectangle(x, y, this.img.getWidth() / 4, this.img.getHeight() / 4);
    this.cH = cH.getInstance();
    this.isAlive = true;
  }

  public void update() { //shoot
    //while (checkBorder()) {
      //this.x++;
      //this.y++;
      //checkBorder(this, cH);

      vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
      vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
      this.x += vx*speed;
      this.y += vy*speed;
      updateBounds();
      cH.checkCollision(this);
      //this.drawImage();

      //checkCollision(this);
      //cH.removeThing(this);
  }
  public Rectangle getBounds(){
    return this.bounds;
  }

  private void updateBounds(){
    this.bounds = new Rectangle(x, y, this.img.getWidth() / 4 , this.img.getHeight() / 4);
  }

  public void handleCollision(GameObject obj, ArrayList<Bullet> ammo){
    if (obj instanceof Wall){
      //ammo.remove(this);
      this.isAlive = false;
    }  if (obj instanceof PowerUp){  //breakable wall
      return;
    } if (obj instanceof BreakableWall){
      //obj.hit(collidables);
      this.isAlive = false;
      cH.getList().remove(obj);
    } if (obj instanceof Tank){
      //obj.hit(collidables);
      if(obj != this.tank) {
        this.isAlive = false;
        ((Tank) obj).hit();

      }
    }
  }

  void drawImage(Graphics g) {
      if (isAlive) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.scale(.3, .3);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
      }
  }

  @Override
  public String toString() {
    return "x =" + x + ", y =" + y + ", angle =" + angle;
  }

}
