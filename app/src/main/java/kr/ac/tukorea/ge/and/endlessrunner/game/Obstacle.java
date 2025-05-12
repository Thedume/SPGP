package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Obstacle extends Sprite implements IBoxCollidable {
    private static final float SPEED = 800f;

    public Obstacle(int resId, float x, float y, float size) {
        super(resId, x, y, size, size);
    }

    @Override
    public void update() {
        float dy = SPEED * GameView.frameTime;
        y += dy;
        dstRect.offset(0, dy);

        if (y - height / 2 > Metrics.height) {
            Scene.top().remove(MainScene.Layer.obstacle, this);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }
}
