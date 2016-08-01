package com.ishabaev.weather.rxview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by ishabaev on 22.07.16.
 */
public class RxEditText extends EditText {

    private PublishSubject<String> editTextSubject = PublishSubject.create();
    private Subscription subscription;

    public RxEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxEditText(Context context) {
        super(context);
    }

    public RxEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (editTextSubject != null) {
            editTextSubject.onNext(text.toString());
        }
    }

    public void setOnRxTextChangeListener(RxEditTextChangeListener listener, int debounce) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = editTextSubject.asObservable()
                .debounce(debounce, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnNext(listener::onTextChanged)
                .subscribe();
    }

    public interface RxEditTextChangeListener {
        void onTextChanged(String text);
    }
}
