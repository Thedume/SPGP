package kr.ac.tukorea.ge.and.endlessrunner.config;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class GameConfig {
    // 플레이어 관련 설정
    public static class Player {
        public static final float MOVE_SPEED = 1200f;
        public static final float MOVE_Y_SPEED = 800f;
        public static final float OFFSET = 250f;
        public static final float EPSILON = 1.0f;
        public static final float INVINCIBLE_DURATION = 1.5f;
        public static final int INITIAL_LIFE = 3;
        public static final float JUMP_DELAY = 200f;  // 점프 후 복귀 지연 시간 (ms)
        
        // 남성 캐릭터 설정
        public static class Male {
            public static final int FRAME_WIDTH = 400 / 6;
            public static final int FRAME_HEIGHT = 100;
            public static final int SPRITE_WIDTH = 200;
            public static final int SPRITE_HEIGHT = 50;
            
            // 남성 캐릭터 특수 스탯
            public static final float MOVE_SPEED_MULTIPLIER = 1.1f;  // 이동 속도 10% 증가
            public static final float INVINCIBLE_DURATION_MULTIPLIER = 1.3f;  // 무적 시간 30% 증가
        }
        
        // 여성 캐릭터 설정
        public static class Female {
            public static final int FRAME_WIDTH = 256 / 4;
            public static final int FRAME_HEIGHT = 104;
            public static final int SPRITE_WIDTH = 180;
            public static final int SPRITE_HEIGHT = 73;
            
            // 여성 캐릭터 특수 스탯
            public static final float COLLISION_SIZE_MULTIPLIER = 0.8f;  // 충돌 판정 20% 감소
            public static final int EXTRA_LIFE = 1;  // 추가 생명력 1개
        }
    }
    
    // 게임 진행 관련 설정
    public static class Game {
        public static final float SCORE_PER_SECOND = 100f;
        public static final float DISTANCE_PER_SECOND = 300f;
        public static final float PLAYER_Y_POSITION = Metrics.height * 0.8f;
        public static final float LANE_OFFSET = Metrics.width / 4f;
        
        // 거리 가중치 설정
        public static final float DISTANCE_MULTIPLIER = 0.5f;  // 1m당 0.5점 추가
        public static final float DISTANCE_BONUS_THRESHOLD = 1000f;  // 1000m마다 보너스
        public static final float DISTANCE_BONUS_MULTIPLIER = 2.0f;  // 보너스 구간에서의 점수 배율
    }
    
    // 장애물 관련 설정
    public static class Obstacle {
        public static final float SPAWN_INTERVAL = 2.5f;
        public static final float SPAWN_DELAY = 0.5f;
        public static final float WALL_SPAWN_CHANCE = 0.2f;
        public static final float LOW_WALL_SPAWN_CHANCE = 0.3f;
        public static final int MIN_OBSTACLES_PER_GROUP = 2;
        public static final int MAX_OBSTACLES_PER_GROUP = 3;
    }
    
    // UI 관련 설정
    public static class UI {
        public static final float SCORE_TEXT_SIZE = 70f;
        public static final float SCORE_SHADOW_RADIUS = 5f;
        public static final float SCORE_PADDING = 30f;
        public static final float HEART_PADDING = 30f;
    }
    
    // 입력 관련 설정
    public static class Input {
        public static final float SWIPE_THRESHOLD = 50f;
    }
} 