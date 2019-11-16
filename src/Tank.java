package src;

import java.awt.Font;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tank extends GameObject{

  private int x;
  private int y;
  private int spawnX;
  private int spawnY;
  private int vx;
  private int vy;
  private int angle;

  //public ArrayList<Bullet> liveBullets = new ArrayList<Bullet>(50);

  private int R = 2;
  private final int ROTATIONSPEED = 2;
  private Bullet bullet;
  private BufferedImage img, bimg;
  private boolean UpPressed;
  private boolean DownPressed;
  private boolean RightPressed;
  private boolean LeftPressed;
  private boolean ShootPressed;
  private float coolDown;
  private float timer;
  private int health;
  private int lives;
  private Rectangle bounds;//use for collisions
  public ArrayList<Bullet> ammo = new ArrayList<Bullet>();
  CollisionHandler cH;

  Tank(int x, int y, int vx, int vy, int angle, BufferedImage img, BufferedImage bimg) {
    this.x = x;
    this.y = y;
    this.spawnX = x;
    this.spawnY = y;
    this.vx = vx;
    this.vy = vy;
    this.img = img;
    this.bimg = bimg;
    this.angle = angle;
    this.bounds = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());
    this.cH = cH.getInstance();
    this.coolDown = 50;
    this.timer = 0;
    this.health = 100;
    cH.getInstance().getList().add(this);
    this.lives = 3;
  }

  public Rectangle getBounds(){
    return this.bounds;
  }
  private void updateBounds(){
    this.bounds = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());
  }

  public int getX(){
    return this.x;
  }
  public int getY(){
    return this.y;
  }
  public int getVx(){
    return this.vx;
  }
  public int getVy(){
    return this.vy;
  }
  public ArrayList<Bullet> getAmmo(){
    return this.ammo;
  }

  void toggleUpPressed() {
    this.UpPressed = true;
  }

  void toggleDownPressed() {
    this.DownPressed = true;
  }

  void toggleRightPressed() {
    this.RightPressed = true;
  }

  void toggleLeftPressed() {
    this.LeftPressed = true;
  }

  void toggleShootPressed() {
    this.ShootPressed = true;
  }

  void unToggleUpPressed() {
    this.UpPressed = false;
  }

  void unToggleDownPressed() {
    this.DownPressed = false;
  }

  void unToggleRightPressed() {
    this.RightPressed = false;
  }

  void unToggleLeftPressed() {
    this.LeftPressed = false;
  }

  void unToggleShootPressed() {
    this.ShootPressed = false;
  }

  public void update() {
     if (lives >= 1) {
       if (this.health == 0) {
         this.lives--;
         this.health = 100;
         this.x = spawnX;
         this.y = spawnY;
       }
       if (this.UpPressed) {
         this.moveForwards();
       }
       if (this.DownPressed) {
         this.moveBackwards();
       }
       if (this.LeftPressed) {
         this.rotateLeft();
       }
       if (this.RightPressed) {
         this.rotateRight();
       }
       if (this.ShootPressed) {
         shootBullet();
       }
       cH.getInstance().checkCollision(this);

       for (int i = 0; i < ammo.size(); i++) {
         bullet = ammo.get(i);
         bullet.update();
       }
       this.timer++;
       //this.refresh();
     } else {
       System.exit(0);
     }
  }


  private void refresh(){
    Bullet b;
    for (int i = 0 ; i < this.ammo.size(); i++){
      b = this.ammo.get(i);
      if (!b.isAlive){
        this.ammo.remove(i);
        break;                            //break to prevent concurrency issues, just removes 1 dead bullet every update
      }
    }
  }

  private void shootBullet(){
    if (this.timer >= this.coolDown) {
      bullet = new Bullet(this, this.x, this.y, this.angle, bimg);
      ammo.add(bullet);
      this.timer = 0;
    }
  }

  public void handleCollision(GameObject obj){
    if (obj instanceof Wall || obj instanceof BreakableWall){
      if(this.UpPressed){
        this.x -= vx;
        this.y -= vy;
      } else if(this.DownPressed){
        this.x += vx;
        this.y += vy;
      }
    } else if (obj instanceof PowerUp) {
      if (((PowerUp) obj).isSpeed) {
        this.R += 2;
        cH.getList().remove(obj);
      } if (((PowerUp) obj).isLife) {
        this.lives += 1;
        cH.getList().remove(obj);
      } if (((PowerUp) obj).isFR) {
        this.coolDown -= 15;
        cH.getList().remove(obj);
      }
    }
  }

  public void hit(){
    this.health -= 5;
  }

  private void rotateLeft() {
    this.angle -= this.ROTATIONSPEED;
    updateBounds();
  }
  private void rotateRight() {
    this.angle += this.ROTATIONSPEED;
    updateBounds();
  }

  private void moveBackwards() {
    vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
    vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
    x -= vx;
    y -= vy;
    checkBorder();
    updateBounds();
  }

  private void moveForwards() {
    vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
    vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
    x += vx;
    y += vy;
    checkBorder();
    updateBounds();
  }

  private void checkBorder() {
    if (x < 30) {
      x = 30;
    }
    if (x >= GameWorld.MAP_WIDTH - 88) {
      x = GameWorld.MAP_WIDTH - 88;
    }
    if (y < 40) {
      y = 40;
    }
    if (y >= GameWorld.MAP_HEIGHT - 80) {
      y = GameWorld.MAP_HEIGHT - 80;
    }
  }

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", angle=" + angle;
  }

  void drawImage(Graphics g) {

    for(Bullet bullet : ammo){
      bullet.drawImage(g);
    }

    AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
    rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.img, rotation, null);

    if(this.health >= 75) {
      g2d.setColor(Color.GREEN);
    } else if(this.health > 25) {
      g2d.setColor(Color.YELLOW);
    } else if(this.health < 25) {
      g2d.setColor(Color.RED);
    }
    g2d.fillRect(this.x, this.y+50,50, 10);
    g2d.setColor(Color.PINK);
    g2d.setFont(new Font("bold", Font.BOLD, 15));
    g2d.drawString(String.valueOf(this.lives),this.x-10,this.y+60);
  }

}
