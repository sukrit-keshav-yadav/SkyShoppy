package com.hyskytech.skyshoppy.View.Fragment.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.View.Activity.ShoppingActivity
import com.hyskytech.skyshoppy.databinding.FragmentLoginBinding
import com.hyskytech.skyshoppy.databinding.FragmentRegisterBinding
import com.hyskytech.skyshoppy.View.dialog.setupBottomSheetDialog
import com.hyskytech.skyshoppy.util.RegisterValidation
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.LoginViewModel
import com.hyskytech.skyshoppy.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            btnLoginLogin.setOnClickListener {
                val email = EdtEmail.text.toString().trim()
                val password = EdtPassword.text.toString().trim()
                viewModel.login(email, password)
            }

            lifecycleScope.launchWhenStarted {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.btnLoginLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Log In Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.btnLoginLogin.revertAnimation()
                            Intent(requireContext(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            binding.btnLoginLogin.revertAnimation()
                        }
                        else -> Unit
                    }
                }
            }


            lifecycleScope.launchWhenStarted {
                viewModel.resetPassword.collect {
                    when (it) {
                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            Snackbar.make(
                                requireView(),
                                "Password Reset Link Sent successfully",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                "Error: ${it.message.toString()}",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.EdtEmail.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    } else {
                        binding.EdtEmail.error = null
                    }
                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.EdtPassword.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    } else {
                        binding.EdtPassword.error = null
                    }
                }

            }


            binding.tvforgotPassword.setOnClickListener {
                setupBottomSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
            }

            binding.goToRegisterPage.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    binding.btnLoginLogin to "TransButton",
                    binding.EdtEmail to "TransEmail",
                    binding.EdtPassword to "TransPassword",
                    binding.textView3 to "TransWelcome",
                    binding.optionsLL to "TransOptions"
                )
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment,
                    null,
                    null,
                    extras
                )
            }

            sharedElementEnterTransition = ChangeBounds().apply {
                duration = 600
            }
            sharedElementReturnTransition = ChangeBounds().apply {
                duration = 600
            }
        }
    }
}