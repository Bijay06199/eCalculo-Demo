package com.raisetech.ecalculo.activities.reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.DatePicker.DatePickerDialog;
import com.hornet.dateconverter.Model;
import com.raisetech.ecalculo.R;
import com.raisetech.ecalculo.activities.creation.AddCustomerActivity;
import com.raisetech.ecalculo.adapters.otherAdapters.FixedGridLayoutManager;
import com.raisetech.ecalculo.adapters.reportsAdapter.PartyStatementAdapter;
import com.raisetech.ecalculo.adapters.transactionAdapter.entry.AccountMasterAdapter;
import com.raisetech.ecalculo.dtos.AccountMasterDTO;
import com.raisetech.ecalculo.dtos.AccountStatementLedgerDTO;
import com.raisetech.ecalculo.listeners.AccountMasterSelectedListener;
import com.raisetech.ecalculo.utilities.APIRequest;
import com.raisetech.ecalculo.utilities.APIs;
import com.raisetech.ecalculo.utilities.AppTexts;
import com.raisetech.ecalculo.utilities.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PartyStatementActivity extends AppCompatActivity implements View.OnClickListener, AccountMasterSelectedListener {
    private PartyStatementActivity activity = PartyStatementActivity.this;

    private DateFormat df;
    private RequestQueue requestQueue;
    private int scrollX = 0;
    private SharedPreferences preferences;
    private Calendar calendar, calenderTemp2, calenderTemp1;
    private androidx.appcompat.app.AlertDialog progressDialog;

    private TextView tVDateFrom, tVDateTo, tVCancelSalesInvoice, tVTitle, tVParty;

    private LinearLayout lLDateFrom, lLDateTo, lLNoData;

    private HorizontalScrollView scrollMain;
    private RecyclerView rVPartyStatement;

    private Button buttonSearch;
    private LayoutInflater inflater;
    private AlertDialog dialog;

    private String dateFrom, dateTo, rDate;

    private List<AccountMasterDTO> accountMasterDTOList;
    private FrameLayout viewSelectItem;
    private AccountMasterAdapter accountMasterAdapter;
    private AlertDialog accountMasterDialog;
    private TextView tVAccountName;
    List<AccountStatementLedgerDTO> accountStatementLedgerDTOSList;
    private PartyStatementAdapter partyStatementAdapter;
    private int accountId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_statement);
        init();
        initRecyclerView();
        checkAndSetDates();
        getAccountList();
    }


    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void init() {
        tVDateFrom = findViewById(R.id.tVDateFrom);
        tVDateTo = findViewById(R.id.tVDateTo);
        tVCancelSalesInvoice = findViewById(R.id.tVCancelSalesInvoice);
        tVTitle = findViewById(R.id.tVTitle);
        tVParty = findViewById(R.id.tVParty);
        tVAccountName = findViewById(R.id.tVAccountName);

        scrollMain = findViewById(R.id.scrollMain);
        rVPartyStatement = findViewById(R.id.rVPartyStatement);

        lLDateTo = findViewById(R.id.lLDateTo);
        lLDateFrom = findViewById(R.id.lLDateFrom);
        lLNoData = findViewById(R.id.lLNoData);

        buttonSearch = findViewById(R.id.buttonSearch);
        viewSelectItem = findViewById(R.id.viewSelectItem);

        inflater = LayoutInflater.from(activity);

        requestQueue = Volley.newRequestQueue(activity);

        calendar = Calendar.getInstance();
        calenderTemp2 = Calendar.getInstance();
        calenderTemp1 = Calendar.getInstance();
        rDate = getString(R.string.dateHint);

        progressDialog = AppUtil.progressDialog(activity, AppTexts.pleaseWait);
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);

        df = new SimpleDateFormat("yyyy-MM-dd");

        tVTitle.setText("Party Statement");

        rVPartyStatement.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollX += dx;
                scrollMain.scrollTo(scrollX, 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        initListeners();
    }

    private void initListeners() {
        lLDateTo.setOnClickListener(activity);
        lLDateFrom.setOnClickListener(activity);
        tVCancelSalesInvoice.setOnClickListener(activity);
        buttonSearch.setOnClickListener(activity);
        tVParty.setOnClickListener(activity);
        viewSelectItem.setOnClickListener(activity);
    }

    private void initRecyclerView() {
        accountStatementLedgerDTOSList = new ArrayList<>();
        partyStatementAdapter = new PartyStatementAdapter(activity, accountStatementLedgerDTOSList);

        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rVPartyStatement.setLayoutManager(manager);
        rVPartyStatement.setAdapter(partyStatementAdapter);
    }


    @SuppressLint("SimpleDateFormat")
    private void checkAndSetDates() {
//        progressDialog.show();
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String fromDate = preferences.getString(AppTexts.spFromDate, "");
//        String toDate = preferences.getString(AppTexts.spToDate, "");

        //load ledger for the first time
//        getLedger(fromDate, toDate);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Model model1 = new DateConverter().getNepaliDate(currentYear, currentMonth, currentDay);
        calenderTemp1.set(model1.getYear(), model1.getMonth(), model1.getDay());
        String currentDateNepali = df.format(calenderTemp1.getTime());
        tVDateFrom.setText(currentDateNepali);
        tVDateTo.setText(currentDateNepali);
    }

    private void getAccountList() {
        progressDialog.show();
        String url = APIs.supplierAndCustomerAccount + preferences.getString(AppTexts.orgId, "") + "/" + preferences.getInt(AppTexts.userId, 0);
        APIRequest itemServiceResponse = new APIRequest(
                Request.Method.GET,
                url,
                accountListResponse(),
                activityErrorListener()
        );
        requestQueue.add(itemServiceResponse);
        AppUtil.customizeRequest(itemServiceResponse);
    }

    private Response.Listener<JSONObject> accountListResponse() {
        return response -> {
            System.out.println("response:: " + response);
            progressDialog.dismiss();
            try {
                accountMasterDTOList = new ArrayList<>();
                if (response.getString(AppTexts.status).equals(AppTexts.success)) {
                    JSONArray accountNatureList = response.getJSONArray(AppTexts.data);
                    for (int i = 0; i < accountNatureList.length(); i++) {
                        String object = accountNatureList.getJSONObject(i).toString();
                        accountMasterDTOList.add(new Gson().fromJson(object, AccountMasterDTO.class));
                    }

                } else {
                    AppUtil.somethingWrongDialog(activity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tVCancelSalesInvoice:
                onBackPressed();
                break;

            case R.id.lLDateFrom:
                lLDateFrom.setSelected(true);
                lLDateTo.setSelected(false);
                nepaliCalender();
                break;

            case R.id.lLDateTo:
                lLDateFrom.setSelected(false);
                lLDateTo.setSelected(true);
                nepaliCalender();
                break;

            case R.id.viewSelectItem:
                populateAccountMaster(accountMasterDTOList);
                break;

            case R.id.buttonSearch:
                if (tVAccountName.getText().toString().equals(AppTexts.dash)){
                    String message = "Please Select an account to view Ledger";
                    AppUtil.showErrorTitleBox(activity,message);
                }else {
                    dateFrom = tVDateFrom.getText().toString();
                    dateTo = tVDateTo.getText().toString();

                    if (dateFrom.equals(rDate) || dateTo.equals(rDate)) {
                        System.out.println("here...............");
                        if (dateFrom.equals(rDate)) {
                            AppUtil.infoDialog(activity, AppTexts.error, "Please select a date in 'From' section!");
                            tVDateFrom.setText(rDate);
                        } else {
                            AppUtil.infoDialog(activity, AppTexts.error, "Please select a date in 'To' section!");
                            tVDateTo.setText(rDate);
                        }
                    } else {
                        Date sDate = null;
                        Date eDate = null;
                        try {
                            sDate = df.parse(dateFrom);
                            eDate = df.parse(dateTo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar fromDate = Calendar.getInstance();
                        fromDate.setTime(sDate);
                        Calendar toDate = Calendar.getInstance();
                        toDate.setTime(eDate);


                        if (fromDate.after(toDate)) {
                            AppUtil.infoDialog(activity, AppTexts.error, "Please select a date earlier than the one selected in 'From' section!");
                            tVDateTo.setText(rDate);
                        } else {
                            getValues();
                        }
                    }
                }
                break;

            case R.id.tVParty:
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                View addPartyDialog = inflater.inflate(R.layout.layout_add_party, null);

                AlertDialog.Builder addPartyBuilder = new AlertDialog.Builder(activity, R.style.MaterialDialogSheet);
                addPartyBuilder.setView(addPartyDialog);

                EditText eTSearchItem = addPartyDialog.findViewById(R.id.eTSearchItem);
                RecyclerView rVParty = addPartyDialog.findViewById(R.id.rVParty);
                RelativeLayout rLAddParty = addPartyDialog.findViewById(R.id.rLAddParty);

//                if (eTSearchItem.requestFocus()) {
//                    new Handler().postDelayed(() -> inputMethodManager.showSoftInput(eTSearchItem, InputMethodManager.SHOW_IMPLICIT), 150);
//                }

                rLAddParty.setOnClickListener(v13 -> {
                    startActivity(new Intent(activity, AddCustomerActivity.class));
                    dialog.dismiss();
                });
//
//                eTSearchItem.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });

                addPartyBuilder.setCancelable(true);
                dialog = addPartyBuilder.create();
                Window addPartyWindow = dialog.getWindow();
                addPartyWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                addPartyWindow.setGravity(Gravity.TOP);
                dialog.show();
                break;
        }
    }

    private void nepaliCalender() {
        com.hornet.dateconverter.DatePicker.DatePickerDialog dpd =
                com.hornet.dateconverter.DatePicker.DatePickerDialog.newInstance(nepaliDateListener);
        DateConverter dc = new DateConverter();
        Calendar cal = Calendar.getInstance();


        com.hornet.dateconverter.DatePicker.DatePickerDialog dialog =
                com.hornet.dateconverter.DatePicker.DatePickerDialog.newInstance(nepaliDateListener);
        dialog.setVersion(DatePickerDialog.Version.VERSION_1);
//            dialog.setMaxDate();
//            dialog.setMinDate(sModel);
        dialog.show(getSupportFragmentManager(), "");
    }


    private com.hornet.dateconverter.DatePicker.DatePickerDialog.OnDateSetListener nepaliDateListener = new com.hornet.dateconverter.DatePicker.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.hornet.dateconverter.DatePicker.DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;

            NumberFormat f = new DecimalFormat("00");
            String yearMonth = "", yearDay = "";
            int month = monthOfYear;
            if (month < 10) {
                yearMonth = f.format(month);
            } else {
                yearMonth = String.valueOf(monthOfYear);
            }

            if (dayOfMonth < 10) {
                yearDay = f.format(dayOfMonth);
            } else {
                yearDay = String.valueOf(dayOfMonth);
            }

            String selectedDate = year + "-" + yearMonth + "-" + yearDay;

            if (lLDateFrom.isSelected()) {
                tVDateFrom.setText(selectedDate);
            }

            if (lLDateTo.isSelected()) {
                tVDateTo.setText(selectedDate);
            }
        }
    };

    private void populateAccountMaster(List<AccountMasterDTO> accountMasterDTOList) {
        View view = inflater.inflate(R.layout.layout_select_account_head, null);
        accountMasterSearchListener((EditText) view.findViewById(R.id.eTSearchAccountGroup));
        RecyclerView rVCategories = view.findViewById(R.id.rVAccountGroup);
        rVCategories.setLayoutManager(new LinearLayoutManager(activity));
        accountMasterAdapter = new AccountMasterAdapter(activity, accountMasterDTOList, activity,"");
        rVCategories.setAdapter(accountMasterAdapter);

        accountMasterDialog = (new AlertDialog.Builder(activity).setView(view)).create();
        accountMasterDialog.show();
    }


    private void accountMasterSearchListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (accountMasterAdapter != null) {
                    accountMasterAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    private void getValues() {
        dateFrom = tVDateFrom.getText().toString();
        dateTo = tVDateTo.getText().toString();
        //getting date month and year from the dates in textview and converting to english
        //from date conversion
        int yearFrom = Integer.parseInt(dateFrom.substring(0, 4));
        int monthFrom = Integer.parseInt(dateFrom.substring(5, 7));
        int dayFrom = Integer.parseInt(dateFrom.substring(8, 10));

        Model model1 = new DateConverter().getEnglishDate(yearFrom, monthFrom, dayFrom);
        calenderTemp1.set(model1.getYear(), model1.getMonth(), model1.getDay());

        String fromDate = df.format(calenderTemp1.getTime());

        //to date conversion
        System.out.println("toDate:: " + dateTo);
        String toYear = dateTo.substring(0, 4);
        String toMonth = dateTo.substring(5, 7);
        String toDate = dateTo.substring(8, 10);

        int yearTo = Integer.parseInt(toYear);
        int monthTo = Integer.parseInt(toMonth);
        int dayTo = Integer.parseInt(toDate);


        Model model2 = new DateConverter().getEnglishDate(yearTo, monthTo, dayTo);
        calenderTemp2.set(model2.getYear(), model2.getMonth(), model2.getDay());
        String convertedEnglishToDate = df.format(calenderTemp2.getTime());
        toDate = convertedEnglishToDate;


        if (fromDate.equals(rDate) || toDate.equals(rDate)) {
            AppUtil.infoDialog(activity, AppTexts.error, "Please select 'From' and 'To' dates");
        } else {
            initRecyclerView();
            getPartyLedger(dateFrom, dateTo);
        }
    }

    private void getPartyLedger(String startDate , String endDate){
        progressDialog.show();
        //1 is for account master Id
        String url = APIs.partyStatement + preferences.getString(AppTexts.orgId, "") + "/" + accountId + "/" + preferences.getString(AppTexts.fyId, "") + "/" + startDate + "/" + endDate;
        APIRequest ledgerInquiryRequest = new APIRequest(
                Request.Method.GET,
                url,
                partyInquiryResponse(),
                activityErrorListener()
        );
        requestQueue.add(ledgerInquiryRequest);
        AppUtil.customizeRequest(ledgerInquiryRequest);
    }

    private Response.Listener<JSONObject> partyInquiryResponse() {
        return response -> {
            System.out.println("response:: " + response);
            try {
                if (response.getString(AppTexts.status).equals(AppTexts.success)) {

                    JSONArray listArray = response.getJSONArray(AppTexts.data);
                    for (int i = 0; i < listArray.length(); i++) {
                        AccountStatementLedgerDTO dto = new Gson().fromJson(listArray.getJSONObject(i).toString(), AccountStatementLedgerDTO.class);
                        accountStatementLedgerDTOSList.add(dto);
                    }
                    if (accountStatementLedgerDTOSList.size() > 0) {
                        lLNoData.setVisibility(View.GONE);
                        rVPartyStatement.setVisibility(View.VISIBLE);
                        scrollMain.setVisibility(View.VISIBLE);
                        partyStatementAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        lLNoData.setVisibility(View.VISIBLE);
                        scrollMain.setVisibility(View.GONE);
                        rVPartyStatement.setVisibility(View.GONE);
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





    private Response.ErrorListener activityErrorListener() {
        return error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            AppUtil.showErrorDialog(activity, error);
        };
    }

    @Override
    public void onAccountSelected(String accountName, int id, double closingBalance) {
        System.out.println("name::: " + accountName);
        System.out.println("id::: " + id);
        System.out.println("closing::: " + closingBalance);
        tVAccountName.setText(accountName);
        accountId = id;
        accountMasterDialog.dismiss();
    }
}
