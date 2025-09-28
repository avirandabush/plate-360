package com.avirandabush.plate360.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avirandabush.plate360.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResultScreen : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.result_screen, container, false)
    }
}
