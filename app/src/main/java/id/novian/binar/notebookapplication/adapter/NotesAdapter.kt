package id.novian.binar.notebookapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.novian.binar.notebookapplication.database.entities.Notes
import id.novian.binar.notebookapplication.databinding.ItemDataBinding

class NotesAdapter(private val listener: NotesActionListener):
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val diffCallBack = object: DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitNotes(items: List<Notes>) = differ.submitList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class NotesViewHolder(private val binding: ItemDataBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(notes: Notes){
            binding.tvTitle.text = notes.title
            binding.tvNotes.text = notes.notes

            binding.ivDelete.setOnClickListener {
                listener.onDelete(notes)
            }

            binding.ivEdit.setOnClickListener {
                listener.onEdit(notes)
            }
        }
    }
}

interface NotesActionListener {
    fun onDelete(notes: Notes)
    fun onEdit(notes: Notes)
}