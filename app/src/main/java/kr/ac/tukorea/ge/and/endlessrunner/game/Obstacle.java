package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Obstacle extends Sprite implements IBoxCollidable {
    private static final float SPEED = 600f;
    private float startX, endX;
    private float startY = -500f;
    private float endY = Metrics.height * 0.8f;
    private float startScale = 0.3f;
    private float endScale = 1.25f;

    public Obstacle(int resId, float x, float yOffset) {
        super(resId, x, -1500f + yOffset, 0, 0);
        this.startX = Metrics.width / 2f; // 중앙에서 시작
        this.endX = x; // 목표 x로 퍼짐
    }

    @Override
    public void update() {
        float dy = SPEED * GameView.frameTime;
        y += dy;

        float t = (y - startY) / (endY - startY);
        t = Math.min(Math.max(t, 0f), 1f); // Clamp

        float scale = startScale + (endScale - startScale) * t;
        float size = 150f * scale;

        x = startX + (endX - startX) * t;
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

