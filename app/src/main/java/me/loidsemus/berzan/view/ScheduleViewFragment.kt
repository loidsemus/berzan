import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import me.loidsemus.berzan.R
import me.loidsemus.berzan.SettingsActivity
import me.loidsemus.berzan.util.URLBuilder

class ScheduleViewFragment : Fragment() {

    var rootView: View? = null

    @SuppressLint("ApplySharedPref")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false)

        // Show and populate class choose thing if not already chosen
        if (!PreferenceManager.getDefaultSharedPreferences(context).contains("schoolClass")) {

            rootView?.scheduleView?.visibility = View.GONE
            rootView?.classChooseLayout?.visibility = View.VISIBLE

            rootView?.chooseButton?.setOnClickListener {
                startActivity(Intent(context, SettingsActivity::class.java))
            }
        } else {
            load(context!!)
        }

        return rootView
    }

    fun load(context: Context) {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        rootView?.classChooseLayout?.visibility = View.GONE
        rootView?.scheduleView?.visibility = View.VISIBLE

        val url = URLBuilder(PreferenceManager.getDefaultSharedPreferences(context).getInt("weekDisplay", 1), when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            0 -> 1
            1 -> 2
            2 -> 4
            3 -> 8
            4 -> 16
            else -> 0
        }, PreferenceManager.getDefaultSharedPreferences(context).getString("schoolClass", "null"),
                URLBuilder.Resolution((metrics.widthPixels / 2.0).toInt(), (metrics.heightPixels / 2.2).toInt()))

        Log.i("URL", Uri.parse(url.toString()).toString())

        Picasso.get()
                .load(Uri.parse(url.toString()))
                .placeholder(R.drawable.ic_loading_image)
                .error(R.drawable.ic_loading_error)
                .fit()
                .into(rootView?.scheduleView)
    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): ScheduleViewFragment {
            val fragment = ScheduleViewFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}