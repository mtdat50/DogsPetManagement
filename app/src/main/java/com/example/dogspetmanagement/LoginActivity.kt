package com.example.dogspetmanagement

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.UserDao
import com.example.dogspetmanagement.databinding.ActivityLoginBinding
import com.example.dogspetmanagement.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDAO: UserDao
    private var accountList = mutableListOf< Pair< String, String > >()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDAO = AppDatabase.getInstance(this).userDao()
        lifecycleScope.launch {
//            inserted user:
//            userDAO.insert(User("user1", "pass1"))
//            userDAO.insert(User("user2", "pass2"))
//            userDAO.insert(User("user3", "pass3"))

            val queryResult = userDAO.getAll()
            for (user in queryResult)
                accountList.add(Pair(user.username, user.password))
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            var correct = false

            for (account in accountList)
                correct = correct || (username == account.first && password == account.second)


            if (correct) {
                binding.textView4.visibility = View.INVISIBLE
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
                binding.textView4.visibility = View.VISIBLE
        }
    }
}
