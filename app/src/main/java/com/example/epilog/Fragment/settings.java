package com.example.epilog.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.epilog.MainActivity;
import com.example.epilog.R;
import com.example.epilog.User;
import com.example.epilog.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class settings extends Fragment {
    String TAG = "sett";
    FragmentSettingsBinding bd;
    String email;
    String nickname;
    SharedPreferences preferences;
    AlertDialog logout;
    AlertDialog newnick;
    AlertDialog dev;
    EditText inputnick;
    DocumentReference documentReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    FragmentActivity fragmentActivity;

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == logout && which == DialogInterface.BUTTON_POSITIVE) {
                mAuth.signOut();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (dialog == newnick && which == DialogInterface.BUTTON_POSITIVE) {
                if (inputnick.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "올바른 닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    String changednick = inputnick.getText().toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nickname", changednick);
                    editor.commit();
                    documentReference = FirebaseFirestore.getInstance().collection("user").document(preferences.getString("uid", ""));
                    documentReference.update("nickname", changednick);
                    Toast.makeText(getActivity(), "닉네임이 변경되었어요!", Toast.LENGTH_SHORT).show();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().remove(settings.this).commit();
                    manager.popBackStack();
                    FragmentManager newmanager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = newmanager.beginTransaction();
                    ft.add(R.id.main_layout, new settings()).commitAllowingStateLoss();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentSettingsBinding.inflate(getLayoutInflater());
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        documentReference = FirebaseFirestore.getInstance().collection("user").document(preferences.getString("uid", ""));
                        String path = "profiles/" + preferences.getString("uid", "") + "/" + "profile";
                        Bitmap bitmap = null;

                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), uri));
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference btmap = storageRef.child(path);
                        UploadTask uploadTask = btmap.putBytes(data);
                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            String puturl = taskSnapshot.getMetadata().getReference().toString();
                            documentReference.update("profile", puturl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "프로필 사진이 변경되었어요!", Toast.LENGTH_SHORT).show();
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference path2 = storage.getReferenceFromUrl(puturl);
                                        path2.getDownloadUrl().addOnSuccessListener(uri->{
                                            Glide.with(getContext())
                                                    .load(uri)
                                                    .centerCrop()
                                                    .into(bd.profile);
                                        });
                                    }
                                }
                            });
                        }).addOnFailureListener(exception -> {

                        });

                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        bd.changeprofile.setOnClickListener(vv -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        preferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = getActivity();
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        Log.d("TAG", "onStart: " + email);
        bd.logout.setOnClickListener(v -> {
            logout = new AlertDialog.Builder(getActivity())
                    .setTitle("로그아웃 하시겠어요?")
                    .setPositiveButton("로그아웃", dialogListener)
                    .setNegativeButton("돌아가기", null)
                    .create();
            logout.show();
        });
        bd.changenick.setOnClickListener(vv -> {
            inputnick = new EditText(getActivity());
            inputnick.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
            newnick = new AlertDialog.Builder(getActivity())
                    .setTitle("새로운 닉네임을 설정해주세요!")
                    .setMessage("닉네임은 최대 5자까지 가능해요.")
                    .setView(inputnick)
                    .setPositiveButton("확인", dialogListener)
                    .setNegativeButton("취소", null)
                    .show();
        });
        bd.dev.setOnClickListener(vvv -> {
            dev = new AlertDialog.Builder(getActivity())
                    .setMessage("20221480 김동현\n20221488 김종호\n20221508 이우주")
                    .setNegativeButton("닫기", null)
                    .show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        preferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
        documentReference = FirebaseFirestore.getInstance().collection("user").document(preferences.getString("uid", ""));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String url = user.getProfile();
                Log.d(TAG, "onSuccess: "+url);
                if(url!=null){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Log.d("test", "bindSliderImage: "+storage);
                    StorageReference path = storage.getReferenceFromUrl(url);
                    Log.d("test", "bindSliderImage: "+path);
                    path.getDownloadUrl().addOnSuccessListener(uri->{
                        Glide.with(fragmentActivity)
                                .load(uri)
                                .into(bd.profile);
                    });
                }
            }
        });
        email = preferences.getString("email", "");
        nickname = preferences.getString("nickname", "");
        bd.nick.setText(nickname);
        bd.email.setText(email);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}