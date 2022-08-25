package com.tookancustomer.modules.payment.constants;

import android.app.Activity;
import android.content.Context;

import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;

public interface PaymentConstants {

    /**
     * Payment for value
     */
    enum PaymentForFlow {
        ORDER_PAYMENT(0),
        SIGN_UP_FEE(1),
        IN_APP_WALLET(2),
        GIFT_CARD(3),
        REPAY_FROM_TASK_DETAILS(4),
        REWARD(5),
        DEBT(6),
        CUSTOM_ORDER_HIPPO(10),  //payment initiated from hippo
        USER_SUBSCRIPTION(11);

        public final long intValue;

        PaymentForFlow(long intValue) {
            this.intValue = intValue;
        }
    }


    /**
     * Lists all the values that a Payment can have.
     */
    enum PaymentValue {
        NONE(-1, R.string.empty, R.string.empty, -1),
        STRIPE(2, R.string.stripe, R.string.card, 1),
        PAYPAL(4, R.string.pay_with_paypal, R.string.paypal, 2),
        CASH(8, R.string.continue_with_cash, R.string.cash, 0),
        VENMO(16, R.string.venmo, R.string.venmo, 0),
        PAYFORT(32, R.string.payfort, R.string.card, 1),
        PAYTM(64, R.string.pay_with_paytm, R.string.paytm, 6),
        RAZORPAY(128, R.string.pay_with_razorpay, R.string.netbanking, 2),
        PAYSTACK(256, R.string.pay_with_paystack, R.string.netbanking, 2),
        VPOS(281, R.string.pay_via_vpos, R.string.pay_via_vpos, 2),
        BILLPLZ(512, R.string.pay_with_netbanking, R.string.netbanking, 2),
        UNACCOUNTED(700, R.string.unaccounted, R.string.unaccounted, -1),
        PAYFAST(1024, R.string.pay_with_card, R.string.card, 2),
        FAC(2048, R.string.pay_with_card, R.string.card, 1),
        INSTAPAY(4096, R.string.instaPay, R.string.instaPay, 2),
        PAYU_LATAM(8192, R.string.pay_with_card, R.string.card, 2),
        INAPP_WALLET(16384, R.string.inapp_wallet, R.string.inapp_wallet, 3),
        AUTHORISE_DOT_NET(32768, R.string.authorise_dot_net, R.string.card, 1),
        PAY_LATER(65536, R.string.pay_later, R.string.pay_later, 0),
        PAYMOB(131072, R.string.pay_with_paymob, R.string.pay_with_paymob, 2),
        PAYNOW(1048576, R.string.pay_with_paynow, R.string.pay_with_paynow, 2),
        VISTA_MONEY(262144, R.string.pay_with_card, R.string.card, 1),
        STRIPE_IDEAL(524288, R.string.pay_with_stripe_ideal, R.string.pay_with_stripe_ideal, 2),
        MPAISA(8388608, R.string.pay_with_MPAISA, R.string.pay_with_MPAISA, 2),
        PAYTM_LINK(16777216, R.string.paytm, R.string.paytm, 0),
        SSL_COMERZE(67108864, R.string.pay_with_netbanking, R.string.pay_with_netbanking, 2),
        TWO_CHECKOUT(268435456, R.string.pay_with_netbanking, R.string.pay_with_netbanking, 2),
        FAC_3DS(1073741824, R.string.pay_with_card_3ds, R.string.card, 2),
        CHECKOUT_COM(536870912, R.string.pay_with_checkout_dot_com, R.string.pay_with_checkout_dot_com, 2),
        AZUL(405, R.string.pay_with_card, R.string.azul, 1),
        RAZORPAY_UPI(5006, R.string.pay_via_razorpay_upi, R.string.pay_via_razorpay_upi, 2),
        PAYTM_UPI(5007, R.string.paytm_upi, R.string.paytm_upi, 2),
        PAY_ON_DELIVERY(9, R.string.pay_on_delivery, R.string.pay_on_delivery, 0 );

        public final long intValue;
        public final int payViaPaymentStringId;
        public final int paymentMethodStringId;
        public final int paymentType; /* Here payment type refers to cash, cards, wallets
         * Payment type=0 --> cash
         * Payment type=1 --> card
         * Payment type=2 --> wallet
         * Payment type=3 --> in app wallet
         * */

        PaymentValue(long intValue, int payViaPaymentStringId, int paymentMethodStringId, int paymentType) {
            this.intValue = intValue;
            this.payViaPaymentStringId = payViaPaymentStringId;
            this.paymentMethodStringId = paymentMethodStringId;
            this.paymentType = paymentType;
        }

        public static PaymentValue getPaymentByValue(long intValue) {
            PaymentValue paymentValue = null;

            for (PaymentValue status : values()) {
                if (status.intValue == intValue) {
                    paymentValue = status;
                    break;
                }
            }
            return paymentValue == null ? NONE : paymentValue;
        }

        public static String getPaymentString(Activity activity, long intValue, String displayString) {
            PaymentValue paymentValue = getPaymentByValue(intValue);

            if (paymentValue.intValue == INAPP_WALLET.intValue) {
                return StorefrontCommonData.getTerminology().getWallet();
            } else if (paymentValue.intValue == PAY_LATER.intValue) {
                return StorefrontCommonData.getTerminology().getPayLater();
            } else {
                if (activity instanceof MakePaymentActivity) {
                    return displayString;
//                    return getMessage(activity, paymentValue.payViaPaymentStringId);
                } else {
                    return displayString;
//                    return getMessage(activity, paymentValue.paymentMethodStringId);
                }
            }
        }

        private static String getMessage(Context context, int resourceId) {
            return StorefrontCommonData.getString(context, resourceId);
        }
    }

}
