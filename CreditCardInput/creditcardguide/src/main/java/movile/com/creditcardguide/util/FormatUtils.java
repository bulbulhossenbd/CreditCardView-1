package movile.com.creditcardguide.util;

import java.text.DecimalFormat;

public final class FormatUtils {

    private FormatUtils(){}

    public static DecimalFormat getCurrencyFormat() {
        return new DecimalFormat("0.00");
    }

}
