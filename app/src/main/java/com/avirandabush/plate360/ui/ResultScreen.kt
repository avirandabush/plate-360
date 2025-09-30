package com.avirandabush.plate360.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.avirandabush.plate360.R
import com.avirandabush.plate360.model.VehicleHistory
import com.avirandabush.plate360.model.VehicleRecord
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

class ResultScreen : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_RECORD = "arg_record"
        private const val ARG_HISTORY = "arg_history"

        fun newInstance(record: VehicleRecord, history: VehicleHistory?): ResultScreen {
            val fragment = ResultScreen()
            val args = Bundle().apply {
                putString(ARG_RECORD, Gson().toJson(record))
                history?.let { putString(ARG_HISTORY, Gson().toJson(it)) }
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.result_screen, container, false)

        val recordJson = arguments?.getString(ARG_RECORD)
        val historyJson = arguments?.getString(ARG_HISTORY)

        val record = recordJson?.let { Gson().fromJson(it, VehicleRecord::class.java) }
        val history = historyJson?.let { Gson().fromJson(it, VehicleHistory::class.java) }

        val tableVehicle = view.findViewById<TableLayout>(R.id.tableVehicle)
        val tableHistory = view.findViewById<TableLayout>(R.id.tableHistory)

        record?.let { r ->
            addRow(tableVehicle, "מספר רכב", r.mispar_rechev)
            addRow(tableVehicle, "קוד יצרן", r.tozeret_cd)
            addRow(tableVehicle, "יצרן", r.tozeret_nm)
            addRow(tableVehicle, "סוג דגם (שם)", r.sug_degem_nm)
            addRow(tableVehicle, "סוג דגם (קוד)", r.sug_degem)
            addRow(tableVehicle, "קוד דגם", r.degem_cd)
            addRow(tableVehicle, "שם דגם", r.degem_nm)
            addRow(tableVehicle, "רמת גימור", r.ramat_gimur)
            addRow(tableVehicle, "רמת אבזור בטיחותי", r.ramat_eivzur_betihuty)
            addRow(tableVehicle, "קבוצת זיהום", r.kvutzat_zihum)
            addRow(tableVehicle, "שנת ייצור", r.shnat_yitzur)
            addRow(tableVehicle, "דגם מנוע", r.degem_manoa)
            addRow(tableVehicle, "מבחן אחרון", r.mivchan_acharon_dt)
            addRow(tableVehicle, "תוקף רישוי", r.tokef_dt)
            addRow(tableVehicle, "בעלות", r.baalut)
            addRow(tableVehicle, "מספר שילדה", r.misgeret)
            addRow(tableVehicle, "קוד צבע", r.tzeva_cd)
            addRow(tableVehicle, "צבע רכב", r.tzeva_rechev)
            addRow(tableVehicle, "צמיג קדמי", r.zmig_kidmi)
            addRow(tableVehicle, "צמיג אחורי", r.zmig_ahori)
            addRow(tableVehicle, "סוג דלק", r.sug_delek_nm)
            addRow(tableVehicle, "הוראת רישום", r.horaat_rishum)
            addRow(tableVehicle, "מועד עלייה לכביש", r.moed_aliya_lakvish)
            addRow(tableVehicle, "כינוי מסחרי", r.kinuy_mishari)
        }

        history?.let { h ->
            addRow(tableHistory, "מספר רכב", h.mispar_rechev?.toString())
            addRow(tableHistory, "מספר מנוע", h.mispar_manoa)
            addRow(tableHistory, "ק״מ אחרון", h.kilometer_test_aharon?.toString())
            addRow(tableHistory, "שינוי מבנה", h.shinui_mivne_ind?.toString())
            addRow(tableHistory, "גפ״ם (גז)", h.gapam_ind?.toString())
            addRow(tableHistory, "שינוי צבע", h.shnui_zeva_ind?.toString())
            addRow(tableHistory, "שינוי צמיג", h.shinui_zmig_ind?.toString())
            addRow(tableHistory, "רישום ראשון", h.rishum_rishon_dt)
            addRow(tableHistory, "מקוריות", h.mkoriut_nm)
        }

        return view
    }

    private fun addRow(table: TableLayout, label: String, value: String?) {
        val row = TableRow(requireContext()).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        }

        val tvLabel = TextView(requireContext()).apply {
            text = label
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.RIGHT
            setPadding(8, 8, 16, 8)
        }

        val tvValue = TextView(requireContext()).apply {
            text = value ?: "-"
            textSize = 16f
            gravity = Gravity.RIGHT
            setPadding(16, 8, 8, 8)
        }

        row.addView(tvValue)
        row.addView(tvLabel)
        table.addView(row)
    }

}
