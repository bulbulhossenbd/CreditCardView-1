package movile.com.creditcardguide.model;

import android.text.TextUtils;

public class CreditCardPaymentMethod extends PaymentMethod {

    private static final long serialVersionUID = -947666485361003120L;
    private static final int BIN_SIZE = 6;
    private String creditCardName;
    private String creditCardNumber;
    private Integer expireMonth;
    private Integer expireYear;
    private String securityCode;
    private IssuerCode issuerCode;
    private Integer installments;

    public CreditCardPaymentMethod() {
        super(Type.CREDIT_CARD);
    }

    public CreditCardPaymentMethod(String creditCardNumber,
                                   String creditCardName, Integer expireMonth, Integer expireYear, String securityCode,
                                   IssuerCode issuerCode, Integer installments) {

        super(Type.CREDIT_CARD);
        this.creditCardNumber = creditCardNumber;
        this.creditCardName = creditCardName;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.securityCode = securityCode;
        this.issuerCode = issuerCode;
        this.installments = installments;

    }

    public String getBin() {
        return creditCardNumber != null && creditCardNumber.length() >= BIN_SIZE ?
                TextUtils.substring(creditCardNumber, 0, BIN_SIZE) : null;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Integer getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(Integer expireMonth) {
        this.expireMonth = expireMonth;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public IssuerCode getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(IssuerCode issuerCode) {
        this.issuerCode = issuerCode;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    @Override
    public String toString() {
                return "creditCardName='" + creditCardName + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", expireMonth=" + expireMonth +
                ", expireYear=" + expireYear +
                ", securityCode='" + securityCode + '\'' +
                ", issuerCode=" + issuerCode + '\'' +
                ", installments=" + installments;
    }
}
