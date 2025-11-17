package com.example.easynote.service.local
import com.example.easynote.models.ChatGptResponse
import com.example.easynote.models.NoteTable
import com.example.easynote.service.remote.ChatGptService
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        Description: ${table.description}
        Fields: $fieldsText
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
        
        Output must be a JSON object with this exact format:
        
        {
          "selected_table": "<table_name>",
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
        val table = json.optString("selected_table", "")
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
            title,
            summary,
            table,
            fieldsMap,
            timestamp
        )
    }


}