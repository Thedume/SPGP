package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;

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
    private Sprite backgroundSprite;

    public TitleScene() {
        initLayers(Layer.COUNT.ordinal());

        // 배경 스프라이트 추가
        backgroundSprite = new Sprite(R.mipmap.title_background, Metrics.width / 2, Metrics.height / 2, Metrics.width, Metrics.height);
        add(Layer.ui, backgroundSprite);

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
        maleButton = new Button(characterIndex == 0 ? R.mipmap.selected : R.mipmap.select, maleX, buttonY, buttonWidth, buttonHeight, pressed -> {
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
        femaleButton = new Button(characterIndex == 1 ? R.mipmap.selected : R.mipmap.select, femaleX, buttonY, buttonWidth, buttonHeight, pressed -> {
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
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        SharedPreferences prefs = GameView.view.getContext().getSharedPreferences("score", Context.MODE_PRIVATE);
        String scoresStr = prefs.getString("records", "");
        if (!scoresStr.isEmpty()) {
            // 점수 표시 배경
            Paint bgPaint = new Paint();
            bgPaint.setColor(Color.argb(180, 0, 0, 0));
            float padding = 20f;
            float startY = Metrics.height * 0.2f;  // 시작 위치를 위로 조정
            float endY = startY + 450f;  // 5순위까지 표시할 수 있도록 높이 증가
            canvas.drawRect(Metrics.width * 0.2f, startY, Metrics.width * 0.8f, endY, bgPaint);

            // "HIGH SCORES" 텍스트
            paint.setTextSize(70f);
            canvas.drawText("HIGH SCORES", Metrics.width / 2, startY + 55f, paint);

            // 점수 목록
            paint.setTextSize(55f);
            String[] scores = scoresStr.split(",");
            for (int i = 0; i < scores.length; i++) {
                String rank = (i + 1) + "위";
                String score = scores[i] + "점";
                float y = startY + 120f + i * 70f;  // 간격을 좀 더 조밀하게 조정
                
                // 순위와 점수 사이에 점선 그리기
                Paint linePaint = new Paint();
                linePaint.setColor(Color.WHITE);
                linePaint.setStyle(Paint.Style.STROKE);
                linePaint.setStrokeWidth(2f);
                float lineY = y + 20f;
                canvas.drawLine(Metrics.width * 0.3f, lineY, Metrics.width * 0.7f, lineY, linePaint);
                
                // 순위와 점수 텍스트 그리기
                canvas.drawText(rank, Metrics.width * 0.35f, y, paint);
                canvas.drawText(score, Metrics.width * 0.65f, y, paint);
            }
        }
    }
}