package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TitleScene extends Scene {
    private int characterIndex = 0; // 0 = 남자, 1 = 여자
    private Sprite titleSprite;
    private Button maleButton;
    private Button femaleButton;

    public TitleScene() {
        initLayers(Layer.COUNT.ordinal());

        // 타이틀 스프라이트 추가
        titleSprite = new Sprite(R.mipmap.endless_runner_logo, Metrics.width / 2, 150, 800, 550);
        add(Layer.ui, titleSprite);

        float iconY = Metrics.height * 0.6f;
        float buttonY = Metrics.height * 0.725f;
        float iconSize = 225f;
        float buttonWidth = 300f;
        float buttonHeight = 150f;
        float maleX = Metrics.width * 0.3f;
        float femaleX = Metrics.width * 0.7f;

        // 캐릭터 이미지
        add(Layer.ui, new Sprite(R.mipmap.char_male_icon, maleX, iconY, iconSize, iconSize));
        add(Layer.ui, new Sprite(R.mipmap.char_female_icon, femaleX, iconY, iconSize, iconSize));

        // 남자 선택 버튼
        maleButton = new Button(characterIndex == 0 ? R.mipmap.select : R.mipmap.selected, maleX, buttonY, buttonWidth, buttonHeight, pressed -> {
            if (pressed) {
                characterIndex = 0;
                maleButton.setImageResourceId(R.mipmap.selected);
                femaleButton.setImageResourceId(R.mipmap.select);
            }
            return true;
        });
        maleButton.setCustomSize(true);
        add(Layer.ui, maleButton);

        // 여자 선택 버튼
        femaleButton = new Button(characterIndex == 1 ? R.mipmap.select : R.mipmap.selected, femaleX, buttonY, buttonWidth, buttonHeight, pressed -> {
            if (pressed) {
                characterIndex = 1;
                maleButton.setImageResourceId(R.mipmap.select);
                femaleButton.setImageResourceId(R.mipmap.selected);
            }
            return true;
        });
        femaleButton.setCustomSize(true);
        add(Layer.ui, femaleButton);

        // 게임 시작 버튼
        add(Layer.ui, new Button(R.mipmap.game_play_btn, Metrics.width / 2, Metrics.height * 0.9f, pressed -> {
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
        paint.setTextSize(55f);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        SharedPreferences prefs = GameView.view.getContext().getSharedPreferences("score", Context.MODE_PRIVATE);
        String scoresStr = prefs.getString("records", "");
        if (!scoresStr.isEmpty()) {
            String[] scores = scoresStr.split(",");
            for (int i = 0; i < scores.length; i++) {
                canvas.drawText((i + 1) + "위: " + scores[i] + "점", Metrics.width / 2, Metrics.height * 0.25f + i * 80, paint);
            }
        }
    }
}