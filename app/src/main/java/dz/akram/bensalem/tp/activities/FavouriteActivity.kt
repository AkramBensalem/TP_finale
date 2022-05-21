package dz.akram.bensalem.tp.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import dz.akram.bensalem.tp.R
import dz.akram.bensalem.tp.adapters.CustomAdapter
import dz.akram.bensalem.tp.viewmodels.favourites.FavouriteViewModel
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dz.akram.bensalem.tp.ApplicationSong
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.service.FavouriteNotificationReceiver
import dz.akram.bensalem.tp.service.NotificationReceiver
import dz.akram.bensalem.tp.utils.Constants
import dz.akram.bensalem.tp.viewmodels.favourites.FactorViewModelFactory
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {

    private var currentSong: Song? = null

    private val mViewModel: FavouriteViewModel by viewModels {
        FactorViewModelFactory((application as ApplicationSong).favouriteRepository)
    }// add layer of abstraction to provide data to the view

    private val mBottomSheetLayout : LinearLayout by lazy {  findViewById(R.id.favourite_bottom_sheet_layout) }
    private lateinit var sheetBehavior : BottomSheetBehavior<LinearLayout>

    private val recyclerView : RecyclerView by lazy { findViewById(R.id.favourite_recyclerView) }
    private val imagePlaceHolder : ImageView by lazy { findViewById(R.id.favourite_iv_no_favourites_song) }
    private val adapter : CustomAdapter by lazy { CustomAdapter(mViewModel.favouriteList.value){ song, position ->
            // open music bottom sheet
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            if (position != mViewModel.currentSongPosition.value) {
                mViewModel.updateCurrentSongPosition(position)
                sendBroadcastReceiver(Constants.ACTION_FAVOURITE_MUSIC_CLICKED, song)
            }
        }
        }
     }


    private val notificationReceiver = FavouriteNotificationReceiver()


    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                Constants.ACTION_FAVOURITE_UI_PLAY -> {

                    Toast.makeText(this@FavouriteActivity, "play", Toast.LENGTH_SHORT).show()

                    playMusic()
                }
                Constants.ACTION_FAVOURITE_UI_PAUSE -> {
                    pauseMusic()
                    Toast.makeText(this@FavouriteActivity, "pause", Toast.LENGTH_SHORT).show()
                }

                Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION -> sendBroadcastReceiver(Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION, mViewModel.nextSong())
                Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION ->  sendBroadcastReceiver(Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION, mViewModel.currentSong())
                Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION -> sendBroadcastReceiver(Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION, mViewModel.previousSong())
            }

        }
    }



    private lateinit var playPauseIcon: ImageButton
    private lateinit var skipNextIcon: ImageButton
    private lateinit var skipPreviousIcon: ImageButton
    private lateinit var titleSong: TextView

    private lateinit var removeFromFavouriteIcon: ImageButton
    private lateinit var closePlayBottomSheetIcon: ImageButton


    private var intentFilter = IntentFilter()
    private var uiIntentFilter = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        currentSong = mViewModel.currentSong()

        intentFilter.apply {
            addAction(Constants.ACTION_FAVOURITE_MUSIC_CLICKED)
            addAction(Constants.ACTION_FAVOURITE_SKIP_NEXT)
            addAction(Constants.ACTION_FAVOURITE_SKIP_PREVIOUS)
            addAction(Constants.ACTION_FAVOURITE_TOGGLE_PLAYBACK)
        }

        uiIntentFilter.apply {
            addAction(Constants.ACTION_FAVOURITE_UI_PLAY)
            addAction(Constants.ACTION_FAVOURITE_UI_PAUSE)
            addAction(Constants.ACTION_FAVOURITE_UI_SKIP_PREVIOUS)
            addAction(Constants.ACTION_FAVOURITE_SKIP_NEXT)

            addAction(Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION)
            addAction(Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION)
            addAction(Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION)
        }


        setupRecyclerView()

        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)



        handleUI()
        handleBottomSheetUI()

        playPauseIcon = findViewById(R.id.favourite_play_pause)
        skipNextIcon = findViewById(R.id.favourite_skip_next)
        skipPreviousIcon = findViewById(R.id.favourite_skip_previous)
        titleSong = findViewById(R.id.favourite_title_song)
        removeFromFavouriteIcon = findViewById(R.id.favourite_remove_favourite_button)
        removeFromFavouriteIcon.setImageResource(R.drawable.ic_baseline_delete_forever_24)
        closePlayBottomSheetIcon = findViewById(R.id.favourite_close_play_bottom_sheet_button)

        playPauseIcon.setOnClickListener {
            sendBroadcastReceiver(Constants.ACTION_FAVOURITE_TOGGLE_PLAYBACK, mViewModel.currentSong())
        }

        skipNextIcon.setOnClickListener {
            sendBroadcastReceiver(Constants.ACTION_FAVOURITE_SKIP_NEXT, mViewModel.nextSong())
        }

        skipPreviousIcon.setOnClickListener {
            sendBroadcastReceiver(Constants.ACTION_FAVOURITE_SKIP_PREVIOUS, mViewModel.previousSong())
        }

        removeFromFavouriteIcon.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            currentSong?.let {
                    song -> mViewModel.deleteSong(song)
                sendBroadcastReceiver(Constants.ACTION_FAVOURITE_STOP, mViewModel.previousSong())
                Toast.makeText(this, "removed from favourite", Toast.LENGTH_SHORT).show()
            }

        }

        closePlayBottomSheetIcon.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    // setup recyclerView
    private fun setupRecyclerView() {
       recyclerView.adapter = adapter
       recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    }


    private fun handleUI() {
        lifecycleScope.launch {
            mViewModel.favouriteList.collect { value ->
                Log.d("FavouriteActivityAkram", "list change: $value")

                 adapter.setData(value)

                if (value.isEmpty()) {
                    imagePlaceHolder.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    imagePlaceHolder.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun handleBottomSheetUI(){
        lifecycleScope.launch {
            mViewModel.currentSongPosition.collect { value ->
                value?.let {

                    currentSong = mViewModel.getSongByPosition(value)

                    if (value == 0) {
                        skipPreviousIcon.visibility = View.INVISIBLE
                    } else {
                        skipPreviousIcon.visibility = View.VISIBLE
                    }

                    if (value == mViewModel.favouriteList.value.size.minus(1)) {
                        skipNextIcon.visibility = View.INVISIBLE
                    }else {
                        skipNextIcon.visibility = View.VISIBLE
                    }


                    titleSong.text = currentSong?.name
                }

            }
        }
    }

    // play music
    private fun playMusic() {
        playPauseIcon.setImageResource(R.drawable.ic_round_pause_circle_outline_24)
    }

    // pause music
    private fun pauseMusic() {
        playPauseIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    private fun sendBroadcastReceiver(
        action: String,
        song : Song?
    ) {
        val intent = Intent(action)
        if (song != null) {
            intent.putExtra(Constants.EXTRA_SONG, song)
        }
        sendBroadcast(intent)
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(notificationReceiver, intentFilter)
        registerReceiver(mReceiver, uiIntentFilter)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
        unregisterReceiver(mReceiver)
    }



}