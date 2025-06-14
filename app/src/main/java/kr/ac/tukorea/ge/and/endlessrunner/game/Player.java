package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.SheetSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.Sound;

public class Player extends SheetSprite implements IBoxCollidable {
    // 기존 상태/애니메이션 프레임 유지
    private enum State { RUN, JUMP, SLIDE }
    private State state = State.RUN;

    private int life = 3;
    private boolean isMale = true;

    private final float[] laneX = new float[3];
    private int currentLane = 1;

    private float moveSpeed = 1200f; // 초당 픽셀 속도
    private Float targetX = null; // 이동 목표 x좌표
    private Float targetY = null;
    private boolean returningY = false; // 복귀 중 여부
    private float originalY;
    private float moveYSpeed = 800f; // Y축 이동 속도
    private static final float OFFSET = 250f;
    private static final float EPSILON = 1.0f;

    private boolean isInvincible = false;
    private static final float INVINCIBLE_DURATION = 1.5f;
    private float invincibleTimer = 0f;

    public Player(int resId, boolean isMale, float fps) {
        super(resId, fps);
        this.state = State.RUN;
        this.isMale = isMale;
        this.targetY = null;
        this.returningY = false;
        
        // 남녀 캐릭터에 따른 스프라이트 설정
        int frameWidth, frameHeight;
        if (isMale) {
            frameWidth = 400 / 6;  // 6등분
            frameHeight = 100;
        } else {
            frameWidth = 256 / 4;  // 4등분
            frameHeight = 104;
        }

        // 반드시 srcRects 초기화
        if (isMale) {
            this.srcRects = new Rect[12];  // 2행 6열 (6등분)
            for (int i = 0; i < 12; i++) {
                int col = i % 6;  // 6등분
                int row = i / 6;
                this.srcRects[i] = new Rect(
                        col * frameWidth,
                        row * frameHeight,
                        (col + 1) * frameWidth,
                        (row + 1) * frameHeight
                );
            }
        } else {
            this.srcRects = new Rect[16];  // 4행 4열 (4등분)
            for (int i = 0; i < 16; i++) {
                int col = i % 4;
                int row = i / 4;
                this.srcRects[i] = new Rect(
                        col * frameWidth,
                        row * frameHeight,
                        (col + 1) * frameWidth,
                        (row + 1) * frameHeight
                );
            }
        }
        float centerY = Metrics.height * 0.8f;
        this.originalY = centerY;

        float laneOffset = Metrics.width / 4; // 중앙 기준 좌우 간격
        laneX[0] = Metrics.width / 2 - laneOffset;
        laneX[1] = Metrics.width / 2;
        laneX[2] = Metrics.width / 2 + laneOffset;

        this.originalY = centerY;
        
        // 남녀 캐릭터에 따른 크기 설정
        float spriteWidth, spriteHeight;
        if (isMale) {
            spriteWidth = 200;
            spriteHeight = 50;  // 높이를 프레임 비율에 맞게 조정
        } else {
            spriteWidth = 180;  // 여자 캐릭터 폭을 180으로 조정
            spriteHeight = 73;  // 104/256 * 180 = 73.125 (비율 유지)
        }
        
        setPosition(laneX[currentLane], originalY, spriteWidth, spriteHeight);
        this.y = originalY;
    }

    public void jump() {
        if (targetY != null) return; // 이미 이동 중이면 무시
        state = State.JUMP;
        targetY = originalY - OFFSET;
        returningY = false;
        // 점프 효과음 재생
        Sound.playEffect(R.raw.jump);
    }

    public void slide() {
        if (targetY != null) return;
        state = State.SLIDE;
        targetY = originalY + OFFSET;
        returningY = false;
    }

    public void moveLeft() {
        if (currentLane > 0) {
            currentLane--;
            targetX = laneX[currentLane];
        }
    }

    public void moveRight() {
        if (currentLane < 2) {
            currentLane++;
            targetX = laneX[currentLane];
        }
    }

    public void decreaseLife() {
        if (!isInvincible) {
            life--;
            isInvincible = true;
            invincibleTimer = INVINCIBLE_DURATION;
            // 데미지 효과음 재생
            Sound.playEffect(R.raw.damaged);
        }
    }

    public boolean isDead() {
        return life <= 0;
    }

    public int getLife() {
        return life;
    }

    @Override
    public void draw(Canvas canvas) {
        // 무적 상태일 때 깜빡이는 효과
        if (isInvincible && (int)(System.currentTimeMillis() / 100) % 2 == 0) {
            return;
        }
        
        // 기존 draw 유지
        long now = System.currentTimeMillis();
        float t = (now - createdOn) / 1000f;

        int[] frameSet;

        if (isMale) {
            switch (state) {
                case RUN:
                    frameSet = new int[]{0, 1, 2, 3, 4, 5};  // 첫 번째 행
                    break;
                case JUMP:
                    frameSet = new int[]{0, 1, 2, 3, 4, 5};  // 점프도 첫 번째 행 사용
                    break;
                case SLIDE:
                    frameSet = new int[]{0, 1, 2, 3, 4, 5};  // 슬라이드도 첫 번째 행 사용
                    break;
                default:
                    frameSet = new int[]{0, 1, 2, 3, 4, 5}; // fallback
                    break;
            }
        } else {
            switch (state) {
                case RUN:
                    frameSet = new int[]{0, 1, 2, 3};  // 4등분
                    break;
                case JUMP:
                    frameSet = new int[]{0, 1, 2, 3};  // 4등분
                    break;
                case SLIDE:
                    frameSet = new int[]{0, 1, 2, 3};  // 4등분
                    break;
                default:
                    frameSet = new int[]{0, 1, 2, 3}; // fallback
                    break;
            }
        }
        if (srcRects == null || frameSet == null || frameSet.length == 0) return;

        int index = Math.round(t * fps) % frameSet.length;
        canvas.drawBitmap(bitmap, srcRects[frameSet[index]], dstRect, null);
    }

    @Override
    public void update() {
        super.update();

        // 무적 상태 업데이트
        if (isInvincible) {
            invincibleTimer -= GameView.frameTime;
            if (invincibleTimer <= 0) {
                isInvincible = false;
            }
        }

        // X축 이동 (좌우 레인)
        if (targetX != null) {
            float dx = targetX - x;
            float dir = Math.signum(dx);
            float move = moveSpeed * GameView.frameTime;

            if (Math.abs(dx) <= move) {
                x += dx;
                dstRect.offset(dx, 0);
                targetX = null;
            } else {
                float step = dir * move;
                x += step;
                dstRect.offset(step, 0);
            }
        }

        // Y축 이동 (점프/슬라이드)
        if (targetY != null) {
            float dy = targetY - y;
            float dir = Math.signum(dy);
            float move = moveYSpeed * GameView.frameTime;

            if (Math.abs(dy) <= move) {
                y += dy;
                dstRect.offset(0, dy);

                if (returningY) {
                    targetY = null;      // 완전히 종료
                    returningY = false;
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        targetY = originalY;
                        returningY = true;
                    }, 200);
                }
            } else {
                float step = dir * move;
                y += step;
                dstRect.offset(0, step);
            }
        }
    }

    public float getLaneX(int lane) {
        if (lane < 0 || lane >= laneX.length) return laneX[1]; // 기본 중앙값
        return laneX[lane];
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    public boolean isJumping() {
        return state == State.JUMP;
    }

    public boolean isJumpEnded() {
        return state == State.JUMP && targetY == null && !returningY;
    }
}
