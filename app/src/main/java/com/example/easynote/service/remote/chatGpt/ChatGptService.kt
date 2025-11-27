package com.example.easynote.service.remote.chatGpt

import com.example.easynote.BuildConfig
import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.responses.Response
import com.openai.models.responses.ResponseCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatGptService {

    private val client: OpenAIClient by lazy {

        val API_KEY = BuildConfig.OPENAI_API_KEY

        if (API_KEY.isBlank()) {
            throw IllegalStateException(
                "OpenAI API key no configurada. Añade OPENAI_API_KEY a local.properties."
            )
        }

        OpenAIOkHttpClient.Companion
            .builder()
            .apiKey(API_KEY)
            .build()
    }

    suspend fun request(inputText: String): Response = withContext(Dispatchers.IO) {
        val params = ResponseCreateParams.Companion.builder()
            .input(inputText)
            .model("gpt-5-nano")
            .build()

        client.responses().create(params)
    }

    fun getText(response: Response): String {
        return response.output()                      // List<ResponseOutputItem>
            .firstOrNull { it.message().isPresent }   // el primer item que tiene un mensaje
            ?.message()!!
            .get()                                    // sacamos ResponseOutputMessage del Optional
            .content()                                 // List<Content>
            .firstOrNull()
            ?.outputText()                             // ResponseOutputText?
            ?.get()
            ?.text()                                   // String?
            ?: ""
    }

}