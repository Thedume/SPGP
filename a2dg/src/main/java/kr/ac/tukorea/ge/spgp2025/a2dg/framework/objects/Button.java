package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects;

import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ITouchable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Button extends Sprite implements ITouchable {
    public interface OnTouchListener {
        public boolean onTouch(boolean pressed);
    }
    protected OnTouchListener listener;
    private static final String TAG = Button.class.getSimpleName();
    private boolean useCustomSize = false;

    public Button(int bitmapResId, float cx, float cy, float width, float height, OnTouchListener listener) {
        super(bitmapResId, cx, cy, width, height);
        this.listener = listener;
    }

    public Button(int bitmapResId, float cx, float cy, OnTouchListener listener) {
        super(bitmapResId, cx, cy, 512f, 512f);
        this.listener = listener;
    }

    public void setCustomSize(boolean useCustomSize) {
        this.useCustomSize = useCustomSize;
    }

    protected boolean captures;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        //Log.d(TAG, "onTouch:" + this + " action=" + action);
        if (action == MotionEvent.ACTION_DOWN) {
            float[] pts = Metrics.fromScreen(e.getX(), e.getY());
            float x = pts[0], y = pts[1];
            if (!dstRect.contains(x, y)) {
                return false;
            }
            captures = true;
            return listener.onTouch(true);
        } else if (action == MotionEvent.ACTION_UP) {
            captures = false;
            return listener.onTouch(false);
        }
        return captures;
    }

    @Override
    public void draw(android.graphics.Canvas canvas) {
        float drawWidth = useCustomSize ? width : 512f;
        float drawHeight = useCustomSize ? height : 512f;

        // 이미지 그리기 영역: 실제 버튼 크기 사용
        float left = x - drawWidth / 2;
        float top = y - drawHeight / 2;
        float right = x + drawWidth / 2;
        float bottom = y + drawHeight / 2;

        android.graphics.RectF imageRect = new android.graphics.RectF(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, imageRect, null);
    }
}
