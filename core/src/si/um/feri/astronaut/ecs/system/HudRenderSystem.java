package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.astronaut.common.GameManager;


public class HudRenderSystem extends EntitySystem {

    private static final float PADDING = 20.0f;

    private final SpriteBatch batch;
    private final Viewport hudViewport;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    public HudRenderSystem(SpriteBatch batch, Viewport hudViewport, BitmapFont font) {
        this.batch = batch;
        this.hudViewport = hudViewport;
        this.font = font;
    }

    @Override
    public void update(float deltaTime) {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        font.setColor(Color.WHITE);

        // score
        // SCORE
        String scoreString = "SCORE: " + GameManager.INSTANCE.getResult();
        layout.setText(font, scoreString);
        float scoreX = hudViewport.getWorldWidth() / 6 - layout.width / 2;
        float y = hudViewport.getWorldHeight() - layout.height;
        font.draw(batch, layout, scoreX, y);

// HEALTH
        String healthString = "HEALTH: " + GameManager.INSTANCE.getHealth();
        layout.setText(font, healthString);
        float healthX = 3 * hudViewport.getWorldWidth() / 6 - layout.width / 2;
        font.draw(batch, layout, healthX, y);

// SHIELD
        int shieldDuration = (int) GameManager.INSTANCE.getShieldDuration();
        String shieldString = "SHIELD: " + shieldDuration;
        layout.setText(font, shieldString);
        float shieldX = 5 * hudViewport.getWorldWidth() / 6 - layout.width / 2;
        font.draw(batch, layout, shieldX, y);


        if (GameManager.INSTANCE.isGameOver()) {
            font.setColor(Color.RED);
            layout.setText(font, "THE END");
            float endX = (hudViewport.getWorldWidth() + layout.width) / 2 - layout.width;
            float endY = (hudViewport.getWorldHeight() + layout.height) / 2 - layout.height;
            font.draw(batch, layout, endX, endY);
        }

        batch.end();
    }
}
