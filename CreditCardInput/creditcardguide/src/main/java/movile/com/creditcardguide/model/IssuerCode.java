package movile.com.creditcardguide.model;


import java.io.Serializable;

import movile.com.creditcardguide.R;

public enum IssuerCode implements Serializable {

    AMEX (R.string.amex, R.drawable.card_american, R.drawable.american, R.drawable.american_back, R.drawable.sign_american, R.color.gray_hard_text, R.color.white),
    AURA (R.string.aura, R.drawable.card_aura, R.drawable.aura, R.drawable.aura_back, R.drawable.sign_master, R.color.gray_hard_text, R.color.gray_hard_text),
    DINERS (R.string.diners, R.drawable.card_diners, R.drawable.diners, R.drawable.diners_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text),
    PAYPAL (R.string.paypal, R.drawable.card_paypal, R.drawable.paypal, R.drawable.paypal_back, R.drawable.sign_paypal, R.color.white, R.color.gray_hard_text),
    ELO (R.string.elo, R.drawable.card_elo,  R.drawable.elo, R.drawable.elo_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text),
    HIPERCARD (R.string.hipercard, R.drawable.card_hiper,   R.drawable.hipercard, R.drawable.hipercard_back, R.drawable.sign_hipercard, R.color.white, R.color.gray_hard_text),
    MASTERCARD(R.string.redecardcredito, R.drawable.card_master,  R.drawable.mastercard, R.drawable.mastercard_back, R.drawable.sign_master, R.color.white, R.color.gray_hard_text),
    NUBANK(R.string.nubank, R.drawable.card_nubank,  R.drawable.nubank, R.drawable.nubank_back, R.drawable.sign_master, R.color.white, R.color.gray_hard_text),
    VISACREDITO (R.string.visacredito, R.drawable.card_visa,  R.drawable.visa, R.drawable.visa_back, R.drawable.sign_visa, R.color.white, R.color.gray_hard_text),
    VISAELECTRON (R.string.visaelectron, R.drawable.card_visa,  R.drawable.visa, R.drawable.visa_back, R.drawable.sign_visa, R.color.white, R.color.gray_hard_text),
    OTHER (R.string.other, R.drawable.card_placeholder,  R.drawable.default_card, R.drawable.default_card_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text);

    private int nameId;
    private Integer iconId;
    private Integer imageCardFront;
    private Integer imageCardBack;
    private Integer imageSignCard;
    private Integer colorText;
    private Integer colorShadowText;

    IssuerCode(int nameId, Integer iconId, Integer imageCardFront, Integer imageCardBack, Integer imageSignCard, Integer colorText, Integer colorShadowText) {
        this.nameId = nameId;
        this.iconId = iconId;
        this.imageCardFront = imageCardFront;
        this.imageCardBack = imageCardBack;
        this.imageSignCard = imageSignCard;
        this.colorText = colorText;
        this.colorShadowText = colorShadowText;
    }

    public int getNameId() {
        return nameId;
    }

    public Integer getIconId() {
        return iconId;
    }

    public Integer getImageCardFront() {
        return imageCardFront;
    }

    public Integer getImageCardBack() {
        return imageCardBack;
    }

    public Integer getImageSignCard() {
        return imageSignCard;
    }

    public Integer getColorText() {
        return colorText;
    }

    public Integer getColorShadowText() {
        return colorShadowText;
    }
}
