package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.Rect;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class FemalePlayer extends Player {
    private static final int FRAME_WIDTH = 256 / 4;  // 4등분
    private static final int FRAME_HEIGHT = 104;
    private static final int SPRITE_WIDTH = 180;
    private static final int SPRITE_HEIGHT = 73;  // 104/256 * 180 = 73.125 (비율 유지)

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
                    col * FRAME_WIDTH,
                    row * FRAME_HEIGHT,
                    (col + 1) * FRAME_WIDTH,
                    (row + 1) * FRAME_HEIGHT
            );
        }
    }

    @Override
    protected int[] getFrameSetForState(State state) {
        return new int[]{0, 1, 2, 3};  // 모든 상태에서 동일한 프레임셋 사용
    }

    @Override
    protected float getSpriteWidth() {
        return SPRITE_WIDTH;
    }

    @Override
    protected float getSpriteHeight() {
        return SPRITE_HEIGHT;
    }
} 