import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//TODO
// https://www.youtube.com/watch?v=q6TL2RyysV4&list=PL4JiBC1xc22vSSl2t7sX8xVEp2irhe1JE&index=6
// https://www.youtube.com/watch?v=iS_qh-PSTHk&t=8s
//  Minute 40


@Composable
fun EnterTokenDial(
    token: String,
    onTokenChange: (String) -> Unit,
    onSubmit: () -> Unit
){


    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ){

        Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(
            RoundedCornerShape(5.dp)).background(MaterialTheme.colors.surface).padding(16.dp)
        )
        {

            OutlinedTextField(value = token,
                onValueChange = onTokenChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Remote user token 2")
                },
                maxLines = 1
            )


            Spacer(Modifier.height(15.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End)
            {
                OutlinedButton(onClick =
                {
                    scope.launch {
                        val localToken = Firebase.messaging.token.await()
                        clipboardManager.setText(AnnotatedString(localToken))

                        Toast.makeText(context, "Copied local token!", Toast.LENGTH_LONG).show()

                    }
                }
                )
                {
                    Text("copy token")

                }

                Spacer(Modifier.width(16.dp))

                Button(onClick = onSubmit)
                {
                    Text("Submit")
                }

            }



        }

    }



}



@Preview(showBackground = true)
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
fun EnterTokenDialPreview(){

    val onTokenChange : (String) -> Unit = {}
    val onSubmit : () -> Unit = {}

    EnterTokenDial("",  onTokenChange,  onSubmit)



}

