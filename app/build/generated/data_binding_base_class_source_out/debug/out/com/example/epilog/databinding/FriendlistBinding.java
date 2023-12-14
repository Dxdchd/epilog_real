// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.epilog.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FriendlistBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView deletefriend;

  @NonNull
  public final TextView mainemail;

  @NonNull
  public final CircleImageView mainfriendprofile;

  @NonNull
  public final TextView mainnickname;

  @NonNull
  public final ImageView usercalander;

  private FriendlistBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView deletefriend,
      @NonNull TextView mainemail, @NonNull CircleImageView mainfriendprofile,
      @NonNull TextView mainnickname, @NonNull ImageView usercalander) {
    this.rootView = rootView;
    this.deletefriend = deletefriend;
    this.mainemail = mainemail;
    this.mainfriendprofile = mainfriendprofile;
    this.mainnickname = mainnickname;
    this.usercalander = usercalander;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FriendlistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FriendlistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.friendlist, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FriendlistBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deletefriend;
      ImageView deletefriend = ViewBindings.findChildViewById(rootView, id);
      if (deletefriend == null) {
        break missingId;
      }

      id = R.id.mainemail;
      TextView mainemail = ViewBindings.findChildViewById(rootView, id);
      if (mainemail == null) {
        break missingId;
      }

      id = R.id.mainfriendprofile;
      CircleImageView mainfriendprofile = ViewBindings.findChildViewById(rootView, id);
      if (mainfriendprofile == null) {
        break missingId;
      }

      id = R.id.mainnickname;
      TextView mainnickname = ViewBindings.findChildViewById(rootView, id);
      if (mainnickname == null) {
        break missingId;
      }

      id = R.id.usercalander;
      ImageView usercalander = ViewBindings.findChildViewById(rootView, id);
      if (usercalander == null) {
        break missingId;
      }

      return new FriendlistBinding((ConstraintLayout) rootView, deletefriend, mainemail,
          mainfriendprofile, mainnickname, usercalander);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}