import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ChatGPT(private val modelId: String) {
    companion object {
        private const val API_URL = "https://api-inference.huggingface.co/models/<model_id>"
        private const val API_KEY = "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"
        private const val JSON_MEDIA_TYPE = "application/json"
    }

    private val httpClient = OkHttpClient()
    private val gson = Gson()

    @Throws(IOException::class)
    fun generateResponse(prompt: String): String {
        val json = "{\"inputs\":\"$prompt\"}"
        val body = json.toRequestBody(JSON_MEDIA_TYPE.toMediaType())

        val request = Request.Builder()
            .url(API_URL.replace("<model_id>", modelId))
            .header("Authorization", "Bearer $API_KEY")
            .post(body)
            .build()

        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        val mapper = ObjectMapper().registerKotlinModule()
        val responses = mapper.readValue<Array<GPT2Response>>(response.body!!.string())[0]

        return responses?.generated_text ?: ""
    }

    data class GPT2Response(val generated_text: String)
}
