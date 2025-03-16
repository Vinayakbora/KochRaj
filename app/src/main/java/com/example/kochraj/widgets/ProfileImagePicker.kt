package com.example.kochraj.widgets

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.kochraj.R
import com.example.kochraj.utils.ImageUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileImagePicker(
    currentImageUrl: String?,
    onImageSelected: (ByteArray) -> Unit,
    isUploading: Boolean = false
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageLoading by remember { mutableStateOf(false) }

    // Request permission for reading external storage
    val permissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    // Image picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            isImageLoading = true

            // Process the image in a background thread
            Thread {
                // Resize and crop the image
                val bitmap = ImageUtils.getBitmapFromUri(context, it, 500)
                bitmap?.let { processedBitmap ->
                    // Convert to byte array for upload
                    val imageBytes = ImageUtils.bitmapToByteArray(processedBitmap)
                    // Pass to callback on main thread
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        onImageSelected(imageBytes)
                        isImageLoading = false
                    }
                } ?: run {
                    isImageLoading = false
                }
            }.start()
        }
    }

    // Image painter
    val painter = rememberAsyncImagePainter(
        model = selectedImageUri ?: currentImageUrl ?: R.drawable.profile
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
            .clickable {
                if (permissionState.status.isGranted) {
                    galleryLauncher.launch("image/*")
                } else {
                    permissionState.launchPermissionRequest()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when {
            isImageLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            painter.state is AsyncImagePainter.State.Loading -> {
                Image(
                    painter = painter,
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            painter.state is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Show upload indicator
        if (isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )
            }
        } else if (selectedImageUri == null && currentImageUrl == null) {
            // Show add icon if no image is selected
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

