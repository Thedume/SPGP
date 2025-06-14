package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.Rect;

import kr.ac.tukorea.ge.and.endlessrunner.config.GameConfig;

public class FemalePlayer extends Player {
    public FemalePlayer(int resId, float fps) {
        super(resId, fps);
    }

    @Override
    protected void initSpriteSettings() {
        this.srcRects = new Rect[16];  // 4행 4열 (4등분)
        for (int i = 0; i < 16; i++) {
            int col = i % 4;
            int row = i / 4;
            this.srcRects[i] = new Rect(
                    col * GameConfig.Player.Female.FRAME_WIDTH,
                    row * GameConfig.Player.Female.FRAME_HEIGHT,
                    (col + 1) * GameConfig.Player.Female.FRAME_WIDTH,
                    (row + 1) * GameConfig.Player.Female.FRAME_HEIGHT
            );
        }
    }

    @Override
    protected int[] getFrameSetForState(State state) {
        return new int[]{0, 1, 2, 3};  // 모든 상태에서 동일한 프레임셋 사용
    }

    @Override
    protected float getSpriteWidth() {
        return GameConfig.Player.Female.SPRITE_WIDTH;
    }

    @Override
    protected float getSpriteHeight() {
        return GameConfig.Player.Female.SPRITE_HEIGHT;
    }
} 