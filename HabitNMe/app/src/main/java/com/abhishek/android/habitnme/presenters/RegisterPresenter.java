package com.abhishek.android.habitnme.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.RegisterActivity;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.inject.Inject;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;


public class RegisterPresenter extends RxPresenter<RegisterActivity> {
    private static final String TAG = RegisterPresenter.class.getSimpleName();
    @Inject
    Context context;

    private static final int START_REGISTER = 1;

    public RegisterPresenter() {
        BaseApplication.getDataComponent().inject(this);
    }

    public void startRegister(final String name, final String email, final String password) {
        final Observable<Boolean> registerObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                register(name, email, password, subscriber);
            }
        });
        restartableFirst(START_REGISTER, new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return registerObservable;
            }
        }, new Action2<RegisterActivity, Boolean>() {
            @Override
            public void call(RegisterActivity registerActivity, Boolean aBoolean) {
                if (aBoolean) {

                    registerActivity.onRegisterSuccess();
                } else {
                    registerActivity.onRegisterFail();
                }
            }
        }, new Action2<RegisterActivity, Throwable>() {
            @Override
            public void call(RegisterActivity registerActivity, Throwable throwable) {
                registerActivity.onRegisterFail();
            }
        });
        start(START_REGISTER);
    }

    private void register(final String name, String email, String password, final Subscriber<? super Boolean> resultSub) {
         FirebaseAuth mAuth =  FirebaseAuth.getInstance();
         //final boolean result = false;
         mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 FirebaseUser user = firebaseAuth.getCurrentUser();
                 if(user!=null){
                     UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                             .setDisplayName(name).build();
                     user.updateProfile(profileChangeRequest)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (!task.isSuccessful()) {
                                         Toast.makeText(context, "Authentication failed.",
                                                 Toast.LENGTH_SHORT).show();
                                         resultSub.onNext(false);
                                         resultSub.onCompleted();
                                     } else {
                                         Toast.makeText(context, "User created succesfully.",
                                                 Toast.LENGTH_SHORT).show();
                                         resultSub.onNext(true);
                                         resultSub.onCompleted();
                                     }
                                 }
                             });
                 }
             }
         });
         mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.e(TAG, e.getMessage());
                     }
                 })
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                         // If sign in fails, display a message to the user. If sign in succeeds
                         // the auth state listener will be notified and logic to handle the
                         // signed in user can be handled in the listener.
                         if (!task.isSuccessful()) {
                             Toast.makeText(context, "Authentication failed.",
                                     Toast.LENGTH_SHORT).show();

                             resultSub.onNext(false);
                             resultSub.onCompleted();
                             return;
                         }

                         FirebaseUser user =task.getResult().getUser();

                         //FirebaseAuth.getInstance().


                     }
                 });
    }
}
