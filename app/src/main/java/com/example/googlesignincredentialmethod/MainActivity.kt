package com.example.googlesignincredentialmethod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.googlesignincredentialmethod.ui.theme.GoogleSignInCredentialMethodTheme
import com.example.googlesignincredentialmethod.utils.GoogleCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleSignInCredentialMethodTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(lifecycleScope)
            }
        }
    }
}

@Composable
fun MainScreen(lifecycleScope: LifecycleCoroutineScope? = null) {
    val context = LocalContext.current
    val resultState = remember {
        mutableStateOf<GoogleIdTokenCredential?>(null)
    }
    val state = remember {
        GoogleCredential()
    }
    LaunchedEffect(key1 = Unit, block = {
        state.setSignInListener(object : GoogleCredential.SignInResult {
            override fun onSuccess(result: GoogleIdTokenCredential) {
                Toast.makeText(context, "Success..!", Toast.LENGTH_SHORT).show()
                resultState.value = result
            }

            override fun onError(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    })
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (text, button, myResult) = createRefs()
        Text(text = "Google SignIn", modifier = Modifier.constrainAs(text) {
            top.linkTo(parent.top,40.dp)
            centerHorizontallyTo(parent)
        }, style = MaterialTheme.typography.headlineMedium)
        val modifier = Modifier.constrainAs(myResult) {
            top.linkTo(text.bottom)
            centerHorizontallyTo(parent)
            bottom.linkTo(button.top)
            height = Dimension.preferredWrapContent
        }
        ShowAccountInfo(resultState, modifier)
        Button(onClick = {
            state.launchGoogleSignInFlow(lifecycleScope!!, context)
        }, Modifier.constrainAs(button) {
            top.linkTo(myResult.bottom)
            centerHorizontallyTo(parent)
            bottom.linkTo(parent.bottom)
        }) {
            Text(text = "Sign-in")
        }
    }
}

@Composable
fun ShowAccountInfo(result: MutableState<GoogleIdTokenCredential?>, modifier: Modifier) {
    Column(modifier = modifier) {
        Row {
            Text(text = "Name: ")
            Text(text = result.value?.displayName ?: "NA")
        }
        Row {
            Text(text = "Token: ")
            Text(text = result.value?.idToken ?: "NA", maxLines = 4)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoogleSignInCredentialMethodTheme {
        MainScreen(null)
    }
}