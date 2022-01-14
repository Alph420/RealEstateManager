package com.openclassrooms.realestatemanager.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 14/01/2022 13:22
 * Project : RealEstateManager
 **/
interface MainSchedulerProvider {
    val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}

interface IOSchedulerProvider {
    val io: Scheduler
        get() = Schedulers.io()
}

interface NetworkSchedulers : MainSchedulerProvider, IOSchedulerProvider
class NetworkSchedulersImpl : NetworkSchedulers
