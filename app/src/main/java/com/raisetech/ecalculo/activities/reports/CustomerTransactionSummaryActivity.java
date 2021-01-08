package com.raisetech.ecalculo.activities.reports;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.DatePicker.DatePickerDialog;
import com.hornet.dateconverter.Model;
import com.raisetech.ecalculo.R;
import com.raisetech.ecalculo.utilities.AppTexts;
import com.raisetech.ecalculo.utilities.AppUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CustomerTransactionSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomerTransactionSummaryActivity activity = CustomerTransactionSummaryActivity.this;
    private DateFormat df;
    private RequestQueue requestQueue;
    private int scrollX = 0;
    private SharedPreferences preferences;
    private Calendar calendar, calenderTemp2, calenderTemp1;
    private androidx.appcompat.app.AlertDialog progressDialog;

    private TextView tVTitle, tVDateFrom, tVDateTo, tVTotalOpeningBalance, tVTotalPurchaseAmount, tVTotalPurchaseAmountWC, tVTotalTaxAmount, tVTotalClosingBalance, tVCancel;
    private LinearLayout lLDateFrom, lLDateTo;
    private RecyclerView rVSupplierTransaction;
    private HorizontalScrollView scrollMain, scrollTotalMain;
    private Button buttonSearch;

    private String dateFrom, dateTo, rDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_transaction_summary);
        init();
        checkAndSetDates();
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private void init() {
        tVTitle = findViewById(R.id.tVTitle);
        tVDateFrom = findViewById(R.id.tVDateFrom);
        tVDateTo = findViewById(R.id.tVDateTo);
        tVTotalOpeningBalance = findViewById(R.id.tVTotalOpeningBalance);
        tVTotalPurchaseAmount = findViewById(R.id.tVTotalPurchaseAmount);
        tVTotalPurchaseAmountWC = findViewById(R.id.tVTotalPurchaseAmountWC);
        tVTotalTaxAmount = findViewById(R.id.tVTotalTaxAmount);
        tVTotalClosingBalance = findViewById(R.id.tVTotalClosingBalance);
        tVCancel = findViewById(R.id.tVCancel);

        lLDateFrom = findViewById(R.id.lLDateFrom);
        lLDateTo = findViewById(R.id.lLDateTo);

        rVSupplierTransaction = findViewById(R.id.rVSupplierTransaction);

        scrollMain = findViewById(R.id.scrollMain);
        scrollTotalMain = findViewById(R.id.scrollTotalMain);

        buttonSearch = findViewById(R.id.buttonSearch);

        requestQueue = Volley.newRequestQueue(activity);

        calendar = Calendar.getInstance();
        calenderTemp2 = Calendar.getInstance();
        calenderTemp1 = Calendar.getInstance();
        rDate = getString(R.string.dateHint);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);

        df = new SimpleDateFormat("yyyy-MM-dd");

        tVTitle.setText("Customer Transaction Summary");

        rVSupplierTransaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollX += dx;
                scrollMain.scrollTo(scrollX, 0);
                scrollTotalMain.scrollTo(scrollX, 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        initListeners();

    }

    private void initListeners() {
        lLDateFrom.setOnClickListener(activity);
        lLDateTo.setOnClickListener(activity);
        tVCancel.setOnClickListener(activity);
        buttonSearch.setOnClickListener(activity);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tVCancel:
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

            case R.id.buttonSearch:
                dateFrom = tVDateFrom.getText().toString();
                dateTo = tVDateTo.getText().toString();

                if (dateFrom.equals(rDate) || dateTo.equals(rDate)) {
                    System.out.println("here...............");
                    if (dateFrom.equals(rDate)) {
                        AppUtil.infoDialog(activity, AppTexts.error, "Please select a date in 'From' section!");
                        tVDateFrom.setText(rDate);
                    }else{
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
//            getLedger(fromDate, toDate);
        }
    }

}
