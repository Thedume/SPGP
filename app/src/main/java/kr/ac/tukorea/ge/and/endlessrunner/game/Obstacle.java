package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;
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
    
    public enum Type {
        NORMAL,
        WALL
    }
    
    private Type type;

    public Obstacle(int resId, float x, float yOffset, Type type) {
        super(resId, x, -1500f + yOffset, 0, 0);
        this.startX = Metrics.width / 2f;
        this.endX = x;
        this.type = type;
        
        if (type == Type.WALL) {
            // 벽 타입은 높이만 더 크게 설정
            this.startScale = 0.3f;
            this.endScale = 1.25f;
        }
    }

    @Override
    public void update() {
        float dy = SPEED * GameView.frameTime;
        y += dy;

        float t = (y - startY) / (endY - startY);
        t = Math.min(Math.max(t, 0f), 1f);

        float scale = startScale + (endScale - startScale) * t;
        float size = 150f * scale;

        x = startX + (endX - startX) * t;
        
        if (type == Type.WALL) {
            // 벽 타입은 높이만 1.5배로 설정
            setSize(size, size * 1.5f);
        } else {
            setSize(size, size);
        }

        if (y - height / 2 > Metrics.height) {
            Scene.top().remove(MainScene.Layer.obstacle, this);
        }
    }

    @Override
    public RectF getCollisionRect() {
        if (Scene.top() instanceof MainScene) {
            MainScene scene = (MainScene) Scene.top();
            if (!scene.isMale()) {
                // 여성 캐릭터의 경우 충돌 판정을 더 작게
                float centerX = dstRect.centerX();
                float centerY = dstRect.centerY();
                float width = dstRect.width() * GameConfig.Player.Female.COLLISION_SIZE_MULTIPLIER;
                float height = dstRect.height() * GameConfig.Player.Female.COLLISION_SIZE_MULTIPLIER;
                return new RectF(
                    centerX - width/2,
                    centerY - height/2,
                    centerX + width/2,
                    centerY + height/2
                );
            }
        }
        return dstRect;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        dstRect.set(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
    }
    
    public boolean isWall() {
        return type == Type.WALL;
    }
}

