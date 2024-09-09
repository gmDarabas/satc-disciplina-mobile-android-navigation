package com.example.cacatesouro

import android.app.Activity
import com.example.cacatesouro.ui.theme.CacaTesouroTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CacaTesouroTheme {
                val navigationController = rememberNavController()
                NavHost(navController = navigationController, startDestination = "telaInicial") {
                    composable("telaInicial",
                        enterTransition = {
                            return@composable fadeIn(tween(1000))
                        }, exitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                            )
                        }) {
                        TelaInicial { navigationController.navigate("pista01Screen") }
                    }

                    composable("pista01Screen") {
                        Pista01Screen(
                            { navigationController.navigate("pista02Screen") },
                            { navigationController.popBackStack() })
                    }

                    composable("pista02Screen") {
                        Pista02Screen(
                            { navigationController.navigate("pista03Screen") },
                            { navigationController.popBackStack() })
                    }

                    composable("pista03Screen") {
                        Pista03Screen({ navigationController.navigate("telaFinal") },
                            { navigationController.popBackStack() })
                    }

                    composable("telaFinal") {
                        TelaFinal {
                            navigationController.navigate("telaInicial") {
                                popUpTo(0)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelaGenerica(
    titulo: String,
    tituloTopBar: String,
    descricao: String,
    btnText: String,
    onBtnClick: () -> Unit,
    onBack: () -> Unit
) {
    TopBar(tituloTopBar) { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5)
            )

            Text(
                text = descricao,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Button(
                onClick = onBtnClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                )
            ) {
                Text(text = btnText)
            }
        }
    }
}

@Composable
fun TelaInicial(onStartClicked: () -> Unit) {
    val activity = (LocalContext.current as? Activity)
    TelaGenerica(
        titulo = "Caça ao Tesouro",
        tituloTopBar = "Home",
        descricao = "Resolva os enigmas",
        btnText = "Iniciar Caça",
        onBtnClick = onStartClicked,
        onBack = { activity?.finish() }
    )
}


@Composable
fun TelaFinal(goHome: () -> Unit) {
    TelaGenerica(
        titulo = "Parabéns!!",
        tituloTopBar = "Voltar",
        descricao = "Você completou todos desafios!",
        btnText = "Voltar para tela inicial",
        onBtnClick = goHome,
        onBack = goHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(titulo: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = titulo) },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun PistaScreen(
    titulo: String = "Pista",
    pista: String,
    resposta: String,
    onNextClicked: () -> Unit,
    onBack: () -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf(false) }

    val verificarResposta: () -> Unit = {
        if (inputValue.lowercase().trim() == resposta) {
            erro = false
            onNextClicked()
        } else {
            erro = true
        }
    }


    TopBar(titulo, onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = pista,
                style = MaterialTheme.typography.titleLarge
            )


            OutlinedTextField(
                value = inputValue,
                onValueChange = { value: String -> inputValue = value },
                label = { Text("Digite sua resposta") },
                isError = erro,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = { verificarResposta() }
                ),
                modifier = Modifier.fillMaxWidth()
            )


            if (erro) {
                Text(
                    text = "Resposta incorreta. Tente novamente.",
                    color = MaterialTheme.colorScheme.error
                )
            }


            Button(
                onClick = verificarResposta,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Confirmar")
            }
        }
    }
}

@Composable
fun Pista01Screen(onNextClicked: () -> Unit, onBack: () -> Unit) {
    val pista =
        "Eu sou o cérebro de qualquer dispositivo, processando bilhões de instruções por segundo. Quem sou eu?"
    val resposta = "cpu"

    PistaScreen(pista = pista, resposta = resposta, onNextClicked = onNextClicked, onBack = onBack)
}

@Composable
fun Pista02Screen(onNextClicked: () -> Unit, onBack: () -> Unit) {
    val pista =
        "Sem mim, os dispositivos não se comunicam, não compartilham informações, e o mundo ficaria desconectado. Quem sou eu?"
    val resposta = "internet"

    PistaScreen(pista = pista, resposta = resposta, onNextClicked = onNextClicked, onBack = onBack)
}

@Composable
fun Pista03Screen(onNextClicked: () -> Unit, onBack: () -> Unit) {
    val pista =
        "Eu sou o sistema que dá vida ao hardware e faço tudo funcionar em harmonia. Sem mim, nada seria possível. O que sou?"
    val resposta = "sistema operacional"

    PistaScreen(pista = pista, resposta = resposta, onNextClicked = onNextClicked, onBack = onBack)
}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    CacaTesouroTheme {
        Pista01Screen({}, {})
    }
}
