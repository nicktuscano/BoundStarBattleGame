package src;

import java.awt.*;
import java.util.ArrayList;

//Singleton

public class CollisionHandler {

  private static CollisionHandler cH;
  private ArrayList<GameObject> collidables; // <-- move to this

  public static CollisionHandler getInstance(){
    if(cH == null)
      cH = new CollisionHandler();
    return cH;
  }

  private CollisionHandler (){
    collidables = new ArrayList<>();
  }
  public ArrayList<GameObject> getList() {
    return this.collidables;
  }


  public void checkCollision(Tank tank) {
    GameObject obj;
    Rectangle tb = tank.getBounds();                   //nested for loop to compare all collisions?
    for (int i = 0; i < collidables.size(); i++) {      //handleCollision func for each gameobj
      obj = collidables.get(i);
      if (tb.intersects(obj.getBounds())) {
          tank.handleCollision(obj);
      }
    }
  }
  public void checkCollision(Bullet bullet) {
    GameObject obj;
    Rectangle tb = bullet.getBounds();                   //nested for loop to compare all collisions?
    for (int i = 0; i < collidables.size(); i++) {      //handleCollision func for each gameobj
      obj = collidables.get(i);
      if (tb.intersects(obj.getBounds())) {
        bullet.handleCollision(obj,bullet.tank.getAmmo());
      }
    }
  }
  public void removeThing(GameObject obj) {
    collidables.remove(obj);
  }
}

// List walls = new Arraylist<Wall>(); in gameworld? static or final?
// List tanksNbullets = new Arraylist<GameObject>(); in gameworld? static or final?

  /*private boolean checkWallCollision(Tank tank){
    Wall wall = new wall
    if(walls.get(wall).intersects(wall.bounds))
    return false;
  }
  private boolean checkTankCollision(Tank tank){
    return false;
  }*/
