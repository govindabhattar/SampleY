package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;

public class Terminology implements Serializable {

    @SerializedName("VEG_ONLY")
    @Expose
    private String vegOnly;
    @SerializedName("FOOD_READY")
    @Expose
    private String foodReady;
    @SerializedName("ORDER_ONLINE")
    @Expose
    private String orderOnline;
    @SerializedName("TRANSACTION_CHARGE")
    @Expose
    private String transactionCharge;
    @SerializedName("PICKUP_AND_DROP")
    @Expose
    private String pickupAndDrop;
    @SerializedName("CATALOGUE")
    @Expose
    private String catalogue;
    @SerializedName("PRODUCT")
    @Expose
    private String product;
    @SerializedName("PRODUCTS")
    @Expose
    private String products;
    @SerializedName("CATEGORY")
    @Expose
    private String category;
    @SerializedName("ORDERS")
    @Expose
    private String orders;
    @SerializedName("ORDER")
    @Expose
    private String order;
    @SerializedName("MERCHANT")
    @Expose
    private String merchant;
    @SerializedName("MERCHANTS")
    @Expose
    private String merchants;
    @SerializedName("CUSTOMER")
    @Expose
    private String customer;
    @SerializedName("STORE")
    @Expose
    private String store;
    @SerializedName("TOOKAN")
    @Expose
    private String tookan;
    @SerializedName("HIPPO")
    @Expose
    private String hippo;
    @SerializedName("PICKUP")
    @Expose
    private String pickup;
    @SerializedName("DELIVERY")
    @Expose
    private String delivery;
    @SerializedName("DELIVERY_CHARGE")
    @Expose
    private String deliveryCharge;
    @SerializedName("SCHEDULING")
    @Expose
    private String scheduling;
    @SerializedName("VARIANTS")
    @Expose
    private String variants;
    @SerializedName("INVENTORY")
    @Expose
    private String inventory;
    @SerializedName("SERVING")
    @Expose
    private String serving;
    @SerializedName("ITEM")
    @Expose
    private String item;
    @SerializedName("ITEMS")
    @Expose
    private String items;
    @SerializedName("START_TIME")
    @Expose
    private String startTime;
    @SerializedName("END_TIME")
    @Expose
    private String endTime;
    @SerializedName("CART")
    @Expose
    private String cart;
    @SerializedName("SCHEDULE_ORDER")
    @Expose
    private String scheduleOrder;
    @SerializedName("NOTES")
    @Expose
    private String notes;
    @SerializedName("PROCEED_TO_PAY")
    @Expose
    private String proceedToPay;
    @SerializedName("CHECKOUT")
    @Expose
    private String checkout;
    @SerializedName("PAYMENT_METHOD")
    @Expose
    private String paymentMethod;
    @SerializedName("BILL_SUMMARY")
    @Expose
    private String billSummary;
    @SerializedName("PAY")
    @Expose
    private String pay;
    @SerializedName("PAYMENT")
    @Expose
    private String payment;
    @SerializedName("NOTIFICATIONS")
    @Expose
    private String notifications;
    @SerializedName("DELIVERY_FROM")
    @Expose
    private String deliveryFrom;
    @SerializedName("DELIVER_TO")
    @Expose
    private String deliveryTo;
    @SerializedName("PROJECT")
    @Expose
    private String project;
    @SerializedName("ORDERED")
    @Expose
    private String ordered;
    @SerializedName("PENDING")
    @Expose
    private String pending;
    @SerializedName("ACCEPTED")
    @Expose
    private String accepted;
    @SerializedName("REJECTED")
    @Expose
    private String rejected;
    @SerializedName("CANCELLED")
    @Expose
    private String cancelled;
    @SerializedName("COMPLETED")
    @Expose
    private String completed;
    @SerializedName("DISPATCHED")
    @Expose
    private String dispatched;
    @SerializedName("JOB_ASSIGNED")
    @Expose
    private String jobassigned;
    @SerializedName("PROCESSED")
    @Expose
    private String processed;
    @SerializedName("PICKED_UP")
    @Expose
    private String pickedup;
    @SerializedName("SPONSORED")
    @Expose
    private String sponsored;
    @SerializedName("SELF_PICKUP")
    @Expose
    private String selfPickup;
    @SerializedName("HOME_DELIVERY")
    @Expose
    private String homeDelivery;
    @SerializedName("DYNAMIC_PAGES")
    @Expose
    private String dynamicPages;
    @SerializedName("CHECKOUT_TEMPLATE")
    @Expose
    private String checkoutTemplate;
    @SerializedName("LOYALTY_POINTS")
    @Expose
    private String loyaltyPoints;
    @SerializedName("CUSTOM_ORDER")
    @Expose
    private String customOrder;
    @SerializedName("CUSTOM")
    @Expose
    private String custom;
    @SerializedName("ANY_WHERE")
    @Expose
    private String anywhere;
    @SerializedName("ORDER_REMARKS")
    @Expose
    private String orderRemarks;
    @SerializedName("GO_TO_MARKETPLACE")
    @Expose
    private String goToMarketplace;
    @SerializedName("WALLET")
    @Expose
    private String wallet;
    @SerializedName("GIFT_CARD")
    @Expose
    private String giftCard;
    @SerializedName("BUY")
    @Expose
    private String buy;
    @SerializedName("REDEEM")
    @Expose
    private String redeem;
    @SerializedName("PICKUP_FROM")
    @Expose
    private String pickupFrom;
    @SerializedName("REFERRAL")
    @Expose
    private String referral;
    @SerializedName("HOME_ADDRESS")
    @Expose
    private String homeAddress;
    @SerializedName("YOUR_ORDER_DESCRIPTION")
    @Expose
    private String yourOrderDescription;
    @SerializedName("ENTER_DESCRIPTION_HERE")
    @Expose
    private String enterDescriptionHere;
    @SerializedName("TIP")
    @Expose
    private String tip;
    @SerializedName("PAY_LATER")
    @Expose
    private String payLater;
    @SerializedName("REWARDS")
    @Expose
    private String rewards;
    @SerializedName("STORE_CLOSED")
    @Expose
    private String storeClosedMessage;
    @SerializedName("OUT_OF_STOCK")
    @Expose
    private String outOfStock;
    @SerializedName("AGENT")
    @Expose
    private String agent;
    @SerializedName("SUBSCRIPTION_AVAILABLE")
    @Expose
    private String subscriptionsAvailable;
    @SerializedName("SUBSCRIBE")
    @Expose
    private String subscribe;
    @SerializedName("SUBSCRIPTIONS")
    @Expose
    private String subscriptions;
    @SerializedName("APARTMENT_NO")
    @Expose
    private String apartmentNo;
    @SerializedName("ADDRESS")
    @Expose
    private String address;
    @SerializedName("QR_CODE")
    @Expose
    private String qrcode;
    @SerializedName("POSTAL_CODE")
    @Expose
    private String postalCode;
    @SerializedName("LANDMARK")
    @Expose
    private String landmark;
    @SerializedName("SURGE")
    @Expose
    private String surge;
    @SerializedName("ANNOUNCEMENTS")
    @Expose
    private String announcements;
    @SerializedName("REORDER")
    @Expose
    private String reorder;
    @SerializedName("EDIT_ORDER")
    @Expose
    private String editOrder;
    @SerializedName("SURGE_APPLIED")
    @Expose
    private String surgeApplied;
    @SerializedName("READY_TO_PLACE_YOUR_ORDER")
    @Expose
    private String READY_TO_PLACE_YOUR_ORDER;
    @SerializedName("PAY_ON_DELIVERY")
    @Expose
    private String payOnDelivery;

    public String getPayOnDelivery() {
        return payOnDelivery != null ? payOnDelivery : "Pay On Delivery";
    }

    public void setPayOnDelivery(String payOnDelivery) {
        this.payOnDelivery = payOnDelivery;
    }

    public String getFoodReady() {
        return foodReady != null ? foodReady : "Food Ready";
    }

    public void setFoodReady(String foodReady) {
        this.foodReady = foodReady;
    }

    public String getQrcode() {
        return qrcode != null ? qrcode : "QR Code";
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getREADY_TO_PLACE_YOUR_ORDER() {
        if (READY_TO_PLACE_YOUR_ORDER != null && !READY_TO_PLACE_YOUR_ORDER.isEmpty())
            return READY_TO_PLACE_YOUR_ORDER;
        else
            return "Ready to Place Your Order With ";
    }

    public void setREADY_TO_PLACE_YOUR_ORDER(String READY_TO_PLACE_YOUR_ORDER) {
        this.READY_TO_PLACE_YOUR_ORDER = READY_TO_PLACE_YOUR_ORDER;
    }

    public String getJobassigned() {
        return jobassigned;
    }

    public void setJobassigned(String jobassigned) {
        this.jobassigned = jobassigned;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getPickedup() {
        return pickedup;
    }

    public void setPickedup(String pickedup) {
        this.pickedup = pickedup;
    }

    public String getAgent() {
        return agent != null ? agent : "Agent";
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAnnouncements() {
        return announcements != null ? announcements : "Announcements";
    }

    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    public String getSurge() {
        return surge;
    }

    public void setSurge(String surge) {
        this.surge = surge;
    }

    public String getSurgeApplied() {
        return surgeApplied;
    }

    public void setSurgeApplied(String surgeApplied) {
        this.surgeApplied = surgeApplied;
    }

    public String getApartmentNo() {
        return apartmentNo != null ? apartmentNo : "Apartment No";
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getAddress() {
        return address != null ? address : "Address";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode != null ? postalCode : "Postal Code";
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLandmark() {
        return landmark != null ? landmark : "landmark";
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getSubscriptions() {
        return subscriptions;
    }

    public String getSubscribe() {
        return subscribe != null ? subscribe : "Subscribe";
    }

    public String getSubscriptionsAvailable() {
        return subscriptionsAvailable != null ? subscriptionsAvailable : "Subscription Available";
    }


    public String getOrderRemarks() {
        return orderRemarks != null ? orderRemarks : "Any Suggestions?";
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints != null ? loyaltyPoints : "Loyalty Points";
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getReorder() {
        return reorder != null ? reorder : "Reorder";
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }

    public String getDynamicPages() {
        return dynamicPages != null ? dynamicPages : "Dynamic Pages";
    }

    public void setDynamicPages(String dynamicPages) {
        this.dynamicPages = dynamicPages;
    }

    public String getSelfPickup() {
        return selfPickup != null ? selfPickup : "Take Away";
    }

    public void setSelfPickup(String selfPickup) {
        this.selfPickup = selfPickup;
    }

    public String getHomeDelivery() {
        return homeDelivery != null ? homeDelivery : "Home Delivery";
    }

    public void setHomeDelivery(String homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    public String getTransactionCharge() {
        return transactionCharge;
    }

    public String getProducts() {
        return products != null ? products : "Products";
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getCatalogue() {
        return catalogue != null ? catalogue : "";
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public String getProduct() {
        return product != null ? product : "Product";
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct(boolean isCaps) {
        if (isCaps)
            return product != null ? Utils.capitaliseWords(product) : "Product";
        else
            return product != null ? Utils.convertToLowerCase(product) : "product";
    }

    public String getDeliveryFrom(boolean isCaps) {
        if (isCaps)
            return deliveryFrom != null ? Utils.capitaliseWords(deliveryFrom) : "Delivery From";
        else
            return deliveryFrom != null ? Utils.convertToLowerCase(deliveryFrom) : "delivery from";
    }

    public String getDeliveryTo(boolean isCaps) {
        if (isCaps)
            return deliveryTo != null ? Utils.capitaliseWords(deliveryTo) : "Delivery To";
        else
            return deliveryTo != null ? Utils.convertToLowerCase(deliveryTo) : "delivery to";
    }

    public String getCategory() {
        return category != null ? category : "";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrders() {
        return orders != null ? orders : "";
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getOrder() {
        return order != null ? order : "";
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMerchant() {
        return merchant != null ? merchant : "";
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchants() {
        return merchants != null ? merchants : "";
    }

    public void setMerchants(String merchants) {
        this.merchants = merchants;
    }

    public String getCustomer(boolean isCaps) {
        if (isCaps)
            return customer != null ? Utils.capitaliseWords(customer) : "Customer";
        else
            return customer != null ? Utils.convertToLowerCase(customer) : "customer";

    }

    public String getProject() {
        return project != null ? project : "My Projects";
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStore() {
        return store != null ? store : "";
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore(boolean isCaps) {
        if (isCaps)
            return store != null ? Utils.capitaliseWords(store) : "Store";
        else
            return store != null ? Utils.convertToLowerCase(store) : "Store";
    }

    public String getTookan() {
        return tookan != null ? tookan : "";
    }

    public void setTookan(String tookan) {
        this.tookan = tookan;
    }

    public String getHippo() {
        return hippo != null ? hippo : "";
    }

    public void setHippo(String hippo) {
        this.hippo = hippo;
    }

    public String getPickup(boolean isCaps) {
        if (isCaps)
            return pickup != null ? Utils.capitaliseWords(pickup) : "Pickup";
        else
            return pickup != null ? Utils.convertToLowerCase(pickup) : "pickup";
    }

    public String getPickup() {
        return pickup != null ? pickup : "pickup";
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDelivery(boolean isCaps) {
        if (isCaps)
            return delivery != null ? Utils.capitaliseWords(delivery) : "Delivery";
        else
            return delivery != null ? Utils.convertToLowerCase(delivery) : "delivery";
    }

    public String getDeliveryCharge() {
        return deliveryCharge != null ? deliveryCharge : "Delivery Charge";
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getScheduling() {
        return scheduling != null ? scheduling : "";
    }

    public void setScheduling(String scheduling) {
        this.scheduling = scheduling;
    }

    public String getVariants() {
        return variants != null ? variants : "";
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public String getInventory() {
        return inventory != null ? inventory : "";
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getServing() {
        return serving != null ? serving : "";
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getItem(boolean isCaps) {
        if (isCaps)
            return item != null ? Utils.capitaliseWords(item) : "Item(s)";
        else
            return item != null ? Utils.convertToLowerCase(item) : "item(s)";

    }

    public String getItems(boolean isCaps) {
        if (isCaps)
            return items != null ? Utils.capitaliseWords(items) : "Items";
        else
            return items != null ? Utils.convertToLowerCase(items) : "items";

    }

    public String getStartTime(boolean isCaps) {
        if (isCaps)
            return startTime != null ? Utils.capitaliseWords(startTime) : "Start Time";
        else
            return startTime != null ? Utils.convertToLowerCase(startTime) : "start time";

//        return startTime != null ? startTime : "Start Time";
    }

    public String getEndTime(boolean isCaps) {
        if (isCaps)
            return endTime != null ? Utils.capitaliseWords(endTime) : "End Time";
        else
            return endTime != null ? Utils.convertToLowerCase(endTime) : "end time";

//        return endTime != null ? endTime : "";
    }

    public String getCart(boolean isCaps) {
        if (isCaps)
            return cart != null ? Utils.capitaliseWords(cart) : "Cart";
        else
            return cart != null ? Utils.convertToLowerCase(cart) : "cart";
    }

    public String getScheduleOrder() {
        return scheduleOrder != null ? scheduleOrder : "Schedule Order";
    }

    public void setScheduleOrder(String scheduleOrder) {
        this.scheduleOrder = scheduleOrder;
    }

    public String getNotes(boolean isCaps) {
        if (isCaps)
            return notes != null ? Utils.capitaliseWords(notes) : "Notes";
        else
            return notes != null ? Utils.convertToLowerCase(notes) : "notes";
    }

    public String getProceedToPay(boolean isCaps) {
        if (isCaps)
            return proceedToPay != null ? Utils.capitaliseWords(proceedToPay) : "Proceed to Pay";
        else
            return proceedToPay != null ? Utils.convertToLowerCase(proceedToPay) : "proceed to pay";
    }

    public String getCheckout(boolean isCaps) {
        if (isCaps)
            return checkout != null ? Utils.capitaliseWords(checkout) : "Checkout";
        else
            return checkout != null ? Utils.convertToLowerCase(checkout) : "checkout";
    }

    public String getPaymentMethod() {
        return paymentMethod != null ? paymentMethod : "Payment Method";
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillSummary() {
        return billSummary != null ? billSummary : "Bill Summary";
    }

    public void setBillSummary(String billSummary) {
        this.billSummary = billSummary;
    }

    public String getPay(boolean isCaps) {
        if (isCaps)
            return pay != null ? Utils.capitaliseWords(pay) : "Pay";
        else
            return pay != null ? Utils.convertToLowerCase(pay) : "pay";
    }

    public String getPayment(boolean isCaps) {
        if (isCaps)
            return payment != null ? Utils.capitaliseWords(payment) : "Payment";
        else
            return payment != null ? Utils.convertToLowerCase(payment) : "payment";
    }

    public String getNotifications(boolean isCaps) {
        if (isCaps)
            return notifications != null ? Utils.capitaliseWords(notifications) : "Notifications";
        else
            return notifications != null ? Utils.convertToLowerCase(notifications) : "notifications";
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDelivery() {
        return delivery != null ? delivery : "Delivery";
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getProceedToPay() {
        return proceedToPay;
    }

    public void setProceedToPay(String proceedToPay) {
        this.proceedToPay = proceedToPay;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getDeliveryFrom() {
        return deliveryFrom;
    }

    public void setDeliveryFrom(String deliveryFrom) {
        this.deliveryFrom = deliveryFrom;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDispatched() {
        return dispatched;
    }

    public void setDispatched(String dispatched) {
        this.dispatched = dispatched;
    }

    public String getSponsored() {
        return sponsored != null ? sponsored : "Sponsored";
    }

    public void setSponsored(String sponsored) {
        this.sponsored = sponsored;
    }

    public String getCheckoutTemplate() {
        return checkoutTemplate != null ? checkoutTemplate : "Checkout Template";
    }

    public void setCheckoutTemplate(String checkoutTemplate) {
        this.checkoutTemplate = checkoutTemplate;
    }

    public String getCustomOrder() {
        return customOrder != null ? customOrder : "Custom Order";
    }

    public String getCustom() {
        return custom != null ? custom : "Custom";
    }

    public String getAnywhere() {
        return anywhere != null ? anywhere : "Anywhere";
    }

    public String getGoToMarketplace() {
        return goToMarketplace != null ? goToMarketplace : "Go To Marketplace";
    }

    public String getWallet() {
        return wallet != null ? wallet : "Wallet";
    }

    public String getGiftCard() {
        return giftCard != null ? giftCard : "Gift Card";
    }

    public String getBuy() {
        return buy != null ? buy : "Buy";
    }

    public String getRedeem() {
        return redeem != null ? redeem : "Redeem";
    }

    public String getPickupFrom() {
        return pickupFrom;
    }

    public String getPickupFrom(boolean isCaps) {
        if (isCaps)
            return pickupFrom != null ? Utils.capitaliseWords(pickupFrom) : "PICKUP FROM";
        else
            return pickupFrom != null ? Utils.convertToLowerCase(pickupFrom) : "Pickup from";

    }

    public String getReferral() {
        return referral;
    }

    public String getReferral(boolean isCaps) {
        if (isCaps)
            return referral != null ? Utils.capitaliseWords(referral) : "REFER & EARN";
        else
            return referral != null ? Utils.convertToLowerCase(referral) : "Refer & Earn";
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getYourOrderDescription() {
        return yourOrderDescription != null ? yourOrderDescription : "What do you want to order?";
    }

    public String getEditOrder() {
        return editOrder != null ? editOrder : "EditOrder";
    }

    public void setEditOrder(String editOrder) {
        this.editOrder = editOrder;
    }

    public String getEnterDescriptionHere() {
        return enterDescriptionHere != null ? enterDescriptionHere : "Enter description here";
    }

    public String getTip() {
        return tip != null ? tip : "tip";
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPayLater() {
        return payLater != null ? payLater : "Pay Later";
    }

    public String getRewards() {
        return rewards != null ? rewards : "Rewards";
    }

    public String getStoreClosedMessage() {
        return storeClosedMessage != null && !storeClosedMessage.isEmpty() ? storeClosedMessage : "The store is not accepting online orders currently.";
    }

    public String getOutOfStock() {
        return outOfStock != null ? outOfStock : "Out of Stock";
    }

    public String getPickupAndDrop() {
        return pickupAndDrop;
    }

    public String getVegOnly() {
        return vegOnly != null ? vegOnly : "Veg Only";
    }

    public void setVegOnly(String vegOnly) {
        this.vegOnly = vegOnly;
    }

    public String getOrderOnline() {
        return orderOnline != null ? orderOnline : "Order Online";
    }

    public void setOrderOnline(String orderOnline) {
        this.orderOnline = orderOnline;
    }

}
