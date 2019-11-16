package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Wall extends GameObject{

  private int x;
  private int y;
  private int angle;
  //private boolean isBreakable;

  private BufferedImage img;
  private Rectangle bounds;           //use for collisions
  private BufferedImage wimg;


  Wall(int x, int y, int angle, BufferedImage img) {
    this.x = x;
    this.y = y;
    //this.isBreakable = isBreakable;
    this.img = img;
    this.angle = 0;
    this.bounds = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());
  }

  public Rectangle getBounds(){
    return this.bounds;
  }

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
//OLD CODE

/*
public void spawnWalls(){

    Wall w = null;
    int wH = wimg.getHeight(this);
    int wW = wimg.getWidth(this);
    int xSize = SCREEN_WIDTH / wW;
    int ySize = SCREEN_HEIGHT / wH;

    for(int i=0; i < xSize; i+= wW ) {
      for(int j=0; j < ySize; j+= wH ){
        w = new Wall(i,j,0, wimg);
        cH.objList.add(w);
        buffer.drawImage(wimg,i,j,this);

      }
    }
  }
 */