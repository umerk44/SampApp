package com.test.istestprojectapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.istestprojectapplication.ISApplication
import com.test.istestprojectapplication.R
import com.test.istestprojectapplication.core.RemoteCallState
import com.test.istestprojectapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val TAG: String? = LoginFragment::class.simpleName

    private val  viewModel : LoginViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObserver()
        setUpListener()
    }

    private fun setUpListener() {
        login.setOnClickListener{ onLoginClicked() }
        forgotPassword.setOnClickListener { showToast("Action not supported") }
    }

    private fun onLoginClicked() {
        val userName = name.text.toString()
        val password = password.text.toString()

        if( validate(userName, password) )
            viewModel.login(userName, password)

    }

    private fun validate(userName: String, _password: String) : Boolean{

        return when {
            userName.isBlank() -> {
                name.error = "Please enter username"
                false
            }
            _password.isBlank() -> {
                password.error = "Please enter password"
                false
            }

            else -> true
        }
    }


    private fun setUpObserver() {
        viewModel.loginView.observe(viewLifecycleOwner, {observeLoginCall(it)})

    }

    private fun observeLoginCall(remoteCallState: RemoteCallState<String>) {
        when(remoteCallState) {
            is RemoteCallState.Loading ->  {
                disableLoginButton()
                progress.visibility = View.VISIBLE
            }
            is RemoteCallState.Success -> {
                enableLoginButton()
                progress.visibility = View.GONE
                onLoginSuccess()
//                  findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            }
            is RemoteCallState.Failed -> {
                enableLoginButton()
                progress.visibility = View.GONE
                showToast(remoteCallState.message)
            }
        }
    }

    private fun enableLoginButton() {
        login.isEnabled = true
    }

    private fun disableLoginButton() {
        login.isEnabled = false
    }

    private fun onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess")
        findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}