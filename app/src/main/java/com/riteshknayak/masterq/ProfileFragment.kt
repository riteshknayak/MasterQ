package com.riteshknayak.masterq

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.riteshknayak.masterq.databinding.FragmentProfileBinding
import java.util.*


class ProfileFragment : Fragment() {
    private var mName: String? = null
    private var mScore: Int? = null
    var database: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null
    private var uid: String? = null
    var binding: FragmentProfileBinding? = null
    private var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var image: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth!!.currentUser!!.uid

        database!!.collection("users")
            .document(uid!!)
            .get().addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.getString("name") != null) {
                    mName = documentSnapshot.getString("name")
                    binding!!.name.text = mName
                }
                if (documentSnapshot["score"] != null) {
                    val s = documentSnapshot["score"] as Long
                    val mScore = s.toInt()
                    binding!!.score.text = mScore.toString()
                    database!!.collection("users")
                        .whereGreaterThan("score",mScore)
                        .get().addOnSuccessListener { Q ->
                            val position : Int = Q.size()+1
                            binding!!.leaderboardPosition.text = "#"+position.toString()
                        }
                }
            }

        database!!.collection("users")
            .document(uid!!)
            .get().addOnSuccessListener { doc ->
                if (doc.getString("imageUrl") != null) {
                    context?.let {
                        Glide.with(it)
                            .load(doc.getString("imageUrl"))
                            .into(binding!!.profileImage)
                    }

                }
            }
        binding!!.profileImage.setOnClickListener {
            ImagePicker.with(requireActivity())
                .cropSquare() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }

        binding!!.addProfile.setOnClickListener {
            ImagePicker.with(requireActivity())
                .cropSquare() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }
        return binding!!.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    binding!!.profileImage.setImageURI(fileUri)

                    image = storageReference.child("pictures/$uid")
                    image?.putFile(fileUri)?.addOnSuccessListener {
                        image?.downloadUrl?.addOnSuccessListener { uri ->

                            val imageUid: MutableMap<String, String> = HashMap()
                            imageUid["imageUrl"] = uri.toString()

                            database?.collection("users")
                                ?.document(uid.toString())
                                ?.set(imageUid)
                        }
                        Toast.makeText(context, "Image Is Uploaded.", Toast.LENGTH_SHORT)
                            .show()
                    }?.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Upload Failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}