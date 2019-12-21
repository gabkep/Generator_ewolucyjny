package projekt;

import java.util.Random;

public enum MapDirection {
    N,
    NW,
    W,
    SW,
    S,
    SE,
    E,
    NE;

    public static MapDirection getRandomDirection()
    {
        Random rnd = new Random();
        int randomRotation = rnd.nextInt(8);
        MapDirection direction = MapDirection.N;
        return direction.changeDirection(randomRotation);
    }

    public String toString() {
        String s = "";
        switch (this) {
            case N: s = "N";
                break;
            case NW: s = "NW";
                break;
            case W: s = "W";
                break;
            case SW: s = "SW";
                break;
            case S: s = "S";
                break;
            case SE: s = "SE";
                break;
            case E: s = "E";
                break;
            case NE: s = "NE";
                break;
        }
        return s;
    }

    public MapDirection changeDirection(int rotation)
    {
        MapDirection newDirection = this;
        while(rotation > 0) {
            newDirection = next(newDirection);
            rotation--;
        }
        return newDirection;
    }

    public Vector2d toUnitVector() {
        Vector2d ret = new Vector2d(0,0);
        switch (this) {
            case N: ret = new Vector2d(0, 1);
                break;
            case NE: ret = new Vector2d(1, 1);
                break;
            case E: ret = new Vector2d(1, 0);
                break;
            case SE: ret = new Vector2d(1, -1);
                break;
            case S: ret = new Vector2d(0, -1);
                break;
            case SW: ret = new Vector2d(-1, -1);
                break;
            case W: ret = new Vector2d(-1, 0);
                break;
            case NW: ret = new Vector2d(-1, 1);
                break;
        }
        return ret;
    }

    private MapDirection next(MapDirection currentDirection) {
        MapDirection dir = currentDirection;
        switch (currentDirection) {
            case N: dir = NE;
                break;
            case NE: dir = E;
                break;
            case E: dir = SE;
                break;
            case SE: dir = S;
                break;
            case S: dir = SW;
                break;
            case SW: dir = W;
                break;
            case W: dir = NW;
                break;
            case NW: dir = N;
                break;
        }
        return dir;
    }
}
