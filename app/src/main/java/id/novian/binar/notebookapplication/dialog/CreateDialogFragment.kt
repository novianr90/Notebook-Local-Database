package id.novian.binar.notebookapplication.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import id.novian.binar.notebookapplication.database.entities.Notes
import id.novian.binar.notebookapplication.databinding.FragmentCreateDialogBinding
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import id.novian.binar.notebookapplication.helper.NotesRepo
import id.novian.binar.notebookapplication.helper.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateDialogFragment : DialogFragment() {

    private var _binding : FragmentCreateDialogBinding? = null
    private val binding get() = _binding!!

    private val dataProfileRepo: DataProfileRepo by lazy { DataProfileRepo(requireContext()) }
    private val notesRepo: NotesRepo by lazy { NotesRepo(requireContext()) }
    private val sessionMgr: SessionManager by lazy { SessionManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnInputClicked()
    }

    private fun btnInputClicked() {
        binding.btnInput.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val notes = binding.etNotes.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                saveNotesToDb(title, notes)
            }
            dismiss()
        }
    }

    private fun saveNotesToDb(title: String, note: String){
        val email = sessionMgr.getLogin(SessionManager.EMAIL, null)!!
        CoroutineScope(Dispatchers.IO).launch {
            val username = dataProfileRepo.getUsernameFromEmail(email)!!
            val notes = Notes(null, title, username, note)
            val result = notesRepo.insertNotes(notes)
            if (result != 0L){
                toastInMainThread("Berhasil")
            } else {
                toastInMainThread("Gagal")
            }
        }
    }

    private fun toastInMainThread(msg: String){
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }
}