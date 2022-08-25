package com.tookancustomer.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import com.tookancustomer.adapters.FreshSortingAdapter;
import com.tookancustomer.models.SortResponseModel;
import com.tookancustomer.R;

import java.util.ArrayList;


/**
 * Created by shankar on 23/04/17.
 */

public class FreshSortDialog extends Dialog {

	private View viewClicked;
	private FreshSortingAdapter freshSortingAdapter;

	public <T extends FreshSortingAdapter.Callback> FreshSortDialog(@NonNull final T dialogCallback1, @StyleRes int themeResId, Activity context, ArrayList<SortResponseModel> sortList) {
		super(context, themeResId);
		setContentView(R.layout.storefront_dialog_fresh_sort);
		RecyclerView rvSortOptions = findViewById(R.id.rvSortOptions);
		rvSortOptions.setLayoutManager(new LinearLayoutManager(context));
		rvSortOptions.setItemAnimator(new DefaultItemAnimator());
		freshSortingAdapter = new FreshSortingAdapter(context, sortList, rvSortOptions, dialogCallback1);
		rvSortOptions.setAdapter(freshSortingAdapter);

		Window window = getWindow();
		WindowManager.LayoutParams wlp = getWindow().getAttributes();
		wlp.gravity = Gravity.START | Gravity.TOP;
		wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//to avoid black backgorund during animation
		window.setAttributes(wlp);
	}

	public void show(View viewClicked) {
		this.viewClicked = viewClicked;
		animateEnterDialog();
		super.show();
	}

	@Override
	public void show() {
	}

	@Override
	public void dismiss() {
		if (viewClicked != null) {
			int[] openingViewLocation = new int[2];
			viewClicked.getLocationOnScreen(openingViewLocation);
			getWindow().getDecorView().setPivotX(openingViewLocation[0] + viewClicked.getMeasuredWidth() / 2);
			getWindow().getDecorView().setPivotY(viewClicked.getY());
			getWindow().getDecorView().animate()
					.scaleX(0.0f)
					.scaleY(0.0f)
					.setDuration(150)
					.setInterpolator(new AccelerateInterpolator())
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							if (getWindow().getDecorView() != null) {
								FreshSortDialog.super.dismiss();


							}
							viewClicked = null;
						}
					});

		} else {
			super.dismiss();
		}
	}

	private void animateEnterDialog() {
		if (viewClicked != null) {
			int[] openingViewLocation = new int[2];
			viewClicked.getLocationOnScreen(openingViewLocation);
			WindowManager.LayoutParams wlp = getWindow().getAttributes();
			wlp.x = openingViewLocation[0];
			wlp.y = openingViewLocation[1] + viewClicked.getHeight();
			getWindow().getDecorView().setPivotX(openingViewLocation[0] + viewClicked.getMeasuredWidth() / 2);
			getWindow().getDecorView().setPivotY(viewClicked.getY());
			getWindow().getDecorView().setScaleX(0.0f);
			getWindow().getDecorView().setScaleY(0.0f);
			getWindow().setAttributes(wlp);
			getWindow().getDecorView().
					animate().
					scaleX(1f).
					scaleY(1f).
					setDuration(150);
		}
	}


}