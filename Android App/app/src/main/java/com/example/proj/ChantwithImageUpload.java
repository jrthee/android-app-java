package com.example.proj;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChantwithImageUpload extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    EditText set_text;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    DatabaseReference mRootReference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser;
    RecyclerView rv;
    RecyclerViewAdapterWithImage rva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chantwith_image_upload);
        set_text=(EditText)findViewById(R.id.set_text);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        rv=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rva=new RecyclerViewAdapterWithImage(this);
        rv.setAdapter(rva);
    }
    public void SendPhotoMessage(View view){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
            uploadImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    uploadImage();

                } else {

                    Toast.makeText(this,"Need Permission to use Camera",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void PostMessage(View view){
        DatabaseReference postsRef = mRootReference.child("posts");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(new PostMessage.Post(currentUser.getDisplayName(), set_text.getText().toString(),null));

        String postId = newPostRef.getKey();
        Map<String, Object> nodes = new HashMap<String, Object>();
        nodes.put(postId+"/extra", "extra info");
        postsRef.updateChildren(nodes);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] pixel_data=baos.toByteArray();
            String path="images/"+ UUID.randomUUID()+".jpg";
            final StorageReference imageRef=storage.getReference(path);
            UploadTask uploadTask=imageRef.putBytes(pixel_data);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.d("Link",downloadUri.toString());
                        DatabaseReference postsRef = mRootReference.child("posts");
                        DatabaseReference newPostRef = postsRef.push();
                        newPostRef.setValue(new PostMessage.Post(currentUser.getDisplayName(),
                                set_text.getText().toString(),downloadUri.toString()));
                    } else {
                        // Handle failures
                        Toast.makeText(ChantwithImageUpload.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            /*uploadTask.addOnSuccessListener(ChantwithImageUpload.this,
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") String url=imageRef.getDownloadUrl();
                    Log.d("Link",url.toString());
                    DatabaseReference postsRef = mRootReference.child("posts");
                    DatabaseReference newPostRef = postsRef.push();
                    newPostRef.setValue(new PostMessage.Post(currentUser.getDisplayName(),
                            set_text.getText().toString(),url.toString()));
                }
            });*/
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void uploadImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}

