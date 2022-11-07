package com.isaacdelosreyes.googletvleanback.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.isaacdelosreyes.googletvleanback.ui.catalog.utils.CategoriesEnum
import java.io.Serializable

fun ImageView.loadUrl(imageUrl: String?) {
    Glide.with(this).load(imageUrl).into(this)
}

fun CategoriesEnum.getName() = when (this) {
    CategoriesEnum.POPULAR -> "Popular"
    CategoriesEnum.LEAST_VOTED -> "Menos votadas"
    CategoriesEnum.MOST_VOTED -> "Más votadas"
    CategoriesEnum.REVENUE -> "Top ingresos"
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

/**
 * Usamos la librería de Glide para convertir la imagen que nos llega de la api como url en
 * un drawable para poder asignarlo a nuestro fondo.
 */

fun Context?.loadGlideImageDrawable(imageUrl: String, doSomething: (Drawable) -> Unit) {
    this?.let {
        Glide.with(it).asDrawable().load(imageUrl)
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                doSomething(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //no-op
            }
        })
    }
}

/**
 * Usamos la librería de Glide para convertir la imagen que nos llega de la api como url en
 * un bitmap para poder asignarlo a nuestro fondo.
 */

fun Context?.loadGlideImageBitmap(imageUrl: String, doSomething: (Bitmap) -> Unit) {
    this?.let {
        Glide.with(it).asBitmap().load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                doSomething(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //no-op
            }
        })
    }
}