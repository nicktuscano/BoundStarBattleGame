/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;


import static javax.imageio.ImageIO.read;

/**
 * @author Nicholas Tuscano
 */
public class GameWorld extends JPanel {


  public static final int SCREEN_WIDTH = 800;
  public static final int SCREEN_HEIGHT = 600;

  public static final int MAP_WIDTH = 1200;
  public static final int MAP_HEIGHT = 1200;
  private BufferedImage world, wallimg, bwallimg, speedimg, ground, frimg, lifeimg;
  private Graphics2D buffer;
  private JFrame jf;
  private Tank t1, t2;
  CollisionHandler cH;
  MapBuilder mb;

  public static void main(String[] args) {
    Thread x;
    GameWorld trex = new GameWorld();
    trex.init();
    try {
      while (true) {
        trex.t1.update();
        trex.t2.update();
        trex.repaint();
       // System.out.println(trex.t1);
        Thread.sleep(1000 / 144);
      }
    } catch (InterruptedException ignored) {
    }
  }

  private void init() {

    this.jf = new JFrame("Tank Game");
    this.world = new BufferedImage(GameWorld.MAP_WIDTH, GameWorld.MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);

    this.mb = new MapBuilder();
    this.cH = cH.getInstance();
    BufferedImage t1img = null, t2img = null, bulletimg = null;

    try {

      BufferedImage tmp;
      System.out.println(System.getProperty("user.dir"));
      mb.loadMap();

      t1img = read(new File("resources/tank1.png"));
      t2img = read(new File("resources/tank2.png"));
      ground = read(new File("resources/Background.bmp"));
      wallimg = read(new File("resources/Wall1.gif"));
      bwallimg = read(new File("resources/Wall2.gif"));
      bulletimg = read(new File("resources/bullet2.png"));
      speedimg = read(new File("resources/Shield1.gif"));
      lifeimg = read(new File("resources/heart.png"));
      frimg = read(new File("resources/Shield2.gif"));
      mapInit();

    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    t2 = new Tank(350, 450, 0, 0, 0, t1img, bulletimg);
    t1 = new Tank(750, 450, 0, 0, 180, t2img, bulletimg);

    TankControl player2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
    TankControl player1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SHIFT);

    this.jf.setLayout(new BorderLayout());
    this.jf.add(this);
    this.jf.addKeyListener(player1);
    this.jf.addKeyListener(player2);
    this.jf.setSize(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT + 30);
    this.jf.setResizable(false);
    jf.setLocationRelativeTo(null);
    this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.jf.setVisible(true);

  }

  @Override
  public void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g;

    buffer = world.createGraphics();
    drawBackGroundWithTileImage();
    super.paintComponent(g2);
    this.t1.drawImage(buffer);
    this.t2.drawImage(buffer);
    drawMap(buffer);
    //g2.drawImage(world, 0,0,null);

    int t1x = checkBX(t1);
    int t1y = checkBY(t1);
    int t2x = checkBX(t2);
    int t2y = checkBY(t2);


    BufferedImage p1View = this.world.getSubimage(t1x, t1y, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
    BufferedImage p2View = this.world.getSubimage(t2x, t2y, SCREEN_WIDTH / 2, SCREEN_HEIGHT);


    g2.drawImage(p1View, 0, 0, null);
    g2.drawImage(p2View, SCREEN_WIDTH / 2, 0, null);


    AffineTransform minimap = AffineTransform.getTranslateInstance(SCREEN_WIDTH / 2.25, 490);
    minimap.scale(.10, .10);
    g2.drawImage(world, minimap, null);

  }

  //modified from airstrike source code
  public void drawBackGroundWithTileImage() {

    int TileWidth = ground.getWidth(null);
    int TileHeight = ground.getHeight(null);
    int NumberX = (MAP_WIDTH / TileWidth);
    int NumberY = (MAP_HEIGHT / TileHeight);

    for (int i = -1; i <= NumberY; i++) {
      for (int j = 0; j <= NumberX; j++) {
        buffer.drawImage(ground, j * TileWidth,
            i * TileHeight, TileWidth,
            TileHeight, null);
      }
    }
  }

  void drawMap(Graphics g) {
    for (int i = 0; i < cH.getInstance().getList().size(); i++) {
      GameObject obj = cH.getInstance().getList().get(i);
      obj.drawImage(g);
    }
  }

  void mapInit() {
    int TileW = wallimg.getWidth(null);
    int TileH = wallimg.getHeight(null);
    Wall w;
    PowerUp p;
    BreakableWall bw;
    for (int i = 0; i < 37; i++) {
      for (int j = 0; j < 37; j++) {
        if (mb.map[i][j].equals('#')) {
          w = new Wall(j * TileW, i * TileH, 0, wallimg);
          cH.getInstance().getList().add(w);
        } else if (mb.map[i][j].equals('0')) {
          bw = new BreakableWall(j * TileW, i * TileH, 0, bwallimg);
          cH.getInstance().getList().add(bw);
        } else if (mb.map[i][j].equals('S')) {
          p = new PowerUp(j * TileW, i * TileH, true,false,false, speedimg);
          cH.getInstance().getList().add(p);
        } else if (mb.map[i][j].equals('L')) {
          p = new PowerUp(j * TileW, i * TileH, false,true,false, lifeimg);
          cH.getInstance().getList().add(p);
        } else if (mb.map[i][j].equals('F')) {
          p = new PowerUp(j * TileW, i * TileH, false,false,true, frimg);
          cH.getInstance().getList().add(p);
        }
      }
    }
  }

  public int checkBX(Tank tank) {
    int thing = tank.getX() - SCREEN_WIDTH / 4;
    if (thing < 0) {
      thing = 0;
    } else if (thing > MAP_WIDTH - SCREEN_WIDTH / 2) {
      thing = MAP_WIDTH - SCREEN_WIDTH / 2;
    }
    return thing;
  }

  public int checkBY(Tank tank) {
    int thing = tank.getY();
    if (thing < 0) {
      thing = 0;
    } else if (thing > MAP_HEIGHT - SCREEN_HEIGHT) {
      thing = MAP_HEIGHT - SCREEN_HEIGHT ;
    }
    return thing;
  }
}

