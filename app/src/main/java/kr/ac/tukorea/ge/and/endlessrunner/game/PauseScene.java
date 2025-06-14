package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.app.AlertDialog;
import android.content.DialogInterface;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class PauseScene extends Scene {
    public enum Layer {
        bg, title, touch
    }

    // 버튼 크기 상수 정의
    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 120f;
    private static final float BUTTON_SPACING = 150f;  // 버튼 사이 간격

    public PauseScene() {
        initLayers(Layer.values().length);
        float w = Metrics.width, h = Metrics.height;
        
        // 반투명 배경
        add(Layer.bg, new Sprite(R.mipmap.trans_50b, w/2, h/2, w, h));
        
        // 일시정지 타이틀
        add(Layer.title, new Sprite(R.mipmap.endless_runner_logo, w/2, h/3, 800f, 550f));
        
        // 재개 버튼
        Button resumeBtn = new Button(R.mipmap.resume_btn, w/2, h/2, BUTTON_WIDTH, BUTTON_HEIGHT, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if (pressed) {
                    pop();
                }
                return false;
            }
        });
        resumeBtn.setCustomSize(true);
        add(Layer.touch, resumeBtn);
        
        // 종료 버튼
        Button exitBtn = new Button(R.mipmap.exit_btn, w/2, h/2 + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if (pressed) {
                    new AlertDialog.Builder(GameView.view.getContext())
                            .setTitle("게임 종료")
                            .setMessage("게임을 종료하시겠습니까?")
                            .setNegativeButton("아니오", null)
                            .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    popAll();
                                }
                            })
                            .create()
                            .show();
                }
                return false;
            }
        });
        exitBtn.setCustomSize(true);
        add(Layer.touch, exitBtn);
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
