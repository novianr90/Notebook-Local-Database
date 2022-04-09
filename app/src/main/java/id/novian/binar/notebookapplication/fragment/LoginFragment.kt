package id.novian.binar.notebookapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import id.novian.binar.notebookapplication.databinding.FragmentLoginBinding
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import id.novian.binar.notebookapplication.helper.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val dataProfileRepo: DataProfileRepo by lazy { DataProfileRepo(requireContext()) }
    private val sessionMgr: SessionManager by lazy { SessionManager(requireContext())}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEmailAndPasswordEmptyOrNull()
        btnLoginClicked()
        tvRegisterClicked()
    }

    private fun btnLoginClicked(){

        binding.btnLogin.setOnClickListener{
            var valid = true
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.containerEtName.error = "Input your email"
                valid = false
            } else {
                binding.containerEtName.error = null
            }

            if (password.isEmpty()) {
                binding.containerEtPassword.error = "Input your password"
                valid = false
            } else {
                binding.containerEtPassword.error = null
            }

            if (valid){
                CoroutineScope(Dispatchers.IO).launch{
                    val result = dataProfileRepo.checkRegisteredProfile(email, password)
                    val emailData = dataProfileRepo.getEmail(email)
                    val passwordData = dataProfileRepo.getPassword(password)
                    if (!result.isNullOrEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            createToast("Welcome $email")
                            sessionMgr.setEmail(SessionManager.EMAIL, email)
                            sessionMgr.setLogin(true)
                            it.findNavController()
                                .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        }
                    } else {
                        if (emailData.isNullOrEmpty()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                createToast("You are not registered")
                            }
                        }
                        if (passwordData.isNullOrEmpty()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                createToast("Wrong Password")
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            createToast("Wrong Data")
                        }
                    }
                }
            }
        }
    }

    private fun checkEmailAndPasswordEmptyOrNull() {
        binding.etEmail.apply {
            doAfterTextChanged {
                if (text.isNullOrEmpty()){
                    binding.containerEtName.error = "Input your email"
                }
                else {
                    binding.containerEtName.error = null
                }
            }
        }

        binding.etPassword.apply {
            doAfterTextChanged {
                if (text.isNullOrEmpty()){
                    binding.containerEtPassword.error = "Input your password"
                } else {
                    binding.containerEtPassword.error = null
                }
            }
        }
    }

    private fun tvRegisterClicked(){
        binding.tvRegisterAcc.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun createToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}