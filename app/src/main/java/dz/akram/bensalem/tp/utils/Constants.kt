package dz.akram.bensalem.tp.utils

import dz.akram.bensalem.tp.BuildConfig

object Constants {

    const val SONG_TABLE_NAME = "song_table"
    const val DATABASE_NAME = "song_db"

    const val CHANNEL_ID = "music_id"
    const val NOTIFICATION_ID = 1
    const val FAVOURITE_NOTIFICATION_ID = 2


    const val ACTION_MUSIC_CLICKED = "${BuildConfig.APPLICATION_ID}.action.MUSIC_CLICKED"
    const val ACTION_SOME_INTENT = "${BuildConfig.APPLICATION_ID}.some_intent"


    // Action intents handled by this service
    const val ACTION_TOGGLE_PLAYBACK = "${BuildConfig.APPLICATION_ID}.action.TOGGLE_PLAYBACK"
    const val ACTION_PLAY = "${BuildConfig.APPLICATION_ID}.action.PLAY"
    const val ACTION_PAUSE = "${BuildConfig.APPLICATION_ID}.action.PAUSE"

    const val ACTION_SKIP_NEXT = "${BuildConfig.APPLICATION_ID}.action.SKIP_NEXT"
    const val ACTION_SKIP_PREVIOUS = "${BuildConfig.APPLICATION_ID}.action.SKIP_PREVIOUS"

    const val ACTION_STOP = "${BuildConfig.APPLICATION_ID}.action.STOP"
    const val ACTION_SKIP = "${BuildConfig.APPLICATION_ID}.action.SKIP"
    const val ACTION_REWIND = "${BuildConfig.APPLICATION_ID}.action.REWIND"



    const val ACTION_UI_PLAY = "${BuildConfig.APPLICATION_ID}.action.ui.PLAY"
    const val ACTION_UI_PAUSE = "${BuildConfig.APPLICATION_ID}.action.ui.PAUSE"
    const val ACTION_UI_SKIP_NEXT = "${BuildConfig.APPLICATION_ID}.action.ui.SKIP_NEXT"
    const val ACTION_UI_SKIP_PREVIOUS = "${BuildConfig.APPLICATION_ID}.action.ui.SKIP_PREVIOUS"


    const val ACTION_TOGGLE_PLAY_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.notification.PLAY_NOTIFICATION"
    const val ACTION_PLAY_NEXT_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.notification.PLAY_NEXT_NOTIFICATION"
    const val ACTION_PLAY_PREVIOUS_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.notification.PLAY_PREVIOUS_NOTIFICATION"




    const val ACTION_FAVOURITE_MUSIC_CLICKED = "${BuildConfig.APPLICATION_ID}.action.favourite.MUSIC_CLICKED"

    // Action intents handled by this service
    const val ACTION_FAVOURITE_TOGGLE_PLAYBACK = "${BuildConfig.APPLICATION_ID}.action.favourite.TOGGLE_PLAYBACK"
    const val ACTION_FAVOURITE_PLAY = "${BuildConfig.APPLICATION_ID}.action.favourite.PLAY"
    const val ACTION_FAVOURITE_PAUSE = "${BuildConfig.APPLICATION_ID}.action.favourite.PAUSE"

    const val ACTION_FAVOURITE_SKIP_NEXT = "${BuildConfig.APPLICATION_ID}.action.favourite.SKIP_NEXT"
    const val ACTION_FAVOURITE_SKIP_PREVIOUS = "${BuildConfig.APPLICATION_ID}.action.favourite.SKIP_PREVIOUS"

    const val ACTION_FAVOURITE_STOP = "${BuildConfig.APPLICATION_ID}.action.favourite.STOP"
    const val ACTION_FAVOURITE_SKIP = "${BuildConfig.APPLICATION_ID}.action.favourite.SKIP"
    const val ACTION_FAVOURITE_REWIND = "${BuildConfig.APPLICATION_ID}.action.favourite.REWIND"



    const val ACTION_FAVOURITE_UI_PLAY = "${BuildConfig.APPLICATION_ID}.action.favourite.ui.PLAY"
    const val ACTION_FAVOURITE_UI_PAUSE = "${BuildConfig.APPLICATION_ID}.action.favourite.ui.PAUSE"
    const val ACTION_FAVOURITE_UI_SKIP_NEXT = "${BuildConfig.APPLICATION_ID}.action.favourite.ui.SKIP_NEXT"
    const val ACTION_FAVOURITE_UI_SKIP_PREVIOUS = "${BuildConfig.APPLICATION_ID}.action.favourite.ui.SKIP_PREVIOUS"


    const val ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.favourite.notification.PLAY_NOTIFICATION"
    const val ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.favourite.notification.PLAY_NEXT_NOTIFICATION"
    const val ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.action.favourite.notification.PLAY_PREVIOUS_NOTIFICATION"







    // Extra to force the playing
    const val EXTRA_DATA = "extra_data"
    const val EXTRA_SONG = "extra_song"
}