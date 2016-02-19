package movile.com.creditcardguide;

import android.widget.EditText;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PurchaseOption;


public abstract class CreditCardValidator {

    protected CreditCardValidator() {}

    public abstract boolean validateSecurityCode(IssuerCode issuerCode, EditText creditCardSecurityCode, boolean update);
    public abstract boolean validateExpiredDate(EditText creditCardExpiredDate, boolean update);
    public abstract boolean validateCreditCardFlag(PurchaseOption purchaseOption, Integer portions);
    public abstract boolean validateCreditCardNumber(EditText creditCardNumber, boolean update);
    public abstract boolean validateCreditCardName(EditText creditCardName, boolean update);

}