package com.abhishek.android.habitnme.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;


public class LoginPresenter extends RxPresenter<LoginActivity> {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    @Inject
    protected FirebaseAuth firebaseAuth;

    @Inject
    protected Context context;

    private static int LOGIN_REQ = 1;

    LoginPresenter() {
        BaseApplication.getDataComponent().inject(this);
    }

    public void login(String email, String password, final Subscriber<? super Boolean> subscriber) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            subscriber.onNext(false);
                            subscriber.onCompleted();
                            return;
                        }
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        // ...
                    }
                });
    }

    public void startLogin(final String email, final String password) {
        final Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                login(email, password, subscriber);
            }
        });
        restartableFirst(LOGIN_REQ, new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return observable;
            }
        }, new Action2<LoginActivity, Boolean>() {
            @Override
            public void call(LoginActivity loginActivity, Boolean aBoolean) {
                loginActivity.onLoginResult(aBoolean);
            }
        }, new Action2<LoginActivity, Throwable>() {
            @Override
            public void call(LoginActivity loginActivity, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                loginActivity.onLoginResult(false);
            }
        });
        start(LOGIN_REQ);
    }

    public void forgotPasseord(String email) {
        firebaseAuth.sendPasswordResetEmail(email);
    }

}
