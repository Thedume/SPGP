package kr.ac.tukorea.ge.and.endlessrunner.game;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.VertScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {
    public enum Layer {
        bg, obstacle, player, ui, controller, COUNT
    }


    private Player player;
    private float playerX = Metrics.width / 2;
    private float playerY = Metrics.height * 0.8f;

    private int score = 0;
    private float distance = 0f;
    private boolean isMale;

    private float startX, startY;
    private static final float SWIPE_THRESHOLD = 50f;

    private float obstacleTimer = 0f;
    private float spawnInterval = 1.2f;
    private int lastLane = -1;

    private static final String TAG = MainScene.class.getSimpleName();


    public MainScene(boolean isMale) {
        this.isMale = isMale;

        initLayers(Layer.COUNT.ordinal());

        // 배경 추가
        add(Layer.bg, new VertScrollBackground(R.mipmap.bg_city, 300));

        // 캐릭터 생성
        int resId = isMale ? R.mipmap.char_male : R.mipmap.char_female;
        player = new Player(resId, 10); // 10fps
        player.setPosition(playerX, playerY, 200, 200);
        add(Layer.player, player);
    }

    @Override
    public void update() {
        super.update();

        score += GameView.frameTime * 100; // 초당 100점 정도
        distance += GameView.frameTime * 300; // 초당 300픽셀 = 3m

        // 장애물 생성 로직
        obstacleTimer += GameView.frameTime;
        if (obstacleTimer >= spawnInterval) {
            obstacleTimer = 0f;
            int lane;
            do {
                lane = (int)(Math.random() * 3);
            } while (lane == lastLane);
            lastLane = lane;

            float x = player.getLaneX(lane);
            float y = -100f;
            float size = 150f;
            Obstacle obs = new Obstacle(R.mipmap.obstacle_box, x);
            add(Layer.obstacle, obs);
        }

        // 충돌 감지
        ArrayList<IGameObject> obstacles = objectsAt(Layer.obstacle);
        for (IGameObject obj : obstacles) {
            if (!(obj instanceof IBoxCollidable)) continue;
            if (CollisionHelper.collides(player, (IBoxCollidable) obj)) {
                Log.d(TAG, "\uD83D\uDCA5 충돌 발생!");
                player.decreaseLife();
                remove(Layer.obstacle, obj);

                if (player.isDead()) {
                    new GameOverScene(score, distance, isMale).change();
                }
                break;
            }
        }
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.controller.ordinal();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = pts[0];
                startY = pts[1];
                return true;
            case MotionEvent.ACTION_UP:
                float dx = pts[0] - startX;
                float dy = pts[1] - startY;

                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > SWIPE_THRESHOLD) {
                    if (dx < 0) {
                        Log.d(TAG, "스와이프 ◀: moveLeft()");
                        player.moveLeft();
                    } else {
                        Log.d(TAG, "스와이프 ▶: moveRight()");
                        player.moveRight();
                    }
                } else if (Math.abs(dy) > SWIPE_THRESHOLD) {
                    if (dy < 0) {
                        Log.d(TAG, "스와이프 ↑: jump()");
                        player.jump();
                    } else {
                        Log.d(TAG, "스와이프 ↓: slide()");
                        player.slide();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);

        // 체력 (왼쪽 상단)
        canvas.drawText("♥ x " + player.getLife(), 30, 60, paint);

        // 점수 (오른쪽 상단)
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Score: " + score, Metrics.width - 30, 60, paint);
    }

}