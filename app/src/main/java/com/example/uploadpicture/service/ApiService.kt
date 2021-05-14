package com.example.uploadpicture.service

import android.util.Log
import com.example.uploadpicture.retrofit.RetrofitClient
import com.example.uploadpicture.retrofit.RetrofitInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
//
//object ApiService {
//    private var retrofitInterface: RetrofitInterface
//
//    init {
//        val retrofit = RetrofitClient.instance
//        retrofitInterface = retrofit.create(RetrofitInterface::class.java)
//    }
//    var compositeDisposable = CompositeDisposable()

//    /**
//     * Fetching data
//     * */
//    fun fetchPokeMonList(listener: DataReady) {
//        compositeDisposable.add(
//            retrofitInterface.pokemonList().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    Log.i("PokemonList", "${it.results}")
//                    listener.onPokemonListSuccess(it.results)
//                }
//        )
//    }
//}