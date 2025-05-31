package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Obstacle extends Sprite implements IBoxCollidable {
    private static final float SPEED = 600f;
    private static final float START_Y = -1500f;
    private static final float END_Y = Metrics.height * 0.8f;
    private static final float START_SCALE = 0.3f;
    private static final float END_SCALE = 1.5f;

    public Obstacle(int resId, float x) {
        super(resId, x, START_Y, 0, 0); // 초기 크기는 0, 크기는 update()에서 결정
    }

    @Override
    public void update() {
        float dy = SPEED * GameView.frameTime;
        y += dy;

        float t = (y - START_Y) / (END_Y - START_Y);
        t = Math.min(Math.max(t, 0f), 1f); // Clamp between 0 and 1
        float scale = START_SCALE + (END_SCALE - START_SCALE) * t;
        float size = 150f * scale;
        setSize(size, size);

        if (y - height / 2 > Metrics.height) {
            Scene.top().remove(MainScene.Layer.obstacle, this);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        dstRect.set(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
    }

}

