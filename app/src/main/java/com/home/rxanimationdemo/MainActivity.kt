package com.home.rxanimationdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.rotation
import com.mikhaellopez.rxanimation.scale
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    companion object {
        private const val ANIMATION_DURATION = 2000L
    }

    private val composite = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scale = 1f
        val rotation = 360f
        RxAnimation.sequentially(
            image_view_this_year.scale(scale, ANIMATION_DURATION),
            RxAnimation.together(
                image_view_pig1.scale(scale, ANIMATION_DURATION),
                image_view_pig1.rotation(rotation, ANIMATION_DURATION)
            )
        ).subscribe().addTo(composite)

        image_view_pig2.throttleClick()
            .switchMapCompletable {
                image_view_pig2.rotation(rotation, ANIMATION_DURATION, reverse = true)
            }
            .subscribe().addTo(composite)
    }

    private fun View.throttleClick(duration: Long = ANIMATION_DURATION): Observable<Unit> =
        clicks().throttleFirst(duration, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
}
