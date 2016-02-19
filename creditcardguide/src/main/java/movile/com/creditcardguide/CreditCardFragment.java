package movile.com.creditcardguide;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PurchaseOption;
import movile.com.creditcardguide.util.FontUtils;
import movile.com.creditcardguide.util.FormatUtils;
import movile.com.creditcardguide.view.CreditCardView;
import movile.com.creditcardguide.view.LockableViewPager;

public class CreditCardFragment extends Fragment implements TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener {

    private static final int AMEX_MAX_CVV = 4;
    private static final int COMMON_MAX_CVV = 3;

    public enum Step {
        FLAG, NUMBER, EXPIRE_DATE, CVV, NAME
    }

    private static final String PURCHASE_OPTION_SELECTED = "PURCHASE_OPTION_SELECTED";

    private View viewHolder;
    private List<Step> pages;
    private ListView listFlagCreditCard;
    private EditText editNumberCard;
    private EditText editExpireCard;
    private EditText editCVVCard;
    private EditText editNameCard;

    private CreditCardView creditCardView;

    private LockableViewPager pager;
    private LinearLayout layoutPayment;
    private FrameLayout layoutData;

    private ImageView btNext;
    private Button btEdit;
    private Spinner spInstallments;

    private Button btPay;
    private TextView txtValue;
    private Switch switchSaveCard;

    private Step lastStep = Step.FLAG;
    private PurchaseOption selectedPurchaseOption;
    private InstallmentsCardAdapter installmentsCardAdapter;
    private CreditCardValidator validator;
    private List<PurchaseOption> purchaseOptions;
    private Double valueTotal = new Double(0);
    private ActionOnPayListener actionOnPayListener;
    private AtomicBoolean backFinish = new AtomicBoolean(false);
    private int colorFieldWrong;
    private int colorField;

    private TextView textLabelNumber;
    private TextView textLabelExpireDate;
    private TextView textLabelCVV;
    private TextView textLabelOwnerName;
    private TextView textLabelTotal;

    private String labelCardOwner;
    private String labelCardDateExp;
    private String labelNumber;
    private String labelExpireDate;
    private String labelCVV;
    private String labelOwnerName;
    private String labelButtonPay;
    private String labelTotal;
    private Drawable payBackground;
    private ColorStateList btPayTextColor;
    private Boolean attrInstallments;
    private Boolean attrSaveCard;
    private CreditCardPaymentMethod savedCard;
    private boolean cardRestored;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ActionOnPayListener) {
            actionOnPayListener = (ActionOnPayListener) activity;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ActionOnPayListener) {
            actionOnPayListener = (ActionOnPayListener) context;
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        parseAttrs(activity, attrs);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardFragment);

        if (typedArray != null) {
            this.labelCardOwner = typedArray.getString(R.styleable.CreditCardFragment_labelCardOwner);
            this.labelCardDateExp = typedArray.getString(R.styleable.CreditCardFragment_labelCardDateExp);
            this.labelNumber = typedArray.getString(R.styleable.CreditCardFragment_labelNumber);

            this.labelExpireDate = typedArray.getString(R.styleable.CreditCardFragment_labelExpireDate);
            this.labelCVV = typedArray.getString(R.styleable.CreditCardFragment_labelCVV);
            this.labelOwnerName = typedArray.getString(R.styleable.CreditCardFragment_labelOwnerName);
            this.labelButtonPay = typedArray.getString(R.styleable.CreditCardFragment_labelButtonPay);
            this.labelTotal = typedArray.getString(R.styleable.CreditCardFragment_labelTotal);
            this.payBackground = typedArray.getDrawable(R.styleable.CreditCardFragment_buttonPayBackground);
            this.btPayTextColor = typedArray.getColorStateList(R.styleable.CreditCardFragment_buttonPayTextColor);

            this.attrInstallments = typedArray.getBoolean(R.styleable.CreditCardFragment_installments, true);
            this.attrSaveCard = typedArray.getBoolean(R.styleable.CreditCardFragment_saveCard, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PURCHASE_OPTION_SELECTED, selectedPurchaseOption);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_credit_card2, null);
        FontUtils.loadFonts(view);

        if (savedInstanceState != null && savedInstanceState.containsKey(PURCHASE_OPTION_SELECTED)) {
            selectedPurchaseOption = (PurchaseOption) savedInstanceState.getSerializable(PURCHASE_OPTION_SELECTED);
        }

        colorFieldWrong = getActivity().getResources().getColor(R.color.red_wrong);
        colorField = getActivity().getResources().getColor(R.color.edittext_color);

        validator = new CreditCardValidatorProd();

        listFlagCreditCard = (ListView) view.findViewById(R.id.view_list_card_listview);
        creditCardView = (CreditCardView) view.findViewById(R.id.creditcard_view);

        editNumberCard = (EditText) view.findViewById(R.id.ed_number_credit_card);
        editExpireCard = (EditText) view.findViewById(R.id.ed_expire_credit_card);
        editCVVCard = (EditText) view.findViewById(R.id.ed_cvv_credit_card);
        editNameCard = (EditText) view.findViewById(R.id.ed_name_credit_card);
        btEdit = (Button) view.findViewById(R.id.bt_edit);
        layoutPayment = (LinearLayout) view.findViewById(R.id.frg_input_card_layout_payment);
        layoutData = (FrameLayout) view.findViewById(R.id.frg_input_card_layout_data);
        spInstallments = (Spinner) view.findViewById(R.id.frg_payment_sp_portion);

        btNext = (ImageView) view.findViewById(R.id.frg_input_card_bt_next);
        btPay = (Button) view.findViewById(R.id.frg_input_card_bt_pay);
        txtValue = (TextView) view.findViewById(R.id.frg_input_card_txt_value);
        switchSaveCard = (Switch) view.findViewById(R.id.frg_input_card_sw_save_card);

        textLabelNumber = (TextView) view.findViewById(R.id.txt_label_number_card);
        textLabelExpireDate = (TextView) view.findViewById(R.id.txt_label_expire_date);
        textLabelCVV = (TextView) view.findViewById(R.id.txt_label_cvv);
        textLabelOwnerName = (TextView) view.findViewById(R.id.txt_label_owner_name);
        textLabelTotal = (TextView) view.findViewById(R.id.txt_label_total);

        pager = (LockableViewPager) view.findViewById(R.id.pager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = view;
        bindAttr();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                backFinish.set(false);

                Step step = pages.get(position);

                if (step == Step.FLAG) {
                    hideKeyboard();
                }

                if (step != Step.CVV) {
                    cardRestored = false;
                }

                setNavigationButtons(step, false);
                lastStep = step;

                pageChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setIssuerCode(IssuerCode.OTHER);

        btNext.setVisibility(View.INVISIBLE);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btNext.isActivated()) {
                    nextPage();
                } else {
                    shakeField();
                }
            }
        });

        editNumberCard.addTextChangedListener(this);
        editNumberCard.setOnEditorActionListener(this);
        editNumberCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateCreditCardNumber(editNumberCard, false);
            }
        });

        editExpireCard.addTextChangedListener(this);
        editExpireCard.setOnEditorActionListener(this);
        editExpireCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateExpiredDate(editExpireCard, false);
            }
        });

        editCVVCard.addTextChangedListener(this);
        editCVVCard.setOnEditorActionListener(this);
        editCVVCard.setOnFocusChangeListener(this);
        editCVVCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false);
            }
        });

        editNameCard.addTextChangedListener(this);
        editNameCard.setOnFocusChangeListener(this);
        editNameCard.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editNameCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateCreditCardName(editNameCard, false);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRestored = false;
                showEditCardLayout(true);
            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer installments = (Integer) spInstallments.getSelectedItem();

                if (validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false)) {
                    if (validator.validateCreditCardFlag(selectedPurchaseOption, installments)) {
                        CreditCardPaymentMethod creditCardMethod = new CreditCardPaymentMethod();
                        creditCardMethod.setCreditCardNumber(editNumberCard.getText().toString().replaceAll(" ", ""));
                        creditCardMethod.setIssuerCode(selectedPurchaseOption.getIssuerCode());
                        creditCardMethod.setSecurityCode(editCVVCard.getText().toString());
                        creditCardMethod.setCreditCardName(editNameCard.getText().toString());
                        creditCardMethod.setInstallments(installments);
                        applyExpiredDate(creditCardMethod);

                        if (actionOnPayListener != null) {
                            actionOnPayListener.onComplete(creditCardMethod, switchSaveCard.isChecked());
                        }
                    } else {
                        //TODO: soft bug
                        Toast.makeText(getActivity(), R.string.invalid_credit_card_flag, Toast.LENGTH_LONG).show();
                    }
                } else {
                    pager.setCurrentItem(pages.indexOf(Step.CVV));
                    showEditCardLayout(false);
                    showKeyboard(editCVVCard);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        EditText editText = null;

        if (editNumberCard.getText().hashCode() == s.hashCode()) {
            editText = editNumberCard;
            creditCardView.setTextNumber(s);
        } else if (editExpireCard.getText().hashCode() == s.hashCode()) {
            editText = editExpireCard;
            creditCardView.setTextExpDate(s);
        } else if (editCVVCard.getText().hashCode() == s.hashCode()) {
            editText = editExpireCard;
            creditCardView.setTextCVV(s);
        } else if (editNameCard.getText().hashCode() == s.hashCode()) {
            editText = editNameCard;
            creditCardView.setTextOwner(s);
        }

        if (editText != null) {
            checkColor(editText);
        }

        setNavigationButtons(lastStep, true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT && v.getTag() instanceof FieldValidator) {
            FieldValidator fieldValidator = (FieldValidator) v.getTag();

            if (fieldValidator.isValid()) {
                nextPage();
            }

            return true;
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == editCVVCard) {
            if (hasFocus) {
                creditCardView.flipToBack();
            } else {
                creditCardView.flipToFront();
            }
        } else if (v == editNameCard) {
            if (hasFocus) {
                editNameCard.setSelection(editNameCard.length());
            }
        }
    }

    private void nextPage() {
        if (pages.indexOf(lastStep) == pages.size() - 1|| cardRestored) {
            showPaymentLayout();
        } else {
            pager.setCurrentItem(pages.indexOf(lastStep) + 1);
        }
    }

    public void setLabelCardOwner(String label) {
        creditCardView.setTextLabelOwner(label);
    }

    public void setLabelCardDateExp(String label) {
        creditCardView.setTextLabelExpDate(label);
    }

    public void setLabelNumber(String label) {
        textLabelNumber.setText(label);
    }

    public void setLabelExpireDate(String label) {
        textLabelNumber.setText(label);
    }

    public void setLabelCVV(String label) {
        textLabelCVV.setText(label);
    }

    public void setLabelOwnerName(String label) {
        textLabelOwnerName.setText(label);
    }

    public void setTextButtonPay(String label) {
        btPay.setText(label);
    }

    public void setTextTotal(String label) {
        textLabelTotal.setText(label);
    }

    public void setBackgroundButtonPay(Drawable drawable) {
        btPay.setBackground(drawable);
    }

    public void setTextColorButtonPay(ColorStateList colorStateList) {
        btPay.setTextColor(colorStateList);
    }

    public void setVisibleInstallments(boolean visible) {
        if (visible) {
            viewHolder.findViewById(R.id.frg_input_lnl_portion).setVisibility(View.VISIBLE);
        } else {
            viewHolder.findViewById(R.id.frg_input_lnl_portion).setVisibility(View.GONE);
        }
    }

    public void setVisibleSaveCard(boolean visible) {
        if (visible) {
            viewHolder.findViewById(R.id.frg_input_frm_save_card).setVisibility(View.VISIBLE);
        } else {
            viewHolder.findViewById(R.id.frg_input_frm_save_card).setVisibility(View.GONE);
        }
    }

    private void bindAttr() {
        if (!TextUtils.isEmpty(labelCardOwner)) {
            setLabelCardOwner(labelCardOwner);
        }

        if (!TextUtils.isEmpty(labelCardDateExp)) {
            setLabelCardDateExp(labelCardDateExp);
        }

        if (!TextUtils.isEmpty(labelNumber)) {
            setLabelNumber(labelNumber);
        }

        if (!TextUtils.isEmpty(labelExpireDate)) {
            setLabelExpireDate(labelExpireDate);
        }

        if (!TextUtils.isEmpty(labelCVV)) {
            setLabelCVV(labelCVV);
        }

        if (!TextUtils.isEmpty(labelOwnerName)) {
            setLabelOwnerName(labelOwnerName);
        }

        if (!TextUtils.isEmpty(labelButtonPay)) {
            setTextButtonPay(labelButtonPay);
        }

        if (!TextUtils.isEmpty(labelTotal)) {
            setTextTotal(labelTotal);
        }

        if (payBackground != null) {
            setBackgroundButtonPay(payBackground);
        }

        if (btPayTextColor != null) {
            setTextColorButtonPay(btPayTextColor);
        }

        setVisibleInstallments(attrInstallments == null || attrInstallments);
        setVisibleSaveCard(attrSaveCard == null || attrSaveCard);
    }

    private void pageChanged() {
        if (actionOnPayListener != null) {
            actionOnPayListener.onChangedPage(lastStep);
        }
    }

    private void checkColor(EditText edit) {
        if (edit.getCurrentTextColor() == colorFieldWrong) {
            edit.setTextColor(colorField);
        }
    }

    private void shakeField() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        switch (lastStep) {
            case NUMBER : {
                viewHolder.findViewById(R.id.page_one).startAnimation(anim);
                editNumberCard.setTextColor(colorFieldWrong);
            } break;

            case EXPIRE_DATE : {
                viewHolder.findViewById(R.id.page_two).startAnimation(anim);
                editExpireCard.setTextColor(colorFieldWrong);
            } break;

            case CVV : {
                viewHolder.findViewById(R.id.page_three).startAnimation(anim);
                editCVVCard.setTextColor(colorFieldWrong);
            } break;

            case NAME : {
                viewHolder.findViewById(R.id.page_four).startAnimation(anim);
                editNameCard.setTextColor(colorFieldWrong);
            } break;
        }
    }

    private void setNavigationButtons(Step page, boolean isEditingText) {

        switch (page) {
            case FLAG: {
                btNext.setVisibility(View.INVISIBLE);
            } break;
            case NUMBER: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateCreditCardNumber(editNumberCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case EXPIRE_DATE: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateExpiredDate(editExpireCard, false)) {

                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case CVV: {
                btNext.setVisibility(View.VISIBLE);

                if (selectedPurchaseOption == null) {
                    // TODO: soft bug
//                    Crashlytics.log("Selected purchase Option is null");
                }

                if (validator == null) {
                    // TODO: soft bug
//                    Crashlytics.log("validator credit card is null");
                }

                if (validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case NAME: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateCreditCardName(editNameCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;
        }

    }


    private void showEditCardLayout(final boolean editFlagCard) {
        backFinish.set(false);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_rigth_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutPayment.setVisibility(View.GONE);

                if (editFlagCard) {
                    switchSaveCard.setVisibility(View.VISIBLE);
                    btEdit.setVisibility(View.INVISIBLE);
                }

                pageChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        if (editFlagCard) {
            pager.setCurrentItem(pages.indexOf(Step.FLAG));
            clearFields();

            Snackbar snackbar = Snackbar.make(viewHolder, R.string.option_pay_using_other_card_chosen, Snackbar.LENGTH_LONG)
                    .setActionTextColor(getActivity().getResources().getColor(R.color.white))
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restoreSavedCard(getCurrentCcv());
                        }
                    });

            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

        layoutPayment.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_rigth_in);
        layoutData.startAnimation(anim2);
        layoutData.setVisibility(View.VISIBLE);
    }

    private void animBtNext() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.pop_bounce_show);

        if (!btNext.isActivated()) {
            btNext.startAnimation(animation);
        }
    }

    private void clearFields() {
        editNumberCard.setText("");
        editExpireCard.setText("");
        editCVVCard.setText("");
        editNameCard.setText("");
        creditCardView.clear();

        setIssuerCode(IssuerCode.OTHER);
    }

    private void showPaymentLayout() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_left_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutData.setVisibility(View.GONE);
                btPay.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.pop_bounce_show));
                pageChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        layoutData.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_left_in);
        layoutPayment.startAnimation(anim2);
        layoutPayment.setVisibility(View.VISIBLE);

        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    public void setPagesOrder(Step... pages) throws IllegalArgumentException {
        if (pages.length < Step.values().length) {
            throw new IllegalArgumentException("You need provide at least " + Step.values().length + " pages");
        } else {
            this.pages = new ArrayList<>();

            for (int i = 0; i < Step.values().length; i++) {
                this.pages.add(pages[i]);
            }

            pager.setOffscreenPageLimit(5);
            FieldsPageAdapter adapter = new FieldsPageAdapter(viewHolder, this.pages);
            pager.setAdapter(adapter);
        }
    }

    public void setListPurchaseOptions(List<PurchaseOption> optionList) {
        setListPurchaseOptions(optionList, 0D);
    }

    public void setListPurchaseOptions(List<PurchaseOption> optionList, Double totalValue) {
        this.purchaseOptions = optionList;
        FlagCardAdapter flagCardAdapter = new FlagCardAdapter(getActivity(), purchaseOptions);
        listFlagCreditCard.setAdapter(flagCardAdapter);
        listFlagCreditCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                purchaseOptionSelected(purchaseOptions.get(i));
            }
        });

        setTotalValue(totalValue);
    }

    public void setTotalValue(Double totalValue) {
        valueTotal = totalValue;

        txtValue.setText(String.format("%s %s", getString(R.string.currency),
                FormatUtils.getCurrencyFormat().format(valueTotal)));
    }

    private void purchaseOptionSelected(PurchaseOption purchaseOption) {
        selectedPurchaseOption = purchaseOption;

        List<Integer> installments = new ArrayList<>();

        for (int i = 1; purchaseOption.getType() != null && i <= purchaseOption.getInstallments(); i++) {
            installments.add(i);
        }

        if (installments.isEmpty()) {
            spInstallments.setClickable(false);
        } else {
            spInstallments.setClickable(true);
        }

        installmentsCardAdapter = new InstallmentsCardAdapter(getActivity(), installments);
        spInstallments.setAdapter(installmentsCardAdapter);

        if (purchaseOption.getIssuerCode() == IssuerCode.AMEX) {
            int maxLength = AMEX_MAX_CVV;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editCVVCard.setFilters(fArray);
        } else {
            int maxLength = COMMON_MAX_CVV;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editCVVCard.setFilters(fArray);
        }

        setIssuerCode(purchaseOption.getIssuerCode());
        showKeyboard(editNumberCard);

        nextPage();
    }

    private void showKeyboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 400);
    }

    private void setIssuerCode(IssuerCode issuerCode) {
        editCVVCard.setText("");
        creditCardView.chooseFlag(issuerCode);
    }

    public void restoreSavedCard(CreditCardPaymentMethod creditCardPaymentMethod) {
        this.savedCard = creditCardPaymentMethod;
        boolean canPayWithSaveCard = false;
        int installments = 0;

        if (creditCardPaymentMethod != null) {
            for (PurchaseOption purchaseOption : purchaseOptions) {
                if (creditCardPaymentMethod.getIssuerCode() == purchaseOption.getIssuerCode()) {
                    canPayWithSaveCard = true;
                    installments = purchaseOption.getInstallments();
                    break;
                }
            }
        }

        if (creditCardPaymentMethod != null && canPayWithSaveCard) {
            cardRestored = true;
            backFinish.set(true);
            editNumberCard.setText(creditCardPaymentMethod.getCreditCardNumber());
            creditCardView.setTextNumber(editNumberCard.getText());
            creditCardView.setTextOwner(creditCardPaymentMethod.getCreditCardName());
            editNameCard.setText(creditCardPaymentMethod.getCreditCardName());
            String expireDateS = String.format("%s/%s",
                    String.format("%02d", creditCardPaymentMethod.getExpireMonth() != null ?
                            creditCardPaymentMethod.getExpireMonth() : 0),
                    creditCardPaymentMethod.getExpireYear());

            creditCardView.setTextExpDate(expireDateS);
            editExpireCard.setText(expireDateS);
            btEdit.setVisibility(View.VISIBLE);
            switchSaveCard.setChecked(true);
            switchSaveCard.setVisibility(View.GONE);

            List<Integer> installmentsList = new ArrayList<>();

            for (int i = 1; i <= installments; i++) {
                installmentsList.add(i);
            }

            if (installmentsList.isEmpty()) {
                spInstallments.setClickable(false);
            } else {
                spInstallments.setClickable(true);
            }

            installmentsCardAdapter = new InstallmentsCardAdapter(getActivity(), installmentsList);
            spInstallments.setAdapter(installmentsCardAdapter);


            selectedPurchaseOption = new PurchaseOption(creditCardPaymentMethod.getType(),
                    creditCardPaymentMethod.getIssuerCode(),
                    creditCardPaymentMethod.getInstallments());

            if (creditCardPaymentMethod.getIssuerCode() == IssuerCode.AMEX) {
                int maxLength = 4;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editCVVCard.setFilters(fArray);
            } else {
                int maxLength = 3;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editCVVCard.setFilters(fArray);
            }

            layoutPayment.setVisibility(View.VISIBLE);
            layoutData.setVisibility(View.GONE);

            pageChanged();

            setIssuerCode(selectedPurchaseOption.getIssuerCode());
        }
    }

    private CreditCardPaymentMethod getCurrentCcv() {
        return savedCard;
    }

    private void applyExpiredDate(CreditCardPaymentMethod creditCardMethod) {
        String value = editExpireCard.getText().toString();
        value = value.replace("/", "");

        String expire = TextUtils.substring(value, 0, 2);

        if (TextUtils.isDigitsOnly(expire)) {
            creditCardMethod.setExpireMonth(Integer.valueOf(expire));
        }

        expire = TextUtils.substring(value, 2, value.length());

        if (TextUtils.isDigitsOnly(expire)) {
            creditCardMethod.setExpireYear(Integer.valueOf(expire));
        }
    }

    public void backPressed() {
        if (layoutData.getVisibility() == View.VISIBLE) {
            if (pages.indexOf(lastStep) > 0) {
                pager.setCurrentItem(pages.indexOf(lastStep) - 1);
            } else {
                getActivity().finish();
            }
        } else {
            if (backFinish.compareAndSet(true, false)) {
                getActivity().finish();
            } else {
                pager.setCurrentItem(pages.size() - 1);
                showEditCardLayout(false);
            }
        }
    }
}
