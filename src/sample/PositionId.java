package sample;


public class PositionId {
    public int x;
    public int y;

    PositionId(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PositionId position = (PositionId) o;

        if(x != position.x) return false;
        if(y != position.y) return false;

        return true;
    }

    public int hashCode(){
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
