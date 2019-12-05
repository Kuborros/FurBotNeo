package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerDescriptor;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.PersistentHttpStream;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DelegatedAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.InternalAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;


class InvidiousAudioTrack extends DelegatedAudioTrack {
    private static final Logger log = LoggerFactory.getLogger(InvidiousAudioTrack.class);

    private final MediaContainerDescriptor containerTrackFactory;
    private final InvidiousAudioSourceManager sourceManager;

    InvidiousAudioTrack(AudioTrackInfo trackInfo, MediaContainerDescriptor containerTrackFactory, InvidiousAudioSourceManager sourceManager) {
        super(trackInfo);

        this.containerTrackFactory = containerTrackFactory;
        this.sourceManager = sourceManager;
    }

    MediaContainerDescriptor getContainerTrackFactory() {
        return containerTrackFactory;
    }

    @Override
    public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
        try (HttpInterface httpInterface = sourceManager.getHttpInterface()) {
            log.debug("Starting youtube track from URL: {}", trackInfo.identifier);

            try (PersistentHttpStream inputStream = new PersistentHttpStream(httpInterface, new URI(trackInfo.identifier), Long.MAX_VALUE)) {
                processDelegate((InternalAudioTrack) containerTrackFactory.createTrack(trackInfo, inputStream), localExecutor);
            }
        }
    }

    @Override
    protected AudioTrack makeShallowClone() {
        return new InvidiousAudioTrack(trackInfo, containerTrackFactory, sourceManager);
    }

    @Override
    public AudioSourceManager getSourceManager() {
        return sourceManager;
    }
}