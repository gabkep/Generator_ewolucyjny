package projekt;

import java.util.Objects;

public class Vector2d {
    public final Integer x;
    public final Integer y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ")";
    }
    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }
    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }
    public Vector2d upperRight(Vector2d other) {
        int max_x = this.x > other.x ? this.x : other.x;
        int max_y = this.y > other.y ? this.y : other.y;
        return new Vector2d(max_x, max_y);
    }
    public  Vector2d lowerLeft(Vector2d other) {
        int min_x = this.x < other.x ? this.x : other.x;
        int min_y = this.y < other.y ? this.y : other.y;
        return new Vector2d(min_x, min_y);
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }
    public Vector2d substract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d)) {
            return false;
        }
        Vector2d that = (Vector2d) other;
        return this.x == that.x && this.y == that.y;
    }
    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
