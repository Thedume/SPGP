package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.Rect;

import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;

public class MalePlayer extends Player {
    public MalePlayer(int resId, float fps) {
        super(resId, fps);
        applyCharacterStats(true);  // 남성 캐릭터 스탯 적용
    }

    @Override
    protected void initSpriteSettings() {
        this.srcRects = new Rect[12];  // 2행 6열 (6등분)
        for (int i = 0; i < 12; i++) {
            int col = i % 6;  // 6등분
            int row = i / 6;
            this.srcRects[i] = new Rect(
                    col * GameConfig.Player.Male.FRAME_WIDTH,
                    row * GameConfig.Player.Male.FRAME_HEIGHT,
                    (col + 1) * GameConfig.Player.Male.FRAME_WIDTH,
                    (row + 1) * GameConfig.Player.Male.FRAME_HEIGHT
            );
        }
    }

    @Override
    protected int[] getFrameSetForState(State state) {
        return new int[]{0, 1, 2, 3, 4, 5};  // 모든 상태에서 동일한 프레임셋 사용
    }

    @Override
    protected float getSpriteWidth() {
        return GameConfig.Player.Male.SPRITE_WIDTH;
    }

    @Override
    protected float getSpriteHeight() {
        return GameConfig.Player.Male.SPRITE_HEIGHT;
    }
} 