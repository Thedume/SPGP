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
import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;

public abstract class Player extends SheetSprite implements IBoxCollidable {
    protected enum State { RUN, JUMP, SLIDE }
    protected State state = State.RUN;

    protected int life = GameConfig.Player.INITIAL_LIFE;
    protected final float[] laneX = new float[3];
    protected int currentLane = 1;

    protected float moveSpeed = GameConfig.Player.MOVE_SPEED;
    protected Float targetX = null;
    protected Float targetY = null;
    protected boolean returningY = false;
    protected float originalY;
    protected float moveYSpeed = GameConfig.Player.MOVE_Y_SPEED;
    protected static final float OFFSET = GameConfig.Player.OFFSET;
    protected static final float EPSILON = GameConfig.Player.EPSILON;

    protected boolean isInvincible = false;
    protected static final float INVINCIBLE_DURATION = GameConfig.Player.INVINCIBLE_DURATION;
    protected float invincibleTimer = 0f;
    
    // 캐릭터별 스탯 적용
    protected void applyCharacterStats(boolean isMale) {
        if (isMale) {
            // 남성 캐릭터 스탯 적용
            moveSpeed *= GameConfig.Player.Male.MOVE_SPEED_MULTIPLIER;
            invincibleTimer *= GameConfig.Player.Male.INVINCIBLE_DURATION_MULTIPLIER;
        } else {
            // 여성 캐릭터 스탯 적용
            life += GameConfig.Player.Female.EXTRA_LIFE;
        }
    }

    public Player(int resId, float fps) {
        super(resId, fps);
        this.state = State.RUN;
        this.targetY = null;
        this.returningY = false;
        
        initSpriteSettings();
        initPosition();
    }

    protected abstract void initSpriteSettings();
    protected abstract int[] getFrameSetForState(State state);

    protected void initPosition() {
        float centerY = GameConfig.Game.PLAYER_Y_POSITION;
        this.originalY = centerY;

        float laneOffset = GameConfig.Game.LANE_OFFSET;
        laneX[0] = Metrics.width / 2 - laneOffset;
        laneX[1] = Metrics.width / 2;
        laneX[2] = Metrics.width / 2 + laneOffset;

        this.originalY = centerY;
        setPosition(laneX[currentLane], originalY, getSpriteWidth(), getSpriteHeight());
        this.y = originalY;
    }

    protected abstract float getSpriteWidth();
    protected abstract float getSpriteHeight();

    public void jump() {
        if (targetY != null) return;
        state = State.JUMP;
        targetY = originalY - OFFSET;
        returningY = false;
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
        if (isInvincible && (int)(System.currentTimeMillis() / 100) % 2 == 0) {
            return;
        }
        
        long now = System.currentTimeMillis();
        float t = (now - createdOn) / 1000f;

        int[] frameSet = getFrameSetForState(state);
        if (srcRects == null || frameSet == null || frameSet.length == 0) return;

        int index = Math.round(t * fps) % frameSet.length;
        canvas.drawBitmap(bitmap, srcRects[frameSet[index]], dstRect, null);
    }

    @Override
    public void update() {
        super.update();

        if (isInvincible) {
            invincibleTimer -= GameView.frameTime;
            if (invincibleTimer <= 0) {
                isInvincible = false;
            }
        }

        updateXPosition();
        updateYPosition();
    }

    protected void updateXPosition() {
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
    }

    protected void updateYPosition() {
        if (targetY != null) {
            float dy = targetY - y;
            float dir = Math.signum(dy);
            float move = moveYSpeed * GameView.frameTime;

            if (Math.abs(dy) <= move) {
                y += dy;
                dstRect.offset(0, dy);

                if (returningY) {
                    targetY = null;
                    returningY = false;
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        targetY = originalY;
                        returningY = true;
                    }, (long)GameConfig.Player.JUMP_DELAY);
                }
            } else {
                float step = dir * move;
                y += step;
                dstRect.offset(0, step);
            }
        }
    }

    public float getLaneX(int lane) {
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
        return state == State.JUMP && targetY == null;
    }

    public boolean isSliding() {
        return state == State.SLIDE;
    }

    public boolean isSlideEnded() {
        return state == State.SLIDE && targetY == null;
    }
}
