package org.savdev.mykmm

import App
import ChatViewModel
import EnterTokenDial
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.savdev.mykmmimport.ChatScreen

class MainActivity : ComponentActivity() {

    private val viewModel : ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPersmission()

        setContent {
            MaterialTheme{
                Surface(color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                )
                {
                    val state = viewModel.state
                    if(state.isEnteringToken){
                        EnterTokenDial(
                            token = state.remoteToken,
                            onTokenChange = viewModel::onRemoteTokenChange,
                            onSubmit = viewModel::onSumitRemoteToken
                        )
                    }else{
                        ChatScreen(
                            messageText = state.messageText,
                            onMessageSend = { viewModel.sendMessage(isBrodcast = false) },
                            onMessageBroadCast = { viewModel.sendMessage(isBrodcast = true) },
                            onMessageChange = viewModel::onMessageChance
                        )
                    }

                }
            }
        }
    }



    private fun requestNotificationPersmission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}