package uk.ac.gre.nt4738f.comp1786.core.payloads;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UploadedPayload {
    @NotNull
    public final String userId;

    public final ArrayList<UploadedExpense> detailList;

    public UploadedPayload(@NonNull String userId, @NotNull ArrayList<UploadedExpense> detailList) {
        this.userId = userId;
        this.detailList =detailList;
    }
}
