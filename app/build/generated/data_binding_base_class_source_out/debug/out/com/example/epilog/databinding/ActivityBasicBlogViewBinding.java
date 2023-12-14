// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.epilog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import me.relex.circleindicator.CircleIndicator3;

public final class ActivityBasicBlogViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ScrollView blogViewScrollView;

  @NonNull
  public final ImageButton blogback;

  @NonNull
  public final ImageView comment;

  @NonNull
  public final TextView commentcnt;

  @NonNull
  public final TextView content;

  @NonNull
  public final TextView datebtn;

  @NonNull
  public final ImageView deleteblog;

  @NonNull
  public final ImageView feel;

  @NonNull
  public final ImageView heart;

  @NonNull
  public final CircleIndicator3 indicator;

  @NonNull
  public final TextView likecnt;

  @NonNull
  public final TextView locationbtn;

  @NonNull
  public final TextView title;

  @NonNull
  public final TextView toptitle;

  @NonNull
  public final ViewPager2 viewpager;

  @NonNull
  public final ImageView weather;

  private ActivityBasicBlogViewBinding(@NonNull LinearLayout rootView,
      @NonNull ScrollView blogViewScrollView, @NonNull ImageButton blogback,
      @NonNull ImageView comment, @NonNull TextView commentcnt, @NonNull TextView content,
      @NonNull TextView datebtn, @NonNull ImageView deleteblog, @NonNull ImageView feel,
      @NonNull ImageView heart, @NonNull CircleIndicator3 indicator, @NonNull TextView likecnt,
      @NonNull TextView locationbtn, @NonNull TextView title, @NonNull TextView toptitle,
      @NonNull ViewPager2 viewpager, @NonNull ImageView weather) {
    this.rootView = rootView;
    this.blogViewScrollView = blogViewScrollView;
    this.blogback = blogback;
    this.comment = comment;
    this.commentcnt = commentcnt;
    this.content = content;
    this.datebtn = datebtn;
    this.deleteblog = deleteblog;
    this.feel = feel;
    this.heart = heart;
    this.indicator = indicator;
    this.likecnt = likecnt;
    this.locationbtn = locationbtn;
    this.title = title;
    this.toptitle = toptitle;
    this.viewpager = viewpager;
    this.weather = weather;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBasicBlogViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBasicBlogViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_basic_blog_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBasicBlogViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.blogViewScrollView;
      ScrollView blogViewScrollView = ViewBindings.findChildViewById(rootView, id);
      if (blogViewScrollView == null) {
        break missingId;
      }

      id = R.id.blogback;
      ImageButton blogback = ViewBindings.findChildViewById(rootView, id);
      if (blogback == null) {
        break missingId;
      }

      id = R.id.comment;
      ImageView comment = ViewBindings.findChildViewById(rootView, id);
      if (comment == null) {
        break missingId;
      }

      id = R.id.commentcnt;
      TextView commentcnt = ViewBindings.findChildViewById(rootView, id);
      if (commentcnt == null) {
        break missingId;
      }

      id = R.id.content;
      TextView content = ViewBindings.findChildViewById(rootView, id);
      if (content == null) {
        break missingId;
      }

      id = R.id.datebtn;
      TextView datebtn = ViewBindings.findChildViewById(rootView, id);
      if (datebtn == null) {
        break missingId;
      }

      id = R.id.deleteblog;
      ImageView deleteblog = ViewBindings.findChildViewById(rootView, id);
      if (deleteblog == null) {
        break missingId;
      }

      id = R.id.feel;
      ImageView feel = ViewBindings.findChildViewById(rootView, id);
      if (feel == null) {
        break missingId;
      }

      id = R.id.heart;
      ImageView heart = ViewBindings.findChildViewById(rootView, id);
      if (heart == null) {
        break missingId;
      }

      id = R.id.indicator;
      CircleIndicator3 indicator = ViewBindings.findChildViewById(rootView, id);
      if (indicator == null) {
        break missingId;
      }

      id = R.id.likecnt;
      TextView likecnt = ViewBindings.findChildViewById(rootView, id);
      if (likecnt == null) {
        break missingId;
      }

      id = R.id.locationbtn;
      TextView locationbtn = ViewBindings.findChildViewById(rootView, id);
      if (locationbtn == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.toptitle;
      TextView toptitle = ViewBindings.findChildViewById(rootView, id);
      if (toptitle == null) {
        break missingId;
      }

      id = R.id.viewpager;
      ViewPager2 viewpager = ViewBindings.findChildViewById(rootView, id);
      if (viewpager == null) {
        break missingId;
      }

      id = R.id.weather;
      ImageView weather = ViewBindings.findChildViewById(rootView, id);
      if (weather == null) {
        break missingId;
      }

      return new ActivityBasicBlogViewBinding((LinearLayout) rootView, blogViewScrollView, blogback,
          comment, commentcnt, content, datebtn, deleteblog, feel, heart, indicator, likecnt,
          locationbtn, title, toptitle, viewpager, weather);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}