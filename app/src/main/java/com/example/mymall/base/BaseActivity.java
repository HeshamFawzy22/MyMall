package com.example.mymall.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public AlertDialog showMessage(String message ,
                                   String positiveMsg , String negativeMsg ,
                                   DialogInterface.OnClickListener onClickListenerPositive,
                                   DialogInterface.OnClickListener onClickListenerNegative){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveMsg,onClickListenerPositive)
                .setNegativeButton(negativeMsg,onClickListenerNegative)
                .setTitle("Warning!")
                .setCancelable(false);
        return builder.show();
    }
    public AlertDialog showMessage(int message ,
                                   int positiveMsg , int negativeMsg ,
                                   DialogInterface.OnClickListener onClickListenerPositive,
                                   DialogInterface.OnClickListener onClickListenerNegative){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveMsg,onClickListenerPositive)
                .setNegativeButton(negativeMsg,onClickListenerNegative)
                .setTitle("Warning!")
                .setCancelable(false);
        return builder.show();
    }
    public AlertDialog showMessage(int message , int positiveMsg, DialogInterface.OnClickListener onClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveMsg,onClickListener)
                .setCancelable(false)
                .setTitle("Warning!");
        return builder.show();
    }
    public AlertDialog showMessage(String message, String posActionName,
                                   DialogInterface.OnClickListener onClickListener,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(int message, int posActionName,
                                   DialogInterface.OnClickListener onClickListener,
                                   boolean isCancelable
    ){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener);
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(String message,String posActionName){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }
    public AlertDialog showMessage(int message,int posActionName){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }
    ProgressDialog progressDialog;
    public void showProgressDialog(String message){
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void hideProgressDialog(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
//    public void onBackPressed() {
//        showMessage(R.string.do_you_want_to_exit, R.string.ok, R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//    }

}
