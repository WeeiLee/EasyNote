package com.example.easynote.service.local.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.easynote.models.FieldType
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object DatabaseManager {

    private lateinit var database: DaoDatabase

    fun initialize(context: Context) {
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DaoDatabase::class.java,
                "easy_note_database"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    Log.d("DB", "Database initialized")
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {
                        insertDefaultTable()
                        Log.d("DB", "insert for default")

                    }
                }
            })
                .fallbackToDestructiveMigration()
                .build()
        }
    }


    fun getAllNotes(): Flow<List<Note>> =
        database.noteDao().getNotes().map { list ->
            list.map { createNoteFromEntity(it) }
        }

    fun getNoteById(noteId: Int): Flow<Note?> =
        database.noteDao()
            .getNoteById(noteId)
            .map { entity ->
                entity?.let { createNoteFromEntity(it) }
            }

    fun getNotesByTableID(id: Int): Flow<List<Note>> =
        database.noteDao()
            .getNotesByTableId(id)
            .map { entities ->
                entities.map { createNoteFromEntity(it) }
            }


    fun getTableFromNote(note: Note): Flow<NoteTable?> =
        database.tableDao().getTableById(note.noteTableId).map { entity ->
            entity?.let { createTableFromEntity(it) }
        }

    fun getTables(): Flow<List<NoteTable>> =
        database.tableDao().getTables().map { list ->
            list.map { createTableFromEntity(it) }
        }

    suspend fun deleteNote(id: Int) {
        database.noteDao().deleteNoteById(id)
    }

    suspend fun deleteTable(table: NoteTable) {
        database.noteDao().deleteNotesByTableId(table.id)
        database.tableDao().deleteTableById(table.id)
    }

    suspend fun addNote(note: Note): Long {
        val table = getTableFromNote(note).first()
        val tableTypes = table?.types?.keys?.toSet() ?: emptySet()

        val fieldKeys = note.fields?.keys?.toSet() ?: emptySet()

        if (!fieldKeys.all { it in tableTypes }) {
            println("DEBUG tableTypes = $tableTypes")
            println("DEBUG fieldKeys = $fieldKeys")
            throw IllegalArgumentException("Fields contain keys not defined in the table's types.")
        }

        val entity = Notes(
            id = note.id,
            title = note.title,
            originalContent = note.originalContent,
            summary = note.summary,
            fields = note.fields,
            noteTableId = note.noteTableId,
            timestamp = note.timestamp
        )
        return database.noteDao().insertNote(entity)
    }

    suspend fun createTable(noteTable: NoteTable): Long {
        val entity = Tables(
            id = noteTable.id,
            title = noteTable.title,
            description = noteTable.description,
            types = noteTable.types
        )
        return database.tableDao().insertTable(entity)
    }

    suspend fun updateNote(note: Note): Boolean {
        val entity = Notes(
            id = note.id,
            title = note.title,
            originalContent = note.originalContent,
            summary = note.summary,
            fields = note.fields,
            noteTableId = note.noteTableId,
            timestamp = note.timestamp
        )
        return database.noteDao().updateNote(entity) == 1
    }

    private fun createTableFromEntity(t: Tables) =
        NoteTable(t.id, t.title, t.description, t.types)

    private fun createNoteFromEntity(n: Notes) =
        Note(n.id, n.title, n.summary, n.originalContent, n.fields, n.noteTableId, n.timestamp)

    private suspend fun insertDefaultTable() {
        val eventTypes = mutableMapOf<String, FieldType>()

        eventTypes["Título"] = FieldType.TEXT
        eventTypes["Fecha"] = FieldType.TIME
        eventTypes["Notas"] = FieldType.TEXT
        eventTypes["Lugar"] = FieldType.TEXT
        eventTypes["Recordatorio activado"] = FieldType.EVENT

        val eventTable = NoteTable(
            0,
            "Eventos",
            "Tabla diseñada para registrar eventos programados por el usuario. Incluye campos obligatorios para fecha, hora y título del evento, así como información adicional como ubicación, notas y opciones de recordatorio. Se utiliza para gestionar actividades o citas.",
            eventTypes
        )

        val alarmTypes = mutableMapOf<String, FieldType>()

        alarmTypes["Título"] = FieldType.TEXT
        alarmTypes["Hora"] = FieldType.TIME
        alarmTypes["Nota"] = FieldType.TEXT

        val alarmTable = NoteTable(
            1,
            "Alarmas",
            "Tabla diseñada para almacenar alarmas configuradas por el usuario. Incluye campos para la hora exacta, sin fecha, título descriptivo. Se utiliza para gestionar alarmas o recordatorio personalizadas que generan un aviso en un momento específico.",
            alarmTypes
        )


        val weightTypes = mutableMapOf<String, FieldType>()

        weightTypes["Peso (kg)"] = FieldType.REAL
        weightTypes["Fecha"] = FieldType.TIME
        weightTypes["Notas"] = FieldType.TEXT

        val weightTable = NoteTable(
            2,
            "Seguimiento de Peso",
            "Tabla diseñada para registrar y monitorear el peso corporal del usuario a lo largo del tiempo. Incluye campos para el valor del peso, la fecha de registro, cambios respecto a mediciones anteriores y notas adicionales. Se utiliza para hacer seguimiento de la evolución del peso, establecer metas y analizar tendencias de salud.",
            weightTypes
        )

        val expenseTypes = mutableMapOf<String, FieldType>()

        expenseTypes["Cantidad"] = FieldType.REAL
        expenseTypes["Fecha"] = FieldType.TIME
        expenseTypes["Categoría"] = FieldType.TEXT
        expenseTypes["Método de pago"] = FieldType.TEXT
        expenseTypes["Notas"] = FieldType.TEXT

        val expenseTable = NoteTable(
            3,
            "Gastos",
            "Tabla diseñada para registrar y controlar los gastos diarios del usuario. Incluye campos para el monto económico, la fecha del gasto, la categoría, la forma de pago y notas adicionales. Se utiliza para llevar un seguimiento financiero, analizar hábitos de consumo y organizar el presupuesto personal.",
            expenseTypes
        )

        val othersTypes = mutableMapOf<String, FieldType>()

        othersTypes["Título"] = FieldType.TEXT
        othersTypes["Descripción"] = FieldType.TEXT
        othersTypes["Fecha creada"] = FieldType.TIME


        val othersTable = NoteTable(
            4,
            "Others",
            "Tabla genérica diseñada para almacenar cualquier información que no coincide con las categorías principales. Incluye campos flexibles como título, descripción, fecha, etiqueta, valores numéricos y contenido adicional. Se utiliza como contenedor universal para datos no clasificados.",
            othersTypes
        )

        this.createTable(eventTable)
        this.createTable(alarmTable)
        this.createTable(expenseTable)
        this.createTable(weightTable)
        this.createTable(othersTable)
        this.insertNoteForTest()
    }

    private suspend fun insertNoteForTest() {
        val types1 = mutableMapOf<String, String>()
        types1["Nombre"] = "ANA"

        val note2: Note = Note(
            null,
            "Reunión de PE",
            "hoy hay un reunión",
            "Reunión de PE",
            null,
            0,
            "12-01-11",
        )
        this.addNote(note2)
    }
}
