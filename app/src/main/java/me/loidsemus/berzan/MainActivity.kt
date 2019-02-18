package me.loidsemus.berzan

import ScheduleViewFragment
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.time.DayOfWeek


class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendar = Calendar.getInstance()
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt("weekDisplay", calendar.get(Calendar.WEEK_OF_YEAR)).apply()

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = mSectionsPagerAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        } else if(id == R.id.action_choose_week) {
            val options: MutableList<CharSequence> = ArrayList()
            for(i in 1..52) {
                options.add("" + i)
            }
            selector("Välj vecka", options) { _, i ->
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt("weekDisplay", options[i].toString().toInt()).commit()
                toast("Vecka ${options[i]}")
                refreshFragments()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun refreshFragments() {
        /*for(i in mSectionsPagerAdapter?.fragments?.keys) {
            (mSectionsPagerAdapter?.getItem(i) as ScheduleViewFragment).load(applicationContext)
        }*/
        mSectionsPagerAdapter?.fragments?.forEach {
            (it.value as ScheduleViewFragment).load(applicationContext)
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        var fragments: MutableMap<Int, Fragment> = HashMap()

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ScheduleViewFragment
            if(!fragments.containsKey(position)) {
                val fragment = ScheduleViewFragment.newInstance(position)
                fragments[position] = fragment
                Log.i("SIZE", "" + fragments.size)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return 5
        }
    }

}
