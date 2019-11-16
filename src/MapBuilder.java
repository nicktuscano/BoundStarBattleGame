package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MapBuilder {

  private static BufferedReader mapLoader;
  CollisionHandler cH;

  protected static Character[][] map = new Character[37][37];




  public void loadMap() throws IOException {
    int x = 0;
    String line;
    mapLoader = new BufferedReader(new FileReader("resources/map2.txt"));
    while ((line = mapLoader.readLine()) != null) {
      for (int y=0; y< line.length(); y++) {
        map[x][y] = line.charAt(y);
      }
      x++;
    }
  }

}
