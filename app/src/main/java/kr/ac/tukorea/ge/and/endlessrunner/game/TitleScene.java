package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.and.endlessrunner.game.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TitleScene extends Scene {

    private int characterIndex = 0; // 0 = 남자, 1 = 여자

    public TitleScene() {
        initLayers(Layer.COUNT.ordinal());

        float iconY = Metrics.height * 0.3f;
        float buttonY = Metrics.height * 0.5f;
        float iconSize = 200f;
        float buttonWidth = 150f;
        float buttonHeight = 75f;
        float maleX = Metrics.width * 0.3f;
        float femaleX = Metrics.width * 0.7f;

        // 캐릭터 이미지
        add(Layer.ui, new Sprite(R.mipmap.char_male_icon, maleX, iconY, iconSize, iconSize));
        add(Layer.ui, new Sprite(R.mipmap.char_female_icon, femaleX, iconY, iconSize, iconSize));

        // 남자 선택 버튼
        add(Layer.ui, new Button(R.mipmap.default_btn, maleX, buttonY, buttonWidth, buttonHeight, pressed -> {
            characterIndex = 0;
            return true;
        }) {
            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                Paint paint = new Paint();
                paint.setTextSize(40f);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(characterIndex == 0 ? "SELECTED" : "SELECT", x, y + 15f, paint);
            }
        });

        // 여자 선택 버튼
        add(Layer.ui, new Button(R.mipmap.default_btn, femaleX, buttonY, buttonWidth, buttonHeight, pressed -> {
            characterIndex = 1;
            return true;
        }) {
            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                Paint paint = new Paint();
                paint.setTextSize(40f);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(characterIndex == 1 ? "SELECTED" : "SELECT", x, y + 15f, paint);
            }
        });

        // 게임 시작 버튼
        add(Layer.ui, new Button(R.mipmap.game_play_btn, Metrics.width / 2, Metrics.height * 0.75f, 300, 120, pressed -> {
            new MainScene(characterIndex == 0).change();
            return true;
        }));
    }

    public enum Layer {
        ui, COUNT
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.ui.ordinal();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(70f);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        // 제목
        canvas.drawText("ENDLESS RUNNER", Metrics.width / 2, 150, paint);
    }
}

