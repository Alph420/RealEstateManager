package com.openclassrooms.realestatemanager.activity

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Julien Jennequin on 17/12/2021 13:49
 * Project : RealEstateManager
 **/
open class BaseActivity : AppCompatActivity() {
    val disposeBag = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }
}