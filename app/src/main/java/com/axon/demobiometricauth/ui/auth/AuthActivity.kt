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
import com.axon.demobiometricauth.databinding.ActivityAuthBinding
import com.axon.demobiometricauth.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBiometric()
        subscribeToViewModel()
        setupUi()

        binding.tvUseBiometric.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun setupUi() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text?.toString() ?: return@setOnClickListener
                val password = etPassword.text?.toString() ?: return@setOnClickListener
                viewModel.loginWithUsername(username, password)
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.userLogged.collect(this) { isLogged ->
            if (isLogged) {
                startActivity(MainActivity.createIntent(this@AuthActivity))
            }
        }

        viewModel.progress.collect(this) { progress ->
            binding.apply {
                btnLogin.isClickable = !progress
                progressBar.isVisible = progress
            }
        }

        viewModel.isUserLogged.collect(this@AuthActivity) { isUserLogged ->
            if (isUserLogged) {
                biometricPrompt.authenticate(promptInfo)
                binding.tvUseBiometric.isVisible = true
            }
        }
    }

    private fun initBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
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

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(MainActivity.createIntent(this@AuthActivity))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext,
                        "Failed", Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

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