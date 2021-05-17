package ru.sagutdinov.netologyhws

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import ru.sagutdinov.netologyhws.api.AttachmentModel
import java.io.IOException


class CreatePostActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        imageViewPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        createButton.setOnClickListener {
            val textContent = enterContentEditText.text.toString()
            if (textContent.isEmpty()) {
                Toast.makeText(this, R.string.empty_text, Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    try {
                        switchDeterminateBar(true)
                        val attachModelId = getAttachModel(this@CreatePostActivity)
                        val response = Repository.createPost(textContent, attachModelId)
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CreatePostActivity,
                                R.string.create_post_is_successful,
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(Activity.RESULT_OK)
                            //startActivityForResult(intent, 1)
                            finish()
                        } else {
                            Toast.makeText(
                                this@CreatePostActivity,
                                R.string.create_post_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@CreatePostActivity,
                            R.string.connect_to_server_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }

                }
            }
        }
    }

    private fun switchDeterminateBar(isLaunch: Boolean) {
        if (isLaunch) {
            determinateBarCreatePost.isVisible = true
            createButton.isEnabled = false
        } else {
            determinateBarCreatePost.isVisible = false
            createButton.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                switchDeterminateBar(true)
                lifecycleScope.launch {
                    try {
                        switchDeterminateBar(true)
                        val imageUploadResult = Repository.upload(it)
                        if (imageUploadResult.isSuccessful) {
                            Toast.makeText(
                                this@CreatePostActivity,
                                R.string.photo_upload_is_successful,
                                Toast.LENGTH_SHORT
                            ).show()
                            imageViewPhoto.isEnabled = false
                            imageViewPhoto.foreground = getDrawable(R.drawable.ic_baseline_check_red_24)
                            imageViewPhoto.background = getDrawable(R.drawable.ic_baseline_photo_camera_gray24)
                            val attachmentModel = imageUploadResult.body()!!
                            savedAttachModel(attachmentModel.id, this@CreatePostActivity)
                        } else {
                            Toast.makeText(
                                this@CreatePostActivity,
                                R.string.photo_upload_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@CreatePostActivity,
                            R.string.connect_to_server_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }
                }
            }
        }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}