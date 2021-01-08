package com.raisetech.ecalculo.zorbistore.data.network


import com.google.gson.JsonSyntaxException
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: () -> Response<List<T>>) : List<T>? {
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()

        }else{
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("messageId"))
                    message.append(JsonSyntaxException(it).message)

                }catch(e: JSONException){

                }
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())

        }
    }

}