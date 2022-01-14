package com.openclassrooms.realestatemanager.utils

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 14/01/2022 13:24
 * Project : RealEstateManager
 **/
class TestNetworkSchedulers : NetworkSchedulers {
    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val main: Scheduler
        get() = Schedulers.trampoline()
}
