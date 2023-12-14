// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.epilog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityBooklistBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageButton addbook;

  @NonNull
  public final ImageButton bookback;

  private ActivityBooklistBinding(@NonNull LinearLayout rootView, @NonNull ImageButton addbook,
      @NonNull ImageButton bookback) {
    this.rootView = rootView;
    this.addbook = addbook;
    this.bookback = bookback;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBooklistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBooklistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_booklist, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBooklistBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addbook;
      ImageButton addbook = ViewBindings.findChildViewById(rootView, id);
      if (addbook == null) {
        break missingId;
      }

      id = R.id.bookback;
      ImageButton bookback = ViewBindings.findChildViewById(rootView, id);
      if (bookback == null) {
        break missingId;
      }

      return new ActivityBooklistBinding((LinearLayout) rootView, addbook, bookback);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}