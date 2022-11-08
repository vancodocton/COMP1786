package uk.ac.gre.nt4738f.comp1786.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;


public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private final List<Expense> itemsList;

    public ExpenseRecyclerViewAdapter(List<Expense> items) {
        itemsList = items;
    }

    @Override
    @NotNull
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recycler_expense_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder viewHolder, int position) {
        final Expense item = itemsList.get(position);
        viewHolder.typeTxt.setText(item.type);
        viewHolder.timeTxt.setText(item.time.toString());
        viewHolder.amountTxt.setText(String.format(Locale.getDefault(),"%,.2f", item.amount));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}

