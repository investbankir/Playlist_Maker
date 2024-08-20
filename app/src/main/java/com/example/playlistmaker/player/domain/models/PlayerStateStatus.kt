package com.example.playlistmaker.player.domain.models

sealed class PlayerStateStatus(val isPlayButtonEnabled: Boolean, val buttonText: String, val progress: String) {
   class STATE_DEFAULT : PlayerStateStatus(false, "PLAY", "00:00")
    class STATE_PREPARED : PlayerStateStatus(true, "PLAY", "00:00")
    class STATE_PLAYING(progress: String) : PlayerStateStatus(true, "PAUSE", progress)
    class STATE_PAUSED(progress: String) : PlayerStateStatus(true, "PLAY", progress)
}
