package com.example.social.sa.screens.home.add_edit_post

import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark

fun NavGraphBuilder.addEditPostDest(navController: NavController) {
    composable(Screens.PostReviewScreen.route, enterTransition = {
        slideInVertically()
    }) {
        val viewModel = hiltViewModel<PostEditPostViewModel>()
        val state by viewModel.state.collectAsState()
        AddEditPostScreen(state, viewModel::onEvent)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditPostScreen(
    state: AddEditPostState,
    onEvent: (AddEditPostEvent) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.create_post))
                }, navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = ""
                        )
                    }
                }, actions = {
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.send))
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                cursorBrush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary
                    )
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textDirection = TextDirection.Content
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.what_happening),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    it()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .horizontalScroll(
                        rememberScrollState()
                    ),
            ) {
                state.pickedImage.forEach {
                    PickedImage(imageUri = it) {
                        
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier, contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    CameraIcon()
                }
                items(state.images) {
                    PickImages(imageUri = it, onEvent = onEvent)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconsLayout()
        }
    }
}

@Composable
fun IconsLayout(modifier: Modifier = Modifier) {
    HorizontalDivider()
    Row(modifier = Modifier.padding(8.dp).imePadding()){
        IconButton(onClick = { /*TODO*/ }) {
            Icon(painter = painterResource(id = R.drawable.image_icon), contentDescription = "image")
        }

    }
}
@Composable
fun PickedImage(modifier: Modifier = Modifier, imageUri: String, onImageDelete: (String) -> Unit) {
    Box {
        AsyncImage(
            model = imageUri,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(25f))
        )
        FilledTonalIconButton(
            onClick = { onImageDelete(imageUri) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(painter = painterResource(id = R.drawable.close_icon), contentDescription = "")
        }
    }
}

@Composable
fun CameraIcon() {
    OutlinedButton(modifier = Modifier
        .size(80.dp), shape = RoundedCornerShape(25f), onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}

@PreviewBothLightAndDark
@Composable
private fun AddEditPreview() {
    SocialTheme {
        AddEditPostScreen(AddEditPostState())
    }
}

@Composable
fun PickImages(
    modifier: Modifier = Modifier,
    imageUri: String,
    onEvent: (AddEditPostEvent) -> Unit
) {
    AsyncImage(
        model = imageUri,
        contentDescription = "",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(25f))
            .clickable {
                onEvent(AddEditPostEvent.PickImage(imageUri))
            },
        contentScale = ContentScale.Crop
    )

}