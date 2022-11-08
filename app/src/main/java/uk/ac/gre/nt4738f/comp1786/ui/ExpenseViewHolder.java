package uk.ac.gre.nt4738f.comp1786.ui;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.gre.nt4738f.comp1786.R;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    public final TextView typeTxt;
    public final TextView timeTxt;
    public final TextView amountTxt;
    public final View view;

    public ExpenseViewHolder(final View view) {
        super(view);
        this.view = view;
        typeTxt = view.findViewById(R.id.textViewExpenseType);
        timeTxt = view.findViewById(R.id.textViewExpenseTime);
        amountTxt = view.findViewById(R.id.textViewExpenseAmount);
    }
}
