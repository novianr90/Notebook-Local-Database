package id.novian.binar.notebookapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.novian.binar.notebookapplication.databinding.FragmentSplashBinding
import id.novian.binar.notebookapplication.helper.SessionManager
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {
    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val sessionMgr: SessionManager by lazy { SessionManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            splashToNextFragment()
        }
    }

    private suspend fun splashToNextFragment(){
        delay(2000)
        if (sessionMgr.isLogin()){
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        } else{
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

}