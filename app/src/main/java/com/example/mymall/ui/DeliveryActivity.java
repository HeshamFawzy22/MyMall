package com.example.mymall.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapters.CartAdapter;
import com.example.mymall.config.Config;
import com.example.mymall.database.MyDatabase;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.ui.MyCartFragment.selectedAddress;
import static com.example.mymall.ui.ProductDetailsActivity.loadingDialog;
import static com.example.mymall.ui.ProductDetailsActivity.showProgressDialog;

public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // sand box because we are on test
            .clientId(Config.PAYPAL_CLIENT_ID);
    private boolean successResponse = false;
    private boolean allProductsAvailable = true;
    public static boolean getQtyIds = true;
    public static boolean fromCart;


    public static final int SELECTED_ADDRESS = 0;
    public static List<CartItemModel> cartItemModelList;

    protected Toolbar toolbar;
    protected RecyclerView deliveryRecyclerView;
    protected Button changeOrAddAddressBtn;
    protected Button delivery_continue_btn;
    protected TextView totalCartAmount;
    protected TextView fullName;
    protected TextView address;
    protected TextView pincode;

    protected ConstraintLayout orderConfirmationLayout;
    protected TextView orderId;
    protected ImageView continueShoppingBtn;


    //Declare
    private LinearLayoutManager linearLayoutManager;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_delivery);

        // start paypal service
        Intent intent = new Intent(this , PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION , config);
        startService(intent);

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getQtyIds = true;

        setCartItemModelList();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getQtyIds) {
            //////////// Accessing Quantity
            for (int i = 0; i < cartItemModelList.size() - 1; i++) {
                for (int y = 0 ; y < cartItemModelList.get(i).getProductQuantity() ; y++){
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                    Map<String , Object> timestamp = new HashMap<>();
                    timestamp.put("time" , FieldValue.serverTimestamp());
                    final int finalI = i;
                    final int finalY = y;
                    MyDatabase.getProductsReferences()
                            .document(cartItemModelList.get(i).getProductId())
                            .collection(MyDatabase.QUANTITY_BRANCH)
                            .document(quantityDocumentName)
                            .set(timestamp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //add id in my List Ids(QtyIds)
                                    cartItemModelList.get(finalI).getQtyIds().add(quantityDocumentName);

                                    if (cartItemModelList.get(finalI).getProductQuantity() == finalY + 1){
                                        MyDatabase.getProductsReferences()
                                                .document(cartItemModelList.get(finalI).getProductId())
                                                .collection(MyDatabase.QUANTITY_BRANCH)
                                                .orderBy("time" , Query.Direction.ASCENDING)
                                                .limit(cartItemModelList.get(finalI).getStockQuantity())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            List<String> serverQuantity = new ArrayList<>();
                                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                                serverQuantity.add(documentSnapshot.getId());
                                                            }
                                                            for (String qtyId : cartItemModelList.get(finalI).getQtyIds()){
                                                                if (!serverQuantity.contains(qtyId)){
                                                                    Toast.makeText(DeliveryActivity.this, "Sorry! all products may not be available in required quantity..", Toast.LENGTH_SHORT).show();
                                                                    allProductsAvailable = false;
                                                                }
                                                                if (serverQuantity.size() >= cartItemModelList.get(finalI).getStockQuantity()){
                                                                    MyDatabase.getProductsReferences()
                                                                            .document(cartItemModelList.get(finalI).getProductId())
                                                                            .update("in_stock" , false);
                                                                }
                                                            }
                                                        }else {
                                                            Toast.makeText(DeliveryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
                /*final int finalI = i;
                MyDatabase.getProductsReferences()
                        .document(ProductDetailsActivity.cartItemModelList.get(i).getProductId())
                        .collection(MyDatabase.QUANTITY_BRANCH)
                        .orderBy("available", Query.Direction.DESCENDING)
                        .limit(cartItemModelList.get(i).getProductQuantity())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if ((boolean) documentSnapshot.get("available")) {
                                            MyDatabase.getProductsReferences()
                                                    .document(cartItemModelList.get(finalI).getProductId())
                                                    .collection(MyDatabase.QUANTITY_BRANCH)
                                                    .document(documentSnapshot.getId())
                                                    .update("available", false)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                cartItemModelList.get(finalI).getQtyIds().add(documentSnapshot.getId());
                                                            } else {
                                                                //Error
                                                                Toast.makeText(DeliveryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            //Not Available
                                            allProductsAvailable = false;
                                            Toast.makeText(DeliveryActivity.this, "All products may not be available at required quantity!", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } else {
                                    //error
                                    Toast.makeText(DeliveryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/
            }
        }else {
            getQtyIds = true;
        }
        //////////// Accessing Quantity
        setUserData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIds) {
            for (int i = 0; i < cartItemModelList.size() - 1; i++) {
                if (!successResponse) {
                    for (final String qtyId : cartItemModelList.get(i).getQtyIds()) {
                        final int finalI = i;
                        MyDatabase.getProductsReferences()
                                .document(cartItemModelList.get(i).getProductId())
                                .collection(MyDatabase.QUANTITY_BRANCH)
                                .document(qtyId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyId.equals(cartItemModelList.get(finalI).getQtyIds().get(cartItemModelList.get(finalI).getQtyIds().size() - 1))){
                                            cartItemModelList.get(finalI).getQtyIds().clear();
                                            MyDatabase.getProductsReferences()
                                                    .document(cartItemModelList.get(finalI).getProductId())
                                                    .collection(MyDatabase.QUANTITY_BRANCH)
                                                    .orderBy("time" , Query.Direction.ASCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()){
                                                                if (task.getResult().getDocuments().size() < cartItemModelList.get(finalI).getStockQuantity()){
                                                                    MyDatabase.getProductsReferences()
                                                                            .document(cartItemModelList.get(finalI).getProductId())
                                                                            .update("in_stock" , true);

                                                                }
                                                            }else {
                                                                Toast.makeText(DeliveryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }else {
                    cartItemModelList.get(i).getQtyIds().clear();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this , PayPalService.class));
        super.onDestroy();
    }

    private void setCartItemModelList() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(cartItemModelList, totalCartAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    private void setUserData() {
        fullName.setText(AddAddressActivity.addressesModelList.get(selectedAddress).getName() + " - " + AddAddressActivity.addressesModelList.get(selectedAddress).getMobileNo());
        address.setText(AddAddressActivity.addressesModelList.get(selectedAddress).getAddress());
        pincode.setText(AddAddressActivity.addressesModelList.get(selectedAddress).getPincode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_or_add_address_btn) {
            getQtyIds = false;
            Intent intent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
            intent.putExtra("MODE", SELECTED_ADDRESS);
            startActivity(intent);

        } else if (view.getId() == R.id.delivery_continue_btn) {
//            showPaymentDialog(this);
            if (allProductsAvailable) {
                paymentProcess();
            }
        }
//        else if (view.getId() == R.id.paytm){
//            paymentMethodDialog.dismiss();
//            showProgressDialog(this);
//            //Using the code below, you can get runtime permissions needed from user to auto-read the OTP
//            if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
//            }
//
//        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void showPaymentDialog(Context context){
//        paymentMethodDialog = new Dialog(context);
//        paymentMethodDialog.setContentView(R.layout.payment_method);
//        paymentMethodDialog.setCancelable(true);
//        paymentMethodDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.slider_background));
//        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paytem = paymentMethodDialog.findViewById(R.id.paytm);
//        paymentMethodDialog.show();
//    }

    private void paymentProcess() {
        String totalAmount = String.valueOf(CartAdapter.totalAmount);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalAmount) , "USD" ,
                "Total Amount" , PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this , com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION , config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT , payPalPayment);
        startActivityForResult(intent , PAYPAL_REQUEST_CODE);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showConfirmationLayout(data);
            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                orderConfirmationLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            orderConfirmationLayout.setVisibility(View.GONE);
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showConfirmationLayout(Intent data) {
        PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
        if (paymentConfirmation != null) {
//            try {
                successResponse = true;
                getQtyIds = false;
                for (int i = 0 ; i < cartItemModelList.size() - 1 ; i++){
                    for (String qtyId : cartItemModelList.get(i).getQtyIds()){
                        MyDatabase.getProductsReferences()
                                .document(cartItemModelList.get(i).getProductId())
                                .collection(MyDatabase.QUANTITY_BRANCH)
                                .document(qtyId)
                                .update("user_ID" , FirebaseAuth.getInstance().getUid());

                    }
                }
                if (MainActivity.mainActivity != null){
                    MainActivity.mainActivity.finish();
                    MainActivity.mainActivity = null;
                    MainActivity.showCart = false;
                }else {
                    MainActivity.resetMainActivity = true;
                }
                if (ProductDetailsActivity.productDetailsActivity != null){
                    ProductDetailsActivity.productDetailsActivity.finish();
                    ProductDetailsActivity.productDetailsActivity = null;
                }
                if (fromCart){
                    showProgressDialog(this);
                    Map<String , Object> updateCartList = new HashMap<>();
                    final List<Integer> indexList = new ArrayList<>();
                    long cartListSize = 0;
                    for (int i = 0 ; i < ProductDetailsActivity.cartList.size() ; i++){
                        if (!cartItemModelList.get(i).isInStock()){
                            updateCartList.put("product_ID_" + cartListSize , cartItemModelList.get(i).getProductId());
                            cartListSize++;
                        }else {
                            indexList.add(i);
                        }
                    }
                    updateCartList.put("list_size" , cartListSize);
                    UserDao.updateUserCartList(FirebaseAuth.getInstance().getUid(), updateCartList, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                for (int i = 0 ; i < indexList.size() ; i++){
                                    ProductDetailsActivity.cartList.remove(indexList.get(i).intValue());
                                    ProductDetailsActivity.cartItemModelList.remove(indexList.get(i).intValue());
                                    ProductDetailsActivity.cartItemModelList.remove(ProductDetailsActivity.cartItemModelList.size() - 1);
                                }
                            }else {
                                Toast.makeText(DeliveryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }
//                String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
//                JSONObject jsonObject = new JSONObject(paymentDetails);
//                String orderIdTxt = jsonObject.getJSONObject("response").getString("id");
                String orderIdTxt = UUID.randomUUID().toString().substring(0 , 20);
                delivery_continue_btn.setEnabled(false);
                changeOrAddAddressBtn.setEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                orderConfirmationLayout.setVisibility(View.VISIBLE);
                orderId.setText(orderIdTxt);
                continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else {
            orderConfirmationLayout.setVisibility(View.GONE);
            Toast.makeText(this, "payment Confirmation is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse){
            finish();
        }
        super.onBackPressed();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        deliveryRecyclerView = (RecyclerView) findViewById(R.id.delivery_recyclerView);
        changeOrAddAddressBtn = (Button) findViewById(R.id.change_or_add_address_btn);
        changeOrAddAddressBtn.setOnClickListener(DeliveryActivity.this);
        delivery_continue_btn = (Button) findViewById(R.id.delivery_continue_btn);
        delivery_continue_btn.setOnClickListener(DeliveryActivity.this);
        changeOrAddAddressBtn.setVisibility(View.VISIBLE);
        totalCartAmount = findViewById(R.id.total_cart_amount);
        fullName = (TextView) findViewById(R.id.address_full_name);
        address = (TextView) findViewById(R.id.address);
        pincode = (TextView) findViewById(R.id.address_pincode);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        orderId = findViewById(R.id.order_id);
        continueShoppingBtn = findViewById(R.id.continue_shopping);
    }
}