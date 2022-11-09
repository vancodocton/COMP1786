package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmDialogFragment extends DialogFragment {
    public interface IOnButtonClickListener {
        void onDeleteConfirmDialogPositiveClick(DialogFragment dialog);
    }

    private final String deletedTargetName;
    IOnButtonClickListener listener;

    public DeleteConfirmDialogFragment(String deletedTargetName) {
        super();
        this.deletedTargetName = deletedTargetName;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (IOnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Caller must implement DeleteConfirmDialogFragment.Listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to delete " + deletedTargetName + " ?")
                .setPositiveButton("Yes", (dialog, id) ->
                        listener.onDeleteConfirmDialogPositiveClick(DeleteConfirmDialogFragment.this))
                .setNegativeButton("No", null);
        return builder.create();
    }
}
