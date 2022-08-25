package com.tookancustomer.appdata;

import android.app.Activity;
import android.content.Context;

import com.tookancustomer.R;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Constants.NLevelImageStyles.NONE;

/**
 * Created by cl-macmini-83 on 19/11/16.
 */
public interface Constants {

    String EMPTY_STRING = "";

    int START_TIME_BUFFER = 3; //in minutes
    int START_TIME_BUFFER_INSTANT = 0; //in minutes
    int END_TIME_BUFFER = 2;
    int SCHEDULED_STATUS = 25;
    double MAP_CURRENT_DISTANCE_CHECK = -1; // in meters
    double MAP_UPDATED_LOCATION_DIFFERENCE = 5;
    double MAP_UPDATED_LOCATION_DIFFERENCE_RESTAURENTS_VIEW = 100;

    int SEARCH_INTERVAL = 800;
    int MERCHANT_PAGINATION_LIMIT = 25;

    /**
     * Card types
     */
    String CARD_VISA = "Visa";
    String CARD_MASTER = "MasterCard";
    String CARD_AMERICAN_EXPRESS = "American Express";
    String CARD_DISCOVER = "Discover";
    String CARD_DINERS_CLUB = "Diners Club";
    String CARD_JCB = "JCB";


    /**
     * The messages to be conveyed when an error
     * is reported as a result of Api Call Failure
     */
    enum StatusDescription {
        CUSTOM(-1),

        NONE(R.string.empty),
        HTTP_ERROR(R.string.http_error),
        CONNECTION_REFUSED(R.string.connection_refused_error),
        SSL_HANDSHAKE_FAILED(R.string.ssl_handshake_error),
        NO_INTERNET(R.string.not_connected_to_internet_text),
        SOCKET_TIMED_OUT(R.string.remote_server_failed_error),
        CONNECTION_TIMED_OUT(R.string.connection_timed_out_error),
        NO_HTTP_RESPONSE(R.string.remote_server_could_not_respond),
        PARSING_ERROR(R.string.an_error_was_procured_while_parsing),
        RUNTIME_ERROR(R.string.an_unexpected_error_occurred),
        UNKNOWN_ERROR_OCCURRED(R.string.an_unexpected_error_occurred),
        CONNECTION_RESET_BY_PEER(R.string.connection_reset_error),
        UNEXPECTED_END_OF_STREAM(R.string.unexpected_end_stream);

        private final int resourceId;
        private String message;

        StatusDescription(int resourceId) {
            this.resourceId = resourceId;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage(Context context) {
            if (context == null)
                return "Cannot process your request at the moment";
            return resourceId == -1 ? message : StorefrontCommonData.getString(context, resourceId);
        }
    }

    /**
     * The type of file being Saved
     */
    enum FileType {
        LOG_FILE("logs", ".log"),
        IMAGE_FILE("snapshots", ""),
        GENERAL_FILE("public", ".txt"),
        PRIVATE_FILE("system", ".sys");

        public final String extension;
        public final String directory;

        FileType(String relativePath, String extension) {
            this.extension = extension;
            this.directory = relativePath;
        }
    }

    /**
     * Lists all the NLevelImageStyles
     */
    enum NLevelImageStyles {
        SMALL(1, ""),
        MEDIUM(2, ""),
        LARGE(3, ""),
        NONE(4, "");


        public final int appStyleValue;
        public final String appStyleFont;

        NLevelImageStyles(int appStyleValue, String appStyleFont) {
            this.appStyleValue = appStyleValue;
            this.appStyleFont = appStyleFont;
        }

        public static String getAppFontByValue(Context context, int appStyleValue) {
            NLevelImageStyles appStyles = null;
            for (NLevelImageStyles status : values()) {
                if (status.appStyleValue == appStyleValue) {
                    appStyles = status;
                    break;
                }
            }
            return appStyles == null ? NONE.appStyleFont : appStyles.appStyleFont;
        }
    }

    enum UnitType {
        UNIT(1, "item"),
        KG(8, "kg"),
        POUND(9, "pound");


        public final int appStyleValue;
        public final String appStyleFont;

        UnitType(int unit, String appStyleFont) {
            this.appStyleValue = unit;
            this.appStyleFont = appStyleFont;
        }

        public static String getUnitTextFromValue(Context context, int unit) {
            UnitType unitType = null;
            for (UnitType status : values()) {
                if (status.appStyleValue == unit) {
                    unitType = status;
                    break;
                }
            }
            return unitType == null ? NONE.appStyleFont : unitType.appStyleFont;
        }
    }


    /**
     * Lists all the NLevelAppStyles
     */
    enum NLevelAppStyles {
        LIGHT(1, "fonts/OpenSans-Light.ttf"),
        REGULAR(2, "fonts/OpenSans-Regular.ttf"),
        BOLD(3, "fonts/OpenSans-Bold.ttf"),
        SEMIBOLD(4, "fonts/OpenSans-Semibold.ttf");


        public final int appStyleValue;
        public final String appStyleFont;

        NLevelAppStyles(int appStyleValue, String appStyleFont) {
            this.appStyleValue = appStyleValue;
            this.appStyleFont = appStyleFont;
        }

        public static String getAppFontByValue(Context context, int appStyleValue) {
            NLevelAppStyles appStyles = null;
            for (NLevelAppStyles status : values()) {
                if (status.appStyleValue == appStyleValue) {
                    appStyles = status;
                    break;
                }
            }
            return appStyles == null ? REGULAR.appStyleFont : appStyles.appStyleFont;
        }
    }

    /**
     * Lists all the NLevelButtonType
     */
    enum NLevelButtonType {
        SELECT_TEXT_BUTTON(1),
        ADD_AND_REMOVE_BUTTON(2),
        NEXT_ARROW_BUTTON(3),
        HIDDEN_BUTTON(4);

        public final int buttonValue;

        NLevelButtonType(int buttonValue) {
            this.buttonValue = buttonValue;
        }

        public static int getButtonIdByValue(Context context, int buttonValue) {
            NLevelButtonType buttonType = null;
            for (NLevelButtonType status : values()) {
                if (status.buttonValue == buttonValue) {
                    buttonType = status;
                    break;
                }
            }

            return buttonType == null ? HIDDEN_BUTTON.buttonValue : buttonType.buttonValue;
        }
    }

    /**
     * Lists all the NLevelLayoutType
     */
    enum NLevelLayoutType {
        LIST_LAYOUT(1, R.layout.itemview_product_list),
        BANNER_LAYOUT(2, R.layout.itemview_product_banner),
        MENU_LAYOUT(4, R.layout.itemview_product_list);

        public final int layoutValue;
        public final int layoutMode;

        NLevelLayoutType(int layoutValue, int layoutMode) {
            this.layoutValue = layoutValue;
            this.layoutMode = layoutMode;
        }

        public static int getLayoutModeByValue(Context context, int layoutValue) {
            NLevelLayoutType layoutType = null;
            for (NLevelLayoutType status : values()) {
                if (status.layoutValue == layoutValue) {
                    layoutType = status;
                    break;
                }
            }
            return layoutType == null ? LIST_LAYOUT.layoutMode : layoutType.layoutMode;
        }
    }

    enum RatingColorValues {
        LOW_RATING(R.color.rating_low),
        MEDIUM_RATING(R.color.rating_medium),
        HIGH_RATING(R.color.rating_high);

        public final int colorResourceId;

        RatingColorValues(int colorResourceId) {
            this.colorResourceId = colorResourceId;
        }

        public static int getColorResourceValue(Context context, Number ratings) {
            if (ratings.doubleValue() < 1.3) {
                return LOW_RATING.colorResourceId;
            } else if (ratings.doubleValue() < 3.3) {
                return MEDIUM_RATING.colorResourceId;
            } else {
                return HIGH_RATING.colorResourceId;
            }
        }
    }

    enum WalletTransactionStatus {
        NULL(0, R.drawable.ic_add_money, R.string.empty, ""),
        ADD(1, R.drawable.ic_add_money, R.string.added_to_wallet, "+"),
        DEDUCTION(2, R.drawable.ic_debit_money, R.string.paid_for_order, "-"),
        REFUND(3, R.drawable.ic_credit_money, R.string.refunded_back, "+"),
        CREDIT(4, R.drawable.ic_credit_money, R.string.credited_by_admin, "+"),
        FAIL(5, R.drawable.ic_failed_transaction, R.string.wallet_transaction_failed, "! "),
        GIFT_CARD_PURCHASE(6, R.drawable.ic_debit_money, R.string.gift_card_purchase, "- "),
        GIFT_CARD_REDEEM(7, R.drawable.ic_credit_money, R.string.gift_card_redeem, "+ "),
        DEDUCT_BY_ADMIN(8, R.drawable.ic_debit_money, R.string.deducted_by_admin, "- "),
        CASHBACK_FOR_ORDER(9, R.drawable.ic_debit_money, R.string.cashback_for, "+ "),
        DEDUCTION_TO_BUY_REWARD(10, R.drawable.ic_debit_money, R.string.wallet_money_paid_for, "- "),
        DEBT_PAYMENT(11, R.drawable.ic_debit_money, R.string.debt_paid, "- ");

        public final int transactionType;
        public final int drawableValue;
        public final int stringValue;
        public final String transactionMoneySymbol;

        WalletTransactionStatus(int transactionType, int drawableValue, int stringValue, String transactionMoneySymbol) {
            this.transactionType = transactionType;
            this.drawableValue = drawableValue;
            this.stringValue = stringValue;
            this.transactionMoneySymbol = transactionMoneySymbol;
        }

        public static WalletTransactionStatus getWalletTransactionData(int transactionType) {
            WalletTransactionStatus walletTransactionStatus = NULL;

            for (WalletTransactionStatus status : values()) {
                if (status.transactionType == transactionType) {
                    walletTransactionStatus = status;
                    break;
                }
            }
            return walletTransactionStatus;

        }
    }

    enum GoogleAnalyticsValues {
        SIGN_IN_SUCCESS(1, "sign_in_success"),
        SIGN_IN_FAILURE(2, "sign_in_failure"),
        SIGN_UP_SUCCESS(3, "signup_success"),
        SIGN_UP_FAILURE(4, "signup_failure"),
        ADD_QUANTITY(5, "add_quantity"),
        REMOVE_QUANTITY(6, "remove_quantity"),
        GO_TO_CHECKOUT(7, "go_to_checkout"),
        GO_TO_PAYMENT(8, "go_to_payment"),
        CATEGORY_CLICK(9, "category_click"),
        ADD_ADDRESS(10, "add_address"),
        ORDER_CREATED_SUCCESS(11, "order_created_success"),
        ORDER_CREATED_FAILURE(12, "order_created_failure"),
        RESTAURANT_ORDER_ONLINE(13, "restaurant_detail_order_online"),
        RESTAURANT_CLICK(14, "restaurant_click");


        public final int gaId;
        public final String gaString;

        GoogleAnalyticsValues(int gaId, String gaString) {
            this.gaId = gaId;
            this.gaString = gaString;
        }
    }

    enum ProductsUnitType {
        FIXED(1, R.string.empty, R.string.empty),
        PER_MINUTE(2, R.string.minute, R.string.minutes),
        PER_HOUR(3, R.string.hour, R.string.hours),
        PER_DAY(4, (UIManager.getBusinessModelType().equalsIgnoreCase("Rental") ? R.string.night : R.string.dayss),
                (UIManager.getBusinessModelType().equalsIgnoreCase("Rental") ? R.string.nights : R.string.dayss)),
        PER_Week(5, R.string.weekss, R.string.weekss),
        PER_MONTH(6, R.string.month, R.string.months),
        PER_YEAR(7, R.string.yearss, R.string.yearss),
        PER_KILOGRAM(8, R.string.kilogram, R.string.kilograms),
        PER_POUND(9, R.string.pounds, R.string.pounds),
        PER_PERSON(10, R.string.persons, R.string.persons),
        PER_KILOMETER(11, R.string.kms, R.string.kms),
        PER_FEET(12, R.string.text_feett, R.string.text_feett),
        PER_SQUARE_FEET(13, R.string.text_square_eet, R.string.text_square_eet),
        PER_LINEAR_FEET(14, R.string.text_linear_feett, R.string.text_linear_feett),
        PER_SQM(15, R.string.per_square_meter, R.string.per_square_meter),
        PER_METER(16, R.string.meter, R.string.meter);



        public final int value;
        public final int stringValueSingular;
        public final int stringValuePlural;

        ProductsUnitType(int value, int stringValueSingular, int stringValuePlural) {
            this.value = value;
            this.stringValueSingular = stringValueSingular;
            this.stringValuePlural = stringValuePlural;
        }

        public static ProductsUnitType getUnitType(int value) {
            ProductsUnitType productsUnitType = null;

            for (ProductsUnitType taskType : values()) {
                if (taskType.value == value) {
                    productsUnitType = taskType;
                    break;
                }
            }
            return productsUnitType == null ? FIXED : productsUnitType;
        }

        public static long getStartEndDifferenceMultiple(int value, int unit) {
            long difference = 1;

            if (getUnitType(value) == PER_MINUTE) {
                difference = 60;
            } else if (getUnitType(value) == PER_HOUR) {
                difference = 60 * 60;
            } else if (getUnitType(value) == PER_DAY) {
                difference = 60 * 60 * 24;
            } else if (getUnitType(value) == PER_Week) {
                difference = 60 * 60 * 24 * 7;
            } else if (getUnitType(value) == PER_MONTH) {
                difference = 60 * 60 * 24 * 30;
            } else if (getUnitType(value) == PER_YEAR) {
                difference = 60 * 60 * 24 * 365;
            }


            return difference * unit * 1000;
        }

        public static String getUnitTypeText(Activity mActivity, int unit, int value, boolean showSingleUnit) {
            String text = "";
            if (unit <= 1) {
                if (showSingleUnit) {
                    text = unit + " " + Utils.capitaliseWords(
                            StorefrontCommonData.getString(mActivity, ProductsUnitType.getUnitType(value).stringValueSingular));
                } else {
                    text = Utils.capitaliseWords(StorefrontCommonData.getString(mActivity, ProductsUnitType.getUnitType(value).stringValueSingular));
                }
            } else {
                text = unit + " " + Utils.capitaliseWords(StorefrontCommonData.getString(mActivity, ProductsUnitType.getUnitType(value).stringValuePlural));
            }

            return text;
        }
    }

    /**
     * Lists all the Statuses that a Task can have.
     */
    enum TaskStatus {
        NONE(-1, R.string.empty, R.string.empty, "", R.color.transparent),
        ASSIGNED(0, R.string.assigned_text, R.string.assigned_text, "", R.color.status_assigned),
        STARTED(1, R.string.start_text, R.string.started_text, "", R.color.status_started),
        SUCCESSFUL(2, R.string.successful_text, R.string.successful_text, "", R.color.status_successful),
        FAILED(3, R.string.fail_text, R.string.failed_text, "", R.color.status_failed),
        ARRIVED(4, R.string.arrive_text, R.string.arrived_text, "", R.color.status_in_progress),
        PARTIAL(5, R.string.partial_text, R.string.partial_text, "", R.color.status_failed),
        UNASSIGNED(6, R.string.unassigned_text, R.string.unassigned_text, "", R.color.status_unassigned),
        ACCEPTED(7, R.string.accept_text, R.string.accepted_text, "", R.color.status_accepted),
        ACKNOWLEDGED(7, R.string.acknowledge_text, R.string.acknowledged_text, "", R.color.status_accepted),
        DECLINED(8, R.string.decline_text, R.string.declined_text, "", R.color.status_declined),
        ORDERED(10, R.string.ordered_text, R.string.ordered_text, StorefrontCommonData.getTerminology().getOrdered(), R.color.status_in_progress),
//        CANCELED(9, R.string.cancel_text, R.string.canceled_text, R.color.status_canceled),

        //        DELETED(10,!Dependencies.isStorefrontApp() ?R.string.ordered_text :R.string.delete_text,
//                !Dependencies.isStorefrontApp()?R.string.ordered_text :R.string.deleted_text,
//        R.color.status_in_progress),
//
//        IGNORED(11,!Dependencies.isStorefrontApp() ?R.string.accepted_text :R.string.ignored_text,
//                !Dependencies.isStorefrontApp()?R.string.accepted_text :R.string.ignored_text,
//        R.color.status_accepted),

        PENDING_STATUS(9, R.string.pending_text, R.string.pending_text, StorefrontCommonData.getTerminology().getPending(), R.color.status_in_progress),
        DELETED(10, R.string.ordered_text, R.string.ordered_text, StorefrontCommonData.getTerminology().getOrdered(), R.color.status_in_progress),
        IGNORED(11, R.string.accepted_text, R.string.accepted_text, StorefrontCommonData.getTerminology().getAccepted(), R.color.status_accepted),

        DISPATCHED(12, R.string.dispatched_text, R.string.dispatched_text, StorefrontCommonData.getTerminology().getDispatched(), R.color.status_in_progress),
        DELIVERED(13, R.string.delivered_text, R.string.delivered_text, StorefrontCommonData.getTerminology().getCompleted(), R.color.status_successful),
        REJECTED(14, R.string.rejected_text, R.string.rejected_text, StorefrontCommonData.getTerminology().getRejected(), R.color.status_declined),
        CANCELLED(15, R.string.canceled_text, R.string.canceled_text, StorefrontCommonData.getTerminology().getCancelled(), R.color.status_declined),
        DELIVERY_PENDING(16, R.string.dispatched_text, R.string.dispatched_text, StorefrontCommonData.getTerminology().getDispatched(), R.color.status_in_progress),
        PENDING(17, R.string.dispatched_text, R.string.dispatched_text, StorefrontCommonData.getTerminology().getDispatched(), R.color.status_in_progress),
        ORDERASSIGNED(44, R.string.JOB_ASSIGNED, R.string.JOB_ASSIGNED, StorefrontCommonData.getTerminology().getJobassigned(), R.color.status_job_assigned),
        PROCESSED(45, R.string.PROCESSED, R.string.PROCESSED, StorefrontCommonData.getTerminology().getProcessed(), R.color.status_processed),
        PICKED_UP(46, R.string.PICKED_UP, R.string.PICKED_UP, StorefrontCommonData.getTerminology().getPickedup(), R.color.status_picked_up),
        FOOD_READY(47, R.string.FOOD_READY, R.string.FOOD_READY, StorefrontCommonData.getTerminology().getFoodReady(), R.color.status_food_ready);
        public final int value;
        public final int activeResourceId;
        public final int passiveResourceId;
        public final int colorResourceId;
        public final String backendTerminology;

        TaskStatus(int value, int activeResourceId, int passiveResourceId, String backendTerminology, int colorResourceId) {
            this.value = value;
            this.activeResourceId = activeResourceId;
            this.passiveResourceId = passiveResourceId;
            this.colorResourceId = colorResourceId;
            this.backendTerminology = backendTerminology;
        }

        public static TaskStatus getTaskStatusByValue(int value) {
            TaskStatus mTaskStatus = null;

            for (TaskStatus status : values()) {
                if (status.value == value) {
                    mTaskStatus = status;
                    break;
                }
            }

            return mTaskStatus == null ? NONE : mTaskStatus;
        }

        public static int getColorRes(int status) {
            int colorID = R.color.status_started;

            for (TaskStatus taskStatus : values()) {
                if (taskStatus.value == status) {
                    colorID = taskStatus.colorResourceId;
                    break;
                }
            }

            return colorID;
        }

        public static String getCurrentTasksFiler() {
            String filter = "";
            String prefix = ",";

            StringBuilder filterString = new StringBuilder();

            filterString.append(STARTED.value).append(prefix).append(ARRIVED.value);

            filter = String.valueOf(filterString);

            return filter;
        }

        public String getActive(Context context) {
            return getMessage(context, activeResourceId);
        }

        public String getPassive(Context context) {
            if (backendTerminology != null && !backendTerminology.isEmpty()) {
                return backendTerminology;
            } else {
                return getMessage(context, passiveResourceId);
            }
        }

        private String getMessage(Context context, int resourceId) {
            return StorefrontCommonData.getString(context, resourceId);
        }
    }


    /**
     * Lists all the Statuses that a Task can have.
     */
    enum RecurringStatus {

        NONE(-1, R.string.empty, R.string.empty, "", R.color.transparent),
        PENDING_STATUS(0, R.string.pending_text, R.string.pending_text, StorefrontCommonData.getTerminology().getPending(), R.color.status_in_progress),

        ACCEPTED(1, R.string.accept_text, R.string.accepted_text, StorefrontCommonData.getTerminology().getAccepted(), R.color.status_accepted),
        REJECTED(2, R.string.rejected_text, R.string.rejected_text, StorefrontCommonData.getTerminology().getRejected(), R.color.status_declined);

        public final int value;
        public final int activeResourceId;
        public final int passiveResourceId;
        public final int colorResourceId;
        public final String backendTerminology;

        RecurringStatus(int value, int activeResourceId, int passiveResourceId, String backendTerminology, int colorResourceId) {
            this.value = value;
            this.activeResourceId = activeResourceId;
            this.passiveResourceId = passiveResourceId;
            this.colorResourceId = colorResourceId;
            this.backendTerminology = backendTerminology;
        }

        public static RecurringStatus getTaskStatusByValue(int value) {
            RecurringStatus mTaskStatus = null;

            for (RecurringStatus status : values()) {
                if (status.value == value) {
                    mTaskStatus = status;
                    break;
                }
            }

            return mTaskStatus == null ? NONE : mTaskStatus;
        }

        public static int getColorRes(int status) {
            int colorID = R.color.status_started;

            for (RecurringStatus taskStatus : values()) {
                if (taskStatus.value == status) {
                    colorID = taskStatus.colorResourceId;
                    break;
                }
            }

            return colorID;
        }

        public String getPassive(Context context) {
            if (backendTerminology != null && !backendTerminology.isEmpty()) {
                return backendTerminology;
            } else {
                return getMessage(context, passiveResourceId);
            }
        }

        private String getMessage(Context context, int resourceId) {
            return StorefrontCommonData.getString(context, resourceId);
        }


    }

    /**
     * Lists the different types of the Tasks
     * supported by Tookan
     */
    enum TaskType {
        NONE(-1, R.string.empty),
        PICK_UP(0, R.string.pickup_text),
        DELIVERY(1, R.string.delivery_text),
        MOBILE_WORKFORCE(2, R.string.mobile_workforce_text),
        APPOINTMENT(3, R.string.appointment_text);

        public final int value;
        public final int resourceId;

        TaskType(int value, int resourceId) {
            this.value = value;
            this.resourceId = resourceId;
        }

        /**
         * Method to retrieve TaskType via its value
         *
         * @param value
         * @return
         */
        public static TaskType getTaskByValue(int value) {
            TaskType mTaskType = null;

            for (TaskType taskType : values()) {
                if (taskType.value == value) {
                    mTaskType = taskType;
                    break;
                }
            }

            return mTaskType == null ? NONE : mTaskType;
        }

        public static int getTaskType(int value) {
            int resourceID = 0;

            for (TaskType taskType : values()) {
                if (taskType.value == value) {
                    resourceID = taskType.resourceId;
                    break;
                }
            }
            return resourceID;
        }
    }


    /**
     * Tells whether the App is in Background
     */
    enum AppStatus {
        IN_FOREGROUND,
        IN_BACKGROUND
    }


    /**
     * Specifies the Different Time Intervals
     */
    interface TimeRange {
        long LOCATION_FETCH_INTERVAL = 1000;
        long LOCATION_FASTEST_INTERVAL = 3000;
        long LOCATION_REFRESH_INTERVAL = 5000;
    }

    /**
     * Represents the TrackingData Priority values
     */
    interface LocationPriority {
        int LOW = 0;
        int BALANCED = 1;
        int BEST = 2;
    }

    /**
     * Represents Content Pages type
     */
    interface ContentPagesTypes {
        String THANKYOU_PAGE = "1";
        String SORRY_PAGE = "2";
    }

    /**
     * Represents ChargeType
     */
    interface ChargeType {
        int PERCENTAGE = 0;
        int FIXED = 1;
    }

    /**
     * Different formats of the Date
     */
    interface DateFormat {
        String STANDARD_DATE_FORMAT_NO_SEC = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d") + " HH:mm";
        String STANDARD_DATE_FORMAT_NEW = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d") + " HH:mm:ss";
        String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String END_USER_DATE_FORMAT_24 = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d") + ", HH:mm";
        String END_USER_DATE_FORMAT = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d") + ", hh:mm aa";
        String CHECKOUT_DATE_FORMAT = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
        String END_USER_DATE_FORMAT2 = "dd MMM yyyy, hh:mm aa";
        String END_USER_DATE_FORMAT3 = "MMM dd, yyyy | hh:mm aa";
        String END_USER_DATE_FORMAT4 = "MMMM dd, yyyy | hh:mm aa";
        String STANDARD_DATE_FORMAT_12 = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d") + " hh:mm aa";
        String STANDARD_DATE_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String STANDARD_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String BACKEND_PICKUP_TIME = "MM/dd/yyyy hh:mm aa";
        String TIME_FORMAT_12_without_ampm = "hh:mm";
        String TIME_FORMAT_12 = "hh:mm aa";
        String ONLY_DATE_NEW = "yyyy-MM-dd";
        String ONLY_DATE = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
        String DATE_FORMAT = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
        String DATE_FORMAT_MMM_dd = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
        String ONLY_DATE_mm_dd_yy = "MM/dd/yyyy";
        String TIME_FORMAT_24 = "HH:mm:ss";
        String TIME_FORMAT_24_WITHOUT_SECOND = "HH:mm";
        String TIME_FORMAT_24_no_seconds = "HH:mm";
        String CALENDER_FORMAT_DATE = "EEE MMM dd HH:mm:ss z yyyy";
        String DAY_OF_WEEK_WITH_DATE_ONLY = "EEE " + StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
        String DOB_DATE_FORMAT = StorefrontCommonData.getAppConfigurationData().getDate_format().replaceAll("Y", "y").replaceAll("D", "d");
    }

    /**
     * Modes that tell what mode to select while
     * navigation through google maps
     */
    interface LayoutType {
        int PICKUP_DELIVERY = 0;
        int APPOINTMENT = 1;
        int FOS = 2;
        int PICKUP = 3;
        int DELIVERY = 4;
    }

    interface ImageType {
        int BOTH = 0;
        int CAMERA_ONLY = 1;
        int GALLERY_ONLY = 2;
    }

    /**
     * Enum that tells the Current Status of the App for Login
     */
    interface RegistrationStatus {
        int VERIFIED = 1;
        int OTP_PENDING = 2;
        int VERIFICATION_PENDING = 4;

        int ACKNOWLEDGMENT_PENDING = 8;
        int OTP_VERIFIED = 3;
        int RESUBMIT_VERIFICATION = 6;
        int REJECTED = 5;
    }

    interface TransactionStatus {
        int ORDER_COMPlETE = 1;  // when order creation successfully
        int TRANSACTION_COMPLETE = 2; // when transaction for payment successfull
        int INIT = 3;   // when only init payment api hit successfull  but transaction incomplete
        int PAYMENT_FAILED_REFUNDED = 4; //
        int TRANSACTION_INCOMPLETE = 5; // when transaction is incomplete
        int TRANSACTION_REFUNDED = 6; // when transaction is refunded
        int TRANSACTION_Failed = 7; // when transaction is Failed
        int TRANSACTION_PARTIAL = 8; // when it is edit order and remaining amount is there
        int TRANSACTION_PARTIAL_REFUND = 9; // when refund amount is not equal to order amount
        int TRANSACTION_ON_HOLD = 10;  //in case of hold payment flow for payfort
    }

    interface OnboardingBusinessType {
        int CATERING = 804;
        int LAUNDRY = 805;
    }

    interface MapPlanType {
        int STANDARD = 1;
        int PREMIUM = 2;
    }


    interface ProductAddedInCart {
        int MULTI_PRODUCT = 1;
        int SINGLE_PRODUCT = 2;
    }


    interface BusinessType {
        int PRODUCTS_BUSINESS_TYPE = 1;
        int SERVICES_BUSINESS_TYPE = 2;
    }

    interface ServiceFlow {
        int PICKUP_DELIVERY = 1;
        int APPOINTMENT = 2;
        int SERVICE_AS_PRODUCT = 3;
    }

    interface SelectedPickupMode {
        int NONE = 0;
        int HOME_DELIVERY = 1;
        int SELF_PICKUP = 2;
        int PICK_AND_DROP = 3;
    }

    interface DeliveryMode {

        int HOME_DELIVERY = 2;
        int SELF_PICKUP = 4;
        int PICK_AND_DROP = 8;

    }

    interface MapConstants {
        int GOOGLEMAP = 2;
        int FLIGHTMAP = 0;
    }

    interface CustomOrderFields {
        int HIDDEN = 0;
        int MANDATORY = 1;
        int OPTIONAL = 2;
    }

    interface LaundryTaskType {

        int PICKUP_TASK = 1;
        int DELIVERY_TASK = 2;

    }

    interface TipType {
        int PERCENTAGE = 1;
        int AMOUNT = 2;
    }


    interface PAYMENT_MODES {
        int CARD = 1;
        int WEBVIEW = 2;
        int CASHMETHOD = 3;
        int WALLET = 4;
        int PAYLATER = 5;
        int paytm = 6;
    }


    interface PromoMode {
        int PUBLIC = 0;
        int HIDDEN = 1;
        int AUTO_APPLY = 2;
    }

    interface PromotionOn {
        int SUBTOTAL = 0;
        int DELIVERY_CHARGE = 1;
    }

    /**
     * Tells the Notification Statuses according to the Flag
     */
    interface NotificationFlags {
        int JOB_ASSIGN = 1;
        int REASSIGN = 2;
        int TASK_REMINDER = 3;
        int JOB_DELETED = 4;
        int BUILD_UPDATE = 5;
        int TASK_UPDATE = 6;
        int FLEET_STATUS_CHANGED = 7;
        int SILENT_REFRESH = 8;
        int WAKEFUL_NOTIFICATION = 9;
        int RESTART_LOCATION_SERVICES = 10;
        int MARK_ONLINE = 11;
        int JOB_SUCCESS = 12;
        int USER_DEBT_PENDING = 68;
        int RULE_ACCEPTED = 69;
        int RULE_REJECTED = 70;
        int RECURRING_TASK_CREATION_FAIL = 71;
        int RULE_CREATED = 72;
        int ORDER_FAILURE = 82;
        int ORDER_ASSIGNED = 44;
        int PROCESSED = 45;
        int PICKED_UP = 46;

    }

    interface BroadcastFilters {
        String CHANGE_PASSWORD = "from_change_password";
        String WISHLIST_CHANGE = "WISHLIST_CHANGE";
    }

    interface GoogleApiResultStatus {
        String OK = "OK";
        String ZERO_RESULTS = "ZERO_RESULTS";
        String REQUEST_DENIED = "REQUEST_DENIED";
    }

    interface RegistrationProcess {
        String PHONE_VERIFICATION = "PHONE_VERIFICATION";
        String EMAIL_VERIFICATION = "EMAIL_VERIFICATION";
        String SIGNUP_TEMPLATE = "SIGNUP_TEMPLATE";
        String SIGNUP_FEE = "SIGNUP_FEE";
    }

    interface ServiceTimeConstants {
        int FIXED = 1;
        int MINUTES = 2;
        int HOURS = 3;
        int DAYS = 4;
        int WEEKS = 5;
        int MONTHS = 6;
        int YEARS = 7;
        int KILOGRAM = 8;
        int POUND = 9;
        int PERSON = 10;
        int KILOMETER = 11;
        int FEET = 12;
        int SQUARE_FEET = 13;
        int LINEAR_FEET = 14;
        int PER_SQM = 15;
        int METRE = 16;
    }

    interface SignupFields {
        int EMAIL_ONLY = 0;
        int PHONE_ONLY = 1;
        int EMAIL_PHONE_BOTH = 2;
    }


    interface TaskTypeConstants {
        int FOOD = 0;
        int PICKUP = 1;
        int DELIVERY = 2;
    }

    /**
     * Modes that tell what type of currency app supports
     */
    interface CurrencyFormat {
        int DEFAULT = 1;
        int COMMA_SEP = 2;
        int DOT_SEP = 3;
        int QUOTE_SEP = 4;
    }


    interface RecurringType {
        int DATE = 1;
        int OCCURENCE_COUNT = 2;
    }

    /**
     * get payment url task type Constants
     */
    interface PaymenetTaskType {
        String FOOD = "1";
        String CUSTOM_ORDER = "2";
        String LAUNDARY = "3";
        String FREELANCER = "4";
        String LAUNDRY_CUSTOM_ORDER = "5";
        String RECURRING_TASk = "6";
        String CREATE_CHARGE = "7";
        String CUSTOM_QUOTATION_ORDER = "9";
        String REWARD = "10";
    }

}
