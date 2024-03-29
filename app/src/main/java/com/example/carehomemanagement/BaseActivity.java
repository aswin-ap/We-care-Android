package com.example.carehomemanagement;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.utils.onDialogYesClick;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class BaseActivity extends AppCompatActivity {

    // This is for showing the progress
    Dialog mLoadingDialog;
    onDialogYesClick yesClick;


    public void showLoading(Context context) {
        if (mLoadingDialog == null) {
            callLoader(context);
        } else {
            hideLoading();
            callLoader(context);
        }
    }

    private void callLoader(Context context) {
        mLoadingDialog = new Dialog(context);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading_layout);
        mLoadingDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        mLoadingDialog.show();
    }

    // This is for hiding the progress
    public void hideLoading() {
        if (mLoadingDialog != null)
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
                mLoadingDialog.getWindow().closeAllPanels();
            }
    }


    public void showSnackBar(View snackBarView, String message) {
        Snackbar sb;
        sb = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        sb.show();
    }

    public FirebaseFirestore getFireStoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public String currentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm aa");
        String formattedDate = dateFormat.format(new Date());
        return formattedDate;
    }

    public String currentDateWithText() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(cal.getTimeInMillis()));
        } catch (Exception e) {
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }


}
