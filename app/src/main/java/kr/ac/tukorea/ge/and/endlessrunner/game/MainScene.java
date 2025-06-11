package kr.ac.tukorea.ge.and.endlessrunner.game;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
    private boolean isPlayerDead = false;  // 플레이어 생존 상태 추적

    private float startX, startY;
    private static final float SWIPE_THRESHOLD = 50f;

    private int lastLane = -1;

    private static final String TAG = MainScene.class.getSimpleName();

    // 장애물 전용
    private float spawnInterval = 2.5f;          // 장애물 묶음 생성 주기
    private float obstacleTimer = 0f;

    private float spawnDelay = 0.5f;             // 개별 장애물 간 생성 간격
    private float spawnDelayTimer = 0f;

    private Queue<Integer> obstacleQueue = new LinkedList<>(); // 생성 예약된 레인 인덱스들

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
        // 장애물 묶음 생성 예약
        obstacleTimer += GameView.frameTime;
        if (obstacleTimer >= spawnInterval && obstacleQueue.isEmpty()) {
            obstacleTimer = 0f;

            int[] lanes = {0, 1, 2};
            shuffleArray(lanes);
            int count = 2 + (int)(Math.random() * 2); // 2~3개

            for (int i = 0; i < count; i++) {
                obstacleQueue.add(lanes[i]);
            }
            spawnDelayTimer = 0f; // 다음 spawn 시작
        }

// 예약된 장애물 생성
        if (!obstacleQueue.isEmpty()) {
            spawnDelayTimer += GameView.frameTime;
            if (spawnDelayTimer >= spawnDelay) {
                spawnDelayTimer = 0f;

                int lane = obstacleQueue.poll();
                float x = player.getLaneX(lane);
                float yOffset = (float)(Math.random() * 100f);

                Obstacle obs = new Obstacle(R.mipmap.obstacle_box, x, yOffset);
                add(Layer.obstacle, obs);
            }
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
                    isPlayerDead = true;  // 플레이어가 죽었음을 표시
                    new GameOverScene(score, distance, isMale).change();
                }
                break;
            }
        }
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.controller.ordinal();
    }

    private boolean gestureUsed = false;
    private float swipeThreshold = 50f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPlayerDead) {
            // 게임 오버 상태에서는 모든 터치 이벤트를 무시하고 초기화
            gestureUsed = false;
            return false;
        }

        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        float x = pts[0], y = pts[1];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPlayerDead) return false;
                startX = x;
                startY = y;
                gestureUsed = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isPlayerDead || gestureUsed) return false;

                float dx = x - startX;
                float dy = y - startY;

                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > swipeThreshold) {
                    if (dx > 0) player.moveRight(); else player.moveLeft();
                    gestureUsed = true;
                } else if (Math.abs(dy) > swipeThreshold) {
                    if (dy > 0) player.slide(); else player.jump();
                    gestureUsed = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                gestureUsed = false;
                break;
        }

        if (player.isDead()) {
            isPlayerDead = true;
            gestureUsed = false;  // 제스처 상태 초기화
            new GameOverScene(score, distance, isMale).change();
        }
        return true;
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