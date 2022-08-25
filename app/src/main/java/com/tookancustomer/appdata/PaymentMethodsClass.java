package com.tookancustomer.appdata;

import android.app.Activity;

import com.hippo.utils.filepicker.Constant;
import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.userdata.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INAPP_WALLET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_LINK;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_ON_DELIVERY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.UNACCOUNTED;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.getPaymentByValue;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.getPaymentString;

public class PaymentMethodsClass {
    private static HashMap<Long, Integer> allEnabledPaymentMethodHashmap, cashEnabledPaymentMethodHashmap,
            cardEnabledPaymentMethodHashmap, walletEnabledPaymentMethodHashmap, inappWalletEnabledPaymentMethodHashmap;
    private static List<PaymentMethod> paymentMethods = new ArrayList<>();

    public static void clearPaymentHashMaps() {
        allEnabledPaymentMethodHashmap = null;
        cashEnabledPaymentMethodHashmap = null;
        cardEnabledPaymentMethodHashmap = null;
        walletEnabledPaymentMethodHashmap = null;
        inappWalletEnabledPaymentMethodHashmap = null;
    }

    /* Return all enabled payment methods hashmap */
    public static HashMap<Long, Integer> getPaymentMethodsMap() {
        if (allEnabledPaymentMethodHashmap != null) {
            return allEnabledPaymentMethodHashmap;
        } else {
            allEnabledPaymentMethodHashmap = new HashMap<>();
            paymentMethods = StorefrontCommonData.getFormSettings().getPaymentMethods();
            for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {
                if (paymentMethod.getEnabled() == 1 && paymentMethod.getValue() != UNACCOUNTED.intValue)
                    allEnabledPaymentMethodHashmap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
            }

            return allEnabledPaymentMethodHashmap;
        }
    }

    public static Set<Long> getPaymentMethodsKeySet() {
        return getPaymentMethodsMap().keySet();
    }

    public static boolean isPaymentEnabled() {
        return getPaymentMethodsKeySet().size() > 0;
    }

    /* Return only cash enabled payment methods hashmap */
    public static HashMap<Long, Integer> getCashPaymentMethodsHashMap() {
//        if (cashEnabledPaymentMethodHashmap != null) {
//            return cashEnabledPaymentMethodHashmap;
//        } else {
        cashEnabledPaymentMethodHashmap = new HashMap<>();
        for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {
            if (paymentMethod.getEnabled() == 1 && /*getPaymentByValue(paymentMethod.getValue()).paymentType*/
                    (paymentMethod.getPaymentMode() == Constants.PAYMENT_MODES.CASHMETHOD || paymentMethod.getValue() == PAYTM_LINK.intValue))
                cashEnabledPaymentMethodHashmap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
        }
        return cashEnabledPaymentMethodHashmap;
//        }
    }

    public static Set<Long> getCashPaymentMethodsKeySet() {
        return getCashPaymentMethodsHashMap().keySet();
    }

    public static boolean isCashPaymentEnabled() {
        return getCashPaymentMethodsHashMap().size() > 0;
    }


    /* Return only cards enabled payment methods hashmap */
    public static HashMap<Long, Integer> getCardPaymentMethodsHashMap() {
        if (cardEnabledPaymentMethodHashmap != null) {
            return cardEnabledPaymentMethodHashmap;
        } else {
            cardEnabledPaymentMethodHashmap = new HashMap<>();
            for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {
                if (paymentMethod.getEnabled() == 1 && /*getPaymentByValue(paymentMethod.getValue()).paymentType == 1*/paymentMethod.getPaymentMode() == Constants.PAYMENT_MODES.CARD)
                    cardEnabledPaymentMethodHashmap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
            }
            return cardEnabledPaymentMethodHashmap;
        }
    }

    public static Set<Long> getCardPaymentMethodsKeySet() {
        return getCardPaymentMethodsHashMap().keySet();
    }

    public static boolean isCardPaymentEnabled() {
        return getCardPaymentMethodsHashMap().size() > 0;
    }

    /* Return only wallets enabled payment methods hashmap */
    public static HashMap<Long, Integer> getWalletPaymentMethodsHashMap() {
//        if (walletEnabledPaymentMethodHashmap != null) {
//            return walletEnabledPaymentMethodHashmap;
//        } else {
        walletEnabledPaymentMethodHashmap = new HashMap<>();
        for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {
            if (paymentMethod.getEnabled() == 1 && /*getPaymentByValue(paymentMethod.getValue()).paymentType == 2*/(paymentMethod.getPaymentMode() == Constants.PAYMENT_MODES.WEBVIEW
                    || paymentMethod.getValue() == PAYTM.intValue))
                walletEnabledPaymentMethodHashmap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
        }
        return walletEnabledPaymentMethodHashmap;
//        }
    }

    public static Set<Long> getWalletPaymentMethodsKeySet() {
        return getWalletPaymentMethodsHashMap().keySet();
    }

    public static boolean isWalletPaymentEnabled() {
        return getWalletPaymentMethodsHashMap().size() > 0;
    }

    public static boolean isPaytmEnabled() {
        return getWalletPaymentMethodsHashMap().containsKey(PAYTM.intValue);
    }

    /* Return only in app wallets enabled payment methods hashmap */
    public static HashMap<Long, Integer> getInAppWalletPaymentMethodsHashMap() {
//        if (inappWalletEnabledPaymentMethodHashmap != null) {
//            return inappWalletEnabledPaymentMethodHashmap;
//        } else {
        inappWalletEnabledPaymentMethodHashmap = new HashMap<>();
         for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {
            if (paymentMethod.getEnabled() == 1 && /*getPaymentByValue(paymentMethod.getValue()).paymentType == 3*/paymentMethod.getPaymentMode() == Constants.PAYMENT_MODES.WALLET)
                inappWalletEnabledPaymentMethodHashmap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
        }
        return inappWalletEnabledPaymentMethodHashmap;
//        }
    }

    public static Set<Long> getInAppWalletPaymentMethodsKeySet() {
        return getInAppWalletPaymentMethodsHashMap().keySet();
    }

    public static boolean isInAppWalletPaymentEnabled() {

        /*if (PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(STRIPE.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(RAZORPAY.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(BILLPLZ.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(PAYPAL.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(PAYFORT.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(PAYMOB.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(PAYSTACK.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(PAYNOW.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(STRIPE_IDEAL.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(MPAISA.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(SSL_COMERZE.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(TWO_CHECKOUT.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(CHECKOUT_COM.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(VISTA_MONEY.intValue) ||
                PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(FAC_3DS.intValue) ||
                UIManager.isMerchantPaymentMethodsEnabled()

        ) {*/
        if (getInAppWalletPaymentMethodsHashMap().size() > 0 && PaymentMethodsClass.getPaymentMethodsMap().keySet().contains(INAPP_WALLET.intValue))
            return true;

        return false;


//        } else {
//            return false;
//        }

    }

    /*
     * Get enabled payment method value on basis of priority
     * Priority--> card > wallets > in app wallet > cash
     * */
    public static Long getEnabledPaymentMethod() {
        Long enabledPaymentValue = 0l;

        Set<Long> cardPaymentMethodsKeySet = getCardPaymentMethodsKeySet();

        if (cardPaymentMethodsKeySet.size() > 0) {
            return (Long) cardPaymentMethodsKeySet.toArray()[0];
        } else {

            Set<Long> walletPaymentMethodsKeySet = getWalletPaymentMethodsKeySet();
            if (walletPaymentMethodsKeySet.size() > 0) {
                return (Long) walletPaymentMethodsKeySet.toArray()[0];

            } else {
                Set<Long> inAppWalletPaymentMethodsKeySet = getInAppWalletPaymentMethodsKeySet();
                if (inAppWalletPaymentMethodsKeySet.size() > 0) {
                    return (Long) inAppWalletPaymentMethodsKeySet.toArray()[0];

                } else {

                    Set<Long> cashPaymentMethodsKeySet = getCashPaymentMethodsKeySet();
                    if (cashPaymentMethodsKeySet.size() > 0) {
                        return (Long) cashPaymentMethodsKeySet.toArray()[0];
                    }
                }
            }
        }
        return enabledPaymentValue;
    }

    /*
     * Return payment list consisting of all wallets and cash payment methods (No cards)
     * Priority will be inapp wallet > cash > wallets
     * */
    public static List<Datum> getPaymentList(Activity activity, List<Datum> paymentList) {
        return getPaymentList(activity, paymentList, false, true);
    }

    public static List<Datum> getPaymentList(Activity activity, List<Datum> paymentList, boolean isInAppWalletRequired, boolean isCashRequired) {
        Set<Long> inAppWalletPaymentMethodsKeySet = getInAppWalletPaymentMethodsKeySet();
        Set<Long> cashPaymentMethodsKeySet = getCashPaymentMethodsKeySet();
        Set<Long> walletPaymentMethodsKeySet = getWalletPaymentMethodsKeySet();
        paymentMethods = StorefrontCommonData.getFormSettings().getPaymentMethods();
        if (isInAppWalletRequired && isInAppWalletPaymentEnabled()) {
            Iterator iterInAppWallet = inAppWalletPaymentMethodsKeySet.iterator();
            while (iterInAppWallet.hasNext()) {
                long paymentValue = (long) iterInAppWallet.next();
                String displayString = "";
                int paymentFlowType = 0;
                for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
                    if (paymentValue == paymentMethods.get(i).getValue()) {
                        paymentFlowType = paymentMethods.get(i).getPaymentMode();
                        if (activity instanceof MakePaymentActivity)
                            displayString = paymentMethods.get(i).getLabel();
                        else
                            displayString = paymentMethods.get(i).getName();
                        break;
                    }
                }

                Datum datum = new Datum(paymentValue, getPaymentString(activity, paymentValue, displayString), paymentValue, paymentFlowType);
                paymentList.add(datum);
            }
        }

        if (isCashRequired) {
            Iterator iterCash = cashPaymentMethodsKeySet.iterator();
            while (iterCash.hasNext()) {
                long paymentValue = (long) iterCash.next();
                String displayString = "";
                int paymentFlowType = 0;
                for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
                    if (paymentValue == paymentMethods.get(i).getValue()) {
                        paymentFlowType = paymentMethods.get(i).getPaymentMode();
                        if (activity instanceof MakePaymentActivity)
                            displayString = paymentMethods.get(i).getLabel();
                        else
                            displayString = paymentMethods.get(i).getName();
                        break;
                    }
                }
                Datum datum = new Datum(paymentValue, getPaymentString(activity, paymentValue, displayString), paymentValue, paymentFlowType);
                paymentList.add(datum);
            }
        }

        Iterator iterWallet = walletPaymentMethodsKeySet.iterator();
        while (iterWallet.hasNext()) {
            long paymentValue = (long) iterWallet.next();
            String displayString = "";
            int paymentFlowType = 0;
            for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
                if (paymentValue == paymentMethods.get(i).getValue()) {
                    paymentFlowType = paymentMethods.get(i).getPaymentMode();
                    if (activity instanceof MakePaymentActivity)
                        displayString = paymentMethods.get(i).getLabel();
                    else
                        displayString = paymentMethods.get(i).getName();
                    break;
                }
            }
            Datum datum = new Datum(paymentValue, getPaymentString(activity, paymentValue, displayString), paymentValue, paymentFlowType);
            paymentList.add(datum);
        }

        return paymentList;
    }
}