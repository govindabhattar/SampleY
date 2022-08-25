package com.tookancustomer.mapfiles;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sarthak on 29/12/15.
 */
public interface OnSearchResultSelected {
    void moveMapWithSearchResult(final LatLng searchLatLng);
}
