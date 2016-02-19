package movile.com.creditcardguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import movile.com.creditcardguide.R;


public class FontFitTextView extends TextView
{

    private Paint mTestPaint;
    private float maxFontSize;
    private static final float MAX_FONT_SIZE_DEFAULT_VALUE = 20f;

    public FontFitTextView(Context context)
    {
        super(context);
        initialise(context, null);
    }

    public FontFitTextView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        initialise(context, attributeSet);
    }

    public FontFitTextView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        initialise(context, attributeSet);
    }

    private void initialise(Context context, AttributeSet attributeSet)
    {
        if(attributeSet!=null)
        {
            TypedArray styledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FontFitTextView);
            maxFontSize = styledAttributes.getDimension(R.styleable.FontFitTextView_maxFontSize, MAX_FONT_SIZE_DEFAULT_VALUE);
            styledAttributes.recycle();
        }
        else
        {
            maxFontSize = MAX_FONT_SIZE_DEFAULT_VALUE;
        }

        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHeight)
    {
        if (textWidth <= 0)
            return;
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        int targetHeight = textHeight - this.getPaddingTop() - this.getPaddingBottom();
        float hi = maxFontSize;
        float lo = 2;
//      final float threshold = 0.5f; // How close we have to be
        final float threshold = 1f; // How close we have to be

        mTestPaint.set(this.getPaint());

        Rect bounds = new Rect();

        while ((hi - lo) > threshold)
        {
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);

            mTestPaint.getTextBounds(text, 0, text.length(), bounds);

            if (bounds.width() >= targetWidth || bounds.height() >= targetHeight)
                hi = size; // too big
            else
                lo = size; // too small

//          if (mTestPaint.measureText(text) >= targetWidth)
//              hi = size; // too big
//          else
//              lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth, height);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after)
    {
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (w != oldw)
        {
            refitText(this.getText().toString(), w, h);
        }
    }
}