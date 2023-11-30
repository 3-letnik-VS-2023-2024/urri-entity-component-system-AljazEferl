package si.um.feri.astronaut.ecs.system.passive;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import si.um.feri.astronaut.assets.AssetDescriptors;

public class SoundSystem extends EntitySystem {

    private final AssetManager assetManager;

    private Sound damageSound;
    private Sound laughSound;

    public SoundSystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false); // passive system
        init();
    }

    private void init() {
        damageSound = assetManager.get(AssetDescriptors.DAMAGE_SOUNDS);
        laughSound = assetManager.get((AssetDescriptors.LAUGH_SOUNDS));
    }

    public void pickDamage() {
        damageSound.play();
    }
    public void pickLaugh(){
        laughSound.play();
    }
}
