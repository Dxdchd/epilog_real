// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.epilog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CalanderdayBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ImageView backimg;

  @NonNull
  public final FrameLayout itemDayLayout;

  @NonNull
  public final TextView itemDayText;

  private CalanderdayBinding(@NonNull FrameLayout rootView, @NonNull ImageView backimg,
      @NonNull FrameLayout itemDayLayout, @NonNull TextView itemDayText) {
    this.rootView = rootView;
    this.backimg = backimg;
    this.itemDayLayout = itemDayLayout;
    this.itemDayText = itemDayText;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CalanderdayBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CalanderdayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.calanderday, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CalanderdayBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backimg;
      ImageView backimg = ViewBindings.findChildViewById(rootView, id);
      if (backimg == null) {
        break missingId;
      }

      FrameLayout itemDayLayout = (FrameLayout) rootView;

      id = R.id.item_day_text;
      TextView itemDayText = ViewBindings.findChildViewById(rootView, id);
      if (itemDayText == null) {
        break missingId;
      }

      return new CalanderdayBinding((FrameLayout) rootView, backimg, itemDayLayout, itemDayText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
