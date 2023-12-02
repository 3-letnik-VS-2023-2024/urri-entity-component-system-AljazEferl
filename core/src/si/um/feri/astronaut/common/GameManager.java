package si.um.feri.astronaut.common;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private int result;
    private int health;
    public float duration;
     private int hits;

    private boolean shield;

    private GameManager() {
    }

    public int getResult() {
        return result;
    }

    public void resetResult() {
        result = 0;
        health = 100;
    }

    public void incResult() {
        result++;
    }

    public float getShieldDuration(){
        return duration;

    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isGameOver() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void damage() {
        health = health - 10;
    }
}
