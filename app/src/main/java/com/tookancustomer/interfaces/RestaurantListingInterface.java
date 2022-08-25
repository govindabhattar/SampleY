package com.tookancustomer.interfaces;

import com.tookancustomer.models.bannersData.Banner;

/**
 * Created by neerajwadhwani on 10/10/18.
 */

public interface RestaurantListingInterface {
    void redirectToMerchant(Integer merchantId, int position, Banner banner);
}
