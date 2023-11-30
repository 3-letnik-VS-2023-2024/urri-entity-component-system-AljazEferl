package si.um.feri.astronaut.common;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private int result;
    private int health;

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

    public boolean isGameOver() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public void damage() {
        health--;
    }
}
