package com.knowledge.newapp.ui.framgment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.knowledge.newapp.R;
import com.knowledge.newapp.databinding.FragmentAuthBinding;
import com.knowledge.newapp.models.Entities.User;
import com.knowledge.newapp.viewmodels.UserViewModel;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Auth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Auth extends Fragment implements TextWatcher, View.OnClickListener {
    private FragmentAuthBinding binding;
    private UserViewModel userViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Auth() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Auth.
     */
    // TODO: Rename and change types and number of parameters
    public static Auth newInstance(String param1, String param2) {
        Auth fragment = new Auth();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_auth, container, false);
        binding=FragmentAuthBinding.inflate(inflater,container,false);
        initlistener();
        binding.logintoregister.setOnClickListener(v->{
            binding.RegisterView.setVisibility(View.VISIBLE);
            binding.LoginView.setVisibility(View.GONE);
            binding.ForgotView.setVisibility(View.GONE);
        });
        binding.regitertologin.setOnClickListener(v->{
            binding.RegisterView.setVisibility(View.GONE);
            binding.LoginView.setVisibility(View.VISIBLE);
            binding.ForgotView.setVisibility(View.GONE);
        });
        binding.forgottologin.setOnClickListener(v->{
            binding.RegisterView.setVisibility(View.GONE);
            binding.LoginView.setVisibility(View.VISIBLE);
            binding.ForgotView.setVisibility(View.GONE);
        });
        binding.forgotPassView.setOnClickListener(v->{
            binding.RegisterView.setVisibility(View.GONE);
            binding.LoginView.setVisibility(View.GONE);
            binding.ForgotView.setVisibility(View.VISIBLE);
        });
        return binding.getRoot();
    }

    public void initlistener(){
//        login
        binding.loginEmailId.addTextChangedListener(this);
        binding.loginPasswordId.addTextChangedListener(this);
        binding.loginSubmitId.setOnClickListener(this);
//        register
        binding.registerNameId.addTextChangedListener(this);
        binding.registerEmailID.addTextChangedListener(this);
        binding.registerMobileID.addTextChangedListener(this);
        binding.registerPasswordID.addTextChangedListener(this);
        binding.registerPasswordConfirmID.addTextChangedListener(this);
        binding.registerSubmitID.setOnClickListener(this);
//        forgot
        binding.forgotEmailId.addTextChangedListener(this);
        binding.forgotPasswordId.addTextChangedListener(this);
        binding.forgotPasswordCnfID.addTextChangedListener(this);
        binding.forgotSubmitId.setOnClickListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onClick(View view) {
        NavController navController = Navigation.findNavController(view);
        if(view.getId()==R.id.loginSubmitId){
            if(checkValidationLogin()){

                userViewModel.login( binding.loginEmailId.getText().toString(),binding.loginPasswordId.getText().toString());
                userViewModel.loginResult.observe(this,success->{
                    Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.Auth_to_Main);
                });
            }
        }else if(view.getId()==R.id.registerSubmitID){
            if(checkValidationRegister()){
                User user=new User(
                        binding.registerNameId.getText().toString(),
                        binding.registerEmailID.getText().toString(),
                        binding.registerMobileID.getText().toString(),
                        binding.registerPasswordID.getText().toString()
                );
                userViewModel.register(user);
                userViewModel.registerResult.observe(this,success->{
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.Auth_to_Main);
                });
            }
        }else if(view.getId()==R.id.forgotSubmitId){
            if(checkValidationForgot()){
                userViewModel.resetPassword(binding.forgotEmailId.getText().toString(),binding.forgotPasswordId.getText().toString());
                userViewModel.passwordResetResult.observe(this,success->{
                    Toast.makeText(getActivity(), "Your password has been changed", Toast.LENGTH_SHORT).show();
                    binding.LoginView.setVisibility(View.VISIBLE);
                    binding.RegisterView.setVisibility(View.GONE);
                    binding.ForgotView.setVisibility(View.GONE);
                });
            }
        }


    }
    private boolean checkValidationLogin(){
        if(binding.loginEmailId.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(binding.loginEmailId.getText().toString()).matches()){
            binding.loginEmailId.getText().clear();
            binding.loginEmailId.setBackgroundColor(Color.RED);
            binding.loginEmailId.requestFocus();
            return false;
        }
        if(binding.loginPasswordId.getText().toString().equals("") || !binding.loginPasswordId.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")){
            binding.loginPasswordId.getText().clear();
            binding.loginPasswordId.setBackgroundColor(Color.RED);
            binding.loginPasswordId.requestFocus();
            return false;
        }
        return true;
    }
    private boolean checkValidationRegister(){
        if(binding.registerNameId.getText().toString().equals("") || !binding.registerNameId.getText().toString().matches("^[A-Za-z ]{2,}$")){
            binding.registerNameId.getText().clear();
            binding.registerNameId.setBackgroundColor(Color.RED);
            binding.registerNameId.requestFocus();
            return false;
        }
        if(binding.registerEmailID.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(binding.registerEmailID.getText().toString()).matches()){
            binding.registerEmailID.getText().clear();
            binding.registerEmailID.setBackgroundColor(Color.RED);
            binding.registerEmailID.requestFocus();
            return false;
        }
        if(binding.registerMobileID.getText().toString().equals("") || !binding.registerMobileID.getText().toString().matches("^[0-9]{10}$")){
            binding.registerMobileID.getText().clear();
            binding.registerMobileID.setBackgroundColor(Color.RED);
            binding.registerMobileID.requestFocus();
            return false;
        }
        if(binding.registerPasswordID.getText().toString().equals("") || !binding.registerPasswordID.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")){
            binding.registerPasswordID.getText().clear();
            binding.registerPasswordID.setBackgroundColor(Color.RED);
            binding.registerPasswordID.requestFocus();
            return false;
        }
        if(binding.registerPasswordConfirmID.getText().toString().equals("") || !binding.registerPasswordConfirmID.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")){
            binding.registerPasswordConfirmID.getText().clear();
            binding.registerPasswordConfirmID.setBackgroundColor(Color.RED);
            binding.registerPasswordConfirmID.requestFocus();
            return false;
        }
        if(!binding.registerPasswordID.getText().toString().equals(binding.registerPasswordConfirmID.getText().toString())){
            binding.registerPasswordID.getText().clear();
            binding.registerPasswordID.setBackgroundColor(Color.RED);
            binding.registerPasswordID.requestFocus();
            binding.registerPasswordConfirmID.getText().clear();
            binding.registerPasswordConfirmID.setBackgroundColor(Color.RED);
            binding.registerPasswordConfirmID.requestFocus();
            return false;
        }
        return true;
    }
    private boolean checkValidationForgot() {
        if(binding.forgotEmailId.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(binding.forgotEmailId.getText().toString()).matches()){
            binding.forgotEmailId.getText().clear();
            binding.forgotEmailId.setBackgroundColor(Color.RED);
            binding.forgotEmailId.requestFocus();
            return false;
        }
        if(binding.forgotPasswordId.getText().toString().equals("") || !binding.forgotPasswordId.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")){
            binding.forgotPasswordId.getText().clear();
            binding.forgotPasswordId.setBackgroundColor(Color.RED);
            binding.forgotPasswordId.requestFocus();
            return false;
        }
        if(binding.forgotPasswordCnfID.getText().toString().equals("") || !binding.forgotPasswordCnfID.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")){
            binding.forgotPasswordCnfID.getText().clear();
            binding.forgotPasswordCnfID.setBackgroundColor(Color.RED);
            binding.forgotPasswordCnfID.requestFocus();
            return false;
        }
        if(!binding.forgotPasswordId.getText().toString().equals(binding.forgotPasswordCnfID.getText().toString())){
            binding.forgotPasswordId.getText().clear();
            binding.forgotPasswordId.setBackgroundColor(Color.RED);
            binding.forgotPasswordId.requestFocus();
            binding.forgotPasswordCnfID.getText().clear();
            binding.forgotPasswordCnfID.setBackgroundColor(Color.RED);
            binding.forgotPasswordCnfID.requestFocus();
            return false;
        }

    return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}