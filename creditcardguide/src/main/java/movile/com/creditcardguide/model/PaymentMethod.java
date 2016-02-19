package movile.com.creditcardguide.model;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = -6446858523439800464L;

    public enum Type {

        CREDIT_CARD
    }

    private Type type;

    public PaymentMethod(){
        this.type = Type.CREDIT_CARD;
    }

    public PaymentMethod(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
