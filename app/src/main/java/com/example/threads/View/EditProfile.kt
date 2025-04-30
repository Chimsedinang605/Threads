package com.example.threads.View

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threads.Data.SharePref
import com.example.threads.model.UserModel
import com.example.threads.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navHostController: NavHostController) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    // Current user data
    val currentName = remember { mutableStateOf(SharePref.getName(context)) }
    val currentUsername = remember { mutableStateOf(SharePref.getUsername(context)) }
    val currentBio = remember { mutableStateOf(SharePref.getBio(context)) }

    // Profile image handling
    val currentImageUrl = remember { mutableStateOf(SharePref.getImageUrl(context)) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val isUploading = remember { mutableStateOf(false) }

    // For showing snackbar messages
    val snackbarHostState = remember { SnackbarHostState() }

    // Image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri.value = it
        }
    }

    // Current user ID from Firebase Auth
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentUserUid.isNotEmpty()) {
                                coroutineScope.launch {
                                    isUploading.value = true

                                    // If a new image was selected, upload it
                                    val imageUrl = selectedImageUri.value?.let { uri ->
                                        uploadImageToFirebase(uri, currentUserUid)
                                    } ?: currentImageUrl.value

                                    // Update user data
                                    val updatedUser = UserModel(
                                        name = currentName.value,
                                        username = currentUsername.value,
                                        imgUrl = imageUrl,
                                        uid = currentUserUid
                                    )

                                    userViewModel.updateUserProfile(updatedUser, currentBio.value,
                                        onSuccess = {
                                            // Update SharePref values
                                            SharePref.setName(context, currentName.value)
                                            SharePref.setUsername(context, currentUsername.value)
                                            SharePref.setBio(context, currentBio.value)
                                            if (imageUrl.isNotEmpty()) {
                                                SharePref.setImageUrl(context, imageUrl)
                                            }

                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Profile updated successfully")
                                            }
                                            isUploading.value = false
                                            navHostController.navigateUp()
                                        },
                                        onFailure = { exception ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Failed to update profile: ${exception.message}")
                                            }
                                            isUploading.value = false
                                        }
                                    )
                                }
                            }
                        },
                        enabled = !isUploading.value
                    ) {
                        if (isUploading.value) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable { launcher.launch("image/*") }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = selectedImageUri.value ?: currentImageUrl.value
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Photo",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name field
            OutlinedTextField(
                value = currentName.value,
                onValueChange = { currentName.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username field
            OutlinedTextField(
                value = currentUsername.value,
                onValueChange = { currentUsername.value = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bio field
            OutlinedTextField(
                value = currentBio.value,
                onValueChange = { currentBio.value = it },
                label = { Text("Bio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            if (isUploading.value) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
                Text(
                    "Updating profile...",
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Function to upload image to Firebase Storage
private suspend fun uploadImageToFirebase(fileUri: Uri, userId: String): String {
    return try {
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("profile_images/$userId/$fileName")

        // Upload task
        val uploadTask = storageRef.putFile(fileUri).await()

        // Get download URL
        val downloadUrl = storageRef.downloadUrl.await()
        downloadUrl.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}