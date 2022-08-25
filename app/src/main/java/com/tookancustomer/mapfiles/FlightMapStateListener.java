package com.tookancustomer.mapfiles;


import android.app.Activity;


import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.Timer;
import java.util.TimerTask;

public abstract class FlightMapStateListener {
    private static final int SETTLE_TIME = 500;
    private boolean mMapTouched = false;
    private boolean mMapSettled = false;
    private Timer mTimer;

    private MapboxMap mFlightMap;
    private com.mapbox.mapboxsdk.camera.CameraPosition mLastPositionFlightmap;
    private Activity mActivity;


    public FlightMapStateListener(MapboxMap maps, TouchableFlightMapFragment touchableMapFragment, Activity activity) {
        this.mFlightMap = maps;

        this.mActivity = activity;


        mFlightMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unsettleMap();
                if (!mMapTouched) {
                    runSettleTimer();
                }
            }
        });

        touchableMapFragment.setTouchListener(new TouchableWrapper.OnTouchListener() {
            @Override
            public void onTouch() {
                touchMap();
                unsettleMap();
            }

            @Override
            public void onRelease() {
                releaseMap();
                runSettleTimer();
            }

            @Override
            public void onDoubleTap() {
                mFlightMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomIn());
            }

            @Override
            public void onTwoFingerDoubleTap() {
                mFlightMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomOut());
            }

            @Override
            public void pinchIn() {
                mFlightMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomTo(mFlightMap.getCameraPosition().zoom - 0.04f));
            }

            @Override
            public void pinchOut() {
                mFlightMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomTo(mFlightMap.getCameraPosition().zoom + 0.04f));
            }

        });


    }

    private void updateLastPosition() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLastPositionFlightmap = FlightMapStateListener.this.mFlightMap.getCameraPosition();
            }
        });
    }

    private void runSettleTimer() {
        updateLastPosition();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        com.mapbox.mapboxsdk.camera.CameraPosition currentPosition = FlightMapStateListener.this.mFlightMap.getCameraPosition();
                        if (currentPosition.equals(mLastPositionFlightmap)) {
                            settleMap();
                        }
                    }
                });
            }
        }, SETTLE_TIME);
    }

    private synchronized void releaseMap() {
        if (mMapTouched) {
            mMapTouched = false;
            onMapReleased();
        }
    }

    private void touchMap() {
        if (!mMapTouched) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
            }
            mMapTouched = true;
            onMapTouched();
        }
    }

    private void unsettleMap() {
        if (mMapSettled) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
            }
            mMapSettled = false;
            mLastPositionFlightmap = null;
            onMapUnsettled();
        }
    }

    private void settleMap() {
        if (!mMapSettled) {
            mMapSettled = true;
            onMapSettled();
        }
    }

    protected abstract void onMapTouched();

    protected abstract void onMapReleased();

    protected abstract void onMapUnsettled();

    protected abstract void onMapSettled();
}
