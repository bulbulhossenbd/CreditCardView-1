package movile.com.creditcardguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import movile.com.creditcardguide.R;
import movile.com.creditcardguide.anim.AnimateImageTransitionFade;
import movile.com.creditcardguide.anim.FlipAnimation;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.util.FontUtils;

/**
 * Created by FelipeSilvestre on 16/02/16.
 */
public class CreditCardView extends FrameLayout {

    private FrameLayout frameLayoutFrontCard;
    private FrameLayout frameLayoutBackCard;
    private FrameLayout rootCreditCard;

    private TextView textLabelOwner;
    private FontFitTextView textOwner;
    private TextView textLabelExpDate;
    private TextView textExpDate;
    private TextView textNumber;
    private FontFitTextView textCVV;

    private ImageView cardFront;
    private ImageView cardBack;
    private ImageView cardSign;

    private String labelCardOwner;
    private String labelCardDateExp;


    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardView);

        if (typedArray != null) {
            labelCardOwner = typedArray.getString(R.styleable.CreditCardView_labelOwner);
            labelCardDateExp = typedArray.getString(R.styleable.CreditCardView_labelDateExp);
        }

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.credit_card_view, this, true);
        FontUtils.loadFonts(this);

        rootCreditCard = (FrameLayout) findViewById(R.id.container_card);
        frameLayoutFrontCard = (FrameLayout) findViewById(R.id.frg_input_card_front);
        frameLayoutBackCard = (FrameLayout) findViewById(R.id.frg_input_card_back);

        textNumber = (TextView) findViewById(R.id.txt_number_credit_card);
        textExpDate = (TextView) findViewById(R.id.txt_expire_credit_card);
        textOwner = (FontFitTextView) findViewById(R.id.txt_name_credit_card);
        cardFront = (ImageView) findViewById(R.id.view_front_card_image);
        cardBack = (ImageView) findViewById(R.id.view_back_card_image);
        cardSign = (ImageView) findViewById(R.id.view_back_card_image_sign);
        textLabelExpDate = (TextView) findViewById(R.id.txt_expire_credit_card_label);
        textLabelOwner = (TextView) findViewById(R.id.txt_name_credit_card_label);
        textCVV = (FontFitTextView) findViewById(R.id.back_credit_card_txt_cvv);

        if (!TextUtils.isEmpty(labelCardOwner)) {
            setTextLabelOwner(labelCardOwner);
        }

        if (!TextUtils.isEmpty(labelCardDateExp)) {
            setTextLabelExpDate(labelCardDateExp);
        }
    }

    public void setTextCVV(CharSequence textCVV) {
        this.textCVV.setText(textCVV, TextView.BufferType.NORMAL);
    }

    public void setTextNumber(CharSequence textNumber) {
        this.textNumber.setText(textNumber);
    }

    public void setTextExpDate(CharSequence textExpDate) {
        this.textExpDate.setText(textExpDate);
    }

    public void setTextLabelExpDate(CharSequence textLabelExpDate) {
        this.textLabelExpDate.setText(textLabelExpDate.toString().toUpperCase());
    }

    public void setTextOwner(CharSequence textOwner) {
        this.textOwner.setText(textOwner.toString().toUpperCase(), TextView.BufferType.NORMAL);
    }

    public void setTextLabelOwner(CharSequence textLabelOwner) {
        this.textLabelOwner.setText(textLabelOwner.toString().toUpperCase());
    }

    public void flipToBack() {
        FlipAnimation flipAnimation = new FlipAnimation(frameLayoutFrontCard, frameLayoutBackCard);
        rootCreditCard.startAnimation(flipAnimation);
    }

    public void flipToFront() {
        FlipAnimation flipAnimation = new FlipAnimation(frameLayoutBackCard, frameLayoutFrontCard);
        rootCreditCard.startAnimation(flipAnimation);
    }

    public boolean isShowingFront() {
        return frameLayoutFrontCard.getVisibility() == VISIBLE;
    }

    public void clear() {
        textCVV.setText("");
        textExpDate.setText(getContext().getString(R.string.hint_credit_expire_date));
        textNumber.setText("");
        textOwner.setText("");
    }

    public void chooseFlag(IssuerCode issuerCode) {
        if (issuerCode == IssuerCode.AMEX) {
            textCVV.setText("0000", TextView.BufferType.NORMAL);
        } else {
            textCVV.setText("000", TextView.BufferType.NORMAL);
        }

        AnimateImageTransitionFade.imageViewAnimatedChange(getContext(), cardFront,
                issuerCode.getImageCardFront());

        cardBack.setImageResource(issuerCode.getImageCardBack());
        cardSign.setImageResource(issuerCode.getImageSignCard());

        textLabelExpDate.setTextColor(getResources().getColor(issuerCode.getColorText()));
        textExpDate.setTextColor(getResources().getColor(issuerCode.getColorText()));
        textLabelOwner.setTextColor(getResources().getColor(issuerCode.getColorText()));
        textOwner.setTextColor(getResources().getColor(issuerCode.getColorText()));
        textNumber.setTextColor(getResources().getColor(issuerCode.getColorText()));

        textLabelExpDate.setShadowLayer(1, 1, 1, getResources().getColor(issuerCode.getColorShadowText()));
        textExpDate.setShadowLayer(1, 1, 1, getResources().getColor(issuerCode.getColorShadowText()));
        textLabelOwner.setShadowLayer(1, 1, 1, getResources().getColor(issuerCode.getColorShadowText()));
        textOwner.setShadowLayer(1, 1, 1, getResources().getColor(issuerCode.getColorShadowText()));
        textNumber.setShadowLayer(1, 1, 1, getResources().getColor(issuerCode.getColorShadowText()));
    }
}
