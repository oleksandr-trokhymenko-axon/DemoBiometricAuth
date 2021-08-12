package com.axon.demobiometricauth.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.axon.demobiometricauth.base.extensions.collect
import com.axon.demobiometricauth.base.extensions.hideKeyboard
import com.axon.demobiometricauth.databinding.ActivityAuthBinding
import com.axon.demobiometricauth.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    /** Declare var`s for biometric */
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Call methods from onCreate */
        initBiometric()
        subscribeToViewModel()
        setupUi()

        /** Calling biometric on click. Button is visible if you`re already logged. */
        binding.tvUseBiometric.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    /** Method for login with login and password. */
    private fun setupUi() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text?.toString() ?: return@setOnClickListener
                val password = etPassword.text?.toString() ?: return@setOnClickListener
                hideKeyboard()
                viewModel.loginWithUsername(username, password)
            }
        }
    }

    private fun subscribeToViewModel() {
        /** Check if user input correct login and password and navigate to next screen. */
        viewModel.userLogged.collect(this) { isLogged ->
            if (isLogged) {
                startActivity(MainActivity.createIntent(this@AuthActivity))
            }
        }

        /** Show progress bar, when http request is executing. */
        viewModel.progress.collect(this) { progress ->
            binding.apply {
                btnLogin.isClickable = !progress
                progressBar.isVisible = progress
            }
        }

        /** Call biometric when open auth screen with logged user. */
        viewModel.isUserLogged.collect(this@AuthActivity) { isUserLogged ->
            if (isUserLogged) {
                biometricPrompt.authenticate(promptInfo)
                binding.tvUseBiometric.isVisible = true
            }
        }
    }

    /** Initialization biometric.  */
    private fun initBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                /** Method execute, when response is error. */
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                /** Method execute, when response is success. */
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(MainActivity.createIntent(this@AuthActivity))
                }

                /** Method execute, when is cancelled. */
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext,
                        "Failed", Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        /** Build biometric dialog UI with title, description and button. */
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential if you are already logged with login and password")
            .setNegativeButtonText("Cancel")
            .build()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}