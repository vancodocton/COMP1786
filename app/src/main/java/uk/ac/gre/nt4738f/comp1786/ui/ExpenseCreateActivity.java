package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.EntityState;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.ExpenseValidator;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.IEntityValidator;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class ExpenseCreateActivity extends AppCompatActivity implements IUpdateDate {
    TripDbHelper dbHelper;
    private int tripId;
    IEntityValidator<Expense> expenseValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_create);
        dbHelper = new TripDbHelper(getApplicationContext());
        expenseValidator = new ExpenseValidator();
        tripId = getIntent().getIntExtra(TripDetailsActivity.EXTRA_TRIP_ID, 0);

        ((TextView) findViewById(R.id.textViewExpenseTime))
                .setText(LocalDate.now().toString());

        Button btnSave = findViewById(R.id.btnCreateExpenseSave);
        btnSave.setOnClickListener(view -> clickOnSaveBtn());
    }

    public void clickOnSaveBtn() {
        Expense expense = getExpenseFromInputs();

        EntityState state = expenseValidator.Validate(expense);

        if (!state.isValid) {
            displayAlert(ExpenseCreateActivity.this, "Invalid Input", state.getSingleErrorMessage());
        } else {
            try {
                long id = dbHelper.insertExpenseOrThrow(expense);

                Log.i(ExpenseCreateActivity.this.toString(), String.format("Created expense with id '%d'.", id));

                Intent intent = new Intent(ExpenseCreateActivity.this, TripDetailsActivity.class);
                setResult(RESULT_OK);
                finish();
            } catch (Exception e) {
                Log.e(ExpenseCreateActivity.this.toString(), "Create expense failed.", e);
            }
        }
    }

    public Expense getExpenseFromInputs() {
        final TextView typeTxt = findViewById(R.id.editTextExpenseType);
        final TextView timeTxt = findViewById(R.id.textViewExpenseTime);
        final TextView amountTxt = findViewById(R.id.editTextExpenseAmount);

        String type = typeTxt.getText().toString();
        double amount;
        try {
            amount = Double.parseDouble(amountTxt.getText().toString());
        } catch (NumberFormatException e) {
            amount = 0;
        }
        LocalDate time = LocalDate.parse(timeTxt.getText().toString());

        return Expense.New(tripId, type, time, amount);
    }

    public static void displayAlert(@NotNull Context context, @NotNull String title, @NotNull String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Back", (dialogInterface, i) -> {
                }).show();
    }

    public void showExpenseDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(R.id.textViewExpenseTime);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void updateDatePicker(int textViewDatePickerId, @NotNull LocalDate date) {
        TextView dobText = findViewById(textViewDatePickerId);
        dobText.setText(date.toString());
    }
}