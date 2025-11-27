package com.example.easynote.service.local.chatGpt

import com.example.easynote.models.ChatGptResponse
import com.example.easynote.models.NoteTable
import com.example.easynote.service.remote.chatGpt.ChatGptService
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.iterator

object ChatGptManager {
    val chatGptService = ChatGptService

    suspend fun request(message: String, tables: List<NoteTable>): ChatGptResponse {

        val prompt = buildPrompt(tables, message)

        val serviceResponse =  chatGptService.request(prompt)

        val serviceResponseMessage = ChatGptService.getText(serviceResponse)

        return parseChatGptJson(serviceResponseMessage)
    }

    fun buildPrompt(tables: List<NoteTable>, userMessage: String): String {
        // --- Convertir las tablas a texto legible ---
        val tablesDescription = tables.joinToString("\n\n") { table ->
            val fieldsText = table.types.entries.joinToString("\n") { (name, type) ->
                "  - $name: ${type.name}"
            }

            """
            Table name: ${table.title}
            Table id: ${table.id}
            Description: ${table.description}
            Fields:
            $fieldsText
            """.trimIndent()
        }

        // --- Prompt final ---
        return """
        You are a data-classification assistant.  
        You will receive:
        
        1. A list of existing tables in my app.  
        2. A user message written in natural language.
        
        Your tasks are:
        - Analyze the user message.
        - Decide which table is the best fit based on its purpose, description, and field structure.
        - Summarize the meaning of the user message in one short sentence.
        - Extract values for each field of the selected table.  
          If a field cannot be extracted, return null.
        - Now is "${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}".
        - EVENT type is the same as DATE but used for reminders/alerts.
        - All field values MUST strictly match the field type defined in the table:
          * TEXT → plain string without descriptions, only extracted raw value.
          * INTEGER → numeric value.
          * TIME → a valid date or datetime in ISO format (YYYY-MM-DD or YYYY-MM-DDTHH:mm).
          * EVENT → ONLY a date/time in ISO format; NEVER text.
          * BOOLEAN → true or false.
          * REAL → numeric value, can have decimals.
        - Title must be a short and unique text summarizing the note.
        - "selected_table" MUST be an integer matching EXACTLY one of the ids of the tables listed.
        - NEVER return the table name. ONLY return the numeric id.
        - Do not generate explanations, notes, interpretations, or paraphrased content inside field values.

        
        
        Output must be a JSON object with this exact format:
        
        {
          "selected_table": <table_id_as_integer>,
          "title": "<note_title>",
          "summary": "<short summary>",
          "fields": {
              "<field1>": <value or null>,
              "<field2>": <value or null>,
              ...
          }
        }
        
        ### Tables ###
        $tablesDescription
        
        ### User message ###
        "$userMessage"
    """.trimIndent()
    }

    private fun parseChatGptJson(jsonString: String): ChatGptResponse {

        val json = JSONObject(jsonString)

        // Datos base
        val summary = json.optString("summary", "")
        val tableId = json.optString("selected_table", "").toInt()
        val fieldsJson = json.optJSONObject("fields") ?: JSONObject()
        val title = json.optString("title", "")

        // Convertir fields a Map<String, Object>
        val fieldsMap = mutableMapOf<String, Any?>()
        for (key in fieldsJson.keys()) {
            fieldsMap[key] = fieldsJson.get(key)
        }

        // Si no viene timestamp, usamos fecha actual
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        return ChatGptResponse(
            tableId,
            title,
            summary,
            fieldsMap,
            timestamp
        )
    }


}