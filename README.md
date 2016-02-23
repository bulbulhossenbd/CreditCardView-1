# CreditCardView
A real life simulation of a credit card for android.

Watch:

[![WATCH](http://img.youtube.com/vi/6BsWDvnu1qA/0.jpg)](https://youtu.be/6BsWDvnu1qA "CreditCardView Android ")

You can use just the representation of credit card with CreditCardView, or you can use the entire solution to collect user's credit card data with CreditCardFragment.

##Usage

In your ``build.gradle`` file:

```groovy
dependencies {
    compile 'com.movile:creditcardguide:1.2-beta'
}
```

###CreditCardView

In your layout xml:

```xml
<movile.com.creditcardguide.view.CreditCardView
       android:id="@+id/creditCardView"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />
```

In your activity/fragment class:

```java
final CreditCardView creditCardView = (CreditCardView) findViewById(R.id.creditCardView);

creditCardView.chooseFlag(IssuerCode.VISACREDITO);
creditCardView.setTextExpDate("12/19");
creditCardView.setTextNumber("5555 4444 3333 1111");
creditCardView.setTextOwner("Felipe Silvestre");
creditCardView.setTextCVV("432");
```

To flip the card, you can call:

```java
creditCardView.flipToBack(); // To show the back side
creditCardView.flipToFront(); // To show the front side
```

If you want to configure the label texts of owner name and valid thru:

```xml
<movile.com.creditcardguide.view.CreditCardView
       xmlns:app="http://schemas.android.com/apk/res-auto"
       android:id="@+id/creditCardView"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:labelOwner="@string/label_owner"
       app:labelDateExp="@string/valid_thru">
```

###CreditCardFragment

In your layout xml:

```xml
<fragment
       android:id="@+id/frgCreditCard"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:name="movile.com.creditcardguide.CreditCardFragment" />
```

In your activity class:

```java
CreditCardFragment inputCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frgCreditCard);

// YOU MUST set the pages order (Set a order that makes sense to the user)
inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
        CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

// YOU MUST provide a purchase options list
inputCardFragment.setListPurchaseOptions(getList(), 230.0);
```

You must set the pages order and provide a purchase options list, to test you can create a fake list like the sample app:

```java
private List<PurchaseOption> getStubList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.PAYPAL, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DINERS, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.NUBANK, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AURA, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.ELO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.HIPERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.OTHER, 6));

        return list;
    }
```

And your activity needs to implement ActionOnPayListener interface to listen:

```java
    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {
        Toast.makeText(this, purchaseOption.toString(), Toast.LENGTH_LONG).show();
    }
```

There are some attributes that you can provide configure the fragment/view:

```xml
<fragment
       xmlns:app="http://schemas.android.com/apk/res-auto"
       android:id="@+id/frgCreditCard"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:name="movile.com.creditcardguide.CreditCardFragment"
       app:labelCardOwner="string"
       app:labelCardDateExp="string"
       app:labelButtonPay="string"
       app:buttonPayBackground="@color/color"
       app:buttonPayTextColor="@color/color"
       app:labelNumber="string"
       app:labelExpireDate="string"
       app:labelCVV="string"
       app:labelOwnerName="string"
       app:labelTotal="string"
       app:installments="false|true"
       app:saveCard="false|true"
       />
```

There are equivalent methods to set it on code.

Please for further information look at the sample app code.

### Open source libraries
CreditCardView uses the following open source files:
* [MaskedEditText](https://github.com/toshikurauchi/MaskedEditText/) by [@Toshi Kurauchi](https://github.com/toshikurauchi) (MIT License)

### Copyright and license

Code and documentation copyright 2016 Movile.
Code released under the [MIT license](https://github.com/Movile/CreditCardView/blob/master/LICENSE.txt).

Felipe Silvestre (Dev) & Victor Namba (Designer)
