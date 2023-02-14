package com.example.appfoodv2.Presenter;

import com.example.appfoodv2.Model.UserModel;

public class UserPreSenter  implements  IUSER{
    private UserModel userModel;
    private  UserView callback;

    public UserPreSenter(UserView callback) {
        this.callback = callback;
         userModel = new UserModel(this);
    }
    public  void HandleLoginUser(String email,String pass){
        userModel.HandleLoginUser(email,pass);
    }


    @Override
    public void OnLengthEmail() {
         callback.OnLengthEmail();
    }

    @Override
    public void OnValidEmail() {
       callback.OnValidEmail();
    }

    @Override
    public void Onpass() {
        callback.OnPass();

    }

    @Override
    public void OnSucess() {
        callback.OnSucess();

    }

    @Override
    public void OnAuthEmail() {
        callback.OnAuthEmail();

    }

    @Override
    public void OnFail() {
        callback.OnFail();
    }

    @Override
    public void OnpassNotSame() {
        callback.OnPassNotSame();
    }

    public void HandleRegist(String email, String pass, String repass) {
        userModel.HandleRegistUser(email,pass,repass);
    }
}
