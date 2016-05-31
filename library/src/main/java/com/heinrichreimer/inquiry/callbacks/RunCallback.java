package com.heinrichreimer.inquiry.callbacks;

public interface RunCallback<RunReturn> {
    void result(RunReturn changed);
}