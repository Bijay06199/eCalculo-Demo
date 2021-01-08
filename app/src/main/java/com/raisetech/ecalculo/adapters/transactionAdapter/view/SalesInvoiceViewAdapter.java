package com.raisetech.ecalculo.adapters.transactionAdapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raisetech.ecalculo.R;
import com.raisetech.ecalculo.dtos.SalesPurchaseReportDTO;
import com.raisetech.ecalculo.utilities.AppUtil;

import java.util.List;

public class SalesInvoiceViewAdapter extends RecyclerView.Adapter<SalesInvoiceViewAdapter.ViewHolder> {
    private Context context;
    private List<SalesPurchaseReportDTO> salesPurchaseReportDTOList;
    private LayoutInflater inflater;

    public  SalesInvoiceViewAdapter(Context context, List<SalesPurchaseReportDTO> salesPurchaseReportDTOList){
        this.context = context;
        this.salesPurchaseReportDTOList = salesPurchaseReportDTOList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_view_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position % 2 == 1) {
            holder.lLDataLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            holder.lLDataLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        SalesPurchaseReportDTO dto = salesPurchaseReportDTOList.get(position);
        String address= dto.getAddress();
        holder.tVNepaliDate.setText(dto.getPurchaseSalesDateNepali());
        holder.tVEngDate.setText(dto.getPurchaseSalesDate());
        holder.tVBillNumber.setText(String.valueOf(dto.getBillNo()));
        holder.tVCustomer.setText(dto.getAccountName());
        if (address!=null && !address.isEmpty()){
            holder.tVAddress.setText(address);
        }else{
            holder.tVAddress.setText("--");
        }
        holder.tVStore.setText(dto.getWarehouse().getName());
        holder.tVSubTotal.setText(AppUtil.formattedAmounts(dto.getSubTotalAmount()));
        holder.tVNonTaxable.setText(AppUtil.formattedAmounts(dto.getNonTaxableAmount()));
        holder.tVTaxable.setText(AppUtil.formattedAmounts(dto.getTaxableAmount()));
        holder.tVVat.setText(AppUtil.formattedAmounts(dto.getVatAmount()));
        holder.tVTotal.setText(AppUtil.formattedAmounts(dto.getTotalBillAmount()));
    }

    @Override
    public int getItemCount() {
        return salesPurchaseReportDTOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tVNepaliDate, tVEngDate, tVBillNumber, tVCustomer, tVAddress, tVStore, tVSubTotal, tVNonTaxable, tVTaxable, tVVat, tVTotal;
        ImageView iVEdit, iVDelete;
        LinearLayout lLDataLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tVNepaliDate = itemView.findViewById(R.id.tVNepaliDate);
            tVEngDate = itemView.findViewById(R.id.tVEngDate);
            tVBillNumber = itemView.findViewById(R.id.tVBillNumber);
            tVCustomer = itemView.findViewById(R.id.tVCustomer);
            tVAddress = itemView.findViewById(R.id.tVAddress);
            tVStore = itemView.findViewById(R.id.tVStore);
            tVSubTotal = itemView.findViewById(R.id.tVSubTotal);
            tVNonTaxable = itemView.findViewById(R.id.tVNonTaxable);
            tVTaxable = itemView.findViewById(R.id.tVTaxable);
            tVVat = itemView.findViewById(R.id.tVVat);
            tVTotal = itemView.findViewById(R.id.tVTotal);

            iVEdit = itemView.findViewById(R.id.iVEdit);
            iVDelete = itemView.findViewById(R.id.iVDelete);


            lLDataLayout = itemView.findViewById(R.id.lLDataLayout);
        }
    }
}
