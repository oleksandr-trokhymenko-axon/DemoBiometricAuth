package com.axon.demobiometricauth.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.axon.demobiometricauth.base.extensions.collect
import com.axon.demobiometricauth.databinding.ActivityMainBinding
import com.axon.demobiometricauth.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Back to auth screen without changes. */
        binding.btnPreviousScreen.setOnClickListener {
            startActivity(AuthActivity.createIntent(this))
        }

        /** Cleared shared prefs and http request for delete session. */
        binding.btnLogOut.setOnClickListener {
            viewModel.logout()
        }

        /** Checking logout event and back to auth screen.  */
        viewModel.userLogged.collect(this) { isLogged ->
            if (!isLogged) startActivity(AuthActivity.createIntent(this@MainActivity))
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}