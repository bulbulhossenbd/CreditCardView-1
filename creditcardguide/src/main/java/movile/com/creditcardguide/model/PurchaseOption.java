package movile.com.creditcardguide.model;

import java.io.Serializable;

public class PurchaseOption implements Serializable {

    private static final long serialVersionUID = -8789963365102863564L;
    private PaymentMethod.Type type;
    private IssuerCode issuerCode;
    private Integer installments;

    public PurchaseOption() {
    }

    public PurchaseOption(PaymentMethod.Type type, IssuerCode issuerCode, Integer installments) {
        this.type = type;
        this.issuerCode = issuerCode;
        this.installments = installments;
    }

    public PaymentMethod.Type getType() {
        return type;
    }

    public void setType(PaymentMethod.Type type) {
        this.type = type;
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
}
