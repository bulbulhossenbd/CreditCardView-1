package movile.com.creditcardguide;

import android.text.TextUtils;
import android.widget.EditText;


import java.util.Calendar;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PurchaseOption;

/**
 * Created by FelipeSilvestre on 04/02/16.
 */
public class CreditCardValidatorProd extends CreditCardValidator {

    @Override
    public boolean validateSecurityCode(IssuerCode issuerCode, EditText creditCardSecurityCode, boolean update) {

        String value;
        boolean result = false;

        if (creditCardSecurityCode.getText() != null && !TextUtils.isEmpty(
                value = creditCardSecurityCode.getText().toString())) {

            value = value.trim().replace(" ", "");

            int minDigits = issuerCode == IssuerCode.AMEX ? 4 : 3;

            if ((result = value.length() == minDigits && TextUtils.isDigitsOnly(value)) && update) {
                creditCardSecurityCode.setText(value);
            }
        }

        return result;

    }

    @Override
    public boolean validateExpiredDate(EditText creditCardExpiredDate, boolean update) {

        String expiredDate;
        boolean validExpiredDate = false;

        if (creditCardExpiredDate.getText() != null && !TextUtils.isEmpty(
                expiredDate = creditCardExpiredDate.getText().toString())) {

            expiredDate = expiredDate.trim().replace(" ", "");
            if ((validExpiredDate = isValidExpiredDate(expiredDate)) && update) {
                creditCardExpiredDate.setText(expiredDate);
            }
        }

        return validExpiredDate;

    }

    @Override
    public boolean validateCreditCardFlag(PurchaseOption purchaseOption, Integer portions) {
        return portions != null && portions != 0;
    }

    private boolean isValidExpiredDate(String expiredDate) {

        boolean valid = false;

        if (expiredDate.length() == 5) {
            expiredDate = expiredDate.replace("/", "");

            Calendar now = Calendar.getInstance();
            now.set(Calendar.DATE, 1);
            now.set(Calendar.HOUR, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.MILLISECOND, 0);

            int year = now.get(Calendar.YEAR);

            Calendar expireDate = Calendar.getInstance();

            int expireMonth = Integer.valueOf(TextUtils.substring(expiredDate, 0, 2)) - 1;
            int expireYear = Integer.valueOf("20" + TextUtils.substring(expiredDate, 2, expiredDate.length()));

            if (expireMonth >= 0 && expireMonth <= 11 && expireYear <= (year + 10)) {

                expireDate.set(Calendar.DATE, 1);
                expireDate.set(Calendar.MONTH, expireMonth);
                expireDate.set(Calendar.YEAR, expireYear);
                expireDate.set(Calendar.HOUR, 0);
                expireDate.set(Calendar.MINUTE, 0);
                expireDate.set(Calendar.MILLISECOND, 0);

                valid = expireDate.getTime().getTime() > now.getTime().getTime();

            }

        }

        return valid;

    }

    @Override
    public boolean validateCreditCardNumber(EditText creditCardNumber, boolean update) {

        String number;
        boolean validCreditCardNumber = false;

        if (creditCardNumber != null
                && !TextUtils.isEmpty(number = creditCardNumber.getText().toString().replaceAll(" ", ""))
                && number.length() > 8
                && !TextUtils.isEmpty(number.replace("0", ""))) {

            number = number.trim().replace(" ", "");
            if ((validCreditCardNumber = luhnTest(number)) && update) {
                creditCardNumber.setText(number);
            }
        }

        return validCreditCardNumber;

    }

    @Override
    public boolean validateCreditCardName(EditText creditCardName, boolean update) {
        String name;
        return creditCardName != null &&
                !TextUtils.isEmpty(name = creditCardName.getText().toString())
                && name.length() > 4 && name.trim().contains(" ");
    }

    public static boolean luhnTest(String number) {

        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();

        for (int i = 0; i < reverse.length(); i++) {

            int digit = Character.digit(reverse.charAt(i), 10);

            if (i % 2 == 0) { //this is for odd digits, they are 1-indexed in the algorithm

                s1 += digit;

            } else { //add 2 * digit for 0-4, add 2 * digit - 9 for 5-9

                s2 += 2 * digit;

                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

}
