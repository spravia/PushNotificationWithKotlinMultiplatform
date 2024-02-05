import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException

class ChatViewModel : ViewModel()
{

    var state by mutableStateOf(ChatState())

    private val api : FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()


    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }

    fun onRemoteTokenChange(newToken : String)
    {
        state  = state.copy(remoteToken = newToken)
    }


    fun onSumitRemoteToken(){
        state = state.copy(isEnteringToken = false)
    }


    fun onMessageChance(message:String){
        state = state.copy(messageText = message)
    }

    fun sendMessage(isBrodcast:Boolean)
    {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if (isBrodcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New message",
                    body = state.messageText
                )
            )

            try {
                if (isBrodcast){
                    api.broadcast(messageDto)
                }else{
                    api.broadcast(messageDto)
                }


                state = state.copy(messageText = "")

            }catch (e:HttpException)
            {
                e.printStackTrace()
            }catch (e:IOException){
                e.printStackTrace()
            }

        }
    }
}