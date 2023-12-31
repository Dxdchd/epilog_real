// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.epilog.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSettingsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView blogapplgo;

  @NonNull
  public final Button call;

  @NonNull
  public final Button changenick;

  @NonNull
  public final Button changeprofile;

  @NonNull
  public final Button dev;

  @NonNull
  public final TextView email;

  @NonNull
  public final TextView epilog;

  @NonNull
  public final Button logout;

  @NonNull
  public final TextView nick;

  @NonNull
  public final CircleImageView profile;

  private FragmentSettingsBinding(@NonNull LinearLayout rootView, @NonNull ImageView blogapplgo,
      @NonNull Button call, @NonNull Button changenick, @NonNull Button changeprofile,
      @NonNull Button dev, @NonNull TextView email, @NonNull TextView epilog,
      @NonNull Button logout, @NonNull TextView nick, @NonNull CircleImageView profile) {
    this.rootView = rootView;
    this.blogapplgo = blogapplgo;
    this.call = call;
    this.changenick = changenick;
    this.changeprofile = changeprofile;
    this.dev = dev;
    this.email = email;
    this.epilog = epilog;
    this.logout = logout;
    this.nick = nick;
    this.profile = profile;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.blogapplgo;
      ImageView blogapplgo = ViewBindings.findChildViewById(rootView, id);
      if (blogapplgo == null) {
        break missingId;
      }

      id = R.id.call;
      Button call = ViewBindings.findChildViewById(rootView, id);
      if (call == null) {
        break missingId;
      }

      id = R.id.changenick;
      Button changenick = ViewBindings.findChildViewById(rootView, id);
      if (changenick == null) {
        break missingId;
      }

      id = R.id.changeprofile;
      Button changeprofile = ViewBindings.findChildViewById(rootView, id);
      if (changeprofile == null) {
        break missingId;
      }

      id = R.id.dev;
      Button dev = ViewBindings.findChildViewById(rootView, id);
      if (dev == null) {
        break missingId;
      }

      id = R.id.email;
      TextView email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.epilog;
      TextView epilog = ViewBindings.findChildViewById(rootView, id);
      if (epilog == null) {
        break missingId;
      }

      id = R.id.logout;
      Button logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.nick;
      TextView nick = ViewBindings.findChildViewById(rootView, id);
      if (nick == null) {
        break missingId;
      }

      id = R.id.profile;
      CircleImageView profile = ViewBindings.findChildViewById(rootView, id);
      if (profile == null) {
        break missingId;
      }

      return new FragmentSettingsBinding((LinearLayout) rootView, blogapplgo, call, changenick,
          changeprofile, dev, email, epilog, logout, nick, profile);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
