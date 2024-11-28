package es.ucm.fdi.azalea.presentation.passwordrecovery;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.databinding.PasswordRecoveryFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;

public class PasswordRecoveryFragment extends Fragment {
    private PasswordRecoveryFragmentBinding binding;
    private PasswordRecoveryViewModel viewModel;

    private EditText mailEditText;
    private Button recoveryButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PasswordRecoveryFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(PasswordRecoveryViewModel.class);
        mailEditText = binding.editTextTextEmailAddress;
        recoveryButton = binding.passwordRecoveryButton;

        initListeners();

        return view;
    }

    private void initListeners(){
        recoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mailEditText.getText().toString().trim().isEmpty()){
                    mailEditText.setError(getString(R.string.createteacher_filledFieldError));

                }else if(viewModel.verifyMail(mailEditText.getText().toString())){
                    viewModel.resetPassword(mailEditText.getText().toString());

                }else{
                    Toast.makeText(requireActivity(),getString
                            (R.string.createteacher_mailFormatError),Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.getRecoveryEvent().observe(requireActivity(), event->{
            if(event instanceof Event.Success){
                Toast.makeText(requireActivity(),getString(R.string.passwordRecovery_success),Toast.LENGTH_LONG).show();
            }else if(event instanceof Event.Error){
                Toast.makeText(requireActivity(),getString(R.string.passwordRecovery_error),Toast.LENGTH_LONG).show();
            }else{
                //Todo logica para mostrar un progressbar
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
