package si.um.feri.astronaut.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AmmoComponent implements Component, Pool.Poolable {
    public boolean hit;

    @Override
    public void reset() {
        hit = false;
    }
}
