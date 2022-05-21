package dz.akram.bensalem.tp.activities

import android.content.*
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dz.akram.bensalem.tp.ApplicationSong
import dz.akram.bensalem.tp.R
import dz.akram.bensalem.tp.adapters.CustomAdapter
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.service.NotificationReceiver
import dz.akram.bensalem.tp.utils.Constants
import dz.akram.bensalem.tp.utils.Constants.ACTION_MUSIC_CLICKED
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_NEXT_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_PREVIOUS_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.ACTION_SOME_INTENT
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAYBACK
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAY_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_PLAY
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.EXTRA_SONG
import dz.akram.bensalem.tp.utils.PermissionUtils
import dz.akram.bensalem.tp.utils.SensorUtils
import dz.akram.bensalem.tp.viewmodels.main.MainViewModel
import dz.akram.bensalem.tp.viewmodels.main.MainViewModelFactory
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    companion object {
        const val MEDIA_LOCATION_PERMISSION_REQUEST_CODE = 3
        const val PERMISSION_READ_EXTERNAL_STORAGE = 5
    }

    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val mViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as ApplicationSong).localStorageRepository, (application as ApplicationSong).favouriteRepository)
    }

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.song_recyclerView) }
    private val imagePlaceHolder: ImageView by lazy { findViewById(R.id.iv_no_favourites_song) }
    private val mBottomSheetLayout: LinearLayout by lazy { findViewById(R.id.bottom_sheet_layout) }
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    private val adapter: CustomAdapter by lazy {
        CustomAdapter(mViewModel.songList.value) { song, position ->

            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                if (position != mViewModel.currentSongPosition.value) {
                    mViewModel.updateCurrentSongPosition(position)
                    sendBroadcastReceiver(ACTION_MUSIC_CLICKED, song)
                }
            }

        }
    }



    private lateinit var playPauseIcon: ImageButton
    private lateinit var skipNextIcon: ImageButton
    private lateinit var skipPreviousIcon: ImageButton
    private lateinit var titleSong: TextView

    private lateinit var addToFavouriteIcon: ImageButton
    private lateinit var closePlayBottomSheetIcon: ImageButton


    private var intentFilter = IntentFilter(ACTION_SOME_INTENT)
    private var uiIntentFilter = IntentFilter()



    private val notificationReceiver = NotificationReceiver()


    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                ACTION_UI_PLAY -> {
                    playMusic()
                }
                ACTION_UI_PAUSE -> {
                    pauseMusic()
                }

                ACTION_PLAY_NEXT_NOTIFICATION -> sendBroadcastReceiver(ACTION_SKIP_NEXT, mViewModel.nextSong())
                ACTION_TOGGLE_PLAY_NOTIFICATION ->  sendBroadcastReceiver(ACTION_TOGGLE_PLAYBACK, mViewModel.currentSong())
                ACTION_PLAY_PREVIOUS_NOTIFICATION -> sendBroadcastReceiver(ACTION_SKIP_PREVIOUS, mViewModel.previousSong())

            }

        }
    }

    private var currentSong: Song? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.checkPermissions(this)

        currentSong = mViewModel.currentSong()
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        SensorUtils.detectCheckPhone(
            sensorManager = sensorManager,
            acceleration  = acceleration,
            currentAcceleration = currentAcceleration,
            lastAcceleration = lastAcceleration,
            onChecking = {
            if (it) sendBroadcastReceiver(ACTION_TOGGLE_PLAYBACK, mViewModel.currentSong())
            },
        onCurrentAccelerationChanged = {currentAcceleration = it},
        onLastAccelerationChanged= {lastAcceleration = it},
        onAccelerationChanged = {acceleration = it}
        )


        intentFilter.apply {
            addAction(ACTION_MUSIC_CLICKED)
            addAction(ACTION_SKIP_NEXT)
            addAction(ACTION_SKIP_PREVIOUS)
            addAction(ACTION_TOGGLE_PLAYBACK)
        }

        uiIntentFilter.apply {
            addAction(ACTION_UI_PLAY)
            addAction(ACTION_UI_PAUSE)
            addAction(ACTION_UI_SKIP_PREVIOUS)
            addAction(ACTION_SKIP_NEXT)

            addAction(ACTION_PLAY_NEXT_NOTIFICATION)
            addAction(ACTION_TOGGLE_PLAY_NOTIFICATION)
            addAction(ACTION_PLAY_PREVIOUS_NOTIFICATION)
        }


        setupRecyclerView()

        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)



        handleUI()
        handleBottomSheetUI()

        playPauseIcon = findViewById(R.id.play_pause)
        skipNextIcon = findViewById(R.id.skip_next)
        skipPreviousIcon = findViewById(R.id.skip_previous)
        titleSong = findViewById(R.id.title_song)
        addToFavouriteIcon = findViewById(R.id.add_favourite_button)
        addToFavouriteIcon.setImageResource(R.drawable.ic_round_favorite_border_24)

        closePlayBottomSheetIcon = findViewById(R.id.close_play_bottom_sheet_button)

        playPauseIcon.setOnClickListener {
            sendBroadcastReceiver(ACTION_TOGGLE_PLAYBACK, mViewModel.currentSong())
        }

        skipNextIcon.setOnClickListener {
            sendBroadcastReceiver(ACTION_SKIP_NEXT, mViewModel.nextSong())
        }

        skipPreviousIcon.setOnClickListener {
            sendBroadcastReceiver(ACTION_SKIP_PREVIOUS, mViewModel.previousSong())
        }

        addToFavouriteIcon.setOnClickListener {
            currentSong?.let {
                    song -> mViewModel.addToFavourite(song)
                Toast.makeText(this, "Added to favourite", Toast.LENGTH_SHORT).show()
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
            mViewModel.songList.collect { value ->
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

                    if (value == mViewModel.songList.value.size.minus(1)) {
                        skipNextIcon.visibility = View.INVISIBLE
                    }else {
                        skipNextIcon.visibility = View.VISIBLE
                    }


                    titleSong.text = currentSong?.name
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
            intent.putExtra(EXTRA_SONG, song)
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