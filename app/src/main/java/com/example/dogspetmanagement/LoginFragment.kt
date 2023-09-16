package com.example.dogspetmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.database.User
import com.example.dogspetmanagement.database.UserDao
import com.example.dogspetmanagement.databinding.FragmentLoginBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch
import android.app.ActionBar as ActionBar


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDAO: UserDao
    private var accountList = mutableListOf< Pair< String, String > >()

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDAO = AppDatabase.getInstance(requireContext()).userDao()
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
                (activity as AppCompatActivity).supportActionBar?.show()
                findNavController().navigate(R.id.action_loginFragment_to_FirstFragment)
            }
            else
                binding.textView4.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}