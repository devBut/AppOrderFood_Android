package com.example.appfoodv2.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.appfoodv2.Presenter.IUSER;

public class UserModel {
    private FirebaseAuth firebaseAuth;
    private  String valid_email="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private  String email;
    private  String pass;
    private IUSER callback;

    public  UserModel(){

    }
//dùng interface callback đến Iuser Presenter
    public  UserModel(IUSER callback){
        this.callback=callback;
        firebaseAuth = FirebaseAuth.getInstance();

    }
    public  void HandleLoginUser(String email,String pass){
        if(email.length()>0){ //ktra da nhap email chua
             //kiem tra email đã hỗ trợ
            if(email.matches(valid_email)){
                if(pass.length()>0){
                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //ktra xac thuc email hay chua
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    callback.OnSucess(); //neu da xac thuc thì dang nhập thành công
                                }else{
                                        // ngc lại bat buoc xac thuc email
                                    callback.OnAuthEmail();
                                }
                            }else{
                                callback.OnFail();
                            }
                        }
                    });
                }else{
                    callback.Onpass();
                }
                //ngc lại ko hỗ trợ
            }else{
                callback.OnValidEmail();
            }

        }else{
            callback.OnLengthEmail();
        }

    }

    public void HandleRegistUser(String email, String pass, String repass) {
        if(email.length()>0){

            if(email.matches(valid_email)){
                if(pass.length()>0){
                    if(pass.equals(repass)){
                        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    callback.OnAuthEmail();
                                }else{
                                    callback.OnFail();
                                }

                            }
                        });
                    }else{
                        callback.OnpassNotSame();
                    }

                }else{
                    callback.Onpass();
                }
            }else{
                callback.OnValidEmail();
            }

        }else{
            callback.OnLengthEmail();
        }
    }

}
