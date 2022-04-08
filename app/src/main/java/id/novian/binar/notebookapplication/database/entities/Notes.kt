package id.novian.binar.notebookapplication.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = false) val title: String,
    @ColumnInfo(name = "data_username") val userName: String,
    @ColumnInfo(name = "notes")val notes: String
)
