package id.novian.binar.notebookapplication.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.novian.binar.notebookapplication.adapter.NotesActionListener
import id.novian.binar.notebookapplication.adapter.NotesAdapter
import id.novian.binar.notebookapplication.database.entities.Notes
import id.novian.binar.notebookapplication.databinding.FragmentHomeBinding
import id.novian.binar.notebookapplication.databinding.LayoutDialogAddBinding
import id.novian.binar.notebookapplication.databinding.LayoutDialogDeleteBinding
import id.novian.binar.notebookapplication.databinding.LayoutDialogEditBinding
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import id.novian.binar.notebookapplication.helper.NotesRepo
import id.novian.binar.notebookapplication.helper.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter
    private val dataProfileRepo: DataProfileRepo by lazy { DataProfileRepo(requireContext()) }
    private val notesRepo: NotesRepo by lazy { NotesRepo(requireContext()) }
    private val sessionMgr: SessionManager by lazy { SessionManager(requireContext()) }

    private fun getEmail(): String? = sessionMgr.getLogin(SessionManager.EMAIL, null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setUsername()
        getDataFromDb()
        tvLogoutClicked()
        fabAddClicked()
    }

    private fun setUsername() {
        val email = getEmail()!!
        CoroutineScope(Dispatchers.IO).launch {
            val username = dataProfileRepo.getUsernameFromEmail(email)
            CoroutineScope(Dispatchers.Main).launch {
                binding.tvWelcomeHome.text = "Welcome, $username"
            }
        }
    }

    private fun tvLogoutClicked() {
        binding.tvLogout.setOnClickListener {
            sessionMgr.removeLogin()
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

    private fun initRecyclerView(){
        binding.rvData.apply {
            layoutManager = LinearLayoutManager(requireContext())
            notesAdapter = NotesAdapter(action)
            adapter = notesAdapter
        }
    }

    private fun fabAddClicked(){
        binding.fabAdd.setOnClickListener {
            val addDialog =
                LayoutDialogAddBinding.inflate(LayoutInflater.from(requireContext()), null, false)

            val builder = AlertDialog.Builder(requireContext())

            builder.setView(addDialog.root)

            val dialog = builder.create()

            addDialog.btnInput.setOnClickListener {
                val title = addDialog.etTitle.text.toString()
                val note = addDialog.etNotes.text.toString()

                saveNotesToDb(title, note)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private val action = object : NotesActionListener {
        override fun onDelete(notes: Notes) {
            val deleteBinding = LayoutDialogDeleteBinding.inflate(LayoutInflater.from(requireContext()), null, false)

            val builder = AlertDialog.Builder(requireContext())

            builder.setView(deleteBinding.root)

            val dialog = builder.create()

            deleteBinding.btnDelete.setOnClickListener {
                deleteNotesDb(notes)
                dialog.dismiss()
            }

            deleteBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.show()
        }

        override fun onEdit(notes: Notes) {
            val editBinding = LayoutDialogEditBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val builder = AlertDialog.Builder(requireContext())

            editBinding.etTitle.setText(notes.title)
            editBinding.etNotes.setText(notes.notes)

            builder.setView(editBinding.root)

            val dialog = builder.create()

            editBinding.btnEdit.setOnClickListener {
                val title = editBinding.etTitle.text.toString()
                val note = editBinding.etNotes.text.toString()

                val newNotes = Notes(notes.id, title, notes.userName, note)
                updateNotesDb(newNotes)

                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun getDataFromDb(){
        val email = sessionMgr.getLogin(SessionManager.EMAIL, null)
        CoroutineScope(Dispatchers.IO).launch {
            val username = email?.let { dataProfileRepo.getUsernameFromEmail(it) }
            val result = username?.let { notesRepo.getNotes(it) }

            if (result != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    notesAdapter.submitNotes(result)
                }
            }
        }
    }

    private fun deleteNotesDb(notes: Notes){
        CoroutineScope(Dispatchers.IO).launch {
            val result = notesRepo.deleteNotes(notes)
            if (result != 0){
                getDataFromDb()
                toastInMainThread("Deleted")
            } else {
                toastInMainThread("Failed")
            }
        }
    }

    private fun updateNotesDb(notes: Notes) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = notesRepo.updateNotes(notes)
            if (result != 0) {
                getDataFromDb()
                toastInMainThread("Updated")
            } else {
                toastInMainThread("Failed")
            }
        }
    }

    private fun saveNotesToDb(title: String, note: String) {
        val email = sessionMgr.getLogin(SessionManager.EMAIL, null)
        CoroutineScope(Dispatchers.IO).launch {
            val username = email?.let { dataProfileRepo.getUsernameFromEmail(it) }
            val notes = username?.let { Notes(null, title, it, note) }
            val result = notes?.let { notesRepo.insertNotes(it) }
            if (result != 0L) {
                getDataFromDb()
                toastInMainThread("Berhasil")
            } else {
                toastInMainThread("Gagal")
            }
        }
    }

    private fun toastInMainThread(msg: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }
}