package com.shinrinyoku.expenker.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shinrinyoku.expenker.R
import com.shinrinyoku.expenker.databinding.FragmentHomeBinding
import java.io.File
import java.io.IOException
import java.math.BigDecimal

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentHomeBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var photoURI: Uri

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.inflate(inflater,container,false)

//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })


        binding.imageCaptureButton.setOnClickListener {
            val expenseName: String = binding.expenseNameEditText.text.toString()
            val amount: BigDecimal = binding.amountEditText.text.toString().toBigDecimal()

            dispatchTakePictureIntent(container?.context)
        }
        return binding.root
    }

    private fun dispatchTakePictureIntent(context: Context?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.e("Home Activity", "Error occurred when creating image")
                null
            }
            photoFile?.also {
                if (context != null) {
                    photoURI =
                            FileProvider.getUriForFile(context, "com.shinrinyoku.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File("${storageDir}/expense_image.jpeg")
    }
}