package kr.ac.tukorea.ge.and.endlessrunner.game;

import java.util.LinkedList;
import java.util.Queue;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class ObstacleSpawner {
    private final Scene scene;
    private final Player player;
    private final MainScene.Layer obstacleLayer;

    private float spawnInterval = GameConfig.Obstacle.SPAWN_INTERVAL;
    private float obstacleTimer = 0f;
    private float spawnDelay = GameConfig.Obstacle.SPAWN_DELAY;
    private float spawnDelayTimer = 0f;
    private Queue<Integer> obstacleQueue = new LinkedList<>();

    public ObstacleSpawner(Scene scene, Player player, MainScene.Layer obstacleLayer) {
        this.scene = scene;
        this.player = player;
        this.obstacleLayer = obstacleLayer;
    }

    public void update() {
        // 장애물 묶음 생성 예약
        obstacleTimer += GameView.frameTime;
        if (obstacleTimer >= spawnInterval && obstacleQueue.isEmpty()) {
            obstacleTimer = 0f;
            scheduleObstacleGroup();
            spawnDelayTimer = 0f;
        }

        // 예약된 장애물 생성
        if (!obstacleQueue.isEmpty()) {
            spawnDelayTimer += GameView.frameTime;
            if (spawnDelayTimer >= spawnDelay) {
                spawnDelayTimer = 0f;
                spawnNextObstacle();
            }
        }
    }

    private void scheduleObstacleGroup() {
        int[] lanes = {0, 1, 2};
        shuffleArray(lanes);
        int count = GameConfig.Obstacle.MIN_OBSTACLES_PER_GROUP + 
                   (int)(Math.random() * (GameConfig.Obstacle.MAX_OBSTACLES_PER_GROUP - 
                                        GameConfig.Obstacle.MIN_OBSTACLES_PER_GROUP + 1));

        for (int i = 0; i < count; i++) {
            obstacleQueue.add(lanes[i]);
        }
    }

    private void spawnNextObstacle() {
        int lane = obstacleQueue.poll();
        float x = player.getLaneX(lane);
        float yOffset = (float)(Math.random() * 100f);

        Obstacle.Type type = Math.random() < GameConfig.Obstacle.WALL_SPAWN_CHANCE ? 
                           Obstacle.Type.WALL : Obstacle.Type.NORMAL;
        int resId = (type == Obstacle.Type.WALL) ? R.mipmap.obstacle_wall : R.mipmap.obstacle_box;
        Obstacle obs = new Obstacle(resId, x, yOffset, type);
        scene.add(obstacleLayer, obs);
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public void setSpawnInterval(float interval) {
        this.spawnInterval = interval;
    }

    public void setSpawnDelay(float delay) {
        this.spawnDelay = delay;
    }

    public void reset() {
        obstacleTimer = 0f;
        spawnDelayTimer = 0f;
        obstacleQueue.clear();
    }
} 