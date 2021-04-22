package com.common.webframe.view;

public interface CompletionHandler<T> {
    void complete(T retValue);

    void complete();

    void setProgressData(T value);
}