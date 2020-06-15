# ViewMoreTextView
Show and hide long text in TextView with click

[![Release](https://jitpack.io/v/csguys/ViewMoreTextView.svg)](https://jitpack.io/#csguys/ViewMoreTextView)

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add the dependency:
```gradle
dependencies {
  implementation 'com.github.csguys:ViewMoreTextView:v1.1'
}
```


## Demo
<img src="https://github.com/csguys/ViewMoreTextView/blob/master/sample.gif" width="40%">


## Usages
```
 <com.csguys.viewmoretextview.ViewMoreTextView
            android:id="@+id/tv1"
            android:textColor="@android:color/black"
            android:layout_marginTop="20dp"
            app:tv_click_color="@color/colorAccent"
            app:tv_max_char="100"
            app:tv_click_underline="true"
            app:tv_expand_text="@string/text_continue"
            app:tv_collapse_text="@string/text_close"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
```
To programmatically set text use method
```
setCharText(CharSequence)

setCharText(String resource id)

```

### Customization

Table below describes the properties available to customize the ViewMoreTextView.


| Property Name          | Format    | Description |
|------------------------|-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| tv_click_color         | color     | Define color for click text which expand and collapse tex |
| tv_max_char            | integer   | Max char count to show , after which text is collapsed |
| tv_click_underline     | boolean   | weather to show underline below click text or not true to show false to hide |
| tv_expand_text         | String Or String resource    | Text to show for expanding |
| tv_collapse_text       | String Or String resource    | Text to show for collapse |
