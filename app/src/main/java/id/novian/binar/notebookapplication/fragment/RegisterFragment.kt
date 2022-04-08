package id.novian.binar.notebookapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import id.novian.binar.notebookapplication.database.entities.DataProfile
import id.novian.binar.notebookapplication.databinding.FragmentRegisterBinding
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val dataProfileRepo: DataProfileRepo by lazy { DataProfileRepo(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConfirmPasswordOnChanged()
        checkUsernameEmailAndPasswordEmptyOrNull()
        saveData()
    }


    private fun saveToDb(username: String, email: String, password: String){
        val data = DataProfile(username, email, password)
        CoroutineScope(Dispatchers.IO).launch {
           dataProfileRepo.insertDataProfile(data)
        }
    }

    private fun saveData(){
        binding.btnRegister.setOnClickListener {
            var valid = true
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            if(username.isEmpty()){
                valid = false
            }

            if(email.isEmpty()){
                valid = false
            }

            if(password.isEmpty()){
                valid = false
            }

            if(confirmPass.isEmpty()){
                valid = false
            }

            if(valid){
                CoroutineScope(Dispatchers.IO).launch {
                    val checkUsername = dataProfileRepo.getUsername(username)
                    val checkEmail = dataProfileRepo.getEmail(email)
                    if (!checkUsername.isNullOrEmpty() || !checkEmail.isNullOrEmpty()){
                        createToastInMainThread("Already Registered")
                    }
                    else {
                        saveToDb(username, email, password)
                        createToastInMainThread("Data Saved")
                        CoroutineScope(Dispatchers.Main).launch {
                            it.findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                        }
                    }
                }
            }
        }
    }

    private fun createToastInMainThread(msg: String){
        CoroutineScope(Dispatchers.Main).launch{
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkConfirmPasswordOnChanged(){
        binding.etConfirmPassword.doAfterTextChanged {
            if(!checkPasswordMatch() || binding.etConfirmPassword.text.isNullOrEmpty()){
                binding.containerConfirmPassword.error = "Doesn't Match"
            } else{
                binding.containerConfirmPassword.error = null
            }
        }
    }

    private fun checkPasswordMatch(): Boolean {
        binding.apply{
            return etPassword.text.toString() == etConfirmPassword.text.toString()
        }
    }

    private fun checkUsernameEmailAndPasswordEmptyOrNull(){
        binding.etUsername.doAfterTextChanged {
            if (binding.etUsername.text.isNullOrEmpty()){
                binding.containerUsername.error = "Need Username"
            } else {
                binding.containerUsername.error = null
            }
        }

        binding.etEmail.doAfterTextChanged {
            if (binding.etEmail.text.isNullOrEmpty()){
                binding.containerEmail.error = "Need Email"
            } else {
                binding.containerEmail.error = null
            }
        }

        binding.etPassword.apply {
            doAfterTextChanged {
                binding.containerPassword.error = if(text.isNullOrEmpty()){
                    "Need Password"
                } else {
                    null
                }
            }
        }

    }
}