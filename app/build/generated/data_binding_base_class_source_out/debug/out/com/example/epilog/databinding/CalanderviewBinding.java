// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.epilog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CalanderviewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView calendarview;

  @NonNull
  public final TextView itemMonthText;

  private CalanderviewBinding(@NonNull LinearLayout rootView, @NonNull RecyclerView calendarview,
      @NonNull TextView itemMonthText) {
    this.rootView = rootView;
    this.calendarview = calendarview;
    this.itemMonthText = itemMonthText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CalanderviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CalanderviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.calanderview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CalanderviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.calendarview;
      RecyclerView calendarview = ViewBindings.findChildViewById(rootView, id);
      if (calendarview == null) {
        break missingId;
      }

      id = R.id.item_month_text;
      TextView itemMonthText = ViewBindings.findChildViewById(rootView, id);
      if (itemMonthText == null) {
        break missingId;
      }

      return new CalanderviewBinding((LinearLayout) rootView, calendarview, itemMonthText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
