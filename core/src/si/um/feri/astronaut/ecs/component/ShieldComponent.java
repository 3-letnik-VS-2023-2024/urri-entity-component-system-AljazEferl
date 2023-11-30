package si.um.feri.astronaut.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ShieldComponent implements Component , Pool.Poolable{

    public boolean hit;
    public float duration;

    @Override
    public void reset() {
        hit = false;
    }
}
