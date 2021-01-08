package com.raisetech.ecalculo.activities.creation;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.raisetech.ecalculo.R;
import com.raisetech.ecalculo.adapters.creationAdapter.CustomerLedgerAdapter;
import com.raisetech.ecalculo.adapters.otherAdapters.FixedGridLayoutManager;
import com.raisetech.ecalculo.dtos.CustomerSupplierLedgerDTO;
import com.raisetech.ecalculo.utilities.APIRequest;
import com.raisetech.ecalculo.utilities.APIs;
import com.raisetech.ecalculo.utilities.AppTexts;
import com.raisetech.ecalculo.utilities.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private AddCustomerActivity activity = AddCustomerActivity.this;
    private TextView tVCancelCustomerLedger;
    private CardView cVAddIcon;
    private Spinner balanceTypeSpinnerCustomer;
    private RecyclerView rVCustomerLedger;

    private androidx.appcompat.app.AlertDialog progressDialog;

    private HorizontalScrollView scrollMain;
    private LinearLayout lLNoData;
    private RequestQueue requestQueue;
    private int scrollX = 0;
    private SharedPreferences preferences;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private CustomerLedgerAdapter customerLedgerAdapter;


    private List<CustomerSupplierLedgerDTO> customerSupplierLedgerDTOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        init();
        getCustomerLedger();
//        populateDebitCreditSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tVCancelCustomerLedger:
                onBackPressed();
                break;

            case R.id.cVAddIcon:
                View createSupplierView = inflater.inflate(R.layout.layout_create_supplier, null);
                AlertDialog.Builder supplierBuilder = new AlertDialog.Builder(activity, R.style.MaterialDialogSheet);
                supplierBuilder.setView(createSupplierView);
                TextView tVSaveCustomer = createSupplierView.findViewById(R.id.tVSaveCustomer);
                TextView tVCancelCustomer = createSupplierView.findViewById(R.id.tVCancelCustomer);

                EditText eTSupplierName = createSupplierView.findViewById(R.id.eTSupplierName);
                EditText eTContactPerson = createSupplierView.findViewById(R.id.eTContactPerson);
                EditText eTShortName = createSupplierView.findViewById(R.id.eTShortName);
                EditText eTAddress = createSupplierView.findViewById(R.id.eTAddress);
                EditText eTTelephoneNumber = createSupplierView.findViewById(R.id.eTTelephoneNumber);
                EditText eTMobileNumber = createSupplierView.findViewById(R.id.eTMobileNumber);
                EditText eTSecondaryMobileNumber = createSupplierView.findViewById(R.id.eTSecondaryMobileNumber);
                EditText eTEmail = createSupplierView.findViewById(R.id.eTEmail);
                EditText eTTPinType = createSupplierView.findViewById(R.id.eTTPinType);
                EditText eTPanVatNo = createSupplierView.findViewById(R.id.eTPanVatNo);
                EditText eTCreditDays = createSupplierView.findViewById(R.id.eTCreditDays);
                EditText eTOpeningBalance = createSupplierView.findViewById(R.id.eTOpeningBalance);
                balanceTypeSpinnerCustomer = createSupplierView.findViewById(R.id.balanceTypeSpinnerSupplier);
                populateDebitCreditSpinner();

                tVSaveCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String supplierName = eTSupplierName.getText().toString();
                        String contactPerson = eTContactPerson.getText().toString();
                        String shortName = eTShortName.getText().toString();
                        String address = eTAddress.getText().toString();
                        String telephoneNumber = eTTelephoneNumber.getText().toString();
                        String mobileNumber = eTMobileNumber.getText().toString();
                        String secondaryContact = eTSecondaryMobileNumber.getText().toString();
                        String email = eTEmail.getText().toString();
                        String panVatNo = eTPanVatNo.getText().toString();
                        String creditDays = eTCreditDays.getText().toString();
                        String openingBalance = eTOpeningBalance.getText().toString();
                        String balanceType = (String) balanceTypeSpinnerCustomer.getSelectedItem();

                        validateFields(supplierName, mobileNumber, openingBalance, balanceType, contactPerson, shortName, address, telephoneNumber, panVatNo, email, creditDays, secondaryContact);
                    }
                });


                tVCancelCustomer.setOnClickListener(view -> dialog.dismiss());


                supplierBuilder.setCancelable(false);
                dialog = supplierBuilder.create();
                Window accountGroupWindow = dialog.getWindow();
                assert accountGroupWindow != null;
                accountGroupWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                accountGroupWindow.setGravity(Gravity.CENTER);
                dialog.show();
                break;

        }
    }

    private void init() {
        tVCancelCustomerLedger = findViewById(R.id.tVCancelCustomerLedger);
        rVCustomerLedger = findViewById(R.id.rVCustomerLedger);
        cVAddIcon = findViewById(R.id.cVAddIcon);

        scrollMain = findViewById(R.id.scrollMain);
        cVAddIcon = findViewById(R.id.cVAddIcon);

        lLNoData = findViewById(R.id.lLNoData);
        inflater = LayoutInflater.from(activity);

        requestQueue = Volley.newRequestQueue(activity);
        progressDialog = AppUtil.progressDialog(activity, AppTexts.pleaseWait);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);


        rVCustomerLedger.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollX += dx;
                scrollMain.scrollTo(scrollX, 0);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        initListeners();
    }


    private void initListeners() {
        tVCancelCustomerLedger.setOnClickListener(activity);
        cVAddIcon.setOnClickListener(activity);
    }

    private void getCustomerLedger() {
        progressDialog.show();
        String url = APIs.customerMasterList + preferences.getString(AppTexts.orgId, "") + "/" + preferences.getInt(AppTexts.userId, 0);
        APIRequest headListResponse = new APIRequest(
                Request.Method.GET,
                url,
                supplierLedgerResponse(),
                activityErrorListener()
        );
        requestQueue.add(headListResponse);
//        AppUtil.customizeRequest(headListResponse);
    }


    private Response.Listener<JSONObject> supplierLedgerResponse() {
        return response -> {
            System.out.println("response:: " + response);
            progressDialog.dismiss();
            try {
                if (response.getString(AppTexts.status).equals(AppTexts.success)) {
                    customerSupplierLedgerDTOS = new ArrayList<>();
                    JSONArray listArray = response.getJSONArray(AppTexts.data);
                    for (int i = 0; i < listArray.length(); i++) {
                        CustomerSupplierLedgerDTO dto = new Gson().fromJson(listArray.getJSONObject(i).toString(), CustomerSupplierLedgerDTO.class);
                        customerSupplierLedgerDTOS.add(dto);
                    }
                    if (customerSupplierLedgerDTOS.size() > 0) {
                        lLNoData.setVisibility(View.GONE);
                        scrollMain.setVisibility(View.VISIBLE);
                        cVAddIcon.setVisibility(View.VISIBLE);
                        rVCustomerLedger.setVisibility(View.VISIBLE);
                        customerLedgerAdapter = new CustomerLedgerAdapter(activity, customerSupplierLedgerDTOS);

                        FixedGridLayoutManager manager = new FixedGridLayoutManager();
                        manager.setTotalColumnCount(1);
                        rVCustomerLedger.setLayoutManager(manager);
                        rVCustomerLedger.setAdapter(customerLedgerAdapter);
                        rVCustomerLedger.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        lLNoData.setVisibility(View.VISIBLE);
                        scrollMain.setVisibility(View.GONE);
                        cVAddIcon.setVisibility(View.GONE);
                        rVCustomerLedger.setVisibility(View.GONE);
                    }
                } else {
                    progressDialog.dismiss();
                    String message = response.getString(AppTexts.message);
                    AppUtil.showErrorTitleBox(activity, message);
                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                AppUtil.somethingWrongDialog(activity);
            }
        };
    }


    private void validateFields(String supplierName, String mobileNumber, String openingBalance, String balanceType, String contactPerson, String shortName, String address, String telephoneNumber, String panVatNo, String email, String creditDays, String secondaryContact) {
        if (supplierName.isEmpty()) {
            AppUtil.showErrorTitleBox(activity, "Please Enter Supplier Name");
        } else if (mobileNumber.isEmpty()) {
            AppUtil.showErrorTitleBox(activity, "Please Enter Contact Number");
        } else {
            if (!openingBalance.isEmpty()) {
                if (balanceType.equals("Select Balance Type")) {
                    AppUtil.showErrorTitleBox(activity, "Please select Transaction type(Dr/Cr)");
                }
            }
            if (!email.isEmpty()) {
                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if (!email.matches(EMAIL_PATTERN)) {
                    AppUtil.showErrorTitleBox(activity, "Please enter a valid email");
                }
            }

            sendDataToServer(supplierName, mobileNumber, openingBalance, balanceType, contactPerson, shortName, address, telephoneNumber, panVatNo, email, creditDays, secondaryContact);
        }
    }

    public boolean isValidMobile(String mobile) {
        String PHONE_PATTERN = "^[0][1-9]\\d{9}$|^[1-9]\\d{9}$";
        return mobile.matches(PHONE_PATTERN);
    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(EMAIL_PATTERN);
    }

    private void sendDataToServer(String supplierName, String mobileNumber, String openingBalance, String balanceType, String contactPerson, String shortName, String address, String telephoneNumber, String panVatNo, String email, String creditDays, String secondaryContact) {
        progressDialog.show();
        String url = APIs.createCustomerAccount + preferences.getString(AppTexts.orgId, "") + "/" + preferences.getInt(AppTexts.userId, 0) + "/" + 1; //1 is FYID bro
        System.out.println("url::: " + url);
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("name", supplierName);
            postObject.put("shortName", shortName);
            postObject.put("address", address);
            postObject.put("primaryContactNumber", mobileNumber);
            postObject.put("telContact", telephoneNumber);
            postObject.put("email", email);
            postObject.put("panNo", panVatNo);
            postObject.put("contactName", contactPerson);
            postObject.put("openingBalance", openingBalance);
            postObject.put("secondaryContactNumber", secondaryContact);
            postObject.put("drCr", balanceType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        APIRequest supplierCreationRequest = new APIRequest(
                Request.Method.POST,
                url,
                postObject,
                submitSupplierCreation(),
                activityErrorListener());
//        AppUtil.customizeRequest(supplierCreationRequest);
        requestQueue.add(supplierCreationRequest);
    }

    private Response.Listener<JSONObject> submitSupplierCreation() {
        return response -> {
            System.out.println("submitResponse:: " + response);
            try {
                String message = response.getString("message");
                if (response.getString(AppTexts.status).equals(AppTexts.success)) {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    builder.setTitle(AppTexts.success);
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton(AppTexts.ok, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        getCustomerLedger();
                    });
                    builder.show();
                } else {
                    progressDialog.dismiss();
                    AppUtil.infoDialog(activity, AppTexts.error, message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AppUtil.somethingWrongDialog(activity);
            }
        };
    }

    private void populateDebitCreditSpinner() {
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select Balance Type");
        spinnerArray.add("Dr");
        spinnerArray.add("Cr");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        balanceTypeSpinnerCustomer.setAdapter(adapter);
    }

    private Response.ErrorListener activityErrorListener() {
        return error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            AppUtil.showErrorDialog(activity, error);
        };
    }

}
