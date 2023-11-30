package si.um.feri.astronaut.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ShieldComponent implements Component , Pool.Poolable{

    public boolean hit;
    public boolean shield;
    public float duration;
    public boolean getShield(){
        return shield;
    }
    public void setShield(boolean shield){
        this.shield = shield;
    }

    @Override
    public void reset() {
        hit = false;
    }
}
