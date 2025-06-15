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
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.VertScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.Sound;
import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;

public class MainScene extends Scene {
    // private final Button pauseBtn;

    public enum Layer {
        bg, obstacle, player, ui, controller, touch, COUNT
    }


    private Player player;
    private float playerX = Metrics.width / 2;
    private float playerY = GameConfig.Game.PLAYER_Y_POSITION;

    private int score = 0;
    private float distance = 0f;
    private boolean isMale;
    private boolean isPlayerDead = false;  // 플레이어 생존 상태 추적
    private float scoreUpdateTimer = 0f;  // 점수 업데이트 타이머 추가
    private static final float SCORE_UPDATE_INTERVAL = 0.1f;  // 0.1초마다 점수 업데이트

    private float startX, startY;
    private static final float SWIPE_THRESHOLD = GameConfig.Input.SWIPE_THRESHOLD;

    private int lastLane = -1;

    private static final String TAG = MainScene.class.getSimpleName();

    // 장애물 전용
    private float spawnInterval = GameConfig.Obstacle.SPAWN_INTERVAL;
    private float obstacleTimer = 0f;

    private float spawnDelay = GameConfig.Obstacle.SPAWN_DELAY;
    private float spawnDelayTimer = 0f;

    private Queue<Integer> obstacleQueue = new LinkedList<>(); // 생성 예약된 레인 인덱스들

    private ObstacleSpawner obstacleSpawner;

    public MainScene(boolean isMale) {
        this.isMale = isMale;

        initLayers(Layer.COUNT.ordinal());

        // 배경 추가
        add(Layer.bg, new VertScrollBackground(R.mipmap.bg_city, 300));

        // 캐릭터 생성
        int resId = isMale ? R.mipmap.to_back_man : R.mipmap.to_back_female;
        player = isMale ? new MalePlayer(resId, 10) : new FemalePlayer(resId, 10);
        player.setPosition(playerX, playerY, 200, 200);
        add(Layer.player, player);

        // 장애물 생성기 초기화
        obstacleSpawner = new ObstacleSpawner(this, player, Layer.obstacle);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // 배경음악 재생
        Sound.playMusic(R.raw.background);
        obstacleSpawner.reset();
    }

    @Override
    public void onExit() {
        super.onExit();
        // 배경음악 정지
        Sound.stopMusic();
    }

    @Override
    public void update() {
        super.update();

        // 점수 업데이트 로직 수정
        scoreUpdateTimer += GameView.frameTime;
        if (scoreUpdateTimer >= SCORE_UPDATE_INTERVAL) {
            score += (int)(GameConfig.Game.SCORE_PER_SECOND * SCORE_UPDATE_INTERVAL);
            scoreUpdateTimer = 0f;
        }
        
        distance += GameView.frameTime * GameConfig.Game.DISTANCE_PER_SECOND;

        // 장애물 생성 업데이트
        obstacleSpawner.update();

        // 충돌 감지
        ArrayList<IGameObject> obstacles = objectsAt(Layer.obstacle);
        for (IGameObject obj : obstacles) {
            if (!(obj instanceof IBoxCollidable)) continue;
            if (CollisionHelper.collides(player, (IBoxCollidable) obj)) {
                Obstacle obstacle = (Obstacle) obj;
                if (obstacle.isWall() || !player.isJumping() || player.isJumpEnded()) {
                    Log.d(TAG, "\uD83D\uDCA5 충돌 발생!");
                    player.decreaseLife();
                    remove(Layer.obstacle, obj);

                    if (player.isDead()) {
                        isPlayerDead = true;
                        new GameOverScene(score, distance, isMale).change();
                    }
                    break;
                }
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
    public boolean onBackPressed() {
        Log.d(TAG, "뒤로가기 버튼 눌림");
        new PauseScene().push();
        return true;
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
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
        paint.setTextSize(GameConfig.UI.SCORE_TEXT_SIZE);
        paint.setTextAlign(Paint.Align.LEFT);

        // 체력 (왼쪽 상단)
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < player.getLife(); i++) {
            hearts.append("♥ ");
        }
        canvas.drawText(hearts.toString(), GameConfig.UI.HEART_PADDING, 75, paint);

        // 점수 (오른쪽 상단)
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.YELLOW);
        paint.setShadowLayer(GameConfig.UI.SCORE_SHADOW_RADIUS, 0, 0, Color.BLACK);
        canvas.drawText("SCORE", Metrics.width - GameConfig.UI.SCORE_PADDING, 60, paint);
        canvas.drawText(String.format("%,d", score), Metrics.width - GameConfig.UI.SCORE_PADDING, 120, paint);
        paint.clearShadowLayer();
    }

    public boolean isMale() {
        return isMale;
    }
}