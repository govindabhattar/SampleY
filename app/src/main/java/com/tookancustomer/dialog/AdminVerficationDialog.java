package com.tookancustomer.dialog;

import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Utils;

public class AdminVerficationDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvReview, tvApprovalMail;
    private Button btnOk;
    private static AdminVerficationDialog dialog;
    private static AdminVerificationDialogListener listener;

    public static AdminVerficationDialog getInstance(AdminVerificationDialogListener listener1) {
        listener = listener1;
        if (dialog == null) {
            dialog = new AdminVerficationDialog();
            dialog.setCancelable(false);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_admin_verification, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        tvReview = view.findViewById(R.id.tvReview);
        tvApprovalMail = view.findViewById(R.id.tvApprovalMail);
        btnOk = view.findViewById(R.id.btnOk);

        setData();
        setClickListener();
    }

    private void setData() {
        tvReview.setText(StorefrontCommonData.getString(tvReview.getContext(), R.string.account_under_review_text));
        tvApprovalMail.setText(StorefrontCommonData.getString(tvApprovalMail.getContext(), R.string.confirmation_email_text));
        btnOk.setText(StorefrontCommonData.getString(btnOk.getContext(), R.string.ok_text));
    }

    private void setClickListener() {
        Utils.setOnClickListener(this, btnOk);
    }

    @Override
    public void onStart() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (listener != null)
                    listener.onAcknowledge();
                break;
        }
    }


    public interface AdminVerificationDialogListener {
        void onAcknowledge();
    }
}
