package com.heinrichreimer.inquiry.callbacks;

import android.support.annotation.Nullable;

public interface GetCallback<RowType> {
    void result(@Nullable RowType[] result);
}
