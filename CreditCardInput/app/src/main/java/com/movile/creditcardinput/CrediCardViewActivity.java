package com.movile.creditcardinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.view.CreditCardView;

public class CrediCardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credicard_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Credit Card View");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CreditCardView creditCardView = (CreditCardView) findViewById(R.id.act_creditcard_view);

        creditCardView.chooseFlag(IssuerCode.VISACREDITO);
        creditCardView.setTextExpDate("12/19");
        creditCardView.setTextNumber("5555 4444 3333 1111");
        creditCardView.setTextOwner("Felipe Silvestre");
        creditCardView.setTextCVV("432");

        ((EditText) findViewById(R.id.ed_owner_name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardView.setTextOwner(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.bt_flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardView.isShowingFront()) {
                    creditCardView.flipToBack();
                } else {
                    creditCardView.flipToFront();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
