package movile.com.creditcardguide;

import movile.com.creditcardguide.model.CreditCardPaymentMethod;

/**
 * Created by FelipeSilvestre on 17/02/16.
 */
public interface ActionOnPayListener {
    void onChangedPage(CreditCardFragment.Step page);
    void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard);
}
